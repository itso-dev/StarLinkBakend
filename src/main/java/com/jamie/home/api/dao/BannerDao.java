package com.jamie.home.api.dao;

import com.jamie.home.api.model.BANNER;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BannerDao {
    List<BANNER> getListBanner(SEARCH search);
    Integer getListBannerCnt(SEARCH search);
    BANNER getBanner(BANNER banner);
    Integer insertBanner(BANNER banner);
    Integer updateBanner(BANNER banner);
    Integer deleteBanner(BANNER banner);
}
