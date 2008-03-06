/*
 * ExtractorWebAnalyzer.java
 * 
 * created on February 24, 2008, 07:25 AM
 * 
 * New processor, that accepts or rejects html page. Decide is made by
 * WebAnalyzer modul.
 * // todo comment
 */
package cz.webarchiv.webanalyzer.multithread.analyzer;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.util.AnalyzerConstants;
import cz.webarchiv.webanalyzer.multithread.criteria.DictionarySearcher;
import cz.webarchiv.webanalyzer.multithread.criteria.EmailSearcher;
import cz.webarchiv.webanalyzer.multithread.criteria.GeoIpSearcher;
import cz.webarchiv.webanalyzer.multithread.criteria.PhoneSearcher;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class UrlAnalyzer {

    private static final Logger log4j = Logger.getLogger(UrlAnalyzer.class);
    // searchers used by urlAnalyzer
    private GeoIpSearcher geoIpSearcher;
    private DictionarySearcher dictionarySearcher;
    private EmailSearcher emailSearcher;
    private PhoneSearcher phoneSearcher;
    // other variables
    private PointsCounter pointsCounter;
    private long minPointsToValid = WebAnalyzerProperties.getInstance().
            getMinPointsToValid();

    /**
     * Public construtor.
     */
    public UrlAnalyzer() {
        this.pointsCounter = new PointsCounter();
        this.geoIpSearcher = new GeoIpSearcher(pointsCounter);
        this.dictionarySearcher = new DictionarySearcher(pointsCounter);
        this.emailSearcher = new EmailSearcher(pointsCounter);
        this.phoneSearcher = new PhoneSearcher(pointsCounter);
    }

    /**
     * Method that starts to analyze the given Url and collects reached points.
     * In the end it decides whether the Url is czech or is not. Border is
     * defined by properties file.
     */
    public boolean analyze(String url, String content, Set links) {
        log4j.debug("analyze url=" + url);
        ProcessedCrawlURI curi = new ProcessedCrawlURI(url, content, links);
        // start all searchers
        geoIpSearcher.search(curi);
        dictionarySearcher.search(curi);
        emailSearcher.search(curi);
        phoneSearcher.search(curi);
        // log statistics
        log4j.debug(this.getStatistics(curi));
        return this.getValid();
    }

    /**
     * Method return statistics of all Searcher processors used in analyze
     * @return statistics of all searchers
     */
    private String getStatistics(ProcessedCrawlURI curi) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR +
                "Statistics for url=" + curi.getUrlName() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // searchers
        stringBuilder.append(geoIpSearcher.toString());
        stringBuilder.append(dictionarySearcher.toString());
        stringBuilder.append(emailSearcher.toString());
        stringBuilder.append(phoneSearcher.toString());

        stringBuilder.append("Reached Points=" + pointsCounter.getPoints() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        stringBuilder.append("Valid=" + this.getValid() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        return stringBuilder.toString();
    }

    /**
     * Method return whether curi is valid or not.
     * @return true only if curi is valid, reached enough points
     */
    private boolean getValid() {
        return pointsCounter.getPoints() >= minPointsToValid;
    }

    /**
     * Returns assigned PointsCounter, that stores points reached for 
     * processed URL. This method is used only for test purposes.
     * @return instance of class PointsCounter
     */
    public PointsCounter getPointsCounter() {
        return pointsCounter;
    }
}
