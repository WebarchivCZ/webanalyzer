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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import cz.webarchiv.webanalyzer.multithread.criteria.AStatisticsSearcher;
import cz.webarchiv.webanalyzer.multithread.managers.DBStatsManager;

/**
 *
 * @author praso
 */
public class UrlAnalyzer {

    private static final Logger log4j = Logger.getLogger(UrlAnalyzer.class);
    // searchers used by urlAnalyzer stored in a list
    List<ISearcher> searchers = new ArrayList<ISearcher>();
    // other variables
    private PointsCounter pointsCounter;
    private long minPointsToValid = WebAnalyzerProperties.getInstance().
            getMinPointsToValid();

    /**
     * Public construtor.
     */
    public UrlAnalyzer() {
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
        // start all searchers
        for (ISearcher searcher : searchers) {
            searcher.search(curi);
        }
        // log statistics
        log4j.info(this.getStatistics(curi));
        // insert statistics into DB if webanalyzer.properties says so
        if (WebAnalyzerProperties.getInstance().getDbStatsUse() == 1) {
            // call method that prepars parameters and inserts data into DB
            insertStats(curi);
        }
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
     * Possible design of statistics inserted into DB. For particular curi,
     * searchers, points counter, minValidPoints. According to the searcher used
     * assign appropriate values to parametres in precompiled query.
     *
     * Call this method only in case it is defined to do it so in webanalyzer.properties
     */
    private void insertStats(ProcessedCrawlURI curi) {
        // prepare ProcessedCrawlURI curi object for insert statement
        // todo set autoincremment for id
        log4j.debug("insertStats curi=" + curi.toString());
        curi.setId(0);
        curi.setReachedPoints(this.pointsCounter.getPoints());
        // set parametres according to searchers used
        // todo change this block of code. It's not effective way to do this action
        for (ISearcher searcher : searchers) {
            if (searcher instanceof GeoIpSearcher) {
                log4j.trace("add GeoIpSearcher stats into DB stats table");
                curi.setGeoIpAll(((AStatisticsSearcher) searcher).getAllElements());
                curi.setGeoIpValid(((AStatisticsSearcher) searcher).getValidElements());
                continue;
            }
            if (searcher instanceof DictionarySearcher) {
                log4j.trace("add DictionarySearcher stats into DB stats table");
                curi.setDictAll(((AStatisticsSearcher) searcher).getAllElements());
                curi.setDictValid(((AStatisticsSearcher) searcher).getValidElements());
                continue;
            }
            if (searcher instanceof HtmlLangSearcher) {
                log4j.trace("add HtmlLangSearcher stats into DB stats table");
                curi.setHtmlTagAll(((AStatisticsSearcher) searcher).getAllElements());
                curi.setHtmlTagValid(((AStatisticsSearcher) searcher).getValidElements());
                continue;
            }
            if (searcher instanceof EmailSearcher) {
                log4j.trace("add EmailSearcher stats into DB stats table");
                curi.setEmailAll(((AStatisticsSearcher) searcher).getAllElements());
                curi.setEmailValid(((AStatisticsSearcher) searcher).getValidElements());
                continue;
            }
        }
        // curi filled up with all necessary information for insert statement
        // call DBStatsManager and insert curi into DB
        DBStatsManager.getInstance().insertStats(curi);
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
