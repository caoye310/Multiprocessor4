package edu.vt.ece.hw4.backoff;

public class LinearBackoff implements Backoff {
    private final int minDelay = 1;
    private final int maxDelay = 2000;
    int delay = minDelay;

    @Override
    public void backoff() throws InterruptedException {
        Thread.sleep(Math.min(delay, maxDelay));
        delay += 1;
    }
}