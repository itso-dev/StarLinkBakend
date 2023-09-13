package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class MEMBER {
    private Integer member;
    private Integer country;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private String name;
    private String phone;
    private String gender;
    private String address;
    private String address_detail;
    private String marketing_yn;

    private Boolean auto_login;

    public MEMBER() {}

    public MEMBER(Integer member) {
        this.member = member;
    }
}
