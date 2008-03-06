/*
 * Urls.java
 *
 * Created on August 8, 2007, 12:20 AM
 *
 * Trieda, ktora bude sluzit na hladanie dolezitych odkazov z portalov a pod.
 * Odkaz z analyzovanej stranky sa preda tejto triede a ta sa ho pokusi najst
 * v subore s odkazmi z portalov. Ak ho najde tak pripocita body Counteru.
 * Vyhladavanie pouziva projekt LUCENE. Pred hladanim je nevyhnutne triedu
 * nainicilizovat inicializacnou metodou, tak aby vedela, kde je ulozeny index,
 * podla ktoreho ma vyhladavat. Index sa vytvori v inicializcnej metode a ulozi
 * sa do urciteho adresara (defaultne "index"). Pri vytvoreni indexu musime
 * do home projektu vlozit adresar so subormi(s odkazmi z portalov) a cestu k
 * tomuto adresaru musime predat inicilizacnej metode.
 */

package org.archive.analyzer.criteria;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.archive.analyzer.*;
import org.archive.analyzer.lucene.URLFormater;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 *
 * @author praso
 */
public class Urls {
    
    private static final Logger log4j = Logger.getLogger(Urls.class);
    
    /* Jedina instancia Urls. */
    private static final Urls INSTANCE = new Urls();
    
    /* Konstanta, ktora sa pricita k bodom stranky, ak sa najde Url. */
    private static final int URL_VALUE = 1;
    
    /* Odkaz na jedinacika Counter, pouzijem jeho metodu inkrement. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Staticke atributy z LUCENE, nainicializuju sa v inicializeLuceneIndex. */
    private static QueryParser parser;
    private static Searcher searcher;
    private static IndexReader indexReader;
    
    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych objektov. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu triedy
     * Urls.
     *
     * @return urls   jedina instancia tejto triedy.
     */
    public static Urls getInstance() {
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
        String path = "_anal/lucene_index/" + partOfIndexPath + "/urls_index";
        
        try {
            indexReader = IndexReader.open(path);
            searcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new StandardAnalyzer();
            parser = new QueryParser(field, analyzer);
        } catch (Exception e) {
            log4j.error("inicializeLuceneIndex error indexDir="
                    + partOfIndexPath);
            e.printStackTrace();
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
            log4j.error("closeLuceneIndexSearcher caused by ", ioe.getCause());
        }
    }
    
    /**
     * Konstruktor, ktory by mohol nainicializovat vytvaranie indexov.
     */
    private Urls() {
    }
    
    /**
     * Metoda, ktora bude v predanom liste odkazov URL (objekt String), hladat
     * odkazy z portalov (seznam.cz atd...). Ak najde odkaz, ktory sa nachadza
     * v subore s odkazmi z portalov, tak pricita analyzovanej stranke body.
     * Pouziva atributy nainicializovane statickou metodou
     * inicializeLuceneIndex.
     *
     * @param links   seznam odkazov najdenych na analyzovanej stranke
     */
    public void search(List links) {
        log4j.debug("URLS.search links=links");
        if (links.isEmpty()) {
            // Zalogujem ze je prazdny a odidem z metody.
            log4j.warn("List of founded links on analyzed page is empty.");
            return;
        }
        String link = null;
        try {
            // Prehladavam v odkazoch pomocou nainicializovaneho indexu.
            
            Query query;
            Hits hits;
            for (Iterator i = links.iterator(); i.hasNext();) {
                numberOfAllAnalyzedObjects++;
                // Preformatujem link do podoby v ktorej su linky z portalov.
                link = URLFormater.formatURLforLucene((String) i.next());
                // teraz tetno link zacnem vyhladavat v indexe vytvorenom LUCENE
                query = parser.parse(link);
                hits = searcher.search(query);
                if (hits.length() > 0) {
                    COUNTER.incrementPointsByValue(URL_VALUE);
                    numberOfValidFoundedObjects++;
                    
                    if (sout)
                        System.out.println("Urls founded url : " + link);
                }
            }
//        } catch (ParseException pe) {
//            log4j.error("ParserException link=" + link, pe.getCause());
        } catch (Exception e) {
            log4j.error("searching link with Lucene caused error", e.getCause());
        }
        log4j.debug("URLS.search links=links");
    }
    
    /**
     * Metoda, ktora bude vyhladavat prave jednu predanu url. Bude sa pouzivat
     * pri analyzovani prvej startUrl, kde si mozeme nastavit kolko bodov
     * chceme pridat za najdenie url.
     *
     * @param link      odkaz, url v retazcovej podobe.
     * @param points    pocet bodov, ktore chceme pripocitat za najdenie url.
     */
    public void search(String link) {
        log4j.debug("URL.search link=" + link);
        try {
            // Prehladavam v odkazoch pomocou nainicializovaneho indexu.
            Query query;
            Hits hits;
            numberOfAllAnalyzedObjects++;
            // Preformatujem link do podoby v ktorej su linky z portalov.
            link = URLFormater.formatURLforLucene(link);
            // teraz tetno link zacnem vyhladavat v indexe vytvorenom LUCENE
            query = parser.parse(link);
            hits = searcher.search(query);
            if (hits.length() > 0) {
                COUNTER.incrementPointsByValue(URL_VALUE);
                numberOfValidFoundedObjects++;
                
                if (sout)
                    System.out.println("Url founded url : " + link);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("searching link with Lucene caused error", e.getCause());
        }
        log4j.debug("URLS.search links=links");
    }
    
    /**
     * Metoda, ktora vracia precenta vyskytu validnych objektov, cize url
     * odkazov pre danu krajiny.
     *
     * @return      precento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "URL NOT FOUND !";
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

