package dev.ronin_engineer.kafka.producer.wallet.domain.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionType {

    PAYMENT("PAYMENT"),
    TOPUP("TOPUP"),
    ;


    private final String type;

}
