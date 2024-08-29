package dev.ronin_engineer.kafka.producer.wallet.infrastructure.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.ronin_engineer.kafka.common.dto.KafkaMessage;
import dev.ronin_engineer.kafka.producer.wallet.api.dto.FraudCheckResultMessage;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class KryoSerializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    public byte[] serialize(Object o) {
        if (o == null) {
            return new byte[]{};
        }

        var kryo = kryoThreadLocal.get();
        var outputStream = new ByteArrayOutputStream();
        var output = new Output(outputStream);
        kryo.writeClassAndObject(output, o);
        output.close();

        return outputStream.toByteArray();
    }

    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        var kryo = kryoThreadLocal.get();
        var inputStream = new ByteArrayInputStream(bytes);
        var input = new Input(inputStream);
        var o = kryo.readClassAndObject(input);
        input.close();

        return o;
    }
}
