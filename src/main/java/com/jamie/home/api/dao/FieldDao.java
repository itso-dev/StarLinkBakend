package com.jamie.home.api.dao;

import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FieldDao {
    List<FIELD> getListField(SEARCH search);
    FIELD getField(FIELD field);
}
