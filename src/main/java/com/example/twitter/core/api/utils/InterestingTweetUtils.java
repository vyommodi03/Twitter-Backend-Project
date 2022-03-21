package com.example.twitter.core.api.utils;

import com.example.twitter.core.api.InterestingTweet;
import com.example.twitter.core.config.ESConfig;
import com.example.twitter.core.database.ESUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterestingTweetUtils {
    private InterestingTweetUtils() {
        throw new IllegalStateException("InterestingTweetUtils");
    }

    public static List<InterestingTweet> getSearchResult(SearchSourceBuilder searchSourceBuilder){
        RestHighLevelClient client = ESUtils.getClient();

        SearchRequest request = new SearchRequest(ESConfig.INTERESTING_TWEET_INDEX);
        request.source(searchSourceBuilder);

        List<InterestingTweet> interestingTweetList = new ArrayList<>();

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) {
            return interestingTweetList;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit hit : searchHits) {
            InterestingTweet interestingTweet = null;
            try {
                interestingTweet = objectMapper.readValue(hit.getSourceAsString(), InterestingTweet.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (interestingTweet != null) {
                interestingTweetList.add(interestingTweet);
            }
        }
        return interestingTweetList;
    }
}
