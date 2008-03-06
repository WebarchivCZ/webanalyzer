/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.test;

import cz.webarchiv.webanalyzer.multithread.analyzer.PointsCounter;
import cz.webarchiv.webanalyzer.multithread.analyzer.UrlAnalyzer;
import cz.webarchiv.webanalyzer.multithread.criteria.GeoIpSearcher;
import cz.webarchiv.webanalyzer.multithread.managers.GeoIPManager;
import cz.webarchiv.webanalyzer.multithread.managers.WebAnalyzerManager;
import cz.webarchiv.webanalyzer.multithread.testobj.MakeTestCrawlURI;
import cz.webarchiv.webanalyzer.multithread.testobj.SimpleTestCrawlURI;
import cz.webarchiv.webanalyzer.multithread.testobj.SimpleTestThread;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class GeoIPSearcherTest extends TestCase {

    private static final Logger log4j =
            Logger.getLogger(GeoIPSearcherTest.class);

    public GeoIPSearcherTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        WebAnalyzerManager.getInstance().initializeManagers();
    }

    protected void tearDown() throws Exception {
        WebAnalyzerManager.getInstance().closeManagers();
    }

    public void testSearch() {
        log4j.debug("testSearch");

        // WebAnalyzerManager is already initialized in setUp() method.
        UrlAnalyzer urlAnalyzer = new UrlAnalyzer();

        String url = "http://seznam.cz";
        String content = "";
        Set links = new HashSet();

        urlAnalyzer.analyze(url, content, links);

        assertEquals(1, urlAnalyzer.getPointsCounter().getPoints());
    }

    public void testSearchInThread() {
        try {
            log4j.debug("testSearchInThread");

            // todo use profiler to analyze the efficiency      

            List<Thread> toes = new ArrayList<Thread>();
            List<UrlAnalyzer> analyzers = new ArrayList<UrlAnalyzer>();
            MakeTestCrawlURI maker = new MakeTestCrawlURI();
            for (int i = 0; i < 50; i++) {
                // make test data
                SimpleTestCrawlURI curi = maker.createEmptyCrawlURI();
                // create a thread
                SimpleTestThread toe = new SimpleTestThread();
                toe.setSimpleTestCrawlURI(curi);
                toe.setAnalyzers(analyzers);
                toes.add(new Thread(toe));
            }

            for (Thread t : toes) {
                t.run();
            }

            // pause main thread for a while, so that the toe thread has enough
            // time to search URL.
            Thread.currentThread().sleep(5000l);

            for (UrlAnalyzer a : analyzers) {
                log4j.debug(a.getPointsCounter().getPoints());
                assertEquals(1, a.getPointsCounter().getPoints());
            }
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(GeoIPSearcherTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testMultiThreadSimulation() {
        log4j.debug("testMultiThreadSimulation");

    }
}
