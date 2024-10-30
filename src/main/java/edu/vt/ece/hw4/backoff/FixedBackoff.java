package edu.vt.ece.hw4.backoff;

public class FixedBackoff implements Backoff {
    private final int delay = 10;

    @Override
    public void backoff() throws InterruptedException {
        Thread.sleep(delay);
    }
}