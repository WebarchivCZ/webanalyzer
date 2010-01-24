/*
 * BinSearcher.java
 *
 * Created on Sobota, 2007, apr�l 28, 15:03
 *
 * Trieda BinarySearch sluzi na vyhladavanie daneho slova zadaneho ako retazec
 * v slovniku. Vyuziva triedy DictLoader na pracu so slovnikom a IndexLoader na
 * pracu s indexom k slovniku.
 */

package org.archive.analyzer.dictionary;

/**
 *
 * @author Ivan Vlcek
 */
public class BinSearcher {
    
    /* BinSearcher, ktory bude vyhladavat slovka v texte, je jedinacik */
    private static final BinSearcher INSTANCE = new BinSearcher();

    /* DictLoader na pracu so slovnikom. */
    private static final DictLoader DICT_LOADER = DictLoader.getInstance();
    
    /* IndexLoader na pracu s indexom k sloniku */
    private static final IndexLoader INDEX_LOADER = IndexLoader.getInstance();
    
    /* Velkost indexu, pouzita pri binarnom hladani( polenie intervalu ). */
//    private int indexSize = INDEX_LOADER.getSize();
    
    /* Premenne pre metodu binarySearch(), aby sa nemuseli stale vytvarat. */
    /* Zaciatok indexu */
    private int low;
    /* Koniec indexu. */
    private int high;
    /* Stred indexu */
    private int mid;
    /* Pocet bytov, ktore preskoci inputstream pri nacitani slova. */
    private int skipBytes;
    /* Retazec, kde sa bude ukladat slovo zo slovnika na porovnavanie. */
    private String dictWord;
    
    /* Ak sa nenajde hladane slovo metoda binarySearch vrati -1 */
    public static final int NOT_FOUND = -1;
    
    /**
     * Staticka metoda, ktora vrati odkaz na jedinacika BinSearcher.
     *
     * @return odkaz na jedinacika BinSearcher.
     */
    public static BinSearcher getInstance() {
        return INSTANCE;
    }
    
    /**
     * Konstruktor by mal vytvorit iba jednu instanciu pre celu aplikaciu.
     * Navrhovy vzor jedninacik. Konstruktor inicializuje instancie tried
     * IndexLoader a DictLoader na pracu so slovnikom a prislusnym indexom
     * pomocou predanych parametrov .
     *
     */
    private BinSearcher() {
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
    public int searchWord(String word) {
        // Zaciatok indexu
        low = 0;
        // Koniec indexu.
        
        // TODO toto je asi zbytocne nastavovat pri hladavani kazdeho slova.
        // nastavit raz pri "inicizlizacii" by malo stacit.
        // rovnako pozret aj v ForbiddenBinSearcher.
        high = INDEX_LOADER.getSize();
        // Stred indexu
        // int mid;
        // Pocet bytov, ktore preskoci inputstream pri nacitani slova
        // int skipBytes;
        // Retazec, kde sa bude ukladat slovo zo slovnika na porovnavanie.
        // String dictWord;
        
        while(low <= high) {
            mid = (low + high) / 2;
            // Nastavim hodnotu skipBytes.
            skipBytes = INDEX_LOADER.getElement(mid).intValue();
            // Nacitam slovo zo slovnika na danom byte.
            dictWord = DICT_LOADER.loadWord(skipBytes);
            if(dictWord == null) {
                // End of file reached, loadWord returned null
                return NOT_FOUND;
            }
            if((dictWord.compareTo(word)) < 0) {
                low = mid + 1;
            } else if(dictWord.compareTo(word) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }      
}
