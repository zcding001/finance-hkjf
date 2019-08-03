package com.hongkun.finance.payment.service;

import java.util.List;

import com.hongkun.finance.payment.model.FinPayConfig;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinPayConfigService.java
 * @Class Name : FinPayConfigService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinPayConfigService {

	/**
	 * @Described : 单条插入
	 * @param finPayConfig
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertFinPayConfig(FinPayConfig finPayConfig);

	/**
	 * @Described : 批量插入
	 * @param List<FinPayConfig>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinPayConfigBatch(List<FinPayConfig> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinPayConfig>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinPayConfigBatch(List<FinPayConfig> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finPayConfig
	 *            要更新的数据
	 * @return : void
	 */
	void updateFinPayConfig(FinPayConfig finPayConfig);

	/**
	 * @Described : 批量更新数据
	 * @param finPayConfig
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinPayConfigBatch(List<FinPayConfig> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinPayConfig
	 */
	FinPayConfig findFinPayConfigById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finPayConfig
	 *            检索条件
	 * @return List<FinPayConfig>
	 */
	List<FinPayConfig> findFinPayConfigList(FinPayConfig finPayConfig);

	/**
	 * @Described : 条件检索数据
	 * @param finPayConfig
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinPayConfig>
	 */
	List<FinPayConfig> findFinPayConfigList(FinPayConfig finPayConfig, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finPayConfig
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinPayConfig>
	 */
	Pager findFinPayConfigList(FinPayConfig finPayConfig, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finPayConfig
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinPayConfigCount(FinPayConfig finPayConfig);

	/**
	 * @Description :
	 * @Method_Name : findPayConfigInfo;
	 * @param systemTypeName
	 *            系统名称
	 * @param platformSourceName
	 *            平台名称
	 * @param payChannel
	 *            支付渠道
	 * @param payStyle
	 *            支付方式
	 * @return
	 * @return : FinPayConfig;
	 * @Creation Date : 2017年6月15日 上午9:57:31;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinPayConfig findPayConfigInfo(String systemTypeName, String platformSourceName, String payChannel,
			String payStyle);
}
