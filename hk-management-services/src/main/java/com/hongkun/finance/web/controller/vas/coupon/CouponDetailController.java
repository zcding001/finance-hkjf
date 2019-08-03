package com.hongkun.finance.web.controller.vas.coupon;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.service.RosNoticeService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.dto.DistributeCouponDTO;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasCouponDonationRecordService;
import com.hongkun.finance.vas.service.VasCouponProductService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hongkun.finance.invest.constants.InvestConstants.BID_PRODUCT;
import static com.hongkun.finance.invest.constants.InvestConstants.INVEST;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_DETAIL_STATE;
import static com.hongkun.finance.vas.constants.VasCouponConstants.COUPON_PRODUCT_TYPE;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 卡券详情处理controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.vas.coupon.CouponDetailController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/couponDetailController")
public class CouponDetailController {

    @Reference
    private DicDataService dicDataService;
    @Reference
    private VasCouponDetailService couponDetailService;
    @Reference
    private CouponFacade couponFacade;
    @Reference
    private RegUserService userService;
    @Reference
    private VasCouponProductService couponProductService;
    @Reference
    private VasCouponDonationRecordService couponDonationRecordService;
    @Reference
    private RosNoticeService rosNoticeService;
    /***
     * 去往卡券详情查看页的准备数据（下拉框的准备数据）
     * @return
     */
    @RequestMapping("/toCouponDetailSearch")
    @ResponseBody
    public ResponseEntity toCouponDetailSearch() {
        ResponseEntity prepareData = new ResponseEntity(SUCCESS);
        Arrays.asList(
                COUPON_PRODUCT_TYPE/*卡券类型*/,
                COUPON_DETAIL_STATE/*状态*/)
                .stream()
                .forEach((e) -> prepareData.getParams().put(e, dicDataService.findDicDataBySubjectName(VasConstants.VAS, e)));
        //卡券使用范围：标的类型
        prepareData.getParams().put(BID_PRODUCT, dicDataService.findDicDataBySubjectName(INVEST, BID_PRODUCT));
        return prepareData;
    }
    /***
     * 搜索卡券详情信息(移入卡券详情中心)
     * @return
     */
    @RequestMapping("/couponDetailList")
    @ResponseBody
    public ResponseEntity couponDetailList(VasCouponDetailVO vasCouponDetailVO,Pager pager) {
        vasCouponDetailVO.setSortColumns("vcd.create_time desc");
        return new ResponseEntity(SUCCESS, couponFacade.couponDetailSearch(vasCouponDetailVO,pager));
    }
    /***
     * 根据条件查询派发卡券的候选人
     * @param userVO
     * @param pager
     * @return
     */
    @RequestMapping("/listCouponCandidate")
    @ResponseBody
    public ResponseEntity listCouponCandidate(UserVO userVO, Pager pager) {
        //执行查询
        Pager resultPage = this.userService.listConditionPage(userVO, pager);
        if (BaseUtil.resultPageHasNoData(resultPage)) {
            return new ResponseEntity(201, "没有任何数据");
        }
        resultPage.getData().stream().forEach((e) -> {
            UserVO u = (UserVO) e;
            //置空身份证号
            u.setIdCard(null);
            //置空邀请码
            u.setInviteNo(null);

        });
        //返回
        return new ResponseEntity(SUCCESS, resultPage);
    }
    /**
     * @param distributeCouponDTO
     * @return : ResponseEntity
     * @Description : 给用户赠送卡券
     * @Method_Name : distributeCouponToUser
     * @Creation Date  : 2017年11月06日 下午02:24:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/distributeCouponToUser")
    @ResponseBody
    @ActionLog(msg = "给用户派发卡券")
    public ResponseEntity distributeCouponToUser(@Validated DistributeCouponDTO distributeCouponDTO) {
        if (this.couponDetailService.distributeCouponToUser(distributeCouponDTO.getUserIds(), distributeCouponDTO.getCouponIds(),
                distributeCouponDTO.getReason())) {
            //成功的话通知用户去看卡券
            notifyUserGetCouponDetail(distributeCouponDTO.getUserIds(),distributeCouponDTO.getCouponIds());
            return new ResponseEntity(SUCCESS, "派发成功");
        }

        return new ResponseEntity(ERROR, "派发失败");
    }
    /**
     * 通知用户领取卡券
     *
     * @param
     */
    private void notifyUserGetCouponDetail(List<Integer> getUserIds, List<Integer> couponProductIds) {
        getUserIds.forEach((msgGetUserId) -> couponProductIds.forEach(cId->{
            VasCouponProduct product = couponProductService.findVasCouponProductById(cId);
            StringBuilder couponInfo = new StringBuilder();
            //组装站内信卡券信息
            if (product.getWorth().compareTo(BigDecimal.ZERO) == 1){
                couponInfo.append(product.getWorth());
            }
            //发送
            String email = "";
            if (product.getType().equals(VasCouponConstants.COUPON_PRODUCT_TYPE_INVEST_REDPACKET)){
                couponInfo.append("元");
                email = rosNoticeService.getEmailsByType(RosterConstants.ROS_NOTICE_INVEST_REDPCAK);
            }
            if (product.getType().equals(VasCouponConstants.COUPON_PRODUCT_TYPE_RATE_COUPON) ||
                    product.getType().equals(VasCouponConstants.COUPON_PRODUCT_TYPE_FRIENDS)){
                couponInfo.append("%");
                email = rosNoticeService.getEmailsByType(RosterConstants.ROS_NOTICE_TYPE_RATE_COUPON);
            }
            couponInfo.append(dicDataService.findNameByValue(VasConstants.VAS, VasCouponConstants.COUPON_PRODUCT_TYPE,
                    product.getType()));
            String msgReadyToUse = String.format(SmsMsgTemplate.MSG_DISTRIBUTE_COUPON.getMsg(), couponInfo.toString());
            //发送站内信
            SmsSendUtil.sendSmsMsgToQueue(new SmsWebMsg(msgGetUserId, SmsMsgTemplate.MSG_DISTRIBUTE_COUPON.getTitle(),
                    msgReadyToUse, 1));

            //发送邮件通知信息
            UserVO userVO = userService.findRegUserTelAndRealNameById(msgGetUserId);
            List<Object> args = new ArrayList<>();
            args.add(userVO.getLogin());
            args.add(userVO.getRealName());
            args.add(couponInfo.toString());
            SmsSendUtil.sendEmailMsgToQueue(new SmsEmailMsg(msgGetUserId,email,SmsMsgTemplate.MSG_DISTRIBUTE_COUPON_NOTICE.getTitle(),
                    SmsMsgTemplate.MSG_DISTRIBUTE_COUPON_NOTICE.getMsg(), SmsConstants.SMS_TYPE_NOTICE,args.toArray()));
        }));
    }
    /**
     * @param couponId
     * @param num
     * @return : ResponseEntity
     * @Description : 生成卡券数据
     * @Method_Name : generateCouponDetailList
     * @Creation Date  : 2017年07月07日 下午17:24:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/generateCouponDetailList")
    @ResponseBody
    @ActionLog(msg = "生成线下卡券信息, 卡券产品id: {args[0]}, 生成卡券数量: {args[1]}")
    public ResponseEntity generateCouponDetailList(@RequestParam(value = "couponId",required = false) Integer couponId,
                                                @RequestParam(value = "num",required = false) Integer num) {
        return couponDetailService.generateCouponDetailList(couponId,num);
    }
    /**
     * @param couponId  卡券详情id
     * @return : ResponseEntity
     * @Description : 查看卡券详情的转赠记录
     * @Method_Name : couponDonationRecordList
     * @Creation Date  : 2017年11月09日 下午14:24:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/couponDonationRecordList")
    @ResponseBody
    public ResponseEntity couponDonationRecordList(@RequestParam("couponDetailId") int couponId) {
        return couponFacade.getCouponDonationRecordList(couponId);
    }

}
