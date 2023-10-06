package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class CHAT {
    private Integer chat;
    private Integer room;
    private Integer member;
    private Integer type;
    private String content;
    private String reg_date;
    private String upd_date;

    private Map<String, Object> other_info = new HashMap<>();
    private ArrayList<MultipartFile> files_new;

    public CHAT() {}

    public CHAT(Integer chat) {
        this.chat = chat;
    }
}
