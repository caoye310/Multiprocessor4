package edu.vt.ece.spin;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.ThreadLocal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class SpinSleepLock implements Lock {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final int maxSpin;
    private final Object monitor = new Object();

    public SpinSleepLock(int maxSpin) {
        this.maxSpin = maxSpin;
    }

    @Override
    public void lock() {
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
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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

    // any class implementing Lock must provide these methods
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
    public boolean tryLock(long time,
                           TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }
}