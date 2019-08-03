package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.dao.FundAdvisoryDao;
import com.hongkun.finance.fund.dao.FundAgreementDao;
import com.hongkun.finance.fund.dao.FundInfoDao;
import com.hongkun.finance.fund.dao.FundUserInfoDao;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.FundUserInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundAgreementService;
import com.hongkun.finance.fund.util.FundUtils;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.FundAgreementServiceImpl.java
 * @Class Name    : FundAgreementServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class FundAgreementServiceImpl implements FundAgreementService {

	private static final Logger logger = LoggerFactory.getLogger(FundAgreementServiceImpl.class);
	
	/**
	 * FundAgreementDAO
	 */
	@Autowired
	private FundAgreementDao fundAgreementDao;

	@Autowired
	private FundUserInfoDao fundUserInfoDao;

	@Autowired
	private FundAdvisoryDao fundAdvisoryDao;

	@Autowired
	private FundInfoDao fundInfoDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertFundAgreement(FundAgreement fundAgreement) {
		this.fundAgreementDao.save(fundAgreement);
		return fundAgreement.getId();
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundAgreementBatch(List<FundAgreement> list) {
		this.fundAgreementDao.insertBatch(FundAgreement.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundAgreementBatch(List<FundAgreement> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.fundAgreementDao.insertBatch(FundAgreement.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundAgreement(FundAgreement fundAgreement) {
		this.fundAgreementDao.update(fundAgreement);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundAgreementBatch(List<FundAgreement> list, int count) {
		this.fundAgreementDao.updateBatch(FundAgreement.class, list, count);
	}
	
	@Override
	public FundAgreement findFundAgreementById(int id) {
		return this.fundAgreementDao.findByPK(Long.valueOf(id), FundAgreement.class);
	}
	
	@Override
	public List<FundAgreement> findFundAgreementList(FundAgreement fundAgreement) {
		return this.fundAgreementDao.findByCondition(fundAgreement);
	}
	
	@Override
	public List<FundAgreement> findFundAgreementList(FundAgreement fundAgreement, int start, int limit) {
		return this.fundAgreementDao.findByCondition(fundAgreement, start, limit);
	}
	
	@Override
	public Pager findFundAgreementList(FundAgreement fundAgreement, Pager pager) {
		return this.fundAgreementDao.findByCondition(fundAgreement, pager);
	}
	
	@Override
	public int findFundAgreementCount(FundAgreement fundAgreement){
		return this.fundAgreementDao.getTotalCount(fundAgreement);
	}
	
	@Override
	public Pager findFundAgreementList(FundAgreement fundAgreement, Pager pager, String sqlName){
		return this.fundAgreementDao.findByCondition(fundAgreement, pager, sqlName);
	}
	
	@Override
	public Integer findFundAgreementCount(FundAgreement fundAgreement, String sqlName){
		return this.fundAgreementDao.getTotalCount(fundAgreement, sqlName);
	}

    @Override
    public FundAgreement findFundAgreementInfo(FundAgreement fundAgreement) {
		try{
			fundAgreement.setSortColumns("id desc");
			List<FundAgreement> list = this.fundAgreementDao.findByCondition(fundAgreement);
			if(!CollectionUtils.isEmpty(list)){
				return list.get(0);
			}
		}catch (Exception e){
			logger.error("条件查询海外基金预约协议信息失败!",e);
		}
        return null;
    }

    @Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public int insertOrUpdateAdvisoryAndFundUserInfo(FundUserInfo fundUserInfo, FundAgreement agreement,RegUserDetail userDetail) {
		try{
			fundUserInfo.setRegUserId(agreement.getRegUserId());
			if(null != fundUserInfo.getId() && fundUserInfo.getId() > 0){
				fundUserInfoDao.update(fundUserInfo);
			}else{
				fundUserInfoDao.save(fundUserInfo);
			}
			FundInfoVo fundInfoVo = new FundInfoVo();
			fundInfoVo.setId(agreement.getFundInfoId());
			FundInfoVo fundInfo = fundInfoDao.getFundInfo(fundInfoVo);
			// 插入预约信息
			FundAdvisory fundAdvisory = new FundAdvisory();
			fundAdvisory.setInfoIds(String.valueOf(fundInfo.getId()));
			fundAdvisory.setRegUserId(agreement.getRegUserId());
			fundAdvisory.setProjectInfoName(fundInfo.getProjectName());
			fundAdvisory.setName(agreement.getUserSurname() + agreement.getUserName());
			fundAdvisory.setTel(agreement.getTel());
			fundAdvisory.setSex(FundUtils.getSexFromIdCard(userDetail.getIdCard()));
			fundAdvisory.setProjectParentType(FundConstants.FUND_PROJECT_PARENT_TYPE_ABROAD);
			// 插入主键返回值 回调给fundAdvisory
			fundAdvisoryDao.save(fundAdvisory);
			// 关联预约信息 更新状态 -- 前台资质拒绝 无需更改状态
			agreement.setFundAdvisoryId(fundAdvisory.getId());
			if(agreement.getState().equals(FundConstants.FUND_ADVISORY_STATE_QULICATION_REJECT)){
				agreement.setState(null);
			}else{
				agreement.setState(FundConstants.FUND_ADVISORY_STATE_UNDER_AUDIT);
			}
			return fundAgreementDao.update(agreement);
		}catch (Exception e){
			if(logger.isErrorEnabled()){
				logger.error("保存或更新海外基金股权个人信息失败！",e);
				throw new RuntimeException("保存或更新海外基金股权个人信息失败！",e);
			}
		}
        return 0;
    }
}
