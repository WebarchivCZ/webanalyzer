/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author praso
 */
public class WebAnalyzerProperties {

    private static final WebAnalyzerProperties INSTANCE =
            new WebAnalyzerProperties();

    public static WebAnalyzerProperties getInstance() {
        return INSTANCE;
    }
    // properties for geoIpSearcher
    private String geoIpSearcherCountryCode;
    private int geoIpSearcherPoint;
    // properties for dictionary
    private String dictionaryManagerLanguage;
    private int dictionarySearcherPoint;
    // properties for ulrAnalyzer
    private long minPointsToValid;
    private int depthToArchive;
    // properties for emailSearcher
    private String emailSearcherRegexp;
    private int emailSearcherPoint;
    // properties for phoneSearhcer
    private String phoneSearcherRegexp;
    private int phoneSearcherPoint;
    // properties for htmlLangSearcher
    private String htmlLangSearcherRegexp;
    private int htmlLangSearcherPoint;
    // searchers to use
    private List<Integer> searchersToUse = new ArrayList<Integer>();

    private WebAnalyzerProperties() {
    // singleton
    }
    
    /**
     * Method inserts searchers, that will be used in the process into list.
     * @param use   represents 0 or 1, 0 - rejects and 1 - inserts searcher
     * @param searcherId represents id of searcher to be inserted
     */
    public void insertSearcher(int use, int searcherId) {
        if (use == 1) {
            searchersToUse.add(Integer.valueOf(searcherId));
        }
    }
    
    /**
     * Method that returns unmodifiable collection of searchersToUse
     * @return collection of searchers to use
     */
    public Collection<Integer> getSearchersToUse() {
        return Collections.unmodifiableList(searchersToUse);
    }
    
    /**
     * Method returns 1 if list of searchers to use, contains searcher 
     * given as a parameter
     * @param searcherId, id of searcher
     * @return 1 if searchers to use contains given searcher, 0 elsewhere
     */
    public int containsSearcher(int searcherId) {
        if (searchersToUse.contains(Integer.valueOf(searcherId))) {
            return 1;
        } else {
            return 0;
        } 
    }
    
    /**
     * Clears collection
     */
    public void clearSearchers() {
        this.searchersToUse.clear();
    }

    public String getGeoIpSearcherCountryCode() {
        return geoIpSearcherCountryCode;
    }

    public void setGeoIpSearcherCountryCode(String geoIpSearcherCountryCode) {
        this.geoIpSearcherCountryCode = geoIpSearcherCountryCode;
    }

    public int getGeoIpSearcherPoint() {
        return geoIpSearcherPoint;
    }

    public void setGeoIpSearcherPoint(int geoIpSearcherPoint) {
        this.geoIpSearcherPoint = geoIpSearcherPoint;
    }

    public String getDictionaryManagerLanguage() {
        return dictionaryManagerLanguage;
    }

    public void setDictionaryManagerLanguage(String dictionaryManagerLanguage) {
        this.dictionaryManagerLanguage = dictionaryManagerLanguage;
    }

    public int getDictionarySearcherPoint() {
        return dictionarySearcherPoint;
    }

    public void setDictionarySearcherPoint(int dictionarySearcherPoint) {
        this.dictionarySearcherPoint = dictionarySearcherPoint;
    }

    public long getMinPointsToValid() {
        return minPointsToValid;
    }

    public void setMinPointsToValid(long minPointsToValid) {
        this.minPointsToValid = minPointsToValid;
    }

    public int getDepthToArchive() {
        return depthToArchive;
    }

    public void setDepthToArchive(int depthToArchive) {
        this.depthToArchive = depthToArchive;
    }

    public String getEmailSearcherRegexp() {
        return emailSearcherRegexp;
    }

    public void setEmailSearcherRegexp(String emailSearcherRegexp) {
        this.emailSearcherRegexp = emailSearcherRegexp;
    }

    public int getEmailSearcherPoint() {
        return emailSearcherPoint;
    }

    public void setEmailSearcherPoint(int emailSearcherPoint) {
        this.emailSearcherPoint = emailSearcherPoint;
    }

    public String getPhoneSearcherRegexp() {
        return phoneSearcherRegexp;
    }

    public void setPhoneSearcherRegexp(String phoneSearcherRegexp) {
        this.phoneSearcherRegexp = phoneSearcherRegexp;
    }

    public int getPhoneSearcherPoint() {
        return phoneSearcherPoint;
    }

    public void setPhoneSearcherPoint(int phoneSearcherPoint) {
        this.phoneSearcherPoint = phoneSearcherPoint;
    }

    public String getHtmlLangSearcherRegexp() {
        return htmlLangSearcherRegexp;
    }

    public void setHtmlLangSearcherRegexp(String htmlLangSearcherRegexp) {
        this.htmlLangSearcherRegexp = htmlLangSearcherRegexp;
    }

    public int getHtmlLangSearcherPoint() {
        return htmlLangSearcherPoint;
    }

    public void setHtmlLangSearcherPoint(int htmlLangSearcherPoint) {
        this.htmlLangSearcherPoint = htmlLangSearcherPoint;
    }
}
