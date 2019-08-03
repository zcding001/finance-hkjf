package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointAccountService.java
 * @Class Name    : PointAccountService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointAccountService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointAccount 持久化的数据对象
	 * @return				: void
	 */
	void insertPointAccount(PointAccount pointAccount);

	/**
	 * @Described			: 更新数据
	 * @param pointAccount 要更新的数据
	 * @return				: void
	 */
	int updatePointAccount(PointAccount pointAccount);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointAccount
	 */
	PointAccount findPointAccountById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointAccount 检索条件
	 * @return	List<PointAccount>
	 */
	List<PointAccount> findPointAccountList(PointAccount pointAccount);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointAccount 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointAccount>
	 */
	List<PointAccount> findPointAccountList(PointAccount pointAccount, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointAccount 检索条件
	 * @param pager    分页数据
	 * @return	List<PointAccount>
	 */
	Pager findPointAccountList(PointVO pointAccount, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointAccount 检索条件
	 * @return	int
	 */
	int findPointAccountCount(PointAccount pointAccount);
	/**
	 *  @Description    : 通过userid查询积分账户
	 *  @Method_Name    : findPointAccountByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : PointAccount
	 *  @Creation Date  : 2017年8月16日 下午5:13:08 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	PointAccount findPointAccountByRegUserId(int regUserId);

	/**
	 * 根据现有的积分增加/减少积分值：防止不同数据源的并发
	 * @param pointAccount
	 */
	/**
	 *  @Description    : 根据regUserId更新积分账户
	 *  @Method_Name    : updateByRegUserId
	 *  @return
	 *  @Creation Date  : 2018年04月03日 下午16:16:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    void updateByRegUserId(PointAccount pointAccount);

	/**
	 *  @Description    : 获取当前用户的积分和积分转赠利率
	 *  @Method_Name    : getUserPointAndRate
	 *  @return
	 *  @Creation Date  : 2017年12月28日 下午17:57:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity getUserPointAndRate(int regUserId);

	void updatePointAccountBatch(List<PointAccount> list, int count);
}
