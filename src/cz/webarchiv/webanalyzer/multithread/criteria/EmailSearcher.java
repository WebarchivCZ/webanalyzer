/*
 * This searcher looks for email in the text content of the processed crawlURI
 * It relevant email is found, PointsCounter is incremented.
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
public class EmailSearcher extends AStatisticsSearcher {

    private static final Logger log4j = Logger.getLogger(EmailSearcher.class);
    private final String emailDefaultRegexp = 
            "([a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,4})";
    private PointsCounter pointsCounter;
    private Pattern emailPattern;
    private Pattern emailDefaultPattern;
    private String foundDefaultEmail;
    /* Variables that are set by properties file. */
    private String emailSearcherRegexp = 
            WebAnalyzerProperties.getInstance().getEmailSearcherRegexp();
    private int emailSearcherPoint = 
            WebAnalyzerProperties.getInstance().getEmailSearcherPoint();

    /**
     * Constructor
     * @param pointsCounter, stores points reached by processed URI
     */
    public EmailSearcher(PointsCounter pointsCounter) {
        try {
            this.pointsCounter = pointsCounter;
            this.emailPattern = Pattern.compile(
                    emailSearcherRegexp, Pattern.CASE_INSENSITIVE);
            this.emailDefaultPattern = Pattern.compile(
                    emailDefaultRegexp, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException pse) {
            log4j.error("constructor, syntax error in regexp=" +
                    emailSearcherRegexp);
            throw new RuntimeException("create EmailSearcher instance", pse);
        }
    }

    /**
     * Looks for emails according to the defined regexp from 
     * webanalyzer.properties file. If relevant email id found 
     * pointsCounter is incremented according to the value point
     * defined in properties file.
     * @param processedCrawlURI to be examined
     */
    public void search(ProcessedCrawlURI curi) {
        log4j.debug("search curi=" + curi.toString());
        search(curi.getContent());
    }

    /**
     * Looks for emails in the text.
     * @param text to be examined
     */
    private void search(String text) {
        Matcher matcherDefault = emailDefaultPattern.matcher(text);
        while (matcherDefault.find()) {
            foundDefaultEmail = matcherDefault.group();
            allElements++;
            Matcher matcher = emailPattern.matcher(foundDefaultEmail);
            if (matcher.find()) {
                pointsCounter.increment(emailSearcherPoint);
                validElements++;
                log4j.info("search found email=" + foundDefaultEmail);
            }
        }
    }
}
