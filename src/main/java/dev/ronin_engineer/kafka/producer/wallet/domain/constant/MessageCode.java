package dev.ronin_engineer.kafka.producer.wallet.domain.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageCode {

    FUND_TRANSFER("FUND_TRANSFER"),
    ;

    private final String code;
}
