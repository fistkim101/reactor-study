package com.fistkim.reactorstudy.sideeffect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class SideEffectTest {

    @Test
    void sideEffect() {
        Flux<Integer> numbers = Flux.fromIterable(List.of(1, 2, 3))
                .doOnEach(number -> System.out.println("doOnEach : " + number))
                .doOnNext(number -> System.out.println("doOnNext : " + number))
                .doOnSubscribe(number -> System.out.println("doOnSubscribe : " + number))
                .log()
                .doOnComplete(() -> System.out.println("doOnComplete"));

        StepVerifier.create(numbers)
                .assertNext(number -> {
                    Assertions.assertEquals(1, number);
                })
                .assertNext(number -> {
                    Assertions.assertEquals(2, number);
                })
                .assertNext(number -> {
                    Assertions.assertEquals(3, number);
                })
                .verifyComplete();
    }

}
