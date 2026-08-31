package com.brijesh.microservices.currencyconversionservice.controller;

import com.brijesh.microservices.currencyconversionservice.bean.CurrencyConversion;
import com.brijesh.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CurrencyConversionController}.
 *
 * NOTE ON COVERAGE: calculateCurrencyConversionDynamic() -- the
 * "-resttemplate" endpoint -- instantiates its own `new RestTemplate()`
 * inline, the same testability issue documented in
 * CircuitBreakerControllerTest. It is intentionally not covered here; inject
 * RestTemplate as a collaborator (constructor or @Autowired field) to make
 * it mockable, then this class is the right place to add that test.
 *
 * The Feign-based endpoint (calculateCurrencyConversionFeign) has no such
 * problem: CurrencyExchangeProxy is an @Autowired field, so it's a normal
 * Mockito mock target.
 */
@ExtendWith(MockitoExtension.class)
class CurrencyConversionControllerTest {

    @Mock
    private CurrencyExchangeProxy currencyExchangeProxy;

    @InjectMocks
    private CurrencyConversionController controller;

    @Test
    void calculateCurrencyConversion_returnsHardcodedStubbedConversion() {
        CurrencyConversion result =
                controller.calculateCurrencyConversion("USD", "INR", BigDecimal.TEN);

        assertThat(result.getId()).isEqualTo(10001L);
        assertThat(result.getFrom()).isEqualTo("USD");
        assertThat(result.getTo()).isEqualTo("INR");
        assertThat(result.getConversionMultiple()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(result.getQuantity()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(result.getTotalCalculatedAmount()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void calculateCurrencyConversionFeign_multipliesQuantityByRateFromProxy() {
        CurrencyConversion exchangeResponse = new CurrencyConversion(
                10001L, "USD", "INR", BigDecimal.valueOf(65), null, null, "8000");
        when(currencyExchangeProxy.retrieveExchangeValue("USD", "INR")).thenReturn(exchangeResponse);

        CurrencyConversion result =
                controller.calculateCurrencyConversionFeign("USD", "INR", BigDecimal.TEN);

        assertThat(result.getId()).isEqualTo(10001L);
        assertThat(result.getConversionMultiple()).isEqualByComparingTo(BigDecimal.valueOf(65));
        assertThat(result.getQuantity()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(result.getTotalCalculatedAmount()).isEqualByComparingTo(BigDecimal.valueOf(650));
        assertThat(result.getEnvironment()).isEqualTo("8000 feign");
        verify(currencyExchangeProxy).retrieveExchangeValue("USD", "INR");
    }

    @Test
    void calculateCurrencyConversionFeign_propagatesUnderlyingAmountCorrectly_forFractionalRate() {
        CurrencyConversion exchangeResponse = new CurrencyConversion(
                20002L, "EUR", "GBP", BigDecimal.valueOf(0.85), null, null, "8001");
        when(currencyExchangeProxy.retrieveExchangeValue("EUR", "GBP")).thenReturn(exchangeResponse);

        CurrencyConversion result =
                controller.calculateCurrencyConversionFeign("EUR", "GBP", BigDecimal.valueOf(200));

        assertThat(result.getTotalCalculatedAmount()).isEqualByComparingTo(BigDecimal.valueOf(170.00));
        verify(currencyExchangeProxy).retrieveExchangeValue("EUR", "GBP");
    }
}
