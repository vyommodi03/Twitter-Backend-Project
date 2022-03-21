package com.example.twitter.core.database;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESUtils {
    private static RestHighLevelClient client = null;

    private ESUtils() {
        throw new IllegalStateException("ESUtils");
    }

    public synchronized static RestHighLevelClient getClient() {
        if(client == null){
            RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200));
            client = new RestHighLevelClient(builder);
        }
        return client;
    }

    public synchronized static void close(){
        if(client != null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Elastic search closed");
    }
}
