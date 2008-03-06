/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread;

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

    private WebAnalyzerProperties() {
    // singleton
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
}
