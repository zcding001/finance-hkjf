package com.hongkun.finance.vas.dao;

import java.util.List;

import com.hongkun.finance.vas.model.VasRebatesRuleChild;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.VasRebatesRuleChildDao.java
 * @Class Name : VasRebatesRuleChildDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasRebatesRuleChildDao extends MyBatisBaseDao<VasRebatesRuleChild, java.lang.Long> {
	/**
	 * @Description : 通过规则ID，查询子规则信息
	 * @Method_Name : findVasRebatesRuleChildByRuleId;
	 * @param vasRebatesRuleId
	 *            规则ID
	 * @return
	 * @return : List<VasRebatesRuleChild>;
	 * @Creation Date : 2018年4月27日 下午1:44:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<VasRebatesRuleChild> findVasRebatesRuleChildByRuleId(int vasRebatesRuleId);

	/**
	 * @Description : 通过规则ID和用户类型查询子规则信息
	 * @Method_Name : findRuleChildByUserTypeAndRuleId;
	 * @param userType
	 *            用户类型 0-普通用户，1-物业员工,2-销售员工，3-内部员工
	 * @param vasRebatesRuleId
	 *            规则ID
	 * @return
	 * @return : VasRebatesRuleChild;
	 * @Creation Date : 2018年4月28日 上午9:03:41;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	VasRebatesRuleChild findRuleChildByUserTypeAndRuleId(Integer userType, int vasRebatesRuleId);

	/**
	 * @Description : 通过规则ID，用户类型，更新用户角色规则
	 * @Method_Name : updateRuleChildByUserTypeAndRuleId;
	 * @param userType
	 * @param vasRebatesRuleId
	 * @return
	 * @return : int;
	 * @Creation Date : 2018年4月28日 下午6:00:58;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateRuleChildByUserTypeAndRuleId(Integer userType, int vasRebatesRuleId);
}
