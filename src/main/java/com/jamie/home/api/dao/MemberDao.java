package com.jamie.home.api.dao;

import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberDao {
    List<MEMBER> getListMember(SEARCH search);
    Integer getListMemberCnt(SEARCH search);
    MEMBER getMember(MEMBER member);
    MEMBER getMemberByCol(MEMBER member);
    Integer insertMember(MEMBER member);
    Integer updateMember(MEMBER member);
    Integer deleteMember(MEMBER member);
}
