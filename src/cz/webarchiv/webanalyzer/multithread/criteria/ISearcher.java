/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.criteria;

import cz.webarchiv.webanalyzer.multithread.analyzer.ProcessedCrawlURI;

/**
 *
 * @author praso
 */
public interface ISearcher {

    public void search(ProcessedCrawlURI curi);
    
    public String toString();
}
