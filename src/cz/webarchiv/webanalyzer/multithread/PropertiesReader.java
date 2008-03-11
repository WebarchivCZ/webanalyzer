/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread;

import cz.webarchiv.webanalyzer.multithread.analyzer.util.AnalyzerConstants;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class PropertiesReader {

    private static final Logger log4j = Logger.getLogger(PropertiesReader.class);
    private static final WebAnalyzerProperties WEB_ANALYZER_PROPERTIES =
            WebAnalyzerProperties.getInstance();
    private static final PropertiesReader INSTANCE =
            new PropertiesReader();
    // properties for webanalyzer from properties file
    private final String PROPERTIES_PATH = "_anal/webanalyzer.properties";
    private final String GEOIPSEARHCER_COUNTRYCODE = "webanalyzer.searcher.geoip.countrycode";
    private final String GEOIPSEARCHER_POINT = "webanalyzer.searcher.geoip.point";
    private final String GEOIPSEARCHER_USE = "webanalyzer.searcher.geoip.use";
    private final String DICTIONARY_MANAGER_LANGUAGE = "webanalyzer.manager.dictionary.language";
    private final String DICTIONARY_SEARCHER_POINT = "webanalyzer.searcher.dictionary.point";
    private final String DICTIONARY_SEARCHER_USE = "webanalyzer.searcher.dictionary.use";
    private final String URL_ANALYZER_MIN_POINTS_TO_VALID = "webanalyzer.urlanalyzer.min.valid.points";
    private final String URL_ANALYZER_DEPTH_TO_ARCHIVE = "webanalyzer.urlanalyzer.depth.toarchive";
    private final String EMAILSEARCHER_REGEXP = "webanalyzer.searcher.email.regexp";
    private final String EMAILSEARCHER_POINT = "webanalyzer.searcher.email.point";
    private final String EMAILSEARCHER_USE = "webanalyzer.searcher.email.use";
    private final String PHONESEARCHER_REGEXP = "webanalyzer.searcher.phone.regexp";
    private final String PHONESEARCHER_POINT = "webanalyzer.searcher.phone.point";
    private final String PHONESEARCHER_USE = "webanalyzer.searcher.phone.use";
    private final String HTMLLANGSEARCHER_REGEXP = "webanalyzer.searcher.htmllang.regexp";
    private final String HTMLLANGSEARCHER_POINT = "webanalyzer.searcher.htmllang.point";
    private final String HTMLLANGSEARCHER_USE = "webanalyzer.searcher.htmllang.use";
    // todo domysliet
    // array of searchers to use in analyze

    public static PropertiesReader getInstance() {
        return INSTANCE;
    }
    private static Set<String> availableLanguages;
    private static Set<String> availableCountryCodes;

    // todo initialize all collections of languages
    static {
        availableLanguages = new HashSet<String>();
        availableLanguages.add("cz");
        // todo doplnit country kody GeoLiteCountry
        availableCountryCodes = new HashSet<String>();
        availableCountryCodes.add("cz");
    }

    /** Creates a new instance of PropertyReader */
    private PropertiesReader() {
    }

    /**
     * Metoda, ktora nacita premenne zo suboru properties a pomocou nich
     * vytvori objekt PropertiesReader. Pred tym vsak musi nacitane hodnoty
     * zvalidovat pomocou validacnych funkcii.
     */
    public WebAnalyzerProperties loadPropertiesReader() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(PROPERTIES_PATH));

            // urlanalyzer properties
            WEB_ANALYZER_PROPERTIES.setMinPointsToValid(
                    validateLong(properties.getProperty(
                    URL_ANALYZER_MIN_POINTS_TO_VALID),
                    URL_ANALYZER_MIN_POINTS_TO_VALID, 0, Long.MAX_VALUE));
            WEB_ANALYZER_PROPERTIES.setDepthToArchive(
                    validateInt(properties.getProperty(
                    URL_ANALYZER_DEPTH_TO_ARCHIVE),
                    URL_ANALYZER_DEPTH_TO_ARCHIVE, 0, 1000000));

            // geoipsearcher properties
            WEB_ANALYZER_PROPERTIES.setGeoIpSearcherCountryCode(
                    validateAvailableCollection(properties.getProperty(
                    GEOIPSEARHCER_COUNTRYCODE), GEOIPSEARHCER_COUNTRYCODE,
                    availableCountryCodes));
            WEB_ANALYZER_PROPERTIES.setGeoIpSearcherPoint(
                    validateInt(properties.getProperty(GEOIPSEARCHER_POINT),
                    GEOIPSEARCHER_POINT, 0, 10));
            WEB_ANALYZER_PROPERTIES.insertSearcher(
                    validateInt(properties.getProperty(GEOIPSEARCHER_USE), 
                    GEOIPSEARCHER_USE, 0, 1), 
                    AnalyzerConstants.Searchers.GEO_IP_SEARCHER);

            // dictionarysearcher properties
            WEB_ANALYZER_PROPERTIES.setDictionaryManagerLanguage(
                    validateAvailableCollection(properties.getProperty(
                    DICTIONARY_MANAGER_LANGUAGE), DICTIONARY_MANAGER_LANGUAGE,
                    availableLanguages));
            WEB_ANALYZER_PROPERTIES.setDictionarySearcherPoint(
                    validateInt(properties.getProperty(
                    DICTIONARY_SEARCHER_POINT),
                    DICTIONARY_SEARCHER_POINT, 0, 10));
            WEB_ANALYZER_PROPERTIES.insertSearcher(
                    validateInt(properties.getProperty(DICTIONARY_SEARCHER_USE),
                    DICTIONARY_SEARCHER_USE, 0, 1),
                    AnalyzerConstants.Searchers.DICTIONARY_SEARCHER);

            // emailsearcher properties
            WEB_ANALYZER_PROPERTIES.setEmailSearcherRegexp(
                    validateRegexp(properties.getProperty(
                    EMAILSEARCHER_REGEXP), 
                    EMAILSEARCHER_REGEXP));
            WEB_ANALYZER_PROPERTIES.setEmailSearcherPoint(
                    validateInt(properties.getProperty(
                    EMAILSEARCHER_POINT),
                    EMAILSEARCHER_POINT, 0, 10));
            WEB_ANALYZER_PROPERTIES.insertSearcher(
                    validateInt(properties.getProperty(EMAILSEARCHER_USE), 
                    EMAILSEARCHER_USE, 0, 1), 
                    AnalyzerConstants.Searchers.EMAIL_SEARCHER);
            
            // pohnesearcher properties
            WEB_ANALYZER_PROPERTIES.setPhoneSearcherRegexp(
                    validateRegexp(properties.getProperty(
                    PHONESEARCHER_REGEXP),
                    PHONESEARCHER_REGEXP));
            WEB_ANALYZER_PROPERTIES.setPhoneSearcherPoint(
                    validateInt(properties.getProperty(
                    PHONESEARCHER_POINT), 
                    PHONESEARCHER_POINT, 0, 10));
            WEB_ANALYZER_PROPERTIES.insertSearcher(
                    validateInt(properties.getProperty(PHONESEARCHER_USE), 
                    PHONESEARCHER_USE, 0, 1), 
                    AnalyzerConstants.Searchers.PHONE_SEARCHER);
            
            // htmllangseacher properies
            WEB_ANALYZER_PROPERTIES.setHtmlLangSearcherRegexp(
                    validateRegexp(properties.getProperty(
                    HTMLLANGSEARCHER_REGEXP),
                    HTMLLANGSEARCHER_POINT));
            WEB_ANALYZER_PROPERTIES.setHtmlLangSearcherPoint(
                    validateInt(properties.getProperty(
                    HTMLLANGSEARCHER_POINT),
                    HTMLLANGSEARCHER_POINT, 0, 10));
            WEB_ANALYZER_PROPERTIES.insertSearcher(
                    validateInt(properties.getProperty(HTMLLANGSEARCHER_USE), 
                    HTMLLANGSEARCHER_USE, 0, 1), 
                    AnalyzerConstants.Searchers.HTML_LANG_SEARCHER);

            // log properties from properties file
            log4j.info(this.toString());
        } catch (IOException ioe) {
            log4j.fatal("loadProperiesReader, can't load properties file=" +
                    PROPERTIES_PATH);
        }
        return WEB_ANALYZER_PROPERTIES;
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
    private static int validateInt(String intNumber, String property,
            int minValue, int maxValue) {
        if (intNumber == null) {
            log4j.fatal("validateInt intNumber is null, property=" + property);
            System.exit(0);
        }
        int number = 0;
        try {
            number = Integer.parseInt(intNumber);
        } catch (NumberFormatException nfe) {
            log4j.fatal("validateInt intNumber is in bad format, intNumber=" +
                    intNumber + " property=" + property);
            System.exit(0);
        }
        if (!((number >= minValue) && (number <= maxValue))) {
            log4j.fatal("validateInt number=" + number + " doesn't fetch " +
                    "minValue=" + minValue + " maxValue=" +
                    maxValue + " property=" + property);
            System.exit(0);
        }
        return number;
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
    private static long validateLong(String longNumber, String property,
            long minValue, long maxValue) {
        if (longNumber == null) {
            log4j.fatal("validateLong longNumber is null, property=" + property);
            System.exit(0);
        }
        long number = 0;
        try {
            number = Long.parseLong(longNumber);
        } catch (NumberFormatException nfe) {
            log4j.fatal("validateLong longNumber is in bad format, longNumber=" +
                    longNumber + ", property=" + property);
            System.exit(0);
        }
        if (!((number >= minValue) && (number <= maxValue))) {
            log4j.fatal("validateLong number=" + number + " doesn't fetch " +
                    "minValue=" + minValue + " maxValue=" +
                    maxValue + ", property=" + property);
            System.exit(0);
        }
        return number;
    }

    /**
     * Validates regular expresion. If syntax is invalid, the Exception 
     * is thrown.
     * @param regexp to validate
     * @param property, the name of the property from properties file
     * @return valid regexp
     */
    private static String validateRegexp(String regexp, String property) {
        try {
            Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException pse) {
            log4j.fatal("validateRegexp for property=" + property +
                    ", regexp=" + regexp, pse);
            System.exit(0);
        }
        return regexp;
    }

    /**
     * Validate String.
     *
     * @param inputString
     */
    private static String validateString(String inputString, String property,
            int minLength, int maxLength) {
        if (inputString == null) {
            log4j.fatal("ValidateString inputString for property=" + property +
                    " is null");
            System.exit(0);
        }
        inputString = inputString.toLowerCase();
        if (inputString.length() < minLength || inputString.length() > maxLength) {
            log4j.fatal("ValidateString inputString's length=" +
                    inputString.length() + " doesn't fetch minValue=" +
                    minLength + " maxValume=" + maxLength + ", property=" +
                    property);
            System.exit(0);
        }
        return inputString;
    }

    private static String validateAvailableCollection(String inputString,
            String property, Set<String> availableCollection) {
        inputString = inputString.toLowerCase();
        if (!availableCollection.contains(inputString)) {
            log4j.fatal("validateAvailableCollection for property=" +
                    property + ", input=" + inputString);
            System.exit(0);
        }
        return inputString;
    }

    public static void main(String[] args) {
        // Read properties file.
        PropertiesReader P = PropertiesReader.getInstance();
        P.loadPropertiesReader();
    }

    /**
     * Returns String representation of loaded properties.
     * @return loaded properties
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(this.getClass().getName() + " - loaded properties {" +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // urlanalyzer properties
        result.append(URL_ANALYZER_MIN_POINTS_TO_VALID + "=" +
                WEB_ANALYZER_PROPERTIES.getMinPointsToValid() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(URL_ANALYZER_DEPTH_TO_ARCHIVE + "=" +
                WEB_ANALYZER_PROPERTIES.getDepthToArchive() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // geoipsearcher properties
        result.append(GEOIPSEARCHER_USE + "=" +
                WEB_ANALYZER_PROPERTIES.containsSearcher(
                AnalyzerConstants.Searchers.GEO_IP_SEARCHER) + 
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(GEOIPSEARHCER_COUNTRYCODE + "=" +
                WEB_ANALYZER_PROPERTIES.getGeoIpSearcherCountryCode() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(GEOIPSEARCHER_POINT + "=" +
                WEB_ANALYZER_PROPERTIES.getGeoIpSearcherPoint() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // dictionary properties
        result.append(DICTIONARY_SEARCHER_USE + "=" +
                WEB_ANALYZER_PROPERTIES.containsSearcher(
                AnalyzerConstants.Searchers.DICTIONARY_SEARCHER) + 
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(DICTIONARY_MANAGER_LANGUAGE + "=" +
                WEB_ANALYZER_PROPERTIES.getDictionaryManagerLanguage() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(DICTIONARY_SEARCHER_POINT + "=" +
                WEB_ANALYZER_PROPERTIES.getDictionarySearcherPoint() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // email properties
        result.append(EMAILSEARCHER_USE + "=" +
                WEB_ANALYZER_PROPERTIES.containsSearcher(
                AnalyzerConstants.Searchers.EMAIL_SEARCHER) + 
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(EMAILSEARCHER_REGEXP + "=" +
                WEB_ANALYZER_PROPERTIES.getEmailSearcherRegexp() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(EMAILSEARCHER_POINT + "=" +
                WEB_ANALYZER_PROPERTIES.getEmailSearcherPoint() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // phone properties
        result.append(PHONESEARCHER_USE + "=" +
                WEB_ANALYZER_PROPERTIES.containsSearcher(
                AnalyzerConstants.Searchers.PHONE_SEARCHER) + 
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(PHONESEARCHER_REGEXP + "=" +
                WEB_ANALYZER_PROPERTIES.getPhoneSearcherRegexp() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(PHONESEARCHER_POINT + "=" +
                WEB_ANALYZER_PROPERTIES.getPhoneSearcherPoint() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        // lang properties
        result.append(HTMLLANGSEARCHER_USE + "=" +
                WEB_ANALYZER_PROPERTIES.containsSearcher(
                AnalyzerConstants.Searchers.HTML_LANG_SEARCHER) + 
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(HTMLLANGSEARCHER_REGEXP + "=" +
                WEB_ANALYZER_PROPERTIES.getHtmlLangSearcherRegexp() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append(HTMLLANGSEARCHER_POINT + "=" + 
                WEB_ANALYZER_PROPERTIES.getHtmlLangSearcherPoint() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR);
        result.append("}");
        return result.toString();
    }
}
