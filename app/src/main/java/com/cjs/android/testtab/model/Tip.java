package com.cjs.android.testtab.model;

import java.io.Serializable;

public class Tip implements Serializable {
    private String title;
    private String content;

    public Tip() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
