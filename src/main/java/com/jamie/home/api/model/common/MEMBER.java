package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class MEMBER {
    private Integer member;
    private String id;
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
    private Boolean marketing_yn;
    private String profile_file;

    private Boolean auto_login;

    private ArrayList<MultipartFile> profile_file_new;
    private Map<String, Object> other_info = new HashMap<>();

    public MEMBER() {}

    public MEMBER(Integer member) {
        this.member = member;
    }
}
