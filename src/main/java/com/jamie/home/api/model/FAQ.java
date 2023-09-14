package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FAQ {
    private Integer faq;
    private String question;
    private String answer;
    private String reg_date;
    private String upd_date;

    public FAQ() {}

    public FAQ(Integer faq) {
        this.faq = faq;
    }
}
