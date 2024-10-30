package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final int maxSpin;
    private final Object monitor = new Object();

    public SpinSleepLock(int maxSpin) {
        this.maxSpin = maxSpin;
    }

    @Override
    public void lock() throws InterruptedException {
        while (true) {
            if (locked.compareAndSet(false, true)) {
                return;
            }
            if (counter.get() < maxSpin) {
                counter.incrementAndGet();
                try {
                    while (locked.get()) {

                    }
                } finally {
                    counter.decrementAndGet();
                }
            } else {
                synchronized (monitor) {
                    while (locked.get()) {
                        monitor.wait();
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
        synchronized (monitor) {
            monitor.notify();
        }
    }
}