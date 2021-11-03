package com.osaid.taskmaster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Task {
    private String title;
    private String body;
    private String state;

    public Task() {
    }

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
