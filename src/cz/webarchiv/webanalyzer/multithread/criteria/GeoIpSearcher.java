/*
 * Searcher, that looks for country of IP address. Uses GeoLiteCountry.
 */

package cz.webarchiv.webanalyzer.multithread.criteria;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.PointsCounter;
import cz.webarchiv.webanalyzer.multithread.analyzer.ProcessedCrawlURI;
import cz.webarchiv.webanalyzer.multithread.managers.GeoIPManager;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class GeoIpSearcher extends AStatisticsSearcher implements ISearcher {
    
    private static final Logger log4j = Logger.getLogger(GeoIpSearcher.class);
    private static final GeoIPManager GEO_IP_MANAGER = 
            GeoIPManager.getInstance();
    
    private PointsCounter pointsCounter;
    
    /* Variables that are set by property file. */
    private String geoIpSearcherCountryCode = WebAnalyzerProperties.getInstance().
            getGeoIpSearcherCountryCode();
    private int geoIpSearcherPoint = WebAnalyzerProperties.getInstance().
            getGeoIpSearcherPoint();
    
    /**
     *  Constructor
     * @param pointsCounter, stores points reached by processed URI
     */
    public GeoIpSearcher(PointsCounter pointsCounter) {
        this.pointsCounter = pointsCounter;
    }
    
    /**
     * Looks for url in the GoeLiteCountry DB. If the url with
     * defined countryCode is found, points of the processed url
     * will be incremented by point value.
     * @param url, to look for
     * @param point, to add if the url is found, todo set by property file
     * @param country code of url, that we are looking for todo set by property 
     * file
     */
    public void search(String url, String countryCode, int point) {
        try {
            log4j.debug("search url=" + url + ", countryCode=" + countryCode + 
                    ", point=" + point);
            URL hostURL = new URL(url);
            InetAddress inetAddress = InetAddress.getByName(hostURL.getHost());
            if (countryCode.equals(
                    GEO_IP_MANAGER.getCountryCode(inetAddress))) {
                pointsCounter.increment(point);
                validElements++;
            }
            allElements++;
        } catch (MalformedURLException ex) {
            log4j.warn("search, Url is malformed url=" + url, ex);
        } catch (UnknownHostException uhe) {
            log4j.warn("search, unknown host for GeoCountryLite DB, url=" + 
                    url);
        }
    }
    
    /**
     * Looks for URLs (processed URL and links from processed URL) in the DB 
     * GeoLiteCountry. If URL is found in the 
     * DB and it's country code equals to defined country code, that is set by
     * property file, the points for the processed URL are incremented by the
     * value point, that is also set by property file.
     * @param processedCrawURI to be examined
     */
    public void search(ProcessedCrawlURI curi) {
        log4j.debug("search processedCrawlURI=" + curi.toString());
        search(curi.getUrlName(), geoIpSearcherCountryCode, geoIpSearcherPoint);
        for (String link : curi.getOutlinks()) {
            search(link, geoIpSearcherCountryCode, geoIpSearcherPoint);
        }
    }
    


//    public PointsCounter getPointsCounter() {
//        return pointsCounter;
//    }
//
//    public void setPointsCounter(PointsCounter pointsCounter) {
//        this.pointsCounter = pointsCounter;
//    }

}
