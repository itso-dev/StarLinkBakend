package com.jamie.home.api.dao;

import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface InterDao {
    List<INTERPRETER> getListInterpreter(SEARCH search);
    Integer getListInterpreterCnt(SEARCH search);
    INTERPRETER getInterpreter(INTERPRETER interpreter);
    Integer insertInterpreter(INTERPRETER interpreter);
    Integer updateInterpreter(INTERPRETER interpreter);
    Integer deleteInterpreter(INTERPRETER interpreter);
    void insertInterpreterLocation(Map<String, Object> param);
    List<Integer> getListInterpreterLocation(Integer interpreter);
    void deleteInterpreterLocation(Map<String, Object> param);
    void insertInterpreterCategory(Map<String, Object> param);
    List<Integer> getListInterpreterCategory(Integer interpreter);
    void deleteInterpreterCategory(Map<String, Object> param);
    void insertInterpreterLanguage(Map<String, Object> param);
    List<Integer> getListInterpreterLanguage(Integer interpreter);
    void deleteInterpreterLanguage(Map<String, Object> param);
}
