package com.hongkun.finance.contract.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.contract.model.CarContract;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.CarContractService.java
 * @Class Name    : CarContractService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface CarContractService {
	
	/**
	 * @Described			: 单条插入
	 * @param carContract 持久化的数据对象
	 * @return				: void
	 */
	void insertCarContract(CarContract carContract);
	
	/**
	 * @Described			: 批量插入
	 * @param List<CarContract> 批量插入的数据
	 * @return				: void
	 */
	void insertCarContractBatch(List<CarContract> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<CarContract> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertCarContractBatch(List<CarContract> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param carContract 要更新的数据
	 * @return				: void
	 */
	void updateCarContract(CarContract carContract);
	
	/**
	 * @Described			: 批量更新数据
	 * @param carContract 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateCarContractBatch(List<CarContract> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	CarContract
	 */
	CarContract findCarContractById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carContract 检索条件
	 * @return	List<CarContract>
	 */
	List<CarContract> findCarContractList(CarContract carContract);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carContract 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<CarContract>
	 */
	List<CarContract> findCarContractList(CarContract carContract, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carContract 检索条件
	 * @param pager	分页数据
	 * @return	List<CarContract>
	 */
	Pager findCarContractList(CarContract carContract, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param carContract 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findCarContractCount(CarContract carContract);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findCarContractList(CarContract carContract, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findCarContractCount(CarContract carContract, String sqlName);
}
