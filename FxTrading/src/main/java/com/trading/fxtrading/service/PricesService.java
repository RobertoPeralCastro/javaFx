package com.trading.fxtrading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trading.entities.Price;
import trading.repositories.PricesRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PricesService
{

    private PricesRepository pricesRepository;

    @Autowired
    public PricesService(PricesRepository pricesRepository)
    {
        this.pricesRepository=pricesRepository;
    }

    /**
     * returns a list of all the prices from the database or null if the list is empty.
     * @return
     */
    public Optional<List<Price>> getPrices()
    {
        List<Price> prices = pricesRepository.findAll();
        if(prices.size()==0)
        {
            return Optional.ofNullable(null);
        }
        else
        {
            return Optional.of(prices);
        }
    }




}
