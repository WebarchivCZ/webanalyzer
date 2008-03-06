/*
 * CitiesBinSearcher.java
 *
 * Created on September 9, 2007, 2:12 PM
 *
 * Trieda sluzi na vyhladavanie daneho slova zadaneho ako retazec v slovniku
 * miest. Vyuziva triedy CitiesDictLoader na pracu so slovnikom a
 * CitiesIndexLoader na pracu s indexom k slovniku.
 *
 */

package org.archive.analyzer.dictionary;

import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class CitiesBinSearcher {

    private final Logger log4j = Logger.getLogger(CitiesBinSearcher.class);
    
    /* Jedina instancia tejto triedy. */
    private static final CitiesBinSearcher INSTANCE = new CitiesBinSearcher();
    
    /* CitiesDictLoader na pracu so slovnikom. */
    private static final CitiesDictLoader CITIES_DICT_LOADER = 
            CitiesDictLoader.getInstance();
    
    /* CitiesIndexLoader  na pracu s indexom k slovniku. */
    private static final CitiesIndexLoader CITIES_INDEX_LOADER = 
            CitiesIndexLoader.getInstance();
    
    /* Ak sa nenajde hladane slovo, metoda, vrati -1. */
    public static final int NOT_FOUND = -1;
    
    /* Premenne pre meotdu binarySearch. */
    private int low;
    private int high;
    private int mid;
    private int skipBytes;
    private String cityWord;

    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu tejto
     * tiedy.
     * @return instance     jedina instancia tejto triedy.
     */
    public static CitiesBinSearcher getInstance() {
        return INSTANCE;
    }
    
    /** 
     * Creates a new instance of CitiesBinSearcher 
     */
    private CitiesBinSearcher() {
    }
    
    /**
     * Metoda, ktora bude v slovniku vyhladavat slovo predane ako parameter.
     * Ak sa najde tak vratim presnu poziciu slova, to znamena na ktorom byte v 
     * slovniku toto slovo zacina. Ak sa slovo nenajde tak sa vrati -1.
     *
     * @param city      slovo (mesto), ktore hladame v slovniku miest.
     * @return pozicia bytu na ktorej sa slovo nachadza.
     */
    public int searchCity(String city) {
        // Zaciatok indexu.
        low = 0;
        // TODO asi staci nastavit iba raz, nie pri kazdom slove ktore hladam.
        high = CITIES_INDEX_LOADER.getSize();
        
        while (low <= high) {
            mid = (low + high) / 2;
            // Nastavim hodnotu skipBytes.
            skipBytes = CITIES_INDEX_LOADER.getElement(mid).intValue();
            cityWord = CITIES_DICT_LOADER.loadWord(skipBytes);
            
            if ((cityWord.compareTo(city)) < 0) {
                low = mid + 1;
            } else if (cityWord.compareTo(city) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }
}
