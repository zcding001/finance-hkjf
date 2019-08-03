package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.dao.PointProductImgDao;
import com.hongkun.finance.point.dao.PointProductOrderDao;
import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.point.model.vo.PointOrderVO;
import com.hongkun.finance.point.service.PointProductOrderService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointProductOrderServiceImpl.java
 * @Class Name    : PointProductOrderServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class PointProductOrderServiceImpl implements PointProductOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PointProductOrderServiceImpl.class);

    /**
     * PointProductOrderDAO
     */
    @Autowired
    private PointProductOrderDao pointProductOrderDao;
    @Autowired
    private PointProductImgDao productImgDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public Integer insertPointProductOrder(PointProductOrder pointProductOrder) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertPointProductOrder, 生成积分商品订单, 入参: 订单信息: {}", pointProductOrder);
        }
        try {
           return  pointProductOrderDao.save(pointProductOrder);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("生成积分商品订单异常, 积分商品订单为: {}\n异常信息: ", pointProductOrder, e);
            }
            throw new GeneralException("生成积分商品订单异常,请重试");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public Integer updatePointProductOrder(PointProductOrder pointProductOrder) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updatePointProductOrder, 更新积分商品订单, 入参: 订单信息: {}", pointProductOrder);
        }
        try {
            return pointProductOrderDao.update(pointProductOrder);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新积分商品订单异常, 需要更新的积分商品订单为: {}\n异常信息: ", pointProductOrder, e);
            }
            throw new GeneralException("更新积分商品订单异常,请重试");
        }
    }


    @Override
    public PointProductOrder findPointProductOrderById(int id) {
        return this.pointProductOrderDao.findByPK(Long.valueOf(id), PointProductOrder.class);
    }


    @Override
    public int findPointProductOrderCount(PointProductOrder pointProductOrder) {
        return this.pointProductOrderDao.getTotalCount(pointProductOrder);
    }

    @Override
    public Pager listPointOrder(PointOrderQuery pointProductOrder, Pager pager) {
        return pointProductOrderDao.listPointOrder(pointProductOrder, pager);
    }

    @Override
    public ResponseEntity listUserPointProductOrder(PointOrderQuery pointOrderQuery, Pager pager) {
        pointOrderQuery.setSortColumns("modify_time desc");
        Pager result = pointProductOrderDao.listPointOrder(pointOrderQuery, pager);

        if (!BaseUtil.resultPageHasNoData(result)) {
            List<PointOrderVO> orderVos = result.getData().stream().map(order -> {
                PointProductOrder productOrder = (PointProductOrder) order;
                //赋值订单属性
                PointOrderVO pointOrderVO = BeanPropertiesUtil.mergeAndReturn(new PointOrderVO(), productOrder);
                //查询商品小图
                List<PointProductImg> headImg =
                        productImgDao.findProductHeadImgsByProductIds(Arrays.asList(pointOrderVO.getProductId()));
                PointProductImg img = headImg.get(0);
                if (img != null) {
                    pointOrderVO.setSmallImgUrl(img.getSmallImgUrl());
                }
                return pointOrderVO;
            }).collect(Collectors.toList());
            //处理结果视图
            result.setData(orderVos);

        }
        return new ResponseEntity(SUCCESS, result);
    }
}
