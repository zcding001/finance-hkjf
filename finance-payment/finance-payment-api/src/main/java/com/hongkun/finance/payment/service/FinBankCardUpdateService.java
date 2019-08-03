package com.hongkun.finance.payment.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.payment.model.FinBankCardUpdate;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.service.FinBankCardUpdateService.java
 * @Class Name    : FinBankCardUpdateService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FinBankCardUpdateService {
	
	/**
	 * @Described			: 单条插入
	 * @param finBankCardUpdate 持久化的数据对象
	 * @return				: void
	 */
	void insertFinBankCardUpdate(FinBankCardUpdate finBankCardUpdate);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FinBankCardUpdate> 批量插入的数据
	 * @return				: void
	 */
	void insertFinBankCardUpdateBatch(List<FinBankCardUpdate> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FinBankCardUpdate> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFinBankCardUpdateBatch(List<FinBankCardUpdate> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param finBankCardUpdate 要更新的数据
	 * @return				: void
	 */
	void updateFinBankCardUpdate(FinBankCardUpdate finBankCardUpdate);
	
	/**
	 * @Described			: 批量更新数据
	 * @param finBankCardUpdate 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFinBankCardUpdateBatch(List<FinBankCardUpdate> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FinBankCardUpdate
	 */
	FinBankCardUpdate findFinBankCardUpdateById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param finBankCardUpdate 检索条件
	 * @return	List<FinBankCardUpdate>
	 */
	List<FinBankCardUpdate> findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate);
	
	/**
	 * @Described			: 条件检索数据
	 * @param finBankCardUpdate 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FinBankCardUpdate>
	 */
	List<FinBankCardUpdate> findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param finBankCardUpdate 检索条件
	 * @param pager	分页数据
	 * @return	List<FinBankCardUpdate>
	 */
	Pager findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param finBankCardUpdate 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFinBankCardUpdateCount(FinBankCardUpdate finBankCardUpdate);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFinBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findFinBankCardUpdateCount(FinBankCardUpdate finBankCardUpdate, String sqlName);

	/**
	 *  @Description    ：条件检索解绑银行卡信息列表
	 *  @Method_Name    ：findBankCardUpdateList
	 *  @param finBankCardUpdate
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager
	 *  @Creation Date  ：2018年05月16日 16:15
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    Pager findBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager);

	/**
	 *  @Description    ：审核银行卡变更信息
	 *  @Method_Name    ：auditBankCardUpdate
	 *  @param finBankCardUpdate
	 *  @return void
	 *  @Creation Date  ：2018年05月16日 16:55
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity<?>  auditBankCardUpdate(FinBankCardUpdate finBankCardUpdate);

	/**
	 *  @Description    ：查询解绑因银行卡详情
	 *  @Method_Name    ：findBankCardUpdateInfo
	 *  @param id
	 *  @return com.hongkun.finance.payment.model.FinBankCardUpdate
	 *  @Creation Date  ：2018年05月16日 17:14
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    FinBankCardUpdate findBankCardUpdateInfo(Integer id);

	/**
	 *  @Description    ：根据用户Id及银行卡Id查询解绑信息
	 *  @Method_Name    ：findFinBankCardUpdateByBankIdAndUserId
	 *  @param id
	 *  @param bankCardId
	 *  @return com.hongkun.finance.payment.model.FinBankCardUpdate
	 *  @Creation Date  ：2018年05月21日 09:29
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    FinBankCardUpdate findFinBankCardUpdateByBankIdAndUserId(Integer id, Integer bankCardId);
}
