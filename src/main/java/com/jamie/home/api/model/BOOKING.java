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
public class BOOKING {
    private Integer booking;
    private Integer interpreter;
    private Integer member;
    private String start;
    private String end;
    private Integer purpose;
    private Integer category;
    private Integer people;
    private Integer people_cnt;
    private String phone_type;
    private String phone_number;
    private Boolean pickup_yn;
    private Integer pickup_price;
    private String plane_name;
    private String plane_start;
    private String plane_end;
    private Boolean insurance_yn;
    private String request_msg;
    private Integer state;
    private String cancel_msg;
    private Float grade;
    private String reg_date;
    private String upd_date;

    private Map<String, Object> other_info = new HashMap<>();

    public BOOKING() {}

    public BOOKING(Integer booking) {
        this.booking = booking;
    }
}
