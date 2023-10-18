package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class RECOMMEND {
    private Integer interpreter_recommend;
    private Integer interpreter;
    private Integer location;
    private Integer sort;
    private String reg_date;
    private String upd_date;

    private Map<String, Object> other_info = new HashMap<>();

    public RECOMMEND() {}

    public RECOMMEND(Integer interpreter_recommend) {
        this.interpreter_recommend = interpreter_recommend;
    }
}
