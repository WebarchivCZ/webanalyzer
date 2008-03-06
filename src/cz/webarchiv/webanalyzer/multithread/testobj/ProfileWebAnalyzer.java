/*
 * Class that tests threads and memory use netbeans profiler to run this.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzer;
import cz.webarchiv.webanalyzer.multithread.analyzer.UrlAnalyzer;
import cz.webarchiv.webanalyzer.multithread.managers.WebAnalyzerManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class ProfileWebAnalyzer {

    private static final Logger log4j =
            Logger.getLogger(ProfileWebAnalyzer.class);

    private static void dictionarySearching() throws Exception {
        log4j.debug("dictionarySearching");
        WebAnalyzerManager.getInstance().initializeManagers();

        MakeTestCrawlURI maker = new MakeTestCrawlURI();
        SimpleTestCrawlURI curi = maker.createEmptyCrawlURI("http://seznam.cz");
        UrlAnalyzer analyzer = new UrlAnalyzer();
        analyzer.analyze(curi.getUrl().toString(),
                curi.getContent(), curi.getOutLinks());

        log4j.debug("reached points=" +
                analyzer.getPointsCounter().getPoints());
        WebAnalyzerManager.getInstance().closeManagers();
        log4j.debug("dictionarySearching");
    }

    private static void multiThreadSearching() throws Exception {
        try {
            log4j.debug("multiThreadSearching");
            WebAnalyzer.getInstance().initialize();
//            WebAnalyzerManager.getInstance().initializeManagers();

            // todo urobit crawl pre search(curi) aj seach(curi.name, points, ...)
            List<SimpleTestThread> toes = new ArrayList<SimpleTestThread>();
            List<UrlAnalyzer> analyzers = new ArrayList<UrlAnalyzer>();
            MakeTestCrawlURI maker = new MakeTestCrawlURI();
            for (int i = 0; i < 50; i++) {
                SimpleTestCrawlURI curi = maker.createEmptyCrawlURI();
                curi.setOutLinks(maker.createTestURIs());
                // create a thread
                SimpleTestThread toe = new SimpleTestThread();
                toe.setSimpleTestCrawlURI(curi);
                toe.setAnalyzers(analyzers);
                toes.add(toe);
            }

            for (SimpleTestThread t : toes) {
                t.start();
            }

            // pause main thread for a while, so that the toe thread has enough
            // time to search URL.
            Thread.currentThread().sleep(40 * 1000);

            for (UrlAnalyzer analyzer : analyzers) {
                log4j.debug(analyzer.getPointsCounter().getPoints());
            }

//            WebAnalyzerManager.getInstance().closeManagers();
            WebAnalyzer.getInstance().close();
            log4j.debug("multiThreadSearching");
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ProfileWebAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
//        dictionarySearching();
        multiThreadSearching();
    }
}
