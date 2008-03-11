/*
 * Looks for phone numbers in the content of the processed URI.
 * If valid phoneNumber is found pointsCounter is incremented. Regexp for 
 * PhoneSearcher and value of point are defined in webanalyzer.properties file.
 */
package cz.webarchiv.webanalyzer.multithread.criteria;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzerProperties;
import cz.webarchiv.webanalyzer.multithread.analyzer.PointsCounter;
import cz.webarchiv.webanalyzer.multithread.analyzer.ProcessedCrawlURI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class PhoneSearcher extends AStatisticsSearcher implements ISearcher {

    private static final Logger log4j = Logger.getLogger(PhoneSearcher.class);
    private PointsCounter pointsCounter;
    // todo initialize default regexp, pozri predvolby v europe
    private String phoneSearcherDefaultRegexp = "\\+[0-9]{3} ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}";
    private Pattern phonePattern;
    private Pattern phoneDefaultPattern;
    private String foundDefaultPhone;
    /* Variables that are set by properties file. */
    private String phoneSearcherRegexp =
            WebAnalyzerProperties.getInstance().getPhoneSearcherRegexp();
    private int phoneSearcherPoint =
            WebAnalyzerProperties.getInstance().getPhoneSearcherPoint();

    /**
     * Constructor, initializes pattern.
     * @param pointsCounter, stores reached points of processed URI
     */
    public PhoneSearcher(PointsCounter pointsCounter) {
        try {
            this.pointsCounter = pointsCounter;
            this.phoneDefaultPattern = 
                    Pattern.compile(phoneSearcherDefaultRegexp);
            this.phonePattern = Pattern.compile(phoneSearcherRegexp);
        } catch (PatternSyntaxException pse) {
            log4j.fatal("constructor syntac error regexp=" + 
                    phoneSearcherRegexp, pse);
            throw new RuntimeException(pse);
        }
    }
    
    /**
     * Looks for phoneNumbers defined by webanalyzer.properties file. If valid
     * phoneNumber is found, the pointsCounter is incremented according to the
     * point defined in properties file
     * @param ProcessedCrawlURI curi to be examined
     */
    public void search(ProcessedCrawlURI curi) {
        log4j.debug("search curi=" + curi.toString());
        search(curi.getContent());
    }
    
    /**
     * Looks for phones in given text from processed URI
     * @param text to be examined
     */
    private void search(String text) {
        Matcher matcherDefault = phoneDefaultPattern.matcher(text);
        while (matcherDefault.find()) {
            foundDefaultPhone = matcherDefault.group();
            allElements++;
            if (phonePattern.matcher(foundDefaultPhone).find()) {
                pointsCounter.increment(phoneSearcherPoint);
                validElements++;
                log4j.debug("search found phone=" + foundDefaultPhone);
            }
        }
    }
}
