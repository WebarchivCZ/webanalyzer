/*
 * This class contanis the main method that executes filtering dictionary and
 * creating index.
 */

package cz.webarchiv.webanalyzer.dictionary;

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
