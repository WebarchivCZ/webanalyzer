package vlakna;

import java.util.concurrent.atomic.AtomicInteger;

public class Task extends  Thread {
    
    private static AtomicInteger counter = new AtomicInteger();
    private static int MAX = 50;
    
    public Task(int id) {
        super("Vlakno " + id);
    }
    
    private static void doSomething() {
        for (int i = 0;i<10000000;i++);
    };
    
    public void run() {
        while (true) {
            doSomething();
            //synchronized (counter) {
                int n = counter.getAndIncrement();
                doSomething();
                if (n > MAX) {
                    break;
                }
                doSomething();
                System.out.println(getName() + ": " + n);
            //}
            doSomething();
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new Task(0).start();
        new Task(1).start();
        new Task(2).start();
    }
    
}
