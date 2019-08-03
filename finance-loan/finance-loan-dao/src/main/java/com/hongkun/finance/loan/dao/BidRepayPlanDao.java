package com.hongkun.finance.loan.dao;

import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.dao.BidRepayPlanDao.java
 * @Class Name : BidRepayPlanDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface BidRepayPlanDao extends MyBatisBaseDao<BidRepayPlan, java.lang.Long> {

	Date findLastRepayPlanTime(Integer bidId);

	Date findNextRepayPlanTime(Integer bidId);

	BidRepayPlan findCurrRepayPlan(Integer bidId);

	/**
	 * 
	 * @Description : 统计还款计划 服务费、利息、罚息、本金等
	 * @Method_Name : countRepayPlanAmount
	 * @param bidRepayPlan
	 * @return
	 * @return : HashMap<String,Object>
	 * @Creation Date : 2017年7月5日 下午3:38:51
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	Map<String, Object> countRepayPlanAmount(BidRepayPlan bidRepayPlan);

	/**
	 * @Description : 条件删除还款计划
	 * @Method_Name : delete
	 * @param bidRepayPlan
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午9:46:43
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	void delete(BidRepayPlan bidRepayPlan);
	/**
	 * 
	 *  @Description    : 批量删除还款计划
	 *  @Method_Name    : delBidRepayPlanBatch
	 *  @param repayPlans
	 *  @return         : int
	 *  @Creation Date  : 2017年10月17日 上午11:31:47 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	int delBidRepayPlanBatch(List<BidRepayPlan> repayPlans);
	
	/**
	 *  @Description    : 提前还本(结清)操作更新还款计划
	 *  @Method_Name    : updateRepayPlanForRepaySettle
	 *  @param bidRepayPlan
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2017年7月10日 下午2:22:24 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	int updateRepayPlanForRepaySettle(BidRepayPlan bidRepayPlan);
	/**
	 * 
	 *  @Description    : 查询还款计划
	 *  @Method_Name    : findReplanListByBidId
	 *  @param bidInfoId
	 *  @return
	 *  @return         : List<BidRepayPlan>
	 *  @Creation Date  : 2017年12月28日 下午2:05:25 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidRepayPlan> findReplanListByBidId(Integer bidInfoId);
	/**
	 * 
	 *  @Description    : 通过标的id查询每个标的截至日期
	 *  @Method_Name    : findLastRepayPlanTimeByIds
	 *  @param bidIds
	 *  @return
	 *  @return         : List<BidRepayPlan>
	 *  @Creation Date  : 2018年1月23日 下午1:46:23 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<BidRepayPlan> findLastRepayPlanTimeByIds(List<Integer> bidIds);
	
	/**
	 *  @Description    : 查询当前还款期数
	 *  @Method_Name    : findCurrRepayPlanIdByBidIds
	 *  @param bidIds
	 *  @param regUserId
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2018年3月15日 下午1:32:20 
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
	*  @Description    ：查询还款计划（收入统计专用）
	*  @Method_Name    ：findBidRepayPlanListForStaIncomes
	*  @param planCdt
	*  @return java.util.List<com.hongkun.finance.loan.model.BidRepayPlan>
	*  @Creation Date  ：2018/5/3
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    List<BidRepayPlan> findBidRepayPlanListForStaIncomes(BidRepayPlan planCdt);
	/**
	*  @Description    ：查询某个标的本息
	*  @Method_Name    ：findSumRepayAtmByBidId
	*  @param bidId  标的id
	*  @return java.math.BigDecimal
	*  @Creation Date  ：2018/5/16
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    BigDecimal findSumRepayAtmByBidId(Integer bidId);

	/**
	 *  @Description    ：后台管理查询还款列表
	 *  @Method_Name    ：findRepayPlanListForManageStatistics
	 *  @param bidRepayPlanVo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年06月12日 22:13
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Pager findRepayPlanListForManageStatistics(BidRepayPlanVo bidRepayPlanVo, Pager pager);

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
