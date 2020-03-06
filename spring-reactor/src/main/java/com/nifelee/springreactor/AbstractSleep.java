package com.nifelee.springreactor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSleep {

    protected static ExecutorService es = Executors.newSingleThreadExecutor();

    protected static void sleep(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            // ignore
        }
    }

}
