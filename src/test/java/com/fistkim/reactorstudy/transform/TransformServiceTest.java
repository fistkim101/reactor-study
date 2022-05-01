package com.fistkim.reactorstudy.transform;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Random;

class TransformServiceTest {

    TransformService transformService = new TransformService();
    Flux<String> names = Flux.fromIterable(List.of("alex", "leo", "siri"));

    @Test
    void toUpperCaseFromFlux() {
        StepVerifier.create(transformService.toUpperCaseFromFlux(names))
                .expectNext("ALEX", "LEO", "SIRI")
                .verifyComplete();
    }

    @Test
    void splitAllByFlatMap() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        numbers.flatMap(number -> {
                    int delay = new Random().nextInt(10);
                    return Mono.just(number + 1).delayElement(Duration.ofMillis(delay));
                })
                .log()
                .subscribe();

//        transformService.splitAllByFlatMap(names).subscribe();
//        StepVerifier.create(transformService.splitAllByFlatMap(names))
//                .verifyComplete();
    }

    @Test
    void defaultIfEmpty() {
        Flux<String> names = Flux.fromIterable(List.of("alex", "leo", "siri"));
        Flux<String> emptyFlux = Flux.empty();
        Flux<String> targetFlux = emptyFlux.defaultIfEmpty("leo").log();

        StepVerifier.create(targetFlux)
                .expectNext("leo")
                .verifyComplete();
    }

    @Test
    void switchIfEmpty() {
        Flux<String> names = Flux.fromIterable(List.of("alex", "leo", "siri"));
        Flux<String> emptyFlux = Flux.empty();
        Flux<String> targetFlux = emptyFlux.switchIfEmpty(names).log();

        StepVerifier.create(targetFlux)
                .expectNext("alex", "leo", "siri")
                .verifyComplete();
    }

    @Test
    void transformTest() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Flux<String> stringNumbers = numbers.flatMap(number -> Mono.just(number.toString()));
        Flux<String> stringNumbers2 = names.transform(number -> Mono.just(number.toString()));

        StepVerifier.create(stringNumbers2)
                .expectNext("1")
                .expectComplete();
    }

    @Test
    void concatMapTest() throws InterruptedException {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        long delay = 1000L;
        numbers.concatMap(number -> Mono.just(number).delayElement(Duration.ofMillis(delay)))
                .log()
                .subscribe();
        Thread.sleep(15000L);
    }

    @Test
    void flatMapTest() throws InterruptedException {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        long delay = 1000L;
        numbers.flatMapSequential(number -> Mono.just(number).delayElement(Duration.ofMillis(delay)))
                .log()
                .subscribe();

        Thread.sleep(15000L);
    }

    @Test
    void flatMapManyTest() {
        Mono<String> nameMono = Mono.just("leo");

        Flux<String> chars = nameMono.flatMapMany(name -> Flux.fromIterable(List.of(name.split("")))).log();

        StepVerifier.create(chars)
                .expectNext("l", "e", "o")
                .verifyComplete();
    }

}