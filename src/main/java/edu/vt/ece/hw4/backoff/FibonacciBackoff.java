package edu.vt.ece.hw4.backoff;


public class FibonacciBackoff implements Backoff {
    private final int minDelay = 1;
    private final int maxDelay = 2000;
    int delay1 = minDelay;
    int delay2 = minDelay;

    @Override
    public void backoff() throws InterruptedException {
        int tmp = delay1 + delay2;
        delay1 = delay2;
        delay2 = tmp;
        Thread.sleep(Math.min(maxDelay , delay1));
    }
}
