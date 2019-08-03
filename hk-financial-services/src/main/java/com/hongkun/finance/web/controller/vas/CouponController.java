package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.model.ResponseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理前台关于卡券相关的的业务逻辑
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.vas.CouponController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/couponController")
public class CouponController {

    @Reference
    private CouponFacade couponFacade;
    @Reference
    private VasCouponDetailService couponDetailService;

    /**
     * 转赠卡券
     * @param acceptorId   卡券接收人
     * @param couponId     卡券id
     * @return : ResponseEntity
     * @Description : 获取用户的卡券详情列表
     * @Method_Name : couponDonation
     * @Creation Date  : 2017年12月27日 下午16:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/couponDonation")
    @ResponseBody
    public ResponseEntity couponDonation(@RequestParam int acceptorId, @RequestParam int couponId){
        RegUser regUser =  BaseUtil.getLoginUser();
        return couponFacade.doCouponDonation(regUser,acceptorId,couponId);
    }

    /**
     * @param state  获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(必填)
     * @param type   获取用户对应的卡券产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券(非必填)}
     * @return : ResponseEntity
     * @Description : 获取用户的卡券详情列表
     * @Method_Name : getUserCouponDetailList
     * @Creation Date  : 2017年12月13日 下午05:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserCouponDetailList")
    @ResponseBody
    public ResponseEntity getUserCouponDetailList(Integer state,
                                                  @RequestParam(required = false) Integer type){
        RegUser regUser =  BaseUtil.getLoginUser();
        Map<String,Object> param = new HashMap<>(3);
        param.put("acceptorUserId",regUser.getId());
        param.put("state",state);
        param.put("type",type);
        List<VasCouponDetailVO> result = couponDetailService.getUserCouponDetailList(param);
        return new ResponseEntity(SUCCESS, result);
    }

    /**
     * @param avtKey  卡券激活码(必填)
     * @return : ResponseEntity
     * @Description : 激活卡券
     * @Method_Name : activeCouponDetail
     * @Creation Date  : 2017年12月15日 上午09:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/activeCouponDetail")
    @ResponseBody
    public ResponseEntity activeCouponDetail(String avtKey){
        if (StringUtils.isBlank(avtKey)){
            return new ResponseEntity(ERROR,"卡券激活码不能为空！");
        }
        RegUser regUser =  BaseUtil.getLoginUser();
        return couponDetailService.activeCouponDetail(regUser.getId(),avtKey);
    }
}
