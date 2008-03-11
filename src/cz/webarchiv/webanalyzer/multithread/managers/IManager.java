/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.managers;

/**
 *
 * @author praso
 */
public interface IManager {
    
    public void init() throws Exception;
    
    public void close() throws Exception;

}
