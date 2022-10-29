package com.novruz.account.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
    @Value("${queue.name.account}")
    private String accountQueueName;

    @Value("${queue.name.transaction}")
    private String transactionQueueName;

    @Bean
    public Queue accountQueue() {
        return new Queue(accountQueueName, true);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(transactionQueueName, true);
    }
}
