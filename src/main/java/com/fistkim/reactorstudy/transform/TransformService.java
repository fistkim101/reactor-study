package com.fistkim.reactorstudy.transform;

import reactor.core.publisher.Flux;

import java.util.List;

public class TransformService {

    public Flux<String> toUpperCaseFromFlux(Flux<String> names) {
        return names
                .map(String::toUpperCase)
                .log("upperCase")
                .map(String::toLowerCase)
                .log("lowerCase")
                .map(String::toUpperCase)
                .log("upperCase_again");
    }

    public Flux<String> splitAllByFlatMap(Flux<String> names) {
        return names
                .flatMap(name -> Flux.fromIterable(List.of(name.split(""))))
                .log();
    }

}
