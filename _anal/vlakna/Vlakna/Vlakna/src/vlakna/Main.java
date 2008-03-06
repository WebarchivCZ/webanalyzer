/*
 * Main.java
 *
 * Created on 3. duben 2007, 11:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vlakna;

import java.util.Random;

/**
 *
 * @author xadamek2
 */
public class Main {

    private static Random random = new Random();
    
    public static class MyThread1 extends Thread {
        public void run() {
            for (int i=0; i<20; i++) {
                System.out.println("Tuky");
                try {
                    Thread.sleep(random.nextInt(20));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static class MyRunnable2 implements Runnable {
        public void run() {
            for (int i=0; i<20; i++) {
                System.out.println("Tuk");
                try {
                    Thread.sleep(random.nextInt(20));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
        
    public static void main(String[] args) {
        Thread thread1 = new MyThread1();
        Thread thread2 = new Thread(new MyRunnable2());
        thread1.start();
        thread2.start();
        System.out.println("Main Thread ended");
    }
    
}
