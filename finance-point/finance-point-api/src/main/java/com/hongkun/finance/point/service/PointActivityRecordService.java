package com.hongkun.finance.point.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointActivityRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointActivityRecordService.java
 * @Class Name    : PointActivityRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointActivityRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointActivityRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertPointActivityRecord(PointActivityRecord pointActivityRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointActivityRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertPointActivityRecordBatch(List<PointActivityRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointActivityRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertPointActivityRecordBatch(List<PointActivityRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param pointActivityRecord 要更新的数据
	 * @return				: void
	 */
	void updatePointActivityRecord(PointActivityRecord pointActivityRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param pointActivityRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updatePointActivityRecordBatch(List<PointActivityRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointActivityRecord
	 */
	PointActivityRecord findPointActivityRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointActivityRecord 检索条件
	 * @return	List<PointActivityRecord>
	 */
	List<PointActivityRecord> findPointActivityRecordList(PointActivityRecord pointActivityRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointActivityRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointActivityRecord>
	 */
	List<PointActivityRecord> findPointActivityRecordList(PointActivityRecord pointActivityRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointActivityRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<PointActivityRecord>
	 */
	Pager findPointActivityRecordList(PointActivityRecord pointActivityRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointActivityRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findPointActivityRecordCount(PointActivityRecord pointActivityRecord);
	/**
	 *  @Description    : 根据商品ID，查询促俏活动记录
	 *  @Method_Name    : findSellingRecordList;
	 *  @param id
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年9月17日 下午2:53:30;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    ResponseEntity<?>  findSellingRecordList(Integer id,Pager pager);
}
