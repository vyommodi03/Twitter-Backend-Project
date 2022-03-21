package com.example.twitter.core.database;

import com.example.twitter.core.config.MongoDBConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbUtils {
    private MongoClient mongoClient = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> dbCollection = null;

    public MongoDbUtils() {
        mongoClient = new MongoClient(new MongoClientURI(MongoDBConfig.MONGO_URL));
        database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        dbCollection = database.getCollection(MongoDBConfig.COLLECTION_NAME);
    }

    public List<String> getRegex() {
        MongoCursor<Document> cursor = dbCollection.find().iterator();
        List<String> regex = new ArrayList<>();
        while (cursor.hasNext()) {
            regex.add(cursor.next().getString("regex"));
        }
        return regex;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
