/*
 * NewMain.java
 *
 * Created on 11. duben 2007, 15:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author xadamek2
 */
public class NewMain {
    
    public static void main(String[] args) {
        Set<String> col = new HashSet<String>();
        col.add("Dagi");
        col.add("NkD");
        for (String key : col) {
            col.remove(key);
            System.out.println(key);
        }
    }
    
}
