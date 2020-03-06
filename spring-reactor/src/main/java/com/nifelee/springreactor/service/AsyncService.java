package com.nifelee.springreactor.service;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncService extends AbstractSleep {

    @Async
    public Future<String> futureRun() {
        log.info("run..");
        sleep(2L);
        return new AsyncResult<>("Future");
    }

    @Async
    public ListenableFuture<String> listenableFutureRun() {
        log.info("run..");
        sleep(2L);
        return new AsyncResult<>("ListenableFuture");
    }

    public String call(String message) {
        sleep(1L);
        return "service/" + message;
    }

}
