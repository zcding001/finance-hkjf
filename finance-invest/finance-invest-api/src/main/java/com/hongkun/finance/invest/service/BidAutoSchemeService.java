package com.hongkun.finance.invest.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidAutoScheme;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project       : invest
 * @Program Name  : com.hongkun.finance.invest.service.BidAutoSchemeService.java
 * @Class Name    : BidAutoSchemeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidAutoSchemeService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidAutoScheme 持久化的数据对象
	 * @return				: void
	 */
	ResponseEntity<?> insertBidAutoScheme(BidAutoScheme bidAutoScheme);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidAutoScheme> 批量插入的数据
	 * @return				: void
	 */
	void insertBidAutoSchemeBatch(List<BidAutoScheme> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidAutoScheme> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidAutoSchemeBatch(List<BidAutoScheme> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidAutoScheme 要更新的数据
	 * @return				: void
	 */
	ResponseEntity<?> updateBidAutoScheme(BidAutoScheme bidAutoScheme);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidAutoScheme 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidAutoSchemeBatch(List<BidAutoScheme> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidAutoScheme
	 */
	BidAutoScheme findBidAutoSchemeById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAutoScheme 检索条件
	 * @return	List<BidAutoScheme>
	 */
	List<BidAutoScheme> findBidAutoSchemeList(BidAutoScheme bidAutoScheme);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAutoScheme 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidAutoScheme>
	 */
	List<BidAutoScheme> findBidAutoSchemeList(BidAutoScheme bidAutoScheme, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAutoScheme 检索条件
	 * @param pager	分页数据
	 * @return	List<BidAutoScheme>
	 */
	Pager findBidAutoSchemeList(BidAutoScheme bidAutoScheme, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidAutoScheme 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidAutoSchemeCount(BidAutoScheme bidAutoScheme);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findBidAutoSchemeList(BidAutoScheme bidAutoScheme, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findBidAutoSchemeCount(BidAutoScheme bidAutoScheme, String sqlName);
	
	/**
	 *  @Description    : 查询用户的自动投资数据
	 *  @Method_Name    : findBidAutoSchemeByRegUserId
	 *  @param regUserId
	 *  @return         : BidAutoScheme
	 *  @Creation Date  : 2018年3月13日 上午11:22:42 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	BidAutoScheme findBidAutoSchemeByRegUserId(Integer regUserId);

	/**
	 *  @Description    ：获取可用的自动投资方案列表
	 *  @Method_Name    ：findAvailableBidAutoSchemeList
	 *  @param bidAutoScheme
	 *  @return java.util.List<com.hongkun.finance.invest.model.BidAutoScheme>
	 *  @Creation Date  ：2018年08月29日 10:41
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	List<BidAutoScheme> findAvailableBidAutoSchemeList(BidAutoScheme bidAutoScheme);
}
