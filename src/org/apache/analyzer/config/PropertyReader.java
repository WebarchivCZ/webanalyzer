/*
 * PropertyReader.java
 *
 * Created on October 9, 2007, 2:59 PM
 *
 * Trieda, ktora bude sluzit na ziskavanie vlastnosti zo suboru
 * webanal.properties.
 */

package org.apache.analyzer.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class PropertyReader {
    
    private static final Logger log4j = Logger.getLogger(PropertyReader.class);
    private static final PropertySaver PROPERTY_SAVER = PropertySaver.INSTANCE;
    
    /** Creates a new instance of PropertyReader */
    public PropertyReader() {
    }
    
    /**
     * Metoda, ktora nacita premenne zo suboru properties a pomocou nich
     * vytvori objekt PropertySaver. Pred tym vsak musi nacitane hodnoty
     * zvalidovat pomocou validacnych funkcii.
     */
    public static void loadPropertySaver() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("webanal.properties"));
            
            PROPERTY_SAVER.setCrawlerPropertyMaxUrls(validateInt(properties.getProperty("property.crawler.maxUrls"), "property.crawler.maxUrls", 1, 10000000));
            PROPERTY_SAVER.setCrawlerPropertyTimeout(validateInt(properties.getProperty("property.crawler.timeout"), "property.crawler.timeout", 3000, 10000));
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }
        
    }
    
    /**
     * Validate the input string.
     *
     * @param String intnumber
     * @param String property   name of the property
     * @param minValue          minValue for this property
     * @param maxValue          maxValue for this property
     * @return number if valid, if not valid exit program
     */
    private static int validateInt(String intNumber, String property, int minValue,
            int maxValue) {
        if (intNumber == null) {
            log4j.error("validateInt intNumber is null, property=" + property);
            System.exit(0);
        }
        int number = 0;
        try {
            number = Integer.parseInt(intNumber);
        } catch (NumberFormatException nfe) {
            log4j.error("validateInt intNumber is in bas format, intNumber=" +
                    intNumber);
            System.exit(0);
        }
        if (!((number >= minValue) && (number <= maxValue))) {
            log4j.error("validateInt number=" + number + " doesn't fetch " +
                    "minValue=" + minValue + " maxValue=" +
                    maxValue);
            System.exit(0);
        }
        return number;
    }
    
    /**
     * Validate String.
     *
     * @param inputString
     */
    private static String ValidateString(String inputString, String property,
            int minLength, int maxLength) {
        if (inputString == null) {
            log4j.error("ValidateString inputString for property=" + property +
                    " is null");
            System.exit(0);
        }
        if (inputString.length() < minLength || inputString.length() > maxLength) {
            log4j.error("ValidateString inputString's length=" +
                    inputString.length() + " doesn't fetch minValue=" +
                    minLength + " maxValume=" + maxLength);
            System.exit(0);
        }
        return inputString;
    }
    
    public static void main(String[] args) {
        // Read properties file.
        loadPropertySaver();
        
    }
    
}
