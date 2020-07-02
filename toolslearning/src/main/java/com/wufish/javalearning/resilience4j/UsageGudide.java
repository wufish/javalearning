package com.wufish.javalearning.resilience4j;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.internal.AtomicRateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedRunnable;
import io.vavr.Predicates;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.cache.Caching;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.*;

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
@Slf4j
public class UsageGudide {

    public static void CircuitBreaker() {
        // CircuitBreaker - 断路器
        // 1. 使用注册器注册器创建 CircuitBreaker，map 缓存
        //  1.1 默认配置创建注册器
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        //  1.2 自定义断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)// 关闭状态，断路器环形缓冲区大小
                .failureRateThreshold(50)// 故障率阈值百分比，超过该值，断路器开始断路调用
                .waitDurationInOpenState(Duration.ofMillis(1000))// 等待持续时间，在切换半开状态之前断路器应该保持的时间
                .ringBufferSizeInHalfOpenState(2)// 半开状态，断路器环形缓冲区大小
                .recordExceptions(IOException.class, TimeoutException.class)//增加故障技术的异常列表
                //.ignoreExceptions(BusinessException.class, OtherBusinessException.class)// 忽略的异常
                // 默认所有异常都被认定为失败事件，你可以定制Predicate的test检查，实现选择性记录，只有该函数返回为true时异常才被认定为失败事件。
                .recordException(throwable -> Match(throwable).of(
                        Case($(instanceOf(WebServiceException.class)), true),
                        Case($(), false)))
                .build();
        CircuitBreakerRegistry circuitBreakerRegistry2 = CircuitBreakerRegistry.of(circuitBreakerConfig);
        //  1.3 获取断路器
        CircuitBreaker circuitBreaker = circuitBreakerRegistry2.circuitBreaker("otherName");
        CircuitBreaker circuitBreaker2 = circuitBreakerRegistry2.circuitBreaker("otherName", circuitBreakerConfig);

        // 2. 直接创建CircuitBreaker实例
        CircuitBreaker defaultCircuitBreaker = CircuitBreaker.ofDefaults("testName");
        CircuitBreaker customCircuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);

        // Example
        // 1. 装饰函数
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(defaultCircuitBreaker,
                () -> "This can be any method which returns: 'Hello");
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");
        System.out.println(result.isSuccess());
        System.out.println(result.get());//This can be any method which returns: 'Hello world'

        CircuitBreaker otherTestCircuitBreaker = CircuitBreaker.ofDefaults("otherTestName");
        CheckedFunction1<Object, String> decoratedFunction =
                CircuitBreaker.decorateCheckedFunction(otherTestCircuitBreaker, (input) -> input + " worlds");
        Try<String> chainResult = Try.of(decoratedSupplier).mapTry(decoratedFunction);
        System.out.println(chainResult.isSuccess());
        System.out.println(chainResult.get());

        // Simulate a failure attempt
        System.out.println("\r\nSimulate a failure attempt");
        circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .build();
        circuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);

        // 2. Simulate a failure attempt
        circuitBreaker.onError(0, TimeUnit.SECONDS, new RuntimeException());
        // CircuitBreaker is still CLOSED, because 1 failure is allowed
        System.out.println(circuitBreaker.getState());
        // Simulate a failure attempt
        circuitBreaker.onError(0, TimeUnit.SECONDS, new RuntimeException());
        // CircuitBreaker is OPEN, because the failure rate is above 50%
        System.out.println(circuitBreaker.getState());

        CheckedFunction0<String> checkedFunction0 = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () ->
                "Hello");
        // When I decorate my function and invoke the decorated function
        result = Try.of(CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "Hello"))
                .map(value -> value + " world");

        // Then the call fails, because CircuitBreaker is OPEN
        System.out.println(result.isFailure());
        // Exception is CircuitBreakerOpenException
        System.out.println(result.failed().get().getMessage());

        // 3. 重启, 返回原始状态，丢失当前所有指标
        circuitBreaker.reset();

        // 4. 从调用前异常中恢复
        // 5. 从调用后异常中恢复, 熔断器降级
        Try<String> recover = Try.of(checkedFunction0).recover(throwable -> "hello recovery");

        // 6. 自定义异常处理
        // 默认所有异常都被认定为失败事件，你可以定制Predicate的test检查，实现选择性记录，只有该函数返回为true时异常才被认定为失败事件。
        CircuitBreakerConfig exceptionBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .ringBufferSizeInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .recordFailure(throwable -> Match(throwable).of(
                        Case($(Predicates.instanceOf(WebServiceException.class)), true),
                        Case($(), false)))
                .build();

        // 7. 事件处理
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> log.info(""))
                .onError(event -> log.info(""))
                .onIgnoredError(event -> log.info(""))
                .onReset(event -> log.info(""))
                .onStateTransition(event -> log.info(""));
        // 监听所有事件
        circuitBreaker.getEventPublisher()
                .onEvent(event -> log.info(""));

        // 8. 监控
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        // Returns the current failure rate in percentage.
        float failureRate = metrics.getFailureRate();
        // Returns the current number of buffered calls.
        int bufferedCalls = metrics.getNumberOfBufferedCalls();
        // Returns the current number of failed calls.
        int failedCalls = metrics.getNumberOfFailedCalls();
        // Returns the current number of not permitted calls when the CircuitBreaker is open.
        long numberOfNotPermittedCalls = metrics.getNumberOfNotPermittedCalls();
    }

    public static void RateLimiter() {
        // For example you want to restrict the calling rate of some method to be not higher than 10 req/ms.
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(10)// 频次阈值
                .limitRefreshPeriod(Duration.ofMillis(1))// 限制刷新周期，每个周期限速器将其权限计数设置为limitForPeriod值
                .timeoutDuration(Duration.ofMillis(25))//默认等待权限持续时间。
                .build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        // Use registry
        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("backend");
        RateLimiter rateLimiterWithCustomConfig = rateLimiterRegistry.rateLimiter("backend#2", config);

        // Or create RateLimiter directly
        RateLimiter rateLimiter = RateLimiter.of("NASDAQ :-)", config);

        CheckedRunnable restrictedCall = RateLimiter.decorateCheckedRunnable(rateLimiter, () -> {
            System.out.println("");
        });
        Try.run(restrictedCall)
                .andThenTry(restrictedCall)
                .onFailure(throwable -> log.info("Wait before call it again :)"));

        Supplier<Integer> integerSupplier = RateLimiter.decorateSupplier(rateLimiter, () -> 1);

        // durring second refresh cycle limiter will get 100 permissions
        // 新的超时持续时间不会影响当前正在等待权限的线程。
        // 新限制不会影响当前期间的权限，仅适用于下一个限制。
        rateLimiter.changeLimitForPeriod(100);

        // 2. 事件处理
        rateLimiter.getEventPublisher()
                .onSuccess(event -> log.info(""))
                .onFailure(event -> log.info(""));

        RateLimiter.Metrics metrics = rateLimiter.getMetrics();
        int numberOfThreadsWaitingForPermission = metrics.getNumberOfWaitingThreads();
        int availablePermissions = metrics.getAvailablePermissions();

        AtomicRateLimiter atomicLimiter = new AtomicRateLimiter("", RateLimiterConfig.custom().build());
    }

    public static void TimeLimiter() throws Exception {
        // For example, you want to restrict the execution of a long running task to 60 seconds.
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(60))
                .cancelRunningFuture(true)
                .build();

        // Create TimeLimiter
        TimeLimiter timeLimiter = TimeLimiter.of(config);

        // Run your call to BackendService.doSomething() asynchronously
        Supplier<CompletableFuture<Integer>> futureSupplier = () -> CompletableFuture.supplyAsync(() -> 1);

        // Either execute the future
        Integer result = timeLimiter.executeFutureSupplier(futureSupplier);

        // Or decorate your supplier so that the future can be retrieved and executed upon
        Callable restrictedCall = TimeLimiter
                .decorateFutureSupplier(timeLimiter, futureSupplier);

        Try.of(restrictedCall::call)
                .onFailure(throwable -> log.info("A timeout possibly occurred."));

        Supplier<CompletableFuture<Integer>> futureSupplier2 = () -> CompletableFuture.supplyAsync
                (() -> 1);

        Callable restrictedCall2 = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

        // Decorate the restricted callable with a CircuitBreaker
        Callable chainedCallable = CircuitBreaker.decorateCallable(null, restrictedCall2);

        Try.of(chainedCallable::call)
                .onFailure(throwable -> log.info("We might have timed out or the circuit breaker has opened."));
    }

    public static void Bulkhead() {
        // 舱壁 - 限制并行执行的数量，隔离和减少 后端调用下游依赖，对于cpu型任务，只减少负载
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();

        // Create a custom configuration for a Bulkhead
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(150)// 允许的最大并行执行量
                .maxWaitDuration(Duration.ofMillis(100))// 尝试进入饱和舱壁时可以阻止线程的最大时间
                .build();

        // Create a BulkheadRegistry with a custom global configuration
        BulkheadRegistry registry = BulkheadRegistry.of(config);

        // Get-Or-Create a Bulkhead from the registry - bulkhead will be backed by the default config
        Bulkhead bulkhead1 = registry.bulkhead("foo");

        // Get-Or-Create a Bulkhead from the registry, use a custom configuration when creating the bulkhead
        BulkheadConfig custom = BulkheadConfig.custom()
                .maxWaitDuration(Duration.ofMillis(0))
                .build();

        Bulkhead bulkhead2 = registry.bulkhead("bar", custom);

        Bulkhead bulkhead11 = Bulkhead.ofDefaults("foo");

        Bulkhead bulkhead22 = Bulkhead.of("bar",
                BulkheadConfig.custom()
                        .maxConcurrentCalls(50)
                        .build()
        );

        // Given
        Bulkhead bulkhead = Bulkhead.of("test", config);
        Bulkhead anotherBulkhead = Bulkhead.of("testAnother", config);

        // When I create a Supplier and a Function which are decorated by different Bulkheads
        CheckedFunction0<String> decoratedSupplier
                = Bulkhead.decorateCheckedSupplier(bulkhead, () -> "Hello");

        CheckedFunction1<String, String> decoratedFunction
                = Bulkhead.decorateCheckedFunction(anotherBulkhead, (input) -> input + " world");

        // and I chain a function with map
        Try<String> result = Try.of(decoratedSupplier)
                .mapTry(decoratedFunction::apply);

        // 您可以使用changeConfig方法在运行时修改隔板参数。
        // 注意！新maxWaitTime持续时间不会影响当前正在等待权限的线程。

        bulkhead.getEventPublisher()
                .onCallPermitted(event -> log.info(""))
                .onCallRejected(event -> log.info(""))
                .onCallFinished(event -> log.info(""));
    }

    public static void Retry() {
        // Create a Retry context with a default global configuration
        // (maxAttempts = 3, waitDurationInOpenState = 500[ms])
        RetryConfig config1 = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .build();
        Retry retry = Retry.of("id", config1);

        RetryConfig config2 = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(100))
                .retryOnException(throwable -> Match(throwable).of(
                        Case($(Predicates.instanceOf(WebServiceException.class)), true),
                        Case($(), false)))
                .build();

        // Create a Retry with default configuration
        retry = Retry.ofDefaults("id");
        // Decorate the invocation of the HelloWorldService
        CheckedFunction0<String> retryableSupplier = Retry.decorateCheckedSupplier(retry, () -> "retry");

        // When I invoke the function
        Try<String> result = Try.of(retryableSupplier).recover((throwable) -> "Hello world from recovery function");
    }

    public static void Cache() {
        javax.cache.Cache<String, String> cacheInstance = Caching.getCache("cacheName", String.class, String.class);
        Cache<String, String> cacheContext = Cache.of(cacheInstance);
        // Decorate your call to BackendService.doSomething()
        CheckedFunction1<String, String> cachedFunction = Decorators.ofCheckedSupplier(() -> "cache")
                .withCache(cacheContext)
                .decorate();
        String value = Try.of(() -> cachedFunction.apply("cacheKey")).get();

        cacheContext.getEventPublisher()
                .onCacheHit(event -> log.info(""))
                .onCacheMiss(event -> log.info(""))
                .onError(event -> log.info(""));
    }

    public static void main(String[] args) {
        CircuitBreaker();
        RateLimiter();
        Bulkhead();
    }
}
