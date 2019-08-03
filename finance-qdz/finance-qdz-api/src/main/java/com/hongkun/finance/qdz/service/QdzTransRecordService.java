package com.hongkun.finance.qdz.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.vo.QdzTransferInfo;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.service.QdzTransRecordService.java
 * @Class Name : QdzTransRecordService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface QdzTransRecordService {

	/**
	 * @Described : 单条插入
	 * @param qdzTransRecord
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertQdzTransRecord(QdzTransRecord qdzTransRecord);

	/**
	 * @Described : 批量插入
	 * @param List<QdzTransRecord>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertQdzTransRecordBatch(List<QdzTransRecord> list);

	/**
	 * @Described : 批量插入
	 * @param List<QdzTransRecord>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertQdzTransRecordBatch(List<QdzTransRecord> list, int count);

	/**
	 * @Described : 更新数据
	 * @param qdzTransRecord
	 *            要更新的数据
	 * @return : void
	 */
	void updateQdzTransRecord(QdzTransRecord qdzTransRecord);

	/**
	 * @Described : 批量更新数据
	 * @param qdzTransRecord
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateQdzTransRecordBatch(List<QdzTransRecord> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return QdzTransRecord
	 */
	QdzTransRecord findQdzTransRecordById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param qdzTransRecord
	 *            检索条件
	 * @return List<QdzTransRecord>
	 */
	List<QdzTransRecord> findQdzTransRecordList(QdzTransRecord qdzTransRecord);

	/**
	 * @Described : 条件检索数据
	 * @param qdzTransRecord
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<QdzTransRecord>
	 */
	List<QdzTransRecord> findQdzTransRecordList(QdzTransRecord qdzTransRecord, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param qdzTransRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<QdzTransRecord>
	 */
	Pager findQdzTransRecordList(QdzTransRecord qdzTransRecord, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param qdzTransRecord
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findQdzTransRecordCount(QdzTransRecord qdzTransRecord);

	/**
	 * @Description :通过用户ID，查询钱袋子记录信息
	 * @Method_Name : findQdzTransRecordByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : QdzTransRecord;
	 * @Creation Date : 2017年7月11日 下午5:53:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	QdzTransRecord findQdzTransRecordByRegUserId(Integer regUserId);

	/**
	 * @Description : 按条件查询当天转入总金额
	 * @Method_Name : findSumTransMoneyOfDay;
	 * @param regUserId
	 * @param type
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月16日 下午8:37:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findSumTransMoneyOfDay(Integer regUserId, Integer type);

	/**
	 * @Description : 按条件查询当月转入次数
	 * @Method_Name : findTransferInTimesOfMonth;
	 * @param regUserId
	 * @param type
	 * @return
	 * @return : Integer;
	 * @Creation Date : 2017年7月16日 下午8:36:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Integer findTransferInTimesOfMonth(Integer regUserId, Integer type);

	/**
	 * @Description : 按照类型，状态 统计某个时间段内交易总金额
	 * @Method_Name : findInvestSumMoney;
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param transTypeEnum
	 *            交易类型
	 * @param state
	 *            状态
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月19日 下午3:18:11;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findInvestSumMoney(Date startTime, Date endTime, TransTypeEnum transTypeEnum, Integer state);

	/**
	 * @Description : 分页查询钱袋子账单流水信息
	 * @Method_Name : findQdzTransferRecordList;
	 * @param QdzTransferInfo
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月20日 上午10:09:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzTransferRecordList(QdzTransferInfo qdzTransferInfo, Pager pager);

	/**
	 * @Description :根据钱袋子记录ID，删除钱袋子记录
	 * @Method_Name : deleteById;
	 * @param qdzRecordId
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月28日 下午4:43:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteById(Integer qdzRecordId);
	/**
     *  @Description    : 按条件查询转入转出人数
     *  @Method_Name    : findTransferUserSum;
     *  @param qdzTransRecord
     *  @return
     *  @return         : Integer;
     *  @Creation Date  : 2018年9月21日 上午11:47:59;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    Integer findTransferUserSum(QdzTransRecord qdzTransRecord);
}
