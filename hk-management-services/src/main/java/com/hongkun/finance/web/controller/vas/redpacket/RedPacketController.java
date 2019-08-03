package com.hongkun.finance.web.controller.vas.redpacket;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hongkun.finance.vas.constants.VasConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Description : 处理红包模块的控制器
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.redpacket.RedPacketController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("/redpacketController")
public class RedPacketController {
    private static final Logger logger = LoggerFactory.getLogger(RedPacketController.class);
    @Reference
    private VasRedpacketFacade vasRedpacketFacade;

    @Reference
    private VasRedpacketInfoService vasRedpacketInfoService;

    @Value("${packetInfo}")
    private String packetInfo;
    /**
     * 获取红包分页数据的代码
     *
     * @param pager
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity listPage(VasRedpacketVO vasRedpacketVO, Pager pager) {
        //过滤不合法的状态
        if (vasRedpacketVO.getType()!=null&&vasRedpacketVO.getType()<0) {
            vasRedpacketVO.setType(null);
        }
        return vasRedpacketFacade.listPage(vasRedpacketVO, pager);
    }


    /**
     * 生成线下红包
     *
     * @param redpacketDTO 红包数据生成类
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/produceRedpacket")
    @ResponseBody
    @Token
    @ActionLog(msg = "生成线下红包, 红包数量: {args[0].num}, 红包单个金额: {args[0].value}, 截止日期: {args[0].endTime}")
    public ResponseEntity produceRedpacket(@Validated({RedpacketDTO.ProduceRedpacket.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        /**
         step 0:检查用户红包金额，单个金额不能超过50元
         */
        if (MAX_OFFLINE_REDPACKET_VALUE.compareTo(redpacketDTO.getValue())<0) {
            return new ResponseEntity(ERROR, "单个红包金额不能超过" + MAX_OFFLINE_REDPACKET_VALUE);
        }
        return vasRedpacketFacade.distributeRedpacket(redpacketDTO,REDPACKET_SOURCE_OFFLINE, PlatformSourceEnums.PC);
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
    @ActionLog(msg = "给用户派发红包, 红包金额: {args[0].value}, 截止日期: {args[0].endTime}, 用户ids: {args[0].userIds}")
    public ResponseEntity distributeRedPacketToUser(@Validated({RedpacketDTO.DistributeRedPacketToUser.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        return  vasRedpacketFacade.distributeRedpacket(redpacketDTO,REDPACKET_SOURCE_DISTRIBUTE, PlatformSourceEnums.PC);
    }


    /**
     * 审核红包
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/checkRedPacket")
    @ResponseBody
    @Token
    @ActionLog(msg = "审核红包, 审核id: {args[0].checkId}, 审核结果: {args[0].state}")
    public ResponseEntity checkRedPacket(@Validated({RedpacketDTO.CheckRedPacket.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        redpacketDTO.setPacketInfo(packetInfo);
        return vasRedpacketFacade.changeRedPacketState(redpacketDTO,PlatformSourceEnums.PC);
    }

    /**
     * 失效红包
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/invalidRedPacket")
    @ResponseBody
    @Token
    @ActionLog(msg = "失效红包, 红包ids: {args[0].redPacketIds}")
    public ResponseEntity invalidRedPacket(@Validated({RedpacketDTO.InvalidRedPacket.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        //设置失效状态
        redpacketDTO.setState(VasConstants.REDPACKET_STATE_FAILED);
        return this.vasRedpacketFacade.changeRedPacketState(redpacketDTO,PlatformSourceEnums.PC);
    }


    /**
     * 删除红包
     *
     * @return
     * @Author : zhongpingtang@yiruntz.com 唐忠平
     */
    @RequestMapping("/deleteRedPacket")
    @ResponseBody
    @Token
    @ActionLog(msg = "删除红包, 红包ids: {args[0].redPacketIds}")
    public ResponseEntity deleteRedPacket(@Validated({RedpacketDTO.InvalidRedPacket.class}) RedpacketDTO redpacketDTO) {
        BeanPropertiesUtil.mergeProperties(redpacketDTO,BaseUtil.getLoginUser());
        //设置失效状态
        redpacketDTO.setState(VasConstants.REDPACKET_STATE_DELETED);
        return this.vasRedpacketInfoService.deleteRedPacket(redpacketDTO);
    }

}
