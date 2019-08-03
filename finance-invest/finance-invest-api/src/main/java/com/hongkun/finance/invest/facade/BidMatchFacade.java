package com.hongkun.finance.invest.facade;

import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description   : 标的匹配业务处理
 * @Project       : finance-invest-api
 * @Program Name  : com.hongkun.finance.invest.facade.BidMatchFacade.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface BidMatchFacade {
	/**
	 *  @Description    :  查询待匹配标的列表
	 *  @Method_Name    : matchBidInfoList
	 *  @param title     标的标题
	 *  @param title     标的类型 1-优选 2-散标
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月19日 下午4:04:25 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> matchBidInfoList(String title,int bidType,Pager pager);
	/**
	 *  @Description    : 匹配查询--标的列表
	 *  @Method_Name    : findBidInfoVOByCondition
	 *  @param title
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月20日 下午1:52:47 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> allMatchBidInfoList(String title,Pager pager);

	/**
	 *  @Description    : 根据标的id查询匹配情况
	 *  @Method_Name    : findMatchListByBidId
	 *  @param bidId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月20日 下午4:17:33 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findMatchListByBidId(Integer bidId,Pager pager);
	/**
	 *  @Description    : 匹配统计 每日散标、优选待匹配金额
	 *  @Method_Name    : bidMatchAmountStatistics
	 *  @param startDate
	 *  @param endDate
	 *  @param limitDays  限制天数--多少天内需要匹配的
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月21日 下午3:03:33 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> bidMatchAmountStatistics(Date startDate, Date endDate, Integer limitDays);
	/**
	 *  @Description    : 待匹配标的详情
	 *  @Method_Name    : matchBidInfoDetail
	 *  @param bidId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月26日 上午10:20:41 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> matchBidInfoDetail(Integer bidId,Integer bidType);
	/**
	 *  @Description    : 查询匹配记录中优选和散标标的详情
	 *  @Method_Name    : matchedDetails
	 *  @param bidMatchId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年9月30日 下午3:09:31 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> matchedDetails(Integer bidMatchId);
	/**
	*  @Description    ：待匹配标的列表（不带分页）
	*  @Method_Name    ：miniMatchBidInfoList
	*  @param title
	*  @param bidType 标的类型
	*  @param matchingMoney 待匹配金额，如果是优选，查询出来的散标匹配金额不能高于此金额
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/5/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    ResponseEntity<?> miniMatchBidInfoList(String title, Integer bidType, BigDecimal matchingMoney);
	/**
	*  @Description    ：查询需要导出的匹配列表集合
	*  @Method_Name    ：findMatchBidListForExport
	*  @param bidType
	*  @return java.util.List<com.hongkun.finance.invest.model.vo.BidInfoVO>
	*  @Creation Date  ：2018/6/12
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<BidInfoVO> findMatchBidListForExport(Integer bidType);
}
