package edu.vt.ece.hw4.barriers;

import java.util.concurrent.atomic.AtomicInteger;

public class Barrier2 {
    private final AtomicInteger[] b;

    public Barrier2(int n) {
        b = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            b[i] = new AtomicInteger(0);
        }
    }

    public void await(int id) {
        if (id == 0) {
            b[0].set(1);
        } else {
            while (b[id - 1].get() == 0) {
            }
            b[id].set(1);
            //System.out.println(id);
        }

        if (id == b.length - 1) {
            b[b.length - 1].set(2);
        } else {
            //System.out.println(b[b.length - 1]);
            while (b[b.length - 1].get() != 2) {

            }
        }
    }
}
