package com.hongkun.finance.fund.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.fund.model.HouseProIntroduce;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.HouseProIntroduceService.java
 * @Class Name    : HouseProIntroduceService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface HouseProIntroduceService {
	
	/**
	 * @Described			: 单条插入
	 * @param houseProIntroduce 持久化的数据对象
	 * @return				: void
	 */
	void insertHouseProIntroduce(HouseProIntroduce houseProIntroduce);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProIntroduce> 批量插入的数据
	 * @return				: void
	 */
	void insertHouseProIntroduceBatch(List<HouseProIntroduce> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProIntroduce> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertHouseProIntroduceBatch(List<HouseProIntroduce> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param houseProIntroduce 要更新的数据
	 * @return				: void
	 */
	void updateHouseProIntroduce(HouseProIntroduce houseProIntroduce);
	
	/**
	 * @Described			: 批量更新数据
	 * @param houseProIntroduce 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateHouseProIntroduceBatch(List<HouseProIntroduce> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	HouseProIntroduce
	 */
	HouseProIntroduce findHouseProIntroduceById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProIntroduce 检索条件
	 * @return	List<HouseProIntroduce>
	 */
	List<HouseProIntroduce> findHouseProIntroduceList(HouseProIntroduce houseProIntroduce);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProIntroduce 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<HouseProIntroduce>
	 */
	List<HouseProIntroduce> findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProIntroduce 检索条件
	 * @param pager	分页数据
	 * @return	List<HouseProIntroduce>
	 */
	Pager findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param houseProIntroduce 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findHouseProIntroduceCount(HouseProIntroduce houseProIntroduce);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findHouseProIntroduceCount(HouseProIntroduce houseProIntroduce, String sqlName);
}
