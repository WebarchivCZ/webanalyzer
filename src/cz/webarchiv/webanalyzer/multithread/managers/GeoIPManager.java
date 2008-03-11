/*
 * Manager that establishes the connection to GeoLiteCountry DB.
 * todo try both implementations synchronized, or something else
 */
package cz.webarchiv.webanalyzer.multithread.managers;

import com.maxmind.geoip.LookupService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class GeoIPManager implements IManager {

    private static final Logger log4j = Logger.getLogger(GeoIPManager.class);
    private static final GeoIPManager INSTANCE = new GeoIPManager();
    
    /* Object to search in DB. */
    private LookupService lookupService;

    /**
     * Returns instance of this object.
     * @return instance of this object.
     */
    public static GeoIPManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Private constructor
     */
    private GeoIPManager() {
    // empty
    }

    /**
     * Initialize GeoIPManager
     */
    public void init() throws Exception {
        try {
            log4j.debug("initGeoIPManager");
            String dbFilePath = "_anal/geoip/GeoIP.dat";
            File dbFile = new File(dbFilePath);
            lookupService = new LookupService(
                    dbFile, LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException ioe) {
            log4j.fatal("initGeoIPManager", ioe.getCause());
            throw new RuntimeException(
                    "error when inicializing LookupService for GeoIPManager",
                    ioe.getCause());
        }
    }

    /**
     * Close GeoIPManager
     */
    public void close() throws Exception {
        lookupService.close();
    }

    /**
     * Get countryCode of the given IP. IP is represented by
     * InetAddress object. 
     * @param ip
     * @return country code in upper case
     */
    public synchronized String getCountryCode(InetAddress ip) {
        return lookupService.getCountry(ip).getCode().toLowerCase();
    }
}
