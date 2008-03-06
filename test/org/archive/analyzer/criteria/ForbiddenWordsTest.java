/*
 * ForbiddenWordsTest.java
 * JUnit based test
 *
 * Created on August 19, 2007, 1:11 PM
 */

package org.archive.analyzer.criteria;

import junit.framework.*;
import java.util.StringTokenizer;
import org.archive.analyzer.dictionary.ForbiddenDictLoader;
import org.archive.analyzer.dictionary.ForbiddenIndexLoader;

/**
 *
 * @author praso
 */
public class ForbiddenWordsTest extends TestCase {
    
    public ForbiddenWordsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of search method, of class org.archive.analyzer.ForbiddenWords.
     */
    public void testSearch() {
        System.out.println("search");
        
        String text = "dovednost , slovo cesky, petky, tretky, fredky, a " +
                "dalsie slova, ktore su aj v inych jazycich." +
                "dovednost dovézt abandonovat abdominální mahátma.";
        
        ForbiddenIndexLoader.getInstance().initialize("cz");
        ForbiddenDictLoader forbiddenDictLoader = ForbiddenDictLoader.getInstance();
        forbiddenDictLoader.openStreams("cz");
        
        ForbiddenWords forbiddenWords = ForbiddenWords.getInstance();
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        while (stringTokenizer.hasMoreTokens()) {
            forbiddenWords.search(stringTokenizer.nextToken());
        }
        System.out.println(forbiddenWords.statisticsInPercent());
        forbiddenDictLoader.closeStreams();
    }
    
}
