package com.fistkim.reactorstudy.backpressure;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class BackPressure {

    @Test
    void onBackPressureDrop() throws InterruptedException {

        AtomicInteger index = new AtomicInteger();

        Flux.range(1, 100)
                .onBackpressureDrop(element -> {
                    System.out.println("dropped element : " + element);
                })
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        if (value < 50) {
                            request(1);
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        super.hookOnCancel();
                    }
                });

        Thread.sleep(5000L);
    }


    @Test
    void onBackPressureBuffer() throws InterruptedException {

        AtomicInteger index = new AtomicInteger();

        Flux.range(1, 100)
                .onBackpressureBuffer(1, element -> {
                    System.out.println("last buffered element : " + element);
                })
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("hookOnNext : " + value);
                        if (value < 50) {
                            request(1);
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        super.hookOnCancel();
                    }
                });

        Thread.sleep(5000L);
    }

}
