package com.trading.fxtrading;

import com.trading.fxtrading.service.PricesService;
import com.trading.fxtrading.subscriptors.PricesTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import trading.entities.Price;
import trading.repositories.PricesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FxTradingApplicationTests
{
    @Autowired private PricesRepository pricesRepository;
    @Test
    void testConvertCorrectCsvPrice()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        LocalDateTime dateTime = LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS"));
        //Price price = Price.builder().instrumentName("EUR/USD").id(106).bid(1.1).ask(1.2).timestamp(dateTime).build();
        Price transformedPrice = pricesTransformer.buildPriceFromCsv("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001").get();
        assert(transformedPrice.getAsk().equals(1.2) && transformedPrice.getBid().equals(1.1) && transformedPrice.getId()==(106)
                && transformedPrice.getTimestamp().equals(dateTime) && transformedPrice.getInstrumentName().equals("EUR/USD") );

    }


    @Test
    void testConvertIncorrectNumberOfAttributes()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        Optional<Price> transformedPrice = pricesTransformer.buildPriceFromCsv("106,, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001");
        assert( transformedPrice.isPresent() == false);

    }

    @Test
    void testConvertIncorrectIdFormat()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        Optional<Price> transformedPrice = pricesTransformer.buildPriceFromCsv("106sdfs,EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001");
        assert( transformedPrice.isPresent() == false);

    }

    @Test
    void testConvertIncorrectBidFormat()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        Optional<Price> transformedPrice = pricesTransformer.buildPriceFromCsv("106,EUR/USD, kkk,1.2000,01-06-2020 12:01:01:001");
        assert( transformedPrice.isPresent() == false);

    }

    @Test
    void testConvertIncorrectAskFormat()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        Optional<Price> transformedPrice = pricesTransformer.buildPriceFromCsv("106,EUR/USD, 1.1000,kkk,01-06-2020 12:01:01:001");
        assert( transformedPrice.isPresent() == false);

    }

    @Test
    void testConvertIncorrectTimestampFormat()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        Optional<Price> transformedPrice = pricesTransformer.buildPriceFromCsv("106,EUR/USD, 1.1000,1.2000,01-06-2020 120:01:01:001");
        assert( transformedPrice.isPresent() == false);

    }

    @Test
    void testBidAskMargins()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        LocalDateTime dateTime = LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS"));
        Price price = Price.builder().instrumentName("EUR/USD").id(106).bid(1.1).ask(1.2).timestamp(dateTime).build();
        Price transformedPrice = pricesTransformer.modifyPricesMargin(Optional.of(price));
        assert(transformedPrice.getAsk().equals(1.3) && transformedPrice.getBid().equals(1.0) && transformedPrice.getId()==(106)
                && transformedPrice.getTimestamp().equals(dateTime) && transformedPrice.getInstrumentName().equals("EUR/USD") );
    }

    @Test
    void testProcessCorrectFeed()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        LocalDateTime dateTime = LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS"));
        //Price price = Price.builder().instrumentName("EUR/USD").id(106).bid(1.1).ask(1.2).timestamp(dateTime).build();
        String prices = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110";
        pricesTransformer.persistModifiedPrices(prices);
        assert(pricesRepository.findAll().size() == 5);

    }

    @Test
    void testProcessFeedWith2IncorrectLines()
    {
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        LocalDateTime dateTime = LocalDateTime.parse("01-06-2020 12:01:01:001", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS"));
        //Price price = Price.builder().instrumentName("EUR/USD").id(106).bid(1.1).ask(1.2).timestamp(dateTime).build();
        String prices = "106fd, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,33-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110";
        pricesTransformer.persistModifiedPrices(prices);
        assert(pricesRepository.findAll().size() == 3);

    }

    @Test
    void testGetPricesOk()
    {
        PricesService pricesService = new PricesService(pricesRepository);
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);

        String prices = "106fd, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,33-06-2020 12:01:02:002\n" +
                "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
                "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110";
        pricesTransformer.persistModifiedPrices(prices);

        Optional<List<Price>> obtainedPrices = pricesService.getPrices();
        assert (obtainedPrices.isPresent()==true && obtainedPrices.get().size() == 3);
    }

    @Test
    void testGetPricesEmpty()
    {
        PricesService pricesService = new PricesService(pricesRepository);
        PricesTransformer pricesTransformer = new PricesTransformer(pricesRepository);
        String prices = "106fd, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107asdf, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        pricesTransformer.persistModifiedPrices(prices);
        Optional<List<Price>> obtainedPrices = pricesService.getPrices();
        assert (obtainedPrices.isPresent()==false);
    }
}
