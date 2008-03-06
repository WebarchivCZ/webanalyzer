/*
 * UrlName.java
 *
 * Created on July 22, 2007, 8:08 AM
 *
 * Trieda UrlName bude v nazve predanej danej Url hladat slova zo slovnika.
 * Pri najdeni slova sa inkrementuje citac Counter.
 */

package org.archive.analyzer.criteria;

import java.net.URL;
import org.apache.log4j.Logger;
import org.archive.analyzer.Counter;
import org.archive.analyzer.dictionary.BinSearcher;
import org.archive.analyzer.dictionary.DictLoader;
import org.archive.analyzer.dictionary.IndexLoader;

/**
 *
 * @author praso
 */
public class UrlName {
    
    private final Logger log4j = Logger.getLogger(UrlName.class);
    
    /* Jedina instancia UrlName. */
    private static final UrlName INSTANCE = new UrlName();
    
    /* Counter, ktory bude pricitavat body analyzovanej stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /* Konstanta, ktora sa pricita k bodom stranky, ak sa najde slovicko Url. */
    private static final int URL_NAME_VALUE = 5;
    
    /* Odkaz na jedinacika BinSearcher, ktory vyhladava slovicka v texte. */
    private static final BinSearcher BIN_SEARCHER = BinSearcher.getInstance();
    
    /* Regularny vyraz podla ktoreho sa budu rozdelovat slova z Url.  */
    private static final String URL_NAME_DIVIDER = "\\.|\\/";
    
    private String urlString;
    
    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych url. */
    private int numberOfValidFoundedObjects;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /**
     * Staticka tovarna metdoda, ktora vrati odkaz na jedinu instanciu
     * tejto triedy.
     *
     * @return urlName   jedina instancia tejto triedy,
     */
    public static UrlName getInstance() {
        return INSTANCE;
    }
    
    /**
     * Sukromny konstruktor.
     */
    private UrlName() {
    }
    
    /**
     * Metoda bude vyhladavat slova zo slovnika v predanom texte(nazvu Url).
     * Ak najde slovo v slovniku, tak inkrementuje Coiunter. Inkrementacna
     * metoda countera je zavolana pomocou  instancie BINSEARCHER.
     *
     * @param urlString   nazov Url, ktory sa bude prehladavat.
     */
    public void search(URL url) {
        log4j.debug("URL.search url=" + url.toExternalForm());
//        System.out.println(url.toExternalForm());
        // TODO NASTAVIT REGEXP PO0DLA STYLU ZAPISOVANIA URL, na male pismena.
        urlString = url.toString().replaceAll("http:\\/\\/|www", "");
        String[] urlWords = urlString.toLowerCase().split(URL_NAME_DIVIDER);
        for (int i = 0; i<urlWords.length; i++) {
            numberOfAllAnalyzedObjects++;
            if (BIN_SEARCHER.searchWord(urlWords[i]) != BIN_SEARCHER.NOT_FOUND) {
                COUNTER.incrementPoints();
                numberOfValidFoundedObjects++;
                
                if (sout)
                    System.out.println("UrlName founded word : " + urlWords[i]);
            }
        }
        log4j.debug("URL.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize slov,
     * ktore sa nachadzaju v nazve url.
     *
     * @return      percento vyskytu validnych objetov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "URL NAME NOT FOUND !";
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
    
    
    
    // TODO odstranit pomoocne metoody
    public void openStreams() {
        IndexLoader.getInstance().initialize("cz");
        DictLoader.getInstance().openStreams("cz");
    }
    
    public void closeStreams() {
        DictLoader.getInstance().closeStreams();
    }
}
