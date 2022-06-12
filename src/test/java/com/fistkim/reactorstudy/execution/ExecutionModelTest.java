package com.fistkim.reactorstudy.execution;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

public class ExecutionModelTest {

    @Test
    void publishOnTest() {

        Flux<String> alphabet1 = Flux.fromIterable(List.of("a", "b", "c"))
                .map(this::toUpperCase);
        Flux<String> alphabet2 = Flux.fromIterable(List.of("d", "e", "f"))
                .map(this::toUpperCase);

        StepVerifier.create(alphabet1.mergeWith(alphabet2).log())
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void publishOnTestAsync1() {

        Flux<String> alphabet1 = Flux.fromIterable(List.of("a", "b", "c"))
                .publishOn(Schedulers.parallel())
                .map(this::toUpperCase);
        Flux<String> alphabet2 = Flux.fromIterable(List.of("d", "e", "f"))
                .publishOn(Schedulers.parallel())
                .map(this::toUpperCase);

        StepVerifier.create(alphabet1.mergeWith(alphabet2).log())
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void publishOnTestAsync2() {

        Flux<String> alphabet1 = Flux.fromIterable(List.of("a", "b", "c"))
                .subscribeOn(Schedulers.parallel())
                .map(this::toUpperCase);
        Flux<String> alphabet2 = Flux.fromIterable(List.of("d", "e", "f"))
                .subscribeOn(Schedulers.parallel())
                .map(this::toUpperCase);

        StepVerifier.create(alphabet1.mergeWith(alphabet2).log())
                .expectNextCount(6)
                .verifyComplete();
    }

    private String toUpperCase(String value) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value.toUpperCase();
    }

}
