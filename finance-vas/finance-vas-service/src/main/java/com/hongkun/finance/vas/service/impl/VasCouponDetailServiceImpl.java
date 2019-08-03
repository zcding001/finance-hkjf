package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.dao.VasCouponDetailDao;
import com.hongkun.finance.vas.dao.VasCouponProductDao;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.utils.VasCouponDetailUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.vas.constants.VasCouponConstants.*;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Project : finance
 * @Program Name :
 * com.hongkun.finance.loan.service.impl.VasCouponDetailServiceImpl.java
 * @Class Name : VasCouponDetailServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasCouponDetailServiceImpl implements VasCouponDetailService {

    private static final Logger logger = LoggerFactory.getLogger(VasCouponDetailServiceImpl.class);

    /**
     * VasCouponDetailDAO
     */
    @Autowired
    private VasCouponDetailDao vasCouponDetailDao;
    @Autowired
    private VasCouponProductDao couponProductDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int insertVasCouponDetail(VasCouponDetail vasCouponDetail) {
        try {
            return this.vasCouponDetailDao.save(vasCouponDetail);
        }catch (Exception e){
            logger.error("insertVasCouponDetail, 插入卡券详情异常, 卡券详情信息: {}, 异常信息: ", vasCouponDetail.toString(), e);
            throw new GeneralException("插入卡券详情异常！");
        }
    }

    @Override
    @Compensable(cancelMethod = "updateCouponDetailForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateVasCouponDetail(VasCouponDetail vasCouponDetail) {
        try{
            return this.vasCouponDetailDao.update(vasCouponDetail);
        }catch (Exception e){
            logger.error("updateVasCouponDetail, 更新卡券详情异常, 卡券详情信息: {}, 异常信息: ", vasCouponDetail.toString(), e);
            throw new GeneralException("更新卡券详情异常！");
        }
    }

    /**
     *  @Description    ：更新卡券详情回滚方法
     *  @Method_Name    ：updateCouponDetailForCancel
     *  @param vasCouponDetail
     *  @return int
     *  @Creation Date  ：2018/5/31
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int updateCouponDetailForCancel(VasCouponDetail vasCouponDetail) {
        logger.info("tcc cancel updateCouponDetailForCancel. 更新的卡券数据: {}", vasCouponDetail);
        try{
            //设置为已经使用
            vasCouponDetail.setState(COUPON_DETAIL_USE_ALREADY);
            return this.vasCouponDetailDao.update(vasCouponDetail);
        }catch (Exception e){
            logger.error("tcc cancel updateVasCouponDetail error. , 更新卡券详情异常, 卡券详情信息: {}, 异常信息: {}", vasCouponDetail.toString(), e);
            throw new GeneralException("更新卡券详情异常！");
        }

    }


    @Override
    @Compensable(cancelMethod = "updateVasCouponDetailForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateVasCouponDetailBatch(List<VasCouponDetail> list) {
        logger.info("{}. 批量更新卡券操作, 卡券信息: {}", BaseUtil.getTccTryLogPrefix(), JsonUtils.toJson(list));
        try {
        	if (CommonUtils.isNotEmpty(list)) {
                this.vasCouponDetailDao.updateBatch(VasCouponDetail.class, list, list.size());
            }
		} catch (Exception e) {
            logger.info("{}. 批量更新卡券操作, 卡券信息: {}", BaseUtil.getTccTryLogPrefix(), JsonUtils.toJson(list), e);
			throw new GeneralException("批量卡券更新操作失败");
		}
    }

    @Override
    public VasCouponDetail findVasCouponDetailById(int id) {
        return this.vasCouponDetailDao.findByPK(Long.valueOf(id), VasCouponDetail.class);
    }

    @Override
    public List<VasCouponDetail> findVasCouponDetailList(VasCouponDetail vasCouponDetail) {
        return this.vasCouponDetailDao.findByCondition(vasCouponDetail);
    }

    @Override
    public List<VasCouponDetail> findVasCouponDetailList(VasCouponDetail vasCouponDetail, int start, int limit) {
        return this.vasCouponDetailDao.findByCondition(vasCouponDetail, start, limit);
    }

    @Override
    public Pager findVasCouponDetailList(VasCouponDetail vasCouponDetail, Pager pager) {
        return this.vasCouponDetailDao.findByCondition(vasCouponDetail, pager);
    }

    @Override
    public int findVasCouponDetailCount(VasCouponDetail vasCouponDetail) {
        return this.vasCouponDetailDao.getTotalCount(vasCouponDetail);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public boolean distributeCouponToUser(List<Integer> userIds, List<Integer> couponProductIds, String reason) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: distributeCouponToUser, 执行赠送用户卡券操作, 入参: 接受用户ids: {}, 赠送卡券的ids: {}, 赠送原因: {}", userIds, couponProductIds,reason);
        }
        try {
            // 循环给用户发送卡券
            List<VasCouponDetail> unSaveDetails = new ArrayList<>();
            userIds.stream().forEach((id) -> {
                // 派发每张券
                couponProductIds.stream().forEach((productId) -> {
                    //给用户发送好友券，好友券默认状态为4可转赠
                    VasCouponProduct couponProduct = couponProductDao.findByPK(Long.valueOf(productId),VasCouponProduct.class);
                    int state = COUPON_DETAIL_SEND_ALREADY;
                    if (Objects.equals(couponProduct.getType(),VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
                        state = COUPON_DETAIL_DONATION_ONLY;
                    }
                    VasCouponDetail unSaveDetail = VasCouponDetailUtil.assembleCouponDetail(couponProduct, state,
                            COUPON_DETAIL_SOURCE_DISTRIBUTE, reason, id);
                    // 加入待插入集合
                    unSaveDetails.add(unSaveDetail);
                });
            });
            return vasCouponDetailDao.insertBatch(VasCouponDetail.class, unSaveDetails) == unSaveDetails.size();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("执行赠送用户卡券操作异常, 入参: 接受用户ids: {}, 赠送卡券的ids: {}, 赠送原因: {}\n异常信息: ", userIds, couponProductIds,reason, e);
            }
            throw new GeneralException("执行赠送用户卡券操作异常,请重试!");
        }
    }

    @Override
    public List<VasCouponDetail> generateCouponDetail(VasCouponProduct product, Integer num) {
        // 初始化卡券详情列表
        List<VasCouponDetail> list = new ArrayList<>(num);
        try{
            for (int i = 0; i < num; i++) {
                VasCouponDetail detail = VasCouponDetailUtil.assembleCouponDetail(product, VasCouponConstants.COUPON_DETAIL_CREATE, VasCouponConstants
                        .COUPON_DETAIL_SOURCE_OFFLINE, null, null);
                list.add(detail);
            }
            vasCouponDetailDao.insertBatch(VasCouponDetail.class, list);
        }catch (Exception e){
            logger.error("generateCouponDetail, 生成线下卡券数据异常, 卡券产品信息: {}, 生成数量: {}, 异常信息: ",
                    product.toString(), num, e);
            throw new GeneralException("生成线下卡券数据异常！");
        }
        return list;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ResponseEntity validateCoupon(Integer redPacketId, Integer raiseInterestId, Integer regUserId, Integer type, Integer allowCoupon, BigDecimal money) {
        ResponseEntity success = new ResponseEntity(SUCCESS);
        if (redPacketId > 0) {
            // 验证投资红包
            ResponseEntity redPacketCoupon = validateCouponSpec(redPacketId, regUserId, type, money);
            if (BaseUtil.error(redPacketCoupon)) {
                return redPacketCoupon;
            } else {
                success.getParams().put("investRedPacket", redPacketCoupon.getResMsg());
            }
        }
        if (raiseInterestId > 0) {
            // 验证加息券
            ResponseEntity raiseInterestCoupon = validateCouponSpec(raiseInterestId, regUserId, type, money);
            if (BaseUtil.error(raiseInterestCoupon)) {
                return raiseInterestCoupon;
            } else {
                success.getParams().put("investRaiseInterest", raiseInterestCoupon.getResMsg());
            }
        }
        return success;
    }

    /**
     *  @Description    : 验证指定卡券
     *  @Method_Name    : validateCouponSpec
     * @param couponId   : 卡券iD
     * @param regUserId   : 用户ID
     * @param type   : 类型
     * @param money   : 金额
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    private ResponseEntity validateCouponSpec(Integer couponId, Integer regUserId, Integer type, BigDecimal money) {
        ResponseEntity error = new ResponseEntity(ERROR);
        ResponseEntity success = new ResponseEntity(SUCCESS);
        VasCouponDetail unValidteCoupon = this.vasCouponDetailDao.findByPK(Long.valueOf(couponId),
                VasCouponDetail.class);
        // 验证卡券是否存在
        if (unValidteCoupon == null) {
            error.setResMsg("该卡券不存在，请核实后重试！");
            return error;
        }
        // 查询卡券对应产品
        VasCouponProduct product = this.couponProductDao.findByPK(Long.valueOf(unValidteCoupon.getCouponProductId()),
                VasCouponProduct.class);
        // 核实是否过期
        if (unValidteCoupon.getEndTime().before(new Date())) {
            error.setResMsg(product.getName() + "已经过期");
            return error;
        }

        // 核实是否属于本用户的id
        if (!unValidteCoupon.getAcceptorUserId().equals(regUserId)) {
            error.setResMsg(product.getName() + "不属于当前用户");
            return error;
        }

        // 核实是否使用与指定标的
        if (!product.getBidProductTypeRange().contains(String.valueOf(type))) {
            error.setResMsg(product.getName() + "不适用于当前标的");
            return error;
        }
        // 判断最低投资金额
        if (!(money.compareTo(product.getAmountMin()) >= 0 && money.compareTo(product.getAmountMax()) <= 0)) {
            error.setResMsg(product.getName() + "当前投资金额不在卡券使用范围内");
            return error;
        }
        // 判断最低投资金额
        if (unValidteCoupon.getState().equals(VasCouponConstants.COUPON_DETAIL_USE_ALREADY)) {
            error.setResMsg(product.getName() + "卡券已使用");
            return error;
        }
        success.setResMsg(unValidteCoupon);
        return success;
    }

    /**
     * @param list
     * @return : void
     * @Description : TCC回滚方法
     * @Method_Name : updateVasCouponDetailForRollback
     * @Creation Date : 2017年8月7日 下午3:05:20
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateVasCouponDetailForCancel(List<VasCouponDetail> list) {
    	logger.info("tcc cancel updateVasCouponDetailForCancel. 更新的卡券数据: {}", JsonUtils.toJson(list));
    	try {
    		if (CommonUtils.isNotEmpty(list)) {
	            for (VasCouponDetail obj : list) {
	                obj.setState(1);
	            }
	            this.vasCouponDetailDao.updateBatch(VasCouponDetail.class, list, list.size());
	        }
		} catch (Exception e) {
			logger.error("tcc cancel updateVasCouponDetailForCancel error. 更新的卡券数据: {}\n", JsonUtils.toJson(list), e);
			throw new GeneralException("卡券还原失败");
		}
    }

    @Override
    public List<VasCouponDetailVO> getUserCouponDetailList(Map<String, Object> param) {
        return vasCouponDetailDao.getUserCouponDetailList(param);
    }

    @Override
    public Map<String, Object> getUserInvestUsableCoupon(int productType, int regUserId) {
        //返回的结果
        Map<String, Object> result = new HashMap<>(2);
        //加息券集合
        List<VasCouponDetailVO> interestCouponList = new ArrayList<>();
        //投资红包集合
        List<VasCouponDetailVO> redPacketsCouponList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>(2);
        param.put("acceptorUserId", regUserId);
        param.put("state", COUPON_DETAIL_SEND_ALREADY);
        List<VasCouponDetailVO> couponDetailVOList = vasCouponDetailDao.getUserCouponDetailList(param);
        //遍历集合将加息券和投资红包处理后返回
        couponDetailVOList.forEach((couponDetailVO) -> {
            //判断卡券的适用产品类型是否包括传递的产品类型，并且今天在卡券使用时间内
            if (couponDetailVO.getBidProductTypeRange().contains(String.valueOf(productType)) && new Date().after(couponDetailVO.getBeginTime())) {
                //将符合条件的好友券和加息券添加到加息券集合中
                if (couponDetailVO.getType() == VasCouponConstants.COUPON_PRODUCT_TYPE_RATE_COUPON || couponDetailVO
                        .getType() == VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS) {
                    interestCouponList.add(couponDetailVO);
                } else if (couponDetailVO.getType() == VasCouponConstants.COUPON_PRODUCT_TYPE_INVEST_REDPACKET) {
                    //将符合条件的投资红包添加到投资红包集合中
                    redPacketsCouponList.add(couponDetailVO);
                }
            }
        });
        result.put("interestCouponList", interestCouponList);
        result.put("redPacketsCouponList", redPacketsCouponList);
        return result;
    }

    @Override
    public ResponseEntity activeCouponDetail(int regUserId, String avtKey) {
        logger.info("activeCouponDetail, 激活卡券, 用户标识: {}, 激活码: {}", regUserId, avtKey);
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + VasCouponDetailServiceImpl.class.getSimpleName() +
                "_activeCouponDetail_" + avtKey;
        try {
            //添加redis锁,如果获取锁超时则返回提示信息
            if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
                logger.error("activeCouponDetail, 激活卡券超时, 用户标识: {}, 激活码: {}", regUserId, avtKey);
                return new ResponseEntity(ERROR, "激活卡券超时，请重新操作！");
            }
            //根据激活码获取卡券信息，判断获取的卡券是否符合激活条件
            VasCouponDetail couponDetail = vasCouponDetailDao.findVasCouponDetailByKey(avtKey);
            //1:判断卡券信息是否存在
            if (couponDetail == null) {
                return new ResponseEntity(ERROR, "该卡券信息不存在，请您确认激活码是否正确！");
            }
            //2:判断卡券信息是否在截止使用时间前激活
            if (DateUtils.getCurrentDate().after(couponDetail.getEndTime())) {
                return new ResponseEntity(ERROR, "该卡券已过截止使用时间！");
            }
            //3:判断卡券是否已被激活
            if (couponDetail.getState().equals(VasCouponConstants.COUPON_DETAIL_USE_ALREADY)){
                return new ResponseEntity(ERROR, "该卡券激活码已被使用！");
            }
            if (couponDetail.getState() != VasCouponConstants.COUPON_DETAIL_CREATE) {
                return new ResponseEntity(ERROR, "该卡券已被兑换！");
            }
            //4:判断该卡券是否为本人的卡券
            if (couponDetail.getAcceptorUserId() != 0 && !(couponDetail.getAcceptorUserId().equals(regUserId))){
                return new ResponseEntity(ERROR, "卡券激活码只限本人使用！");
            }
            //5.获取卡券对应的卡券产品
            VasCouponProduct couponProduct = couponProductDao.findByPK(Long.valueOf(couponDetail.getCouponProductId()),
                    VasCouponProduct.class);
            if (couponProduct == null){
                logger.error("activeCouponDetail, 激活卡券-卡券产品不存在, 用户标识: {}, 激活码: {}", regUserId, avtKey);
                return new ResponseEntity(ERROR, "卡券产品异常，请联系客服！");
            }
            //条件符合开始激活卡券
            VasCouponDetail updateVasCouponDetail = new VasCouponDetail();
            updateVasCouponDetail.setId(couponDetail.getId());
            updateVasCouponDetail.setAcceptorUserId(regUserId);
            updateVasCouponDetail.setState(VasCouponConstants.COUPON_DETAIL_SEND_ALREADY);
            //好友券初始状态为4-可转赠
            if (Objects.equals(couponProduct.getType(),VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
                updateVasCouponDetail.setState(VasCouponConstants.COUPON_DETAIL_DONATION_ONLY);
            }
            int result = vasCouponDetailDao.update(updateVasCouponDetail);
            if (result <= 0) {
                logger.error("activeCouponDetail, 激活卡券-卡券信息更新失败, 用户标识: {}, 激活码: {}", regUserId, avtKey);
                return new ResponseEntity(ERROR, "卡券激活失败，请联系客服人员！");
            }
        } catch (Exception e) {
            logger.error("activeCouponDetail, 激活卡券异常, 用户标识: {}, 激活码: {}, 异常信息: ", regUserId, avtKey, e);
            throw new GeneralException("激活卡券异常，请联系客服人员！");
        } finally {
            jedisLock.freeLock(lockKey);
        }
        return new ResponseEntity(SUCCESS, "激活成功！");
    }

    @Override
    public Integer getUserCouponDetailListCount(Map<String, Object> param) {
        return vasCouponDetailDao.getUserCouponDetailListCount(param);
    }

    @Override
    public ResponseEntity generateCouponDetailList(Integer couponId, Integer num) {
        logger.error("generateCouponDetailList, 生成线下卡券数据, 卡券产品id: {}, 生成数量: {}", couponId, num);
        List<VasCouponDetailVO> resultList = new ArrayList<>();
        try{
            if (couponId != null && num != null) {
                VasCouponProduct product = couponProductDao.findByPK(Long.valueOf(couponId), VasCouponProduct.class);
                if (product != null) {
                    List<VasCouponDetail> list = this.generateCouponDetail(product, num);
                    list.stream().forEach((vasCouponDetail -> {
                        VasCouponDetailVO vasCouponDetailVO = new VasCouponDetailVO();
                        vasCouponDetailVO.setAmountMin(product.getAmountMin());
                        vasCouponDetailVO.setAmountMax(product.getAmountMax());
                        vasCouponDetailVO.setName(product.getName());
                        vasCouponDetailVO.setWorth(vasCouponDetail.getWorth());
                        vasCouponDetailVO.setAvtKey(vasCouponDetail.getAvtKey());
                        vasCouponDetailVO.setBeginTime(vasCouponDetail.getBeginTime());
                        vasCouponDetailVO.setEndTime(vasCouponDetail.getEndTime());
                        vasCouponDetailVO.setBidProductTypeRange(vasCouponDetail.getBidProductTypeRange());
                        resultList.add(vasCouponDetailVO);
                    }));
                } else {
                    return new ResponseEntity(ERROR, "卡券产品不存在！");
                }
            }
        }catch (Exception e){
            logger.error("generateCouponDetailList, 生成线下卡券数据异常, 卡券产品id: {}, 生成数量: {}, 异常信息: ", couponId, num, e);
            throw new GeneralException("生成线下卡券数据异常！");
        }
        return new ResponseEntity<>(SUCCESS, resultList);
    }

    @Override
    public void couponDetailOverDue(Date currentDate, int shardingItem) {
        String currentJobDate = DateUtils.format(currentDate, DateUtils.DATE_HH_MM_SS);
        try{
            //1.获取已过期的卡券、投资红包集合；当前时间超过使用截止时间且状态不为已过期状态
            List<VasCouponDetail> list = this.vasCouponDetailDao.getExpiredCouponDetailList(currentDate);
            //2.组装更新记录的条件
            list.forEach((couponDetail)->{
                couponDetail.setState(VasCouponConstants.COUPON_DETAIL_OUT_OF_DATE);
                couponDetail.setModifyTime(DateUtils.getCurrentDate());
            });
            logger.info("couponDetailOverDue, 设置已过期的卡券、投资红包为已过期状态, 处理时间: {}, 处理分片项: {}, " +
                    "要处理的卡券集合: {}", currentJobDate, shardingItem, JSON.toJSON(list));
            //3.批量更新记录为已过期状态
            if(list.size() > 0){
                this.vasCouponDetailDao.updateBatch(VasCouponDetail.class, list, list.size());
            }
        }catch (Exception e){
            logger.error("couponDetailOverDue, 设置已过期的卡券、投资红包为已过期状态异常, 处理时间: {}, 处理分片项: {}, 异常信息: ",
                    currentJobDate, shardingItem, e);
        }
    }

    @Override
    public void generateUserCouponDetail(Integer regUserId, Integer productId, int num,String reason) {
        VasCouponProduct product = couponProductDao.findByPK(Long.valueOf(productId), VasCouponProduct.class);
        if (product == null){
            logger.error("generateUserCouponDetail, 跑批生成用户对应的卡券, 卡券产品不存在, 接收用户id: {}, 卡券产品id: {}, 生成数量: {}, " +
                            "生成原因: {}, 异常信息: ",
                    regUserId, productId, num, reason);
            return;
        }
        int state = COUPON_DETAIL_SEND_ALREADY;
        if(Objects.equals(product.getType(),VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
            state = COUPON_DETAIL_DONATION_ONLY;
        }
        //初始化卡券详情列表
        List<VasCouponDetail> list = new ArrayList<>(num);
        try{
            for (int i = 0;i < num;i++){
                VasCouponDetail detail = VasCouponDetailUtil.assembleCouponDetail(product, state, VasCouponConstants.COUPON_DETAIL_SOURCE_TIMED_TASK, reason, regUserId);
                list.add(detail);
            }
            vasCouponDetailDao.insertBatch(VasCouponDetail.class, list);
        }catch (Exception e){
            logger.error("generateUserCouponDetail, 跑批生成用户对应的卡券异常, 接收用户id: {}, 卡券产品详情: {}, 生成数量: {},生产原因: {}, 异常信息: ",
                    regUserId, product.toString(), num, reason, e);
            throw new GeneralException("跑批生成用户对应的卡券异常！");
        }
    }

    @Override
    public Map<Integer, VasCouponDetail> findVasCouponDetailByIds(Set<Integer> couponIds) {
        return vasCouponDetailDao.findVasCouponDetailByIds(couponIds);
    }

    @Override
    public List<VasCouponDetailVO> getUserWithdrawUsableCoupon(Integer regUserId) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("acceptorUserId", regUserId);
        param.put("state", VasCouponConstants.COUPON_DETAIL_SEND_ALREADY);
        param.put("type", VasCouponConstants.COUPON_PRODUCT_TYPE_DEPOSIT);
        List<VasCouponDetailVO> couponDetailVOList = vasCouponDetailDao.getUserCouponDetailList(param);
        return couponDetailVOList.stream().filter(vasCouponDetailVO -> vasCouponDetailVO.getBeginTime().before(new Date())).collect(Collectors.toList());
    }

}
