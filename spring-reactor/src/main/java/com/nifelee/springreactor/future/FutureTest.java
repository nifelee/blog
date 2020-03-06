package com.nifelee.springreactor.future;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class FutureTest extends AbstractSleep {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<String> future = es.submit(() -> {
            sleep(2L);
            log.debug("execute");
            return "Hello Future";
        });

        log.info("Exit");
        log.info(future.get());
        es.shutdown();
    }

}
