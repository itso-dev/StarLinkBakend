package com.jamie.home.api.service;

import com.jamie.home.api.dao.*;
import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
    @Autowired
    PasswordEncoder encoder;
    @Value("${file.upload.dir}")
    String uploadDir;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MemberDao memberDao;
    @Autowired
    FieldDao fieldDao;
    @Autowired
    BannerDao bannerDao;
    @Autowired
    ContactDao contactDao;
    @Autowired
    FaqDao faqDao;
    @Autowired
    NoticeDao noticeDao;

    public List<FIELD> listField(SEARCH search) {
        return fieldDao.getListField(search);
    }

    public String sendMail(MEMBER member) throws Exception {
        String uuid = UUID.randomUUID().toString();
        String uuidCode = uuid.substring(0,uuid.indexOf('-')).toUpperCase();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 수신 대상
        msgHelper.setTo(member.getEmail());
        msgHelper.setFrom("sender@gmail.com");

        msgHelper.setSubject("[STARLINKSOUTH] 인증코드 발송");
        String htmlContent = "인증코드 ["+uuidCode+"]로 인증해주시기 바랍니다.<br>";
        msgHelper.setText(htmlContent, true);
        javaMailSender.send(message);
        return uuidCode;
    }
}
