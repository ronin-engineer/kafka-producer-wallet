package dev.ronin_engineer.kafka.producer.wallet.infrastructure.service;

import dev.ronin_engineer.kafka.common.dto.MessageMeta;
import dev.ronin_engineer.kafka.producer.wallet.api.dto.FraudCheckResultMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, int timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }


    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @PostConstruct
    public void init() {
        var message = new FraudCheckResultMessage();
        var meta = new MessageMeta();
        meta.setServiceId("test");
        message.setMeta(meta);
        set("k1", message);
        FraudCheckResultMessage value = (FraudCheckResultMessage) get("k1");
        log.info("value: {}", value.getMeta().getServiceId());
    }
}
