package edu.vt.ece.hw4.barriers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import edu.vt.ece.hw4.barriers.Barrier1;
import edu.vt.ece.hw4.barriers.Barrier2;

public class TTASBarrier implements Barrier {
    private final Barrier1 barrier1;
    private final Barrier2 barrier2;
    private final int numThreads;

    public TTASBarrier(int numThreads) {
        this.barrier1 = new Barrier1(numThreads);
        this.barrier2 = new Barrier2(numThreads);
        this.numThreads = numThreads;
    }

    @Override
    public void enter(String barrierType) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Long>> results = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int id = i;
            Callable<Long> task = () -> {
                foo();
                long start = System.currentTimeMillis();

                if (barrierType.equals("Barrier1")) {
                    barrier1.await();
                } else if (barrierType.equals("Barrier2")) {
                    barrier2.await(id);
                }

                long end = System.currentTimeMillis();
                bar();
                return end - start;
            };
            results.add(executor.submit(task));
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MILLISECONDS);

        long totalBarrierTime = 0;
        for (Future<Long> result : results) {
            totalBarrierTime += result.get();
        }
        double averageBarrierTime = totalBarrierTime / (double) numThreads;
        System.out.println(barrierType + ": Average time per thread is " + averageBarrierTime + "ms");
    }

    public void foo() {
        int a = 1;
    }

    public void bar() {
        int a = 1;
    }
}
