/*
 * WordsTest.java
 * JUnit based test
 *
 * Created on August 19, 2007, 5:04 PM
 */

package org.archive.analyzer.criteria;

import junit.framework.*;
import java.util.StringTokenizer;
import org.archive.analyzer.dictionary.DictLoader;
import org.archive.analyzer.dictionary.IndexLoader;

/**
 *
 * @author praso
 */
public class WordsTest extends TestCase {
    
    public WordsTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
//    public void testSearch() {
//        System.out.println("search");
//        
//        String text = "dovednost dovézt abandonovat abdominální mahátma " +
//                "slučitelný zakašlání zrušitelný žďár žvýkačka this is the" +
//                "occasionaly word in english, pure clear colosal track";
//        
//        IndexLoader.getInstance().initialize("cz");
//        DictLoader dictLoader = DictLoader.getInstance();
//        dictLoader.openStreams("cz");
//        
//        Words instance = Words.getInstance();
//        StringTokenizer stringTokenizer = new StringTokenizer(text);
//        while (stringTokenizer.hasMoreTokens()) {
//            String word = stringTokenizer.nextToken();
//            instance.search(word);
//        }
//        System.out.println(instance.statisticsInPercent());
//        dictLoader.closeStreams();
//    }
    
    public void testLoop() {
        System.out.println("Testovanie vyhladavanie v ckykle.");
        
        // POtrebna inicializacia
        String language = "cz";
        IndexLoader.getInstance().initialize(language);
        DictLoader.getInstance().openStreams(language);
        Words WORDS = Words.getInstance();
        
        // Prehladavanie textu.
        String text = "dovednost dovézt abandonovat abdominální mahátma " +
                "slučitelný zakašlání zrušitelný žďár žvýkačka this is the" +
                "occasionaly word in english, pure clear colosal track";
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        while (stringTokenizer.hasMoreTokens()) {
            String word = stringTokenizer.nextToken();
            WORDS.search(word);
        }
        System.out.println(WORDS.statisticsInPercent());
        
        String text2 = "dovednost this is the" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track" +
                "occasionaly word in english, pure clear colosal track";
        StringTokenizer stringTokenizer2 = new StringTokenizer(text2);
        while (stringTokenizer2.hasMoreTokens()) {
            String word = stringTokenizer2.nextToken();
            WORDS.search(word);
        }
        System.out.println(WORDS.statisticsInPercent());
        
        // Uzavrenie streamov
        DictLoader.getInstance().closeStreams();
        
    }
    
}
