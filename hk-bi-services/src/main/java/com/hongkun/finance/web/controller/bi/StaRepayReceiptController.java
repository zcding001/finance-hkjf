package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: finance
 * @description: 回款还款计划统计
 * @author: yunlongliu@hongkun.com.cn
 * @create: 2018-06-12 11:10
 **/
@Controller
@RequestMapping("staRepayReceiptController")
public class StaRepayReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(StaRepayReceiptController.class);

    @Reference
    private LoanFacade loanFacade;


    /**
     *  @Description    ：查询还款计划
     *  @Method_Name    ：repayPlanList
     *  @param pager
     *  @param bidRepayPlanVo
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018年06月12日 13:42
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("repayPlanList")
    @ResponseBody
    public ResponseEntity repayPlanList(Pager pager,BidRepayPlanVo bidRepayPlanVo){
        bidRepayPlanVo.setSortColumns("create_time desc");
        return this.loanFacade.findRepayPlanListForManageStatistics(pager, bidRepayPlanVo);
    }

    /**
     *  @Description    ：查询用户回款计划
     *  @Method_Name    ：receiptPlanList
     *  @param pager
     *  @param bidReceiptPlanVo
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018年06月12日 13:45
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("receiptPlanList")
    @ResponseBody
    public ResponseEntity receiptPlanList(Pager pager, BidReceiptPlanVo bidReceiptPlanVo){
        bidReceiptPlanVo.setSortColumns("create_time desc");
        return this.loanFacade.findReceiptPlanListForManageStatistics(pager, bidReceiptPlanVo);
    }


    /**
     *  @Description    ：根据用户查询回款计划详情列表
     *  @Method_Name    ：staReceiptPlanListInfos
     *  @param regUserId
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018年06月12日 13:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("staReceiptPlanListInfos")
    @ResponseBody
    public ResponseEntity staReceiptPlanListInfos(Pager pager,@RequestParam("regUserId") Integer regUserId){
       return this.loanFacade.findReceiptPlanListByUserId(pager,regUserId );
    }

}
