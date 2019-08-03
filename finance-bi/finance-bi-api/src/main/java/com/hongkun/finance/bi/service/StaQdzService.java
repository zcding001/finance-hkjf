package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaQdz;
import com.hongkun.finance.bi.model.vo.StaQdzInOutVo;
import com.hongkun.finance.bi.model.vo.StaWithdrawVo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaQdzService.java
 * @Class Name    : StaQdzService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaQdzService {
	
	/**
	 * @Described			: 单条插入
	 * @param staQdz 持久化的数据对象
	 * @return				: void
	 */
	void insertStaQdz(StaQdz staQdz);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaQdz> 批量插入的数据
	 * @return				: void
	 */
	void insertStaQdzBatch(List<StaQdz> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaQdz> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertStaQdzBatch(List<StaQdz> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param staQdz 要更新的数据
	 * @return				: void
	 */
	void updateStaQdz(StaQdz staQdz);
	
	/**
	 * @Described			: 批量更新数据
	 * @param staQdz 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateStaQdzBatch(List<StaQdz> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	StaQdz
	 */
	StaQdz findStaQdzById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staQdz 检索条件
	 * @return	List<StaQdz>
	 */
	List<StaQdz> findStaQdzList(StaQdz staQdz);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staQdz 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<StaQdz>
	 */
	List<StaQdz> findStaQdzList(StaQdz staQdz, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staQdz 检索条件
	 * @param pager	分页数据
	 * @return	List<StaQdz>
	 */
	Pager findStaQdzList(StaQdz staQdz, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param staQdz 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findStaQdzCount(StaQdz staQdz);
	/**
     *  @Description    : 查询钱袋子转入转出总金额，总次数
     *  @Method_Name    : findStaQdzSum;
     *  @param staQdz
     *  @return
     *  @return         : StaQdzInOutVo;
     *  @Creation Date  : 2018年9月21日 上午10:15:46;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
	StaQdzInOutVo findStaQdzSum(StaQdz staQdz);
}
