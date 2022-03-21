package com.example.twitter.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/tweet_conversation")
public class TweetConversationController {

    private final TweetConversationService tweetConversationService;

    @Autowired
    public TweetConversationController(TweetConversationService tweetConversationService) {
        this.tweetConversationService = tweetConversationService;
    }

    @GetMapping
    public TweetConversation getConversation() {
        return tweetConversationService.getRandomTweetConversation();
    }

    @GetMapping("/{id}")
    public TweetConversation getConversation(@PathVariable("id") String id) {

        TweetConversation tweetConversation = tweetConversationService.getTweetConversation(id);
        if (tweetConversation.getInterestingTweet() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interesting tweet not found");
        }
        return tweetConversation;
    }
}
