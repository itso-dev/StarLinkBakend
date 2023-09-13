package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

    // auth
    private String returnurl;
    private String enc_data;
    private String auth_iv;
    private String auth_key;
    private String foreigner_number;
    private String foreigner_name;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
