package com.hongkun.finance.loan.service;

import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.BidReturnCapRecord;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.service.BidRepayPlanService.java
 * @Class Name : BidRepayPlanService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface BidRepayPlanService {

	/**
	 * @Described : 单条插入
	 * @param bidRepayPlan
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertBidRepayPlan(BidRepayPlan bidRepayPlan);

	/**
	 * @Described : 批量插入
	 * @param List<BidRepayPlan>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBidRepayPlanBatch(List<BidRepayPlan> list);

	/**
	 * @Described : 批量插入
	 * @param List<BidRepayPlan>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertBidRepayPlanBatch(List<BidRepayPlan> list, int count);

	/**
	 * @Described : 更新数据
	 * @param bidRepayPlan
	 *            要更新的数据
	 * @return : void
	 */
	void updateBidRepayPlan(BidRepayPlan bidRepayPlan);

	/**
	 * @Described : 批量更新数据
	 * @param bidRepayPlan
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateBidRepayPlanBatch(List<BidRepayPlan> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return BidRepayPlan
	 */
	BidRepayPlan findBidRepayPlanById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param bidRepayPlan
	 *            检索条件
	 * @return List<BidRepayPlan>
	 */
	List<BidRepayPlan> findBidRepayPlanList(BidRepayPlan bidRepayPlan);

	/**
	 * @Described : 条件检索数据
	 * @param bidRepayPlan
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<BidRepayPlan>
	 */
	List<BidRepayPlan> findBidRepayPlanList(BidRepayPlan bidRepayPlan, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param bidRepayPlan
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<BidRepayPlan>
	 */
	Pager findBidRepayPlanList(BidRepayPlan bidRepayPlan, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param bidRepayPlan
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findBidRepayPlanCount(BidRepayPlan bidRepayPlan);

	/**
	 * @Description : 查询某个标的的最后一次还款时间
	 * @Method_Name : findLastRepayPlanTime
	 * @param bidId
	 * @return : Date
	 * @Creation Date : 2017年6月26日 上午10:47:33
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	Date findLastRepayPlanTime(Integer bidId);

	/**
	 * @Description : 查询某个标的的下次还款日期
	 * @Method_Name : findNextRepayPlanTime
	 * @param bidId
	 * @return
	 * @return : Date
	 * @Creation Date : 2017年6月26日 上午11:09:11
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	Date findNextRepayPlanTime(Integer bidId);

	/**
	 * 
	 * @Description : 方法描述:校验还款（回款计划和还款计划）
	 * @Method_Name : validateRepayPlan
	 * @param repayId
	 * @param regUserId
	 *            还款人用户ID
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月27日 下午3:36:58
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> validateRepayPlan(int repayId, int regUserId);

	/***
	 * @Description : 统计还款计划 服务费、利息、罚息、本金、待回款金额、已回款金额、提前还款金额
	 * @Method_Name : countRepayPlanAmount
	 * @param bidRepayPlan
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月4日 上午10:45:03
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> countRepayPlanAmount(BidRepayPlan bidRepayPlan);
	
	/**
	 *  @Description    : 【提前还款-部分还款】提前还款时，更新还款计划、回款计划
	 *  @Method_Name    : updateForAdvanceRepaycaptail
	 *  @param insertRepayPlanList 需要插入还款计划
	 *  @param updateRepayPlanList 需要更新还款计划
	 *  @param delRepayPlanList 删除还款计划
	 *  @param insertRepayPlanList 插入回款计划
	 *  @param updateRepayPlanList 更新回款计划
	 *  @param delReceiptPlanList 删除回款计划
	 *  @param bidReturnCapRecord 提前还本记录
	 *  @param settleFlag 是否结清
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月4日 下午6:06:45 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
    void updateForAdvanceRepaycaptail(List<BidRepayPlan> insertRepayPlanList, List<BidRepayPlan> delRepayPlanList, 
                                      List<BidReceiptPlan> insertReceiptPlanList, List<BidReceiptPlan> 
                                              delReceiptPlanList, BidReturnCapRecord bidReturnCapRecord);
	
	/**
	 *  @Description    : 【还款】更新还款计划、回款计划
	 *  @Method_Name    : updateForRepay
	 *  @param bidRepayPlan 还款计划
	 *  @param bidReceiptPlanList 回款计划
	 *  @return         : void
	 *  @Creation Date  : 2017年7月4日 下午11:12:59 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	void updateForRepay(BidRepayPlan bidRepayPlan, List<BidReceiptPlan> bidReceiptPlanList);
	
	/**
	 *  @Description    : 条件检索还款计划统计信息
	 *  @Method_Name    : findRepayPlanCountList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月19日 下午6:24:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findRepayPlanCountList(Pager pager, LoanVO loanVO);
	/**
	 * 
	 *  @Description    : 批量生成还款计划和回款计划
	 *  @Method_Name    : insertBidRepayPlanBatch
	 *  @param repayPlans
	 *  @param receiptPlans
	 *  @return         : void
	 *  @Creation Date  : 2017年10月20日 下午4:38:54 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Compensable
	void insertBidRepayPlanBatch(List<BidRepayPlan> repayPlans, List<BidReceiptPlan> receiptPlans);
	
	/**
	 *  @Description    : 条件检索还款计划数据，含有还款统计数据
	 *  @Method_Name    : findPlanListWithStatistics
	 *  @param pager
	 *  @param bidRepayPlan
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月25日 下午5:48:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findRepayPlanListWithStatistics(Pager pager, BidRepayPlan bidRepayPlan);
	/**
	 * 
	 *  @Description    : 查询某个标的还款计划
	 *  @Method_Name    : findReplanListByBidId
	 *  @param bidInfoId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月28日 下午2:00:17 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findReplanListByBidId(Integer bidInfoId);
	/**
	 * 
	 *  @Description    : 通过标的id查询每个标的截至日期
	 *  @Method_Name    : findLastRepayPlanTimeByIds
	 *  @param bidIds
	 *  @return
	 *  @return         : List<BidRepayPlan>
	 *  @Creation Date  : 2018年1月23日 下午1:41:41 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<BidRepayPlan> findLastRepayPlanTimeByIds(List<Integer> bidIds);
	/**
	 * 
	 *  @Description    : 通过bidIds查询每个标的截止日期，通过investIds查询待回款利息
	 *  @Method_Name    : findEndDateAndSumInterest
	 *  @param bidIds
	 *  @param investIds
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年1月31日 上午9:03:44 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Map<String, Object> findEndDateAndSumInterest(List<Integer> bidIds, List<Integer> investIds);
	
	/**
	 *  @Description    : 查询还款标的的应还款期数
	 *  @Method_Name    : findCurrRepayPlanIdByBidIds
	 *  @param bidIds
	 *  @param regUserId
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月15日 上午11:52:50 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	List<Map<String, Object>> findCurrRepayPlanIdByBidIds(List<Integer> bidIds, Integer regUserId);
	
	/**
	*  查询需要通知的还款计划列表
	*  @Method_Name             ：findNeedNoticeRepayPlanList
	*  @return java.util.List<com.hongkun.finance.loan.model.BidRepayPlan>
	*  @Creation Date           ：2018/4/25
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	List<BidRepayPlan> findNeedNoticeRepayPlanList();
	/**
	*  @Description    ：查询含有服务得已还款还款计划
	*  @Method_Name    ：findBidRepayPlanListForStaIncomes
	*  @param planCdt
	*  @return java.util.List<com.hongkun.finance.loan.model.BidRepayPlan>
	*  @Creation Date  ：2018/5/3
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<BidRepayPlan> findBidRepayPlanListForStaIncomes(BidRepayPlan planCdt);
    /**
    *  @Description    ：获取某个标的本息
    *  @Method_Name    ：findSumRepayAtmByBidId
    *  @param bidId  标的id
    *  @return java.math.BigDecimal
    *  @Creation Date  ：2018/5/16
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    BigDecimal findSumRepayAtmByBidId(Integer bidId);

	/**
	 *  @Description    ：后台管理查询还款计划
	 *  @Method_Name    ：findRepayPlanListForManageStatistics
	 *  @param pager
	 *  @param bidRepayPlanVo
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年06月12日 22:09
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    Pager findRepayPlanListForManageStatistics(Pager pager, BidRepayPlanVo bidRepayPlanVo);
    
    /**
    *  统计标的已还金额
    *  @Method_Name             ：findRepayedSta
    *  @param bidIds
    *  @return java.util.List<com.hongkun.finance.loan.model.BidRepayPlan>
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    List<BidRepayPlan> findRepayedSta(List<Integer> bidIds);
}
