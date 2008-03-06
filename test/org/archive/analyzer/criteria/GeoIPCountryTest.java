/*
 * GeoIPCountryTest.java
 * JUnit based test
 *
 * Created on August 24, 2007, 5:40 PM
 */

package org.archive.analyzer.criteria;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.*;
import java.net.URL;

/**
 *
 * @author praso
 */
public class GeoIPCountryTest extends TestCase {
    
    public GeoIPCountryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of search method, of class org.archive.analyzer.GeoIPCountry.
     */
    public void testSearch() throws MalformedURLException {
        System.out.println("search");
        
        URL url = new URL("http://praso.webzdarma.cz");
        GeoIPCountry geoIPCountry = GeoIPCountry.getInstance();
        geoIPCountry.initializeGeoIPCountry("cz");
        List urls = new ArrayList();
        urls.add("http://ironmaiden.com");
        urls.add("http://is.muni.cz");
        urls.add("http://atlas.sk");
        urls.add("http://praso.webzdarma.cz");
        urls.add("http://cleverlance.com");
        urls.add("http://webarchiv.cz");
        
        geoIPCountry.search(urls);
        
        System.out.println("Statistics " + geoIPCountry.statisticsInPercent());
        geoIPCountry.closeGeoIPCountry();
    }
    
    public void testSearchUrl() throws MalformedURLException {
        System.out.println("Test searchUrl");
        
        URL url = new URL("http://seznam.cz");
        GeoIPCountry geoIPCountry = GeoIPCountry.getInstance();
        geoIPCountry.initializeGeoIPCountry("cz");
        geoIPCountry.search(url, 10);
        geoIPCountry.closeGeoIPCountry();
    }
            
            
}
