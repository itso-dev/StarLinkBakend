package com.jamie.home.api.controller;

import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.ResponseOverlays;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.api.service.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/main/*")
public class MainController {

    @Autowired
    private BasicService basicService;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value="/field/list", method= RequestMethod.POST)
    public ResponseOverlays listField(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null){
                search.calStart();
            }
            List<FIELD> list = basicService.listField(search);
            if(list != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_FILED_SUCCESS", list);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FILED_NULL", null,0);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FILED_FAIL", false,0);
        }
    }
}