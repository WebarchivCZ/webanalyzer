/*
 * Cities.java
 *
 * Created on September 9, 2007, 3:19 PM
 *
 * Trieda, ktora bude vyhladavat nazvy miest v danom slovniku, ktory sa urci 
 * pri inicializacii. Ak najde nazov mesta v slovniku tak pripocita body
 * analyzovanej stranke pomocou Countera.
 * TODO asi by som mal volat inicializovanie slovnikov a vsetkeho potrebneho do
 * tejto triedy. Aby sa mi zmensilo API.
 *
 */

package org.archive.analyzer.criteria;

import org.apache.log4j.Logger;
import org.archive.analyzer.Counter;
import org.archive.analyzer.dictionary.CitiesBinSearcher;

/**
 *
 * @author praso
 */
public class Cities {
    
    private final Logger log4j = Logger.getLogger(Cities.class);
    
    /* Jedina instancia tejto triedy. */
    private static final Cities INSTANCE = new Cities();
    
    /* Counter, ktory bude pripocitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Odkaz na CitiesBinSearcher. */
    private static final CitiesBinSearcher CITIES_BIN_SEARCHER = 
            CitiesBinSearcher.getInstance();

    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vystup najdene objekty? */
    private boolean sout;
    
    /**
     * Staticka tovarna metoda, ktora sprisupni jedinu instanciu tejto triedy.
     *
     * @return      jedina instancia tejto triedy.
     */
    public static Cities getInstance() {
        return INSTANCE;
    }
    
    /** 
     * Private constructor creates a new instance of Cities
     */
    private Cities() {
    }
    
    /**
     * Metoda, ktora bude slovo predane ako parameter vyhladavat v slovniku.
     * Ak ho najde tak pripocita pre analyzovanu stranku body.
     *
     * @param city      slovo, ktore sa ma vyhladat v slovniku
     */
    public void search(String city) {
        log4j.debug("CITIES.search city=" + city);
        numberOfAllAnalyzedObjects++;
        if (CITIES_BIN_SEARCHER.searchCity(city) != CITIES_BIN_SEARCHER.NOT_FOUND) {
            COUNTER.incrementPoints();
            numberOfValidFoundedObjects++;
            
            if (sout)
                System.out.println("Cities founded city : " + city);
        }
        log4j.debug("CITIES.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize miest, pre
     * danu krajinu.
     *
     * @return      percento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "CITY NOT FOUND !";
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
