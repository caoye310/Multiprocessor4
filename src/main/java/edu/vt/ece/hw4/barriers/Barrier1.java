package edu.vt.ece.hw4.barriers;
import edu.vt.ece.hw4.locks.Lock;
import edu.vt.ece.hw4.locks.TTASLock;

import java.util.concurrent.atomic.AtomicInteger;

public class Barrier1 {
    private final int n;
    private AtomicInteger counter;
    private final Lock lock = new TTASLock();

    public Barrier1(int n) {
        this.n = n;
        counter = new AtomicInteger(0);
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            counter.getAndIncrement();
        } finally {
            lock.unlock();
        }
        // Spin until all threads have reached the barrier
        while (counter.get() < n) {
            // Busy-wait
        }
    }
}