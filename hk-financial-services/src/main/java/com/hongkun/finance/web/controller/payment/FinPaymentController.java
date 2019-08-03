package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hongkun.finance.payment.constant.PaymentConstants.PAYMENT_DIC_BUSINESS;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.FREEZE;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.INCOME;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.PAY;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.THAW;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.TURNS_OUT;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.TURNS_IN;

import java.util.Arrays;

/**
 * @Description : 资金管理交易查询
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.web.controller.payment.FinPaymentController.java
 * @Author : maruili on 2018/1/5 9:56
 */
@Controller
@RequestMapping("finPaymentController/")
public class FinPaymentController {
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
    private DicDataService dicDataService;

	@RequestMapping("listPaymentRecord")
	@ResponseBody
	public ResponseEntity<?> listPaymentRecord(Pager pager, FinPaymentRecord finPaymentRecord) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		finPaymentRecord.setRegUserId(regUser.getId());
		Integer state = finPaymentRecord.getState();
		finPaymentRecord.setState(null);
		if(finPaymentRecord.getCreateTimeEnd()!=null){
			finPaymentRecord.setCreateTimeEnd(DateUtils.addDays(finPaymentRecord.getCreateTimeEnd(), 1));
		}

		if (state != null) {
			if (state == 1) { // 划转中
				finPaymentRecord.setStates(Arrays.asList(1, 2, 5,9));
			} else if (state == 2) { // 成功
				finPaymentRecord.setStates(Arrays.asList(6, 8));
			} else if (state == 3) { // 失败
				finPaymentRecord.setStates(Arrays.asList(3, 4, 7));
			}
		}
		finPaymentRecord.setSortColumns("create_time desc");
		pager = finPaymentRecordService.findFinPaymentRecordList(finPaymentRecord, pager);
		return new ResponseEntity<>(Constants.SUCCESS, pager);
	}

	@RequestMapping("listTransactionRecord")
	@ResponseBody
	public ResponseEntity<?> listTransactionRecord(Pager pager, FinFundtransfer finFundtransfer) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		finFundtransfer.setRegUserId(regUser.getId());
		if(finFundtransfer.getCreateTimeEnd()!=null){
			finFundtransfer.setCreateTimeEnd(DateUtils.addDays(finFundtransfer.getCreateTimeEnd(), 1));
		}
		finFundtransfer.setSortColumns("create_time desc");
		finFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_YES);
		finFundtransfer.setFilterUnFreezeRecord(1);
        pager = finFundtransferService.findByCondition(finFundtransfer, pager);
        if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
            pager.getData().stream().forEach((e) -> {
                FinFundtransfer temp = (FinFundtransfer) e;
                Integer subInt = Integer.valueOf(String.valueOf(temp.getSubCode()).substring(0, 2));
                // 收入 解冻
                if(INCOME.getBigTransferType() == subInt || THAW.getBigTransferType() == subInt ||  TURNS_OUT.getBigTransferType() ==subInt ){
                    temp.setSumSubFlag(1); // 加
                // 支出 冻结
                }else if( PAY.getBigTransferType()== subInt || FREEZE.getBigTransferType() == subInt ||  TURNS_IN.getBigTransferType() ==subInt){
                    temp.setSumSubFlag(0); // 减
                }
                String subCodeDesc = dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
                        PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_TRANSRECORD, Integer.parseInt(temp.getTradeType().toString()+ temp.getSubCode().toString()));
                if(StringUtils.isBlank(subCodeDesc)){
                    subCodeDesc =dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
                            PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_TRADE,temp.getTradeType()) + dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
                                    PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_TRANSFER,temp.getSubCode());
                }
                temp.setSubCodeDesc(subCodeDesc);
            });
        }
		return new ResponseEntity<>(Constants.SUCCESS, pager);
	}
}
