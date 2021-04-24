package com.trading.fxtrading.subscriptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import trading.entities.Price;

public class PricesTransformer{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = "priceTransformer")
    public void SampleJmsListenerMethod(Message<Price> order) {
        logger.info("Mensaje Recibido");
    }


}
