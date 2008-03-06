/*
 * Words.java
 *
 * Created on Pondelok, 2007, jún 25, 8:27
 *
 * Trieda Words, ktora bude jedinacik. Vyhladava v predanom
 * texte slovicka.`Vzdy pri najdeni slova zo slovnika
 * inkrementuje citac COUNTER hodnotu stranky.
 */

package org.archive.analyzer.criteria;

import org.apache.log4j.Logger;
import org.archive.analyzer.Counter;
import org.archive.analyzer.dictionary.BinSearcher;

/**
 *
 * @author praso
 */
public class Words {
    
    private final Logger log4j = Logger.getLogger(Words.class);
    
    /* Jedina instancia tejto triedy */
    private static final Words INSTANCE = new Words();
    
    /* Counter, ktory bude pripocitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Odkaz na jedinacika BinSearcher, ktory vyhladava slovicka v texte. */
    private static final BinSearcher BIN_SEARCHER = BinSearcher.getInstance();
    
    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych url. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    
    /**
     * Staticka metoda, ktora spristupni jedinu instanciu
     * tejto triedy.
     *
     * @return instancia jedinacika tejto triedy.
     */
    public static Words getInstance() {
        return INSTANCE;
    }
    
    /**
     * Sukromny konstruktor vytvori instanciu a nastavi potrebne
     * parametre.
     */
    private Words() {
    }
    
    /**
     * Metoda, ktora bude slovo predane ako parameter vyhladavat v slovniku.
     * Ak ho najde tak pripocita body pre analyzovany stranku.
     *
     * @param word      slovo, ktore sa ma vyhladat v slovniku
     */
    public void search(String word) {
//        log4j.debug("WORDS.search word=" + word);
        numberOfAllAnalyzedObjects++;
        if (BIN_SEARCHER.searchWord(word) != BIN_SEARCHER.NOT_FOUND) {
            COUNTER.incrementPoints();
            numberOfValidFoundedObjects++;
            
            if (sout)
                System.out.println("Words founded word : " + word);
        }
//        log4j.debug("WORDS.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize slov, pre
     * danu krajinu.
     *
     * @return      percento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "WORD NOT FOUND !";
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
