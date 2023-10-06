package com.jamie.home.api.service;

import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.ROLE;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService extends BasicService{
    public List<MEMBER> list(SEARCH search) {
        return memberDao.getListMember(search);
    }

    public Integer listCnt(SEARCH search) {
        return memberDao.getListMemberCnt(search);
    }

    public MEMBER get(MEMBER member){
        MEMBER result = memberDao.getMember(member);
        return result;
    }

    public MEMBER getMemberByCol(MEMBER member){
        MEMBER result = memberDao.getMemberByCol(member);
        return result;
    }

    public Integer save(MEMBER member) {
        // 비밀번호 암호화
        member.setPassword(encoder.encode(member.getPassword()));
        member.setRole(ROLE.ROLE_USER);
        member.setProfile_file("[]");

        Integer result = memberDao.insertMember(member);
        return result;
    }

    public Integer modify(MEMBER member) {
        if(member.getPassword() != null){
            member.setPassword(encoder.encode(member.getPassword()));
        }

        MEMBER ori_member = memberDao.getMember(member);
        if(member.getProfile_file_new() != null){
            member.setProfile_file(
                    FileUtils.modiOneFiles(
                            ori_member.getProfile_file(),
                            member.getProfile_file_new(),
                            uploadDir
                    )
            );
        }
        return memberDao.updateMember(member);
    }

    public Integer remove(MEMBER member) {
        return memberDao.deleteMember(member);
    }

    public List<INFO> listMemberInfo(SEARCH search) {
        return memberDao.getListMemberInfo(search);
    }

    public Integer modifyMemberInfoCheck(INFO info) {
        return memberDao.updateMemberInfoCheck(info);
    }

    public Integer modifyMemberInfoCheckAll(INFO info) {
        return memberDao.updateMemberInfoCheckAll(info);
    }
}
