package dev.ronin_engineer.kafka.producer.wallet.domain.service;

import dev.ronin_engineer.kafka.producer.wallet.domain.event.TransactionEvent;
import org.springframework.stereotype.Component;


@Component
public interface TransactionProducer {

    void send(TransactionEvent transaction);
}
