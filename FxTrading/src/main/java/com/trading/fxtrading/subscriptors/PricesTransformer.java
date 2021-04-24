package com.trading.fxtrading.subscriptors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import trading.entities.Price;
import trading.repositories.PricesRepository;

import javax.jms.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PricesTransformer implements MessageListener {


    private PricesRepository pricesRepository;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private JmsTemplate jmsTemplate;
    private Queue queue;
    private final Integer idPosition=0;
    private final Integer instrumentPosition=1;
    private final Integer bidPosition=2;
    private final Integer askPosition=3;
    private final Integer timestampPosition=4;
    private final Double PRICE_MARGIN = 0.1;

    @Autowired
    public PricesTransformer(PricesRepository pricesRepository)
    {
        this.pricesRepository=pricesRepository;
    }

    /**
     * Transform message into Prices , Modify the bid and ask values and persist them to DDBB
     * @param message
     */
    @Override
    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof TextMessage)
            {
                TextMessage msg = (TextMessage) message;
                try
                {
                    persistModifiedPrices(msg.getText());
                }
                catch (JMSException e)
                {
                    logger.error("Error processing text message."+ ExceptionUtils.getStackTrace(e));
                }
            } else {
                System.out.println("Message with unknown format and type:" + message.getJMSType());
            }
        }
        catch (JMSException e)
        {
            logger.error("Error processing message."+ ExceptionUtils.getStackTrace(e));
        }
    }


    public void persistModifiedPrices(String csvPrices)
    {
        List<Price> prices = Arrays.stream(csvPrices.split("\n")).map(c -> buildPriceFromCsv(c)).filter(c->c.isPresent()).map(c->modifyPricesMargin(c)).collect(Collectors.toList());
        prices.forEach(p->pricesRepository.
                save(p));
    }

    /**
     * convert csv line to Price object
     * @param csv
     * @return
     */
    public Optional<Price> buildPriceFromCsv(String csv)
    {
        try {
            List<String> stringAttributes = Arrays.stream(csv.split(",")).map(c -> c.trim()).collect(Collectors.toList());
            if (stringAttributes.size() != 5 )
            {
                logger.error("error parsing Price Line. incorrect number of fields: expected 5, processed " + stringAttributes.size() + ". line = " + csv);
                return Optional.ofNullable(null);
            }
            LocalDateTime dateTime = LocalDateTime.parse(stringAttributes.get(timestampPosition), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS"));
            String instrument = stringAttributes.get(instrumentPosition);
            int id = Integer.parseInt(stringAttributes.get(idPosition));
            Double bid = Double.parseDouble(stringAttributes.get(bidPosition));
            Double ask = Double.parseDouble(stringAttributes.get(askPosition));
            Price price = Price.builder().instrumentName(instrument).id(id).bid(bid).ask(ask).timestamp(dateTime).build();
            return Optional.of(price);
        }
        catch (Exception e)
        {
            logger.error("error parsing Price Line: " + csv);
            return Optional.ofNullable(null);
        }
    }

    /**
     * Add the corresponding ask/bid transformation margins to the provided Price
     * @param price
     * @return the transformed price
     */
    public Price modifyPricesMargin(Optional<Price> price)
    {
        return price.map(c->
        {
            c.setAsk(c.getAsk() + PRICE_MARGIN);
            c.setBid(c.getBid() - PRICE_MARGIN);
            return c;
        })
        .orElse(null);
    }

}
