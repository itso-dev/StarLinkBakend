package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class ROOM {
    private Integer room;
    private Integer interpreter;
    private Integer member;
    private String reg_date;
    private String upd_date;

    private Map<String, Object> other_info = new HashMap<>();

    public ROOM() {}

    public ROOM(Integer room) {
        this.room = room;
    }
}
