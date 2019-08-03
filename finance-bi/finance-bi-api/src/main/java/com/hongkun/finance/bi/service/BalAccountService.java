package com.hongkun.finance.bi.service;

import java.util.List;

import com.hongkun.finance.bi.model.BalAccountRecord;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.BalAccount;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.BalAccountService.java
 * @Class Name    : BalAccountService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BalAccountService {
	
	/**
	 * @Described			: 单条插入
	 * @param balAccount 持久化的数据对象
	 * @return				: void
	 */
	void insertBalAccount(BalAccount balAccount);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BalAccount> 批量插入的数据
	 * @return				: void
	 */
	void insertBalAccountBatch(List<BalAccount> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BalAccount> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBalAccountBatch(List<BalAccount> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param balAccount 要更新的数据
	 * @return				: void
	 */
	void updateBalAccount(BalAccount balAccount);
	
	/**
	 * @Described			: 批量更新数据
	 * @param balAccount 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBalAccountBatch(List<BalAccount> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BalAccount
	 */
	BalAccount findBalAccountById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccount 检索条件
	 * @return	List<BalAccount>
	 */
	List<BalAccount> findBalAccountList(BalAccount balAccount);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccount 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BalAccount>
	 */
	List<BalAccount> findBalAccountList(BalAccount balAccount, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccount 检索条件
	 * @param pager	分页数据
	 * @return	List<BalAccount>
	 */
	Pager findBalAccountList(BalAccount balAccount, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param balAccount 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBalAccountCount(BalAccount balAccount);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findBalAccountList(BalAccount balAccount, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findBalAccountCount(BalAccount balAccount, String sqlName);
	/**
	*  @Description    ：通过userId查询用户对账信息
	*  @Method_Name    ：findBalAccountByRegUserId
	*  @param regUserId
	*  @return com.hongkun.finance.bi.model.BalAccount
	*  @Creation Date  ：2018/5/2
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    BalAccount findBalAccountByRegUserId(Integer regUserId);

    /**
    *  @Description    ：批处理对账信息
    *  @Method_Name    ：dealBatchForBalance
    *  @param updateList  需要更新的对账集合
    *  @param addList   需要插入的对账集合
    *  @param recordList  需要插入的对账历史记录集合
    *  @return void
    *  @Creation Date  ：2018/5/2
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    void dealBatchForBalance(List<BalAccount> updateList, List<BalAccount> addList, List<BalAccountRecord> recordList);
	/**
	*  @Description    ：处理对账信息
	*  @Method_Name    ：insertBalAccountForBalance
	*  @param addAccount 需要插入的对账记录
	*  @param updateAccount 需要更新的对账记录
	*  @param record   需要插入的对账历史记录
	*  @return void
	*  @Creation Date  ：2018/5/2
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    void insertBalAccountForBalance(BalAccount addAccount, BalAccount updateAccount, BalAccountRecord record);
}
