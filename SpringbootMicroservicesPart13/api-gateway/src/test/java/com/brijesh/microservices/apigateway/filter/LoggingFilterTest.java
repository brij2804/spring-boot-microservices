package com.brijesh.microservices.apigateway.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LoggingFilter}, a Spring Cloud Gateway GlobalFilter.
 * ServerWebExchange/GatewayFilterChain are mocked so this runs without
 * standing up an actual gateway or WebFlux server.
 */
@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private RequestPath requestPath;

    @Mock
    private GatewayFilterChain chain;

    private final LoggingFilter loggingFilter = new LoggingFilter();

    @Test
    void filter_logsRequestPathAndDelegatesToChain_returningChainsMono() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);

        Mono<Void> chainResult = Mono.empty();
        when(chain.filter(exchange)).thenReturn(chainResult);

        Mono<Void> result = loggingFilter.filter(exchange, chain);

        // The filter must be transparent: it should return exactly what the
        // chain produced, not swallow it or wrap it in something new.
        assertThat(result).isSameAs(chainResult);
        verify(chain).filter(exchange);
    }

    @Test
    void filter_neverCallsChain_whenSecondCallToFilterInvokedIndependently() {
        // Guards against a regression where the filter accidentally calls
        // chain.filter() more than once for a single request (e.g. if
        // someone later adds retry/error-handling logic carelessly).
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        loggingFilter.filter(exchange, chain);

        verify(chain, org.mockito.Mockito.times(1)).filter(exchange);
    }
}
