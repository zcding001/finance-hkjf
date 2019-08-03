package com.hongkun.finance.contract.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.contract.model.CarInfo;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.CarInfoService.java
 * @Class Name    : CarInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface CarInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param carInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertCarInfo(CarInfo carInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<CarInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertCarInfoBatch(List<CarInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<CarInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertCarInfoBatch(List<CarInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param carInfo 要更新的数据
	 * @return				: void
	 */
	void updateCarInfo(CarInfo carInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param carInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateCarInfoBatch(List<CarInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	CarInfo
	 */
	CarInfo findCarInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carInfo 检索条件
	 * @return	List<CarInfo>
	 */
	List<CarInfo> findCarInfoList(CarInfo carInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<CarInfo>
	 */
	List<CarInfo> findCarInfoList(CarInfo carInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param carInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<CarInfo>
	 */
	Pager findCarInfoList(CarInfo carInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param carInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findCarInfoCount(CarInfo carInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findCarInfoList(CarInfo carInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findCarInfoCount(CarInfo carInfo, String sqlName);

	/**
	 * @Description :根据汽车ID，删除汽车信息
	 * @Method_Name : deleteCarInfoById;
	 * @param carInfoId
	 * @return
	 * @return : int;
	 * @Creation Date : 2018年8月6日 下午4:43:45;
	 * @Author : hanghe@hongkun.com.cn 何杭;
	 */
	int deleteCarInfoById(Integer carInfoId);
}
