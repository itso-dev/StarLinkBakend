package com.jamie.home.api.dao;

import com.jamie.home.api.model.FAQ;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FaqDao {
    List<FAQ> getListFaq(SEARCH search);
    Integer getListFaqCnt(SEARCH search);
    FAQ getFaq(FAQ faq);
    Integer insertFaq(FAQ faq);
    Integer updateFaq(FAQ faq);
    Integer deleteFaq(FAQ faq);
}
