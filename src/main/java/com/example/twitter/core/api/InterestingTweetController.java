package com.example.twitter.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/interesting_tweet")
public class InterestingTweetController {

    private final InterestingTweetService interestingTweetService;

    @Autowired
    public InterestingTweetController(InterestingTweetService interestingTweetService) {
        this.interestingTweetService = interestingTweetService;
    }

    @GetMapping
    public List<InterestingTweet> getInterestingTweets(@RequestParam(name = "after", required = false) Long afterTimestamp,
                                                       @RequestParam(name = "before", required = false) Long beforeTimestamp,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        if (beforeTimestamp == null) {
            beforeTimestamp = Long.MAX_VALUE;
        }

        if (afterTimestamp == null) {
            afterTimestamp = 0L;
        }
        return interestingTweetService.getInterestingTweets(afterTimestamp, beforeTimestamp, size);
    }

    @GetMapping("/{id}")
    public InterestingTweet getInterestingTweet(@PathVariable("id") String id) {
        InterestingTweet interestingTweet = interestingTweetService.getInterestingTweet(id);
        if (interestingTweet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interesting tweet not found");
        }
        return interestingTweet;
    }

    @GetMapping("/specific")
    public List<InterestingTweet> getSpecificInterestingTweet(@RequestParam(name = "keyword") String keyword,
                                                              @RequestParam(name = "size",defaultValue = "10") int size){
        return interestingTweetService.getInterestingTweetFromKeyword(keyword,size);
    }


    @GetMapping("/author")
    public List<InterestingTweet> getInterestingTweetFromAuthor(@RequestParam(name = "name") String author,
                                                                @RequestParam(name = "size",defaultValue = "10") int size){
        return interestingTweetService.getInterestingTweetFromAuthor(author,size);
    }

    @GetMapping("/hashtags")
    public Map<String,Integer> getPopularHashtags(){
        return interestingTweetService.getPopularHashtags();
    }
}
