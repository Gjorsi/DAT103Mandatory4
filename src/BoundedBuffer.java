import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class BoundedBuffer {

    static final int n = 10;
    static final Semaphore mutex = new Semaphore(1); 
    static final Semaphore empty = new Semaphore(n);
    static final Semaphore full = new Semaphore(0);
    static Random random = new Random();
    static Queue<Integer> q;
    
    public static void main(String[] args) throws InterruptedException {
        
        q = new ArrayDeque<>();
        
        new Thread(() -> {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        new Thread(() -> {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
    }

    private static void consume() throws InterruptedException {
        while(true) {
            full.acquire();
            mutex.acquire();
            System.out.println("consumed: " + q.poll());
            empty.release();
            mutex.release();
            Thread.sleep(random.nextInt(2000)); // consumer sleep satt litt høyere slik at vi kan se bufferen fylle seg opp
        }
    }

    private static void produce() throws InterruptedException {
        while (true) {
            int v = random.nextInt(1000);
            empty.acquire();
            mutex.acquire();
            System.out.println("produced: " + v);
            q.offer(v);
            System.out.println("buffer size: " + q.size());
            full.release();
            mutex.release();
            Thread.sleep(random.nextInt(1700));
        }
    }

}
