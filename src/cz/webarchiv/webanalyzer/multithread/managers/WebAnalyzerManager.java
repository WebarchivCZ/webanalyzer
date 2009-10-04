/*
 * Manager for Webanalyzer, that inicializes all needed Managers to
 * connect to Databases or files.
 */
package cz.webarchiv.webanalyzer.multithread.managers;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.util.AnalyzerConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class WebAnalyzerManager {

    private static final Logger log4j = Logger.getLogger(
            WebAnalyzerManager.class);
    private static final WebAnalyzerManager INSTANCE = new WebAnalyzerManager();
    // todo collection of managers to init
    private List<IManager> managers = new ArrayList<IManager>();

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
        for (Integer searcher : WebAnalyzerProperties.getInstance().getSearchersToUse()) {
            switch (searcher.intValue()) {
                case (AnalyzerConstants.Searchers.GEO_IP_SEARCHER):
                    managers.add(GeoIPManager.getInstance());
                    GeoIPManager.getInstance().init();
                    log4j.debug("initializeManagers created geoIPManager");
                    break;
                case (AnalyzerConstants.Searchers.DICTIONARY_SEARCHER):
                    managers.add(DictionaryIndexManager.getInstance());
                    managers.add(DictionaryManager.getInstance());
                    DictionaryIndexManager.getInstance().init();
                    DictionaryManager.getInstance().init();
                    log4j.debug("initlializeManagers created dictionaryManagers");
                    break;
            }
        }
        // initialize DBStatsManager
        if (WebAnalyzerProperties.getInstance().getDbStatsUse() == 1) {
            log4j.debug("initialize DBStatsManager");
            managers.add(DBStatsManager.getInstance());
            DBStatsManager.getInstance().init();
            log4j.debug("initializeManagers created DBStatsManager");
        }
    }

    /**
     * Closes all managers.
     */
    public void closeManagers() throws Exception {
        log4j.debug("closeManagers");
        for (IManager manager : managers) {
            manager.close();
        }
    }
}
