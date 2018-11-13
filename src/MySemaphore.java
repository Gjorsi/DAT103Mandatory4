import java.util.ArrayDeque;
import java.util.Queue;

public class MySemaphore {
    private int permits = 0;
    private boolean mutexLock = true;
    
    Queue<Thread> waiting;
    
    public MySemaphore(int permits) {
        this.permits = permits;
        waiting = new ArrayDeque<>();
    }
    
    public void acquire() throws InterruptedException {
        if (permits < 1) {
            while(!mutexLock) Thread.sleep(50);
            mutexLock = false;
            waiting.offer(Thread.currentThread());
            mutexLock = true;
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }
        }
        permits--;
    }
    
    public void release() throws InterruptedException {
        permits++;
        if (!waiting.isEmpty()) {
            while(!mutexLock) Thread.sleep(50);
            mutexLock = false;
            Thread x = waiting.poll();
            synchronized (x) {
                x.notify();
            }
            mutexLock = true;
        }
    }
}