package edu.vt.ece.hw4.backoff;

public class ExponentialBackoff implements Backoff {

    private final int minDelay = 1;
    private final int maxDelay = 2000;
    int delay = minDelay;

    @Override
    public void backoff() throws InterruptedException {
        delay = Math.min(maxDelay, 2 * delay);
        Thread.sleep(delay);
    }
}
