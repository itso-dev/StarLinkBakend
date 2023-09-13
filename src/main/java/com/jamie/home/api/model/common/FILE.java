package com.jamie.home.api.model.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FILE {
    private String name;
    private String uuid;
    private String path;
    private Boolean del;
}
