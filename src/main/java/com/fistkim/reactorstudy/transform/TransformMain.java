package com.fistkim.reactorstudy.transform;

import reactor.core.publisher.Flux;

import java.util.List;

public class TransformMain {
    public static void main(String[] args) {

        TransformService transformService = new TransformService();

        Flux<String> names = Flux.fromIterable(List.of("alex", "leo", "siri"));
        transformService.toUpperCaseFromFlux(names)
                .subscribe(System.out::println);
    }
}
