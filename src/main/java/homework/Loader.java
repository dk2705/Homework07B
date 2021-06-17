package homework;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;
        
public class Loader implements Runnable {

    private String name;
    private int speed;
    private int timeMs;
    private int timeBeforeFinishMs;
    private Heap heap;
    private Truck truck;
    private Semaphore semaphore;
    
    public Loader(String name, int speed, int timeMs, int timeBeforeFinishMs, Heap heap, Truck truck, Semaphore semaphore) {
        this.name = name;
        this.speed = speed;
        this.timeMs = timeMs;
        this.timeBeforeFinishMs = timeBeforeFinishMs;
        this.heap = heap;
        this.truck = truck;
        this.semaphore = semaphore;
        Thread t = new Thread(this);
        t.setDaemon(false); //главный поток - он завершит работу, когда закончится куча
        t.start();
    }

    public synchronized void getMoney(int volume) throws InterruptedException {
        System.out.println("Loading started: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()) + ": " + volume + " units will be loaded");
        Thread.sleep(timeMs);
        heap.decrease(volume);
        truck.load(volume);
        System.out.println("Loading finished: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()) + ": " + volume + " units loaded");
    }
    
    @Override
    public void run() {
        int heapCurrentVolume = heap.getInitVolume();
        int truckMaxVolume = truck.getMaxVolume();
        while (heap.getCurrentVolume() > 0){
            try{
                semaphore.acquire();
            }catch(InterruptedException e){
                System.out.println("Loading Error");
            }
            while (truck.getCurrentVolume() < truckMaxVolume && heapCurrentVolume > 0) {
                heapCurrentVolume = heap.getCurrentVolume();
                try{
                    getMoney((heapCurrentVolume >= this.speed)?this.speed:heapCurrentVolume);
                }catch(InterruptedException e){
                    System.out.println("Loading Error");
                }
                heapCurrentVolume = heap.getCurrentVolume();
            }
            semaphore.release();
            System.out.println("truck was loaded; pass to transporter");
            System.out.println(heapCurrentVolume + " units left in heap");
        }
        try{
            Thread.sleep(timeBeforeFinishMs); //завершаем не сразу после окончания кучи, чтобы демоны успели перевезти и разгрузить последнюю тележку
        }catch(InterruptedException e){
            System.out.println("Finish waiting Error");
        }    
    }
    
}
