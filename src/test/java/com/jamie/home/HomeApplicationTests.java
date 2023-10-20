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
/*
        // 다중선택 필드 테스트
        // 1. 새로들어온 값
        List<Integer> keyList = new ArrayList<>();
        keyList.add(3);keyList.add(4);keyList.add(1);

        // 2. 원래값 조회
        List<Integer> ori_keyList = new ArrayList<>();
        ori_keyList.add(1);ori_keyList.add(2);ori_keyList.add(3);

        // 3. 입력할 값들
        List<Integer> insertKeyList = keyList.stream().filter(i -> !ori_keyList.contains(i)).collect(Collectors.toList());

        // 4. 제거할 값들
        List<Integer> deleteKeyList = ori_keyList.stream().filter(i -> !keyList.contains(i)).collect(Collectors.toList());

        System.out.println(insertKeyList);
        System.out.println(deleteKeyList);
        boolean b = encoder.matches("12341", "$2a$10$Zy7a5VRHRrMs77lrJ7hf3ea/OUEWTxuJHnNgUJkNzc7LLDn/xWAu2");
        System.out.println(b);
*/
    }
}
