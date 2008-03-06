/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.criteria;

import cz.webarchiv.webanalyzer.multithread.analyzer.util.AnalyzerConstants;

/**
 *
 * @author praso
 */
abstract class AStatisticsSearcher {
    
    long allElements;
    long validElements;

    public double getStatisticsInPercent() {
        if (allElements == 0l) {
            return 0d;
        }
        return (double) (validElements * 100)/allElements;
    }
    
    public String toString() {
        return this.getClass().getName() + 
                ", allElements=" + allElements +
                ", validElements=" + validElements +
                ", in percent=" + this.getStatisticsInPercent() +
                AnalyzerConstants.SystemProperties.LINE_SEPARATOR;
    }
    

}
