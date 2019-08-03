package com.hongkun.finance.user.service;

import java.util.List;

import com.hongkun.finance.user.model.RegUserRecommend;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.RegUserRecommendService.java
 * @Class Name : RegUserRecommendService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface RegUserRecommendService {

	/**
	 * @Described : 单条插入
	 * @param regUserRecommend
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertRegUserRecommend(RegUserRecommend regUserRecommend);

	/**
	 * @Described : 批量插入
	 * @param List<RegUserRecommend>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertRegUserRecommendBatch(List<RegUserRecommend> list);

	/**
	 * @Described : 批量插入
	 * @param List<RegUserRecommend>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertRegUserRecommendBatch(List<RegUserRecommend> list, int count);

	/**
	 * @Described : 更新数据
	 * @param regUserRecommend
	 *            要更新的数据
	 * @return : void
	 */
	void updateRegUserRecommend(RegUserRecommend regUserRecommend);

	/**
	 * @Described : 批量更新数据
	 * @param regUserRecommend
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateRegUserRecommendBatch(List<RegUserRecommend> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return RegUserRecommend
	 */
	RegUserRecommend findRegUserRecommendById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param regUserRecommend
	 *            检索条件
	 * @return List<RegUserRecommend>
	 */
	List<RegUserRecommend> findRegUserRecommendList(RegUserRecommend regUserRecommend);

	/**
	 * @Described : 条件检索数据
	 * @param regUserRecommend
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<RegUserRecommend>
	 */
	List<RegUserRecommend> findRegUserRecommendList(RegUserRecommend regUserRecommend, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param regUserRecommend
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<RegUserRecommend>
	 */
	Pager findRegUserRecommendList(RegUserRecommend regUserRecommend, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param regUserRecommend
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findRegUserRecommendCount(RegUserRecommend regUserRecommend);

	/**
	 * @Described : 通过regUserId查询数据
	 * @param regUserId
	 * @return RegUserRecommend
	 */
	RegUserRecommend findRegUserRecommendByRegUserId(Integer regUserId);
}
