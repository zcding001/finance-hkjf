package com.hongkun.finance.roster.service.impl;

import java.util.List;

import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.roster.model.RosNotice;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.roster.dao.RosNoticeDao;
import com.hongkun.finance.roster.service.RosNoticeService;

import static com.hongkun.finance.roster.constants.RosterConstants.ROS_NOTICE_WAY_EMAIL;
import static com.hongkun.finance.roster.constants.RosterConstants.ROS_NOTICE_WAY_TEL;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.service.impl.RosNoticeServiceImpl.java
 * @Class Name    : RosNoticeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RosNoticeServiceImpl implements RosNoticeService {

	private static final Logger logger = LoggerFactory.getLogger(RosNoticeServiceImpl.class);
	
	/**
	 * RosNoticeDAO
	 */
	@Autowired
	private RosNoticeDao rosNoticeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosNotice(RosNotice rosNotice) {
		this.rosNoticeDao.save(rosNotice);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosNoticeBatch(List<RosNotice> list) {
		this.rosNoticeDao.insertBatch(RosNotice.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosNoticeBatch(List<RosNotice> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.rosNoticeDao.insertBatch(RosNotice.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosNotice(RosNotice rosNotice) {
		this.rosNoticeDao.update(rosNotice);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosNoticeBatch(List<RosNotice> list, int count) {
		this.rosNoticeDao.updateBatch(RosNotice.class, list, count);
	}
	
	@Override
	public RosNotice findRosNoticeById(int id) {
		return this.rosNoticeDao.findByPK(Long.valueOf(id), RosNotice.class);
	}
	
	@Override
	public List<RosNotice> findRosNoticeList(RosNotice rosNotice) {
		return this.rosNoticeDao.findByCondition(rosNotice);
	}
	
	@Override
	public List<RosNotice> findRosNoticeList(RosNotice rosNotice, int start, int limit) {
		return this.rosNoticeDao.findByCondition(rosNotice, start, limit);
	}
	
	@Override
	public Pager findRosNoticeList(RosNotice rosNotice, Pager pager) {
		return this.rosNoticeDao.findByCondition(rosNotice, pager);
	}
	
	@Override
	public int findRosNoticeCount(RosNotice rosNotice){
		return this.rosNoticeDao.getTotalCount(rosNotice);
	}
	
	@Override
	public Pager findRosNoticeList(RosNotice rosNotice, Pager pager, String sqlName){
		return this.rosNoticeDao.findByCondition(rosNotice, pager, sqlName);
	}
	
	@Override
	public Integer findRosNoticeCount(RosNotice rosNotice, String sqlName){
		return this.rosNoticeDao.getTotalCount(rosNotice, sqlName);
	}

	@Override
	public String getEmailsByType(Integer type) {
		StringBuffer result =  new StringBuffer("");
		RosNotice cdt = new RosNotice();
		cdt.setType(type);
		cdt.setNoticeWay(ROS_NOTICE_WAY_EMAIL);
		List<RosNotice> resultList = this.rosNoticeDao.findByCondition(cdt);
		if(CommonUtils.isNotEmpty(resultList)){
			for (int i = 0 ;i <= resultList.size()-1;i++){
				RosNotice rosNotice = resultList.get(i);
				String email = rosNotice.getReceiveEmail();
				if (StringUtils.isNotBlank(email)){
					result.append(email);
					if (i < resultList.size()-1){
						result.append(",");
					}
				}
			}
			return result.toString();
		}
		return null;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> addRosNotice(RosNotice rosNotice) {
		try {
			Integer noticWay = rosNotice.getNoticeWay();
			if (noticWay == ROS_NOTICE_WAY_EMAIL && StringUtils.isBlank(rosNotice.getReceiveEmail())){
				return new ResponseEntity<>(ERROR,"邮件不能为空");
			}
			if (noticWay == ROS_NOTICE_WAY_TEL && StringUtils.isBlank(rosNotice.getReceiveTel())){
				return new ResponseEntity<>(ERROR,"手机号不能为空");
			}
			//判断是否存在当前记录 type noticeWay Email or tel
			List<RosNotice> rosNotices  = rosNoticeDao.findByCondition(rosNotice);
			if (CommonUtils.isNotEmpty(rosNotices)){
				return new ResponseEntity<>(ERROR,"此条通知已经存在");
			}
			rosNoticeDao.save(rosNotice);
			return new ResponseEntity<>(SUCCESS,"添加成功");
		} catch (Exception e) {
			logger.info("addRosNotice, 添加功能通知异常, rosNotice: {}", rosNotice.toString(),e);
			throw new GeneralException("添加通知失败");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> editRosNotice(RosNotice rosNotice) {
		Integer noticWay = rosNotice.getNoticeWay();
		if (!CommonUtils.gtZero(rosNotice.getId())||!CommonUtils.gtZero(noticWay)){
			return new ResponseEntity<>(ERROR,"请选择正确得通知记录");
		}
		if (noticWay == ROS_NOTICE_WAY_EMAIL && StringUtils.isBlank(rosNotice.getReceiveEmail())){
			return new ResponseEntity<>(ERROR,"邮件不能为空");
		}
		if (noticWay == ROS_NOTICE_WAY_TEL && StringUtils.isBlank(rosNotice.getReceiveTel())){
			return new ResponseEntity<>(ERROR,"手机号不能为空");
		}
		rosNoticeDao.update(rosNotice);
		return new ResponseEntity<>(SUCCESS,"修改成功");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> delRosNotice(Integer id) {
		RosNotice rosNotice = rosNoticeDao.findByPK(Long.valueOf(id),RosNotice.class);
		if (rosNotice==null){
			return new ResponseEntity<>(ERROR,"请选择有效得通知记录");
		}
		rosNoticeDao.delete(Long.valueOf(id),RosNotice.class);
		return new ResponseEntity<>(SUCCESS,"删除成功");
	}
}
