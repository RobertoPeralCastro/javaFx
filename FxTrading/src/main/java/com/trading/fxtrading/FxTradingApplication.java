package com.trading.fxtrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;
import trading.entities.Price;

@SpringBootApplication
@EnableJpaRepositories("trading.repositories")
@EntityScan(basePackages = "trading.entities")
@ComponentScan("com.trading.fxtrading.subscriptors")
public class FxTradingApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FxTradingApplication.class, args);
    }
}
