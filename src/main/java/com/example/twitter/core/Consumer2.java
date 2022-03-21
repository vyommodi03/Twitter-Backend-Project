package com.example.twitter.core;

import com.example.twitter.core.config.KafkaConfig;
import com.example.twitter.core.utils.AtomicMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Consumer2 implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Consumer2.class);

    private boolean isContinue = true;
    private CountDownLatch latch = null;

    public Consumer2(CountDownLatch latch) {
        this.latch = latch;
        isContinue = true;
    }

    @Override
    public void run() {
        String groupId = "kafka-testing-grp-2";
        String topic = KafkaConfig.TOPIC;

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAPSERVERS);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));

        while (isContinue) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(consumerRecord.value());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (jsonNode != null && jsonNode.has("text") && jsonNode.hasNonNull("in_reply_to_status_id")) {
//                    logger.info(consumerRecord.value());
                    AtomicMethods.processReply(jsonNode);
                }
            }
            consumer.commitSync();
        }
        System.out.println("Consumer2 about to close");
        consumer.close();
        System.out.println("Consumer2 closed");
        latch.countDown();
    }
    public void close() {
        isContinue = false;
    }
}
