package edu.vt.ece.hw4.locks;

public interface Lock {
    void lock() throws InterruptedException;
    void unlock();
}
