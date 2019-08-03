package com.hongkun.finance.property.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.property.model.ProAddress;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.service.ProAddressService.java
 * @Class Name    : ProAddressService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface ProAddressService {
	
	/**
	 * @Described			: 单条插入
	 * @param proAddress 持久化的数据对象
	 * @return				: void
	 */
	int insertProAddress(ProAddress proAddress);
	
	/**
	 * @Described			: 批量插入
	 * @param List<ProAddress> 批量插入的数据
	 * @return				: void
	 */
	void insertProAddressBatch(List<ProAddress> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<ProAddress> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertProAddressBatch(List<ProAddress> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param proAddress 要更新的数据
	 * @return				: void
	 */
	void updateProAddress(ProAddress proAddress);
	
	/**
	 * @Described			: 批量更新数据
	 * @param proAddress 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateProAddressBatch(List<ProAddress> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	ProAddress
	 */
	ProAddress findProAddressById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proAddress 检索条件
	 * @return	List<ProAddress>
	 */
	List<ProAddress> findProAddressList(ProAddress proAddress);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proAddress 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<ProAddress>
	 */
	List<ProAddress> findProAddressList(ProAddress proAddress, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proAddress 检索条件
	 * @param pager	分页数据
	 * @return	List<ProAddress>
	 */
	Pager findProAddressList(ProAddress proAddress, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param proAddress 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findProAddressCount(ProAddress proAddress);
	/**
	 * @Described			: 删除物业账户收获信息地址
	 * @Described
	 * @param 	proAddressId	信息地址id
	 * @return
	 */
	int delProAddress(int proAddressId);
}
