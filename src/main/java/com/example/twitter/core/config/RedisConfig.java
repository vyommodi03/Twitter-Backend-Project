package com.example.twitter.core.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class RedisConfig {
    private RedisConfig() {
        throw new IllegalStateException("Redis Config");
    }

    public static String INTERESTING_TWEET_KEY;
    public static String NON_INTERESTING_TWEET_KEY;

    static {
        Properties properties = new Properties();
        final URL props = ClassLoader.getSystemResource("redis.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        INTERESTING_TWEET_KEY = properties.getProperty("INTERESTING_TWEET_KEY");
        NON_INTERESTING_TWEET_KEY = properties.getProperty("NON_INTERESTING_TWEET_KEY");
    }
}
