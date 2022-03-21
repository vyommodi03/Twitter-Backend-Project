package com.example.twitter.core.utils;

import com.example.twitter.core.config.ESConfig;
import com.example.twitter.core.config.RedisConfig;
import com.example.twitter.core.database.ESUtils;
import com.example.twitter.core.database.RedisUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.Request;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.Response;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;

public class AtomicMethods {
    private static final Logger logger = LoggerFactory.getLogger(AtomicMethods.class);

    private AtomicMethods() {
        throw new IllegalStateException("AtomicMethods");
    }

    public static synchronized void processInterestingTweet(JsonNode jsonNode) {
        try (Jedis jedis = RedisUtils.getJedisPool().getResource()) {
            jedis.sadd(RedisConfig.INTERESTING_TWEET_KEY, jsonNode.get("id").asText());
        }
        // Store INTERESTING_TWEET in ES
        RestHighLevelClient client = ESUtils.getClient();

        IndexRequest request = new IndexRequest(ESConfig.INTERESTING_TWEET_INDEX);
        request.id(jsonNode.get("id").asText()); //Assign an ID to the document.

        HashMap<String, Object> json = new HashMap<>();
        json.put("id", jsonNode.get("id").asText());
        json.put("text", jsonNode.get("text").asText());
        json.put("created_at", jsonNode.get("created_at").asText());
        json.put("timestamp_ms", jsonNode.get("timestamp_ms").asLong());

        HashMap<String, String> author = new HashMap<>();
        author.put("id", jsonNode.get("user").get("id").asText());
        author.put("name", jsonNode.get("user").get("name").asText());
        author.put("screen_name", jsonNode.get("user").get("screen_name").asText());

        json.put("author", author);
        request.source(json, XContentType.JSON);

        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//            logger.info("INTERESTING_TWEET : " + jsonNode.get("id").asText() + " " + response.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void processNonInterestingTweet(JsonNode jsonNode) {
        try (Jedis jedis = RedisUtils.getJedisPool().getResource()) {
            jedis.sadd(RedisConfig.NON_INTERESTING_TWEET_KEY, jsonNode.get("id").asText());
        }
        // remove child tweet from ES conversation
        RestHighLevelClient client = ESUtils.getClient();

        String endpoint = String.format("%s/_delete_by_query",ESConfig.TWEET_CONVERSATION_INDEX);
        Request request = new Request("POST",endpoint);
        String json = String.format("{\"query\":{\"term\":{\"parentId\":{\"value\":\"%s\"}}}}",jsonNode.get("id").asText());
        request.setEntity(new NStringEntity(json, ContentType.APPLICATION_JSON));
        try {
            Response response =  client.getLowLevelClient().performRequest(request);
//            logger.info("TWEET_CONVERSATION_DELETED : "+jsonNode.get("id").asText()+" - "+response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized void processReply(JsonNode jsonNode) {
        // add in ES conv if parent not present in non-interesting tweet
        String parentTweetId = jsonNode.get("in_reply_to_status_id").asText();

        try (Jedis jedis = RedisUtils.getJedisPool().getResource()) {
            if (!jedis.sismember(RedisConfig.NON_INTERESTING_TWEET_KEY, parentTweetId)) {
                // Store reply tweet in ES conversation
                RestHighLevelClient client = ESUtils.getClient();

                IndexRequest request = new IndexRequest(ESConfig.TWEET_CONVERSATION_INDEX);
                request.id(jsonNode.get("id").asText()); //Assign an ID to the document.

                HashMap<String, Object> json = new HashMap<>();
                json.put("id", jsonNode.get("id").asText());
                json.put("text", jsonNode.get("text").asText());
                json.put("created_at", jsonNode.get("created_at").asText());
                json.put("timestamp_ms", jsonNode.get("timestamp_ms").asLong());
                json.put("parentId",jsonNode.get("in_reply_to_status_id").asText());

                HashMap<String, String> author = new HashMap<>();
                author.put("id", jsonNode.get("user").get("id").asText());
                author.put("name", jsonNode.get("user").get("name").asText());
                author.put("screen_name", jsonNode.get("user").get("screen_name").asText());

                json.put("author", author);
                request.source(json, XContentType.JSON);

                try {
                    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//                    logger.info("TWEET_CONVERSATION : " + jsonNode.get("id").asText() + " " + response.status());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
