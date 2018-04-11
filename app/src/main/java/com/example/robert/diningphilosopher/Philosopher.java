package com.example.robert.diningphilosopher;
import java.util.Random;

/**
 *
 * @author Robert Pierucci
 */
public class Philosopher implements Runnable {

    public int id;
    Monitor monitor = null;
    Random rnd = new Random();

    public Philosopher(int id, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void simulate() {
        try {
            Thread.sleep(rnd.nextInt(3000 - 1000 + 1) + 1000);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void run() {
        while (true) {
            simulate();
            try {
                monitor.getUtensil(id);
            } catch (InterruptedException ex) {
            }
            simulate();
            monitor.dropUtensil(id);
        }
    }
}