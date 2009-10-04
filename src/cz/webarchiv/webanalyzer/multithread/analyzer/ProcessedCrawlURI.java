/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread.analyzer;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author praso
 */
public class ProcessedCrawlURI {

    private URL url;
    private String urlName;
    private String content;
    private String contentType;
    private Set<String> outlinks;
    // stats info
    private long id;
    private URL URL;
    private long reachedPoints;
    // points are initlialized to -1. This value will occur in DBStats table
    // provided that the particular searcher is not used. It helps you
    // distinguish between searchers required and searchers not required for
    // analysis in webanalyzer.properties file. 
    private long geoIpAll = -1L;
    private long geoIpValid = -1L;
    private long dictAll = -1L;
    private long dictValid = -1L;
    private long htmlTagAll = -1L;
    private long htmlTagValid = -1L;
    private long emailAll = -1L;
    private long emailValid = -1L;

    public ProcessedCrawlURI() {
    }

    public ProcessedCrawlURI(String urlName,
            String content, Set<String> outlinks, String contentType) {
        this.setUrlName(urlName);
        this.setContent(content);
        this.setOutlinks(outlinks);
        this.setContentType(contentType);
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        if (urlName == null) {
            urlName = "";
        } else {
            this.urlName = urlName;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null) {
            content = "";
        }
        this.content = content;
    }

    /**
     * Return text representation of the object ProcessedCrawlURI
     * @return text representation
     */
    public String toString() {
        return "urlName=" + urlName +
                "\nurlOutlinks.size=" + outlinks.size() +
                "\nurlContent.length=" + content.length() +
                "\nurlContenType=" + contentType;
    }

    public Set<String> getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(Set<String> outlinks) {
        if (outlinks == null) {
            this.outlinks = new HashSet<String>();
        } else {
            this.outlinks = outlinks;
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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
     * @return the URL
     */
    public URL getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(URL URL) {
        this.URL = URL;
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
}
