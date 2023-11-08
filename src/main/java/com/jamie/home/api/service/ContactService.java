package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactService extends BasicService{
    public List<CONTACT> list(SEARCH search) {
        List<CONTACT> result = contactDao.getListContact(search);
        for(int i=0; i<result.size(); i++){
            setDetailInfo(result.get(i));
        }
        return result;
    }

    private void setDetailInfo(CONTACT contact){
        contact.getOther_info().put("member_info",memberDao.getMember(new MEMBER(contact.getMember())));
        contact.getOther_info().put("interpreter_info",interDao.getInterpreterByMemberKey(new MEMBER(contact.getMember())));
    }

    public Integer listCnt(SEARCH search) {
        return contactDao.getListContactCnt(search);
    }

    public CONTACT get(CONTACT contact){
        CONTACT result = contactDao.getContact(contact);
        setDetailInfo(result);
        return result;
    }

    public Integer save(CONTACT contact) {
        contact.setFiles(
                FileUtils.saveFiles(
                        contact.getFiles_new(),
                        uploadDir
                )
        );
        return contactDao.insertContact(contact);
    }

    public Integer modify(CONTACT contact) {
        CONTACT ori_contact = contactDao.getContact(contact);
        try {
            contact.setFiles(
                    FileUtils.modiFiles(
                            ori_contact.getFiles(),
                            contact.getFiles_del(),
                            contact.getFiles_new(),
                            uploadDir
                    )
            );
        } catch (Exception e) {
            contact.setFiles(null);
        }
        if(ori_contact.getAnswer() == null && contact.getAnswer() != null){
            // 알림 TYPE contact_answer : 관리자 문의 답변
            memberDao.insertMemberInfo(new INFO(ori_contact.getMember(), ori_contact.getContact(), "contact_answer", "새로운 문의 답변이 도착했습니다.","", null));
        }
        return contactDao.updateContact(contact);
    }

    public Integer remove(CONTACT contact) {
        return contactDao.deleteContact(contact);
    }
}
