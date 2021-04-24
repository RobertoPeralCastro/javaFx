package com.trading.fxtrading.controllers;


import com.trading.fxtrading.service.PricesService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trading.entities.Price;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/prices")
@Api("trading-api")
public class PricesController
{
    @Autowired private PricesService pricesService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/getPrices")
    public ResponseEntity getPrices()
    {
        try
        {
            Optional<List<Price>> result = pricesService.getPrices();
            return result.map(c->ResponseEntity.ok().body(c)).orElse(ResponseEntity.noContent().build());
        }
        catch (Exception e)
        {
            logger.error("unexpected error in application. " + ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
