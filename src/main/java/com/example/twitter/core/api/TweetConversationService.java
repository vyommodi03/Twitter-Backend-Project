package com.example.twitter.core.api;

import com.example.twitter.core.config.ESConfig;
import com.example.twitter.core.database.ESUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.FieldSortBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TweetConversationService {
    private final InterestingTweetService interestingTweetService;

    @Autowired
    public TweetConversationService(InterestingTweetService interestingTweetService) {
        this.interestingTweetService = interestingTweetService;
    }

    public TweetConversation getRandomTweetConversation() {
        List<InterestingTweet> interestingTweetList = interestingTweetService.getInterestingTweets(0L, Long.MAX_VALUE, 1);
        return getTweetConversation(interestingTweetList.get(0).getId());
    }

    public TweetConversation getTweetConversation(String tweetId) {

        TweetConversation tweetConversation = new TweetConversation();
        tweetConversation.setInterestingTweet(interestingTweetService.getInterestingTweet(tweetId));

        if (tweetConversation.getInterestingTweet() == null) {
            return tweetConversation;
        }

        RestHighLevelClient client = ESUtils.getClient();

        SearchRequest request = new SearchRequest(ESConfig.TWEET_CONVERSATION_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("parentId", tweetId));
        searchSourceBuilder.sort(new FieldSortBuilder("timestamp_ms").order(SortOrder.DESC));

        request.source(searchSourceBuilder);

        List<TweetReply> tweetReplyList = new ArrayList<>();
        tweetConversation.setTweetReplyList(tweetReplyList);

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) {
            return tweetConversation;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit hit : searchHits) {
            TweetReply tweetReply = null;
            try {
                tweetReply = objectMapper.readValue(hit.getSourceAsString(), TweetReply.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (tweetReply != null) {
                tweetReplyList.add(tweetReply);
            }
        }
        tweetConversation.setTweetReplyList(tweetReplyList);
        return tweetConversation;
    }
}
