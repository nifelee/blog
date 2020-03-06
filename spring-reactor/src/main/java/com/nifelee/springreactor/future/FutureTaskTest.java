package com.nifelee.springreactor.future;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureTaskTest extends AbstractSleep {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            sleep(2L);
            log.debug("execute");
            return "Hello Future";
        });

        es.execute(futureTask);
        log.info("Exit");
        log.info(futureTask.get());
        es.shutdown();
    }

}
