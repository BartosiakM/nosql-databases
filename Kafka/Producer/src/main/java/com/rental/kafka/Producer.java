package com.rental.kafka;

import com.rental.model.Rent;
import com.rental.model.RentToSend;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer {
    private static KafkaProducer<Long, String> kafkaProducer;
    private static final String RENT_TOPIC = "Rent";
    private static final ObjectMapper mapper = createObjectMapper();

    public Producer() throws ExecutionException, InterruptedException {
        initProducer();
        createTopic();
    }

    private static void initProducer() {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        producerConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "rentalProducer");

        kafkaProducer = new KafkaProducer<>(producerConfig);
        kafkaProducer.initTransactions();
    }

    public void createTopic() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9192,kafka:9292,kafka:9392");
        try (Admin admin = Admin.create(props)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, 3, (short) 3);
            CreateTopicsOptions options = new CreateTopicsOptions().timeoutMs(10000).validateOnly(false).retryOnQuotaViolation(true);
            CreateTopicsResult createTopicsResult = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> future = createTopicsResult.values().get(RENT_TOPIC);
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRent(Rent rent, String rentalName) {
        try {
            RentToSend rentToSend = new RentToSend(rent, rentalName);
            String json = mapper.writeValueAsString(rentToSend);

            ProducerRecord<Long, String> record = new ProducerRecord<>(RENT_TOPIC, rent.getId(), json);
            kafkaProducer.beginTransaction();
            kafkaProducer.send(record);
            kafkaProducer.commitTransaction();
        } catch (Exception e) {
            kafkaProducer.abortTransaction();
            e.printStackTrace();
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


}
