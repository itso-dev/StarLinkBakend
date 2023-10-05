package com.jamie.home.api.controller;

import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.common.ResponseOverlays;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.api.service.InterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/interpreter/*")
public class InterController {

    private static final Logger logger = LoggerFactory.getLogger(InterController.class);

    @Autowired
    private InterService interService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null){
                search.calStart();
            }
            List<INTERPRETER> list = interService.list(search);
            if(list != null){
                Integer cnt = interService.listCnt(search);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_INTERPRETER_SUCCESS", list, cnt);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INTERPRETER_NULL", null,0);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INTERPRETER_FAIL", false,0);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            INTERPRETER result = interService.get(new INTERPRETER(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_INTERPRETER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INTERPRETER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INTERPRETER_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute INTERPRETER interpreter) {
        try {
            int result = interService.save(interpreter);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_INTERPRETER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @ModelAttribute INTERPRETER interpreter) {
        try {
            interpreter.setInterpreter(key);
            int result = interService.modify(interpreter);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_INTERPRETER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key) {
        try {
            int result = interService.remove(new INTERPRETER(key));
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_INTERPRETER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/like", method= RequestMethod.POST)
    public ResponseOverlays like(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            search.setInterpreter(key);
            int result = interService.like(search);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_INTERPRETER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_INTERPRETER_FAIL", false);
        }
    }
}