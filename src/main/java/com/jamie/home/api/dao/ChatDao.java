package com.jamie.home.api.dao;

import com.jamie.home.api.model.CHAT;
import com.jamie.home.api.model.ROOM;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatDao {
    List<CHAT> getListChat(SEARCH search);
    Integer getListChatCnt(SEARCH search);
    CHAT getChat(CHAT chat);
    Integer insertChat(CHAT chat);
    Integer updateChat(CHAT chat);
    Integer deleteChat(CHAT chat);
    List<ROOM> getListChatRoom(SEARCH search);
    ROOM getChatRoom(ROOM room);
    CHAT getChatLast(CHAT param);
    Integer insertChatRoom(ROOM room);
}
