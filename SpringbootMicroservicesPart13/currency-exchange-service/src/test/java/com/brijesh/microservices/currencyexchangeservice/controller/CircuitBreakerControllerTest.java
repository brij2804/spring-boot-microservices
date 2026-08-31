package com.brijesh.microservices.currencyexchangeservice.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CircuitBreakerController}.
 *
 * NOTE ON COVERAGE: sampleApi() instantiates its own `new RestTemplate()`
 * inline rather than having one injected, which means there is no seam for
 * Mockito to intercept that HTTP call -- you cannot @Mock a RestTemplate
 * that the method itself constructs. As written, sampleApi() is only really
 * exercisable via an integration-style test (e.g. WireMock on port 8080, or
 * @SpringBootTest with a stubbed downstream). To make this properly
 * unit-testable, inject RestTemplate via the constructor or an @Autowired
 * field instead:
 *
 *   private final RestTemplate restTemplate;
 *   public CircuitBreakerController(RestTemplate restTemplate) { this.restTemplate = restTemplate; }
 *
 * That would let a test @Mock RestTemplate and verify getForEntity(...)
 * directly. Until then, this test class covers the one piece of logic in
 * this controller that IS a plain, mockable-free unit: the fallback method
 * Resilience4j calls when the retried call ultimately fails.
 */
class CircuitBreakerControllerTest {

    private final CircuitBreakerController controller = new CircuitBreakerController();

    @Test
    void hardcodedResponse_returnsFallbackStringRegardlessOfException() {
        String result = controller.hardcodedResponse(new RuntimeException("downstream unavailable"));

        assertThat(result).isEqualTo("fallback response");
    }

    @Test
    void hardcodedResponse_handlesNullException_withoutThrowing() {
        String result = controller.hardcodedResponse(null);

        assertThat(result).isEqualTo("fallback response");
    }
}
