package edu.vt.ece.hw4.backoff;

public class PolynomialBackoff implements Backoff {
    private final int e = 2;
    private final int maxDelay = 2000;
    int count = 1;

    @Override
    public void backoff() throws InterruptedException {
        int delay = (int) Math.min(maxDelay, Math.pow(count, e));
        Thread.sleep(delay);
        count++;
    }
}