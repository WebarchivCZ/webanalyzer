/*
 * Manager for Webanalyzer, that inicializes all needed Managers to
 * connect to Databases or files.
 */

package cz.webarchiv.webanalyzer.multithread.managers;

import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class WebAnalyzerManager {
    
    private static final Logger log4j = Logger.getLogger(
            WebAnalyzerManager.class);

    private static final WebAnalyzerManager INSTANCE = new WebAnalyzerManager();
    
    /**
     * Returns instance of this singleton.
     * @return intance of singleton
     */
    public static WebAnalyzerManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Private constructor.
     */ 
    private WebAnalyzerManager() {
        // empty
    }
    
    /**
     * Inicializes all needed managers.
     */
    public void initializeManagers() throws Exception {
        log4j.debug("initializeManagers");
        GeoIPManager.getInstance().
                initGeoIPManager();
        DictionaryIndexManager.getInstance().
                initDictionaryIndexManager();
        DictionaryManager.getInstance().
                initDictionaryManager();
    }
    
    /**
     * Closes all managers.
     */
    public void closeManagers() throws Exception {
        log4j.debug("closeManagers");
        GeoIPManager.getInstance().
                closeGeoIPManager();
        DictionaryManager.getInstance().
                closeDictionaryManager();
    }
}
