/*
 * This class contanis the main method that executes filtering dictionary and
 * creating index.
 */

package cz.webarchiv.webanalyzer.dictionary;

import java.util.Collection;
import java.util.Set;

/**
 *
 * @author praso
 */
class Manager {
    
    /**
     * Method, that executes filtering.
     * @param input
     * @param output
     */
    void run(String input, IFilter iFilter, String output) {
        (new Editor()).edit(input, iFilter, output);
    }
    
    public static void main(String[] args) {
        filterDictByFile();
//        filterWordlist();
    }
    
    public static void filterDictByFile() {
        String dictFile = "_anal/wordlists/dict.csv";
        String inputFile = "_anal/wordlists/wordlist.en";
        
        Editor editor = new Editor();
        Collection<String> wordlist = editor.loadCollectionFromFile(dictFile);
        wordlist = editor.filterCollectionByFile(wordlist, inputFile);
        editor.writeCollectionToFile(wordlist, dictFile);
    }
    
    public static void filterWordlist() {
    // DANGER comment next lines carefully
//        String input = "_anal/wordlist_syn2k.txt";    // this is for filtering
//        String output = "_anal/korpus_cz_1.txt";      // this is for filtering
        String input = "_anal/korpus_cz_1.txt";     // this is for sorting
        String output = "_anal/korpus_cz_2.txt";    // this is for sorting
        IFilter iFilter = new Filter();
        Manager manager = new Manager();
        manager.run(input, iFilter, output);
    }
}
