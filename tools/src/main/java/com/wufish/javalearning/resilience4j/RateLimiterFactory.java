package com.wufish.javalearning.resilience4j;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.event.RateLimiterEvent;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * The type Rate limiter factory.
 */
@Data
@Slf4j
@Builder
public class RateLimiterFactory {
    private RateLimiter rateLimiter;

    /**
     * Instantiates a new Rate limiter factory.
     *
     * @param rateLimiter the rate limiter
     */
    public RateLimiterFactory(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        rateLimiter.getEventPublisher().onEvent(event -> {
            if (event.getEventType() == RateLimiterEvent.Type.FAILED_ACQUIRE) {
                //WMonitor.sum(attr, 1);
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s-%s", event.getRateLimiterName(), event.getEventType()));
            }
        });
    }

    /**
     * Wrap checked function 0.
     *
     * @param <R>             the type parameter
     * @param checkedSupplier the checked supplier
     * @return the checked function 0
     */
    public <R> CheckedFunction0<R> wrap(CheckedFunction0<R> checkedSupplier) {
        return RateLimiter.decorateCheckedSupplier(rateLimiter, checkedSupplier);
    }

    /**
     * Wrap try try.
     *
     * @param <R>             the type parameter
     * @param checkedSupplier the checked supplier
     * @return the try
     */
    public <R> Try<R> wrapTry(CheckedFunction0<R> checkedSupplier) {
        return Try.of(wrap(checkedSupplier));
    }

    private static RateLimiterConfig getDefaultConfig() {
        return RateLimiterConfig.custom()
                .limitForPeriod(100)
                .limitRefreshPeriod(Duration.ofMillis(1000))
                .timeoutDuration(Duration.ofMillis(0))
                .build();
    }

    /**
     * Of default rate limiter factory.
     *
     * @param name the name
     * @return the rate limiter factory
     */
    public static RateLimiterFactory ofDefault(String name) {
        return of(getDefaultConfig(), name);
    }

    /**
     * Of rate limiter factory.
     *
     * @param rateLimiterConfig the rate limiter config
     * @param name              the name
     * @return the rate limiter factory
     */
    public static RateLimiterFactory of(RateLimiterConfig rateLimiterConfig, String name) {
        RateLimiter rateLimiter = RateLimiterRegistry.of(rateLimiterConfig).rateLimiter(name);
        return new RateLimiterFactory(rateLimiter);
    }
}
