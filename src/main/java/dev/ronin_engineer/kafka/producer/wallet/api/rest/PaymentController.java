package dev.ronin_engineer.kafka.producer.wallet.api.rest;

import dev.ronin_engineer.kafka.producer.wallet.domain.dto.FraudCheckResult;
import dev.ronin_engineer.kafka.producer.wallet.domain.dto.TransactionRequest;
import dev.ronin_engineer.kafka.producer.wallet.domain.event.TransactionEvent;
import dev.ronin_engineer.kafka.producer.wallet.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final TransactionService transactionService;


    @PostMapping
    public TransactionEvent createTransaction(@RequestBody TransactionRequest request) {
        return transactionService.execute(request);
    }

    @PostMapping("/sync")
    public FraudCheckResult checkTransactionFraud(@RequestBody TransactionRequest request) {
        return transactionService.checkFraud(request);
    }

}
