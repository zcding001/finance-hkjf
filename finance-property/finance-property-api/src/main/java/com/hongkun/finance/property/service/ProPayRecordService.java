package com.hongkun.finance.property.service;

import java.util.List;
import java.util.Map;

import org.mengyun.tcctransaction.api.Compensable;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.property.model.ProPayRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.service.ProPayRecordService.java
 * @Class Name    : ProPayRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface ProPayRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param proPayRecord 持久化的数据对象
	 * @return				: void
	 */
	@Compensable
	void insertProPayRecord(ProPayRecord proPayRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<ProPayRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertProPayRecordBatch(List<ProPayRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<ProPayRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertProPayRecordBatch(List<ProPayRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param proPayRecord 要更新的数据
	 * @return				: void
	 */
	void updateProPayRecord(ProPayRecord proPayRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param proPayRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateProPayRecordBatch(List<ProPayRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	ProPayRecord
	 */
	ProPayRecord findProPayRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proPayRecord 检索条件
	 * @return	List<ProPayRecord>
	 */
	List<ProPayRecord> findProPayRecordList(ProPayRecord proPayRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proPayRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<ProPayRecord>
	 */
	List<ProPayRecord> findProPayRecordList(ProPayRecord proPayRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param proPayRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<ProPayRecord>
	 */
	Pager findProPayRecordList(ProPayRecord proPayRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param proPayRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findProPayRecordCount(ProPayRecord proPayRecord);
	/**
	 *  @Description    : 修改缴费记录状态
	 *  @Method_Name    : updateProPayRecordState
	 *  @param proId
	 *  @param state
	 *  @param opinion  审核意见
	 *  @return         : void
	 *  @Creation Date  : 2017年8月11日 下午4:25:16 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Compensable
	void updateProPayRecordState(int proId,int state,String opinion);

	/**
	 *  @Description    : 查询物业缴费记录
	 *  @Method_Name    : findProPayRecordListByPropertyId
	 *  @param id
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月12日 下午1:53:46 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> findProPayRecordListByPropertyId(Integer id, Pager pager);
	/**
	 *  @Description    : 更新缴费记录为已冻结
	 *  @Method_Name    : updateState
	 *  @param params
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月12日 下午1:53:46
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@Compensable
    Integer updateState(Map<String,Object> params);
}
