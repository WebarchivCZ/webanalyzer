/*
 * Test.java
 *
 * Created on 17. duben 2007, 12:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author xadamek2
 */
public class Test {
    
    /** Creates a new instance of Test */
    public Test(int a) {
    }
    
    public void x() {
        Test t = new Test(1) {
            public void x() {};
        };
    }
}
