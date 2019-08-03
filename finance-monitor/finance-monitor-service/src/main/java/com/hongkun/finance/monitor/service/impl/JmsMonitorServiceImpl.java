package com.hongkun.finance.monitor.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.monitor.service.JmsMonitorService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.jms.polling.JmsFailMsg;
import com.yirun.framework.jms.polling.RecoverJmsFailMsgI;
import com.yirun.framework.jms.utils.JmsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JmsMonitorServiceImpl implements JmsMonitorService {

	private RecoverJmsFailMsgI recoverJmsFailMsgI;
	
	public JmsMonitorServiceImpl() {
		recoverJmsFailMsgI = JmsUtils.getRecoverJmsFailMsgI();
	}

	@Override
	public ResponseEntity<?> findJmsFailMsgList(JmsFailMsg jmsFailMsg) {
		List<JmsFailMsg> result = new ArrayList<>();
		List<JmsFailMsg> list = this.recoverJmsFailMsgI.findAllJmsFailMsg();
		if(CommonUtils.isNotEmpty(list)) {
			list.forEach(e -> {
				boolean flag = true;
				//通过消息类型进行过滤
				if(jmsFailMsg.getType() != null && jmsFailMsg.getType() != -999 && !e.getType().equals(jmsFailMsg.getType())) {
					flag = false;
				}
				if(StringUtils.isNotBlank(jmsFailMsg.getDestinationName()) && !e.getDestinationName().toLowerCase().contains(jmsFailMsg.getDestinationName().toLowerCase())) {
					flag = false;
				}
				if(flag) {
					String msg = ToStringBuilder.reflectionToString(e.getMsg(), ToStringStyle.DEFAULT_STYLE);
					e.setMsg(msg);
					result.add(e);
				}
			});
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	@Override
	public ResponseEntity<?> recoverJmsFailMsg(String codes) {
		return this.recoverJmsFailMsgI.recoverJmsFailMsg(Arrays.asList(codes.split(",")));
	}

    @Override
    public ResponseEntity<?> delJmsFailMsg(String codes) {
        return recoverJmsFailMsgI.delJmsFailMsg(Arrays.asList(codes.split(",")));
    }
}
