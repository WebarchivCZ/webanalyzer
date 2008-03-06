/*
 * GeoIPCountry.java
 *
 * Created on August 22, 2007, 7:51 PM
 *
 * Trieda, ktora bude vyhladavat predanu IP adresu v databaze MAXMIND GeoLite
 * Country. Bude si v sebe udrzovat informacie o pocte vsetkych analyzovanych
 * url a o pocte platnych najdenych url, pre dany jazyk (kod krajiny). Ma
 * metodu, ktora vracia kolko percent zo vsetkych analyzovanych url bolo
 * najdenych pre dany kod krajiny.
 *
 */

package org.archive.analyzer.criteria;

import com.maxmind.geoip.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.archive.analyzer.*;

/**
 *
 * @author praso
 */
public class GeoIPCountry {
    
    private static Logger log4j = Logger.getLogger(GeoIPCountry.class);
    
    /* Jedina instancia tejtro triedy. */
    private static final GeoIPCountry INSTANCE = new GeoIPCountry();
    
    /* Odkaz na jedinacika Counter, ktory bude pripocitavat body stranke. */
    private static final Counter COUNTER = Counter.getInstance();
    
    /*Konstanta, ktora sa pricita k bodom stranky. */
    private static final int GEO_IP_COUNTRY_VALUE = 1;
    
    /* Objekt pre vyhldavanie v DB GeoLite Country. */
    private LookupService lookupService;
    
    /* Kod krajiny, ktory sa ma vyhladavat. */
    private String countryCode;
    
    /* Pocet vsetkych analyzovanych url. */
    private int numberOfAllAnalyzedObjects;
    
    /* Pocet validnych najdenych url. */
    private int numberOfValidFoundedObjects;
    
    /* InetAddress pre ziskanie IP adresy z url. */
    private InetAddress inetAddress;
    
    /* Vypisat na vustup najdene objekty? .*/
    private boolean sout;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedinacika tejto triedy.
     *
     * @return instance     jedina instancia tejto triedy.
     */
    public static GeoIPCountry getInstance() {
        return INSTANCE;
    }
    
    
    /**
     * Sukromny kontruktor.
     */
    private GeoIPCountry() {
    }
    
    /**
     * Metoda, ktora nainicializuje GeoLite Country pre vyhladavanie v
     * databaze. DB je binarny subor ulozeny na disku. Je nutne zadat spravne
     * umiestnenie DB. Pri inicializacii sa nastavuje jazyk, kod krajiny, podla
     * toho ake stranky analyzujeme. napr : "cz", "sk" ...
     *
     * @param language      kod krajiny, ktoreho servery hladame.
     */
    public void initializeGeoIPCountry(String language) {
        log4j.debug("inicialize language=" + language);
        try {
            numberOfAllAnalyzedObjects = 0;
            numberOfValidFoundedObjects = 0;
            String dbFileDir = "_anal/geoip/GeoIP.dat";
            File dbFile = new File(dbFileDir);
            
//            System.out.println("existuje subor geoIP.dat ? " + dbFile.exists());
            
            lookupService = new LookupService(
                    dbFile, LookupService.GEOIP_MEMORY_CACHE);
            // TODO overit validaciu vstupneho parametru language, musi byt cz
            // alebo sk atd...
            countryCode = language.toUpperCase();
        } catch (IOException ioe) {
            log4j.error("inicialize language=" + language, ioe.getCause());
            ioe.printStackTrace();
        }
    }
    
    /**
     * Staticka metoda, ktora zatvara objekt na hladanie v MAXMIND DB.
     */
    public void closeGeoIPCountry() {
        lookupService.close();
    }
    
    /**
     * Metoda, ktora bude vyhladavat danu url adresu v databaze
     * MAXMIND GeoLite Country. Vzdy ked najde url ktora smeruje na
     * cz server, tak sa pricitaju body analyzovanej stranke.
     * Kod krajiny, ktory hladame sa nastavuje pri inicializacii tejto
     * triedy, metodou initialize.
     *
     * @param url       url, ktora sa bude vyhladvat v GeoLite Country.
     */
    public void search(List urlList) {
        log4j.debug("GEO_IP_COUNTRY.search urlList size=" + urlList.size());
        URL url = null;
        try {
            for (Iterator i = urlList.iterator(); i.hasNext();) {
                String urlString = (String) i.next();
                url = new URL(urlString);
                numberOfAllAnalyzedObjects++;
                log4j.debug("GEO_IP_COUNTRY.search created url");
                
                // Ziskanie IP adresy z url, funguje iba ked som online na nete.
                inetAddress = InetAddress.getByName(url.getHost());
                log4j.debug("GEO_IP_COUNTRY.search inetAddress created");
                if (lookupService.getCountry(inetAddress).getCode().equals(
                        countryCode)) {
                    COUNTER.incrementPointsByValue(GEO_IP_COUNTRY_VALUE);
                    numberOfValidFoundedObjects++;
                    
                    if (sout)
                        System.out.println("GeoIPCountry code url=" + url +
                                ", country=" + countryCode);
                }
            }
//        } catch (MalformedURLException murle) {
//            log4j.warn("search url=" + url, murle.getCause());
//            murle.printStackTrace();
//        } catch (UnknownHostException uhe) {
//            log4j.warn("search url=" + url, uhe.getCause());
//            uhe.printStackTrace();
        } catch(Exception e) {
            log4j.error("GEO_IP_COUNTRY search caused error, cause=", e.getCause());
        }
        log4j.debug("GEO_IP_COUNTRY.search finished");
    }
    
    /**
     * Metoda, ktora bude vyhladavat danu url adresu v databaze
     * MAXMIND GeoLite Country. Vzdy ked najde url ktora smeruje na
     * cz server, tak sa pricitaju body analyzovanej stranke.
     * Kod krajiny, ktory hladame sa nastavuje pri inicializacii tejto
     * triedy, metodou initialize.
     *
     * @param url       url, ktora sa bude vyhladvat v GeoLite Country.
     */
    public void search(Set urlSet) {
//        log4j.debug("GEO_IP_COUNTRY.search urlList size=" + urlSet.size());
        URL url = null;
        try {
            for (Iterator i = urlSet.iterator(); i.hasNext();) {
                String urlString = (String) i.next();
                url = new URL(urlString);
                numberOfAllAnalyzedObjects++;
//                log4j.debug("GEO_IP_COUNTRY.search created url");
                
                // Ziskanie IP adresy z url, funguje iba ked som online na nete.
                inetAddress = InetAddress.getByName(url.getHost());
//                log4j.debug("GEO_IP_COUNTRY.search inetAddress created");
                if (lookupService.getCountry(inetAddress).getCode().equals(
                        countryCode)) {
                    COUNTER.incrementPointsByValue(GEO_IP_COUNTRY_VALUE);
                    numberOfValidFoundedObjects++;
                    
                    if (sout)
                        System.out.println("GeoIPCountry code url=" + url +
                                ", country=" + countryCode);
                }
            }
//        } catch (MalformedURLException murle) {
//            log4j.warn("search url=" + url, murle.getCause());
//            murle.printStackTrace();
//        } catch (UnknownHostException uhe) {
//            log4j.warn("search url=" + url, uhe.getCause());
//            uhe.printStackTrace();
        } catch(Exception e) {
            log4j.error("GEO_IP_COUNTRY search caused error, cause=", e.getCause());
        }
//        log4j.debug("GEO_IP_COUNTRY.search finished");
    }
    
    /**
     * Metoda, ktora bude vyhladavat prave jednu predanu URL v GeoLiteCountry
     * DB. Bude sa pouzivat pri vyhladavani prvej startUrl, pricom si mozeme
     * nastavit ako ohodnotime najdenie prvych informacii
     *
     * @param url       url, ktora sa ma vyhladat v GeoLiteCountry DB
     * @param points     pocet bodov, ktore sa pricitaju analyzovanej stranke
     */
    public void search(URL url, int points) {
        try {
            inetAddress = InetAddress.getByName(url.getHost());
            if (lookupService.getCountry(inetAddress).getCode().equals(
                    countryCode)) {
                COUNTER.incrementPointsByValue(points);
//                numberOfValidFoundedObjects++;
            }
            if (sout) {
                System.out.println("GeoIPCountry code url=" + url +
                        ", country=" + countryCode);
            }
        } catch (UnknownHostException ex) {
            log4j.warn("search url=" + url + ex.getCause());
            ex.printStackTrace();
        }
//        log4j.debug("GEO_IP_COUNTRY.search finished");
    }
    
    /**
     * Metoda, ktora vracia percenta vyskytu validnych objektov, cize url, ktore
     * odkazuju na servery danej krajiny.
     *
     * @return      percento vyskytu validnych objektov.
     */
    public String statisticsInPercent() {
        if (numberOfAllAnalyzedObjects == 0) {
            return "GEO IP COUNTRY NOT FOUND !";
        }
        float percent;
        percent = numberOfValidFoundedObjects*100 / numberOfAllAnalyzedObjects;
        Float percentFloat = new Float(percent);
        return percentFloat.toString();
    }
    
    /**
     * Nastavuje vypisovanie najdenych objektov na vystup.
     *
     * @param sout      vypisat na standard output ?
     */
    public void setSout(boolean sout) {
        this.sout = sout;
    }
    
    
    
    
    public static void main(String[] args) {
        try {
            String sep = System.getProperty("file.separator");
            
            // Uncomment for windows
            // String dir = System.getProperty("user.dir");
            
            // Uncomment for Linux
            String dir = "/home/praso/devel/projects/netbeans/WebAnalyzer/_anal/geoip";
            
            String dbfile = dir + sep + "GeoIP.dat";
            // You should only call LookupService once, especially if you use
            // GEOIP_MEMORY_CACHE mode, since the LookupService constructor takes up
            // resources to load the GeoIP.dat file into memory
            //LookupService cl = new LookupService(dbfile,LookupService.GEOIP_STANDARD);
            LookupService cl = new LookupService(dbfile,LookupService.GEOIP_MEMORY_CACHE);
            
            // TODO neslo zistit zo zadaneho parametru "praso.webzdarma.cz", asi treba
            // byt online, zisti na nete, ci to pojde
            System.out.println(cl.getCountry("praso.webzdarma.cz").getCode());
            System.out.println(cl.getCountry("151.38.39.114").getCode());
            System.out.println(cl.getCountry("151.38.39.114").getName());
            System.out.println(cl.getCountry("12.25.205.51").getName());
            System.out.println(cl.getCountry("64.81.104.131").getName());
            System.out.println(cl.getCountry("200.21.225.82").getName());
            System.out.println(cl.getCountry("151.38.39.114").getName());
            System.out.println(cl.getRegion("151.38.39.114").region);
            System.out.println(cl.getID("151.38.39.114"));
//            System.out.println(cl.getLocation("151.38.39.114"));
//            System.out.println(cl.getOrg("151.38.39.114"));
            System.out.println(cl.getLocationwithdnsservice("151.38.39.114"));
            
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        }
    }
}

