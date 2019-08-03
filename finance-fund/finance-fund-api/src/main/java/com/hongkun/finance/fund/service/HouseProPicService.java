package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.HouseProPic;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.HouseProPicService.java
 * @Class Name    : HouseProPicService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface HouseProPicService {
	
	/**
	 * @Described			: 单条插入
	 * @param houseProPic 持久化的数据对象
	 * @return				: void
	 */
	Integer insertHouseProPic(HouseProPic houseProPic);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProPic> 批量插入的数据
	 * @return				: void
	 */
	void insertHouseProPicBatch(List<HouseProPic> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProPic> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertHouseProPicBatch(List<HouseProPic> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param houseProPic 要更新的数据
	 * @return				: void
	 */
	void updateHouseProPic(HouseProPic houseProPic);
	
	/**
	 * @Described			: 批量更新数据
	 * @param houseProPic 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateHouseProPicBatch(List<HouseProPic> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	HouseProPic
	 */
	HouseProPic findHouseProPicById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPic 检索条件
	 * @return	List<HouseProPic>
	 */
	List<HouseProPic> findHouseProPicList(HouseProPic houseProPic);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPic 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<HouseProPic>
	 */
	List<HouseProPic> findHouseProPicList(HouseProPic houseProPic, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProPic 检索条件
	 * @param pager	分页数据
	 * @return	List<HouseProPic>
	 */
	Pager findHouseProPicList(HouseProPic houseProPic, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param houseProPic 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findHouseProPicCount(HouseProPic houseProPic);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findHouseProPicList(HouseProPic houseProPic, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findHouseProPicCount(HouseProPic houseProPic, String sqlName);

	/**
	 *  @Description    ：删除房产图片记录
	 *  @Method_Name    ：deleteHousePic
	 *  @param id  房产图片记录id
	 *  @return java.lang.Integer
	 *  @Creation Date  ：2018/10/15
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	Integer deleteHousePic(int id);
}
