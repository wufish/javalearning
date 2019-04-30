package com.wufish.javalearning.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * @Author wzj
 * @Create time: 2019/04/30 14:27
 * @Description: 为了突出与Netflix Hystrix的一些差异：
 * <p>
 * 1. 在Hystrix中，对外部系统的调用必须包含在HystrixCommand中。相比之下，该库提供了高阶函数（装饰器），以增强任何功能接口，
 * lambda表达式或带有断路器，速率限制器或隔板的方法参考。此外，库提供装饰器来重试失败的调用或缓存调用结果。
 * 您可以在任何功能接口，lambda表达式或方法引用上堆叠多个装饰器。这意味着，
 * 您可以将Bulkhead，RateLimiter和Retry装饰器与CircuitBreaker装饰器结合使用。优点是您可以选择所需的装饰器，而不是其他任何东西。
 * 任何修饰函数都可以使用CompletableFuture或RxJava同步或异步执行。
 * <p>
 * 2. 默认情况下，Hystrix将执行结果存储在10个1秒的窗口桶中。如果传递1秒窗口桶，则会创建一个新桶，并删除最旧的桶。
 * 该库将执行结果存储在Ring Bit Buffer中，而没有统计滚动时间窗口。成功的呼叫存储为0位，
 * 失败的呼叫存储为1位。Ring Bit Buffer具有可配置的固定大小，并将这些位存储在long []数组中，与布尔数组相比，这节省了内存。
 * 这意味着Ring Bit Buffer只需要一个包含16个长（64位）值的数组来存储1024个调用的状态。
 * 优点是这个CircuitBreaker可以为低频和高频后端系统提供开箱即用的功能，因为在传递时间窗口时不会丢弃执行结果。
 * <p>
 * 3. Hystrix仅在处于半开状态时执行单次执行，以确定是否关闭CircuitBreaker。该库允许执行可配置数量的执行，
 * 并将结果与​​可配置阈值进行比较，以确定是否关闭CircuitBreaker。
 * <p>
 * 4. 该库提供自定义RxJava运算符来装饰任何Observable或Flowable与Circuit Breaker，Bulkhead或Ratelimiter一起使用。
 * <p>
 * 5. Hystrix和此库发出一系列事件，这些事件对系统操作员有用，可监控有关执行结果和延迟的指标。
 */
public class UsageGudide {

    public void CircuitBreaker() {
        // CircuitBreaker - 断路器
        // 使用默认断路器
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        // 自定义断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)// 故障率阈值百分比，超过该值，断路器开始断路调用
                .waitDurationInOpenState(Duration.ofMillis(1000))// 等待持续时间，在切换半开状态之前断路器应该保持的时间
                .ringBufferSizeInHalfOpenState(2)// 半开状态，断路器环形缓冲区大小
                .ringBufferSizeInClosedState(2)// 关闭状态，断路器环形缓冲区大小
                .recordExceptions(IOException.class, TimeoutException.class)//增加故障技术的异常列表
                //.ignoreExceptions(BusinessException.class, OtherBusinessException.class)// 忽略的异常
                .build();
        CircuitBreakerRegistry circuitBreakerRegistry2 = CircuitBreakerRegistry.of(circuitBreakerConfig);
        // 获取断路器
        CircuitBreaker circuitBreaker = circuitBreakerRegistry2.circuitBreaker("otherName");
        CircuitBreaker circuitBreaker2 = circuitBreakerRegistry2.circuitBreaker("otherName", circuitBreakerConfig);

        // 直接创建CircuitBreaker实例
        CircuitBreaker defaultCircuitBreaker = CircuitBreaker.ofDefaults("testName");
        CircuitBreaker customCircuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);

        // Example
        // 装饰函数
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(defaultCircuitBreaker,
                () -> "This can be any method which returns: 'Hello");
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");
        result.isSuccess();
        String s = result.get();//This can be any method which returns: 'Hello world'
    }
}
