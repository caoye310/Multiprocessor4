package edu.vt.ece.hw4.barriers;

import java.util.concurrent.ExecutionException;

public interface Barrier {
    void enter(String barrierType) throws InterruptedException, ExecutionException;
}
