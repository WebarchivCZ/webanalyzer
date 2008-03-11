/*
 * DictionarySearcher.java
 *
 * Created on Sobota, 2007, apr�l 28, 15:03
 *
 * Trieda BinarySearch sluzi na vyhladavanie daneho slova zadaneho ako retazec
 * v slovniku. Vyuziva triedy DictLoader na pracu so slovnikom a IndexLoader na
 * pracu s indexom k slovniku.
 */
package cz.webarchiv.webanalyzer.multithread.criteria;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.PointsCounter;
import cz.webarchiv.webanalyzer.multithread.analyzer.ProcessedCrawlURI;
import cz.webarchiv.webanalyzer.multithread.analyzer.util.FilterText;
import cz.webarchiv.webanalyzer.multithread.managers.DictionaryIndexManager;
import cz.webarchiv.webanalyzer.multithread.managers.DictionaryManager;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 *
 * @author Ivan Vlcek
 */
public class DictionarySearcher extends AStatisticsSearcher 
        implements ISearcher {

    private static final Logger log4j =
            Logger.getLogger(DictionarySearcher.class);
    /* DictionaryManager na pracu so slovnikom. */
    private static final DictionaryManager DICTIONARY_MANAGER =
            DictionaryManager.getInstance();
    /* DictionaryIndexManager na pracu s indexom k sloniku */
    private static final DictionaryIndexManager DICTIONARY_INDEX_MANAGER =
            DictionaryIndexManager.getInstance();
    /* FilterText prefiltruje text od nepisanych znakov, cisel a podobne. */
    private static final FilterText FILTER_TEXT = FilterText.getInstance();

    /* Ak sa nenajde hladane slovo metoda binarySearch vrati -1 */
    private final int NOT_FOUND = -1;
    // Premenne pre metodu binarySearch(), aby sa nemuseli stale vytvarat.
    private int low;    // zaciatok indexu
    private int high;   // koniec indexu
    private int mid;    // stred indexu
    private int skipBytes;  // byty ktore preskoci inutstream pri citani slova
    private String dictWord;    // slovo zo slovnika
    private PointsCounter pointsCounter;
    // properties from property file
    private int dictionarySearcherPoint = WebAnalyzerProperties.getInstance().
            getDictionarySearcherPoint();

    /**
     * Constructor.
     * @param pointsCounter, stores points reached by processed URI
     */
    public DictionarySearcher(PointsCounter pointsCounter) {
        this.pointsCounter = pointsCounter;
    }

    /**
     * Metoda, ktora bude v texte predanej URI hladat slova zo slovnika
     * Za kazde slovo v slovniku pripocita body pomocou pointsCounter
     * @param curi, processed URI.
     */
    public void search(ProcessedCrawlURI curi) {
        log4j.debug("search processedCrawlURI=" + curi.toString());
        String cleanText = FILTER_TEXT.withoutWhiteSpaces(
                FILTER_TEXT.removeTags(curi.getContent()));
        if (cleanText != null && cleanText.length() > 0) {
            StringTokenizer stringTokenizer =
                    new StringTokenizer(FILTER_TEXT.getFilteredText(cleanText));
            log4j.debug(FILTER_TEXT.getFilteredText(cleanText));
            while (stringTokenizer.hasMoreTokens()) {
                search(stringTokenizer.nextToken());
            }
        }
    }

    /**
     * Metoda, ktora bude hladat slovo zo slovnika. Ak ho najde tak inkrementuje
     * pocet bodov ziskanych pomocou pointsCounter.
     * @param word, slovo ktore sa ma hladat v slovniku
     */
    public void search(String word) {
        if (searchWord(word) != NOT_FOUND) {
            // todo nastavit logy na info, tie ktore netreba logovat vzdy
            log4j.debug("found word=" + word);
            pointsCounter.increment(dictionarySearcherPoint);
            validElements++;
        }
        allElements++;
    }

    /**
     * Metoda, ktora bude v slovniku vyhladavat slovo predane ako parameter.
     * Zatial vypisem na vystup najdene slovo a potom sa rozhodnem co bude
     * dana metoda vracat ako vysledok. Nevyhadudzujem vynimku ak je slovo 
     * word null, vramci rychlosti.
     *
     * @param word      slovo, ktore sa ma vyhladat.
     * @return pozicia, na ktorej sa nachadza hladane slovo;
     */
    private int searchWord(String word) {
        // Zaciatok indexu
        low = 0;
        // Koniec indexu.

        // TODO toto je asi zbytocne nastavovat pri hladavani kazdeho slova.
        // nastavit raz pri "inicizlizacii" by malo stacit.
        // rovnako pozret aj v ForbiddenDictionarySearcher.
        high = DICTIONARY_INDEX_MANAGER.getSize();
        // Stred indexu
        // int mid;
        // Pocet bytov, ktore preskoci inputstream pri nacitani slova
        // int skipBytes;
        // Retazec, kde sa bude ukladat slovo zo slovnika na porovnavanie.
        // String dictWord;

        while (low <= high) {
            mid = (low + high) / 2;
            // Nastavim hodnotu skipBytes.
            skipBytes = DICTIONARY_INDEX_MANAGER.getElement(mid).intValue();
            // Nacitam slovo zo slovnika na danom byte.
            dictWord = DICTIONARY_MANAGER.loadWord(skipBytes);
            if (dictWord == null) {
                // End of file reached, loadWord returned null
                return NOT_FOUND;
            }
            if ((dictWord.compareTo(word)) < 0) {
                low = mid + 1;
            } else if (dictWord.compareTo(word) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }
}
