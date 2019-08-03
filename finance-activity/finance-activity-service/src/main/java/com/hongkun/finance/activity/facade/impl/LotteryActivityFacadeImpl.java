package com.hongkun.finance.activity.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.activity.facade.LotteryActivityFacade;
import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.LotteryAlgorithm;
import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.model.LotteryRecord;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.hongkun.finance.activity.service.LotteryActivityService;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.hongkun.finance.activity.service.LotteryRecordService;
import com.hongkun.finance.activity.util.ActivityUtils;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.hongkun.finance.vas.utils.VasCouponDetailUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE;
import static com.hongkun.finance.vas.constants.VasConstants.SEND_BY_PLATFORM;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_DETAIL_SEND_ALREADY;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_DETAIL_SOURCE_OFFLINE;
import static com.yirun.framework.core.commons.Constants.LOCK_PREFFIX;

/**
 * @Description: 抽奖活动服务
 * @Program: com.hongkun.finance.activity.facade.impl.LotteryActivityFacadeImpl
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-15 10:07
 **/
@Service
public class LotteryActivityFacadeImpl implements LotteryActivityFacade{

    private static Logger logger = LoggerFactory.getLogger(LotteryActivityFacadeImpl.class);

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @Autowired
    private LotteryItemService lotteryItemService;

    @Autowired
    private LotteryRecordService lotteryRecordService;

    @Reference
    private RosInfoService rosInfoService;

    @Reference
    private RegUserService regUserService;

    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private BidInvestService bidInvestService;

    @Reference
    private VasRedpacketInfoService vasRedpacketInfoService;

    @Reference
    private PointAccountService pointAccountService;

    @Reference
    private PointRecordService pointRecordService;

    @Reference
    private VasCouponProductService vasCouponProductService;

    @Reference
    private VasCouponDetailService vasCouponDetailService;

    @Reference
    private FinConsumptionService finConsumptionService;

    @Override
    public ResponseEntity<?> findLotteryActivity(String activityId) {
        Map<String,Object> params = new HashMap<>();
        try{
            LotteryActivity lotteryActivity = null;
            if(!StringUtils.isEmpty(activityId)){
                lotteryActivity = lotteryActivityService.findLotteryActivityById(Integer.valueOf(activityId));
            }
            if(null == lotteryActivity){
                return ResponseEntity.error("活动不存在");
            }
            int flag = DateUtils.betDate(lotteryActivity.getEndTime(), new Date()) <= 0 ? 1:0;
            params.put("flag",flag);
            params.put("info",lotteryActivity);
            Map<String,Object> idsAndAngles = getIdsAndAngles(lotteryActivity);
            params.put("ids", idsAndAngles.get("ids"));
            params.put("angles",idsAndAngles.get("angles"));
            // 随机获取中奖列表
            List<String> showAwards = lotteryRecordService.getLotteryRecordListForShow(lotteryActivity);
            params.put("showAwards", showAwards);
        }catch (Exception e){
            logger.error("获取活动信息失败",e);
            throw new GeneralException("活动获取失败！");
        }
        return new ResponseEntity<>(Constants.SUCCESS,"获取活动成功！",params);
    }



    @Override
    public ResponseEntity<?> lottery(String tel, String activityId, Integer userLocationFlag) {

        Map<String,Object>  params = new HashMap<>();
        // 用户手机号验证
        String reg = "^0{0,1}(13[0-9]|15[0-9]|18[0-9]|14[0-9]|16[0-9]|17[0-9])[0-9]{8}$";
        if (StringUtils.isEmpty(tel) || !Pattern.matches(reg, tel)) {
            return ResponseEntity.error("请输入正确的手机号");
        }
        Long longTel = Long.valueOf(tel.trim());
        Integer intActivityId = Integer.valueOf(activityId.trim());
        JedisClusterLock lock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + LotteryActivity.class.getSimpleName() + activityId;
        try {
            if (lock.lock(lockKey)) {
                LotteryActivity lotteryActivity= lotteryActivityService.findLotteryActivityById(intActivityId);
                if (lotteryActivity == null || lotteryActivity.getState().equals(0)) {
                    return ResponseEntity.error("活动不存在");
                }
                if (DateUtils.betDate(new Date(), lotteryActivity.getStartTime()) <= 0) {
                    return ResponseEntity.error("亲，活动还没开始呢！");
                } else if (DateUtils.betDate(lotteryActivity.getEndTime(), new Date()) <= 0) {
                    return ResponseEntity.error("亲，活动已结束了！");
                }
                LotteryRecord record = new LotteryRecord();
                record.setTel(longTel);
                record.setLotteryActivityId(intActivityId);
                Map<String,Integer> dayAndTotalCount = lotteryRecordService.getDayAndTotalCount(record);
                int dayCount = dayAndTotalCount.get("dayCount");
                int totalCount = dayAndTotalCount.get("totalCount");

                if(totalCount >= lotteryActivity.getLimitUserTimes()){
                    return ResponseEntity.error("您本次活动的抽奖机会已经用完，尽情期待下次活动吧~~");
                }
                if(dayCount >= lotteryActivity.getLimitDayTimes()){
                    return ResponseEntity.error("您今日的抽奖机会已经用完，明天再来吧~~");
                }

                RegUser regUser = regUserService.findRegUserByLogin(longTel);
                // 抽奖
                LotteryItem lotsItem = this.getRandomPrize(lotteryActivity,longTel,userLocationFlag);
                if (null == lotsItem) {
                    return ResponseEntity.error("本次活动奖品已经被抽完啦，下次再来吧~~");
                }
                // 添加中奖纪录
                int id = this.insertLotteryRecord(lotsItem, tel,regUser);
                if (id < 0) {
                    // 抽奖失败
                    return ResponseEntity.error("抽奖失败");
                } else {
                    // 抽奖成功  发放奖品
                    int type = lotsItem.getItemType();
                    this.sendLotteryItem(lotsItem, longTel);
                    params.put("id", lotsItem.getId());
                    params.put("itemName", lotsItem.getItemName());
                    params.put("tel",tel);
                    params.put("type", type);
                    return new ResponseEntity<>(Constants.SUCCESS,"抽奖成功",params);
                }
            }else {
                logger.info("抽奖获取分布式锁失败，tel:{},activityId:{}.",tel,activityId);
                return ResponseEntity.error("当前抽奖人数较多，请稍后再试!");
            }
        } catch (Exception e) {
            logger.info("抽奖异常，tel:{},activityId:{}.",tel,activityId);
            return ResponseEntity.error("摇奖系统繁忙，请稍后再试!");
        }finally {
            lock.freeLock(lockKey);
        }
    }


    @Override
    public ResponseEntity<?> getMyPrizeList(RegUser loginUser) {
        Map<String,Object> params = new HashMap<>();
        if(null == loginUser){
            params.put("loginFlag",0);
            return new ResponseEntity<>(Constants.ERROR,"用户未登录",params);
        }

        List<LotteryActivity> activityList = lotteryActivityService.findLotteryActivityByTel(loginUser.getLogin());
        params.put("activityList",activityList);
        try{
            // 添加投资白名单
            if(!rosInfoService.validateRoster(loginUser.getId(), RosterType.INVEST_CTL, RosterFlag.WHITE)
                    && !rosInfoService.validateRoster(loginUser.getId(), RosterType.INVEST_CTL, RosterFlag.BLACK)){
                RosInfo rosInfo = new RosInfo();
                rosInfo.setRegUserId(loginUser.getId());
                rosInfo.setLogin(loginUser.getLogin());
                rosInfo.setType(RosterType.INVEST_CTL.getValue());
                rosInfo.setFlag(RosterFlag.WHITE.getValue());
                rosInfoService.insertRosInfo(rosInfo);
            }
        }catch (Exception e){
            logger.error("活动抽奖用户添加投资白名单失败, regUserId:{}",loginUser.getId());
        }
        return new ResponseEntity<>(Constants.SUCCESS,"获取活动列表成功",params);
    }



    @Override
    public ResponseEntity<?> exchangePrize(String activityId, String lotteryRecordId, RegUser loginUser) {
        Map<String,Object> params = new HashMap<>();
        if(null == loginUser){
            params.put("loginFlag",0);
            return new ResponseEntity<>(Constants.ERROR,"用户未登录",params);
        }
        try {
            LotteryRecord lotteryRecord = lotteryRecordService.findLotteryRecordById(Integer.valueOf(lotteryRecordId));
            if (lotteryRecord != null && !StringUtils.isEmpty(lotteryRecord.getVerfication())
                    && lotteryRecord.getState() == 1) {
                logger.info("注册手机号{} ,已经兑换过验证码!",loginUser.getLogin());
                return new ResponseEntity<>(Constants.ERROR,"注册手机号：" + loginUser.getLogin() + ",已经兑换过验证码!" );
            } else if (lotteryRecord != null && lotteryRecord.getState() == 1) {
                // 提示用户已经有兑换码
                logger.info("注册手机号{}, 奖品已经发放!",loginUser.getLogin());
                return new ResponseEntity<>(Constants.ERROR,"注册手机号：" + loginUser.getLogin() + ",奖品已经发放!" );
            }

            LotteryActivity lotteryActivity = lotteryActivityService.findLotteryActivityById(Integer.valueOf(activityId.trim()));
            if (DateUtils.betDate(lotteryActivity.getEndTime(), new Date()) <= 0) {
                return new ResponseEntity<>(Constants.ERROR,"亲，活动已结束了！");
            }

            int countTmp = 0;
            String verfication = "";
            while (true) {
                // 随机4位数字，规则：0-9随机组合
                verfication = ActivityUtils.creEightNo(4);
                countTmp = lotteryRecordService.checkVerfication(verfication);
                if (countTmp == 0) {
                    break;
                }
            }
            LotteryRecord record = new LotteryRecord();
            record.setId(Integer.valueOf(lotteryRecordId.trim()));
            record.setVerfication(verfication);
            record.setState(1); // 已兑换
            int count = lotteryRecordService.updateLotteryRecord(record);
            if (count > 0) {
                logger.info("注册手机号{}, ,奖品兑换成功!",loginUser.getLogin());
                params.put("exchangeCode", verfication); // 兑换码
                return new ResponseEntity<>(Constants.SUCCESS,"恭喜您，奖品兑换成功！",params);
            } else {
                logger.info("注册手机号{}, ,奖品生成验证码异常!",loginUser.getLogin());
                return new ResponseEntity<>(Constants.ERROR,"兑奖失败！",params);
            }
        } catch (Exception e) {
            logger.error("注册手机号：{},兑换奖品失败",loginUser.getLogin() );
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getMyPrizeInfo(String activityId, RegUser loginUser) {
        Map<String,Object> params = new HashMap<>();
        if(null == loginUser){
            params.put("loginFlag",0);
            return new ResponseEntity<>(Constants.ERROR,"用户未登录",params);
        }
        if(StringUtils.isEmpty(activityId)){
            return new ResponseEntity<>(Constants.ERROR,"活动不存在",params);
        }
        LotteryRecord lotteryRecord = new LotteryRecord();
        lotteryRecord.setLotteryActivityId(Integer.valueOf(activityId.trim()));
        lotteryRecord.setTel(loginUser.getLogin());
        List<LotteryRecordVo> lotteryRecordList = lotteryRecordService.findLotteryRecordDetailList(lotteryRecord);
        if(!CollectionUtils.isEmpty(lotteryRecordList)){
            params.put("introduction",lotteryRecordList.get(0).getIntroduction());
        }
        params.put("infoList",lotteryRecordList);
        return new ResponseEntity<>(Constants.SUCCESS,"获取中奖记录成功",params);
    }


    /**
     *  @Description    ：获取奖品id及对应的角度
     *  @Method_Name    ：getIdsAndAngles
     *  @param lotteryActivity
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年10月15日 15:36
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private Map<String,Object> getIdsAndAngles(LotteryActivity lotteryActivity) {
        HashMap<String, Object> map = new HashMap<>();
        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setLotteryActivityId(lotteryActivity.getId());
        lotteryItem.setState(1);
        try {
            List<LotteryItem> items = lotteryItemService.findLotteryItemList(lotteryItem);
            Collections.sort(items,Comparator.comparing(LotteryItem::getSequenceNum));
            if(!CollectionUtils.isEmpty(items)){
                StringBuilder idsBuffer = new StringBuilder();
                StringBuilder anglesBuffer = new StringBuilder();
                int size = items.size();
                int deltaAngle = 360 / size;
                for (int i = 0; i < size; i++) {
                    idsBuffer.append(items.get(i).getId() + "-");
                    anglesBuffer.append(deltaAngle * (i + 1) + "-");
                }
                map.put("ids",idsBuffer.substring(0,idsBuffer.length() -1));
                map.put("angles",anglesBuffer.substring(0,anglesBuffer.length()-1));
                return map;
            }
        } catch (Exception e) {
            logger.error("获取所含奖品id及对应的角度失败！",e);
        }
        return map;
    }


    /**
     *  @Description    ：获取中奖奖品
     *  @Method_Name    ：getRandomPrize
     *  @param lotteryActivity
     *  @param tel
     *  @param request
     *  @return com.hongkun.finance.activity.model.LotteryItem
     *  @Creation Date  ：2018年10月15日 15:35
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private LotteryItem getRandomPrize(LotteryActivity lotteryActivity, Long tel, Integer userLocationFlag) {
        // 暂时获得所有奖品 需限制奖品数量
        List<LotteryItem> itemsList = null;
        LotteryAlgorithm lotteryAlgorithm = new LotteryAlgorithm();
        try {
            itemsList = this.limitUserType(lotteryActivity,tel,userLocationFlag);
            itemsList = this.limitCount(itemsList,lotteryActivity);
            if(CollectionUtils.isEmpty(itemsList)){
                return null;
            }
        } catch (Exception e) {
            logger.error("抽奖失败！",e);
        }
        return lotteryAlgorithm.getPrize(itemsList);
    }

    /**
     *  @Description    ：插入中奖记录
     *  @Method_Name    ：insertLotteryRecord
     *  @param lotsItem
     *  @param tel
     *  @param regUser
     *  @return int
     *  @Creation Date  ：2018年10月15日 15:34
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private int insertLotteryRecord(LotteryItem lotsItem, String tel, RegUser regUser) {
        LotteryRecord lotteryRecord = new LotteryRecord();
        lotteryRecord.setLotteryActivityId(lotsItem.getLotteryActivityId());
        lotteryRecord.setLotteryItemId(lotsItem.getId());
        if(lotsItem.getItemType() != 5 ){  // 兑换码奖品
            lotteryRecord.setState(1);
        }else{
            lotteryRecord.setState(0);
        }
        lotteryRecord.setTel(Long.valueOf(tel.trim()));
        if(null != regUser){
            lotteryRecord.setRegUserId(regUser.getId());
            lotteryRecord.setUserName(regUser.getNickName());
            RegUserDetail regDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
            if(null != regDetail){
                lotteryRecord.setUserName(regDetail.getRealName());
            }
        }
        lotteryRecord.setVerfication("");
        return lotteryRecordService.insertLotteryRecord(lotteryRecord);
    }



    /**
     *  @Description    ：奖品数量限制
     *  @Method_Name    ：limitCount
     *  @param itemsList
     *  @param lotteryActivity
     *  @return java.util.List<com.hongkun.finance.activity.model.LotteryItem>
     *  @Creation Date  ：2018年10月15日 15:36
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private List<LotteryItem> limitCount(List<LotteryItem> itemsList, LotteryActivity lotteryActivity) {
        // 直接forEach 会产生并发修改问题
        Iterator<LotteryItem> iterator = itemsList.iterator();
        while (iterator.hasNext()) {
            LotteryItem tempItem = iterator.next();
            if(tempItem.getItemRate().equals(0D)){
                iterator.remove();
                continue;
            }
            // 今日抽奖个数显示
            LotteryRecord lotteryRecord = new LotteryRecord();
            lotteryRecord.setLotteryActivityId(lotteryActivity.getId());
            lotteryRecord.setLotteryItemId(tempItem.getId());
            lotteryRecord.setCreateTimeBegin(DateUtils.getFirstTimeOfDay());
            lotteryRecord.setCreateTimeEnd(DateUtils.getLastTimeOfDay());
            int totalCount = lotteryRecordService.getDayAndTotalCount(lotteryRecord).get("totalCount");
            long deltaDays = DateUtils.getDaysBetween(lotteryActivity.getStartTime(), lotteryActivity.getEndTime());
            if((tempItem.getItemCount()/(deltaDays + 1)) <= totalCount){
                iterator.remove();
            }
        }
        return itemsList;


    }


    /**
     *  @Description    ：查询适合抽奖用户的奖品集
     *  @Method_Name    ：limitUserType
     *  @param lotteryActivity
     *  @param tel
     *  @param userLocationFlag
     *  @return java.util.List<com.hongkun.finance.activity.model.LotteryItem>
     *  @Creation Date  ：2018年10月15日 15:37
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private List<LotteryItem> limitUserType(LotteryActivity lotteryActivity, Long tel, Integer userLocationFlag) throws Exception{
        // 0-全部, 1-新用户, 2-老用户, 3-男用户, 4-女用户, 5-已投资用户, 6-未投资用户
        List<Integer> groups = lotteryItemService.getLotteryItemGroupById(lotteryActivity.getId());
        // 是否为京籍：0-全部，1-是，2-否
        List<Integer> locationFlags = lotteryItemService.getLotteryItemLocationFlagById(lotteryActivity.getId());

        // 用户信息
        List<Integer> userGroupTypes = getUserGroupType(tel);
        if(CollectionUtils.isEmpty(groups) || CollectionUtils.isEmpty(locationFlags)){
            logger.error("活动没有设置合适的奖品集！");
            throw new Exception("活动没有设置合适的奖品集");
        }
        // 判断包含关系
        Integer group = 0;
        Integer locationFlag = 0;
        if(groups.contains(0) && !locationFlags.contains(0)){
            locationFlag = userLocationFlag;
        }else if (!groups.contains(0)){
            for (Integer tempGroup : groups) {
                for (Integer userGroupType : userGroupTypes) {
                    if(tempGroup.equals(userGroupType)){
                        group = tempGroup;
                        break;
                    }
                }
            }
            if(!locationFlags.contains(0)){
                locationFlag = userLocationFlag;
            }
        }
        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setLotteryActivityId(lotteryActivity.getId());
        lotteryItem.setState(1);
        lotteryItem.setLocationFlag(locationFlag);
        lotteryItem.setItemGroup(group);
        return lotteryItemService.findLotteryItemList(lotteryItem);
    }


    /**
     *  @Description    ：获取用户组信息
     *  @Method_Name    ：getUserGroupType
     *  @param tel
     *  @return java.util.List<java.lang.Integer>
     *  @Creation Date  ：2018年10月15日 15:37
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private List<Integer> getUserGroupType(Long tel) {
        List<Integer> groupTypes = new ArrayList<>();
        RegUser regUser = regUserService.findRegUserByLogin(tel);
        groupTypes.add(null == regUser ? 1 : 2);
        // 用户不存在 默认 女用户 且 未投资用户
        if(regUser == null ){
            groupTypes.add(4);
            groupTypes.add(6);
            return  groupTypes;
        }
        RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
        String idCard = regUserDetail.getIdCard();
        if(StringUtils.isEmpty(idCard)){
            groupTypes.add(4);
            groupTypes.add(6);
            return groupTypes;
        }
        groupTypes.add(Integer.valueOf(idCard.length() == 15 ?
                idCard.substring(14,15) : idCard.substring(16,17)) % 2 == 0 ? 4 : 3);
        // 查询投资记录，体验金&活期不作为新手标判断依据
        Integer count = this.bidInvestService.findBidInvestCountForPrefered(regUser.getId());
        if(count > 0){
            groupTypes.add(5);
        }else{
            groupTypes.add(6);
        }
        return groupTypes;
    }




    /**
     *  @Description    ：发放奖品
     *  @Method_Name    ：sendLotteryItem
     *  @param lotsItem
     *  @param tel
     *  @return void
     *  @Creation Date  ：2018年10月15日 15:47
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private void sendLotteryItem(LotteryItem lotsItem, Long tel) {
        try {
            RegUser regUser = regUserService.findRegUserByLogin(tel);
            if (lotsItem.getItemType() == 1) {
                // 红包
                sendRedPackage(lotsItem, tel, regUser);
            } else if (lotsItem.getItemType() == 2) {
                // 积分
                sendPoint(lotsItem, tel, regUser);
            } else if (lotsItem.getItemType() == 3) {
                // 投资红包
                sendRechargeableCard(lotsItem, tel, regUser);
            } else if (lotsItem.getItemType() == 4) {
                // 加息券
                sendRechargeableCard(lotsItem, tel, regUser);
            }
        } catch (Exception e) {
            logger.error("抽奖发奖失败,用户手机号{},异常信息{}", tel, e);
        }
    }

    /**
     *  @Description    ：发投资红包及加息券
     *  @Method_Name    ：sendRechargeableCard
     *  @param lotteryItem
     *  @param tel
     *  @param regUser
     *  @return void
     *  @Creation Date  ：2018年10月15日 15:48
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private void sendRechargeableCard(LotteryItem lotteryItem, Long tel, RegUser regUser) {
        try {
            if(null != regUser){
                VasCouponProduct vasCouponProduct = null;
                //判断对应卡券是否已创建没有则创建, 使用奖品的note存储卡券的id
                if(!StringUtils.isEmpty(lotteryItem.getNote())){
                    vasCouponProduct = vasCouponProductService.findVasCouponProductById(Integer.parseInt(lotteryItem.getNote()));
                }
                if(null == vasCouponProduct){
                    vasCouponProduct = getCustomDefinedProduct(lotteryItem);
                }
                VasCouponDetail vasCouponDetail = VasCouponDetailUtil.assembleCouponDetail(vasCouponProduct, COUPON_DETAIL_SEND_ALREADY,
                        COUPON_DETAIL_SOURCE_OFFLINE, "抽奖活动赠送", regUser.getId());
                int count = vasCouponDetailService.insertVasCouponDetail(vasCouponDetail);
                if(count > 0){
                    // 发送短信及站内信
                    StringBuilder msg = new StringBuilder();
                    msg.append("恭喜您，您摇中").append(lotteryItem.getItemName())
                            .append("！登录鸿坤金服到“我的账户”中查询，可下载APP客户端在“我的福利中”快速查询！");
                    SmsWebMsg smsWebMsg = new SmsWebMsg(regUser.getId(),"发奖通知",msg.toString(),1);
                    SmsTelMsg smsTelMsg = new SmsTelMsg(regUser.getId(),tel,msg.toString(),1);
                    SmsSendUtil.sendSmsMsgToQueue(smsWebMsg,smsTelMsg);
                }
            }
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("抽奖活动发放投资红包或加息券异常,用户手机号{},异常信息{}",tel,e);
            }
        }
    }

    /**
     *  @Description    ：自定义生成卡券产品
     *  @Method_Name    ：getCustomDefinedProduct
     *  @param lotteryItem
     *  @return com.hongkun.finance.vas.model.VasCouponProduct
     *  @Creation Date  ：2018年10月17日 15:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private VasCouponProduct getCustomDefinedProduct(LotteryItem lotteryItem) {
        VasCouponProduct vasCouponProduct = null;
        try {
            LotteryActivity activity = lotteryActivityService.findLotteryActivityById(lotteryItem.getLotteryActivityId());
            vasCouponProduct = new VasCouponProduct();
            if(lotteryItem.getItemType() == 3){
                vasCouponProduct.setType(1);
                vasCouponProduct.setName("抽奖活动" + activity.getName() + " " + lotteryItem.getAmountAtm() + "元投资红包");
            }else{
                vasCouponProduct.setType(0);
                vasCouponProduct.setName("抽奖活动" + activity.getName() + " " + lotteryItem.getAmountAtm() + "%加息券");
            }
            vasCouponProduct.setDeadlineType(0);
            //vasCouponProduct.setWorth(new BigDecimal(lotteryItem.getAmountAtm())); 构造方式有精度问题，用string或者下面方式
            vasCouponProduct.setWorth(BigDecimal.valueOf(lotteryItem.getAmountAtm()));
            vasCouponProduct.setBidProductTypeRange("2,3,4");
            vasCouponProduct.setAmountMin(new BigDecimal(100));
            vasCouponProduct.setAmountMax(new BigDecimal(99999999.99));
            vasCouponProduct.setValidDay(30);
            vasCouponProduct.setCouponScenes(0);
            vasCouponProduct.setState(1);
            vasCouponProductService.insertVasCouponProduct(vasCouponProduct);
            //更新奖品note为卡券id
            LotteryItem item = new LotteryItem();
            item.setId(lotteryItem.getId());
            item.setNote(String.valueOf(vasCouponProduct.getId()));
            lotteryItemService.updateLotteryItem(item);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("抽奖活动创建卡券信息失败",e);
                throw new GeneralException("抽奖活动创建卡券信息失败");
            }
        }
        return vasCouponProduct;
    }

    /**
     *  @Description    ：发积分
     *  @Method_Name    ：sendPoint
     *  @param lotteryItem
     *  @param tel
     *  @param regUser
     *  @return void
     *  @Creation Date  ：2018年10月15日 15:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private void sendPoint(LotteryItem lotteryItem, Long tel, RegUser regUser) {
        try {
            if(null != regUser){
                //初始化积分账户
                PointAccount pointAccount = new PointAccount();
                pointAccount.setRegUserId(regUser.getId());
                pointAccount.setPoint(lotteryItem.getAmountAtm().intValue());
                //根据原来的积分更新现有的积分
                pointAccountService.updateByRegUserId(pointAccount);
                //插入一条积分记录
                PointRecord record = new PointRecord();
                record.setComments("抽奖活动获得积分，用户手机号："+regUser.getLogin());
                Date currentDate = new Date();
                record.setRegUserId(regUser.getId());
                record.setCreateTime(currentDate);
                record.setModifyTime(currentDate);
                record.setPoint(lotteryItem.getAmountAtm().intValue());
                record.setBusinessId(lotteryItem.getId());
                //状态：已确认
                record.setState(PointConstants.POINT_STATE_CONFIRMED);
                //类型：投资送积分
                record.setType(PointConstants.POINT_TYPE_ACTIVITY);
                //插入积分
                int count = pointRecordService.insertPointRecord(record);
                if(count > 0){
                    // 发送短信站内信
                    StringBuilder msg = new StringBuilder();
                    msg.append("恭喜您，您摇中").append(lotteryItem.getItemName())
                            .append("！登录鸿坤金服到“我的账户”中查询，也可下载鸿坤金服APP客户端在“我的账户”快速查询。");
                    SmsWebMsg smsWebMsg = new SmsWebMsg(regUser.getId(),"发奖通知",msg.toString(),1);
                    SmsTelMsg smsTelMsg = new SmsTelMsg(regUser.getId(),tel,msg.toString(),1);
                    SmsSendUtil.sendSmsMsgToQueue(smsWebMsg,smsTelMsg);
                }
            }
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("抽奖活动发放积分异常,用户手机号{},异常信息{}",tel,e);
            }
        }

    }

    /**
     *  @Description    ：发红包
     *  @Method_Name    ：sendRedPackage
     *  @param lotteryItem
     *  @param tel
     *  @param regUser
     *  @return void
     *  @Creation Date  ：2018年10月15日 15:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    private void sendRedPackage(LotteryItem lotteryItem, Long tel, RegUser regUser) {
        try {
            if(null != regUser){
                VasRedpacketInfo vasRedpacketInfo = new VasRedpacketInfo();
                //设置来源为运营派发
                vasRedpacketInfo.setRedpacketSource(VasConstants.REDPACKET_SOURCE_OFFLINE);
                //设置状态为未领取
                vasRedpacketInfo.setState(VasConstants.REDPACKET_STATE_UNRECEIVED);
                //设置为平台派发
                vasRedpacketInfo.setSenderUserName(SEND_BY_PLATFORM);
                //设置红包类型-活动红包
                vasRedpacketInfo.setType(4);
                //设置派发原因
                vasRedpacketInfo.setSendReason("抽奖活动派发红包");
                vasRedpacketInfo.setValue(new BigDecimal(lotteryItem.getAmountAtm()));
                vasRedpacketInfo.setEndTime(DateUtils.addDays(new Date(),30));
                //派发人
                vasRedpacketInfo.setCreateUserId(UserConstants.PLATFORM_ACCOUNT_ID);
                vasRedpacketInfo.setModifiedUserId(UserConstants.PLATFORM_ACCOUNT_ID);
                vasRedpacketInfo.setSenderUserId(UserConstants.PLATFORM_ACCOUNT_ID);
                //接收人
                vasRedpacketInfo.setAcceptorUserId(regUser.getId());
                VasRedpacketInfo result = vasRedpacketInfoService.insert(vasRedpacketInfo);
                //冻结平台的资金并生成资金流水
                FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(UserConstants.PLATFORM_ACCOUNT_ID, vasRedpacketInfo.getId(),
                        vasRedpacketInfo.getValue(), TradeTransferConstants.TRADE_TYPE_RED_PACKET_GENERATE, PlatformSourceEnums.PC);
                finTradeFlow.setTransferSubCode(TRANSFER_SUB_CODE_FREEZE);
                List<FinTradeFlow> finTradeFlows = new ArrayList<>();
                finTradeFlows.add(finTradeFlow);
                finConsumptionService.cashPayBatch(finTradeFlows);
                if(!StringUtils.isEmpty(result.getKey())){
                    // 发送短信站内信
                    StringBuilder msg = new StringBuilder();
                    msg.append("恭喜您，您摇中的").append(lotteryItem.getItemName()).append("已发放,兑换码：").append(result.getKey())
                            .append(",请到“我的账户>我的红包”中兑换。请继续关注我们，谢谢您的信任与支持!");
                    SmsWebMsg smsWebMsg = new SmsWebMsg(regUser.getId(),"发奖通知",msg.toString(),1);
                    SmsTelMsg smsTelMsg = new SmsTelMsg(regUser.getId(),tel,msg.toString(),1);
                    SmsSendUtil.sendSmsMsgToQueue(smsWebMsg,smsTelMsg);
                }
            }
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("抽奖活动发红包异常,用户手机号{},异常信息{}",tel,e);
            }
        }

    }






}
