package com.hongkun.finance.point.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointSignInfo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointSignInfoService.java
 * @Class Name    : PointSignInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointSignInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointSignInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertPointSignInfo(PointSignInfo pointSignInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointSignInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertPointSignInfoBatch(List<PointSignInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointSignInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertPointSignInfoBatch(List<PointSignInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param pointSignInfo 要更新的数据
	 * @return				: void
	 */
	void updatePointSignInfo(PointSignInfo pointSignInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param pointSignInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updatePointSignInfoBatch(List<PointSignInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointSignInfo
	 */
	PointSignInfo findPointSignInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignInfo 检索条件
	 * @return	List<PointSignInfo>
	 */
	List<PointSignInfo> findPointSignInfoList(PointSignInfo pointSignInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointSignInfo>
	 */
	List<PointSignInfo> findPointSignInfoList(PointSignInfo pointSignInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<PointSignInfo>
	 */
	Pager findPointSignInfoList(PointSignInfo pointSignInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointSignInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findPointSignInfoCount(PointSignInfo pointSignInfo);
}
