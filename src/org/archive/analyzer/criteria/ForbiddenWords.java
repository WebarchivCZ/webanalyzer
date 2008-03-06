/*
 * ForbiddenWords.java
 *
 * Created on August 10, 2007, 6:53 PM
 *
 * !!!!!!!!!!!!
 * TTRIEDA BY NEMALA OPET DOSTAT NA VSTUPE CELY TEXT ANALYZOVANEJ STRANKY A PRE
 * CHADAZAT ICH PO SLOVICKU A VYHLADAVAT ICH, ALE MAL BY SOM POUZIT VYHLADAVANIE
 * NORMALNYCH WORD AJ FORBIDDEN_WORDS  V JEDNEJ TRIEDE ...ABY SOM NEPRACHADZAL
 * KAZDY TEXT DVA KRAT.
 *
 * Trieda ForbiddenWord, ktora bude jedinacik. Vyhladava v pradanom
 * texte forbidden words. Vzdy pri najdeni slova zo slovnika sa odpocitaju
 * body analyzovanej strnke.
 */

package org.archive.analyzer.criteria;

import org.apache.log4j.Logger;
import org.archive.analyzer.Counter;
import org.archive.analyzer.dictionary.ForbiddenBinSearcher;

/**
 *
 * @author praso
 */
public class ForbiddenWords {
    
    private final Logger log4j = Logger.getLogger(ForbiddenWords.class);
    
    /* Jedina instancia tejto triedy. */
    private static final ForbiddenWords INSTANCE = new ForbiddenWords();
    
    /* Counter, ktory bude pripocitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Odkaz na jedinacika ForbiddenBinSearcher, ktory hlada slova v texte. */
    private static final ForbiddenBinSearcher FORBIDDEN_BIN_SEARCHER =
            ForbiddenBinSearcher.getInstance();
    
    /* Pocet vsetkych analyzovanych objektov, forbiddenWords. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych objektov. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu tejto
     * triedy.
     *
     * @return      jedina instancia tejto triedy.
     */
    public static ForbiddenWords getInstance() {
        return INSTANCE;
    }
    
    
    /**
     * Sukromny konstruktor.
     */
    private ForbiddenWords() {
    }
    
    /**
     * Metoda, ktora bude slovo predane ako parameter vyhladavat v sloniku.
     * Ak sa najde slovo tak sa analyzovanej stranke odpocitaju body.
     *
     * @param word      slovo, ktore sa ma vyhladat v sloniku
     */
    public void search(String word) {
        log4j.debug("FORBIDDEN_WORDS.search word=" + word);
        numberOfAllAnalyzedObjects++;
        if (FORBIDDEN_BIN_SEARCHER.searchWord(word) != FORBIDDEN_BIN_SEARCHER.NOT_FOUND) {
            COUNTER.incrementPointsByValue(-1);
            numberOfValidFoundedObjects++;
            
            if (sout)
                System.out.println("ForbiddenWords founded word : " + word);
        }
        log4j.debug("FORBIDDEN_WORDS.search");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize
     * forbiddenWords pre danu krajinu.
     *
     * @return      percento vyskytu validnych najdenych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "FORBIDDEN WORD NOT FOUND !";
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
