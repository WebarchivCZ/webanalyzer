/*
 * DictLoaderLuceneTest.java
 *
 * Created on August 16, 2007, 11:31 PM
 *
 * TEST S VYHLADAVANIM SLOV POMOCOU LUCENE NEUSPEL KVOLI DIAKRITIKE. !!!
 *
 * Trieda je obdobou DictLoader. Bude vyhladavat slovicka v slovniku. Bude 
 * vsak pouzivat LUCENE. Chcem otestovat ktora trieda bude rychlejsia. 
 */

package org.archive.analyzer.dictionary;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Hits;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.archive.analyzer.*;

/**
 *
 * @author praso
 */
public class DictLoaderLucene {
    
    private static final Logger log4j = Logger.getLogger(DictLoaderLucene.class);
    
    /* Jedina instancia DictLoaderLuceneTest. */
    private static final DictLoaderLucene INSTANCE = new DictLoaderLucene();
    
    /* Konstanta, ktora sa pripocita k bodom stranky, ked sa najde slovicko. */
    private static final int DICT_LOADER_LUCENE = 1;
    
    /* Odkaz na jedinacika Counter, pouzijem jeho metodu decrement. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Staticka atributy z LUCENE, nainicializuju sa v inicializaLuceneIndex. */
    private static QueryParser parser;
    private static Searcher searcher;
    private static IndexReader indexReader;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu triedy
     * DictLoaderLuceneTest.
     */
    public static DictLoaderLucene getInstance() {
        return INSTANCE;
    }
    
    /**
     * Inicializacna metoda, tkrao nainicializuje IndexReader podla predaneho
     * parametru language. Parameter bude mat jednoduchu hodnotu retazec, 
     * napr: "cz", "sk" ,atd. Tento parameter sa pouzije na utvorenie 
     * vyslednej cesty k tym spravnym indexom pre danu triedu. Zbytom cesty je 
     * nastaveny defaultne napr. "_anal/lucene_index/ + param + 
     * /dict_loader_lucene".
     *
     * @param language   jazyk podla ktoreho sa dotvori cesta k spravnym indexom
     */
    public static void inicializeLuceneIndex(String language) {
        log4j.debug("inicializeLuceneIndex language=" + language);
        // Contents, asi znamena prehladavanie obsahu.
        String field = "contents";
        String path = "_anal/lucene_index/" + language +
                "/dict_index";
        
        try {
            indexReader = IndexReader.open(path);
            searcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new StandardAnalyzer();
            parser = new QueryParser(field, analyzer);
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("inicializeLuceneIndex error language=" + language);
        }
    }
    
    /**
     * Staticka metoda, ktora zavrie indexSearcher z LUCENE. 
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
    private DictLoaderLucene() {
    }
    
    /**
     * Metoda, ktora v predanom texte z analyzovanej stranky vyhladavat slovka
     * zo slovnika. Ak nejake najde tak pripocita body analyzovanej stanke.
     *
     * @param text   text z analyzovanej stranky.
     */
    public void search(String text) {
        if (text.isEmpty()) {
            // Zalogujem ze je prazdnyText a odidem z metody.
            log4j.warn("Founded plain text on analyzed page is empty");
        }
        
        try { 
            // Prehladavam v texte pomocou nainicilizovaneho indexu.
            StringTokenizer stringTokenizer = new StringTokenizer(text);
            String word = null;
            Query query;
            Hits hits;
            while (stringTokenizer.hasMoreTokens()) {
                word = stringTokenizer.nextToken();
                query = parser.parse(word);
                hits = searcher.search(query);
                if (hits.length() > 0) {
                    System.out.println("founded LUCENE word : " + word);
                    // todo nastavit Counter
//                    COUNTER.incrementByParam(DICT_LOADER_LUCENE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log4j.error("searching words in analyzed plain text caused error", 
                    e.getCause());
        }
    }
    
    
    
    
    
}
