package com.novruz.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue accountQueue;
    private final Queue transactionQueue;

    public void publishAccount(String message) {
        rabbitTemplate.convertAndSend(this.accountQueue.getName(), message);
    }

    public void publishTransaction(String message) {
        rabbitTemplate.convertAndSend(this.transactionQueue.getName(), message);
    }
}
