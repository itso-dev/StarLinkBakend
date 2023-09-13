package com.jamie.home.api.controller;

import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.ResponseOverlays;
import com.jamie.home.api.model.common.TOKEN;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.jwt.JwtFilter;
import com.jamie.home.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member/*")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public ResponseOverlays login(
            @Value("${jwt.token-validity-in-seconds}") Double nomalSec,
            @Value("${jwt.token-long-validity-in-seconds}") Double longSec,
            @Validated @RequestBody MEMBER member
    ) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = null;
            TOKEN token = null;
            Double expirySec = 0.0;
            if(member.getAuto_login() != null && member.getAuto_login().booleanValue()){
                jwt = tokenProvider.createLongToken(authentication);
                expirySec = longSec;
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            } else {
                jwt = tokenProvider.createToken(authentication);
                expirySec = nomalSec;
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            }

            MEMBER result = memberService.getMemberByCol(member);
            if(jwt != null){
                token = new TOKEN(result, jwt, (expirySec/3600.0));
                return new ResponseOverlays(HttpServletResponse.SC_OK, "LOGIN_MEMBER_SUCCESS", token);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute MEMBER member) {
        try {
            int result = memberService.save(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            MEMBER result = memberService.get(new MEMBER(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @ModelAttribute MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modify(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key) {
        try {
            int result = memberService.remove(new MEMBER(key));
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "REMOVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "REMOVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "REMOVE_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/find", method= RequestMethod.POST)
    public ResponseOverlays find(@Validated @RequestBody MEMBER member) {
        try {
            MEMBER result = memberService.getMemberByCol(member);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }
}