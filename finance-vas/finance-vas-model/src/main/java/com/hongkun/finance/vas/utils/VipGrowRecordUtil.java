package com.hongkun.finance.vas.utils;

import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description : 异步插入成长值工具类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.utils.VipGrowRecordUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VipGrowRecordUtil {

    private static final Logger logger = LoggerFactory.getLogger(VipGrowRecordUtil.class);

    private static JmsService jmsService;

    static {
        jmsService = ApplicationContextUtils.getBean("jmsService");
    }

    private VipGrowRecordUtil(){}

    public static void sendVipGrowRecordToQueue(VasVipGrowRecordMqVO recordMqVO){
        if (recordMqVO == null){
            logger.error("发送至MQ的成长值记录不能为空！");
            return;
        }
        try{
            jmsService.sendMsg(VasVipConstants.MQ_QUEUE_VAS_VIP_GROW_RECORD, DestinationType.QUEUE,recordMqVO, JmsMessageType.OBJECT);
        }catch (Exception e){
            logger.error("成长值记录：{}，发送至MQ异常，异常信息：",recordMqVO.toString(), e);
        }
    }

    public static void sendVipGrowRecordListToQueue(List<VasVipGrowRecordMqVO> list){
        if (list == null || list.size() == 0){
            logger.error("发送至MQ的成长值记录集合不能为空！");
            return;
        }
        try{
            jmsService.sendMsg(VasVipConstants.MQ_QUEUE_VAS_VIP_GROW_RECORD, DestinationType.QUEUE,list, JmsMessageType.OBJECT);
        }catch (Exception e){
            logger.error("成长值记录集合：{}，发送至MQ异常，异常信息：", JsonUtils.toJson(list), e);
        }
    }

}
