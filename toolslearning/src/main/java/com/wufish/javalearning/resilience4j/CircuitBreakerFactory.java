package com.wufish.javalearning.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * The type Circuit breaker factory.
 */
@Data
@Slf4j
@Builder
public class CircuitBreakerFactory {
    private CircuitBreaker circuitBreaker;

    private CircuitBreakerFactory(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
        circuitBreaker.getEventPublisher().onEvent(event -> {
            if (event.getEventType() == CircuitBreakerEvent.Type.NOT_PERMITTED) {
                // 统计熔断次数
                // WMonitor.sum(attr, 1);
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s-%s", event.getCircuitBreakerName(), event.getEventType()));
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
        return CircuitBreaker.decorateCheckedSupplier(circuitBreaker, checkedSupplier);
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

    private static CircuitBreakerConfig getDefaultConfig() {
        return CircuitBreakerConfig.custom()
                // 创建熔断器配置
                //熔断器打开所依据的失败率
                .failureRateThreshold(50)
                //熔断器打开所持续的时间，时间结束后，熔断器转为半开状态
                .waitDurationInOpenState(Duration.ofSeconds(5))
                //熔断器关闭状态下的ring bit buffer大小
                .ringBufferSizeInClosedState(100)
                //熔断器半开状态下的ring bit buffer大小
                .ringBufferSizeInHalfOpenState(100)
                .build();
    }

    /**
     * Of default circuit breaker factory.
     *
     * @param name the name
     * @return the circuit breaker factory
     */
    public static CircuitBreakerFactory ofDefault(String name) {
        return of(getDefaultConfig(), name);
    }

    /**
     * Of circuit breaker factory.
     *
     * @param breakerConfig the breaker config
     * @param name          the name
     * @return the circuit breaker factory
     */
    public static CircuitBreakerFactory of(CircuitBreakerConfig breakerConfig, String name) {
        CircuitBreaker circuitBreaker = CircuitBreakerRegistry.of(breakerConfig).circuitBreaker(name);
        return new CircuitBreakerFactory(circuitBreaker);
    }
}
