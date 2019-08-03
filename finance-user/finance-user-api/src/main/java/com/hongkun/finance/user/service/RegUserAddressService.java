package com.hongkun.finance.user.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.RegUserAddress;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserAddressService.java
 * @Class Name    : RegUserAddressService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserAddressService {
	
	/**
	 * @Described			: 单条插入
	 * @param regUserAddress 持久化的数据对象
	 * @return				: void
	 */
	void insertRegUserAddress(RegUserAddress regUserAddress);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserAddress> 批量插入的数据
	 * @return				: void
	 */
	void insertRegUserAddressBatch(List<RegUserAddress> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserAddress> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRegUserAddressBatch(List<RegUserAddress> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param regUserAddress 要更新的数据
	 * @return				: int
	 */
	int updateRegUserAddress(RegUserAddress regUserAddress);
	
	/**
	 * @Described			: 批量更新数据
	 * @param regUserAddress 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRegUserAddressBatch(List<RegUserAddress> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RegUserAddress
	 */
	RegUserAddress findRegUserAddressById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserAddress 检索条件
	 * @return	List<RegUserAddress>
	 */
	List<RegUserAddress> findRegUserAddressList(RegUserAddress regUserAddress);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserAddress 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<RegUserAddress>
	 */
	List<RegUserAddress> findRegUserAddressList(RegUserAddress regUserAddress, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserAddress 检索条件
	 * @param pager	分页数据
	 * @return	List<RegUserAddress>
	 */
	Pager findRegUserAddressList(RegUserAddress regUserAddress, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param regUserAddress 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRegUserAddressCount(RegUserAddress regUserAddress);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRegUserAddressList(RegUserAddress regUserAddress, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findRegUserAddressCount(RegUserAddress regUserAddress, String sqlName);

	/**
	 * 设置用户默认地址
	 * @param userId         	  用户id
	 * @param addressId         收货地址id
	 * @return : ResponseEntity
	 * @Description : 设置用户默认地址
	 * @Method_Name : setDefaultAddress
	 * @Creation Date  : 2018年01月09日 下午17:45:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	ResponseEntity setDefaultAddress(Integer userId, int addressId);

	/**
	 * 删除用户收货地址
	 * @param userId         		用户id
	 * @param addressId         收货地址id
	 * @return : ResponseEntity
	 * @Description : 设置用户默认地址
	 * @Method_Name : delAddress
	 * @Creation Date  : 2018年01月10日 上午11:13:50
	 * @Author : pengwu@hongkun.com.cn
	 */
    ResponseEntity delAddress(Integer userId, int addressId);

	/**
	 * 更新用户的收货地址
	 * @param userId              用户id
	 * @param regUserAddress     收货地址信息
	 * @return : ResponseEntity
	 * @Description : 更新用户的收货地址
	 * @Method_Name : updateUserAddress
	 * @Creation Date  : 2018年01月10日 上午11:26:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	ResponseEntity updateRegUserAddress(Integer userId, RegUserAddress regUserAddress);
}
