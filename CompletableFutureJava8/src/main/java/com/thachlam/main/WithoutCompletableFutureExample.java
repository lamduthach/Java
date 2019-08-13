package com.thachlam.main;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thach
 */
public class WithoutCompletableFutureExample {

    public static void main(String[] args) {
        WithoutCompletableFutureExample t = new WithoutCompletableFutureExample();
        t.runTest();
    }

    public void runTest() {
        System.out.println("runTest WithoutCompletableFutureExample");
        long startTime = System.currentTimeMillis();
        List<Car> cars = getCars();
        cars.forEach(car -> {
            float rating = createRating(car.manufacturerId);
            car.setRating(rating);
        });

        cars.forEach(System.out::println);

        long endTime = System.currentTimeMillis();

        System.out.println("Time " + (endTime - startTime) + " ms");
    }

    private float createRating(int manufacturer) {
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
    }

    private List<Car> getCars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1, 3, "Fiesta", 2017));
        carList.add(new Car(2, 7, "Camry", 2014));
        carList.add(new Car(3, 2, "M2", 2008));
        return carList;
    }

    private void longTaskDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}
