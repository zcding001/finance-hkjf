package com.hongkun.finance.qdz.service;

import java.util.Date;
import java.util.List;

import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.hongkun.finance.qdz.vo.QdzInterestDetailInfoVo;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.service.QdzInterestDayDetailService.java
 * @Class Name : QdzInterestDayDetailService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface QdzInterestDayDetailService {

	/**
	 * @Described : 单条插入
	 * @param qdzInterestDayDetail
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertQdzInterestDayDetail(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Described : 批量插入
	 * @param List<QdzInterestDayDetail>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list);

	/**
	 * @Described : 批量插入
	 * @param List<QdzInterestDayDetail>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list, int count);

	/**
	 * @Described : 更新数据
	 * @param qdzInterestDayDetail
	 *            要更新的数据
	 * @return : void
	 */
	int updateQdzInterestDayDetail(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Described : 批量更新数据
	 * @param qdzInterestDayDetail
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return QdzInterestDayDetail
	 */
	QdzInterestDayDetail findQdzInterestDayDetailById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail, int start,
			int limit);

	/**
	 * @Described : 条件检索数据
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<QdzInterestDayDetail>
	 */
	Pager findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findQdzInterestDayDetailCount(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Description : 根据条件查询钱袋子明细信息
	 * @Method_Name : findQdzInterestDayDetailInfo;
	 * @param qdzInterestDetailInfoVo
	 * @return
	 * @return : List<QdzInterestDayDetail>;
	 * @Creation Date : 2017年7月18日 下午5:59:11;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<QdzInterestDayDetail> findQdzInterestDayDetailInfo(QdzInterestDetailInfoVo qdzInterestDetailInfoVo);

	/**
	 * @Description : 查询钱袋子明细信息，返回List
	 * @Method_Name : findQdzInterestDetails;
	 * @param qdzInterestDetailInfoVo
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月19日 上午10:47:13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzInterestDetails(QdzInterestDetailInfoVo qdzInterestDetailInfoVo, Pager pager);

	/**
	 * @Described : 查询钱袋子每日利息成功的明细
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findSuccQdzInterestDayDetails(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Described : 查询钱袋子每日利息成功的明细通过分片
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findQdzInterestDayDetailsByShardingItem(Date day, int shardingItem);
}
