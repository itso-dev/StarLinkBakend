package com.jamie.home.api.dao;

import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.RECOMMEND;
import com.jamie.home.api.model.common.MEMBER;
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
    void insertInterpreterMultiChoice(Map<String, Object> param);
    List<Integer> getListInterpreterMultiChoice(Integer interpreter, String type_value);
    void deleteInterpreterMultiChoice(Map<String, Object> param);
    Integer updateInterpreterLike(SEARCH search);
    INTERPRETER getInterpreterByMemberKey(MEMBER member);
    List<RECOMMEND> getListInterpreterRecommend(SEARCH search);
    Integer getListInterpreterRecommendCnt(SEARCH search);
    Integer insertInterpreterRecommend(RECOMMEND recommend);
    Integer updateInterpreterRecommend(RECOMMEND recommend);
    Integer deleteInterpreterRecommend(RECOMMEND recommend);
    Integer getInterpreterBookingEndCnt(INTERPRETER interpreter);
    Float getInterpreterBookingEndGrade(INTERPRETER interpreter);
    Integer getInterpreterAverageChatTime(INTERPRETER interpreter);
    Integer getInterpreterLikeCnt(INTERPRETER interpreter);
    void updateInterpreterStopState(INTERPRETER interpreter);
}
