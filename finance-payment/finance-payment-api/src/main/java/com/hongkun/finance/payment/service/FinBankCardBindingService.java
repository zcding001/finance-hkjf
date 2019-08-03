package com.hongkun.finance.payment.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.payment.model.FinBankCardBinding;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.FinBankCardBindingService.java
 * @Class Name : FinBankCardBindingService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinBankCardBindingService {

	/**
	 * @Described : 单条插入
	 * @param finBankCardBinding
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertFinBankCardBinding(FinBankCardBinding finBankCardBinding);

	/**
	 * @Described : 批量插入
	 * @param List<FinBankCardBinding>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinBankCardBindingBatch(List<FinBankCardBinding> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinBankCardBinding>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinBankCardBindingBatch(List<FinBankCardBinding> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finBankCardBinding
	 *            要更新的数据
	 * @return : void
	 */
	void updateFinBankCardBinding(FinBankCardBinding finBankCardBinding);

	/**
	 * @Described : 批量更新数据
	 * @param finBankCardBinding
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinBankCardBindingBatch(List<FinBankCardBinding> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinBankCardBinding
	 */
	FinBankCardBinding findFinBankCardBindingById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCardBinding
	 *            检索条件
	 * @return List<FinBankCardBinding>
	 */
	List<FinBankCardBinding> findFinBankCardBindingList(FinBankCardBinding finBankCardBinding);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCardBinding
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinBankCardBinding>
	 */
	List<FinBankCardBinding> findFinBankCardBindingList(FinBankCardBinding finBankCardBinding, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCardBinding
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinBankCardBinding>
	 */
	Pager findFinBankCardBindingList(FinBankCardBinding finBankCardBinding, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finBankCardBinding
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinBankCardBindingCount(FinBankCardBinding finBankCardBinding);

	/**
	 * @Description : 查询用户银行卡信息
	 * @Method_Name : findBankCardBinding;
	 * @param bankCardId
	 *            银行卡ID
	 * @param regUserId
	 *            用户Id
	 * @param payChannel
	 *            支付渠道
	 * @return
	 * @return : FinBankCardBinding;
	 * @Creation Date : 2018年4月4日 下午1:01:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinBankCardBinding findBankCardBinding(Integer bankCardId, Integer regUserId, Integer payChannel);

	/**
	 *  @Description    ：根据id查询银行卡信息
	 *  @Method_Name    ：updateFinBankCardBindingByCardId
	 *  @param bankCardId
	 *  @param state
	 *  @return int
	 *  @Creation Date  ：2018年05月18日 10:45
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    int updateFinBankCardBindingByCardId(Integer bankCardId, Integer state);
}
