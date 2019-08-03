package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hongkun.finance.vas.constants.VasConstants.REDPACKET_SOURCE_PERSONAL;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 前台红包处理controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.vas.RedPacketController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/redPacketController")
public class RedPacketController {

    @Reference
    private VasRedpacketInfoService vasRedpacketInfoService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private VasRedpacketFacade vasRedpacketFacade;

    /**
     * @param pager  分页参数(必填)
     * @return : ResponseEntity
     * @Description : 获取当前用户已领取的红包记录
     * @Method_Name : getUserRedPacketInfo
     * @Creation Date  : 2017年12月18日 上午09:14:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserRedPacketInfo")
    @ResponseBody
    public ResponseEntity getUserRedPacketInfo(Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        return vasRedpacketInfoService.getUserRedPacketInfo(regUser.getId(),pager);
    }
    /**
     * @param pager  分页参数
     * @return : ResponseEntity
     * @Description : 获取用户已发送红包记录
     * @Method_Name : getUserSendOutRedPacketInfo
     * @Creation Date  : 2017年01月11日 上午09:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/getUserSendOutRedPacketInfo")
    @ResponseBody
    public ResponseEntity getUserSendOutRedPacketInfo(Pager pager){
        RegUser regUser = BaseUtil.getLoginUser();
        return vasRedpacketFacade.getUserSendOutRedPacketInfo(regUser.getId(),pager);
    }
    /**
     * @param key  红包兑换码
     * @return : ResponseEntity
     * @Description : 用户兑换红包
     * @Method_Name : exchangeRedPacketInfo
     * @Creation Date  : 2017年12月18日 下午14:30:50
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("/exchangeRedPacketInfo")
    @ResponseBody
    public ResponseEntity exchangeRedPacketInfo(@RequestParam("key") String key){
        if (StringUtils.isBlank(key)){
            return new ResponseEntity(ERROR,"红包兑换码不能为空！");
        }
        RegUser regUser = BaseUtil.getLoginUser();
        return vasRedpacketFacade.exchangeRedPacketInfo(regUser.getId(),key, PlatformSourceEnums.PC);
    }

    /**
     * 给用户派发红包
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/distributeRedPacketToUser")
    @ResponseBody
    @Token
    @ActionLog(msg = "个人给用户派发红包, 红包金额: {args[0].value}, 截止日期: {args[0].endTime}, 用户ids: {args[0].userIds}")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_AUTHORITY)
    public ResponseEntity distributeRedPacketToUser(@Validated({RedpacketDTO.DistributeRedPacketToUser.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        return vasRedpacketFacade.distributeRedpacket(redpacketDTO,REDPACKET_SOURCE_PERSONAL, PlatformSourceEnums.PC);
    }
}
