/*
 * This class filters the content.
 */

package cz.webarchiv.webanalyzer.dictionary;

/**
 *
 * @author praso
 */
class Filter implements IFilter {
    
    private final String NAME = "firstFilter";
    
    public String getName() {
        return NAME;
    }

    public String filter(String line) {
        return line;
    }

}
