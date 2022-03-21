package com.example.twitter.core.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class TwitterConfig {
    private TwitterConfig() {
        throw new IllegalStateException("TwitterConfig");
    }

    public static String CONSUMER_KEYS;
    public static String CONSUMER_SECRETS;
    public static String TOKEN;
    public static String TOKEN_SECRET;
    public static String BEARER_TOKEN;

    static {
        Properties properties = new Properties();
        final URL props = ClassLoader.getSystemResource("twitter.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        CONSUMER_KEYS = properties.getProperty("CONSUMER_KEYS");
        CONSUMER_SECRETS = properties.getProperty("CONSUMER_SECRETS");
        TOKEN = properties.getProperty("TOKEN");
        TOKEN_SECRET = properties.getProperty("TOKEN_SECRET");
        BEARER_TOKEN = properties.getProperty("BEARER_TOKEN");
    }
}
