package com.trading.fxtrading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trading.entities.Price;
import trading.repositories.PricesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PricesService
{
    @Autowired  private PricesRepository pricesRepository;

    private final Double PRICE_MARGIN = 0.1;

    public List<Price> getPrices()
    {
        return pricesRepository.findAll().stream().map(e-> modifyPricesMargin(e)).collect(Collectors.toList());
    }

    public Price modifyPricesMargin(Price price)
    {
        price.setAsk(price.getAsk() + PRICE_MARGIN);
        price.setBid(price.getBid() - PRICE_MARGIN);
        return price;
    }

}
