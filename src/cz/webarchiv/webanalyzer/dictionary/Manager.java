/*
 * This class contanis the main method that executes filtering dictionary and
 * creating index.
 */

package cz.webarchiv.webanalyzer.dictionary;

import java.util.Collection;
import java.util.HashSet;
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
//        filterDictByFile();
//        filterWordlist();
        filterByCollection();
    }
    
    public static void filterDictByFile() {
        String dictFile = "_anal/wordlists/dict.csv";
        String inputFile = "_anal/wordlists/dict.sk";
        
        Editor editor = new Editor();
        Collection<String> wordlist = editor.loadCollectionFromFile(dictFile);
        wordlist = editor.filterCollectionByFile(wordlist, inputFile);
        editor.writeCollectionToFile(wordlist, dictFile);
    }
    
    public static void filterWordlist() {
    // DANGER comment next lines carefully
//        String input = "_anal/wordlist_syn2k.txt";    // this is for filtering
//        String output = "_anal/korpus_cz_1.txt";      // this is for filtering
        String input = "_anal/wordlists/wordlist.en";     // this is for sorting
        String output = "_anal/wordlists/dict.en";    // this is for sorting
        IFilter iFilter = new Filter();
        Manager manager = new Manager();
        manager.run(input, iFilter, output);
    }
    
    public static void filterByCollection() {
        Editor editor = new Editor();
        HashSet<String> origCollection = new HashSet<String>(editor.loadCollectionFromFile("_anal/wordlists/dict.csv"));
        System.out.println("origCollection.size=" + origCollection.size());
        Collection<String> filterBy = editor.loadCollectionFromFile("_anal/wordlists/dict.en");
        System.out.println("filterByColl.size=" + filterBy.size());
        Collection<String> filteredColl = editor.filterByCollection(origCollection, filterBy);
        System.out.println("origCollection.size=" + origCollection.size());
        System.out.println("filteredColl.size=" + filteredColl.size());
        editor.writeCollectionToFile(filteredColl, "_anal/wordlists/dict.cz");
    }
}
