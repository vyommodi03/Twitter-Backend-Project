package com.example.twitter.core.utils;

import com.example.twitter.core.config.TwitterConfig;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.concurrent.BlockingQueue;

public class TwitterClient {
    private TwitterClient() {
        throw new IllegalStateException("Twitter Client");
    }
    public static Client getClient(BlockingQueue<String> msgQueue) {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
//        StatusesFilterEndpoint hbEndpoint = new StatusesFilterEndpoint();
//        List<String>  trackTerms = Lists.newArrayList("ipl");
//        hbEndpoint.trackTerms(trackTerms);
        StatusesSampleEndpoint hbEndpoint = new StatusesSampleEndpoint();
        hbEndpoint.stallWarnings(false);
        // Twitter API and tokens
        Authentication hosebirdAuth = new OAuth1(TwitterConfig.CONSUMER_KEYS, TwitterConfig.CONSUMER_SECRETS, TwitterConfig.TOKEN, TwitterConfig.TOKEN_SECRET);

        /** Creating a client   */
        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hbEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }
}
