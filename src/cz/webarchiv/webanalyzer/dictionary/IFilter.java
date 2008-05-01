/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.dictionary;

/**
 *
 * @author praso
 */
interface IFilter {
    
    String filter(String line);
    
    String getName();

}
