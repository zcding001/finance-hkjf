package com.hongkun.finance.payment.service;

import java.util.List;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinBankRefer;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinBankReferService.java
 * @Class Name : FinBankReferService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinBankReferService {

	/**
	 * @Described : 单条插入
	 * @param finBankRefer
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertFinBankRefer(FinBankRefer finBankRefer);

	/**
	 * @Described : 批量插入
	 * @param List<FinBankRefer>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertFinBankReferBatch(List<FinBankRefer> list);

	/**
	 * @Described : 批量插入
	 * @param List<FinBankRefer>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertFinBankReferBatch(List<FinBankRefer> list, int count);

	/**
	 * @Described : 更新数据
	 * @param finBankRefer
	 *            要更新的数据
	 * @return : void
	 */
	ResponseEntity<?> updateFinBankRefer(FinBankRefer finBankRefer);

	/**
	 * @Described : 批量更新数据
	 * @param finBankRefer
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateFinBankReferBatch(List<FinBankRefer> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinBankRefer
	 */
	FinBankRefer findFinBankReferById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finBankRefer
	 *            检索条件
	 * @return List<FinBankRefer>
	 */
	List<FinBankRefer> findFinBankReferList(FinBankRefer finBankRefer);

	/**
	 * @Described : 条件检索数据
	 * @param finBankRefer
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinBankRefer>
	 */
	List<FinBankRefer> findFinBankReferList(FinBankRefer finBankRefer, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finBankRefer
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinBankRefer>
	 */
	Pager findFinBankReferList(FinBankRefer finBankRefer, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param finBankRefer
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findFinBankReferCount(FinBankRefer finBankRefer);

	/**
	 * @Description : 查询各渠道公共的银行卡信息，用作充值展示
	 * @Method_Name : findBankInfo;
	 * @param payChannel
	 *            PayChannelEnum 支付渠道
	 * @param payStyleCode
	 *            PayStyleEnum 支付方式
	 * @param userType
	 *            用户类型
	 * @return
	 * @return : List<FinBankRefer>;
	 * @Creation Date : 2017年12月25日 上午11:05:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<FinBankRefer> findBankInfo(String payChannel, String payStyleCode, String userType);

	/**
	 * @Description : 查询指定交易渠道下的对应的银行信息
	 * @Method_Name : findBankRefer;
	 * @param payChannelEnum
	 *            支付渠道
	 * @param bankCode
	 *            第三方 银行CODE
	 * @param payStyleEnum
	 *            支付方式
	 * @param userType
	 *            用户类型
	 * @return
	 * @return : FinBankRefer;
	 * @Creation Date : 2017年12月11日 下午2:22:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinBankRefer findBankRefer(PayChannelEnum payChannelEnum, String bankCode, PayStyleEnum payStyleEnum,
			Integer userType);

	/**
	 * @Description : 查询银行维护搜索条件
	 * @Method_Name : findBankCondition;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月11日 上午10:33:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findBankCondition();

}
