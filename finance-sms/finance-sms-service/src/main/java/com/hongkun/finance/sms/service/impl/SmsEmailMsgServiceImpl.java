package com.hongkun.finance.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.dao.SmsEmailMsgDao;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.service.SmsEmailMsgService;
import com.hongkun.finance.sms.utils.MailSenderUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.SmsEmailMsgServiceImpl.java
 * @Class Name    : SmsEmailMsgServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SmsEmailMsgServiceImpl implements SmsEmailMsgService {

	private static final Logger logger = LoggerFactory.getLogger(SmsEmailMsgServiceImpl.class);
	
	/**
	 * SmsEmailMsgDAO
	 */
	@Autowired
	private SmsEmailMsgDao smsEmailMsgDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsEmailMsg(SmsEmailMsg smsEmailMsg) {
//		发送邮件
		boolean success = MailSenderUtil.send(
				smsEmailMsg.getEmail(), 
				smsEmailMsg.getTitle(), 
				smsEmailMsg.getMsg());
//		初始化用于持久化的对象
		smsEmailMsg.setState(success ? 1 : 0);
		String emails = smsEmailMsg.getEmail();
		String[] arr = emails.split(",");
		List<SmsEmailMsg> emailList = new ArrayList<>();
		for(String e : arr){
            SmsEmailMsg dest = new SmsEmailMsg();
            BeanUtils.copyProperties(smsEmailMsg, dest);
            dest.setEmail(e.trim());
            emailList.add(dest);
        }
		if(CommonUtils.isNotEmpty(emailList)){
            this.smsEmailMsgDao.insertBatch(SmsEmailMsg.class, emailList, emailList.size());
        }
		return new ResponseEntity<>(success ? SUCCESS : ERROR, success ? "邮件已发送" : "邮件发送失败");
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> insertSmsEmailMsgBatch(List<SmsEmailMsg> list, int count) {
		List<SmsEmailMsg> errList = new ArrayList<>();
		if(list != null && !list.isEmpty()){
			for(SmsEmailMsg smsEmailMsg : list){
//				发送邮件
				boolean success = MailSenderUtil.send(
						smsEmailMsg.getEmail(), 
						smsEmailMsg.getTitle(), 
						smsEmailMsg.getMsg());
//				初始化用于持久化的对象
				smsEmailMsg.setState(success ? 1 : 0);
				if(!success){
					errList.add(smsEmailMsg);
				}
			}
		}
		this.smsEmailMsgDao.insertBatch(SmsEmailMsg.class, list, count);
		logger.info("fail send email count {}", errList.size());
		return new ResponseEntity<>(SUCCESS, errList);
	}
}
