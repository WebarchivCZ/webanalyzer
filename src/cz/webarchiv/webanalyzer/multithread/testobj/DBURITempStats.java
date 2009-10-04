/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.testobj;

import java.net.URL;

/**
 *
 * @author Administrator
 */
public class DBURITempStats {

    private long id;
    private URL url;
    private String contentType;
    private long reachedPoints;
    private long geoIpAll;
    private long geoIpValid;
    private long dictAll;
    private long dictValid;
    private long htmlTagAll;
    private long htmlTagValid;
    private long emailAll;
    private long emailValid;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the geoIpAll
     */
    public long getGeoIpAll() {
        return geoIpAll;
    }

    /**
     * @param geoIpAll the geoIpAll to set
     */
    public void setGeoIpAll(long geoIpAll) {
        this.geoIpAll = geoIpAll;
    }

    /**
     * @return the geoIpValid
     */
    public long getGeoIpValid() {
        return geoIpValid;
    }

    /**
     * @param geoIpValid the geoIpValid to set
     */
    public void setGeoIpValid(long geoIpValid) {
        this.geoIpValid = geoIpValid;
    }

    /**
     * @return the dictAll
     */
    public long getDictAll() {
        return dictAll;
    }

    /**
     * @param dictAll the dictAll to set
     */
    public void setDictAll(long dictAll) {
        this.dictAll = dictAll;
    }

    /**
     * @return the dictValid
     */
    public long getDictValid() {
        return dictValid;
    }

    /**
     * @param dictValid the dictValid to set
     */
    public void setDictValid(long dictValid) {
        this.dictValid = dictValid;
    }

    /**
     * @return the htmlTagAll
     */
    public long getHtmlTagAll() {
        return htmlTagAll;
    }

    /**
     * @param htmlTagAll the htmlTagAll to set
     */
    public void setHtmlTagAll(long htmlTagAll) {
        this.htmlTagAll = htmlTagAll;
    }

    /**
     * @return the htmlTagValid
     */
    public long getHtmlTagValid() {
        return htmlTagValid;
    }

    /**
     * @param htmlTagValid the htmlTagValid to set
     */
    public void setHtmlTagValid(long htmlTagValid) {
        this.htmlTagValid = htmlTagValid;
    }

    /**
     * @return the emailAll
     */
    public long getEmailAll() {
        return emailAll;
    }

    /**
     * @param emailAll the emailAll to set
     */
    public void setEmailAll(long emailAll) {
        this.emailAll = emailAll;
    }

    /**
     * @return the emailValid
     */
    public long getEmailValid() {
        return emailValid;
    }

    /**
     * @param emailValid the emailValid to set
     */
    public void setEmailValid(long emailValid) {
        this.emailValid = emailValid;
    }

    public String toString() {
        return "DBURITempStats: id=" + id + ", url=" + url;
    }

    /**
     * @return the reachedPoints
     */
    public long getReachedPoints() {
        return reachedPoints;
    }

    /**
     * @param reachedPoints the reachedPoints to set
     */
    public void setReachedPoints(long reachedPoints) {
        this.reachedPoints = reachedPoints;
    }
}
