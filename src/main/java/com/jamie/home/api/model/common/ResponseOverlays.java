package com.jamie.home.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseOverlays<T> {

    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("code")
    public int code;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("idea")
    public String idea;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("body")
    public T body;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("total")
    public int total;
    public ResponseOverlays(int code, String idea, T body, int total) {
        this.code = code;
        this.idea = idea;
        this.body = body;
        this.total = total;
    }
    public ResponseOverlays(int code, String idea, T body) {
        this.code = code;
        this.idea = idea;
        this.body = body;
    }
}