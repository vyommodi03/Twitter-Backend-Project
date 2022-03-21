package com.example.twitter.core;

import com.example.twitter.core.config.KafkaConfig;
import com.example.twitter.core.utils.TwitterClient;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable{
    private boolean isContinue = true;
    private CountDownLatch latch = null;

    public Producer(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        isContinue = true;
        String topic = KafkaConfig.TOPIC;

        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAPSERVERS);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create safe Producer
        prop.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        prop.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACKS_CONFIG);
        prop.setProperty(ProducerConfig.RETRIES_CONFIG, KafkaConfig.RETRIES_CONFIG);
        prop.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, KafkaConfig.MAX_IN_FLIGHT_CONN);

        // Additional settings for high throughput producer
        prop.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, KafkaConfig.COMPRESSION_TYPE);
        prop.setProperty(ProducerConfig.LINGER_MS_CONFIG, KafkaConfig.LINGER_CONFIG);
        prop.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, KafkaConfig.BATCH_SIZE);

        // Create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(30);

        Client client = TwitterClient.getClient(msgQueue);
        client.connect();

        try {
            while (!client.isDone() && isContinue) {
                String msg = msgQueue.poll(5, TimeUnit.SECONDS);
                if (msg != null) {
                    producer.send(new ProducerRecord<>(topic, null, msg), (recordMetadata, e) -> {
                        if (e != null) {
                            System.out.println("Some error OR something bad happened");
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            client.stop();
            producer.flush();
            producer.close();
            System.out.println("Producer closed");
            latch.countDown();
        }
    }

    public void close() {
        isContinue = false;
    }
}
