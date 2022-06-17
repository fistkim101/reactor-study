package com.fistkim.reactorstudy.hotcold;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class HotColdTest {

    @Test
    void hotTest_1() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
        ConnectableFlux<Integer> connectableNumbers = numbers.publish();
        connectableNumbers.connect();
        connectableNumbers.subscribe(num -> System.out.println("subscriber 1 : " + num));
        stopSeconds(5);
        connectableNumbers.subscribe(num -> System.out.println("subscriber 2 : " + num));

        stopSeconds(10);
    }

    @Test
    void autoConnectTest() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
        Flux<Integer> numbersFlux = numbers.publish().autoConnect(2);
        numbersFlux.subscribe(num -> System.out.println("subscriber 1 : " + num));
        stopSeconds(5);
        numbersFlux.subscribe(num -> System.out.println("subscriber 2 : " + num));

        stopSeconds(10);
    }

    @Test
    void refConnectTest() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
        Flux<Integer> numbersFlux = numbers.publish().refCount(2);
        var a = numbersFlux.subscribe(number -> System.out.println("subscriber 1 : " + number));
        var b = numbersFlux.subscribe(number -> System.out.println("subscriber 2 : " + number));
        a.dispose();
        b.dispose();

        stopSeconds(10);
    }

    @Test
    void virtualTimerTest() {
        VirtualTimeScheduler.getOrSet();
        Flux<Integer> numbers = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        StepVerifier.withVirtualTime(() -> numbers)
                .thenAwait(Duration.ofSeconds(12))
                .expectNextCount(10)
                .verifyComplete();
    }

    private void stopSeconds(Integer second) {
        try {
            Thread.sleep(Long.parseLong(second.toString()) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
