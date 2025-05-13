package com.example.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CfrReference {
    @JsonProperty("title")
    private Integer title;

    @JsonProperty("chapter")
    private String chapter;

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}