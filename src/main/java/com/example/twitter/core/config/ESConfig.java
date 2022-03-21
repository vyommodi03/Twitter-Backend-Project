package com.example.twitter.core.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ESConfig {
    private ESConfig() {
        throw new IllegalStateException("ES Config");
    }

    public static String INTERESTING_TWEET_INDEX;
    public static String TWEET_CONVERSATION_INDEX;

    static {
        Properties properties = new Properties();
        final URL props = ClassLoader.getSystemResource("elasticsearch.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        INTERESTING_TWEET_INDEX = properties.getProperty("INTERESTING_TWEET_INDEX");
        TWEET_CONVERSATION_INDEX = properties.getProperty("TWEET_CONVERSATION_INDEX");
    }
}
