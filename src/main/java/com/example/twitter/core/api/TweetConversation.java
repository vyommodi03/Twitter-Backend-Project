package com.example.twitter.core.api;

import java.util.List;

public class TweetConversation {

    private InterestingTweet interestingTweet = null;
    private List<TweetReply> tweetReplyList = null;

    public TweetConversation() {
    }

    public TweetConversation(InterestingTweet interestingTweet, List<TweetReply> tweetReplyList) {
        this.interestingTweet = interestingTweet;
        this.tweetReplyList = tweetReplyList;
    }

    public InterestingTweet getInterestingTweet() {
        return interestingTweet;
    }

    public void setInterestingTweet(InterestingTweet interestingTweet) {
        this.interestingTweet = interestingTweet;
    }

    public List<TweetReply> getTweetReplyList() {
        return tweetReplyList;
    }

    public void setTweetReplyList(List<TweetReply> tweetReplyList) {
        this.tweetReplyList = tweetReplyList;
    }

    @Override
    public String toString() {
        return "TweetConversation{" +
                "interestingTweet=" + interestingTweet +
                ", tweetReplyList=" + tweetReplyList +
                '}';
    }
}
