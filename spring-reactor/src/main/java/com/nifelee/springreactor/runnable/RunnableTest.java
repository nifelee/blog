package com.nifelee.springreactor.runnable;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableTest extends AbstractSleep {

    public static void main(String[] args) {
        es.execute(() -> {
            sleep(2L);
            log.info("execute");
        });

        log.info("Exit");
        es.shutdown();
    }

}
