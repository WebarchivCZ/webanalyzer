/*
 * PropertySaver.java
 *
 * Created on October 9, 2007, 3:13 PM
 *
 * Trieda, ktoru nainicializuje objekt PropertyReader, pri starte programu
 * WebAnalyzer. Tento objekt bude jedinacik a bude poskytovat vlastnosti, ktore
 * potrebuju jednotlive moduly WebAnalyzatoru.
 *
 */

package org.apache.analyzer.config;

/**
 *
 * @author praso
 */
public class PropertySaver {
    
    public static final PropertySaver INSTANCE = new PropertySaver();
    
    private int crawlerPropertyMaxUrls;
    private int crawlerPropertyTimeout;
    
    /** 
     * Creates a new instance of PropertySaver 
     */
    private PropertySaver() {
    }

    public int getCrawlerPropertyMaxUrls() {
        return crawlerPropertyMaxUrls;
    }

    public void setCrawlerPropertyMaxUrls(int crawlerPropertyMaxUrls) {
        this.crawlerPropertyMaxUrls = crawlerPropertyMaxUrls;
    }

    public int getCrawlerPropertyTimeout() {
        return crawlerPropertyTimeout;
    }

    public void setCrawlerPropertyTimeout(int crawlerPropertyTimeout) {
        this.crawlerPropertyTimeout = crawlerPropertyTimeout;
    }
    
    
}
