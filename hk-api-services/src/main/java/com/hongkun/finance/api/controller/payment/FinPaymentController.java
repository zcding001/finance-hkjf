package com.hongkun.finance.api.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.hongkun.finance.payment.constant.PaymentConstants.PAYMENT_DIC_BUSINESS;
import static com.hongkun.finance.payment.constant.TradeStateConstants.*;
import static com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum.*;

/**
 * @Description : 交易记录，充值，提现记录相关接口
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.api.controller.payment.FinPaymentController.java
 * @Author : maruili on 2018/3/8 17:17
 */
@Controller
@RequestMapping("/finPaymentController")
public class FinPaymentController {
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private RegUserService regUserService;

	/**
	 * @Description : 交易记录查询
	 * @Method_Name : listTransactionRecord
	 * @Date : 2018/3/9 14:40
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param pager
	 * @param finFundtransfer
	 * @return
	 */
	@RequestMapping("listTransactionRecord")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public Map<String, Object> listTransactionRecord(Pager pager, FinFundtransfer finFundtransfer) {
		RegUser regUser = BaseUtil.getLoginUser();
		finFundtransfer.setRegUserId(regUser.getId());
		if (finFundtransfer.getCreateTimeEnd() != null) {
			finFundtransfer.setCreateTimeEnd(DateUtils.addDays(finFundtransfer.getCreateTimeEnd(), 1));
		}
		finFundtransfer.setFilterUnFreezeRecord(1);
		finFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_YES);
		finFundtransfer.setSortColumns("id desc");
		pager = finFundtransferService.findByCondition(finFundtransfer, pager);
		if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
			pager.getData().stream().forEach((e) -> {
				FinFundtransfer temp = (FinFundtransfer) e;
				Integer subInt = Integer.valueOf(String.valueOf(temp.getSubCode()).substring(0, 2));
				// 收入 解冻
				if(INCOME.getBigTransferType() == subInt || THAW.getBigTransferType() == subInt ||  TURNS_OUT.getBigTransferType() ==subInt){
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
		return AppResultUtil
				.successOfListInProperties(pager.getData(), null, "transMoney", "tradeType", "createTime", "subCode",
						"sumSubFlag", "afterMoney","subCodeDesc")
				.addResParameter("totalPages", pager.getTotalPages());
	}

	/**
	 * @Description : 充值提现记录查询
	 * @Method_Name : listPaymentRecord
	 * @Date : 2018/3/9 15:02
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param pager
	 * @param finPaymentRecord
	 * @return
	 */
	@RequestMapping("listPaymentRecord")
	@ResponseBody
	public Map<String, Object> listPaymentRecord(Pager pager, FinPaymentRecord finPaymentRecord) {
		RegUser regUser = BaseUtil.getLoginUser();
		finPaymentRecord.setRegUserId(regUser.getId());
		Integer stateType = finPaymentRecord.getStateType();
		finPaymentRecord.setStateType(null);
		if (finPaymentRecord.getCreateTimeEnd() != null) {
			finPaymentRecord.setCreateTimeEnd(DateUtils.addDays(finPaymentRecord.getCreateTimeEnd(), 1));
		}
		List<Integer> stateType01List = Arrays.asList(1, 2, 5, 9,10);
		List<Integer> stateType02List = Arrays.asList(6, 8);
		List<Integer> stateType03List = Arrays.asList(3, 4, 7);
		if (stateType != null) {
			if (stateType == STATE_TYPE_TRANSFERING) { // 划转中
				finPaymentRecord.setStates(stateType01List);
			} else if (stateType == STATE_TYPE_SUCCESS) { // 成功
				finPaymentRecord.setStates(stateType02List);
			} else if (stateType == STATE_TYPE_FAIL) { // 失败
				finPaymentRecord.setStates(stateType03List);
			}
		}
		finPaymentRecord.setSortColumns("create_time desc");
		pager = finPaymentRecordService.findFinPaymentRecordList(finPaymentRecord, pager);
		if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
			pager.getData().stream().forEach((e) -> {
				FinPaymentRecord temp = (FinPaymentRecord) e;
				if (stateType01List.contains(temp.getState())) {
					temp.setStateType(STATE_TYPE_TRANSFERING);
				} else if (stateType02List.contains(temp.getState())) {
					temp.setStateType(STATE_TYPE_SUCCESS);
				} else if (stateType03List.contains(temp.getState())) {
					temp.setStateType(STATE_TYPE_FAIL);
				}
			});
		}
		return AppResultUtil
				.successOfListInProperties(pager.getData(), null, "transMoney", "state", "createTime", "stateType")
				.addResParameter("totalPages", pager.getTotalPages());
	}

	/**
	 * @Description ：累计投资收益记录
	 * @Method_Name ：listInvestIncomeRecord
	 * @param pager
	 * @param finFundtransfer
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Creation Date ：2018年05月21日 15:43
	 * @Author ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("listInvestIncomeRecord")
	@ResponseBody
	public Map<String, Object> listInvestIncomeRecord(Pager pager, FinFundtransfer finFundtransfer) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		finFundtransfer.setRegUserId(regUser.getId());
		if (finFundtransfer.getCreateTimeEnd() != null) {
			finFundtransfer.setCreateTimeEnd(DateUtils.addDays(finFundtransfer.getCreateTimeEnd(), 1));
		}
		finFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_YES);
		finFundtransfer.setSubCodeList(Arrays.asList(1011, 1014)); // 收入利息，收入加息
		finFundtransfer.setSortColumns("id desc");
		pager = finFundtransferService.findByCondition(finFundtransfer, pager);
		Function<Object, Object> getKeyDesc = (key) -> dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
				PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_TRANSFER, (Integer) key);
		return AppResultUtil
				.successOfListInProperties(pager.getData(), null, "transMoney", "tradeType", "createTime", "subCode")
				.addResParameter("totalPages", pager.getTotalPages()).addParameterDescInList("subCode", getKeyDesc);

	}
}
