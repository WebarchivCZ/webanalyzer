/*
 * A.java
 *
 * Created on 3. duben 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vlakna;

import java.util.Random;

public class A {

    private static Random random = new Random();
    
    private static class MyThread1 extends Thread {
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println("Kuky");
                try {
                    Thread.sleep(random.nextInt(20));
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private static class MyRunnable2 implements Runnable {
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println("Kuk");
                try {
                    Thread.sleep(random.nextInt(20));
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Thread thread1 = new MyThread1();
        Thread thread2 = new Thread(new MyRunnable2());
        thread1.start();
        thread2.start();
    }
}
