package com.hongkun.finance.web.controller.monitor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.monitor.service.JmsMonitorService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.polling.JmsFailMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description   : jms异常消息监控回复控制层
 * @Project       : hk-management-services
 * @Program Name  : com.hongkun.finance.web.controller.monitor.JmsMonitorController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/jmsMonitorController/")
public class JmsMonitorController{

	@Reference
	private JmsMonitorService jmsMonitorService;
	
	/**
	 *  @Description    : 检索异常的jms消息
	 *  @Method_Name    : jmsFailMsgList
	 *  @param pager
	 *  @param jmsFailMsg
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月21日 下午3:34:08 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("jmsFailMsgList")
	@ResponseBody
	public ResponseEntity<?> jmsFailMsgList(Pager pager, JmsFailMsg jmsFailMsg){
		ResponseEntity<?> result = jmsMonitorService.findJmsFailMsgList(jmsFailMsg);
		List<?> list = (List<?>) result.getResMsg();
		pager.setData(list);
		pager.setTotalRows(list.size());
		return new ResponseEntity<>(Constants.SUCCESS, pager);
	}

	/**
	 *  @Description    : 回复异常的jms消息
	 *  @Method_Name    : recover
	 *  @param codes
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月21日 下午3:34:29 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("recover")
	@ResponseBody
	public ResponseEntity<?> recover(String codes){
		//校验code合法性，防止SQL注入
		if(!StringUtilsExtend.isUUID(codes)) {
			return new ResponseEntity<>(Constants.ERROR, "[" + codes + "]中含有非法数据.");
		}
		return jmsMonitorService.recoverJmsFailMsg(codes);
	}

    /**
    *  删除jms异常消息
    *  @Method_Name             ：del
    *  @param codes
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/5/30
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    @RequestMapping("del")
    @ResponseBody
    public ResponseEntity<?> del(String codes){
        //校验code合法性，防止SQL注入
        if(!StringUtilsExtend.isUUID(codes)) {
            return new ResponseEntity<>(Constants.ERROR, "[" + codes + "]中含有非法数据.");
        }
        return jmsMonitorService.delJmsFailMsg(codes);
    }
}
