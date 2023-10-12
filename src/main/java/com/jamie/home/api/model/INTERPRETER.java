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
public class INTERPRETER {
    private Integer interpreter;
    private Integer member;
    private Integer country;
    private String location;
    private String category;
    private String language;
    private Integer day_unit;
    private Integer day_price;
    private Integer car;
    private Integer pickup;
    private Integer pickup_unit;
    private Integer pickup_price;
    private String info;
    private String profile;
    private String career;
    private String extra;
    private String files;
    private Integer state;
    private String reject_msg;
    private String reg_date;
    private String upd_date;

    private ArrayList<MultipartFile> files_new;
    private String files_del;

    private Map<String, Object> other_info = new HashMap<>();

    public INTERPRETER() {}

    public INTERPRETER(Integer interpreter) {
        this.interpreter = interpreter;
    }
}
