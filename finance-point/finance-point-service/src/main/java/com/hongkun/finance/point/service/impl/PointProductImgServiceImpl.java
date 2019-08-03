package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.dao.PointProductImgDao;
import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.service.PointProductImgService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointProductImgServiceImpl.java
 * @Class Name    : PointProductImgServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class PointProductImgServiceImpl implements PointProductImgService {

    private static final Logger logger = LoggerFactory.getLogger(PointProductImgServiceImpl.class);

    /**
     * PointProductImgDAO
     */
    @Autowired
    private PointProductImgDao pointProductImgDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertPointProductImg(PointProductImg pointProductImg) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertPointProductImg, 保存积分图片, 入参: pointProductImg: {}", pointProductImg);
        }
        try {
            pointProductImgDao.save(pointProductImg);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存积分图片异常, 需要保存积分商品图片为: {}\n异常信息: ", pointProductImg, e);
            }
            throw new GeneralException("保存积分商品图片异常异常,请重试");
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updatePointProductImg(PointProductImg pointProductImg) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updatePointProductImg, 更新积分图片, 入参: pointProductImg: {}", pointProductImg);
        }
        try {
            pointProductImgDao.update(pointProductImg);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新积分图片异常, 需要更新积分商品图片为: {}\n异常信息: ", pointProductImg, e);
            }
            throw new GeneralException("更新积分商品图片异常异常,请重试");
        }
    }


    @Override
    public PointProductImg findPointProductImgById(int id) {
        return this.pointProductImgDao.findByPK(Long.valueOf(id), PointProductImg.class);
    }

    @Override
    public List<PointProductImg> findPointProductImgList(PointProductImg pointProductImg) {
        return this.pointProductImgDao.findByCondition(pointProductImg);
    }

    @Override
    public List<PointProductImg> findPointProductImgList(PointProductImg pointProductImg, int start, int limit) {
        return this.pointProductImgDao.findByCondition(pointProductImg, start, limit);
    }

    @Override
    public Pager findPointProductImgList(PointProductImg pointProductImg, Pager pager) {
        return this.pointProductImgDao.findByCondition(pointProductImg, pager);
    }

    @Override
    public int findPointProductImgCount(PointProductImg pointProductImg) {
        return this.pointProductImgDao.getTotalCount(pointProductImg);
    }

    @Override
    public Pager findPointProductImgList(PointProductImg pointProductImg, Pager pager, String sqlName) {
        return this.pointProductImgDao.findByCondition(pointProductImg, pager, sqlName);
    }

    @Override
    public Integer findPointProductImgCount(PointProductImg pointProductImg, String sqlName) {
        return this.pointProductImgDao.getTotalCount(pointProductImg, sqlName);
    }
}
