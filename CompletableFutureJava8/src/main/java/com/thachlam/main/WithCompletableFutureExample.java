package com.thachlam.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 *
 * @author Thach
 */
public class WithCompletableFutureExample {
    // CF api explain -> http://codeflex.co/java-multithreading-completablefuture-explained/

    public static void main(String[] args) {
        WithoutCompletableFutureExample without = new WithoutCompletableFutureExample();
        without.runTest();
        
        WithCompletableFutureExample with = new WithCompletableFutureExample();
        with.runTest();
    }

    public void runTest() {
        long startTime = System.currentTimeMillis();

        getCars().thenCompose(cars -> {
            List<CompletableFuture<Car>> updatedCars = cars.stream()
                    .map(car -> createRating(car.manufacturerId).thenApply(r -> {
                car.setRating(r);
                return car;
            })).collect(Collectors.toList());
            
            CompletableFuture<Void> allCF = CompletableFuture.allOf(updatedCars.toArray(new CompletableFuture[updatedCars.size()]));

            return allCF.thenApply((t) -> {
                return updatedCars.stream().map(CompletableFuture::join).collect(Collectors.toList());
            });
        }).whenComplete((cars, ex) -> {
            if (ex == null) {
                cars.forEach(System.out::println);
            } else {
                throw new RuntimeException(ex);
            }
        }).join();

        long endTime = System.currentTimeMillis();

        System.out.println("Time " + (endTime - startTime) + " ms");
    }

    CompletableFuture<Float> createRating(int manufacturer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                longTaskDelay();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            switch (manufacturer) {
                case 2:
                    return 4f;
                case 3:
                    return 4.1f;
                case 7:
                    return 4.2f;
                default:
                    return 5f;
            }
        }).exceptionally(ex -> -1f);
    }

    CompletableFuture<List<Car>> getCars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1, 3, "Fiesta", 2017));
        carList.add(new Car(2, 7, "Camry", 2014));
        carList.add(new Car(3, 2, "M2", 2008));
        return CompletableFuture.supplyAsync(() -> {
            return carList;
        });
    }

    private void longTaskDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}
