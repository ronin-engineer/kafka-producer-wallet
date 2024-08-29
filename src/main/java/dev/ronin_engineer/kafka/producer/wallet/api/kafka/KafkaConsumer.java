package dev.ronin_engineer.kafka.producer.wallet.api.kafka;

import dev.ronin_engineer.kafka.producer.wallet.api.dto.FraudCheckResultMessage;
import dev.ronin_engineer.kafka.producer.wallet.infrastructure.kafka.KafkaConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final KafkaConnector kafkaConnector;

    @KafkaListener(
            topics = "${kafka.fraud-detection.topic}",
            properties = {"spring.json.value.default.type=dev.ronin_engineer.kafka.producer.wallet.api.dto.FraudCheckResultMessage"},
            concurrency = "${kafka.fraud-detection.concurrency}"
    )
    public void listenFraudCheck(@Payload FraudCheckResultMessage message) {
        log.info("Received a fraud check result: {}", message.getPayload().isFraud());
        kafkaConnector.pushResponseToCache(message);
    }

}
