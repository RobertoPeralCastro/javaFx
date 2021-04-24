package com.trading.fxtrading.controllers;


import com.trading.fxtrading.service.PricesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trading.entities.Price;


@RestController
@RequestMapping("/prices")
@Api("trading-api")
public class PricesController
{
    @Autowired private PricesService pricesService;
    @Autowired private JmsTemplate jmsTemplate;

    @GetMapping("/getPrices")
    public void getPrices()
    {
        try
        {
            pricesService.getPrices();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @GetMapping("/sendMessage")
    public void sendMessage()
    {
        Price price = new Price();
        jmsTemplate.convertAndSend("priceTransformer", price);

    }
}
