package com.jamie.home.api.service;

import com.jamie.home.api.model.BANNER;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BannerService extends BasicService{
    public List<BANNER> list(SEARCH search) {
        return bannerDao.getListBanner(search);
    }

    public Integer listCnt(SEARCH search) {
        return bannerDao.getListBannerCnt(search);
    }

    public BANNER get(BANNER banner){
        return bannerDao.getBanner(banner);
    }

    public Integer save(BANNER banner) {
        banner.setFiles(
                FileUtils.saveFiles(
                        banner.getFiles_new(),
                        uploadDir
                )
        );
        return bannerDao.insertBanner(banner);
    }

    public Integer modify(BANNER banner) {
        BANNER ori_Banner = bannerDao.getBanner(banner);
        try {
            banner.setFiles(
                    FileUtils.modiFiles(
                            ori_Banner.getFiles(),
                            banner.getFiles_del(),
                            banner.getFiles_new(),
                            uploadDir
                    )
            );
        } catch (Exception e) {
            banner.setFiles(null);
        }
        return bannerDao.updateBanner(banner);
    }

    public Integer remove(BANNER banner) {
        return bannerDao.deleteBanner(banner);
    }
}
