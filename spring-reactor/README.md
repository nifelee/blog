# Spring Reactor

## 비동기 기술

### Runnable
```java
ExecutorService es = Executors.newSingleThreadExecutor();
es.execute(() -> {
    sleep(2);
    log.info("execute");
});

log.info("Exit");
```

실행결과
```text
15:22:17.163 [main] INFO com.nifelee.springreactor.runnable.RunnableTest - Exit
15:22:19.166 [pool-1-thread-1] INFO com.nifelee.springreactor.runnable.RunnableTest - execute
```

---

### Future
```java 
Future<String> future = es.submit(() -> {
    sleep(2);
    log.debug("execute");
    return "Hello Future";
});

log.info("Exit");
log.info(future.get());
```

실행결과
```text
15:24:14.762 [main] INFO com.nifelee.springreactor.future.FutureTest - Exit
15:24:16.765 [pool-1-thread-1] DEBUG com.nifelee.springreactor.future.FutureTest - execute
15:24:16.767 [main] INFO com.nifelee.springreactor.future.FutureTest - Hello Future
```

### FutureTask
```java 
FutureTask<String> futureTask = new FutureTask<>(() -> {
    sleep(2L);
    log.debug("execute");
    return "Hello Future";
});

es.execute(futureTask);
log.info("Exit");
log.info(futureTask.get());
```

실행결과
```text
15:35:22.804 [main] INFO com.nifelee.springreactor.future.FutureTaskTest - Exit
15:35:24.808 [pool-1-thread-1] DEBUG com.nifelee.springreactor.future.FutureTaskTest - execute
15:35:24.809 [main] INFO com.nifelee.springreactor.future.FutureTaskTest - Hello Future0:w
```

### Anonymous FutureTask
```java 
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
        }
    }
};

es.execute(futureTask);
log.info("Exit");
```

실행결과
```text
15:38:56.130 [main] INFO com.nifelee.springreactor.future.FutureTaskTest - Exit
15:38:58.131 [pool-1-thread-1] DEBUG com.nifelee.springreactor.future.FutureTaskTest - execute
15:38:58.132 [pool-1-thread-1] INFO com.nifelee.springreactor.future.FutureTaskTest - Hello Future
```

### CallbackFutureTask
```java 
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

CallbackFutureTask futureTask = new CallbackFutureTask(() -> {
        sleep(2L);

        //if (true) throw new RuntimeException("Error !!");

        log.debug("execute");
        return "Hello Future";
    },
    s -> log.info("Result : {}", s),
    e -> log.error("Error : {}", e.getMessage())
);

es.execute(futureTask);
log.info("Exit");
```

실행결과
```text
16:07:29.471 [main] INFO com.nifelee.springreactor.future.CallbackFutureTaskTest - Exit
16:07:31.475 [pool-1-thread-1] DEBUG com.nifelee.springreactor.future.CallbackFutureTaskTest - execute
16:07:31.476 [pool-1-thread-1] INFO com.nifelee.springreactor.future.CallbackFutureTaskTest - Result : Hello Future

# RuntimeException
16:07:54.642 [main] DEBUG reactor.core.publisher.Hooks - Enabling stacktrace debugging via onOperatorDebug
16:07:54.650 [main] INFO com.nifelee.springreactor.future.CallbackFutureTaskTest - Exit
16:07:56.656 [pool-1-thread-1] ERROR com.nifelee.springreactor.future.CallbackFutureTaskTest - Error : Error !!
```

---
