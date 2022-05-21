package com.fistkim.reactorstudy.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
public class ExceptionTest {


    @Test
    void retryTest() throws InterruptedException {
        AtomicInteger index = new AtomicInteger();
        Flux<Integer> numbersWithError = Flux.fromIterable(List.of(1, 2, 3))
                .concatWith(Mono.error(new RuntimeException()))
                .onErrorResume(exception -> {
                    if (index.get() == 5) {
                        System.out.println("index equals 5");
                        return Mono.just(10);
                    } else {
                        System.out.println("index < 5");
                        return Mono.error(new RuntimeException());
                    }
                })
                .doOnError(ex -> {
                    index.getAndIncrement();
                })
                .retry()
                .log();

        numbersWithError.subscribe();
        Thread.sleep(10000L);
    }

    @Test
    void doOnErrorTest() {
        Flux<Integer> numbersWithError = Flux.fromIterable(List.of(1, 2, 3)).concatWith(Mono.error(new RuntimeException())).concatWith(Mono.just(4)).doOnError(ex -> {
            System.out.println("exception : " + ex.getClass().getName());
        }).log();

        StepVerifier.create(numbersWithError).expectNext(1, 2, 3).expectError(RuntimeException.class).verify();
    }

    @Test
    void terminateCheck() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbersWithException = numbers.concatWith(Mono.error(RuntimeException::new)).concatWith(Mono.just(4)).log();

        StepVerifier.create(numbersWithException).expectNext(1, 2, 3).expectError().verify();
    }

    @Test
    void onErrorReturnTest() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3)).concatWith(Mono.error(new RuntimeException())).onErrorReturn(4).log();

        StepVerifier.create(numbers).expectNext(1, 2, 3, 4).verifyComplete();
    }

    @Test
    void onErrorReturnTest_2() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(5, 6, 7));

        Flux<Integer> mergedNumbers = numbers_1.concatWith(Mono.error(new RuntimeException())).onErrorReturn(4).concatWith(numbers_2).log();

        StepVerifier.create(mergedNumbers).expectNext(1, 2, 3, 4, 5, 6, 7).verifyComplete();
    }

    @Test
    void onErrorMapTest() {
        Flux<Integer> numbersWithError = Flux.fromIterable(List.of(1, 2, 3)).map(number -> {
            if (number == 2) {
                throw new RuntimeException();
            }

            return number;
        }).onErrorMap(exception -> {
            throw new IllegalArgumentException();
        }).log();

        StepVerifier.create(numbersWithError).expectNext(1).expectError(IllegalArgumentException.class).verify();
    }

    @Test
    void onErrorContinueTest() {
        Flux<Integer> numbersWithError = Flux.fromIterable(List.of(1, 2, 3)).map(number -> {
            if (number == 2) {
                throw new RuntimeException();
            }

            return number;
        }).onErrorContinue((exception, number) -> {
            System.out.println("exception : " + exception.getClass());
            System.out.println("triggered element : " + number);
        }).log();

        StepVerifier.create(numbersWithError).expectNext(1, 3).verifyComplete();
    }

    @Test
    void onErrorResumeTest_1() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> target = numbers.concatWith(Mono.error(new RuntimeException())).onErrorResume(exception -> {
            System.out.println(exception.getClass().getName());
            return Flux.fromIterable(List.of(4, 5, 6));
        }).log();

        StepVerifier.create(target).expectNext(1, 2, 3, 4, 5, 6).verifyComplete();
    }

    @Test
    void onErrorResumeTest_2() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(10, 11, 12));
        Flux<Integer> target = numbers_1.concatWith(Mono.error(new RuntimeException())).concatWith(numbers_2).onErrorResume(exception -> {
            System.out.println(exception.getClass().getName());
            return Flux.fromIterable(List.of(4, 5, 6));
        }).log();

        StepVerifier.create(target).expectNext(1, 2, 3, 4, 5, 6).verifyComplete();
    }

    @Test
    void onErrorResumeTest_3() {
        Flux<Integer> numbers_1 = Flux.fromIterable(List.of(1, 2, 3));
        Flux<Integer> numbers_2 = Flux.fromIterable(List.of(10, 11, 12));
        Flux<Integer> target = numbers_1.concatWith(Mono.error(new RuntimeException())).concatWith(numbers_2).onErrorResume(exception -> {
            System.out.println(exception.getClass().getName());
            return Flux.empty();
        }).log();

        StepVerifier.create(target).expectNext(1, 2, 3).verifyComplete();
    }
}
