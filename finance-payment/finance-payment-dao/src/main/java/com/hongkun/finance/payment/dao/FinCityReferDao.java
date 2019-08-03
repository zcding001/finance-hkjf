package com.hongkun.finance.payment.dao;

import com.hongkun.finance.payment.model.FinCityRefer;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinCityReferDao.java
 * @Class Name : FinCityReferDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinCityReferDao extends MyBatisBaseDao<FinCityRefer, java.lang.Long> {
	/**
	 * @Description : 根据平台的省，市查询第三方的城市信息
	 * @Method_Name : findFinCityRefer;
	 * @param provinceCode
	 *            平台省CODE
	 * @param cityCode
	 *            平台市CODE
	 * @param channelCode
	 *            支付渠道CODE
	 * @return
	 * @return : FinCityRefer;
	 * @Creation Date : 2018年4月3日 下午1:41:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinCityRefer findFinCityRefer(String provinceCode, String cityCode, String channelCode);
}
