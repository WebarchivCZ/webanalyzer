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

    public ProcessedCrawlURI() {

    }

    public ProcessedCrawlURI(String urlName,
            String content, Set<String> outlinks) {
        this.setUrlName(urlName);
        this.setContent(content);
        this.setOutlinks(outlinks);
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
                "\n urlOutlinks.size=" + outlinks.size() +
                "\n urlContent.length=" + content.length() +
                "\n urlContenType" + contentType;
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
}
