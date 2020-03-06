package com.nifelee.springreactor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AsyncControllerLoadTest {

    private static int THREAD_COUNT = 100;

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.error("===================================");
        execute(es);
        log.error("===================================");

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        stopWatch.stop();
        log.error("Total: {}", stopWatch.getTotalTimeSeconds());
    }

    private static void execute(ExecutorService es) {
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        for (int i=0; i<THREAD_COUNT; i++) {
            es.submit(() -> {
                int index = counter.getAndAdd(1);
                log.error("Thread-{}", index);

                barrier.await();

                StopWatch stopWatch = new StopWatch("thread");
                stopWatch.start();

                String response = client.method(HttpMethod.GET)
                        .uri("/mono?index=" + index)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                stopWatch.stop();
                log.error("Elapsed: {} - {} - {}", index, response, stopWatch.getTotalTimeSeconds());

                return null;
            });
        }
    }

}