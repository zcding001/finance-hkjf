package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.BidInfoDao;
import com.hongkun.finance.invest.dao.BidProductDao;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.service.BidProductService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.service.impl.BidProductServiceImpl.java
 * @Class Name    : BidProductServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidProductServiceImpl implements BidProductService {

    private static final Logger logger = LoggerFactory.getLogger(BidProductServiceImpl.class);

    /**
     * BidProductDAO
     */
    @Autowired
    private BidProductDao bidProductDao;

    @Autowired
    private BidInfoDao bidInfoDao;




    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateBidProduct(BidProduct bidProduct) {
        return this.bidProductDao.update(bidProduct);
    }


    @Override
    public BidProduct findBidProductById(int id) {
        return this.bidProductDao.findByPK(Long.valueOf(id), BidProduct.class);
    }

    @Override
    public List<BidProduct> findBidProductList(BidProduct bidProduct) {
        return this.bidProductDao.findByCondition(bidProduct);
    }


    @Override
    public int findBidProductCount(BidProduct bidProduct) {
        return this.bidProductDao.getTotalCount(bidProduct);
    }


    @Override
    public Pager findBidProductWithCondition(BidProduct bidProduct, Pager pager) {
        return this.bidProductDao.findBidProductWithCondition(bidProduct, pager);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity saveBidProduct(BidProduct bidProduct) {
        //设置上架上架状态为下架
        bidProduct.setState(InvestConstants.PRODUCT_STATE_OFF);
        //保存标的产品
        if (this.bidProductDao.save(bidProduct)>0) {
            if (logger.isInfoEnabled()) {
                logger.info("添加标的产品成功：{}", bidProduct);
            }
            return new ResponseEntity(SUCCESS, "添加标的产品成功");
        }
        if (logger.isInfoEnabled()) {
            logger.info("添加标的产品失败：详细信息：{}", bidProduct);
        }
        return new ResponseEntity(ERROR, "添加标的产品失败");
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updateBidProductInfo(BidProduct bidProduct) {
        //判断是否可以修改：如果产品是上架或者产品下面已经建立标的状态，不可以修改
        BidProduct unModifyProduct = bidProductDao.findByPK(Long.valueOf(bidProduct.getId()), BidProduct.class);
        if (unModifyProduct != null) {
            BidInfo countBidInfo = new BidInfo();
            countBidInfo.setBidProductId(bidProduct.getId());
            if (unModifyProduct.getState() != InvestConstants.PRODUCT_STATE_ON) {

                if (bidInfoDao.getTotalCount(countBidInfo) > 0) {
                    return new ResponseEntity(203, "此产品已经建立标的，不允许修改");
                }
                // 尝试更新
                if (bidProductDao.update(bidProduct) > 0) {
                    if (logger.isInfoEnabled()) {
                        logger.info("修改标的产品成功：详细信息：{}", bidProduct);
                    }
                    return new ResponseEntity(SUCCESS, "修改标的产品成功");
                }
            } else {
                return new ResponseEntity(202, "产品为上架状态不允许修改");
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("修改标的产品失败：详细信息：{}", bidProduct);
        }
        return new ResponseEntity(201, "修改产品标的失败");
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity turnOnOrDownProduct(Integer productId, Integer productStateOn) {

        BidProduct product = new BidProduct();
        product.setId(productId);
        product.setState(productStateOn);

        //执行下架操作进行校验,如果该产品下还有未满标的标的，不允许下架
        if (BaseUtil.equelsIntWraperPrimit(productStateOn, InvestConstants.PRODUCT_STATE_OFF)) {
            BidInfo queryBidInfoCount = new BidInfo();
            queryBidInfoCount.setBidProductId(productId);
            queryBidInfoCount.setState(InvestConstants.BID_STATE_WAIT_INVEST);
            if (!BaseUtil.collectionIsEmpty(bidInfoDao.findByCondition(queryBidInfoCount))) {
                return new ResponseEntity(ERROR, "当产品有未满标的标的,不允许下架");
            }
        }

        //执行上架或者下架操作
        int returnState = (bidProductDao.update(product) > 0) ? SUCCESS : ERROR;

        if (logger.isInfoEnabled()) {
            logger.info("标的产品上架或者下架操作,标的产品ID：{},操作结果：{}", productId, returnState);
        }
        return new ResponseEntity(returnState);
    }

    @Override
    public List<Integer> orderedProductIds(List<Integer> orderedProductId) {
        //产品id排序，首先根据产品是否一次本息排序，然后通过产品创建时间排序
        return bidProductDao.orderedProductIds(orderedProductId);
    }

    @Override
    public List<Integer> orderedByPayemntAndCreateTime(List<Integer> orderedProductId) {
        //产品id排序，首先根据产品是否按月付息,到期还本排序，然后通过产品创建时间排序
        return bidProductDao.orderedByPayemntAndCreateTime(orderedProductId);
    }
}
