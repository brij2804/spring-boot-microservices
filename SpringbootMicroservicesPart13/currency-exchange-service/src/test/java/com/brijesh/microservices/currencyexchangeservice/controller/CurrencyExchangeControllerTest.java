package com.brijesh.microservices.currencyexchangeservice.controller;

import com.brijesh.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.brijesh.microservices.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CurrencyExchangeController}, mocking its two
 * collaborators (CurrencyExchangeRepository and Environment) so these run in
 * isolation with no Spring context and no real database.
 */
@ExtendWith(MockitoExtension.class)
class CurrencyExchangeControllerTest {

    @Mock
    private CurrencyExchangeRepository repository;

    @Mock
    private Environment environment;

    @InjectMocks
    private CurrencyExchangeController controller;

    @Test
    void retrieveExchangeValue_returnsHardcodedExchangeWithPortStamped() {
        when(environment.getProperty("local.server.port")).thenReturn("8000");

        CurrencyExchange result = controller.retrieveExchangeValue("USD", "INR");

        assertThat(result.getId()).isEqualTo(1000L);
        assertThat(result.getFrom()).isEqualTo("USD");
        assertThat(result.getTo()).isEqualTo("INR");
        assertThat(result.getConversionMultiple()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.getEnvironment()).isEqualTo("8000");
    }

    @Test
    void retrieveExchangeValueFromRepository_returnsRepositoryResultWithPortStamped_whenFound() {
        CurrencyExchange stored = new CurrencyExchange(10001L, "USD", "INR", BigDecimal.valueOf(65));
        when(repository.findByFromAndTo("USD", "INR")).thenReturn(stored);
        when(environment.getProperty("local.server.port")).thenReturn("8000");

        CurrencyExchange result = controller.retrieveExchangeValueFromRepository("USD", "INR");

        assertThat(result.getId()).isEqualTo(10001L);
        assertThat(result.getConversionMultiple()).isEqualByComparingTo(BigDecimal.valueOf(65));
        assertThat(result.getEnvironment()).isEqualTo("8000");
        verify(repository).findByFromAndTo("USD", "INR");
    }

    @Test
    void retrieveExchangeValueFromRepository_throwsRuntimeException_whenNoMatchingRowExists() {
        when(repository.findByFromAndTo("USD", "XYZ")).thenReturn(null);

        assertThatThrownBy(() -> controller.retrieveExchangeValueFromRepository("USD", "XYZ"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unable to find data for USD to XYZ");

        verify(repository).findByFromAndTo("USD", "XYZ");
    }
}
