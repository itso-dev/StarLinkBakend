package com.jamie.home;

import com.jamie.home.api.dao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class HomeApplicationTests {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    MemberDao memberDao;
    @Test
    void contextLoads() throws Exception {
    }
}
