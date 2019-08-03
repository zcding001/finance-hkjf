package com.hongkun.finance.payment.dao;

import java.util.List;

import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinChannelGrantDao.java
 * @Class Name : FinChannelGrantDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinChannelGrantDao extends MyBatisBaseDao<FinChannelGrant, java.lang.Long> {
	/**
	 * @Description : 根据系统名称、支付方式查询当前系统支持哪些支付方式
	 * @Method_Name : findFinChannelGrantList;
	 * @param systemTypeEnums
	 *            系统Code
	 * @param payStyleEnum
	 *            支付方式
	 * @param platformSourceEnums
	 *            平台来源
	 * @return
	 * @return : List<FinChannelGrant>;
	 * @Creation Date : 2017年12月6日 上午10:54:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<FinChannelGrant> findFinChannelGrantList(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum);

	/**
	 * 查询优先级最高支付渠道
	 * 
	 * @param systemTypeEnums
	 * @param platformSourceEnums
	 * @param payStyleEnum
	 * @return
	 */
	FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum);
}
