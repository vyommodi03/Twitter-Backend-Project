package com.example.twitter.core.api;

public class InterestingTweet {
    private String id;
    private String text;
    private String created_at;
    private long timestamp_ms;
    private Author author;

    public InterestingTweet() {
    }

    public InterestingTweet(String id, String text, String created_at, long timestamp_ms, Author author) {
        this.id = id;
        this.text = text;
        this.created_at = created_at;
        this.timestamp_ms = timestamp_ms;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(long timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "InterestingTweet{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", created_at='" + created_at + '\'' +
                ", timestamp_ms=" + timestamp_ms +
                ", author=" + author +
                '}';
    }
}
