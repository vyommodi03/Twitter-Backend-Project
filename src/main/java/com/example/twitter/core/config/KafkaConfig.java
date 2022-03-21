package com.example.twitter.core.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class KafkaConfig {
    private KafkaConfig() {
        throw new IllegalStateException("KafkaConfig");
    }

    public static String BOOTSTRAPSERVERS;
    public static final String ACKS_CONFIG = "all";
    public static final String MAX_IN_FLIGHT_CONN = "5";
    public static String TOPIC;

    public static final String COMPRESSION_TYPE = "snappy";
    public static final String RETRIES_CONFIG = Integer.toString(Integer.MAX_VALUE);
    public static final String LINGER_CONFIG = "20";
    public static final String BATCH_SIZE = Integer.toString(32 * 1024);

    static {
        Properties properties = new Properties();
        final URL props = ClassLoader.getSystemResource("kafka.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        BOOTSTRAPSERVERS = properties.getProperty("BOOTSTRAPSERVERS");
        TOPIC = properties.getProperty("TOPIC");
    }

}