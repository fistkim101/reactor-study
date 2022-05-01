# reactor-study

### transform

#### map, log
```java
    public Flux<String> toUpperCaseFromFlux(Flux<String> names){
        return names
                .map(String::toUpperCase)
                .log("upperCase")
                .map(String::toLowerCase)
                .log("lowerCase")
                .map(String::toUpperCase)
                .log("upperCase_again");
        }
```
```bash
10:33:20.199 [main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
10:33:20.219 [main] INFO upperCase - | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
10:33:20.223 [main] INFO lowerCase - | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
10:33:20.223 [main] INFO upperCase_again - | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
10:33:20.224 [main] INFO upperCase_again - | request(unbounded)
10:33:20.225 [main] INFO lowerCase - | request(unbounded)
10:33:20.225 [main] INFO upperCase - | request(unbounded)
10:33:20.225 [main] INFO upperCase - | onNext(ALEX)
10:33:20.225 [main] INFO lowerCase - | onNext(alex)
10:33:20.225 [main] INFO upperCase_again - | onNext(ALEX)
ALEX
10:33:20.226 [main] INFO upperCase - | onNext(LEO)
10:33:20.226 [main] INFO lowerCase - | onNext(leo)
10:33:20.226 [main] INFO upperCase_again - | onNext(LEO)
LEO
10:33:20.226 [main] INFO upperCase - | onNext(SIRI)
10:33:20.226 [main] INFO lowerCase - | onNext(siri)
10:33:20.226 [main] INFO upperCase_again - | onNext(SIRI)
SIRI
10:33:20.227 [main] INFO upperCase - | onComplete()
10:33:20.227 [main] INFO lowerCase - | onComplete()
10:33:20.227 [main] INFO upperCase_again - | onComplete()
```

* logger’s own formatter (time, thread, level, message) (https://projectreactor.io/docs/core/release/reference/#_logging_a_sequence)
  * message 는 operator 를 체이닝해서 쓰게 되면 log()를 사용 할 때 어느 operator 에서 찍혔는지 알 수 없기 때문에 보여주는 정보라고 생각하면 된다. 따로 지정해주지 않으면 자동으로 부여해준다.
* map()이 위와 같이 순차적으로 3개가 있다고 해서 실행 단위가 map() 별로 각각 끝나는게 아니라 개별 element 가 각 map 을 모두 순차적으로 통과한다.(stream 과 유사)

<br>

#### map vs flatMap
* Transform the elements emitted by this Flux asynchronously into Publishers, then flatten these inner publishers into a single Flux through merging, which allow them to interleave.

