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
    // properties for dbStats
    private int dbStatsUse;
    private String dbURL;
    private String dbUsername;
    private String dbPassword;
    private String dbTable;
    private int dbResetAutoincrement;
    private int dbTableCreate;
    private int dbDropTable;
    
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

    /**
     * @return the dbStatsUse
     */
    public int getDbStatsUse() {
        return dbStatsUse;
    }

    /**
     * @param dbStatsUse the dbStatsUse to set
     */
    public void setDbStatsUse(int dbStatsUse) {
        this.dbStatsUse = dbStatsUse;
    }

    /**
     * @return the dbURL
     */
    public String getDbURL() {
        return dbURL;
    }

    /**
     * @param dbURL the dbURL to set
     */
    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    /**
     * @return the dbUsername
     */
    public String getDbUsername() {
        return dbUsername;
    }

    /**
     * @param dbUsername the dbUsername to set
     */
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    /**
     * @return the dbPassword
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * @param dbPassword the dbPassword to set
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * @return the dbTable
     */
    public String getDbTable() {
        return dbTable;
    }

    /**
     * @param dbTable the dbTable to set
     */
    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    /**
     * @return the dbResetAutoincrement
     */
    public int getDbResetAutoincrement() {
        return dbResetAutoincrement;
    }

    /**
     * @param dbResetAutoincrement the dbResetAutoincrement to set
     */
    public void setDbResetAutoincrement(int dbResetAutoincrement) {
        this.dbResetAutoincrement = dbResetAutoincrement;
    }

    /**
     * @return the dbTableCreate
     */
    public int getDbTableCreate() {
        return dbTableCreate;
    }

    /**
     * @param dbTableCreate the dbTableCreate to set
     */
    public void setDbTableCreate(int dbTableCreate) {
        this.dbTableCreate = dbTableCreate;
    }

    /**
     * @return the dbDropTable
     */
    public int getDbDropTable() {
        return dbDropTable;
    }

    /**
     * @param dbDropTable the dbDropTable to set
     */
    public void setDbDropTable(int dbDropTable) {
        this.dbDropTable = dbDropTable;
    }
}
