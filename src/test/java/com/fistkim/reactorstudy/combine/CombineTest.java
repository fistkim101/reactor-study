package com.fistkim.reactorstudy.combine;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

public class CombineTest {

    @Test
    void zipWithTest() {
        Flux<String> names = Flux.fromIterable(List.of("leo", "siri", "jbl"));
        Flux<String> capital = Flux.fromIterable(List.of("A", "B"));

        Flux<String> ziped = names.zipWith(capital, (str1, str2) -> str1 + str2).log();

        StepVerifier.create(ziped)
                .expectNext("leoA", "siriB")
                .verifyComplete();
    }

    @Test
    void simpleZipTest() {
        Flux<String> names = Flux.fromIterable(List.of("leo", "siri", "jbl"));
        Flux<String> capital = Flux.fromIterable(List.of("A", "B"));

        Flux<String> ziped = Flux.zip(names, capital)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
        StepVerifier.create(ziped)
                .expectNext("leoA", "siriB")
                .verifyComplete();
    }

    @Test
    void zipSameCountTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6));

        Flux<Integer> ziped = Flux.zip(numbers_1, numbers_2, (num1, num2) -> num1 + num2).log();
        StepVerifier.create(ziped)
                .expectNext(5, 7, 9)
                .verifyComplete();
    }

    @Test
    void zipNotSameCountTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 1, 1));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(1, 1));

        Flux<Integer> ziped = Flux.zip(numbers_1, numbers_2, (num1, num2) -> num1 + num2).log();
        StepVerifier.create(ziped)
                .expectNext(2, 2)
                .verifyComplete();
    }


    @Test
    void concatWithTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6));

        StepVerifier.create(numbers_1.concatWith(numbers_2).log())
                .expectNext(1, 2, 3, 4, 5, 6)
                .verifyComplete();
    }

    @Test
    void concatTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6));

        StepVerifier.create(Flux.concat(numbers_1, numbers_2, Mono.just("string")).log())
                .expectNext(1, 2, 3, 4, 5, 6, "string")
                .verifyComplete();
    }

    @Test
    void mergeTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3)).delayElements(Duration.ofMillis(100));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6)).delayElements(Duration.ofMillis(110));

        Flux<Integer> merged = Flux.merge(numbers_1, numbers_2).log();

        StepVerifier.create(merged)
                .expectNext(1, 4, 2, 5, 3, 6)
                .verifyComplete();
    }

    @Test
    void mergeWithTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3)).delayElements(Duration.ofMillis(100));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6)).delayElements(Duration.ofMillis(110));

        Flux<Integer> merged = numbers_1.mergeWith(numbers_2).log();

        StepVerifier.create(merged)
                .expectNext(1, 4, 2, 5, 3, 6)
                .verifyComplete();
    }

    @Test
    void mergeSequentialTest() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3)).delayElements(Duration.ofMillis(100));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(4, 5, 6)).delayElements(Duration.ofMillis(110));

        Flux<Integer> merged = Flux.mergeSequential(numbers_1, numbers_2).log();

        StepVerifier.create(merged)
                .expectNext(1, 2, 3, 4, 5, 6)
                .verifyComplete();
    }


}
