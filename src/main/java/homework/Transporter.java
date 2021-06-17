package homework;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;

public class Transporter implements Runnable {

    private String name;
    private int timeMs;
    private Semaphore semaphore;

    public Transporter(String name, int timeMs, Semaphore semaphore) {
        this.name = name;
        this.timeMs = timeMs;
        this.semaphore = semaphore;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public synchronized void move() throws InterruptedException {
        System.out.println("Moving started: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        Thread.sleep(timeMs);
        System.out.println("Moving finished: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
    }

    @Override
    public void run() {
        while(true){ //перевозчик ничего не знает о состоянии кучи, поэтому цикл бесконечный (завершится как демон)
            try{
                semaphore.acquire();
            }catch(InterruptedException e){
                System.out.println("Transporting Error");
            }
            try {
                move();
            } catch (InterruptedException e) {
                System.out.println("Transporting Error");
            }
            System.out.println("truck was moved");
            semaphore.release();
        }
    }    

}