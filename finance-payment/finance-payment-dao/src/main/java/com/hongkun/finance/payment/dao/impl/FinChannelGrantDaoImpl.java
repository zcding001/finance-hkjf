package com.hongkun.finance.payment.dao.impl;

import java.util.List;

import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinChannelGrantDao;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.dao.impl.FinChannelGrantDaoImpl.java
 * @Class Name : FinChannelGrantDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinChannelGrantDaoImpl extends MyBatisBaseDaoImpl<FinChannelGrant, java.lang.Long>
		implements FinChannelGrantDao {
	/**
	 * 根据系统名称、支付方式查询当前系统支持哪些支付方式
	 */
	public String FIND_FIN_CHANNEL_GRANT_LIST = ".findFinChannelGrantList";

	@Override
	public List<FinChannelGrant> findFinChannelGrantList(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum) {
		FinChannelGrant finChannelGrant = new FinChannelGrant();
		finChannelGrant.setSysNameCode(systemTypeEnums.getType());
		finChannelGrant.setState(TradeStateConstants.START_USING_STATE);
		finChannelGrant
				.setPlatformSource(platformSourceEnums == null ? "" : String.valueOf(platformSourceEnums.getValue()));
		finChannelGrant.setPayStyle(payStyleEnum.getValue());
		finChannelGrant.setSortColumns("sort asc");
		return getCurSqlSessionTemplate().selectList(FinChannelGrant.class.getName() + FIND_FIN_CHANNEL_GRANT_LIST,
				finChannelGrant);
	}

	@Override
	public FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums,PlatformSourceEnums platformSourceEnums,PayStyleEnum payStyleEnum) {
		List<FinChannelGrant> resultList = findFinChannelGrantList(systemTypeEnums, platformSourceEnums, payStyleEnum);
		if(CommonUtils.isNotEmpty(resultList)){
			return resultList.get(0);
		}
		return null;
	}
}
