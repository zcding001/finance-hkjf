package com.hongkun.finance.payment.service;

import java.util.List;

import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinChannelGrantService.java
 * @Class Name : FinChannelGrantService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinChannelGrantService {

	/**
	 * @Described : 单条插入
	 * @param finChannelGrant
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertFinChannelGrant(FinChannelGrant finChannelGrant);

	/**
	 * @Described : 批量插入
	 * @param List<FinChannelGrant>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinChannelGrantBatch(List<FinChannelGrant> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinChannelGrant>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinChannelGrantBatch(List<FinChannelGrant> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finChannelGrant
	 *            要更新的数据
	 * @return : void
	 */
	void updateFinChannelGrant(FinChannelGrant finChannelGrant);

	/**
	 * @Described : 批量更新数据
	 * @param finChannelGrant
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinChannelGrantBatch(List<FinChannelGrant> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinChannelGrant
	 */
	FinChannelGrant findFinChannelGrantById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finChannelGrant
	 *            检索条件
	 * @return List<FinChannelGrant>
	 */
	List<FinChannelGrant> findFinChannelGrantList(FinChannelGrant finChannelGrant);

	/**
	 * @Described : 条件检索数据
	 * @param finChannelGrant
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinChannelGrant>
	 */
	List<FinChannelGrant> findFinChannelGrantList(FinChannelGrant finChannelGrant, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finChannelGrant
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinChannelGrant>
	 */
	Pager findFinChannelGrantList(FinChannelGrant finChannelGrant, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finChannelGrant
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinChannelGrantCount(FinChannelGrant finChannelGrant);

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
	 * @Description : 查询优先级最高的支付渠道
	 * @Method_Name : findFirstFinChannelGrant;
	 * @param systemTypeEnums
	 *            系统类型
	 * @param platformSourceEnums
	 *            平台来源
	 * @param payStyleEnum
	 *            支付方式
	 * @return
	 * @return : FinChannelGrant;
	 * @Creation Date : 2018年5月17日 上午9:49:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum);
}
