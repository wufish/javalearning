package com.wufish.javalearning.resilience4j;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.cache.Caching;

import org.apache.commons.lang3.StringUtils;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.Predicates;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

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
        Integer result1 = timeLimiter.executeFutureSupplier(futureSupplier);

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

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "result11111";
        });
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(5))  //超时时间阈值，默认 1s
                .cancelRunningFuture(false)  //超时后是否取消线程
                .build();
        TimeLimiter of = TimeLimiter.of(timeLimiterConfig);

        ////////////////////////////////
        // 容错配置
        TimeLimiterConfig testTimeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(60)) // 超时时间阈值，默认 1s
                .cancelRunningFuture(true) // 超时后是否取消线程，默认为 true
                .build();

        TimeLimiter testTimeLimiter = TimeLimiterRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .timeLimiter("testTimeLimiter", testTimeLimiterConfig); // 从缓存获取或者创建新的容错器器放入内存

        testTimeLimiter.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onTimeout(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // Future 装饰器
        Callable<String> decoratedFutureSupplier = testTimeLimiter.decorateFutureSupplier(() -> future);

        String result = Try.of(() -> decoratedFutureSupplier.call()) // 执行任务
                .onFailure(Throwable::printStackTrace) // 任务失败处理
                .recover(ExecutionException.class, e -> "fallback1") // 针对某个异常，做单独的降级处理
                .getOrElse("fallback2"); // 统一降级处理

        System.out.println();
    }

    public static void Retry() {
        // Create a Retry context with a default global configuration
        // (maxAttempts = 3, waitDurationInOpenState = 500[ms])
        RetryConfig config1 = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .build();
        Retry retry1 = Retry.of("id", config1);

        // Create a Retry with default configuration
        retry1 = Retry.ofDefaults("id");
        // Decorate the invocation of the HelloWorldService
        CheckedFunction0<String> retryableSupplier = Retry.decorateCheckedSupplier(retry1, () -> "retry");

        // When I invoke the function
        Try<String> result1 = Try.of(retryableSupplier).recover((throwable) -> "Hello world from recovery function");

        ////////////////////////////////
        // 容错配置
        RetryConfig testRetryConfig = RetryConfig.<String>custom() // String 任务的返回结果类型
                .maxAttempts(2) // 最大重试次数，默认 3
                .intervalFunction(IntervalFunction.of(100L)) // 根据尝试次数调整尝试时间间隔，默认 500ms
                .waitDuration(Duration.ofMillis(100)) // 重试的时间间隔，会转成intervalFunction，二选一
                .retryOnResult(StringUtils::isBlank) // 根据返回结果，判断是否重试
                .retryOnException(throwable ->
                        Match(throwable).of(
                                Case($(Predicates.instanceOf(Exception.class)), true),
                                Case($(), false))
                ) // 根据异常匹配结果，是否需要重试，默认为 null
                .retryExceptions(new Class[]{}) // 需要重试的异常数组，默认为空
                .ignoreExceptions(new Class[]{}) // 不需要重试的异常数组，默认为空
                .build();

        Retry testRetry = RetryRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .retry("testRetry", testRetryConfig); // 从缓存获取或者创建新的容错器器放入内存

        testRetry.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onRetry(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onIgnoredError(event -> System.out.println(event.getEventType()))
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // CheckedFunction0装饰器（可以抛出异常），可以针对 Function，Callable，Runnable，以及线程池异步重试
        CheckedFunction0<String> task = () -> "task";
        CheckedFunction0<String> decoratedTask = Retry.decorateCheckedSupplier(testRetry, task);
        // 线程池异步重试
        Supplier<CompletionStage<String>> decoratedFutureTask =
                Retry.decorateCompletionStage(testRetry, Executors.newScheduledThreadPool(10),
                        () -> CompletableFuture.supplyAsync(() -> "future task")
                );

        String result = Try.of(decoratedTask) // 执行任务
                .onFailure(Throwable::printStackTrace) // 任务失败处理
                .recover(Exception.class, e -> "fallback1") // 针对某个异常，做单独的降级处理
                .getOrElse("fallback2"); // 统一降级处理

    }

    public static void RateLimiter() {
       /* // For example you want to restrict the calling rate of some method to be not higher than 10 req/ms.
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

        RateLimiter atomicLimiter = new AtomicRateLimiter("", RateLimiterConfig.custom().build());
        RateLimiter semaphoreBasedRateLimiter = new SemaphoreBasedRateLimiter("", RateLimiterConfig.custom().build());
*/
        ////////////////////////////////
        // 容错配置
        RateLimiterConfig testRateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(10) // 时间段内限速次数，默认 50
                .timeoutDuration(Duration.ofSeconds(1)) // 限速阻塞超时时间，默认 5s
                .limitRefreshPeriod(Duration.ofSeconds(10)) // 限速流量刷新周期，每个周期限速器将其权限计数设置为limitForPeriod值，默认 500纳秒
                .build();

        RateLimiter testRateLimiter = RateLimiterRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .rateLimiter("testRateLimiter", testRateLimiterConfig); // 从缓存获取或者创建新的容错器器放入内存

        testRateLimiter.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onFailure(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // 任务
        CheckedFunction0<String> task = () -> "task";
        // 任务装饰器
        CheckedFunction0<String> decoratedTask = RateLimiter.decorateCheckedSupplier(testRateLimiter, task);

        String result = Try.of(decoratedTask) // 执行任务
                .onFailure(Throwable::printStackTrace) // 任务失败处理
                .recover(Exception.class, e -> "fallback1") // 针对某个异常，做单独的降级处理
                .getOrElse("fallback2"); // 统一降级处理

        System.out.println(result);
    }

    public static void Bulkhead() throws ExecutionException, InterruptedException {
        // 舱壁 - 限制并行执行的数量，隔离和减少 后端调用下游依赖，对于cpu型任务，只减少负载
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();

        // Create a custom configuration for a Bulkhead
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(150)// 舱壁允许的最大并行执行量， 默认个 25
                .maxWaitDuration(Duration.ofMillis(100)) // 等待进入舱壁的最大超时时间，默认 0s
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

        ////////////////////////////////
        // 信号量舱壁
        // 容错器配置
        BulkheadConfig testBulkheadConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(150)// 舱壁允许的最大并行执行量， 默认个 25
                .maxWaitDuration(Duration.ofMillis(100)) // 等待进入舱壁的最大超时时间，默认 0s
                .build();
        Bulkhead testBulkhead = BulkheadRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .bulkhead("testBulkhead", testBulkheadConfig); // 从缓存获取或者创建新的容错器器放入内存

        testBulkhead.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onCallPermitted(event -> System.out.println(event.getEventType()))
                .onCallRejected(event -> System.out.println(event.getEventType()))
                .onCallFinished(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // 任务
        CheckedFunction0<String> task = () -> "task";
        // 装饰任务
        CheckedFunction0<String> decoratedTask = Bulkhead.decorateCheckedSupplier(testBulkhead, task);

        String result1 = Try.of(decoratedTask) // 执行任务
                .onFailure(Throwable::printStackTrace) // 任务失败处理
                .recover(Exception.class, e -> "fallback1") // 针对某个异常，做单独的降级处理
                .getOrElse("fallback2"); // 统一降级处理

        // 线程池舱壁 - ThreadPoolBulkhead
        // 容错器配置
        ThreadPoolBulkheadConfig testThreadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(10) // 最大线程数，默认可用 cpu 核数
                .coreThreadPoolSize(10) // 核心线程数，默认 availableProcessors > 1 ? availableProcessors -1 : 1;
                .queueCapacity(100) // ArrayBlockingQueue 阻塞队列长度，默认 100
                .keepAliveDuration(Duration.ofMillis(10)) // 线程存活时间，默认 20ms
                .build();

        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkheadRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .bulkhead("threadPoolBulkhead", testThreadPoolBulkheadConfig); // 从缓存获取或者创建新的容错器器放入内存

        threadPoolBulkhead.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onCallPermitted(event -> System.out.println(event.getEventType()))
                .onCallRejected(event -> System.out.println(event.getEventType()))
                .onCallFinished(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // 任务
        Supplier<String> threadPoolTask = () -> "threadPoolTask";
        // 装饰任务
        Supplier<CompletionStage<String>> decoratedThreadPoolTask = ThreadPoolBulkhead.decorateSupplier(threadPoolBulkhead, threadPoolTask);
        // 向线程池提交任务
        CompletionStage<String> completionStage = decoratedThreadPoolTask.get();

        String result2 = completionStage.toCompletableFuture().exceptionally(t -> {
            t.printStackTrace();
            return "fallback1";
        }).get();

    }

    public static void CircuitBreaker() {
        // CircuitBreaker - 断路器
        // 1. 使用注册器注册器创建 CircuitBreaker，map 缓存
        //  1.1 默认配置创建注册器
        CircuitBreakerRegistry circuitBreakerRegistry1 = CircuitBreakerRegistry.ofDefaults();
        //  1.2 自定义断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 故障率阈值百分比，超过该值，断路器开始断路调用，默认值 50
                .slowCallRateThreshold(100) // 慢请求阈值, 默认值 100
                .slowCallDurationThreshold(Duration.ofSeconds(60)) // 慢请求的时间，默认值60s
                .permittedNumberOfCallsInHalfOpenState(10) // 半开状态允许的请求数，默认 10
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(0)) // 半开状态持续的最大时间，默认 0，表示无限等待直到所有的请求都被允许
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 断路器关闭状态下，记录请求的滑动窗口类型，默认 基于计数
                .slidingWindowSize(100) // 断路器关闭状态下，默认值 100，如果滑动窗口类型是 COUNT_BASED，表示窗口计数的个数，如果是 TIME_BASED，则表示窗口时间范围
                .minimumNumberOfCalls(100) // 转换状态前，需要记录的最小的请求个数, 默认值 100
                .waitDurationInOpenState(Duration.ofSeconds(60)) // 断路器从开启状态转换成半开状态的等待时间，默认 60s
                .automaticTransitionFromOpenToHalfOpenEnabled(false) // 默认 false，表示断路器需要新的请求才能触发从开启状态到半开状态，true 表示断路器不需要请求就可以从开启状态转化为半开状态
                .recordExceptions() // 需要判定为失败的异常，默认空
                .ignoreExceptions() // 需要忽略的异常，不会判定为失败，默认空
                // 默认所有异常都被认定为失败事件，你可以定制Predicate的test检查，实现选择性记录，只有该函数返回为true时异常才被认定为失败事件。
                .recordException(throwable -> Match(throwable).of(
                        Case($(instanceOf(Exception.class)), true),
                        Case($(), false)))
                .ignoreException(throwable -> Match(throwable).of(
                        Case($(instanceOf(Exception.class)), true),
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
                        Case($(Predicates.instanceOf(Exception.class)), true),
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

        ///////////////////////////////////
        // 容错器配置
        CircuitBreakerConfig testCircuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 故障率阈值百分比，超过该值，断路器开始断路调用，默认值 50
                .slowCallRateThreshold(100) // 慢请求阈值, 默认值 100
                .slowCallDurationThreshold(Duration.ofSeconds(60)) // 慢请求的时间，默认值60s
                .permittedNumberOfCallsInHalfOpenState(10) // 半开状态允许的请求数，默认 10
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(0)) // 半开状态持续的最大时间，默认 0，表示无限等待直到所有的请求都被允许
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 断路器关闭状态下，记录请求的滑动窗口类型，默认 基于计数
                .slidingWindowSize(100) // 断路器关闭状态下，默认值 100，如果滑动窗口类型是 COUNT_BASED，表示窗口计数的个数，如果是 TIME_BASED，则表示窗口时间范围
                .minimumNumberOfCalls(100) // 转换状态前，需要记录的最小的请求个数, 默认值 100
                .waitDurationInOpenState(Duration.ofSeconds(60)) // 断路器从开启状态转换成半开状态的等待时间，默认 60s
                .automaticTransitionFromOpenToHalfOpenEnabled(false) // 默认 false，表示断路器需要新的请求才能触发从开启状态到半开状态，true 表示断路器不需要请求就可以从开启状态转化为半开状态
                .recordExceptions() // 需要判定为失败的异常，默认空
                .ignoreExceptions() // 需要忽略的异常，不会判定为失败，默认空
                // 默认所有异常都被认定为失败事件，你可以定制Predicate的test检查，实现选择性记录，只有该函数返回为true时异常才被认定为失败事件。
                .recordException(throwable -> Match(throwable).of(
                        Case($(instanceOf(Exception.class)), true),
                        Case($(), false)))
                .ignoreException(throwable -> Match(throwable).of(
                        Case($(instanceOf(Exception.class)), true),
                        Case($(), false)))
                .build();

        CircuitBreaker testCircuitBreaker = CircuitBreakerRegistry.ofDefaults() // 创建注册器，注册器会根据 name 缓存容错器
                .circuitBreaker("testCircuitBreaker", testCircuitBreakerConfig); // 从缓存获取或者创建新的容错器器放入内存

        testCircuitBreaker.getEventPublisher() // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
                .onCallNotPermitted(event -> System.out.println(event.getEventType()))
                .onStateTransition(event -> System.out.println(event.getEventType()))
                .onFailureRateExceeded(event -> System.out.println(event.getEventType()))
                .onSlowCallRateExceeded(event -> System.out.println(event.getEventType()))
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onIgnoredError(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println(event.getEventType()));

        // 任务
        CheckedFunction0<String> task = () -> "task";
        // 装饰任务
        CheckedFunction0<String> decoratedTask = CircuitBreaker.decorateCheckedSupplier(testCircuitBreaker, task);

        String testResult = Try.of(decoratedTask) // 执行任务
                .onFailure(Throwable::printStackTrace) // 任务失败处理
                .recover(Exception.class, e -> "fallback1") // 针对某个异常，做单独的降级处理
                .getOrElse("fallback2"); // 统一降级处理

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

    public static void main(String[] args) throws Exception {
       /* CircuitBreaker();
        RateLimiter();
        Bulkhead();*/
        RateLimiter();
    }
}
