package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class INFO {
    private Integer member_info;
    private Integer member;
    private Integer key;
    private String info_type;
    private String title;
    private String content;
    private Boolean check_yn;
    private Integer type;
    private String reg_date;
    private String upd_date;

    public INFO() {}

    public INFO(Integer member, Integer key, String info_type, String title, String content) {
        this.member = member;
        this.key = key;
        this.info_type = info_type;
        this.title = title;
        this.content = content;
    }
}
