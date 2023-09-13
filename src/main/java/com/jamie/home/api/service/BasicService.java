package com.jamie.home.api.service;

import com.jamie.home.api.dao.FieldDao;
import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.SEARCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
    @Autowired
    PasswordEncoder encoder;
    @Value("${file.upload.dir}")
    String uploadDir;
    @Autowired
    MemberDao memberDao;
    @Autowired
    FieldDao fieldDao;

    public List<FIELD> listField(SEARCH search) {
        return fieldDao.getListField(search);
    }
}
