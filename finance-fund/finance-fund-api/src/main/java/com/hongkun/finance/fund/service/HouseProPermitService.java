package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.HouseProPermit;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.HouseProPermitService.java
 * @Class Name    : HouseProPermitService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface HouseProPermitService {
	
	/**
	 * @Described			: 单条插入
	 * @param houseProPermit 持久化的数据对象
	 * @return				: void
	 */
	Integer insertHouseProPermit(HouseProPermit houseProPermit);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProPermit> 批量插入的数据
	 * @return				: void
	 */
	void insertHouseProPermitBatch(List<HouseProPermit> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProPermit> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertHouseProPermitBatch(List<HouseProPermit> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param houseProPermit 要更新的数据
	 * @return				: void
	 */
	void updateHouseProPermit(HouseProPermit houseProPermit);
	
	/**
	 * @Described			: 批量更新数据
	 * @param houseProPermit 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateHouseProPermitBatch(List<HouseProPermit> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	HouseProPermit
	 */
	HouseProPermit findHouseProPermitById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPermit 检索条件
	 * @return	List<HouseProPermit>
	 */
	List<HouseProPermit> findHouseProPermitList(HouseProPermit houseProPermit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPermit 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<HouseProPermit>
	 */
	List<HouseProPermit> findHouseProPermitList(HouseProPermit houseProPermit, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPermit 检索条件
	 * @param pager	分页数据
	 * @return	List<HouseProPermit>
	 */
	Pager findHouseProPermitList(HouseProPermit houseProPermit, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param houseProPermit 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findHouseProPermitCount(HouseProPermit houseProPermit);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findHouseProPermitList(HouseProPermit houseProPermit, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findHouseProPermitCount(HouseProPermit houseProPermit, String sqlName);

	/**
	 *  @Description    ：删除房产预售证记录
	 *  @Method_Name    ：deleteHousePermit
	 *  @param id 预售证id
	 *  @return java.lang.Integer
	 *  @Creation Date  ：2018/10/15
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	Integer deleteHousePermit(int id);
}
