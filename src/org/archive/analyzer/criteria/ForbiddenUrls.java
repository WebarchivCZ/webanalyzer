/*
 * ForbiddenUrls.java
 *
 * Created on August 10, 2007, 6:52 PM
 *
 * Trieda bude mat podobnu funkcionalitu ako Urls. Lisit sa bude tym, ze
 * bude v subore so zakazanymi odkazmi (URL), vyhladavat dany odkaz a ak sa
 * v tychto zakazanych odkazoch najde tak sa analyzovanej stranke odcitaju
 * body. Zakazane odkazy(URL) budu pravdepodobne sex, erotic a porno stranky.
 * Vyhladavanie v tomto subore bude pomocou LUCENE API.
 *
 */

package org.archive.analyzer.criteria;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.archive.analyzer.*;
import org.archive.analyzer.lucene.URLFormater;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Hits;

/**
 *
 * @author praso
 */
public class ForbiddenUrls {
    
    private static final Logger log4j = Logger.getLogger(ForbiddenUrls.class);
    
    /* Jedina instancia Urls. */
    private static final ForbiddenUrls INSTANCE = new ForbiddenUrls();
    
    /* Konstanta, ktora sa odcita od bodov analyzovanej stranky. */
    private static final int FORBIDEN_URLS_VALUE = -1;
    
    /* Odkaz na jedinacika Counter, pouzijem jeho metodu decrement. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Staticke atributy z LUCENE, nainicializuju sa v inicializeLuceneIndex. */
    private static QueryParser parser;
    private static Searcher searcher;
    private static IndexReader indexReader;
    
    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych url. */
    private int numberOfValidFoundedObjects;

    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu triedy
     * ForbiddenUrls.
     */
    public static ForbiddenUrls getInstance() {
        return INSTANCE;
    }
    
    /**
     * Inicializacna metoda, ktora nainicializuje IndexReader podla predaneho
     * parametru partOfIndexPath. Paramtere bude mat jednoduche hodnotu retazec,
     * napr: "cz", "sk", "en" atd. Tento paramtere sa pouzije na utvorenie
     * vyslednej cesty k tym spravnym indexom pre danu triedu. Zbytok cesty je
     * nastaveny defaultne napr. "_anal/lucene_index/ + param + /urls_index".
     *
     * @param partOfIndexPath   part of path to index files.
     */
    public static void inicializeLuceneIndex(String partOfIndexPath) {
        log4j.debug("inicializeLuceneIndex partOfIndexPath=" + partOfIndexPath);
        // Contents, asi znamena prehladavanie obsahu
        String field = "contents";
        String path = "_anal/lucene_index/" + partOfIndexPath +
                "/forbiden_urls_index";
        
        try {
            indexReader = IndexReader.open(path);
            searcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new StandardAnalyzer();
            parser = new QueryParser(field, analyzer);
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("inicializeLuceneIndex error indexDir="
                    + partOfIndexPath);
        }
    }
    
    /**
     * Staticka metoda, ktora zavrie indexSearcher z Lucene.
     */
    public static void closeLuceneIndexSearcher() {
        try {
            indexReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log4j.error("closeLuceneIndexSearcher caused by " + ioe.getCause());
        }
    }
    
    /**
     * Sukromny konstruktor.
     */
    private ForbiddenUrls() {
    }
    
    /**
     * Metoda, ktora bude v predanom liste odkazov URL (objekt String), hladat
     * odkazy zo zakazanych straniek (porno, sex, ...). Ak najde odkaz, ktory
     * sa nachadza v subore s odkazmi zakazanych straniek, tak pricita
     * analyzovanej stranke body.
     * Pouziva atributy nainicializovane statickou metodou
     * inicializeLuceneIndex.
     *
     * @params links   seznam odazovn najdenych na analyzovanej stranke
     */
    public void search(List links) {
        log4j.debug("FORBIDDEN_URLS.search links=zatim vypnuto");
        if (links.isEmpty()) {
            // Zalogujem ze je prazdny a odidem z metody.
            log4j.warn("List of founded links on analyzed page is empty.");
            return;
        }
        
        try {
            // Prehladavam v odkazoch pomocou nainicializovaneho indexu.
            String link = null;
            Query query;
            Hits hits;
            for (Iterator i = links.iterator(); i.hasNext();) {
                // Preformatujem link do podoby v ktorej su linky zakaznych str.
                link = URLFormater.formatURLforLucene((String) i.next());
                // teraz tento link zacnem vyhladavat v indexe vytvorenom LUCENE
                query = parser.parse(link);
                hits = searcher.search(query);
                numberOfAllAnalyzedObjects++;
                if (hits.length() > 0) {
                    COUNTER.decrementByParam(FORBIDEN_URLS_VALUE);
                    numberOfValidFoundedObjects++;
                    
                    if (sout)
                        System.out.println("ForbiddenUrls : " + link);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("search in links", e.getCause());
        }
        log4j.debug("FORBIDDEN_URL.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize
     * forbiddenUrl pre dany krajinu.
     *
     * @return      precento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "FORBIDDEN URL NOT FOUND !";
        }
        float percent;
        percent = numberOfValidFoundedObjects*100 / numberOfAllAnalyzedObjects;
        Float percentFloat = new Float(percent);
        return percentFloat.toString();
    }
    
    /**
     * Nastavuje vypisovanie najdenych objektov na vystup.
     *
     * @param sout      vypisat na standard output ?
     */
    public void setSout(boolean sout) {
        this.sout = sout;
    }
    
}
