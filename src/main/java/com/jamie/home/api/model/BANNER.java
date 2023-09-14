package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class BANNER {
    private Integer banner;
    private Integer sort;
    private String name;
    private String link;
    private String files;
    private String reg_date;
    private String upd_date;

    private ArrayList<MultipartFile> files_new;
    private String files_del;

    public BANNER() {}

    public BANNER(Integer banner) {
        this.banner = banner;
    }
}