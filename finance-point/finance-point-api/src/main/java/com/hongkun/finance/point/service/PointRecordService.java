package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointRecordService.java
 * @Class Name    : PointRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointRecord 持久化的数据对象
	 * @return				: void
	 */
	int insertPointRecord(PointRecord pointRecord);
	

	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointRecord
	 */
	PointRecord findPointRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRecord 检索条件
	 * @return	List<PointRecord>
	 */
	List<PointRecord> findPointRecordList(PointRecord pointRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointRecord>
	 */
	List<PointRecord> findPointRecordList(PointRecord pointRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<PointRecord>
	 */
	Pager findPointRecordList(PointRecord pointRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointRecord 检索条件
	 * @return	int
	 */
	int findPointRecordCount(PointRecord pointRecord);

	/**
	 * 查询积分的记录
	 * @param pointVO
	 * @param pager
	 * @return
	 */
    Pager listPointRecord(PointVO pointVO, Pager pager);
    
    /**
     *  @Description    : 积分划转
     *  @Method_Name    : transferPoints
     *  @param points   划转积分（如果是扣除传递负数）
     *  @param userId   用户id
     *  @param type     操作类型
     *  @param businessId 业务id
     *  @param comments 业务交易说明
     *  @return
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2017年8月16日 上午10:43:59 
     *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    @Compensable
    ResponseEntity<?> transferPoints(int points,BigDecimal worth,int userId,int type,Integer businessId, String comments);

	/**
	 *  @Description    : 更新记录的状态
	 *  @Method_Name    : updateRecordState
	 *  @param pointVO        :被更新的积分VO
	 *  @param currentUserId : 当前用户ID
	 *  @return           ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
    ResponseEntity updateRecordState(PointVO pointVO, Integer currentUserId);

	void insertPointRecordBatch(List<PointRecord> list);
}
