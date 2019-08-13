package com.thachlam.main;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Thach
 */
public class CompletableFutureApiExamples {
    // runAsync implements Runnable interface, creates new thread and not allows to return a value.

    // suplyAsync implements Supplier interface, creates new thread in the thread pool and returns a value of a parameterized type.
    // Method           Async method            Arguments                                   Returns
    // thenRun()	thenRunAsync()	        –                                           –
    // thenAccept()	thenAcceptAsync()	Result of previous stage                    –
    // thenApply()	thenApplyAsync()	Result of previous stage                    Result of current stage
    // thenCompose()	thenComposeAsync()	Result of previous stage                    Future result of current stage
    // thenCombine()	thenCombineAsync()	Result of two previous stages               Result of current stage
    // whenComplete()	whenCompleteAsync()	Result or exception from previous stage     –
    public String method1() {
        System.out.println("Running method1... thread id: " + Thread.currentThread().getId());
        String returnValue = "Hello from method1!";
        System.out.println("Return value: " + returnValue);
        return returnValue + " ThreadId:" + Thread.currentThread().getId();
    }

    public void method2(String arg) {
        System.out.println("Running method2... thread id: " + Thread.currentThread().getId());
        System.out.println("Incoming argument: " + arg);
    }

    public String method3(String arg) {
        System.out.println("Running method3... thread id: " + Thread.currentThread().getId());
        System.out.println("Incoming argument: " + arg);
        String returnValue = "Hello from method3!";
        System.out.println("Return value: " + returnValue);
        return returnValue;
    }

    public CompletableFuture<String> method4(String arg) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Running method4... thread id: " + Thread.currentThread().getId());
            return arg + "  - Well, hello to you too!";
        });
    }

    public CompletableFuture<String> method5(String arg) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Running method5... thread id: " + Thread.currentThread().getId());
            return arg + arg + arg;
        });
    }

    public static void main(String[] args) {
        CompletableFutureApiExamples cf = new CompletableFutureApiExamples();
        System.out.println("suplyAsyncAndThenAccept");
        cf.suplyAsyncAndThenAccept();
        System.out.println("-------------------------");
        System.out.println("thenApply");
        cf.thenApply();
        System.out.println("-------------------------");
        System.out.println("thenCompose");
        cf.thenCompose();
        System.out.println("-------------------------");
        System.out.println("affOf");
        cf.affOf();
        System.out.println("-------------------------");
        System.out.println("exceptionally");
        cf.exceptionally();
        System.out.println("-------------------------");
        System.out.println("joinOrGet");
        cf.joinOrGet();
    }

    // we will see method1 running in a separate thread, after its completion the result has been passed to method2.
    void suplyAsyncAndThenAccept() {
        System.out.println("Main thread running... thread id: " + Thread.currentThread().getId());
        CompletableFuture cf = CompletableFuture.supplyAsync(() -> method1()).thenAccept((t) -> {
            method2(t);
            System.out.println(t.substring(t.indexOf(":") + 1, t.length()));

        });
        System.out.println("Main thread finished");
        Map<Thread, StackTraceElement[]> curr = Thread.getAllStackTraces();
        curr.entrySet().stream().map((entry) -> entry.getKey()).forEachOrdered((key) -> {
            System.out.println("ThreadId :" + key.getId());
        });
    }

    // thenApply used for passing values from one callback to another. Thus we can chain multiple methods
    void thenApply() {
        CompletableFuture.supplyAsync(() -> method1()).thenApply((str) -> method3(str)).thenAccept((str) -> method2(str));
    }

    // thenCompose used for scenarios where there is a need to call different methods that return CompletableFuture and combine the result.
    void thenCompose() {
        try {
            System.out.println("Main thread running... thread id: " + Thread.currentThread().getId());
            CompletableFuture<String> finalResult = CompletableFuture.supplyAsync(this::method1).thenCompose(this::method4);
            System.out.println(finalResult.get());
            System.out.println("Main thread finished");
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    // Sometimes need to wait for all CompletableFuture calls to complete and only then to run some other task.
    void affOf() {
        try {
            System.out.println("Main thread running... thread id: " + Thread.currentThread().getId());
            CompletableFuture<Void> all = CompletableFuture.allOf(method4("Hello!"), method5("Hi!"));
            CompletableFuture cf = all.thenRun(()
                    -> System.out.println("Running this after all ... thread id: " + Thread.currentThread().getId()));
            cf.get();
            System.out.println("Main thread finished");
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    // exceptionally is used for error handling. In this crazy world of multi-threading exceptions definitely will happen
    void exceptionally() {
        CompletableFuture.supplyAsync(() -> {
            try {
                int i = 1 / 0;  //  Do not try it at home :)))
            } catch (Exception e) {
                throw new RuntimeException("Division by zero!!!", e);
            }
            return "Hello World!";
        }).thenAcceptAsync(s -> {
            System.out.println("Result: " + s);
        }).exceptionally(e -> {
            System.err.println("Error! " + e.getMessage());
            return null;
        });
    }

    // These methods are used to get the result from CompletableFuture stage. 
    // The difference between join and get is that `join` returns the result when a task completed or throws unchecked exception if a task completed exceptionally 
    // while get waits if necessary for the future to complete
    void joinOrGet() {
        String joinedFuture = Stream.of(printMsg("Hello"), printMsg("Java"), printMsg("Concurrency!"))
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        System.out.println(joinedFuture);
    }

    public CompletableFuture<String> printMsg(String arg) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(arg);
            return arg;
        });
    }
}
