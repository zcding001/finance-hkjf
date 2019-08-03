package com.hongkun.finance.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.dao.SmsWebMsgDao;
import com.hongkun.finance.sms.dao.SmsWebMsgDetailDao;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.model.SmsWebMsgDetail;
import com.hongkun.finance.sms.service.SmsWebMsgDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.SmsWebMsgDetailServiceImpl.java
 * @Class Name    : SmsWebMsgDetailServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SmsWebMsgDetailServiceImpl implements SmsWebMsgDetailService {

	private static final Logger logger = LoggerFactory.getLogger(SmsWebMsgDetailServiceImpl.class);
	
	/**
	 * SmsWebMsgDetailDAO
	 */
	@Autowired
	private SmsWebMsgDetailDao smsWebMsgDetailDao;
	@Autowired
	private SmsWebMsgDao smsWebMsgDao;
	
	@Override
	public SmsWebMsgDetail findSmsWebMsgDetailBySmsWebMsgId(int id) {
		return Optional.ofNullable(this.smsWebMsgDetailDao.findSmsWebMsgDetailBySmsWebMsgId(id)).orElse(new SmsWebMsgDetail());
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsgDetail(SmsWebMsgDetail smsWebMsgDetail) {
		this.smsWebMsgDetailDao.save(smsWebMsgDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsgDetailBatch(List<SmsWebMsgDetail> list) {
		this.smsWebMsgDetailDao.insertBatch(SmsWebMsgDetail.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsgDetailBatch(List<SmsWebMsgDetail> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.smsWebMsgDetailDao.insertBatch(SmsWebMsgDetail.class, list, count);
	}
	
	@Override
	public SmsWebMsgDetail findSmsWebMsgDetailById(int id) {
		SmsWebMsgDetail obj = new SmsWebMsgDetail();
		obj.setSmsWebMsgId(id);
		List<SmsWebMsgDetail> list = this.smsWebMsgDetailDao.findByCondition(obj);
		if(list != null && !list.isEmpty()) {
			SmsWebMsgDetail smsWebMsgDetial = list.get(0);
			SmsWebMsg smsWebMsg = new SmsWebMsg();
			smsWebMsg.setId(smsWebMsgDetial.getSmsWebMsgId());
			smsWebMsg.setState(1);
			this.smsWebMsgDao.update(smsWebMsg);
			return smsWebMsgDetial;
		}
		return new SmsWebMsgDetail();
	}
}
