package com.hongkun.finance.loan.service;

import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.service.BidReceiptPlanService.java
 * @Class Name : BidReceiptPlanService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface BidReceiptPlanService {

	/**
	 * @Described : 单条插入
	 * @param bidReceiptPlan
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertBidReceiptPlan(BidReceiptPlan bidReceiptPlan);

	/**
	 * @Described : 批量插入
	 * @param List<BidReceiptPlan>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBidReceiptPlanBatch(List<BidReceiptPlan> list);

	/**
	 * @Described : 批量插入
	 * @param List<BidReceiptPlan>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertBidReceiptPlanBatch(List<BidReceiptPlan> list, int count);

	/**
	 * @Described : 更新数据
	 * @param bidReceiptPlan
	 *            要更新的数据
	 * @return : void
	 */
	void updateBidReceiptPlan(BidReceiptPlan bidReceiptPlan);

	/**
	 * @Described : 批量更新数据
	 * @param bidReceiptPlan
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateBidReceiptPlanBatch(List<BidReceiptPlan> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return BidReceiptPlan
	 */
	BidReceiptPlan findBidReceiptPlanById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param bidReceiptPlan
	 *            检索条件
	 * @return List<BidReceiptPlan>
	 */
	List<BidReceiptPlan> findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan);

	/**
	 * @Described : 条件检索数据
	 * @param bidReceiptPlan
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<BidReceiptPlan>
	 */
	List<BidReceiptPlan> findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param bidReceiptPlan
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<BidReceiptPlan>
	 */
	Pager findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param bidReceiptPlan
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findBidReceiptPlanCount(BidReceiptPlan bidReceiptPlan);

	/**
	 * @Description : 查询某笔投资已收利息
	 * @Method_Name : findSumReceivedInterest
	 * @param investId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月26日 上午11:45:58
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	BigDecimal findSumReceivedInterest(Integer investId);

	BigDecimal findSumNotReceivedInterest(Integer investId);

	/**
	 * @Description : 批量删除
	 * @Method_Name : delBidReceiptPlanBatch
	 * @param salReceiptPlans
	 * @return : void
	 * @Creation Date : 2017年7月7日 下午5:55:40
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	void delBidReceiptPlanBatch(List<BidReceiptPlan> salReceiptPlans);

	/**
	 * 
	 * @Description : 批量 更新&和插入
	 * @Method_Name : updateOrInsertBidReceiptPlanBatch
	 * @param updatePlans
	 * @param insertPlans
	 * @return : void
	 * @Creation Date : 2017年7月26日 上午10:11:16
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	void updateOrInsertBidReceiptPlanBatch(List<BidReceiptPlan> updatePlans, List<BidReceiptPlan> insertPlans);

	/**
	 *  @Description    : 查询待收金额
	 *  @Method_Name    : findAgencyFundByUserId
	 *  @param userId
	 *  @param bidIds   : 需要过滤的标的id
	 *  @return         : LoanVO
	 *  @Creation Date  : 2017年9月22日 上午8:54:02 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	LoanVO findAgencyFundByUserId(Integer userId, List<Integer> bidIds);

	/**
	*  查询待收金额
	*  @Method_Name             ：findAgencyFundByUserId
	*  @param regUserIds
	*  @return java.util.List<com.hongkun.finance.loan.model.vo.LoanVO>
	*  @Creation Date           ：2018/6/6
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    List<LoanVO> findAgencyFundByUserId(List<Integer> regUserIds);
	
	/**
	 *  @Description    : 检索回款计划统计信息
	 *  @Method_Name    : findReceiptPlanCountList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月19日 下午6:23:40 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findReceiptPlanCountList(Pager pager, LoanVO loanVO);
	/**
	 * 
	 *  @Description    : 获取下次回款计划
	 *  @Method_Name    : findNextReceiptPlan
	 *  @param investId
	 *  @return
	 *  @return         : BidReceiptPlan
	 *  @Creation Date  : 2017年12月13日 上午9:09:34 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidReceiptPlan findNextReceiptPlan(Integer investId);
	/**
	 * 
	 *  @Description    : 获取最后一笔回款计划
	 *  @Method_Name    : findLastReceiptPlan
	 *  @param investId
	 *  @return
	 *  @return         : BidReceiptPlan
	 *  @Creation Date  : 2017年12月13日 上午9:24:42 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidReceiptPlan findLastReceiptPlan(Integer investId);
	
	/**
	 *  @Description    : 债权转让--处理回款计划
	 *  @Method_Name    : buyCreditorForReceiptPlan
	 *  @param salReceiptPlanFirst 更新转让人本期回款计划
	 *  @param buyReceiptPlans     插入购买人回款计划
	 *  @param salReceiptPlans     删除转让人回款计划
	 *  @return         : void
	 *  @Creation Date  : 2018年1月3日 下午2:47:37 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	void buyCreditorForReceiptPlan(BidReceiptPlan salReceiptPlanFirst, List<BidReceiptPlan> buyReceiptPlans,
			List<BidReceiptPlan> salReceiptPlans);
	/**
	 * 
	 *  @Description    : 查询某笔投资对应的预期收益
	 *  @Method_Name    : findSumInterest
	 *  @param newInvestId
	 *  @return
	 *  @return         : BigDecimal
	 *  @Creation Date  : 2018年1月16日 下午1:44:43 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	BigDecimal findSumInterest(Integer newInvestId);
	/**
	 * 
	 *  @Description    : 为计算债转年化率查询相关参数（下次回款计划&最后一次回款计划&待收收益）
	 *  @Method_Name    : findParamsForTransferInterestRate
	 *  @param investId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年1月31日 上午11:27:08 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Map<String, Object> findParamsForTransferInterestRate(Integer investId);
	/**
	 * 
	 *  @Description    : 为转让详情查询相关参数（截止日期&待收收益&已收收益）
	 *  @Method_Name    : findTransferManualDetail
	 *  @param bidId
	 *  @param investId
	 *  @return
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年1月31日 上午11:47:27 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Map<String, Object> findTransferManualDetail(Integer bidId, Integer investId);
	
	/**
	 *  @Description    : 统计回款数据，包含回款计划
	 *  @Method_Name    : findReceiptPlanListWithStatistics
	 *  @param pager
	 *  @param bidReceiptPlan
	 *  @return         : Map
	 *  @Creation Date  : 2018年3月11日 下午12:07:43 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Map<String, Object> findReceiptPlanListWithStatistics(Pager pager, BidReceiptPlan bidReceiptPlan);

    /**
     *  @Description    : 统计回款数据，不包含回款计划
     *  @Method_Name    : findReceiptPlanListWithStatistics
     *  @param bidReceiptPlan
     *  @return         : Map
     *  @Creation Date  : 2018年3月11日 下午12:07:43 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
	Map<String, Object> findReceiptPlanListStatistics(BidReceiptPlan bidReceiptPlan);
	/**
	 *  @Description    : 查询某些投资记录的预期收益
	 *  @Method_Name    : findSumInterestByInvestIds
	 *  @param investIds
	 *  @return
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2018年3月21日 下午6:28:51 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<Map<String, Object>> findSumInterestByInvestIds(List<Integer> investIds);

	/**
	 *  @Description    ：近三个月有回款计划的用户
	 *  @Method_Name    ：findUserThreeMonthPlan
	 *  @param userIdList  用户id集合
	 *  @return java.util.List<java.lang.Integer>
	 *  @Creation Date  ：2018/5/2
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    List<Integer> findUserThreeMonthPlan(Set<Integer> userIdList);
	/**
	*  @Description    ：查询优选某一期回款计划
	*  @Method_Name    ：findGoodBidReceiptPlan
	*  @param bidReceiptPlan
	*  @return com.hongkun.finance.loan.model.BidReceiptPlan
	*  @Creation Date  ：2018/5/4
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    BidReceiptPlan findGoodBidReceiptPlan(BidReceiptPlan bidReceiptPlan);

	/**
	 *  @Description    ：后台管理查询回款计划
	 *  @Method_Name    ：findReceiptPlanListForManageStatistics
	 *  @param pager
	 *  @param bidReceiptPlanVo
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年06月12日 17:21
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity<?> findReceiptPlanListForManageStatistics(Pager pager, BidReceiptPlanVo bidReceiptPlanVo);

	/**
	 *  @Description    ：统计管理用户回款计划记录
	 *  @Method_Name    ：findBidReceiptPlanListByUserId
	 *  @param bidReceiptPlanVo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年06月13日 14:40
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    Pager findBidReceiptPlanListByUserId(BidReceiptPlanVo bidReceiptPlanVo, Pager pager);
	/**
	*  @Description    ：查询预期收益（利息）
	*  @Method_Name    ：findSumInterestByInvestId
	*  @param newInvestId
	*  @return java.math.BigDecimal
	*  @Creation Date  ：2018/7/5
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	BigDecimal findSumInterestByInvestId(Integer newInvestId);
	/**
	*  @Description    ：查询回款中得标的最小日期
	*  @Method_Name    ：findMinPlanTimeDue
	*  @param regUserId
	*  @return java.util.Date
	*  @Creation Date  ：2018/10/17
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    Date findMinPlanTimeDue(Integer regUserId);
    
    /**
    *  查询回款统计信息明细
    *  @param regUserId
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @date                    ：2018/10/23
    *  @author                  ：zc.ding@foxmail.com
    */
    List<Map<String, Object>> findReceiptPlanCountDetail(Integer regUserId);
}
