package com.rental.kafka;

import com.rental.repository.Repository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerGroup {
    private final List<KafkaConsumer<Long, String>> kafkaConsumers = new ArrayList<>();
    private static final String RENT_TOPIC = "Rent";
    private final Repository repository = new Repository();

    public void init() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "rentsGroup");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        for (int i = 0; i <3; i++){
            KafkaConsumer<Long,String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
            kafkaConsumer.subscribe(List.of(RENT_TOPIC));
            kafkaConsumers.add(kafkaConsumer);
            System.out.println("Subscribed to " + RENT_TOPIC);
            System.out.println("Created consumer " + (i + 1));
        }
    }

    public void consume(KafkaConsumer<Long, String> kafkaConsumer) {
        try{
            kafkaConsumer.poll(Duration.ofMillis(0));
            Set<TopicPartition> consumerAssignment = kafkaConsumer.assignment();
            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);

            kafkaConsumer.seekToBeginning(consumerAssignment);
            while (true){
                ConsumerRecords<Long, String> records = kafkaConsumer.poll(timeout);
                for (ConsumerRecord<Long, String> record : records){
                    repository.add(record.value());
                    kafkaConsumer.commitSync();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }

    public void groupConsume() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (KafkaConsumer<Long, String> kafkaConsumer : kafkaConsumers) {
            executorService.execute(() -> consume(kafkaConsumer));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown initiated. Closing consumers...");


            for (KafkaConsumer<Long, String> kafkaConsumer : kafkaConsumers) {
                kafkaConsumer.wakeup();
            }

            executorService.shutdown();
            System.out.println("Consumers and executor service have been closed.");
        }));

    }
}

