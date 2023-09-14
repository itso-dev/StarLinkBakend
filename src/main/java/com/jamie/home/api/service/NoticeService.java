package com.jamie.home.api.service;

import com.jamie.home.api.model.NOTICE;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticeService extends BasicService{
    public List<NOTICE> list(SEARCH search) {
        return noticeDao.getListNotice(search);
    }

    public Integer listCnt(SEARCH search) {
        return noticeDao.getListNoticeCnt(search);
    }

    public NOTICE get(NOTICE notice){
        NOTICE result = noticeDao.getNotice(notice);
        return result;
    }

    public Integer save(NOTICE notice) {
        notice.setFiles(
                FileUtils.saveFiles(
                        notice.getFiles_new(),
                        uploadDir
                )
        );
        return noticeDao.insertNotice(notice);
    }

    public Integer modify(NOTICE notice) {
        NOTICE ori_notice = noticeDao.getNotice(notice);
        notice.setFiles(
                FileUtils.modiFiles(
                        ori_notice.getFiles(),
                        notice.getFiles_del(),
                        notice.getFiles_new(),
                        uploadDir
                )
        );
        return noticeDao.updateNotice(notice);
    }

    public Integer remove(NOTICE notice) {
        return noticeDao.deleteNotice(notice);
    }
}
