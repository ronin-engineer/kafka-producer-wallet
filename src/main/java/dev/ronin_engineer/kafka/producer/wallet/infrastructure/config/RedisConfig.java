package dev.ronin_engineer.kafka.producer.wallet.infrastructure.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final KryoSerializer kryoSerializer;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        var stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        var valueObjectSerializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object value) throws SerializationException {
                return kryoSerializer.serialize(value);
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                return kryoSerializer.deserialize(bytes);
            }
        };

        template.setValueSerializer(valueObjectSerializer);
        template.setHashValueSerializer(valueObjectSerializer);

        return template;
    }
}
