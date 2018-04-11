package com.example.robert.diningphilosopher;

/**
 * Created by Robert on 5/7/2017.
 */

public class Monitor {
    public int [] forksArray = null;
    public int [] philStatus = null;

    public Monitor(int [] forksArray, int [] philStatus) {
        this.forksArray = forksArray;
        this.philStatus = philStatus;
    }

    synchronized public void getUtensil(int id) throws InterruptedException {
        int leftUtensil = id;
        int rightUtensil = (id + 1) % 5;
        philStatus[id] = 0;
        while (forksArray[leftUtensil] != 0 || forksArray[rightUtensil] != 0) {
            wait();
        }
        forksArray[leftUtensil] = forksArray[rightUtensil] = 1;
        philStatus[id] = 1;
    }

    synchronized public void dropUtensil(int id) {
        philStatus[id] = 2;
        int leftUtensil = id;
        int rightUtensil = (id + 1) % 5;
        forksArray[leftUtensil] = 0;
        forksArray[rightUtensil] = 0;
        notifyAll();
    }
}
