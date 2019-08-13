package com.thachlam.main;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Thach
 */
public class FutureExample {

    public static void main(String[] args) {
        FutureExample f = new FutureExample();
        f.runExample1();
        f.runExample2();
        f.runCFExample();
    }

    void runExample1() {
        ExecutorService service = Executors.newFixedThreadPool(10);

        // submit task and accept the placeholder object for return value
        Future<Integer> future = service.submit(new Task());

        try {
            // get the task return value
            Integer result = future.get(); // blocking IO
            System.out.println("Result " + result);
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
//        service.shutdown();
    }

    static class Task implements Callable<Integer> {

        @Override
        public Integer call() {
            return new Random().nextInt();
        }
    }

    void runExample2() {
        try {
            ExecutorService service = Executors.newFixedThreadPool(10);

            Future<Object> future1 = service.submit(getOrderTask());
            Object ob1 = future1.get(); // blocking

            Future<Object> future2 = service.submit(performPaymentTask(ob1));
            Object ob2 = future2.get(); // blocking

            Future<Object> future3 = service.submit(sendEmailTask(ob2));
            Object ob3 = future3.get(); // blocking
        } catch (InterruptedException | ExecutionException ex) {
           System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    Callable getOrderTask() {
        return (Callable) Object::new;
    }

    Callable performPaymentTask(Object ob) {
        return (Callable) Object::new;
    }

    Callable sendEmailTask(Object ob) {
        return (Callable) Object::new;
    }

    void runCFExample() {
        ExecutorService cpuBound = Executors.newFixedThreadPool(4);
        ExecutorService ioBound = Executors.newFixedThreadPool(10);
        CompletableFuture.supplyAsync(() -> getOrderTask(), cpuBound)
                .thenApplyAsync((ob) -> performPaymentTask(ob), ioBound)
                .thenAccept((ob) -> sendEmailTask(ob));
    }
}
