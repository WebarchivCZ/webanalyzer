/*
 * HtmlLangSearcher objekt vyhladava v texte podla definovaneho regularneho
 * vyrazu html elementy lang
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
public class HtmlLangSearcher extends AStatisticsSearcher implements ISearcher {
    
    private static final Logger log4j = Logger.getLogger(HtmlLangSearcher.class);
    private final String htmlLangDefaultRegexp = 
            "lang ?= ?[\"|']? ?([a-z]){2} ?[\"|']?";
    private PointsCounter pointsCounter;
    private Pattern htmlLangDefaultPattern;
    private Pattern htmlLangPattern;
    private String foundHtmlLang;
    /* Variables that are set by the properties file. */
    private String htmlLangSearcherRegexp = 
            WebAnalyzerProperties.getInstance().getHtmlLangSearcherRegexp();
    private int htmlLangSearcherPoint = 
            WebAnalyzerProperties.getInstance().getHtmlLangSearcherPoint();
    
    /**
     * Constructor
     * @param pointsCounter, that stores reached points by processed URI
     */
    public HtmlLangSearcher(PointsCounter pointsCounter) {
        try {
        this.pointsCounter = pointsCounter;
        htmlLangDefaultPattern = Pattern.compile(htmlLangDefaultRegexp, Pattern.CASE_INSENSITIVE);
        htmlLangPattern = Pattern.compile(htmlLangSearcherRegexp, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException pse) {
            log4j.fatal("constructor, syntax error in regexp=" +
                    htmlLangSearcherRegexp);
            throw new RuntimeException("create HtmlLangSearcher instance", pse);
        }
    }
    
    /**
     * Metoda, ktora hlada v textovom obsahu html element lang s atributom
     * definovanym v externom subore webanalyzer.properties. Pokial najde
     * definovany element s atributom, tak sa inkrementuje citac pointsCounter
     * o definovany pocet bodov.
     * @param ProcessedCrawlURI, URI to be examined
     */
    public void search(ProcessedCrawlURI curi) {
        log4j.debug("search curi=" + curi);
        search(curi.getContent());
    }
    
    /**
     * Metoda, ktora v texte vyhldava html element lang s danym atributom podla
     * definovaneho regexpu.     
     * @param text, text to be examined
     */
    private void search(String text) {
        Matcher defaultMatcher = htmlLangDefaultPattern.matcher(text);
        while (defaultMatcher.find()) {
            foundHtmlLang = defaultMatcher.group();
            allElements++;
            if (htmlLangPattern.matcher(foundHtmlLang).find()) {
                pointsCounter.increment(htmlLangSearcherPoint);
                validElements++;
                log4j.debug("search found htmlLang=" + foundHtmlLang);
            }
        }
    }

}
