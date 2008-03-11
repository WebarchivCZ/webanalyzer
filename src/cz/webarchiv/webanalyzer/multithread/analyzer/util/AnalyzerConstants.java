/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.analyzer.util;

/**
 *
 * @author praso
 */
public class AnalyzerConstants {

    public static final class CountryCode {
        public static final String CZECH = "cz";
        public static final String SLOVAK = "sk";
    }
    
    public static final class SystemProperties {
        public static final String LINE_SEPARATOR = 
                System.getProperty("line.separator");
    }
    
    public static final class Searchers {
        public static final int GEO_IP_SEARCHER = 1;
        public static final int DICTIONARY_SEARCHER = 2;
        public static final int EMAIL_SEARCHER = 3;
        public static final int HTML_LANG_SEARCHER = 4;
        public static final int PHONE_SEARCHER = 5;
        public static final int test = 0;
    }
}
