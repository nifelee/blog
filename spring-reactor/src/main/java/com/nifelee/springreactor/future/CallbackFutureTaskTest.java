package com.nifelee.springreactor.future;

import com.nifelee.springreactor.AbstractSleep;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class CallbackFutureTaskTest extends AbstractSleep {

    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        CallbackFutureTask futureTask = new CallbackFutureTask(() -> {
                sleep(2L);

                if (true) throw new RuntimeException("Error !!");

                log.debug("execute");
                return "Hello Future";
            },
            s -> log.info("Result : {}", s),
            e -> log.error("Error : {}", e.getMessage())
        );

        es.execute(futureTask);
        log.info("Exit");
        es.shutdown();
    }

}
