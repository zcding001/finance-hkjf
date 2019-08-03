package com.hongkun.finance.qdz.service;

import java.util.Date;
import java.util.List;

import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.service.QdzRateRecordService.java
 * @Class Name : QdzRateRecordService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface QdzRateRecordService {

	/**
	 * @Described : 单条插入
	 * @param qdzRateRecord
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertQdzRateRecord(QdzRateRecord qdzRateRecord);

	/**
	 * @Described : 批量插入
	 * @param List<QdzRateRecord>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertQdzRateRecordBatch(List<QdzRateRecord> list);

	/**
	 * @Described : 批量插入
	 * @param List<QdzRateRecord>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertQdzRateRecordBatch(List<QdzRateRecord> list, int count);

	/**
	 * @Described : 更新数据
	 * @param qdzRateRecord
	 *            要更新的数据
	 * @return : void
	 */
	void updateQdzRateRecord(QdzRateRecord qdzRateRecord);

	/**
	 * @Described : 批量更新数据
	 * @param qdzRateRecord
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateQdzRateRecordBatch(List<QdzRateRecord> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return QdzRateRecord
	 */
	QdzRateRecord findQdzRateRecordById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param qdzRateRecord
	 *            检索条件
	 * @return List<QdzRateRecord>
	 */
	List<QdzRateRecord> findQdzRateRecordList(QdzRateRecord qdzRateRecord);

	/**
	 * @Described : 条件检索数据
	 * @param qdzRateRecord
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<QdzRateRecord>
	 */
	List<QdzRateRecord> findQdzRateRecordList(QdzRateRecord qdzRateRecord, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param qdzRateRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<QdzRateRecord>
	 */
	Pager findQdzRateRecordList(QdzRateRecord qdzRateRecord, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param qdzRateRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findQdzRateRecordCount(QdzRateRecord qdzRateRecord);
	/**
	 * 
	 *  @Description    : 根据时间查询利率规则
	 *  @Method_Name    : findQdzRateRecordByTime
	 *  @param createTime
	 *  @return
	 *  @return         : QdzRateRecord
	 *  @Creation Date  : 2018年3月16日 下午6:28:23 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	QdzRateRecord findQdzRateRecordByTime(Date createTime);
	/**
	 * 
	 *  @Description    : 根据时间查询利率规则
	 *  @Method_Name    : getQdzRateRecord
	 *  @param createTime
	 *  @return
	 *  @return         : QdzRateRecord
	 *  @Creation Date  : 2018年11月20日 下午6:28:23 
	 *  @Author         : xinbangcao@honghun.com.cn 曹新帮
	 */
	QdzRateRecord getQdzRateRecord(Date day);

}
