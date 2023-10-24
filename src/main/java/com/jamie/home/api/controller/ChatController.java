package com.jamie.home.api.controller;

import com.jamie.home.api.model.CHAT;
import com.jamie.home.api.model.ROOM;
import com.jamie.home.api.model.common.ResponseOverlays;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.api.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/chat/*")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            List<CHAT> list = chatService.list(search);
            if(list != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CHAT_SUCCESS", list, list.size());
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_NULL", null,0);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_FAIL", false,0);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            CHAT result = chatService.get(new CHAT(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CHAT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute CHAT chat) {
        try {
            int result = chatService.save(chat);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CHAT_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @RequestBody CHAT chat) {
        try {
            chat.setChat(key);
            int result = chatService.modify(chat);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CHAT_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key) {
        try {
            int result = chatService.remove(new CHAT(key));
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CHAT_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_FAIL", false);
        }
    }

    @RequestMapping(value="/room/list", method= RequestMethod.POST)
    public ResponseOverlays listRoom(@Validated @RequestBody SEARCH search) {
        try {
            List<ROOM> list = chatService.listRoom(search);
            if(list != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CHAT_SUCCESS", list, list.size());
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_NULL", null,0);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_FAIL", false,0);
        }
    }

    @RequestMapping(value="/room/{key}", method= RequestMethod.GET)
    public ResponseOverlays getRoomByKey(@PathVariable("key") int key) {
        try {
            ROOM result = chatService.getRoom(new ROOM(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CHAT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_FAIL", null);
        }
    }

    @RequestMapping(value="/room/get", method= RequestMethod.POST)
    public ResponseOverlays getRoomByNokey(@Validated @RequestBody ROOM room) {
        try {
            ROOM result = chatService.getRoomByNokey(room);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CHAT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CHAT_FAIL", null);
        }
    }

    @RequestMapping(value="/room/save", method= RequestMethod.POST)
    public ResponseOverlays saveRoom(@Validated @RequestBody ROOM room) {
        try {
            int result = chatService.saveRoom(room);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CHAT_SUCCESS", room);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_FAIL", false);
        }
    }

    @RequestMapping(value="/room/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modifyRoomActive(@PathVariable("key") int key) {
        try {
            int result = chatService.modifyRoomActive(new ROOM(key));
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CHAT_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CHAT_FAIL", false);
        }
    }
}