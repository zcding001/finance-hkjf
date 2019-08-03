package com.hongkun.finance.payment.service;

import java.util.List;

import com.hongkun.finance.payment.model.FinPlatformPayway;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.FinPlatformPaywayService.java
 * @Class Name : FinPlatformPaywayService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinPlatformPaywayService {

	/**
	 * @Described : 单条插入
	 * @param finPlatformPayway
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertFinPlatformPayway(FinPlatformPayway finPlatformPayway);

	/**
	 * @Described : 批量插入
	 * @param List<FinPlatformPayway>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinPlatformPaywayBatch(List<FinPlatformPayway> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinPlatformPayway>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinPlatformPaywayBatch(List<FinPlatformPayway> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finPlatformPayway
	 *            要更新的数据
	 * @return : void
	 */
	void updateFinPlatformPayway(FinPlatformPayway finPlatformPayway);

	/**
	 * @Described : 批量更新数据
	 * @param finPlatformPayway
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinPlatformPaywayBatch(List<FinPlatformPayway> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinPlatformPayway
	 */
	FinPlatformPayway findFinPlatformPaywayById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finPlatformPayway
	 *            检索条件
	 * @return List<FinPlatformPayway>
	 */
	List<FinPlatformPayway> findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway);

	/**
	 * @Described : 条件检索数据
	 * @param finPlatformPayway
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinPlatformPayway>
	 */
	List<FinPlatformPayway> findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finPlatformPayway
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinPlatformPayway>
	 */
	Pager findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finPlatformPayway
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinPlatformPaywayCount(FinPlatformPayway finPlatformPayway);

	/**
	 * @Description : 查询某个系统的某个平台下有哪几种支付渠道
	 * @Method_Name : findPayChannelInfo;
	 * @param systemTypeEnums
	 *            系统枚举类
	 * @param platformSourceEnums
	 *            平台枚举类
	 * @return
	 * @return : List<FinPlatformPayway>;
	 * @Creation Date : 2017年12月6日 下午1:43:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<FinPlatformPayway> findPayChannelInfo(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums);

}
