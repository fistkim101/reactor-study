package com.fistkim.reactorstudy.pararell;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class PararellTest {

    @Test
    void pararellTest_1() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .map(this::stop1Second)
                .log();

        StepVerifier.create(numbers)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void pararellTest_2() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .publishOn(Schedulers.parallel())
                .map(this::stop1Second)
                .log();

        StepVerifier.create(numbers)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void pararellTest_3() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors : " + availableProcessors);

        ParallelFlux<Integer> numbers = Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::stop1Second)
                .log();

        StepVerifier.create(numbers)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void pararellTest_4() {
        Flux<Integer> numbers = Flux.range(1, 10)
                .flatMap(number -> Mono.just(number)
                        .map(this::stop1Second)
                        .subscribeOn(Schedulers.parallel()))
                .log();

        StepVerifier.create(numbers)
                .expectNextCount(10)
                .verifyComplete();
    }

    private Integer stop1Second(int value) {
        try {
            System.out.println("current value : " + value);
            Thread.sleep(1000L);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
