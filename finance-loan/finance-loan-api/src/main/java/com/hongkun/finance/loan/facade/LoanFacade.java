package com.hongkun.finance.loan.facade;

import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description   : 放款服务通用接口
 * @Project       : finance-loan-api
 * @Program Name  : com.hongkun.finance.loan.facade.LoanFacade.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public interface LoanFacade {

	/**
	 *  @Description    : 条件检索还款计划、回款计划汇总统计信息
	 *  @Method_Name    : findPlanCountList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月19日 下午6:30:18 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findPlanCountList(Pager pager, LoanVO loanVO);
	
	/**
	 *  @Description    : 条件检索还款计划、回款计划
	 *  @Method_Name    : findPlanList
	 *  @param pager
	 *  @param loanVO
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月20日 上午10:18:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Pager findPlanList(Pager pager, LoanVO loanVO);
	
	/**
	 *  @Description    : 条件检索还款计划、还款计划，包含统计汇总信息（我的账户-还款计划&回款计划）
	 *  @Method_Name    : findPlanListWithStatistics
	 *  @param pager
	 *  @param loanVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月25日 下午2:50:35 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findPlanListWithStatistics(Pager pager, LoanVO loanVO);

    /**
    *  查询回款统计信息-供APP(鸿坤金服)端展示
    *  @Method_Name             ：findReceiptPlanList
    *  @param regUserId
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date           ：2018/5/14
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    Map<String, Object> findReceiptPlanList(Integer regUserId);
    
    /**
    *  查询回款详情
    *  @Method_Name             ：findReceiptPlanDetailList
    *  @param regUserId
    *  @param bidId
    *  @param investId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/5/14
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    ResponseEntity<?> findReceiptPlanDetailList(Integer regUserId, Integer bidId, Integer investId);
	/**
	 *  @Description    : 查看还款详情-供APP端使用
	 *  @Method_Name    : findRepayPlanDetailList
	 *  @param bidId
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月11日 下午4:22:37 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findRepayPlanDetailList(Integer regUserId, Integer bidId);
	
	/**
	*  查找需要通知的还款计划
	*  @Method_Name             ：findRepayPlanAndNotice
	*  @return void
	*  @Creation Date           ：2018/4/25
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	void findRepayPlanAndNotice();
	
	/**
	*  查询回款日历
	*  @Method_Name             ：findReceiptPlanCalendar
	*  @param regUserId         ：用户标识
	*  @param month             ：回款月份第一天 eg 2018-04-01
	*  @return Map<String, Object>
	*  @Creation Date           ：2018/4/28
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    Map<String, Object> findReceiptPlanCalendar(Integer regUserId, String month);
    
    /**
    *  查询还款统计信息
    *  @Method_Name             ：findRepayPlanListCount
    *  @param regUserId         ：用户id
    *  @param pager             ：分页信息
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/5/10
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    ResponseEntity<?> findRepayPlanListCount(Integer regUserId, Pager pager);

	/**
	 *  @Description    ：后台管理查询还款计划
	 *  @Method_Name    ：findRepayPlanListForManageStatistics
	 *  @param pager
	 *  @param bidRepayPlanVo
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018年06月12日 14:31
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    ResponseEntity findRepayPlanListForManageStatistics(Pager pager, BidRepayPlanVo bidRepayPlanVo);

	/**
	 *  @Description    ：后台管理查询回款计划
	 *  @Method_Name    ：findReceiptPlanListForManageStatistics
	 *  @param pager
	 *  @param bidReceiptPlanVo
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018年06月12日 14:31
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity findReceiptPlanListForManageStatistics(Pager pager, BidReceiptPlanVo bidReceiptPlanVo);

	/**
	 *  @Description    ：后台管理根据用户查询回款计划
	 *  @Method_Name    ：findReceiptPlanListByUserId
	 *  @param pager
	 *  @param regUserId
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018年06月12日 23:18
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    ResponseEntity findReceiptPlanListByUserId(Pager pager, Integer regUserId);
    
    /**
    *  查询用户待还款金额，flag true 计算逾期罚息 false 不计算罚息
    *  @param regUser
    *  @param flag  是否包含罚息
    *  @return java.math.BigDecimal
    *  @Creation Date           ：2018/9/27
    *  @Author                  ：zc.ding@foxmail.com
    */
    BigDecimal findRepayingAmount(Integer regUser, boolean flag);


    /**
     *  计算预期收益
     *  @param bidId 标的id
     *  @param increaseRate 加息利率
     *  @param amount 投资金额
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @date                    ：2018/10/10
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> findExpectAtm(Integer bidId,  BigDecimal amount, BigDecimal increaseRate);
}
