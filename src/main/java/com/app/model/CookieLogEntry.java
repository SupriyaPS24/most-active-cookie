package com.app.model;

import java.time.ZonedDateTime;

public class CookieLogEntry {

    private String cookie;
    private ZonedDateTime timestamp;

    // Constructor, Getters, and Setters
    public CookieLogEntry(String cookie, ZonedDateTime timestamp) {
        this.cookie = cookie;
        this.timestamp = timestamp;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
