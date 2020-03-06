package com.nifelee.springreactor.future;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class AnonymousFutureTaskTest extends AbstractSleep {

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            sleep(2L);
            log.debug("execute");
            return "Hello Future";
        }) {
            @Override
            protected void done() {
                try {
                    log.info(get());
                } catch (InterruptedException | ExecutionException e) {
                    // ignore
                }
            }
        };

        es.execute(futureTask);
        log.info("Exit");
        es.shutdown();
    }

}
