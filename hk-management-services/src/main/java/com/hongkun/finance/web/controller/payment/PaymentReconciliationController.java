package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPayMentNoticeFacade;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.hongkun.finance.payment.model.vo.PayCheckVo;
import com.hongkun.finance.payment.service.FinPayCheckAccountService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 支付对账后台功能的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.payment.
 *          PaymentReconciliationController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/paymentReconciliationController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class PaymentReconciliationController {

	@Reference
	private FinPaymentConsoleFacade finPaymentConsoleFacade;
	@Reference
	private FinPayMentNoticeFacade finPayMentNoticeFacade;
	@Reference
	private FinPayCheckAccountService finPayCheckAccountService;

	/**
	 * @Description : 后台功能支付对账查询
	 * @Method_Name : searchByCondition;
	 * @param payCheckCondition
	 *            查询条件
	 * @param pager
	 *            分页对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月15日 下午4:20:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findPayCheckReconliciation")
	@ResponseBody
	public ResponseEntity<?> searchByCondition(PayCheckVo payCheckCondition, Pager pager) {
		return finPaymentConsoleFacade.findPayReconliciation(payCheckCondition, pager);
	}

	/**
	 * @Description : 查询用户与第三方的对账信息
	 * @Method_Name : findUserPayCheck;
	 * @param payCheckCondition
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月18日 下午2:22:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findUserPayCheck")
	@ResponseBody
	public ResponseEntity<?> findUserPayCheck(PayCheckVo payCheckCondition, Pager pager) {
		return finPayCheckAccountService.findUserPayCheckAccount(payCheckCondition.getTradeType(),
				SystemTypeEnums.HKJF, payCheckCondition.getRegUserId(), pager);
	}

	@RequestMapping("/testPayCheck")
	@ResponseBody
	public void testPayCheck() {
		// finPayCheckAccountService.excuteLianLianReconciliation("2017-08-13");
		// finPayCheckAccountService.excuteBaoFuReconciliation("2018-02-01");
	}
}
