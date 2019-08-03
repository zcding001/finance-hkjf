package com.hongkun.finance.invest.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.vo.BidMatchVo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.BidMatchService.java
 * @Class Name    : BidMatchService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidMatchService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidMatch 持久化的数据对象
	 * @return				: void
	 */
	void insertBidMatch(BidMatch bidMatch);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidMatch> 批量插入的数据
	 * @return				: void
	 */
	void insertBidMatchBatch(List<BidMatch> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidMatch> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidMatchBatch(List<BidMatch> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidMatch 要更新的数据
	 * @return				: void
	 */
	void updateBidMatch(BidMatch bidMatch);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidMatch 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidMatchBatch(List<BidMatch> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidMatch
	 */
	BidMatch findBidMatchById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidMatch 检索条件
	 * @return	List<BidMatch>
	 */
	List<BidMatch> findBidMatchList(BidMatch bidMatch);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidMatch 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidMatch>
	 */
	List<BidMatch> findBidMatchList(BidMatch bidMatch, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidMatch 检索条件
	 * @param pager	分页数据
	 * @return	List<BidMatch>
	 */
	Pager findBidMatchList(BidMatch bidMatch, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidMatch 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidMatchCount(BidMatch bidMatch);
	/**
	 *  @Description    : 查询散标的匹配记录
	 *  @Method_Name    : findBidMatchListByCommonBidId
	 *  @param bidInfoId
	 *  @return
	 *  @return         : List<BidMatch>
	 *  @Creation Date  : 2017年7月19日 下午2:33:13 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidMatch> findBidMatchListByCommonBidId(Integer bidInfoId);
	/**
	 *  @Description    : 查询匹配详情
	 *  @Method_Name    : findMatchListByContidion
	 *  @param contidion
	 *  @return
	 *  @return         : List<BidMatchVo>
	 *  @Creation Date  : 2017年7月20日 下午5:27:58 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidMatchVo> findMatchListByContidion(BidMatchVo contidion);
	
	Pager findMatchVoListByContidion(BidMatchVo contidion,Pager pager);

	/**
	*  @Description    ：根据优选标的id查询匹配记录
	*  @Method_Name    ：findBidMatchListByGoodIds
	*  @param goodIds
	*  @return java.util.List<com.hongkun.finance.invest.model.BidMatch>
	*  @Creation Date  ：2019/1/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<BidMatch> findBidMatchListByGoodIds(List<Integer> goodIds);
	/**
	*  @Description    ：查询散标的匹配记录
	*  @Method_Name    ：findBidMatchListByCommonBidIds
	*  @param commonIds
	*  @return java.util.List<com.hongkun.finance.invest.model.BidMatch>
	*  @Creation Date  ：2019/1/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<BidMatch> findBidMatchListByCommonBidIds(List<Integer> commonIds);
}
