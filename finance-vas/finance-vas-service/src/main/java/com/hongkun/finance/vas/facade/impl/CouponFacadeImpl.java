package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.roster.service.RosNoticeService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsAppMsgPush;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponDonationRecord;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.model.vo.VasCouponDonationRecordVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasCouponDonationRecordService;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.hongkun.finance.vas.utils.VasCouponDetailUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hongkun.finance.roster.constants.RosterConstants.ROS_NOTICE_REISSUE_COUPON;
import static com.hongkun.finance.sms.constants.SmsConstants.SMS_APP_MSG_TYPE_COUPON;
import static com.hongkun.finance.user.constants.UserConstants.USER_IDENTIFY_NO;
import static com.hongkun.finance.vas.constants.VasConstants.VAS_STATE_Y;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_DETAIL_SEND_ALREADY;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_PRODUCT_DEADLINE_TYPE_DAYS;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description : CouponDetailFacade实现类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.vas.facade.impl.CouponFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class CouponFacadeImpl implements CouponFacade {

    private static final Logger logger = LoggerFactory.getLogger(CouponFacadeImpl.class);

    @Autowired
    private VasCouponProductService couponProductService;

    @Reference
    private RegUserService userService;

    @Autowired
    private VasCouponDetailService couponDetailService;

    @Autowired
    private VasCouponDonationRecordService couponDonationRecordService;

    @Reference
    private BidInfoService bidInfoService;

    @Reference
    private RosNoticeService rosNoticeService;

    @Override
    public Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO, Pager pager) {
        //所限制的用户id
        List<Integer> limitsUserIds=null;

        if (vasCouponDetailVO.getTel() != null) {
            //如果所填手机号不为空，去用户表中查出显示的userid
            limitsUserIds=userService.findUserIdsByTel(vasCouponDetailVO.getTel());
        }
        if(vasCouponDetailVO.getRegUserId() != null){
            limitsUserIds = new ArrayList<>();
            limitsUserIds.add(vasCouponDetailVO.getRegUserId());
        }

        //查询具有的返回数据
        Pager result = couponProductService.couponDetailSearch(vasCouponDetailVO,limitsUserIds,pager);
        //将用户手机号更新到VO中
        ((List<VasCouponDetailVO>)result.getData()).stream().forEach((vcdVO)->{
            //卡券接收人id不等于0
            if(vcdVO.getAcceptorUserId() != 0){
                vcdVO.setTel(userService.findRegUserById(vcdVO.getAcceptorUserId()).getLogin());
                VasCouponDonationRecord vasCouponDonationRecord = new VasCouponDonationRecord();
                vasCouponDonationRecord.setCouponDetailId(vcdVO.getId());
                int count = couponDonationRecordService.findVasCouponDonationRecordCount(vasCouponDonationRecord);
                vcdVO.setHasDonationRecord(count > 0 ? 1 : 0);
            }
        });
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,readOnly = false)
    public ResponseEntity doCouponDonation(RegUser regUser, int acceptorId, int couponId) {
        logger.info("doCouponDonation, 卡券转赠, 用户标识: {}, 接收用户id: {}, 转赠的卡券id: {}",
                regUser.getId(), acceptorId, couponId);
        VasCouponDetail vasCouponDetail = null;
        VasCouponProduct vasCouponProduct = null;
        JedisClusterLock jedisLock = new JedisClusterLock();
        String lockKey = LOCK_PREFFIX + CouponFacadeImpl.class.getSimpleName() + "_doCouponDonation_" + couponId;
        if (!jedisLock.lock(lockKey, Constants.LOCK_EXPIRES,Constants.LOCK_WAITTIME)){
            logger.error("doCouponDonation, 卡券转赠获取锁超时, 用户标识: {}, 接收用户id: {}, 转赠的卡券id: {}",
                    regUser.getId(), acceptorId, couponId);
            return new ResponseEntity(ERROR,"卡券转赠超时，请重新操作！");
        }
        try{
            //判断卡券接收人是否实名
            RegUser acceptUser = userService.findRegUserById(acceptorId);
            if (acceptUser == null || acceptUser.getIdentify() == USER_IDENTIFY_NO){
                return new ResponseEntity(ERROR,"卡券接收人还未实名，无法转赠！");
            }
            //1.判断卡券是否有效
            vasCouponDetail = couponDetailService.findVasCouponDetailById(couponId);
            if (vasCouponDetail == null){
                return new ResponseEntity(ERROR,"卡券信息不存在，请您重新选择！");
            }
            vasCouponProduct = couponProductService.findVasCouponProductById(vasCouponDetail
                    .getCouponProductId());
            if (vasCouponProduct == null ||
                    (vasCouponProduct.getType() != VasCouponConstants.COUPON_PRODUCT_TYPE_INVEST_REDPACKET &&
                            vasCouponProduct.getType() != VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
                return new ResponseEntity(ERROR,"好友券或投资红包才能进行转赠！");
            }
            //好友券状态为1或4才允许转赠，投资红包状态为1才允许转赠，同时好友券和投资红包都需处在有效时间内被转赠
            if (vasCouponDetail.getState() != VasCouponConstants.COUPON_DETAIL_SEND_ALREADY &&
                    vasCouponDetail.getState() != VasCouponConstants.COUPON_DETAIL_DONATION_ONLY){
                return new ResponseEntity(ERROR,"不是有效的可转赠的卡券！");
            }
            //卡券已过期不能进行转赠
            if (vasCouponDetail.getEndTime().before(DateUtils.getFirstTimeOfDay())){
                return new ResponseEntity(ERROR,"卡券已过期，不能进行转赠！");
            }
            //2.判断卡券发送人，卡券是否有效，是否为好友券或投资红包
            if (!vasCouponDetail.getAcceptorUserId().equals(regUser.getId())){
                return new ResponseEntity(ERROR,"无法转赠不属于自己的卡券！");
            }
            //3.根据卡券的类型，判断卡券接收人，是否有好友券或投资红包
            RegUser receiveRegUser = userService.findRegUserById(acceptorId);
            if (receiveRegUser == null){
                return new ResponseEntity(ERROR,"卡券接收人信息不正确，请您重新选择！");
            }
            Map<String,Object> param = new HashMap<>(3);
            param.put("acceptorUserId",acceptorId);
            param.put("state", VasCouponConstants.COUPON_DETAIL_DONATION_ONLY);
            param.put("type",vasCouponProduct.getType());
            int count = couponDetailService.getUserCouponDetailListCount(param);
            if (count > 0){
                return new ResponseEntity(ERROR,"卡券接收人已拥有相同类型的卡券，请您选择其他用户！");
            }
            //生成卡券转赠记录
            VasCouponDonationRecord couponDonationRecord = new VasCouponDonationRecord();
            couponDonationRecord.setCouponDetailId(couponId);
            couponDonationRecord.setCreateTime(DateUtils.getCurrentDate());
            couponDonationRecord.setSendUserId(regUser.getId());
            couponDonationRecord.setReceiveUserId(acceptorId);
            //更新卡券信息
            couponDonationRecordService.insertVasCouponDonationRecord(couponDonationRecord);
            VasCouponDetail updateDetail = new VasCouponDetail(couponId);
            //重置接受人员
            updateDetail.setAcceptorUserId(acceptorId);
            updateDetail.setState(VasConstants.VAS_STATE_Y);
            //更新卡券详情id
            couponDetailService.updateVasCouponDetail(updateDetail);
        }catch (Exception e){
            logger.error("doCouponDonation, 卡券转赠异常, 用户标识: {}, 接收用户id: {}, 转赠的卡券信息: {}, 异常信息: ",
                    regUser.getId(), acceptorId, vasCouponDetail, e);
            throw new GeneralException("卡券转赠异常，请联系客服！");
        }finally {
            jedisLock.freeLock(lockKey);
        }

        try{
            //卡券信息
            StringBuilder couponDetailInfo = new StringBuilder(vasCouponDetail.getWorth().toString());

            if (Objects.equals(vasCouponProduct.getType(),VasCouponConstants.COUPON_PRODUCT_TYPE_INVEST_REDPACKET)){
                couponDetailInfo.append("元投资红包");
            }
            if (Objects.equals(vasCouponProduct.getType(),VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
                couponDetailInfo.append("%好友券");
            }
            //模糊的手机号
            String tel = regUser.getLogin().toString().substring(0,3) + "****" + regUser.getLogin().toString()
                    .substring(7);
            //替换消息模板的参数
            List<String> args = new ArrayList<>();
            args.add(regUser.getNickName());
            args.add(tel);
            args.add(couponDetailInfo.toString());
            //组装卡券接收人站内信
            SmsMsgInfo smsWebMsg = new SmsWebMsg(acceptorId,SmsMsgTemplate.MSG_DO_COUPON_DONATION_SUCCESS.getTitle(),
                    SmsMsgTemplate.MSG_DO_COUPON_DONATION_SUCCESS.getMsg(), SmsConstants.SMS_TYPE_NOTICE, args.toArray());
            //组装卡券接收人app消息推送
            SmsMsgInfo smsAppMsgPush = new SmsAppMsgPush(acceptorId,SmsMsgTemplate.MSG_DO_COUPON_DONATION_SUCCESS.getTitle(),
                    SmsMsgTemplate.MSG_DO_COUPON_DONATION_SUCCESS.getMsg(),args.toArray());
            ((SmsAppMsgPush) smsAppMsgPush).setType(SMS_APP_MSG_TYPE_COUPON);
            //发送站内信和消息推送
            SmsSendUtil.sendSmsMsgToQueue(smsWebMsg,smsAppMsgPush);
        }catch (Exception e){
            logger.error("doCouponDonation, 卡券转赠发送消息失败, 用户标识: {}, 接收用户id: {}, 转赠的卡券信息: {}, 异常信息: {}",
                    regUser.getId(), acceptorId, vasCouponDetail, e);
        }
        return new ResponseEntity(SUCCESS,"卡券转赠成功");
    }

    @Override
    public ResponseEntity getCouponDonationRecordList(int couponId) {
        VasCouponDonationRecord vasCouponDonationRecord = new VasCouponDonationRecord();
        vasCouponDonationRecord.setCouponDetailId(couponId);
        vasCouponDonationRecord.setSortColumns("create_time desc");
        List<VasCouponDonationRecord> resultList = couponDonationRecordService.findVasCouponDonationRecordList(vasCouponDonationRecord);
        List<VasCouponDonationRecordVO> dataList = new ArrayList<>(resultList.size());
        resultList.stream().forEach((record) -> {
            VasCouponDonationRecordVO data = new VasCouponDonationRecordVO();
            data.setCreateTime(record.getCreateTime());
            UserVO sendUser = userService.findRegUserTelAndRealNameById(record.getSendUserId());
            data.setSendUserName(sendUser.getRealName());
            data.setSendUserTel(sendUser.getLogin());
            UserVO receiveUser = userService.findRegUserTelAndRealNameById(record.getReceiveUserId());
            data.setReceiveUserTel(receiveUser.getLogin());
            data.setReceiveUserName(receiveUser.getRealName());
            dataList.add(data);
        });
        return new ResponseEntity(SUCCESS, dataList);
    }

    @Override
    public ResponseEntity getBidCouponDetailList(int regUserId, int bidId) {
        BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(bidId);
        if (bidInfoVO == null){
            return new ResponseEntity(ERROR,"标地信息异常！");
        }
        Map<String,Object> params = new HashMap<>(2);
        //加息券集合
        params.put("interestCouponList",new ArrayList<VasCouponDetailVO>());
        //投资红包集合
        params.put("redPacketsCouponList",new ArrayList<VasCouponDetailVO>());
        //如果该标支持使用加息券或投资红包
        if (bidInfoVO.getAllowCoupon() != InvestConstants.BID_ALLOW_COUPON_NO){
            //查询出该用户可用的加息券和投资红包
            Map<String,Object> couponInfo = couponDetailService.getUserInvestUsableCoupon(bidInfoVO.getProductType(),
                    regUserId);
            //根据标地设置的此标的是否允许使用卡券,过滤出使用的卡券
            if (bidInfoVO.getAllowCoupon() == InvestConstants.BID_ALLOW_COUPON_YES || bidInfoVO.getAllowCoupon() ==
                    InvestConstants.BID_ALLOW_COUPON_RAISE_INTEREST){
                params.put("interestCouponList",couponInfo.get("interestCouponList"));
            }
            if (bidInfoVO.getAllowCoupon() == InvestConstants.BID_ALLOW_COUPON_YES || bidInfoVO.getAllowCoupon() ==
                    InvestConstants.BID_ALLOW_COUPON_RED_PACKET){
                params.put("redPacketsCouponList",couponInfo.get("redPacketsCouponList"));
            }
        }
        ResponseEntity result = new ResponseEntity(SUCCESS,"查询成功！");
        result.setParams(params);
        return result;
    }

    @Override
    public void reissueUserCouponDetail(Integer regUserId, Integer couponProductId, String reason) {
        VasCouponProduct product = couponProductService.findVasCouponProductById(couponProductId);
        if (product == null){
            logger.error("generateUserCouponDetail, 给用户补发卡券信息, 卡券产品不存在, 接收用户id: {}, 卡券产品id: {}, 补发原因: {}, 异常信息: ",
                    regUserId, couponProductId, reason);
            return;
        }
        Date today = new Date();
        //判断卡券产品是否过期,若过期查找为过期天数的卡券产品
        if (product.getDeadlineType() == 1 && today.after(product.getEndTime())){
            VasCouponProduct condition = new VasCouponProduct();
            condition.setType(product.getType());
            condition.setState(VAS_STATE_Y);
            condition.setDeadlineType(COUPON_PRODUCT_DEADLINE_TYPE_DAYS);
            condition.setSortColumns("create_time");
            List<VasCouponProduct> couponProducts = couponProductService.findVasCouponProductList(condition);
            if (couponProducts.size() > 0){
                product = couponProducts.get(0);
            }else {
                List<Object> args = new ArrayList<>();
                args.add(userService.findRegUserById(regUserId).getLogin());
                args.add(product.getName());
                //给相关人员发送邮件，通知补发卡券失败，需要手动补发
                SmsSendUtil.sendEmailMsgToQueue(new SmsEmailMsg(regUserId,rosNoticeService.getEmailsByType
                        (ROS_NOTICE_REISSUE_COUPON),SmsMsgTemplate.MSG_REISSUE_COUPON_FAIL_NOTICE.getMsg(),
                        SmsConstants.SMS_TYPE_NOTICE,args.toArray()));
                return;
            }
        }
        VasCouponDetail detail = VasCouponDetailUtil.assembleCouponDetail(product, COUPON_DETAIL_SEND_ALREADY, VasCouponConstants
                .COUPON_DETAIL_SOURCE_REISSUE, reason, regUserId);
        couponDetailService.insertVasCouponDetail(detail);
    }
}
