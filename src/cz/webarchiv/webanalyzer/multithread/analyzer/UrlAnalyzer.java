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
import cz.webarchiv.webanalyzer.multithread.criteria.HtmlLangSearcher;
import cz.webarchiv.webanalyzer.multithread.criteria.ISearcher;
import cz.webarchiv.webanalyzer.multithread.criteria.PhoneSearcher;
import cz.webarchiv.webanalyzer.multithread.mime.Content;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class UrlAnalyzer {

    private static final Logger log4j = Logger.getLogger(UrlAnalyzer.class);
    // searchers used by urlAnalyzer stored in a list
    List<ISearcher> searchers = new ArrayList<ISearcher>();
    // searchers used by urlAnalyzer
    // todo odstranit
    private GeoIpSearcher geoIpSearcher;
    private DictionarySearcher dictionarySearcher;
    private EmailSearcher emailSearcher;
    private PhoneSearcher phoneSearcher;
    private HtmlLangSearcher htmlLangSearcher;
    // other variables
    private PointsCounter pointsCounter;
    private long minPointsToValid = WebAnalyzerProperties.getInstance().
            getMinPointsToValid();

    /**
     * Public construtor.
     */
    public UrlAnalyzer() {
//        this.pointsCounter = new PointsCounter();
//        this.geoIpSearcher = new GeoIpSearcher(pointsCounter);
//        this.dictionarySearcher = new DictionarySearcher(pointsCounter);
//        this.emailSearcher = new EmailSearcher(pointsCounter);
//        this.phoneSearcher = new PhoneSearcher(pointsCounter);
//        this.htmlLangSearcher = new HtmlLangSearcher(pointsCounter);
        initialize(WebAnalyzerProperties.getInstance().getSearchersToUse());
    }

    private void initialize(Collection<Integer> searchersToUse) {
        this.pointsCounter = new PointsCounter();
        for (Integer searcher : searchersToUse) {
            switch (searcher.intValue()) {
                case (AnalyzerConstants.Searchers.GEO_IP_SEARCHER):
                    searchers.add(new GeoIpSearcher(pointsCounter));
                    log4j.trace("initialize created goeIpSearcher");
                    break;
                case (AnalyzerConstants.Searchers.DICTIONARY_SEARCHER):
                    // todo predtym inicializovat dict manager
                    searchers.add(new DictionarySearcher(pointsCounter));
                    log4j.trace("initlialize created dictionarySearcher");
                    break;
                case (AnalyzerConstants.Searchers.EMAIL_SEARCHER):
                    searchers.add(new EmailSearcher(pointsCounter));
                    log4j.trace("initialize created emailSearcher");
                    break;
                case (AnalyzerConstants.Searchers.PHONE_SEARCHER):
                    searchers.add(new PhoneSearcher(pointsCounter));
                    log4j.trace("initialize created phoneSearcher");
                    break;
                case (AnalyzerConstants.Searchers.HTML_LANG_SEARCHER):
                    searchers.add(new HtmlLangSearcher(pointsCounter));
                    log4j.trace("initialize created htmlLangSearcher");
                    break;
            }
        }
    }

    /**
     * Method that starts to analyze the given Url and collects reached points.
     * In the end it decides whether the Url is czech or is not. Border is
     * defined by properties file.
     */
    public boolean analyze(String url, String content, Set<String> links,
            String contentType) {
        log4j.debug("analyze url=" + url);
        ProcessedCrawlURI curi = new ProcessedCrawlURI(url, content, links,
                contentType);
        if (!isContetTypeText(curi)) {
            log4j.info("analyze, not text contentType curi=" + curi.toString());
            return false;
        }
        // start all searchers
//        geoIpSearcher.search(curi);
//        dictionarySearcher.search(curi);z
//        emailSearcher.search(curi);
//        phoneSearcher.search(curi);
//        htmlLangSearcher.search(curi);
        for (ISearcher searcher : searchers) {
            searcher.search(curi);
        }
        // log statistics
        log4j.info(this.getStatistics(curi));
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
                curi.toString() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // searchers
//        stringBuilder.append(geoIpSearcher.toString());
//        stringBuilder.append(dictionarySearcher.toString());
//        stringBuilder.append(emailSearcher.toString());
//        stringBuilder.append(phoneSearcher.toString());
//        stringBuilder.append(htmlLangSearcher.toString());
        for (ISearcher searcher : searchers) {
            stringBuilder.append(searcher.toString());
        }

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

    /**
     * Returns true if contentType of the processedCrawlURI is text
     * @param processedCrawlURI
     * @return true if contentType of processedCrawlURI is text
     */
    private boolean isContetTypeText(ProcessedCrawlURI curi) {
        // todo skontrolovat variantu getBytes(charset)
        curi.setContentType((new Content()).getContentType(
                curi.getContentType(), curi.getUrlName(),
                curi.getContent().getBytes()));
        if (curi.getContentType() != null && 
                curi.getContentType().indexOf("text") > -1) {
            return true;
        }
        return false;
    }
}
