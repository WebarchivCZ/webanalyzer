/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.testobj;

import java.net.URL;
import java.util.Set;

/**
 *
 * @author praso
 */
public class SimpleTestCrawlURI {

    private Set outLinks;
    private String content;
    private URL url;

    public Set getOutLinks() {
        return outLinks;
    }

    public void setOutLinks(Set outLinks) {
        this.outLinks = outLinks;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
