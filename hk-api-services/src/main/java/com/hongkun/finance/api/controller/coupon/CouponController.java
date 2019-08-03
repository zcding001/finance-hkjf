package com.hongkun.finance.api.controller.coupon;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 用户福利-卡券controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.coupon.CouponController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/couponController")
public class CouponController {

    @Reference
    private VasCouponDetailService couponDetailService;

    @Reference
    private CouponFacade couponFacade;

    @Reference
    private DicDataService dicDataService;

    /**
     * @param state  获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(必填);包含未到使用时间卡券信息
     * @param pager {currentPage   当前页码
     *                  pageSize}      每页条数
     * @return : Map<String,Object>
     * @Description : 获取用户卡券记录
     * @Method_Name : getCouponDetailList
     * @Creation Date  : 2018年03月08日 下午18:04:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getCouponDetailList")
    @ResponseBody
    public Map<String,Object> getCouponDetailList(int state,Pager pager){
        pager.setInfiniteMode(true);
        RegUser regUser = BaseUtil.getLoginUser();
        Map<String,Object> param = new HashMap<>(3);
        param.put("acceptorUserId",regUser.getId());
        param.put("state",state);
        param.put("pager",pager);
        List<VasCouponDetailVO> result = couponDetailService.getUserCouponDetailList(param);

        return processVasCouponDetailVOList(result);
    }

    private Map<String,Object> processVasCouponDetailVOList(List<VasCouponDetailVO> list){
        return AppResultUtil.successOfListInProperties(list,"查询成功","endTime","type","state","worth","amountMin",
                "id","bidProductTypeRange").processObjInList((orderMap)->{
            orderMap.put("typeName",dicDataService.findNameByValue(VasConstants.VAS,
                    VasCouponConstants.COUPON_PRODUCT_TYPE, (int)orderMap.get("type")));
            String bidProductTypeRange = orderMap.get("bidProductTypeRange").toString();
            StringBuilder bidProductTypeRangeText = new StringBuilder("");
            if (StringUtils.isNotBlank(bidProductTypeRange)){
                String[] typeArr = bidProductTypeRange.split(",");
                for (String type:typeArr){
                    if (StringUtils.isBlank(bidProductTypeRangeText)){
                        bidProductTypeRangeText.append(dicDataService.findNameByValue("invest","product_type",
                                Integer.valueOf(type)));
                    }else {
                        bidProductTypeRangeText.append(",").append(dicDataService
                                .findNameByValue("invest","product_type", Integer.valueOf(type)));
                    }
                }
            }
            orderMap.put("bidProductTypeRangeText",bidProductTypeRangeText.toString());
            orderMap.put("amountMinWy", (BigDecimal.valueOf(Double.valueOf(orderMap.get("amountMin").toString())))
                    .divide(BigDecimal.valueOf(10000)));
        });
    }
    /**
     * @param key           劵卡key
     * @return : Map<String,Object>
     * @Description : 劵卡激活
     * @Method_Name : activeCouponDetail
     * @Creation Date  : 2018年03月09日 上午09:39:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/activeCouponDetail")
    @ResponseBody
    @ActionLog(msg = "卡券激活, 卡券key: {args[0]}")
    public Map<String,Object> activeCouponDetail(String key){
        if (StringUtils.isBlank(key)){
            return AppResultUtil.errorOfMsg("卡券激活码不能为空！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = couponDetailService.activeCouponDetail(regUser.getId(), key);
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @param couponDetailId           卡券id
     * @param recipientId              卡券接收人id
     * @return : Map<String,Object>
     * @Description : 卡券转赠
     * @Method_Name : donationCouponDetail
     * @Creation Date  : 2018年03月09日 上午09:51:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/donationCouponDetail")
    @ResponseBody
    @ActionLog(msg = "卡券转赠, 卡券id: {args[0]}, 接收人id: {args[1]}")
    public Map<String,Object> donationCouponDetail(int couponDetailId, int recipientId){
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = couponFacade.doCouponDonation(regUser, recipientId, couponDetailId);
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @return : Map<String,Object>
     * @Description : 获取用户可用提现券数量
     * @Method_Name : getCouponWithdrawCount
     * @Creation Date  : 2018年03月09日 上午10:17:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getCouponWithdrawCount")
    @ResponseBody
    public Map<String,Object> getCouponWithdrawCount(){
        RegUser regUser = BaseUtil.getLoginUser();
        int count = couponDetailService.getUserWithdrawUsableCoupon(regUser.getId()).size();
        return AppResultUtil.successOfMsg("查询成功").addResParameter("count",count);
    }

    /**
     * @param bidId           标地id
     * @return : Map<String,Object>
     * @Description : 获取用户某标的可用卡券
     * @Method_Name : getBidCouponDetailList
     * @Creation Date  : 2018年03月09日 上午10:49:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getBidCouponDetailList")
    @ResponseBody
    public Map<String,Object> getBidCouponDetailList(int bidId){
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = couponFacade.getBidCouponDetailList(regUser.getId(),bidId);
        //如果获取失败则返回异常信息
        if (result.getResStatus() == ERROR){
            return AppResultUtil.errorOfMsg(result.getResMsg().toString());
        }
        //获取成功处理返回的参数
        Map<String,Object> data = result.getParams();
        List<VasCouponDetailVO> interestCouponList = (List<VasCouponDetailVO>) data.get("interestCouponList");
        List<VasCouponDetailVO> redPacketsCouponList = (List<VasCouponDetailVO>) data.get("redPacketsCouponList");

        return AppResultUtil.statusOfMsg(SUCCESS,"查询成功").addResParameter("interestCouponList",
                processVasCouponDetailVOList(interestCouponList).get("dataList")).
                addResParameter("redPacketsCouponList",processVasCouponDetailVOList(redPacketsCouponList).
                        get("dataList"));
    }

}
