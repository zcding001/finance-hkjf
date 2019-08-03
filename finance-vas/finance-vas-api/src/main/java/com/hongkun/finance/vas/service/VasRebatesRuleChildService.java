package com.hongkun.finance.vas.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.vas.model.VasRebatesRuleChild;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.service.VasRebatesRuleChildService.java
 * @Class Name : VasRebatesRuleChildService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasRebatesRuleChildService {

	/**
	 * @Described : 单条插入
	 * @param vasRebatesRuleChild
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertVasRebatesRuleChild(VasRebatesRuleChild vasRebatesRuleChild);

	/**
	 * @Described : 批量插入
	 * @param List<VasRebatesRuleChild>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertVasRebatesRuleChildBatch(List<VasRebatesRuleChild> list, int count);

	/**
	 * @Described : 更新数据
	 * @param vasRebatesRuleChild
	 *            要更新的数据
	 * @return : void
	 */
	void updateVasRebatesRuleChild(VasRebatesRuleChild vasRebatesRuleChild);

	/**
	 * @Described : 批量更新数据
	 * @param vasRebatesRuleChild
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateVasRebatesRuleChildBatch(List<VasRebatesRuleChild> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return VasRebatesRuleChild
	 */
	VasRebatesRuleChild findVasRebatesRuleChildById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param vasRebatesRuleChild
	 *            检索条件
	 * @return List<VasRebatesRuleChild>
	 */
	List<VasRebatesRuleChild> findVasRebatesRuleChildList(VasRebatesRuleChild vasRebatesRuleChild);

	/**
	 * @Described : 条件检索数据
	 * @param vasRebatesRuleChild
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<VasRebatesRuleChild>
	 */
	Pager findVasRebatesRuleChildList(VasRebatesRuleChild vasRebatesRuleChild, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param vasRebatesRuleChild
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findVasRebatesRuleChildCount(VasRebatesRuleChild vasRebatesRuleChild);

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
}
