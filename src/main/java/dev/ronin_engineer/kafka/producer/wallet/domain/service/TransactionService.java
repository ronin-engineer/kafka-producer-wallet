package dev.ronin_engineer.kafka.producer.wallet.domain.service;

import dev.ronin_engineer.kafka.producer.wallet.domain.constant.TransactionStatus;
import dev.ronin_engineer.kafka.producer.wallet.domain.constant.TransactionType;
import dev.ronin_engineer.kafka.producer.wallet.domain.dto.TransactionRequest;
import dev.ronin_engineer.kafka.producer.wallet.domain.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionProducer producer;


    public TransactionEvent execute(TransactionRequest request) {
        var transactionId = UUID.randomUUID().toString();

        // fake the execution of the transaction
        var transaction = TransactionEvent.builder()
                .transactionId(transactionId)
                .transactionType(TransactionType.PAYMENT.getType())
                .debitAccount(request.getDebitAccount())
                .creditAccount(request.getCreditAccount())
                .amount(request.getAmount())
                .status(TransactionStatus.SUCCESSFUL.getStatus())
                .createdAt(System.currentTimeMillis())
                .build();

        producer.send(transaction);

        return transaction;
    }
}
