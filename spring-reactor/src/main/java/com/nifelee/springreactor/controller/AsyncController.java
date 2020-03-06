package com.nifelee.springreactor.controller;

import com.nifelee.springreactor.AbstractSleep;
import com.nifelee.springreactor.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AsyncController extends AbstractSleep {

    private final AsyncService asyncService;

    @GetMapping("/sync")
    public String sync() {
        log.info("sync..");
        sleep(1L);
        return "async";
    }

    @GetMapping("/callable")
    public Callable<String> callable() {
        log.info("callable..");
        return () -> {
            log.info("async");
            sleep(1L);
            return "callable";
        };
    }

    //tag::DeferredResult
    Queue<DeferredResult<String>> queue = new ConcurrentLinkedDeque<>();

    @GetMapping("/dr")
    public DeferredResult<String> dr() {
        log.info("dr..");
        DeferredResult<String> dr = new DeferredResult<>(TimeUnit.SECONDS.toMillis(120L));
        queue.add(dr);
        return dr;
    }

    @GetMapping("/dr/count")
    public int drCount() {
        return queue.size();
    }

    @GetMapping("/dr/message")
    public String drMessage(String message) {
        for (DeferredResult<String> dr : queue) {
            dr.setResult("DR : " + message);
            queue.remove(dr);
        }
        return "Success";
    }
    //end::DeferredResult

    @GetMapping("/mono")
    public Mono<String> mono(int index) {
        String response = asyncService.call("hello/" + index);
        return Mono.just("mono/" + response);
    }

}
