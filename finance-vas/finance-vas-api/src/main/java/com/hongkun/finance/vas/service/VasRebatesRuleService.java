package com.hongkun.finance.vas.service;

import java.math.BigDecimal;
import java.util.List;

import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.RebatesRuleChildItem;
import com.hongkun.finance.vas.model.RecommendRuleVo;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.service.VasRebatesRuleService.java
 * @Class Name : VasRebatesRuleService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasRebatesRuleService {

	/**
	 * @Described : 单条插入
	 * @param vasRebatesRule
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertVasRebatesRule(VasRebatesRule vasRebatesRule);

	/**
	 * @Description : 插入规则
	 * @Method_Name : insertVasRebatesRule;
	 * @param content
	 *            规则内容（JSON）
	 * @param vasRuleTypeEnum
	 *            规则类型
	 * @param startTime
	 *            规则生效时间
	 * @param endTime
	 *            规则失效时间
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月21日 下午3:48:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int insertVasRebatesRule(String content, VasRuleTypeEnum vasRuleTypeEnum, String startTime, String endTime);

	/**
	 * @Described : 批量插入
	 * @param List<VasRebatesRule>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertVasRebatesRuleBatch(List<VasRebatesRule> list);

	/**
	 * @Described : 批量插入
	 * @param List<VasRebatesRule>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertVasRebatesRuleBatch(List<VasRebatesRule> list, int count);

	/**
	 * @Described : 更新数据
	 * @param vasRebatesRule
	 *            要更新的数据
	 * @return : void
	 */
	int updateVasRebatesRule(VasRebatesRule vasRebatesRule);

	/**
	 * @Described : 批量更新数据
	 * @param vasRebatesRule
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateVasRebatesRuleBatch(List<VasRebatesRule> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return VasRebatesRule
	 */
	VasRebatesRule findVasRebatesRuleById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param vasRebatesRule
	 *            检索条件
	 * @return List<VasRebatesRule>
	 */
	List<VasRebatesRule> findVasRebatesRuleList(VasRebatesRule vasRebatesRule);

	/**
	 * @Described : 条件检索数据
	 * @param vasRebatesRule
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<VasRebatesRule>
	 */
	List<VasRebatesRule> findVasRebatesRuleList(VasRebatesRule vasRebatesRule, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param vasRebatesRule
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<VasRebatesRule>
	 */
	Pager findVasRebatesRuleList(VasRebatesRule vasRebatesRule, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param vasRebatesRule
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findVasRebatesRuleCount(VasRebatesRule vasRebatesRule);

	/**
	 * @Description :通过规则类型查询规则信息
	 * @Method_Name : findVasRebatesRuleByTypeAndState;
	 * @param ruleId
	 * @return
	 * @return : VasRebatesRule;
	 * @Creation Date : 2017年7月14日 下午4:51:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	VasRebatesRule findVasRebatesRuleByTypeAndState(int type, int state);

	/**
	 * @Description :更新债权池金额
	 * @Method_Name : updateCreditorMoney;
	 * @param creditorMoney
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月17日 上午10:46:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateCreditorMoney(BigDecimal creditorMoney);

	/**
	 * @Description : 更新规则
	 * @Method_Name : updateVasRebatesRule;
	 * @param content
	 *            规则内容 （json)
	 * @param startTime
	 *            规则生效时间
	 * @param endTime
	 *            规则失效时间
	 * @param vasRebatesRuleId
	 *            规则ID
	 * @param state
	 *            状态0-初始化，1-启用，2-失效',
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月21日 下午4:50:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateVasRebatesRule(String content, String startTime, String endTime, Integer vasRebatesRuleId, Integer state);

	/**
	 * @Description :
	 *              根据ID启用某个规则，规则启用流程:首先查询出该规则，根据类型将目前已启用的规则置为失效，然后将当前的规则更新为启用状态
	 *              ，同时更新缓存
	 * @Method_Name : findTransRecords
	 * @Date : 2017/10/17 14:49
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param vasRebatesRuleId
	 * @return
	 */
	ResponseEntity<?> enableRuleById(Integer vasRebatesRuleId);

	/**
	 * @Description :插入好友推荐主规则，及每个角色的规则
	 * @Method_Name : insertVasRebatesRule;
	 * @param rebatesRuleChildItem
	 * @param recommendRuleVo
	 * @return
	 * @return : int;
	 * @Creation Date : 2018年4月27日 下午7:08:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> insertVasRebatesRule(RebatesRuleChildItem rebatesRuleChildItem, RecommendRuleVo recommendRuleVo);

	/**
	 * @Description : 好友推荐规则查询分页信息
	 * @Method_Name : findRulePager;
	 * @param vasRebatesRule
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月28日 上午10:51:51;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRulePager(VasRebatesRule vasRebatesRule, Pager pager);

	/**
	 * @Description : 通过规则ID，查询规则信息
	 * @Method_Name : findRuleById;
	 * @param vasRuleId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月28日 下午3:34:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRuleById(int vasRuleId);

	/**
	 * @Description : 更新规则信息
	 * @Method_Name : updateVasRuleById;
	 * @param rebatesRuleChildItem
	 * @param recommendRuleVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月28日 下午5:50:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateVasRuleById(RebatesRuleChildItem rebatesRuleChildItem, RecommendRuleVo recommendRuleVo);
	/**
     * @Description : 校验钱袋子转入转出规则
     * @Method_Name : checkQdzRule;
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年7月17日 下午2:35:10;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
	ResponseEntity<?> checkQdzRule();
	/**
	 *  @Description    : 根据类型获取APP钱袋子规则介绍信息
	 *  @Method_Name    : getQdzRule;
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年10月22日 下午2:56:36;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> getQdzRule(Integer type);
}
