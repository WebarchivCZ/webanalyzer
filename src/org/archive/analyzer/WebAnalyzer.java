/*
 * WebAnalyzer.java
 *
 * Created on Streda, 2007, j�n 13, 18:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.analyzer;

import java.util.Set;
import org.apache.log4j.Logger;
import org.archive.test.CreateIndex;
import org.archive.test.SearchInIndex;

/**
 *
 * @author Ivan Vlcek
 */
public class WebAnalyzer {
    
    private static Logger log4j = Logger.getLogger(Crawler.class);
    
    /* Konstatna, ktora vytvori jedinu instanciu WebAnalyzeru. */
    private static final WebAnalyzer INSTANCE = new WebAnalyzer();
    
    /* Odkaz na instanciu jedinacika Crawler. */
    private static final Crawler CRAWLER = Crawler.getInstance();
    
    /**
     * Verejna staticka tovarna metoda, ktora nam preda jedinu vytvorenu
     * instanciu tejto triedy.
     *
     * @return webAnalyzer       instancia jednicika WebAnalyzer.
     */
    public static WebAnalyzer getInstance() {
        log4j.debug("getInstance()");
        
        return INSTANCE;
    }
    
    /**
     * Verejna staticka metoda, ktora zacne analyzovat danu url podla
     * nastavenej hodnoty krajiny ( zatial iba CZ ). Potom by som mohol
     * predavat aj ine parametre ako je MaxUrls, counter_Border ...
     * Tie by som mohol nastavit v konfiguraku a predat asi len jazyk.
     *
     * @param uri   uri ktore sa bude analyzovat.
     * @return true   ak stranka splna kriteria povodu danej krajiny.
     */
    public static boolean isInScope(String uri) {
        log4j.debug("isInScope uri=" + uri);
        
        // TODO dorobit predanie atributu String language = "cz" z HER, bud sem 
        // alebo tiez v konfiguraku, preriesim neskor, z
        
        String language = "cz";
        
        if (uri == null)
            throw new NullPointerException("uri is null");
        return CRAWLER.crawl(uri, language);
    }
    
    /**
     * Verejna staticka metoda, ktora zacne analyzovat danu url, podla nasta
     * venych hodnot paramterov. (neskor sa prida nastavenie pre krajinu).
     * 
     * @param uri uri analyzovanej webovej stranky
     * @param content textovy obsah analyzovanej stranky
     * @param links odkazy najdene na analyzovanej stranke.
     * @return true prave vtedy, ak sa analyzovana stranka vyhodnoti za cesku.
     */
    public static boolean isInScope(String uri, String content, Set links) {
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }
        /* null values will be processed directly in simple modules */
        log4j.debug("isInScope uri=" + uri);
        return CRAWLER.analyzePage(uri, content, links);
    }
    
    /**
     * Verejna staticka metoda, ktora bude inicializovat vsetky potrebne
     * objekty pre WebAnalyzer.
     */
    public static void initializeWebAnalyzer() {
        CRAWLER.initializeCrawler("cz");
    }
    
    /**
     * Verejna staticka metoda, ktora zatvara vsetky streamy, ktore boli 
     * v procese analyzy pouzite.
     */
    public static void closeWebAnalyzer() {
        CRAWLER.closeCrawler();
    }
    
    /**
     * Verejna staticka metoda, ktora resetuje WebAnalyzer pre dalsie vyuhodno-
     * tenie webovej stranky. Napriklad resetuje citac Counter na nulu.
     */
    public static void resetWebAnalyzer() {
        CRAWLER.resetCrawler();
    }
    
    /**
     * Sukromny konstruktor, zaisti vytvorenie jedinej instancie.
     */
    private WebAnalyzer() {
        log4j.debug("ivlcek WebAnalyzer created hashcode    =" + this.hashCode());
        log4j.debug("ivlcek WebAnalyzer Thread.name=" + 
                Thread.currentThread().getName());
    }
}
