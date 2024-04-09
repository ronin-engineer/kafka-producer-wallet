package dev.ronin_engineer.kafka.producer.wallet.domain.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionEvent {
    private String transactionId;
    private String transactionType;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private Integer status;
    private Long createdAt;
}
