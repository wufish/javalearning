package com.wufish.javalearning.resilience4j;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * The type Time limiter factory.
 */
@Data
@Slf4j
@Builder
public class TimeLimiterFactory {
    private TimeLimiter timeLimiter;

    /**
     * Wrap callable.
     *
     * @param <T>            the type parameter
     * @param futureSupplier the future supplier
     * @return the callable
     */
    public <T> Callable<T> wrap(Supplier<Future<T>> futureSupplier) {
        return TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);
    }

    private static TimeLimiterConfig getDefaultConfig() {
        return TimeLimiterConfig.custom()
                //超时时间阈值
                .timeoutDuration(Duration.ofMillis(5))
                //超时后是否取消线程
                .cancelRunningFuture(false)
                .build();
    }

    /**
     * Of default time limiter factory.
     *
     * @return the time limiter factory
     */
    public static TimeLimiterFactory ofDefault() {
        return of(getDefaultConfig());
    }

    /**
     * Of time limiter factory.
     *
     * @param timeLimiterConfig the time limiter config
     * @return the time limiter factory
     */
    public static TimeLimiterFactory of(TimeLimiterConfig timeLimiterConfig) {
        return new TimeLimiterFactory(TimeLimiter.of(timeLimiterConfig));
    }
}
