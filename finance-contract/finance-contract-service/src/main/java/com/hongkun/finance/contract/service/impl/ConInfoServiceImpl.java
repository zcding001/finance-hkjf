package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.dao.ConInfoDao;
import com.hongkun.finance.contract.dao.ConTypeDao;
import com.hongkun.finance.contract.model.ConInfo;
import com.hongkun.finance.contract.model.ConType;
import com.hongkun.finance.contract.service.ConInfoService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.impl.ConInfoServiceImpl.java
 * @Class Name    : ConInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class ConInfoServiceImpl implements ConInfoService {

	private static final Logger logger = LoggerFactory.getLogger(ConInfoServiceImpl.class);
	
	/**
	 * ConInfoDAO
	 */
	@Autowired
	private ConInfoDao conInfoDao;
	@Autowired
	private ConTypeDao conTypeDao;

	@Override
	public List<ConInfo> findConInfoList(ConInfo conInfo) {
		return this.conInfoDao.findByCondition(conInfo);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertConInfo(ConInfo conInfo) {
		try{
			this.conInfoDao.save(conInfo);
		}catch (Exception e){
			logger.error("insertConInfoBatch, 插入合同信息异常, 合同信息: {}, 异常信息: ", conInfo.toString(), e);
			throw new GeneralException("插入合同信息异常！");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertConInfoBatch(List<ConInfo> list) {
		try{
			this.conInfoDao.insertBatch(ConInfo.class, list);
		}catch (Exception e){
			logger.error("insertConInfoBatch, 批量插入合同信息异常, 合同集合信息: {}, 异常信息: ", JSON.toJSON(list), e);
			throw new GeneralException("批量插入合同信息异常！");
		}
	}

	@Override
	public ConInfo findConInfo(ConInfo conInfo) {
		return this.conInfoDao.findConInfo(conInfo);
	}

	@Override
	public List<ConInfo> findConInfoListByInvestIds(List<Integer> listInvestIds) {
		return this.conInfoDao.findConInfoListByInvestIds(listInvestIds);
	}

	@Override
	public List<Map<String, Object>> initContracts(String contractStr, Integer fromPlace) {
		//获取
		String[] contracts = StringUtils.split(contractStr, ",");
		//是否要执行隐藏策略
		boolean needHide=BaseUtil.equelsIntWraperPrimit(fromPlace, ContractConstants.CONTRACT_LOCATION_BIDD);
		if (ArrayUtils.isNotEmpty(contracts)) {
			return Arrays.stream(contracts).map(type -> {
				Integer typeInt = Integer.valueOf(type);
				if (needHide && BaseUtil.equelsIntWraperPrimit(typeInt, ContractConstants.CONTRACT_TYPE_SECURITY_PLAN)) {
					//本条合同不需要展示
					return null;
				}
				ConType queryConType = new ConType();
				queryConType.setType(Integer.valueOf(type));
				queryConType.setState(ContractConstants.CONTRACT_STATE_Y);
				//查询具体合同
				List<ConType> conTypes = this.conTypeDao.findByCondition(queryConType);
				if (!BaseUtil.collectionIsEmpty(conTypes)) {
					return conTypes.stream().findFirst()
							.map(first -> new HashMap<String, Object>() {
								{
									put("type", first.getType());
									put("text", first.getShowName());
								}
							}).orElse(null);

				}
				return null;
			}).filter(map->map!=null).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public void updateConInfoBatch(List<ConInfo> list, int count) {
		conInfoDao.updateBatch(ConInfo.class,list,count);
	}
}
