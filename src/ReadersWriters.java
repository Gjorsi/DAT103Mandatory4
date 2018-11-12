import java.util.Random;
import java.util.concurrent.Semaphore;

public class ReadersWriters {

    static final Semaphore rwMutex = new Semaphore(1);
    static final Semaphore mutex = new Semaphore(1);
    static int readCount = 0;
    static int val = 0;
    static Random random = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        
        // kjører 1 writer
        new Thread(() -> {
            try {
                write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        // kjører 5 parallelle readers
        for (int i=0; i<5; i++) {
            new Thread(() -> {
                try {
                    read();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void read() throws InterruptedException {
        while(true) {
            
            mutex.acquire();
            if (++readCount == 1) {
                rwMutex.acquire();
            }
            mutex.release();
            
            System.out.println(val);
            
            mutex.acquire();
            if (--readCount == 0) {
                rwMutex.release();
            }
            mutex.release();
            
            Thread.sleep(random.nextInt(5000));
        }
    }

    private static void write() throws InterruptedException {
        while (true) {
            rwMutex.acquire();
            val++;
            rwMutex.release();
            Thread.sleep(random.nextInt(2000));
        }
        
        
    }

}
