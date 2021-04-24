package com.trading.fxtrading;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
@EnableJms
public class JMSConfiguration {
    @Bean
    public DefaultJmsListenerContainerFactory containerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setMaxMessagesPerTask(1);
        factory.setConcurrency("1-5");
        return factory;
    }
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


   /* @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${destination.order}")
    private String orderDestination;

    @Value("${destination.status}")
    private String statusDestination;
*/
   /* @Bean
    public ActiveMQConnectionFactory senderConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);

        return activeMQConnectionFactory;
    }*/

   /* @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory =
                new CachingConnectionFactory(senderConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(10);

        return cachingConnectionFactory;
    }*/

   /* @Bean
    public Destination orderDestination() {
        return new ActiveMQQueue(orderDestination);
    }*/

   /* @Bean
    public Destination statusDestination() {
        return new ActiveMQQueue(statusDestination);
    }*/

   /* @Bean
    public JmsTemplate orderJmsTemplate() {
        JmsTemplate jmsTemplate =
                new JmsTemplate(cachingConnectionFactory());
        jmsTemplate.setDefaultDestination(orderDestination());
        jmsTemplate.setReceiveTimeout(5000);

        return jmsTemplate;
    }*/
}