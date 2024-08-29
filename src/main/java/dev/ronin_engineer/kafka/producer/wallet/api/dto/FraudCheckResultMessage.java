package dev.ronin_engineer.kafka.producer.wallet.api.dto;

import dev.ronin_engineer.kafka.common.dto.KafkaMessage;
import dev.ronin_engineer.kafka.producer.wallet.domain.dto.FraudCheckResult;

public class FraudCheckResultMessage extends KafkaMessage<FraudCheckResult> {
}
