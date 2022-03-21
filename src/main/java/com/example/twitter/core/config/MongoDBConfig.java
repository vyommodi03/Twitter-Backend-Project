package com.example.twitter.core.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class MongoDBConfig {
    private MongoDBConfig() {
        throw new IllegalStateException("MongoDBConfig");
    }

    public static String MONGO_URL;
    public static String DATABASE_NAME;
    public static String COLLECTION_NAME;

    static {
        Properties properties = new Properties();
        final URL props = ClassLoader.getSystemResource("mongodb.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        MONGO_URL = properties.getProperty("MONGO_URL");
        DATABASE_NAME = properties.getProperty("DATABASE_NAME");
        COLLECTION_NAME = properties.getProperty("COLLECTION_NAME");
    }
}
