package com.nifelee.springreactor;

import com.nifelee.springreactor.service.AsyncService;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class SpringReactorApplication {

    private final AsyncService asyncService;

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    // https://nickolasfisher.com/blog/How-to-Configure-Reactive-Netty-in-Spring-Boot-in-Depth
    @Bean
    public NioEventLoopGroup nioEventLoopGroup() {
        return new NioEventLoopGroup(1);
    }

    @Bean
    public NettyReactiveWebServerFactory factory(NioEventLoopGroup eventLoopGroup) {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();

        factory.setServerCustomizers(
                Collections.singletonList(httpServer -> httpServer.tcpConfiguration(
                        tcpServer -> tcpServer.bootstrap(
                                serverBootstrap -> serverBootstrap.group(eventLoopGroup)
                                                                  .channel(NioServerSocketChannel.class)))));

        return factory;
    }

    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    public void futureServiceRun() throws ExecutionException, InterruptedException {
        log.info("run...");

        Future<String> future = asyncService.futureRun();
        log.info("exit : {}", future.isDone());
        log.info("result : {}", future.get());
    }

    @Order(2)
    @EventListener(ApplicationReadyEvent.class)
    public void listenableFutureRun() {
        log.info("run...");

        ListenableFuture<String> future = asyncService.listenableFutureRun();
        future.addCallback(
                s -> log.info("Result : {}", s),
                e -> log.error("Error : {}", e.getMessage())
        );

        log.info("exit");
    }

}
