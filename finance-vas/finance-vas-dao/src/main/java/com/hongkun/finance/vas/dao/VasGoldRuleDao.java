package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasGoldRule;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.VasGoldRuleDao.java
 * @Class Name : VasGoldRuleDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasGoldRuleDao extends MyBatisBaseDao<VasGoldRule, java.lang.Long> {
	/**
	 * @Description : 通过体验金规则类型，和状态，查询有效的体验金规则
	 * @Method_Name : findVasGoldRuleByTypeAndState;
	 * @param type
	 *            体验金规则类型
	 * @param state
	 *            状态:0-初始化，1-有效，2-失效 ， 3-删除'
	 * @return
	 * @return : VasGoldRule;
	 * @Creation Date : 2018年4月8日 上午9:12:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	VasGoldRule findVasGoldRuleByTypeAndState(int type, int state);
}
