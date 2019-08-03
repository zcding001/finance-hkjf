
package com.hongkun.finance.loan.dao;

import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.dao.BidReceiptPlanDao.java
 * @Class Name : BidReceiptPlanDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface BidReceiptPlanDao extends MyBatisBaseDao<BidReceiptPlan, java.lang.Long> {

    BigDecimal getSumInterest(BidReceiptPlan bidReceiptPlan);

    /**
     *  @Description    : 条件删除回款计划
     *  @Method_Name    : delete
     *  @param bidReceiptPlan
     *  @return         : void
     *  @Creation Date  : 2017年7月4日 下午6:25:00 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    void delete(BidReceiptPlan bidReceiptPlan);
    /**
     *  @Description    : 批量删除回款计划
     *  @Method_Name    : delBidReceiptPlanBatch
     *  @param salReceiptPlans
     *  @return         : void
     *  @Creation Date  : 2017年7月7日 下午6:01:45 
     *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
     */
	void delBidReceiptPlanBatch(List<BidReceiptPlan> salReceiptPlans);
    
    /**
     *  @Description    : 提前还本并全部还完时更新还款计划
     *  @Method_Name    : updateReceiptPlanForRepaySettle
     *  @param bidRecepitPlan
     *  @return
     *  @return         : int
     *  @Creation Date  : 2017年7月10日 下午2:33:06 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    int updateReceiptPlanForRepaySettle(BidReceiptPlan bidRecepitPlan);
    
    /**
     *  @Description    : 通过用户id检索用户待收资金
     *  @Method_Name    : findAgencyFundByUserId
     *  @param userId
     *  @param bidIds   : 需要过滤掉的标的id
     *  @return         : LoanVO
     *  @Creation Date  : 2017年9月22日 上午8:55:42 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    LoanVO findAgencyFundByUserId(Integer userId, List<Integer> bidIds);
    
    /**
    *  通过用户id集合检索用户待收资金
    *  @Method_Name             ：findAgencyFundByUserId
    *  @param regUserIds
    *  @return java.util.List<com.hongkun.finance.loan.model.vo.LoanVO>
    *  @Creation Date           ：2018/6/6
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    List<LoanVO> findAgencyFundByUserId(List<Integer> regUserIds);
    
    /**
     * 
     *  @Description    : 获取下次回款计划
     *  @Method_Name    : findNextReceiptPlan
     *  @param investId
     *  @return
     *  @return         : BidReceiptPlan
     *  @Creation Date  : 2017年12月13日 上午9:11:00 
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
	 *  @Creation Date  : 2017年12月13日 上午9:25:21 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidReceiptPlan findLastReceiptPlan(Integer investId);
	
	/**
	 *  @Description    : 统计还款金额
	 *  @Method_Name    : countReceiptPlanAmount
	 *  @param bidReceiptPlan
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2017年12月26日 上午9:31:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Map<String, Object> countWaitReceiptPlanAmount(BidReceiptPlan bidReceiptPlan);
	/**
	 * 
	 *  @Description    : 查询待回款利息
	 *  @Method_Name    : findSumInterestByInvestIds
	 *  @param investIds
	 *  @return
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2018年1月31日 上午9:16:09 
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
	 *  @Description    ：后台管理查询回款计划
	 *  @Method_Name    ：findReceiptPlanListForManageStatistics
	 *  @param bidReceiptPlanVo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年06月12日 22:57
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Pager findReceiptPlanListForManageStatistics(BidReceiptPlanVo bidReceiptPlanVo, Pager pager);

	/**
	 *  @Description    ：统计管理用户回款计划记录
	 *  @Method_Name    ：findBidReceiptPlanListByUserId
	 *  @param bidReceiptPlanVo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年06月13日 14:42
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    Pager findBidReceiptPlanListByUserId(BidReceiptPlanVo bidReceiptPlanVo, Pager pager);
	/**
	*  @Description    ：查询预期利息
	*  @Method_Name    ：findSumInterestByInvestId
	*  @param newInvestId
	*  @return java.math.BigDecimal
	*  @Creation Date  ：2018/7/5
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    BigDecimal findSumInterestByInvestId(Integer newInvestId);

    Date findMinPlanTimeDue(Integer regUserId);

    /**
    *  查询回款统计明细
    *  @param regUserId
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @date                    ：2018/10/23
    *  @author                  ：zc.ding@foxmail.com
    */
    List<Map<String, Object>> findReceiptPlanCountDetail(Integer regUserId);
}
