package com.jamie.home.api.service;

import com.jamie.home.api.model.CHAT;
import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.ROOM;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatService extends BasicService{
    public List<CHAT> list(SEARCH search) {
        return chatDao.getListChat(search);
    }

    public Integer listCnt(SEARCH search) {
        return chatDao.getListChatCnt(search);
    }

    public CHAT get(CHAT chat){
        return chatDao.getChat(chat);
    }

    public Integer save(CHAT chat) {
        if(chat.getFiles_new() != null){
            chat.setContent(
                    FileUtils.saveFiles(
                            chat.getFiles_new(),
                            uploadDir
                    )
            );
        }

        ROOM room = chatDao.getChatRoom(new ROOM(chat.getRoom()));
        INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(room.getInterpreter()));
        if(room.getMember() != chat.getMember()){
            // 알림 TYPE chat_new : 새로운 메세지
            memberDao.insertMemberInfo(new INFO(room.getMember(), room.getRoom(), "chat_new", "새로운 메시지가 도착했습니다.",""));
        }
        if(interpreter.getMember() != chat.getMember()){
            // 알림 TYPE chat_new : 새로운 메세지
            memberDao.insertMemberInfo(new INFO(interpreter.getMember(), room.getRoom(), "chat_new", "새로운 메시지가 도착했습니다.",""));
        }

        return chatDao.insertChat(chat);
    }

    public Integer modify(CHAT chat) {
        return chatDao.updateChat(chat);
    }

    public Integer remove(CHAT chat) {
        return chatDao.deleteChat(chat);
    }

    public List<ROOM> listRoom(SEARCH search) {
        List<ROOM> result = chatDao.getListChatRoom(search);
        for(int i=0; i<result.size(); i++){
            INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(result.get(i).getInterpreter()));
            result.get(i).getOther_info().put("interpreter_member_info",memberDao.getMember(new MEMBER(interpreter.getMember())));
            result.get(i).getOther_info().put("member_info",memberDao.getMember(new MEMBER(result.get(i).getMember())));
            CHAT param = new CHAT();
            param.setRoom(result.get(i).getRoom());
            result.get(i).getOther_info().put("chat",chatDao.getChatLast(param));
        }
        return result;
    }

    public ROOM getRoom(ROOM room){
        return chatDao.getChatRoom(room);
    }

    public Integer saveRoom(ROOM room) {
        return chatDao.insertChatRoom(room);
    }
}
