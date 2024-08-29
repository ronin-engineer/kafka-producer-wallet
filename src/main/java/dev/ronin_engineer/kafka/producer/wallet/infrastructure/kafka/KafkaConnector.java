package dev.ronin_engineer.kafka.producer.wallet.infrastructure.kafka;


import dev.ronin_engineer.kafka.common.dto.KafkaMessage;
import dev.ronin_engineer.kafka.producer.wallet.domain.dto.FraudCheckResult;
import dev.ronin_engineer.kafka.producer.wallet.infrastructure.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConnector {

    private static final int BACKOFF_MULTIPLIER = 2;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisService redisService;

    @Value("${spring.redis.timeout}")   // biz timeout should be different from cache infra timeout
    private Integer cacheTimeout;

    @SneakyThrows
    public KafkaMessage<FraudCheckResult> syncRequest(String topic, KafkaMessage request) {
        kafkaTemplate.send(topic, request);
        log.info("Produced a message to topic: {}, value: {}", topic, request);

        Thread.sleep(5);
        int leftTime = cacheTimeout;
        int initDelay = 20;
        int attempt = 0;

        // exponential backoff
        while (leftTime >= 0) {
            var response = getResponseFromCache(request.getMeta().getServiceId(), request.getMeta().getMessageId());
            if (response != null) {
                return response;
            }

            var delay = (int) (initDelay * Math.pow(BACKOFF_MULTIPLIER, attempt++));
            leftTime -= delay;
            Thread.sleep(delay);
        }

        return null; // should return a kafka message object with metadata type: timeout
    }

    public KafkaMessage getResponseFromCache(String serviceId, String requestId) {
        var key = generateResponseKey(serviceId, requestId);
        var result = redisService.get(key);
        if (result == null)
            return null;

        CompletableFuture.runAsync(() -> redisService.delete(key));

        return (KafkaMessage) result;
    }

    // TODO: add retry logic
    public void pushResponseToCache(KafkaMessage message) {
        String key = generateResponseKey(
                message.getMeta().getServiceId(),
                message.getMeta().getOriginalMessageId()
        );
        redisService.set(key, message, cacheTimeout);
    }


    private static String generateResponseKey(String serviceId, String requestId) {
        return String.join(":", serviceId, requestId);
    }
}
