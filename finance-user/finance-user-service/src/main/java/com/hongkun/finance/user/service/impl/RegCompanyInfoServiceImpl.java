package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegCompanyInfoDao;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.yirun.framework.core.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RegCompanyInfoServiceImpl.java
 * @Class Name    : RegCompanyInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegCompanyInfoServiceImpl implements RegCompanyInfoService {

	private static final Logger logger = LoggerFactory.getLogger(RegCompanyInfoServiceImpl.class);
	
	/**
	 * RegCompanyInfoDAO
	 */
	@Autowired
	private RegCompanyInfoDao regCompanyInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegCompanyInfo(RegCompanyInfo regCompanyInfo) {
		logger.info("updateRegCompanyInfo, 更新企业信息失败. regCompanyInfo : {}", regCompanyInfo.toString());
		try{
			this.regCompanyInfoDao.update(regCompanyInfo);
		}catch(Exception e){
		    logger.error("updateRegCompanyInfo, 更新企业信息失败. regCompanyInfo : {}", regCompanyInfo.toString(), e);
		    throw new GeneralException("更新企业信息失败");
		}
	}

	@Override
	public RegCompanyInfo findRegCompanyInfoByRegUserId(int regUserId) {
		return this.regCompanyInfoDao.findRegCompanyInfoByRegUserId(regUserId);
	}

	@Override
	public List<RegCompanyInfo> findRegCompanyInfoByLegalTel(String legalTel) {
		return this.regCompanyInfoDao.findRegCompanyInfoByLegalTel(legalTel);
	}
}
