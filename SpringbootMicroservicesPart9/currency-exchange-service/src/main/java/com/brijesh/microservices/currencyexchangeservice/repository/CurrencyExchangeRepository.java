package com.brijesh.microservices.currencyexchangeservice.repository;

import com.brijesh.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.brijesh.microservices.currencyexchangeservice.controller.CurrencyExchangeController;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange,Long> {

    CurrencyExchange findByFromAndTo(String from, String to);
}
