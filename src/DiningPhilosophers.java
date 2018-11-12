import java.util.Random;
import java.util.concurrent.Semaphore;

public class DiningPhilosophers {

    static final Semaphore[] chopStick = new Semaphore[5];
    static Random random = new Random();
    
    public static void main(String[] args) {
        
        // initialiser 5 chopsticks
        for (int i=0; i<5; i++) {
            chopStick[i] = new Semaphore(1);
        }
        
        // simulér 5 filosofer
        for (int i=0; i<5; i++) {
            int x = i;
            new Thread(() -> {
                try {
                    philosopher(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        
    }

    private static void philosopher(int i) throws InterruptedException {
        while (true) {
            //filosofen venter på at høyre og venstre chopstick skal bli ledig
            chopStick[i].acquire();
            chopStick[(i+1)%5].acquire();
            // filosofen har 2 chopsticks og kan nå spise
            System.out.println("Philosopher " + i + " is eating.");
            Thread.sleep(random.nextInt(10000)+2000); 
            System.out.println("Philosopher " + i + " has finished eating.");
            // filosofen har spist ferdig og legger fra seg begge chopsticks
            chopStick[i].release();
            chopStick[(i+1)%5].release();
            
            Thread.sleep(random.nextInt(10000)+2000); //tid for å filosofere
        }
        
    }

}
