package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasCouponDetailDao;
import com.hongkun.finance.vas.dao.VasCouponProductDao;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;


/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.service.impl.VasCouponProductServiceImpl.java
 * @Class Name    : VasCouponProductServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasCouponProductServiceImpl implements VasCouponProductService {
    private static final Logger logger = LoggerFactory.getLogger(VasCouponProductServiceImpl.class);
    /**
     * VasCouponProductDAO
     */
    @Autowired
    private VasCouponProductDao vasCouponProductDao;
    @Autowired
    private VasCouponDetailDao vasCouponDetailDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int insertVasCouponProduct(VasCouponProduct vasCouponProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertVasCouponProduct, 执行保存卡券产品操作, 入参: 待保存的卡券产品信息: {}",vasCouponProduct);
        }
        try {
            return  this.vasCouponProductDao.save(vasCouponProduct);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("执行保存卡券产品操作异常, 待保存的卡券产品信息: {}\n异常信息: ", vasCouponProduct, e);
            }
            throw new GeneralException("执行保存卡券产品操作异常,请重试!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void insertVasCouponProductBatch(List<VasCouponProduct> list) {
        this.vasCouponProductDao.insertBatch(VasCouponProduct.class, list);
    }



    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateVasCouponProduct(VasCouponProduct vasCouponProduct) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updateVasCouponProduct, 执行更新卡券产品操作, 入参: 待更新的卡券产品信息: {}",vasCouponProduct);
        }
        try {
            return this.vasCouponProductDao.update(vasCouponProduct);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("执行更新卡券产品操作异常, 待更新的卡券产品信息: {}\n异常信息: ", vasCouponProduct, e);
            }
            throw new GeneralException("执行更新卡券产品操作异常,请重试!");
        }
    }



    @Override
    public VasCouponProduct findVasCouponProductById(int id) {
        return this.vasCouponProductDao.findByPK(Long.valueOf(id), VasCouponProduct.class);
    }

    @Override
    public List<VasCouponProduct> findVasCouponProductList(VasCouponProduct vasCouponProduct) {
        return this.vasCouponProductDao.findByCondition(vasCouponProduct);
    }


    @Override
    public Pager findVasCouponProductList(VasCouponProduct vasCouponProduct, Pager pager) {
        return this.vasCouponProductDao.findByCondition(vasCouponProduct, pager);
    }

    @Override
    public int findVasCouponProductCount(VasCouponProduct vasCouponProduct) {
        return this.vasCouponProductDao.getTotalCount(vasCouponProduct);
    }


    @Override
    public Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO, List<Integer> limitsUserIds, Pager pager) {
        return vasCouponProductDao.couponDetailSearch(vasCouponDetailVO,limitsUserIds,pager);
    }

    @Override
    public ResponseEntity getCouponProduct(VasCouponProduct vasCouponProduct) {
        vasCouponProduct.setSortColumns("create_time desc");
        vasCouponProduct.setState(VasConstants.VAS_STATE_Y);
        vasCouponProduct.setEndTimeEndForGive(new Date());
        List<VasCouponProduct> list = this.findVasCouponProductList(vasCouponProduct);
        List<Map<String,Object>> resultList = new ArrayList<>();
        if (list.size() > 0){
            list.stream().forEach((product) -> {
                Map<String,Object> result = new HashMap<>();
                result.put("value",product.getId());
                result.put("name",product.getName());
                result.put("worth",product.getWorth());
                resultList.add(result);
            });
            return new ResponseEntity(SUCCESS,resultList);
        }
        return new ResponseEntity(ERROR,"获取数据失败");
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateCouponProduct(VasCouponProduct vasCouponProduct) {
        try{
            if (vasCouponProduct.getEndTime() != null){
                vasCouponProduct.setEndTime(DateUtils.getLastTimeOfDay(vasCouponProduct.getEndTime()));
            }
            if (vasCouponProduct == null || vasCouponProduct.getId() == null ||
                    this.findVasCouponProductById(vasCouponProduct.getId()) == null){
                return new ResponseEntity(ERROR,"请选择正确的记录进行修改");
            }
            vasCouponProduct.setModifyTime(new Date());
            if (this.updateVasCouponProduct(vasCouponProduct) > 0){
                return new ResponseEntity(SUCCESS,"修改卡券产品记录成功");
            }
        }catch (Exception e){
            logger.error("updateCouponProduct, 更新卡券产品异常, 卡券产品信息: {}, 异常信息: ", vasCouponProduct.toString(), e);
            throw new GeneralException("更新卡券产品异常！");
        }
        return new ResponseEntity(ERROR,"修改卡券产品记录失败");
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity disableCouponProduct(int id) {
        try{
            if (this.findVasCouponProductById(id) == null){
                return new ResponseEntity(ERROR,"请选择正确的记录进行操作！");
            }
            //判断该卡券产品是否生成过卡券
            VasCouponDetail vasCouponDetail = new VasCouponDetail();
            vasCouponDetail.setCouponProductId(id);
            if (vasCouponDetailDao.getTotalCount(vasCouponDetail) > 0){
                return new ResponseEntity(ERROR,"该卡券产品已生成卡券，不允许禁用！");
            }
            //没有生成过卡券则进行禁用操作
            VasCouponProduct vasCouponProduct = new VasCouponProduct();
            vasCouponProduct.setId(id);
            vasCouponProduct.setState(VasConstants.VAS_STATE_N);
            vasCouponProduct.setModifyTime(new Date());
            if (this.updateVasCouponProduct(vasCouponProduct) > 0){
                return new ResponseEntity(SUCCESS,"禁用卡券产品记录成功");
            }
        }catch (Exception e){
            logger.error("disableCouponProduct, 禁用卡券产品异常, 卡券产品id: {}, 异常信息: ", id, e);
            throw new GeneralException("禁用卡券产品异常！");
        }
        return new ResponseEntity(ERROR,"禁用卡券产品记录失败");
    }

    @Override
    public Pager staCouponList(VasCouponProduct vasCouponProduct, Pager pager) {
        return vasCouponProductDao.staCouponList(vasCouponProduct,pager);
    }
}
