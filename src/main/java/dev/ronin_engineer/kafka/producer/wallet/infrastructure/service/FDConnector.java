package dev.ronin_engineer.kafka.producer.wallet.infrastructure.service;


import dev.ronin_engineer.kafka.common.constant.EventType;
import dev.ronin_engineer.kafka.common.dto.KafkaMessage;
import dev.ronin_engineer.kafka.common.util.MessageBuilder;
import dev.ronin_engineer.kafka.producer.wallet.domain.constant.MessageCode;
import dev.ronin_engineer.kafka.producer.wallet.domain.dto.FraudCheckResult;
import dev.ronin_engineer.kafka.producer.wallet.domain.event.TransactionEvent;
import dev.ronin_engineer.kafka.producer.wallet.infrastructure.kafka.KafkaConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FDConnector {

    @Value("${application-name}")
    private String serviceId;

    @Value("${kafka.transaction}")
    private String transactionTopic;

    private final KafkaConnector kafkaConnector;


    public FraudCheckResult send(TransactionEvent transaction) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.COMMAND,
                    MessageCode.FUND_TRANSFER.getCode(),
                    transaction
            );

            KafkaMessage<FraudCheckResult> response = kafkaConnector.sendSync(transactionTopic, message);
            return response.getPayload();
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: " + transactionTopic);
            e.printStackTrace();
            return null;    // TODO: return the default value
        }
    }
}
