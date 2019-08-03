package com.hongkun.finance.api.controller.redpacket;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description : 红包相关Controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.redpacket.RedPacketController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/redPacketController")
public class RedPacketController {

    @Reference
    private VasRedpacketInfoService redpacketInfoService;

    @Reference
    private VasRedpacketFacade redpacketFacade;

    /**
     * @param state  获取的红包状态：0-未领取，1-已领取，2-已过期
     * @param pager {currentPage   当前页码
     *               pageSize}      每页条数
     * @return : Map<String,Object>
     * @Description : 获取用户红包记录
     * @Method_Name : getRedPacketInfoList
     * @Creation Date  : 2018年03月09日 下午14:25:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getRedPacketInfoList")
    @ResponseBody
    public Map<String,Object> getRedPacketInfoList(int state, Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = redpacketInfoService.getRedPacketInfoList(regUser.getId(),state,pager);
        return AppResultUtil.successOfListInProperties((List<VasRedpacketInfo>)result.getResMsg(),"查询成功"
                ,"value","endTime","state","key");
    }

    /**
     * @param key    红包key
     * @param typeValue    11-IOS,12-ANDROID
     * @return : Map<String,Object>
     * @Description : 激活红包
     * @Method_Name : exchangeRedPacketInfo
     * @Creation Date  : 2018年03月09日 下午14:49:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/exchangeRedPacketInfo")
    @ResponseBody
    @ActionLog(msg = "用户兑换红包, 红包兑换码: {args[0]}, 兑换来源: {args[1]}")
    public Map<String,Object> exchangeRedPacketInfo(String key,int typeValue){
        if (StringUtils.isBlank(key)){
            AppResultUtil.errorOfMsg("红包兑换码不能为空！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        ResponseEntity result = redpacketFacade.exchangeRedPacketInfo(regUser.getId(),key, PlatformSourceEnums
                .typeByValue(typeValue));
        return AppResultUtil.mapOfResponseEntity(result);
    }
}
