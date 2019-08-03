package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.dao.FundInfoDao;
import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.fund.util.FundUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.fund.constants.FundConstants.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.FundInfoServiceImpl.java
 * @Class Name    : FundInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class FundInfoServiceImpl implements FundInfoService {

	private static final Logger logger = LoggerFactory.getLogger(FundInfoServiceImpl.class);
	
	/**
	 * FundInfoDAO
	 */
	@Autowired
	private FundInfoDao fundInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundInfo(FundInfo fundInfo) {
		this.fundInfoDao.save(fundInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundInfoBatch(List<FundInfo> list) {
		this.fundInfoDao.insertBatch(FundInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundInfoBatch(List<FundInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.fundInfoDao.insertBatch(FundInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundInfo(FundInfo fundInfo) {
		this.fundInfoDao.update(fundInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundInfoBatch(List<FundInfo> list, int count) {
		this.fundInfoDao.updateBatch(FundInfo.class, list, count);
	}
	
	@Override
	public FundInfo findFundInfoById(int id) {
		FundInfo fundInfo = this.fundInfoDao.findByPK(Long.valueOf(id), FundInfo.class);
		String[] weekTime = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		
		if (null != fundInfo.getOpendayType() && fundInfo.getOpendayType() == 2
				&& null != fundInfo.getCustomizeType() && null != fundInfo.getCustomizeValue()) {
			StringBuffer customizeTimeStr = new StringBuffer();
			if (fundInfo.getCustomizeType() == 1) {
				customizeTimeStr.append("每周");
				for (int i = 0; i < weekTime.length; i++) {
					if (fundInfo.getCustomizeValue() == i + 1) {
						customizeTimeStr.append(weekTime[i]);
					}
				}

			} else if (fundInfo.getCustomizeType() == 2) {
				customizeTimeStr.append("每月").append(fundInfo.getCustomizeValue() + "号");
			}
			fundInfo.setCustomizeTimeStr(customizeTimeStr.toString());
		}
		return fundInfo;
	}
	
	@Override
	public List<FundInfo> findFundInfoList(FundInfo fundInfo) {
		return this.fundInfoDao.findByCondition(fundInfo);
	}
	
	@Override
	public List<FundInfo> findFundInfoList(FundInfo fundInfo, int start, int limit) {
		return this.fundInfoDao.findByCondition(fundInfo, start, limit);
	}
	
	@Override
	public Pager findFundInfoList(FundInfo fundInfo, Pager pager) {
		return this.fundInfoDao.findByCondition(fundInfo, pager);
	}
	
	@Override
	public int findFundInfoCount(FundInfo fundInfo){
		return this.fundInfoDao.getTotalCount(fundInfo);
	}
	
	@Override
	public Pager findFundInfoList(FundInfo fundInfo, Pager pager, String sqlName){
		return this.fundInfoDao.findByCondition(fundInfo, pager, sqlName);
	}
	
	@Override
	public Integer findFundInfoCount(FundInfo fundInfo, String sqlName){
		return this.fundInfoDao.getTotalCount(fundInfo, sqlName);
	}

    @Override
    public List<String> findFundInfoByIds(List<String> ids) {
        return fundInfoDao.findFundInfoByIds(ids);
    }
	@Override
	public Pager findFundInfoVoByCondition(FundInfoVo contidion, Pager pager) {
		return this.fundInfoDao.findFundInfoVoByCondition(contidion, pager);
	}

	@Override
	public FundInfoVo getFundInfo(FundInfoVo fundInfoVo) {
		return this.fundInfoDao.getFundInfo(fundInfoVo);
	}

	@Override
	public List<FundInfoVo> findIndexFundInfos() {
        /**
         * 先正序查询预约中的产品，获取第一个；如果没有则按修改时间查询停约中的产品，取一个
         * 都没有则不放到集合中
         */
		List<FundInfoVo> fundInfoVos = new ArrayList<FundInfoVo>();
		FundInfoVo  fundInfoVo = null;
		//私募
		fundInfoVo = getFundInfoVoByParentType(FUND_PROJECT_PARENT_TYPE_PRIVATE);
		if(fundInfoVo!=null) {
			fundInfoVos.add(fundInfoVo);
		}
		//海外
		fundInfoVo = getFundInfoVoByParentType(FUND_PROJECT_PARENT_TYPE_ABROAD);
		if(fundInfoVo!=null) {
			fundInfoVos.add(fundInfoVo);
		}
		//信托
		fundInfoVo = getFundInfoVoByParentType(FUND_PROJECT_PARENT_TYPE_TRUST);
		if(fundInfoVo!=null) {
			fundInfoVos.add(fundInfoVo);
		}
		return fundInfoVos;
	}
	/**
	 * 
	 *  @Description    : 获取不同类型股权有效的一个
	 *  @Method_Name    : getFundInfoVoByCount;
	 *  @param parentType
	 *  @return
	 *  @return         : FundInfoVo;
	 *  @Creation Date  : 2018年5月7日 下午4:39:45;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	private FundInfoVo getFundInfoVoByParentType(Integer parentType) {
		FundInfoVo fundInfoVoDtc = new FundInfoVo();
		fundInfoVoDtc.setState(FUND_INFO_STATE_SHELF);
		fundInfoVoDtc.setInfoExist(FUND_INFO_EXIST_YES);
		fundInfoVoDtc.setSortColumns(" create_time asc");
		fundInfoVoDtc.setSubscribeState(FUND_INFO_SUBSCRIBE_STATE_ON);
		fundInfoVoDtc.setParentType(parentType);
		FundInfoVo  fundInfoVo = this.fundInfoDao.getFundInfo(fundInfoVoDtc);
		if(fundInfoVo == null) {
			fundInfoVoDtc.setSortColumns(" modify_time desc");
			fundInfoVoDtc.setSubscribeState(FUND_INFO_SUBSCRIBE_STATE_OFF);
			fundInfoVo = this.fundInfoDao.getFundInfo(fundInfoVoDtc);
		}
		if(fundInfoVo != null) {
			fundInfoVo.setOpenDateFlag(FundUtils.checkOpenDate(fundInfoVo)== true ? 1:0);
			fundInfoVo.setOpenDateDescribe(FundUtils.getFundOpenTime(fundInfoVo));
		}
		return fundInfoVo;
	}
}
