/*
 * MCSLock.java
 *
 * Created on January 20, 2006, 11:41 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package edu.vt.ece.spin;

import edu.vt.ece.hw4.locks.Lock;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.ThreadLocal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

/**
 * Mellor-Crummy Scott Lock
 * @author Maurice Herlihy
 */
public class MCSLock implements Lock {
    AtomicReference<QNode> tail;
    ThreadLocal<edu.vt.ece.spin.MCSLock.QNode> myNode;

    public MCSLock() {
        tail = new AtomicReference<>(null);
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = tail.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked) {
            }     // spin
        }
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (tail.compareAndSet(qnode, null))
                return;
            while (qnode.next == null) {
            } // spin
        }
        qnode.next.locked = false;
        qnode.next = null;
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

    static class QNode {     // Queue node inner class
        boolean locked = false;
        QNode   next = null;
    }
}