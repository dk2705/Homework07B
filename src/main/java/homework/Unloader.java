package homework;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;
        
public class Unloader implements Runnable {

    private String name;
    private int speed;
    private int timeMs;
    private Truck truck;
    private Semaphore semaphore;
    
    public Unloader(String name, int speed, int timeMs, Truck truck, Semaphore semaphore) {
        this.name = name;
        this.speed = speed;
        this.timeMs = timeMs;
        this.truck = truck;
        this.semaphore = semaphore;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public synchronized void unloadMoney(int volume) throws InterruptedException {
        System.out.println("Unloading started: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()) + ": " + volume + " units will be unloaded");
        Thread.sleep(timeMs);
        truck.unload(volume);
        System.out.println("Unloading finished: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()) + ": " + volume + " units unloaded");
    }
    
    @Override
    public void run() {
        while(true){ //разгрузчик ничего не знает о состоянии кучи, поэтому цикл бесконечный (завершится как демон)
            try{
                semaphore.acquire();
            }catch(InterruptedException e){
                System.out.println("Unloading Error");
            }
            while (truck.getCurrentVolume() > 0) {
                try {
                    unloadMoney((truck.getCurrentVolume() >= this.speed) ? this.speed : truck.getCurrentVolume());
                } catch (InterruptedException e) {
                    System.out.println("Unloading Error");
                }
            }
            semaphore.release();
            System.out.println("truck was unloaded; pass to transporter");
        }
    }
    
}
