package com.hongkun.finance.vas.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.vas.model.SysAppVersionRule;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.SysAppVersionRuleService.java
 * @Class Name    : SysAppVersionRuleService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SysAppVersionRuleService {
	
	/**
	 * @Described			: 单条插入
	 * @param sysAppVersionRule 持久化的数据对象
	 * @return				: void
	 */
	int insertSysAppVersionRule(SysAppVersionRule sysAppVersionRule);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SysAppVersionRule> 批量插入的数据
	 * @return				: void
	 */
	int insertSysAppVersionRuleBatch(List<SysAppVersionRule> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SysAppVersionRule> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	int insertSysAppVersionRuleBatch(List<SysAppVersionRule> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param sysAppVersionRule 要更新的数据
	 * @return				: void
	 */
	int updateSysAppVersionRule(SysAppVersionRule sysAppVersionRule);
	
	/**
	 * @Described			: 批量更新数据
	 * @param list 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	int updateSysAppVersionRuleBatch(List<SysAppVersionRule> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SysAppVersionRule
	 */
	SysAppVersionRule findSysAppVersionRuleById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysAppVersionRule 检索条件
	 * @return	List<SysAppVersionRule>
	 */
	List<SysAppVersionRule> findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysAppVersionRule 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<SysAppVersionRule>
	 */
	List<SysAppVersionRule> findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysAppVersionRule 检索条件
	 * @param pager	分页数据
	 * @return	List<SysAppVersionRule>
	 */
	Pager findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param sysAppVersionRule 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findSysAppVersionRuleCount(SysAppVersionRule sysAppVersionRule);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findSysAppVersionRuleList(SysAppVersionRule sysAppVersionRule, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : findSysAppVersionRuleCount
	 *  @param sysAppVersionRule
	 *  @param sqlName
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findSysAppVersionRuleCount(SysAppVersionRule sysAppVersionRule, String sqlName);

	/**
	 *  @Description    ：根据平台和app版本号获取app更新规则
	 *  @Method_Name    ：findRuleCountByVersion
	 *  @param platform  平台：1-ios，2-android
	 *  @param version  app版本号
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018/4/26
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity findRuleCountByVersion(String platform, String version);

	/**
	 *  @Description    ：更新app版本规则状态
	 *  @Method_Name    ：updateAppVersionRuleState
	 *  @param id  app版本规则id
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018/4/26
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    ResponseEntity updateAppVersionRuleState(int id,int state);

	/**
	 *  @Description    ：根据ios传递的版本判断该版本是否在审核中
	 *  @Method_Name    ：getIosAuditVersion
	 *  @param version  ios当前版本号
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018/6/12
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ResponseEntity getIosAuditVersion(String version);
}
