package dev.ronin_engineer.kafka.producer.wallet.infrastructure.service;

import dev.ronin_engineer.kafka.common.constant.EventType;
import dev.ronin_engineer.kafka.common.dto.KafkaMessage;
import dev.ronin_engineer.kafka.common.util.MessageBuilder;
import dev.ronin_engineer.kafka.producer.wallet.domain.constant.MessageCode;
import dev.ronin_engineer.kafka.producer.wallet.domain.event.TransactionEvent;
import dev.ronin_engineer.kafka.producer.wallet.domain.service.TransactionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionProducerImpl implements TransactionProducer {

    @Value("${kafka.transaction}")
    private String transactionTopic;

    @Value("${application-name}")
    private String serviceId;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(TransactionEvent transaction) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.FUND_TRANSFER.getCode(),
                    transaction
            );

            kafkaTemplate.send(transactionTopic, message);
            log.info("Produced a message to topic: {}, value: {}", transactionTopic, transaction);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: " + transactionTopic);
            e.printStackTrace();
        }
    }
}
