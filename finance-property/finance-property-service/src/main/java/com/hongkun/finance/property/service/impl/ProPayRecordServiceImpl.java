package com.hongkun.finance.property.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.property.dao.ProPayRecordDao;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.property.service.ProPayRecordService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.property.constants.PropertyContants.PROPERTY_RECORD_STATE_ING;
/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.service.impl.ProPayRecordServiceImpl.java
 * @Class Name    : ProPayRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class ProPayRecordServiceImpl implements ProPayRecordService {

	private static final Logger logger = LoggerFactory.getLogger(ProPayRecordServiceImpl.class);
	
	private static final String FIND_PROPAYRECORDVO_LIST = ".findProPayRecordVoList";
	
	/**
	 * ProPayRecordDAO
	 */
	@Autowired
	private ProPayRecordDao proPayRecordDao;
	
	@Override
	@Compensable(cancelMethod = "insertProPayRecordCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertProPayRecord(ProPayRecord proPayRecord) {
		logger.info("{}, 生成缴费记录, 记录信息: {}",BaseUtil.getTccTryLogPrefix(),proPayRecord.toString());
		try {
			this.proPayRecordDao.save(proPayRecord);
		} catch (Exception e) {
			logger.error("{}, 生成缴费记录异常, 记录信息: {}, 异常信息: \n",proPayRecord.toString(), e);
			throw new GeneralException("生成缴费记录失败！");
		}
	}
	
	/**
	 * 生成物业缴费记录--回滚方法
	 * @param proPayRecord
	 * @return : ResponseEntity<?>
	 * @Description : TCC回滚方法  transferPoints
	 * @Method_Name : transferPointsCancel
	 * @Creation Date  : 2018年04月16日 下午4:33:39
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, readOnly = false)
	public void insertProPayRecordCancel(ProPayRecord proPayRecord){
		logger.info("tcc insertProPayRecordCancel, 缴费记录回滚, 记录信息: {}",proPayRecord.toString());
		try {
			if(proPayRecord == null || proPayRecord.getId() == null){
				logger.info("insertProPayRecordCancel proPayRecord or getId is null , no need transfer. ");
				return;
			}
			this.proPayRecordDao.delete(Long.valueOf(proPayRecord.getId()), ProPayRecord.class);
		} catch (Exception e) {
			logger.error("tcc insertProPayRecordCancel, 缴费记录回滚异常, 记录信息: {}, 异常信息: ",proPayRecord.toString(), e);
			throw new GeneralException("缴费记录回滚异常！");
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertProPayRecordBatch(List<ProPayRecord> list) {
		this.proPayRecordDao.insertBatch(ProPayRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertProPayRecordBatch(List<ProPayRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.proPayRecordDao.insertBatch(ProPayRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProPayRecord(ProPayRecord proPayRecord) {
		this.proPayRecordDao.update(proPayRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProPayRecordBatch(List<ProPayRecord> list, int count) {
		this.proPayRecordDao.updateBatch(ProPayRecord.class, list, count);
	}
	
	@Override
	public ProPayRecord findProPayRecordById(int id) {
		return this.proPayRecordDao.findByPK(Long.valueOf(id), ProPayRecord.class);
	}
	
	@Override
	public List<ProPayRecord> findProPayRecordList(ProPayRecord proPayRecord) {
		return this.proPayRecordDao.findByCondition(proPayRecord);
	}
	
	@Override
	public List<ProPayRecord> findProPayRecordList(ProPayRecord proPayRecord, int start, int limit) {
		return this.proPayRecordDao.findByCondition(proPayRecord, start, limit);
	}
	
	@Override
	public Pager findProPayRecordList(ProPayRecord proPayRecord, Pager pager) {
		return this.proPayRecordDao.findByCondition(proPayRecord, pager);
	}
	
	@Override
	public int findProPayRecordCount(ProPayRecord proPayRecord){
		return this.proPayRecordDao.getTotalCount(proPayRecord);
	}
	@Override
	@Compensable(cancelMethod = "updateProPayRecordStateCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProPayRecordState(int proId, int state,String opinion) {
		ProPayRecord ppr = new ProPayRecord();
		ppr.setId(proId);
		ppr.setState(state);
		if(StringUtils.isNotBlank(opinion)){
			ppr.setReviewReason(opinion);
		}
		this.proPayRecordDao.update(ppr);
	}

	
	/**
	 * 
	 *  @Description    : TCC 回滚方法 updateProPayRecordState
	 *  @Method_Name    : updateProPayRecordStateCancel
	 *  @param proId
	 *  @param state
	 *  @param opinion
	 *  @return         : void
	 *  @Creation Date  : 2017年10月23日 下午1:53:41 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProPayRecordStateCancel(int proId, int state,String opinion) {
		ProPayRecord ppr = new ProPayRecord();
		ppr.setId(proId);
		ppr.setState(PROPERTY_RECORD_STATE_ING);
		ppr.setState(state);
		if(StringUtils.isNotBlank(opinion)){
			ppr.setReviewReason(opinion);
		}
		this.proPayRecordDao.update(ppr);
	}

//	@Override
//	public Pager findProPayRecordVoList(ProPayRecordVo proPayRecordVo, Pager pager) {
//		return this.proPayRecordDao.findProPayRecordVoList(proPayRecordVo,pager);
//	}

	@Override
	public ResponseEntity<?> findProPayRecordListByPropertyId(Integer id, Pager pager) {
		ProPayRecord proPayRecordVo = new ProPayRecord();
		proPayRecordVo.setId(id);
		proPayRecordVo.setPayType(1);
		Pager result = this.proPayRecordDao.findByCondition(proPayRecordVo, pager,ProPayRecord.class,FIND_PROPAYRECORDVO_LIST);
		return new ResponseEntity<>(Constants.SUCCESS,result);
	}

	@Override
	@Compensable(cancelMethod = "updateStateCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer updateState(Map<String,Object> params) {
		logger.info("{}, 物业缴费更新状态, params: {}", BaseUtil.getTccTryLogPrefix(), params.toString());
		try {
			return this.proPayRecordDao.updateState(params);
		} catch (Exception e) {
            logger.error("{}, 物业缴费更新状态, params: {}", BaseUtil.getTccTryLogPrefix(), params.toString(), e);
			throw  new GeneralException("物业缴费更新状态失败");
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer updateStateCancel(Map<String,Object> params) {
		logger.info("tcc cancel updateStateCancel, 物业缴费更新状态回滚, params: {}",params.toString());
		try {
			Map<String,Object> newParams = new HashMap<String,Object>();
			if(CommonUtils.gtZero((Integer) params.get("proPayRecordId"))){
				newParams.put("proPayRecordId",params.get("proPayRecordId"));
				newParams.put("state",params.get("oldState"));
				newParams.put("oldState",params.get("state"));
				return this.proPayRecordDao.updateState(newParams);
			}
			return 0;
		} catch (Exception e) {
            logger.error("tcc cancel updateStateCancel, 物业缴费更新状态回滚, params: {}", params.toString(), e);
			throw  new GeneralException("物业缴费更新状态回滚失败");
		}
	}
}
