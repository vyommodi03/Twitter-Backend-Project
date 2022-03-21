package com.example.twitter.core.api;

public class Author {
    private String id;
    private String name;
    private String screen_name;

    public Author() {
    }

    public Author(String id, String name, String screen_name) {
        this.id = id;
        this.name = name;
        this.screen_name = screen_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", screen_name='" + screen_name + '\'' +
                '}';
    }
}
