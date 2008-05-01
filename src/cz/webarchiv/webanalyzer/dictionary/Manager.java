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
        String input = "_anal/korpus_cz_1.txt";
//        String input = "_anal/wordlist_syn2k.txt";
//        String output = "_anal/korpus_cz_1.txt";
        String output = "_anal/korpus_cz_2.txt";
        IFilter iFilter = new Filter();
        Manager manager = new Manager();
        manager.run(input, iFilter, output);
    }
}
