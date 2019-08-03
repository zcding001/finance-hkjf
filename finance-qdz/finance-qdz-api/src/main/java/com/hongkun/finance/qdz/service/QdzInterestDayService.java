package com.hongkun.finance.qdz.service;

import java.util.List;

import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.service.QdzInterestDayService.java
 * @Class Name : QdzInterestDayService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface QdzInterestDayService {

	/**
	 * @Described : 单条插入
	 * @param qdzInterestDay
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertQdzInterestDay(QdzInterestDay qdzInterestDay);

	/**
	 * @Described : 批量插入
	 * @param List<QdzInterestDay>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertQdzInterestDayBatch(List<QdzInterestDay> list);

	/**
	 * @Described : 批量插入
	 * @param List<QdzInterestDay>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertQdzInterestDayBatch(List<QdzInterestDay> list, int count);

	/**
	 * @Described : 更新数据
	 * @param qdzInterestDay
	 *            要更新的数据
	 * @return : void
	 */
	void updateQdzInterestDay(QdzInterestDay qdzInterestDay);

	/**
	 * @Described : 批量更新数据
	 * @param qdzInterestDay
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateQdzInterestDayBatch(List<QdzInterestDay> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return QdzInterestDay
	 */
	QdzInterestDay findQdzInterestDayById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDay
	 *            检索条件
	 * @return List<QdzInterestDay>
	 */
	List<QdzInterestDay> findQdzInterestDayList(QdzInterestDay qdzInterestDay);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDay
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<QdzInterestDay>
	 */
	List<QdzInterestDay> findQdzInterestDayList(QdzInterestDay qdzInterestDay, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDay
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<QdzInterestDay>
	 */
	Pager findQdzInterestDayList(QdzInterestDay qdzInterestDay, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param qdzInterestDay
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findQdzInterestDayCount(QdzInterestDay qdzInterestDay);

	/**
	 * @Description : 分页查询钱袋子每日利息
	 * @Method_Name : findQdzInterestDayInfo;
	 * @param qdzInterestDay
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月18日 下午5:57:47;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzInterestDayInfo(QdzInterestDay qdzInterestDay, Pager pager);
}
