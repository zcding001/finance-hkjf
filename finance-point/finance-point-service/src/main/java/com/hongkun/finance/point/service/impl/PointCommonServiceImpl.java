package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.dao.PointAccountDao;
import com.hongkun.finance.point.dao.PointRecordDao;
import com.hongkun.finance.point.dao.PointRuleDao;
import com.hongkun.finance.point.dao.PointSignInfoDao;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.model.PointSignInfo;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.utils.PointUtils;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.*;

@Service
public class PointCommonServiceImpl implements PointCommonService {
    private static final Logger logger = LoggerFactory.getLogger(PointCommonServiceImpl.class);

    @Autowired
    private PointRuleDao pointRuleDao;

    @Autowired
    private PointRecordDao pointRecordDao;

    @Autowired
    private PointAccountDao pointAccountDao;

    @Autowired
    private PointSignInfoDao pointSignInfoDao;

    @Override
    public BigDecimal pointToMoney(int point) {
        BigDecimal money = new BigDecimal(0);
        PointRule pointRule = new PointRule();
        pointRule.setState(PointConstants.CHECK_PASS);
        List<PointRule> pointRules = this.pointRuleDao.findByCondition(pointRule);
        if (CommonUtils.isNotEmpty(pointRules)) {
            PointRule currentRule = pointRules.get(0);
            money = PointUtils.pointToMoney(point, currentRule.getPerMoneyToPoint());
        }
        return money;
    }

    /**
    *  @Description    ：给用户赠送积分
    *  @Method_Name    ：givePointToUser
    *  @param pointVO 需要被保存的积分信息
    *  @param currentUserId 当前用户ID
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity givePointToUser(PointVO pointVO, Integer currentUserId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: givePointToUser, 执行赠送积分操作, 入参: pointVO: {}, 当前操作用户ID: {}", pointVO, currentUserId);
        }
        try {
            pointVO.getUserIds().forEach((userId) -> {
              //step 1:生成积分记录
                PointRecord unSaveRecord = initRecord(pointVO, userId, currentUserId);
                //step 2:插入积分记录
                //设置积分状态为待审核
                unSaveRecord.setState(PointConstants.POINT_STATE_UNCHECK);
                //设置类型为平台赠送
                unSaveRecord.setType(PointConstants.POINT_TYPE_DONATE);
                pointRecordDao.save(unSaveRecord);
            });
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("平台执行赠送积分操作异常, pointVO: {}, 操作用户ID: {}\n异常信息: ", pointVO, currentUserId, e);
            }
            throw new GeneralException("执行赠送积分操作异常,请重试!");
        }

        return ResponseEntity.SUCCESS;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity signInPoint(PointVO unSavedPointVO, Integer currentUserId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: signInPoint, 执行签到送积分, 入参: unSavedPointVO: {}, 当前用户ID: {}", unSavedPointVO, currentUserId);
        }
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + PointCommonService.class.getSimpleName() + "_signInPoint_" + currentUserId;
        try {
            //上redis锁防止并发
            if (!jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
                logger.error("signInPoint, 积分签到时获取锁超时, 用户标识: {}, 签到积分: {}", currentUserId, unSavedPointVO.getPoint());
                return new ResponseEntity(ERROR, "积分签到超时，请重新操作！");
            }
           //step 0:判断用是否是否已经签过到
            if (this.pointRecordDao.userSignCount(currentUserId) > 0L) {
                return ResponseEntity.error("用户今天已经签到过, 不能重复签到");
            }
            // step 1:初始化积分记录
            PointRecord unSaveRecord = initRecord(unSavedPointVO, currentUserId, currentUserId);
            //step 2:插入积分记录
            //设置积分状态为已经确认
            unSaveRecord.setState(PointConstants.POINT_STATE_CONFIRMED);
            //设置类型为签到
            unSaveRecord.setType(PointConstants.POINT_TYPE_SIGN_IN);
            //插入积分记录
            pointRecordDao.save(unSaveRecord);
            //step 3:更新用户连续签到信息
            PointSignInfo pointSignInfo = pointSignInfoDao.getByRegUserId(currentUserId);
            //第一次生成该记录，插入一条
            if (pointSignInfo == null){
                PointSignInfo insertSignInfo = new PointSignInfo();
                insertSignInfo.setCount(1);
                insertSignInfo.setTotal(1);
                insertSignInfo.setRegUserId(currentUserId);
                pointSignInfoDao.save(insertSignInfo);
            }else {
                PointSignInfo updateSignInfo = new PointSignInfo();
                updateSignInfo.setId(pointSignInfo.getId());
                //签到总数在sql中进行加1操作
                updateSignInfo.setTotal(1);
                //判断是否连续签到,如果为连续签到，签到天数+1，否则从1开始
                if (DateUtils.getDaysBetween(new Date(),pointSignInfo.getModifyTime()) == 1){
                    updateSignInfo.setCount(1);
                }else {
                    updateSignInfo.setCount((pointSignInfo.getCount()-1)*-1);
                }
                pointSignInfoDao.update(updateSignInfo);
            }
            //用户积分账户增加积分
            PointAccount acceptAccount = new PointAccount();
            acceptAccount.setRegUserId(currentUserId);
            acceptAccount.setPoint(unSaveRecord.getPoint());
            pointAccountDao.updateByRegUserId(acceptAccount);
            if (logger.isInfoEnabled()) {
                logger.info("签到送积分, 签到用户: {},签到赠送积分值: {}", currentUserId, unSaveRecord.getPoint());
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("签到送积分异常, unSavedPointVO: {}, 当前用户ID: {}\n异常信息: ", unSavedPointVO, currentUserId, e);
            }
            throw new GeneralException("执行赠送积分操作异常,请重试");
        }finally {
            jedisLock.freeLock(lockKey);
        }
        //签到赠送成长值
        try {
            VasVipGrowRecordMqVO vasVipGrowRecordMqVO = new VasVipGrowRecordMqVO();
            vasVipGrowRecordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_SIGN);
            vasVipGrowRecordMqVO.setUserId(currentUserId);
            VipGrowRecordUtil.sendVipGrowRecordToQueue(vasVipGrowRecordMqVO);
        }catch (Exception e){
            logger.error("signInPoint, 用户签到成功但成长值消息发送失败 , 用户id: {}, 发送异步消息失败: ", currentUserId, e);
        }
        return ResponseEntity.SUCCESS;
    }



    /**
    *  @Description    ：初始化积分记录
    *  @Method_Name    ：initRecord
    *  @param pointVO 需要被保存的积分信息
    *  @param userId 目标用户的ID
    *  @param currentUserId   当前操作用户的ID
    *  @return com.hongkun.finance.point.model.PointRecord
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private PointRecord initRecord(PointVO pointVO, Integer userId, Integer currentUserId) {

        PointRecord unSaveRecord = new PointRecord();
        BeanPropertiesUtil.splitProperties(pointVO, unSaveRecord);
        //复制属性
        unSaveRecord.setRegUserId(userId);
        //记录平台赠送当前账户的id
        unSaveRecord.setBusinessId(currentUserId);
        unSaveRecord.setCreateUserId(currentUserId);
        unSaveRecord.setModifyUserId(currentUserId);
        unSaveRecord.setType(PointConstants.POINT_TYPE_DONATE);
        //设置积分价值
        //1.计算积分价值
        Integer pointValue = pointVO.getPoint();
        BigDecimal worth = pointToMoney(pointValue);
        unSaveRecord.setWorth(worth);
        unSaveRecord.setRealWorth(worth);

        //设置平台
        unSaveRecord.setPlatform(1);

        return unSaveRecord;
    }

    @Override
    public ResponseEntity<?> hasSign(Integer regUserId) {
        int state = (this.pointRecordDao.userSignCount(regUserId) > 0L) ? 1 : 0;
        Map<String,Object> param = new HashMap<>();
        param.put("state",state);
        return new ResponseEntity<>(SUCCESS,"",param);
    }
}
