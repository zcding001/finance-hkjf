package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasRebatesRule;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.VasRebatesRuleDao.java
 * @Class Name : VasRebatesRuleDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasRebatesRuleDao extends MyBatisBaseDao<VasRebatesRule, java.lang.Long> {

	/**
	 * @Description :通过规则类型查询规则信息
	 * @Method_Name : findVasRebatesRuleByTypeAndState;
	 * @param key
	 *            缓存的KEY
	 * @param state
	 *            状态
	 * @param type
	 *            类型
	 * @return
	 * @return : VasRebatesRule;
	 * @Creation Date : 2018年03月12日 上午9:51:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	VasRebatesRule findVasRebatesRule(String key, Integer state, Integer type);
}
