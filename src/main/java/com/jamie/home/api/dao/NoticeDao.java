package com.jamie.home.api.dao;

import com.jamie.home.api.model.NOTICE;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoticeDao {
    List<NOTICE> getListNotice(SEARCH search);
    Integer getListNoticeCnt(SEARCH search);
    NOTICE getNotice(NOTICE notice);
    Integer insertNotice(NOTICE notice);
    Integer updateNotice(NOTICE notice);
    Integer deleteNotice(NOTICE notice);
}
