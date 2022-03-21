package com.example.twitter.core.api;

import com.example.twitter.core.api.utils.InterestingTweetUtils;
import com.example.twitter.core.config.ESConfig;
import com.example.twitter.core.database.ESUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.twittertext.Extractor;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.FieldSortBuilder;
import org.opensearch.search.sort.ScoreSortBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InterestingTweetService {

    public InterestingTweet getInterestingTweet(String id) {
        RestHighLevelClient client = ESUtils.getClient();

        InterestingTweet interestingTweet = null;
        GetRequest request = new GetRequest(ESConfig.INTERESTING_TWEET_INDEX, id);
        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null || response.getSourceAsString() == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            interestingTweet = mapper.readValue(response.getSourceAsString(), InterestingTweet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return interestingTweet;
    }

    public List<InterestingTweet> getInterestingTweets(long afterTimestamp, long beforeTimestamp, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("timestamp_ms").lt(beforeTimestamp).gt(afterTimestamp));
        searchSourceBuilder.sort(new FieldSortBuilder("timestamp_ms").order(SortOrder.DESC));
        searchSourceBuilder.size(size);

        return InterestingTweetUtils.getSearchResult(searchSourceBuilder);
    }

    public List<InterestingTweet> getInterestingTweetFromKeyword(String keyword, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("text", keyword));
        searchSourceBuilder.sort(new ScoreSortBuilder());
        searchSourceBuilder.size(size);

        return InterestingTweetUtils.getSearchResult(searchSourceBuilder);
    }

    public List<InterestingTweet> getInterestingTweetFromAuthor(String author, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(author, "author.name", "author.screen_name"));
        searchSourceBuilder.sort(new ScoreSortBuilder());
        searchSourceBuilder.size(size);

        return InterestingTweetUtils.getSearchResult(searchSourceBuilder);
    }

    public Map<String, Integer> getPopularHashtags() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new FieldSortBuilder("timestamp_ms").order(SortOrder.DESC));
        searchSourceBuilder.size(1000);

        List<InterestingTweet> interestingTweetList = InterestingTweetUtils.getSearchResult(searchSourceBuilder);
        Map<String, Integer> hashtags = new HashMap<>();

        Extractor extractor = new Extractor();

        for (InterestingTweet interestingTweet : interestingTweetList) {
            String text = interestingTweet.getText();
            List<String> stringList = extractor.extractHashtags(text);
            stringList.forEach(s -> hashtags.put(s.toLowerCase().trim(), hashtags.getOrDefault(s.toLowerCase().trim(), 0) + 1));
        }

        Map<String, Integer> result = hashtags.entrySet()
                .stream()
                .sorted((i1, i2)
                        -> i2.getValue().compareTo(
                        i1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        Map<String, Integer> topHashtags = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            topHashtags.put(entry.getKey(), entry.getValue());
            count++;
            if (count == 25) {
                break;
            }
        }
        return topHashtags;
    }
}
