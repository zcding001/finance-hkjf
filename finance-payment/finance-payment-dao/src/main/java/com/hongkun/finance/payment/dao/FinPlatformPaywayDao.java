package com.hongkun.finance.payment.dao;

import java.util.List;
import com.hongkun.finance.payment.model.FinPlatformPayway;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinPlatformPaywayDao.java
 * @Class Name : FinPlatformPaywayDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinPlatformPaywayDao extends MyBatisBaseDao<FinPlatformPayway, java.lang.Long> {
	/**
	 * @Description : 查询某个系统的某个平台下有哪几种支付渠道
	 * @Method_Name : findPayChannelInfo;
	 * @param finPlatformPayway
	 * @return
	 * @return : List<FinPlatformPayway>;
	 * @Creation Date : 2017年12月6日 下午1:43:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<FinPlatformPayway> findPayChannelInfo(FinPlatformPayway finPlatformPayway);
}
