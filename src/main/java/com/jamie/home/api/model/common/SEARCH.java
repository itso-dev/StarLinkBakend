package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@ToString
@Getter
@Setter
public class SEARCH {
    // 리스트
    private Integer page;
    private Integer page_block;
    private Integer start;
    private String order_type;
    private String search_type;
    private String search_keyword;

    // 회원
    private Integer member;
    private String email;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private Boolean check_yn;

    // 통역사
    private Integer interpreter;
    private Integer country;
    private Integer location;
    private List<Integer> category_list;
    private Integer language;
    private String start_date;
    private String end_date;
    private Integer day_unit;
    private Integer min_price;
    private Integer max_price;
    private Integer car;
    private Integer pickup;
    private Integer gender;
    private Integer state;
    private Boolean recommend;
    private List<Integer> location_list;

    // 예약
    private Boolean avail_date;

    // 채팅
    private Integer room;
    private Integer chat;
    private String chat_type;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
