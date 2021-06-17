package homework;

import java.util.concurrent.Semaphore;

public class SerialWork {
    

    public static void main(String[] args) {
        Heap heap = new Heap(100);
        Truck truck = new Truck(6,0); //тележка вмещает 6 юнитов и изначально пустая
        Semaphore semaphore = new Semaphore(1, true);
        new Loader("loader", 3, 1000, 8000, heap, truck, semaphore); //скорость = 3 ед. за 1000 мс
        new Transporter("transporter", 5000, semaphore); //время провоза = 5000 мс
        new Unloader("unloader", 2, 1000, truck, semaphore); //скорость = 2 ед. за 1000 мс
    }

}