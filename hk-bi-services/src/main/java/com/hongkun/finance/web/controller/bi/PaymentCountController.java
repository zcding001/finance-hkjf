package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.hongkun.finance.payment.model.vo.PaymentRecordVo;
import com.hongkun.finance.qdz.facade.QdzConsoleFacade;
import com.hongkun.finance.qdz.vo.QdzTransferRecordVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 支付相关统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/paymentCountController")
public class PaymentCountController {

    @Reference
    private FinPaymentConsoleFacade finPaymentConsoleFacade;
   /**
    *  @Description    : 充值提现统计
    *  @Method_Name    : findPayRecordCountList;
    *  @param paymentRecordVo
    *  @param pager
    *  @return
    *  @return         : ResponseEntity<?>;
    *  @Creation Date  : 2018年9月19日 下午4:30:20;
    *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
    */
    @RequestMapping("/findPayRecordCountList")
    @ResponseBody
    ResponseEntity<?> findPayRecordCountList(PaymentRecordVo paymentRecordVo, Pager pager) {
        return finPaymentConsoleFacade.findPaymentCountList(pager, paymentRecordVo);
    }

}
