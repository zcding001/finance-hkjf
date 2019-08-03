package com.hongkun.finance.vas.service;

import java.util.Date;
import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.vas.model.VasVipGrowRule;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.VasVipGrowRuleService.java
 * @Class Name    : VasVipGrowRuleService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasVipGrowRuleService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasVipGrowRule 持久化的数据对象
	 * @return				: int
	 */
	int insertVasVipGrowRule(VasVipGrowRule vasVipGrowRule);

	/**
	 * @Described			: 更新数据
	 * @param vasVipGrowRule 要更新的数据
	 * @return				: int
	 */
	int updateVasVipGrowRule(VasVipGrowRule vasVipGrowRule);

	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasVipGrowRule
	 */
	VasVipGrowRule findVasVipGrowRuleById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGrowRule 检索条件
	 * @return	List<VasVipGrowRule>
	 */
	List<VasVipGrowRule> findVasVipGrowRuleList(VasVipGrowRule vasVipGrowRule);

	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGrowRule 检索条件
	 * @param pager	分页数据
	 * @return	Pager
	 */
	Pager findVasVipGrowRuleList(VasVipGrowRule vasVipGrowRule, Pager pager);

	/**
	 *  @Description    : 通过条件获取成长值规则数量
	 *  @Method_Name    : findVasVipGrowRuleCount
	 *  @param growRule
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 上午11:24:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    Integer findVasVipGrowRuleCount(VasVipGrowRule growRule);

	/**
	 *  @Description    : 保存会员等级成长值规则记录
	 *  @Method_Name    : addVipGrowRule
	 *  @param vasVipGrowRule
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 上午11:24:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity addVipGrowRule(VasVipGrowRule vasVipGrowRule);

	/**
	 *  @Description    : 更新会员成长值规则记录
	 *  @Method_Name    : updateVipGrowRule
	 *  @param vasVipGrowRule
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 上午11:28:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity updateVipGrowRule(VasVipGrowRule vasVipGrowRule);

	/**
	 *  @Description    : 根据用户注册时间获取其对应的会员成长值规则
	 *  @Method_Name    : getVipGrowRuleByRegistTime
	 *  @param registTime
	 *  @return         : List<VasVipGrowRule>
	 *  @Creation Date  : 2018年01月30日 下午16:39:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	List<VasVipGrowRule> getVipGrowRuleByRegistTime(Date registTime);

	/**
	 *  @Description    : 根据会员成长值类型和用户注册时间获取其对应的会员成长值规则
	 *  @Method_Name    : getVipGrowRuleByTypeAndRegistTime
	 *  @param type	 : 获取成长值渠道类型:1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠
	 *  @param registTime
	 *  @return         : VasVipGrowRule
	 *  @Creation Date  : 2018年01月30日 下午16:54:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	VasVipGrowRule getVipGrowRuleByTypeAndRegistTime(int type,Date registTime);
}
