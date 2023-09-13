package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FIELD {
    private Integer field;
    private Integer field_index;
    private Integer sort;
    private String type_name;
    private String name;
    private String reg_date;
    private String upd_date;

    public FIELD() {}

    public FIELD(Integer field) {
        this.field = field;
    }
}
