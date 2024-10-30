package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.*;

public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK = "MCSLock";
    private static final String SPINSLEEPLOCK = "SpinSleepLock";
    private static final String TTASLOCK = "TTASLock";
    private static final String TASLock = "TASLock";
    private static final String CLHLock = "CLHLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "normal" : args[0];

        /*
        String lockClass = (args.length <= 1 ? ALOCK : args[1]);
        int threadCount = (args.length <= 2 ? 16 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;*/
        int [] threads = {2, 4, 8, 16, 32};
        // TASLock, TTASLOCK, ALOCK, BACKOFFLOCK, SPINSLEEPLOCK, CLHLock, MCSLOCK
        String [] lockClasses = {BACKOFFLOCK};
        for (int i = 0; i < threads.length; i++) {
            for(int j = 0; j < lockClasses.length; j++) {
                run(args, mode, lockClasses[j], threads[i], 8000);
            }
        }

    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {
        double time = 0;
        for (int i = 0; i < 7; i++) {
            long tmp_time = 0;
            Lock lock = null;
            switch (lockClass.trim()) {
                case ALOCK:
                    lock = new ALock(threadCount);
                    break;
                case BACKOFFLOCK:
                    lock = new BackoffLock(args.length <= 1 ? "Linear" : args[1]);
                    break;
                case MCSLOCK:
                    lock = new MCSLock();
                    break;
                case SPINSLEEPLOCK:
                    lock = new SpinSleepLock(3);
                    break;
                case TTASLOCK:
                    lock = new TTASLock();
                    break;
                case TASLock:
                    lock = new TASLock();
                    break;
                case CLHLock:
                    lock = new CLHLock();
                    break;
            }

            switch (mode.trim().toLowerCase()) {
                case "normal":
                    final Counter counter = new SharedCounter(0, lock);
                    runNormal(counter, threadCount, iters);
                    break;
                case "empty":
                    tmp_time = runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    runLongCS(lock, threadCount, iters);
                    break;
                case "barrier":
                    Barrier b = new TTASBarrier(threadCount);
                    b.enter("Barrier1");
                    b.enter("Barrier2");
                    //throw new UnsupportedOperationException("Complete this.");
                    break;
                default:
                    throw new UnsupportedOperationException("Implement this");
            }
            if (i!=0 && i!=1){
                time += tmp_time;
            }
        }
        System.out.println(lockClass + " " + threadCount + ": Average time per thread is " + time / 5 + "ms");
    }

    private static void runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        //double time = (double) totalTime / threadCount;
        //return time;
    }

    private static long runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        EmptyCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        return totalTime / threadCount;
    }

    static long runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        LongCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        return totalTime / threadCount;
    }
}