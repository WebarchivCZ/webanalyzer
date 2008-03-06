/*
 * ForbiddenUrlName.java
 *
 * Created on August 10, 2007, 6:52 PM
 *
 * Trieda bude sluzit na vyhladavanie slov na html stranke.
 * Pri najdeni stranky sa inkrementuje citac Counter.
 *
 */

package org.archive.analyzer.criteria;

import java.net.URL;
import org.apache.log4j.Logger;
import org.archive.analyzer.Counter;
import org.archive.analyzer.dictionary.DictLoader;
import org.archive.analyzer.dictionary.ForbiddenBinSearcher;
import org.archive.analyzer.dictionary.ForbiddenDictLoader;
import org.archive.analyzer.dictionary.ForbiddenIndexLoader;

/**
 *
 * @author praso
 */
public class ForbiddenUrlName {
    
    private final Logger log4j = Logger.getLogger(ForbiddenUrlName.class);
    
    /* Jedina instancia ForbiddenUrlName. */
    private static final ForbiddenUrlName INSTANCE = new ForbiddenUrlName();
    
    /* Counter, ktory bude pripocitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Konstanta, ktora sa pricita k bodom stranky aj sa najde slovicko Url. */
    private static final int FORBIDDEN_URL_NAME_VALUE = 1;
    
    /* Odkaz na jedinacika ForbiddenBinSearcher, ktory vyhladava slovicka. */
    private static final ForbiddenBinSearcher FORBIDDEN_BIN_SEARCHER =
            ForbiddenBinSearcher.getInstance();
    
    /* Regularny vyraz podla ktoreho sa budu rozdelovat slovicka v texte. */
    private static String FORBIDDEN_URL_NAME_DIVIDER = "\\.|\\/";
    
    private String forbiddenUrlString;
    
    /* Pocet vsetkych analyzovanych slov, pochadzajucich z nazvu url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych objektov. */
    private int numberOfValidFoundedObjects;

        /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;

    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinu instanciu
     * tejto triedy.
     *
     * @return forbiddenUrlName     jedina instancia tejto triedy.
     */
    public static ForbiddenUrlName getInstance() {
        return INSTANCE;
    }
    
    /**
     * Sukromny konstrutkor.
     */
    public ForbiddenUrlName() {
    }
    
    /**
     * Metoda bude vyhladavat slova zo slovnika v predanom texte (nazvu Url).
     * Ak najde slovo v slovniku, tak inkrementuje Counter. Inkrementacna
     * metoda countera je zavolana pomocou instancie FORBIDDEN_BIN_SEARCHER.
     *
     * @param forbiddenUrlString        nazov Url, ktory sa bude prehladavat
     */
    public void search(URL url) {
        log4j.debug("FORBIDDEN_URL_NAME.search url=" + url.toExternalForm());
        forbiddenUrlString = url.toString().replaceAll("http:\\/\\/|www", "");
        String[] forbiddenUrlWords =
                forbiddenUrlString.toLowerCase().
                split(FORBIDDEN_URL_NAME_DIVIDER);
        for (int i = 0; i < forbiddenUrlWords.length; i++) {
            numberOfAllAnalyzedObjects++;
            if (FORBIDDEN_BIN_SEARCHER.searchWord(forbiddenUrlWords[i]) !=
                    FORBIDDEN_BIN_SEARCHER.NOT_FOUND) {
                COUNTER.incrementPointsByValue(-1);
                numberOfValidFoundedObjects++;
                
                if (sout)
                    System.out.println("ForbiddenUrlWords founded word : " +
                            forbiddenUrlWords[i]);
            }
        }
        log4j.debug("FORBIDDEN_URL_NAME.search finished");
    }
    
    /**
     * Metoda, ktora vracia precenta vyskytu validnych objektov, cize najdenych
     * zakazanych slov, ktore sa nachadzaju v nazve url.
     *
     * @return      precento vyskytu validnych objetkov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "FORBIDDEN URL NAME NOT FOUND !";
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
    
    
    
    
    // TODO odstanit pomocne metody, sluzia pre testovacie ucely.
    public void openStreams() {
        ForbiddenIndexLoader.getInstance().initialize("cz");
        ForbiddenDictLoader.getInstance().openStreams("cz");
    }
    
    public void closeStreams() {
        ForbiddenDictLoader.getInstance().closeStreams();
    }
    
}
