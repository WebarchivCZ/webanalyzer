/*
 * ForbiddenBinSearcher.java
 *
 * Created on August 18, 2007, 6:50 PM
 *
 * Trieda ForbidenBinSearcher sluzi na vyhladavanie daneho slova zadaneho ako
 * retazec v slovniku. Vyuziva triedy ForbiddenDictLoader na pracu so slovnikom
 * a indexLoader na pracu s indexom k slovniku.
 */

package org.archive.analyzer.dictionary;

import org.archive.analyzer.*;


/**
 *
 * @author praso
 */
public class ForbiddenBinSearcher {
    
    /* ForbiddenBinSearcher, ktory bude vyhladavat slovka v texte. */
    private static final ForbiddenBinSearcher INSTANCE = 
            new ForbiddenBinSearcher();
    
    /* Citac, ktory bude pricitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* ForbiddenDictLoader na pracu so slovnikom. */
    private static final ForbiddenDictLoader FORBIDDEN_DICT_LOADER = 
            ForbiddenDictLoader.getInstance();
    
    /* ForbiddenIndexLoader na pracu s indexom. */
    private static final ForbiddenIndexLoader FORBIDDEN_INDEX_LOADER = 
            ForbiddenIndexLoader.getInstance();
    
    /* Ak sa nenajde hladane slovo metoda binarySearch vrati -1 */
    public static final int NOT_FOUND = -1;
    
    /* Premenne pre metodu binarySearch(), aby sa nemuseli stale vytvarat. */
    private int low;
    private int high;
    private int mid;
    private int skipBytes;
    private String forbiddenDictWord;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu tejto
     * triedy.
     *
     * @return      jedina instancia tejto triedy.
     */
    public static ForbiddenBinSearcher getInstance() {
        return INSTANCE;
    }

    /** 
     * Sukromny konstruktor.
     */
    private ForbiddenBinSearcher() {
    }

    /**
     * Metoda, ktora bude v slovniku vyhladavat slovo predane ako parameter.
     * 
     * @param word      slovo, ktore sa ma vyhladatr v slovniku.
     * @return          pozicia na ktorej sa nachadza hladane slovo.
     */
    public int searchWord(String word) {
        // Zaciatok indexu
        low = 0;
        // Koniec indexu.
        high = FORBIDDEN_INDEX_LOADER.getSize();
        
        while(low <= high) {
            mid = (low + high) / 2;
            skipBytes = FORBIDDEN_INDEX_LOADER.getElement(mid).intValue();
            forbiddenDictWord = FORBIDDEN_DICT_LOADER.loadWord(skipBytes);
            if ((forbiddenDictWord.compareTo(word)) < 0) {
                low = mid + 1;
            } else if (forbiddenDictWord.compareTo(word) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        
        return NOT_FOUND;
    }
}
