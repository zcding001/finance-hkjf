package com.hongkun.finance.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.sms.dao.SmsWebMsgDao;
import com.hongkun.finance.sms.dao.SmsWebMsgDetailDao;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.model.SmsWebMsgDetail;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.SmsWebMsgServiceImpl.java
 * @Class Name    : SmsWebMsgServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SmsWebMsgServiceImpl implements SmsWebMsgService {

	private static final Logger logger = LoggerFactory.getLogger(SmsWebMsgServiceImpl.class);
	
	/**
	 * SmsWebMsgDAO
	 */
	@Autowired
	private SmsWebMsgDao smsWebMsgDao;
	@Autowired
	private SmsWebMsgDetailDao smsWebMsgDetailDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsg(SmsWebMsg smsWebMsg) {
		this.smsWebMsgDao.save(smsWebMsg);
		SmsWebMsgDetail smsWebMsgDetail = new SmsWebMsgDetail();
		smsWebMsgDetail.setMsg(smsWebMsg.getMsg());
		smsWebMsgDetail.setSmsWebMsgId(smsWebMsg.getId());
		this.smsWebMsgDetailDao.save(smsWebMsgDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsgBatch(List<SmsWebMsg> list) {
		if(list != null && !list.isEmpty()) {
			this.smsWebMsgDao.insertBatch(SmsWebMsg.class, list);
			this.smsWebMsgDetailDao.insertBatch(SmsWebMsgDetail.class, list.stream().map(o -> {
				SmsWebMsgDetail smsWebMsgDetail = new SmsWebMsgDetail();
				smsWebMsgDetail.setMsg(o.getMsg());
				smsWebMsgDetail.setSmsWebMsgId(o.getId());
				return smsWebMsgDetail;
			}).collect(Collectors.toList()));
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSmsWebMsgBatch(List<SmsWebMsg> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.smsWebMsgDao.insertBatch(SmsWebMsg.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateSmsWebMsg(SmsWebMsg smsWebMsg) {
		return this.smsWebMsgDao.update(smsWebMsg);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateSmsWebMsgBatch(List<SmsWebMsg> list, int count) {
		this.smsWebMsgDao.updateBatch(SmsWebMsg.class, list, count);
	}
	
	@Override
	public SmsWebMsg findSmsWebMsgById(int id) {
		return this.smsWebMsgDao.findByPK(Long.valueOf(id), SmsWebMsg.class);
	}
	
	@Override
	public List<SmsWebMsg> findSmsWebMsgList(SmsWebMsg smsWebMsg) {
		return this.smsWebMsgDao.findByCondition(smsWebMsg);
	}
	
	@Override
	public List<SmsWebMsg> findSmsWebMsgList(SmsWebMsg smsWebMsg, int start, int limit) {
		return this.smsWebMsgDao.findByCondition(smsWebMsg, start, limit);
	}
	
	@Override
	public Pager findSmsWebMsgList(SmsWebMsg smsWebMsg, Pager pager) {
	    smsWebMsg.setSortColumns("create_time DESC");
		return this.smsWebMsgDao.findByCondition(smsWebMsg, pager);
	}
	
	@Override
	public int findSmsWebMsgCount(SmsWebMsg smsWebMsg){
		return this.smsWebMsgDao.getTotalCount(smsWebMsg);
	}
	
	@Override
	public Pager findSmsWebMsgWithDetailList(SmsWebMsg smsWebMsg, Pager pager) {
	    smsWebMsg.setSortColumns("create_time DESC");
		return this.smsWebMsgDao.findByCondition(smsWebMsg, pager, ".findSmsWebMsgWithDetailList");
	}

	@Override
	public int updateSmsWebMsg(String[] ids, Integer state, Integer regUserId) {
		logger.info("updateSmsWebMsg, 更新站内信, 用户: {}, 站内信标识集合: {}, 更新状态: {}", regUserId, ids, state);
		try {
			SmsWebMsg msg = new SmsWebMsg();
			msg.setIds(ids);
			msg.setState(state);
			msg.setRegUserId(regUserId);
			return this.smsWebMsgDao.update(msg);
		} catch (Exception e) {
			logger.error("updateSmsWebMsg, 更新站内信, 用户: {}, 站内信标识集合: {}, 更新状态: {}\n", regUserId, ids, state, e);
			throw new GeneralException("更新站内信失败");
		}
	}

	@Override
	public int findUnreadWebMsg(Integer regUserId) {
		SmsWebMsg smsWebMsg = new SmsWebMsg();
		smsWebMsg.setRegUserId(regUserId);
		smsWebMsg.setState(0);
		smsWebMsg.setSortColumns("create_time DESC");
		return this.smsWebMsgDao.getTotalCount(smsWebMsg);
	}
}
