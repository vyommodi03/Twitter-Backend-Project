package com.example.twitter.core.api;

public class TweetReply {
    private String id;
    private String text;
    private String parentId;
    private String created_at;
    private long timestamp_ms;
    private Author author;

    public TweetReply() {
    }

    public TweetReply(String id, String text, String parentId, String created_at, long timestamp_ms, Author author) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
        return "TweetConversation{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", parentId='" + parentId + '\'' +
                ", created_at='" + created_at + '\'' +
                ", timestamp_ms=" + timestamp_ms +
                ", author=" + author +
                '}';
    }
}
