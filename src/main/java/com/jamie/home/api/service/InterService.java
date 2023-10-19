package com.jamie.home.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.controller.InterController;
import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.RECOMMEND;
import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterService extends BasicService{
    private static final Logger logger = LoggerFactory.getLogger(InterController.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<INTERPRETER> list(SEARCH search) {
        List<INTERPRETER> result = interDao.getListInterpreter(search);
        for(int i=0; i<result.size(); i++){
            setDetailInfo(result.get(i));
        }
        return result;
    }

    private void setDetailInfo(INTERPRETER interpreter){
        interpreter.getOther_info().put("member_info",memberDao.getMember(new MEMBER(interpreter.getMember())));
        interpreter.getOther_info().put("location",interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(),"location"));
        interpreter.getOther_info().put("category",interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(),"category"));
        interpreter.getOther_info().put("language",interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(),"language"));
        interpreter.getOther_info().put("booking_cnt",interDao.getInterpreterBookingEndCnt(new INTERPRETER(interpreter.getInterpreter())));
        interpreter.getOther_info().put("booking_grade",interDao.getInterpreterBookingEndGrade(new INTERPRETER(interpreter.getInterpreter())));
        interpreter.getOther_info().put("average_time",interDao.getInterpreterAverageChatTime(new INTERPRETER(interpreter.getInterpreter())));
        interpreter.getOther_info().put("like_cnt",interDao.getInterpreterLikeCnt(new INTERPRETER(interpreter.getInterpreter())));
    }

    public Integer listCnt(SEARCH search) {
        return interDao.getListInterpreterCnt(search);
    }

    public INTERPRETER get(INTERPRETER interpreter){
        INTERPRETER result = interDao.getInterpreter(interpreter);
        setDetailInfo(result);
        return result;
    }

    public Integer save(INTERPRETER interpreter) {
        interpreter.setFiles(
                FileUtils.saveFiles(
                        interpreter.getFiles_new(),
                        uploadDir
                )
        );

        Integer result = interDao.insertInterpreter(interpreter);

        if(interpreter.getLocation() != null){ // 지역
            try {
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getLocation(), Integer[].class));
                insertMultiChoiceField(interpreter.getInterpreter(), keyList, "location");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        if(interpreter.getCategory() != null){ // 통역분야
            try {
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getCategory(), Integer[].class));
                insertMultiChoiceField(interpreter.getInterpreter(), keyList, "category");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        if(interpreter.getLanguage() != null){ // 통역가능언어
            try {
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getLanguage(), Integer[].class));
                insertMultiChoiceField(interpreter.getInterpreter(), keyList, "language");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }

        return result;
    }

    private void insertMultiChoiceField(int interpreter, List<Integer> keyList, String type){
        Map<String, Object> param = new HashMap<>();
        param.put("interpreter", interpreter);
        param.put("keyList", keyList);
        param.put("type_value", type);
        interDao.insertInterpreterMultiChoice(param);
    }

    public Integer modify(INTERPRETER interpreter) {
        INTERPRETER ori_Interpreter = interDao.getInterpreter(interpreter);
        if(interpreter.getFiles_new() != null){
            interpreter.setFiles(
                    FileUtils.modiOneFiles(
                            ori_Interpreter.getFiles(),
                            interpreter.getFiles_new(),
                            uploadDir
                    )
            );
        }

        if(interpreter.getLocation() != null){ // 지역
            try {
                // 들어온 값
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getLocation(), Integer[].class));
                List<Integer> ori_keyList = interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(), "location");
                modifyMultiChoiceField(interpreter.getInterpreter(), keyList, ori_keyList, "location");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        if(interpreter.getCategory() != null){ // 통역분야
            try {
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getCategory(), Integer[].class));
                List<Integer> ori_keyList = interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(), "category");
                modifyMultiChoiceField(interpreter.getInterpreter(), keyList, ori_keyList, "category");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        if(interpreter.getLanguage() != null){ // 통역가능언어
            try {
                List<Integer> keyList = Arrays.asList(mapper.readValue(interpreter.getLanguage(), Integer[].class));
                List<Integer> ori_keyList = interDao.getListInterpreterMultiChoice(interpreter.getInterpreter(), "language");
                modifyMultiChoiceField(interpreter.getInterpreter(), keyList, ori_keyList, "language");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }

        // 상태값 변경에 따른 알림
        FIELD param = new FIELD();
        param.setType_name("inter_state");
        param.setValue("confirm");
        FIELD confirmField = fieldDao.getField(param);
        param.setValue("reject");
        FIELD rejectField = fieldDao.getField(param);

        if(interpreter.getState() == confirmField.getField()){
            // 알림 TYPE interpreter_confirm : 통역사 승인
            memberDao.insertMemberInfo(new INFO(ori_Interpreter.getMember(), ori_Interpreter.getMember(), "interpreter_confirm", "통역사 신청이 승인되었습니다.",""));
        } else if(interpreter.getState() == rejectField.getField()){
            // 알림 TYPE interpreter_reject : 통역사 거절
            memberDao.insertMemberInfo(new INFO(ori_Interpreter.getMember(), ori_Interpreter.getMember(), "interpreter_reject", "통역사 신청이 반려되었습니다.",interpreter.getReject_msg()));
        }

        return interDao.updateInterpreter(interpreter);
    }

    private void modifyMultiChoiceField(int interpreter, List<Integer> keyList, List<Integer> ori_keyList, String type){
        Map<String, Object> param = new HashMap<>();
        param.put("interpreter", interpreter);
        // 입력할 값들
        List<Integer> insertKeyList = keyList.stream().filter(i -> !ori_keyList.contains(i)).collect(Collectors.toList());
        // 제거할 값들
        List<Integer> deleteKeyList = ori_keyList.stream().filter(i -> !keyList.contains(i)).collect(Collectors.toList());

        if(insertKeyList.size() != 0){
            param.put("keyList", insertKeyList);
            param.put("type_value", type);
            interDao.insertInterpreterMultiChoice(param);
        }
        if(deleteKeyList.size() != 0){
            param.put("keyList", deleteKeyList);
            param.put("type_value", type);
            interDao.deleteInterpreterMultiChoice(param);
        }
    }

    public Integer remove(INTERPRETER interpreter) {
        return interDao.deleteInterpreter(interpreter);
    }

    public int like(SEARCH search) {
        return interDao.updateInterpreterLike(search);
    }

    public List<RECOMMEND> listRecommend(SEARCH search) {
        List<RECOMMEND> result = interDao.getListInterpreterRecommend(search);
        for(int i=0; i<result.size(); i++){
            setDetailRecommendInfo(result.get(i));
        }
        return result;
    }

    private void setDetailRecommendInfo(RECOMMEND recommend){
        INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(recommend.getInterpreter()));
        interpreter.getOther_info().put("member_info", memberDao.getMember(new MEMBER(interpreter.getMember())));
        recommend.getOther_info().put("interpreter_info",interpreter);
    }

    public Integer listRecommendCnt(SEARCH search) {
        return interDao.getListInterpreterRecommendCnt(search);
    }

    public Integer saveRecommend(RECOMMEND recommend) {
        return interDao.insertInterpreterRecommend(recommend);
    }

    public Integer modifyRecommend(RECOMMEND recommend) {
        return interDao.updateInterpreterRecommend(recommend);
    }

    public Integer removeRecommend(RECOMMEND recommend) {
        return interDao.deleteInterpreterRecommend(recommend);
    }
}
