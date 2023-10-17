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
public class REPORT {
    private Integer booking_report;
    private Integer booking;
    private String title;
    private String content;
    private Integer state;
    private String files;
    private String reg_date;
    private String upd_date;

    private ArrayList<MultipartFile> files_new;
    private String files_del;

    private Map<String, Object> other_info = new HashMap<>();

    public REPORT() {}

    public REPORT(Integer booking_report) {
        this.booking_report = booking_report;
    }
}
