/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzer;
import cz.webarchiv.webanalyzer.multithread.analyzer.UrlAnalyzer;
import cz.webarchiv.webanalyzer.multithread.criteria.GeoIpSearcher;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.archive.analyzer.util.RandomUtil;

/**
 *
 * @author praso
 */
public class SimpleTestThread extends Thread {

    private static final Logger log4j =
            Logger.getLogger(SimpleTestThread.class);
    private SimpleTestCrawlURI curi;
    private List<UrlAnalyzer> analyzers;

    // todo premenit GeoSearcher a ostatne searcher aby sa sputstali metodu
    // run, treba spravit nejake rozhranie, aby som mohol nasetovat vlaknu
    // hociktory searcher a pouzivat jeho metody (napr. run, statistics atd...)
    public void run() {
        log4j.debug("run curi=" + curi);
        log4j.debug("Thread.name=" + Thread.currentThread().getName());
        Random aRandom = new Random();

//        waitForAWhile(RandomUtil.generateRandomInt(0, 10, aRandom) * 10000000);
        
        processCuri(curi);

        // wait for random time
//        waitForAWhile(RandomUtil.generateRandomInt(0, 10, aRandom) * 10000000);

    // todo ivl process curi by GeoIPSearcherTest
    // todo ivl call processCuri() method

    }

    private void processCuri(SimpleTestCrawlURI curi) {
        // skus vez Webanalyzer
        WebAnalyzer.getInstance().run(curi.getUrl().toString(), 
                curi.getContent(), curi.getOutLinks(), null);
        // create new UrlAnalyzer
//        UrlAnalyzer urlAnalyzer = new UrlAnalyzer();
//        analyzers.add(urlAnalyzer);
//        urlAnalyzer.analyze(curi.getUrl().toString(), curi.getContent(),
//                curi.getOutLinks());
    }

    public SimpleTestCrawlURI getSimpleTestCrawlURI() {
        return curi;
    }

    public void setSimpleTestCrawlURI(SimpleTestCrawlURI curi) {
        this.curi = curi;
    }

    public List<UrlAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public void setAnalyzers(List<UrlAnalyzer> analyzers) {
        this.analyzers = analyzers;
    }

    private void waitForAWhile(int iterations) {
        for (int i = 0; i < iterations; i++) {
        // do nothing
        }
    }
}
