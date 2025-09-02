package com.reward_project.play_record.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // @Value从application properties文件里读东西
    @Value("${rabbitmq.reward-retry-queue-name}")
    private String rewardRetryQueueName;
    @Value("${rabbitmq.reward-retry-exchange-name}")
    private String rewardRetryExchangeName;
    @Value("${rabbitmq.reward-retry-routing-name}")
    private String rewardRetryRoutingName;


    @Bean
    public Queue rewardRetryQueue() {
        return new Queue(rewardRetryQueueName, false);
    }

    @Bean
    //    Messages are not published directly to a queue; instead, the producer sends messages to an exchange.
    //    An exchange is responsible for routing the messages to different queues with the help of bindings and routing keys.
    //    A binding is a link between a queue and an exchange.
    public DirectExchange rewardRetryExchange() {
        return new DirectExchange(rewardRetryExchangeName);
    }

    @Bean
    public Binding rewardRetryBinding() {
        return BindingBuilder.bind(rewardRetryQueue()).to(rewardRetryExchange()).with(rewardRetryRoutingName);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
