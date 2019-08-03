package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.hongkun.finance.payment.model.vo.PayCheckVo;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 提现管理
 * @Project : hk-management-services
 * @Program Name :
 *          com.hongkun.finance.web.controller.payment.WithdrawalsController.java
 * @Author : maruili on 2017/10/18 16:09
 */
@Controller
@RequestMapping("/withdrawalsController")
public class WithdrawalsController {
	private static final Logger logger = LoggerFactory.getLogger(WithdrawalsController.class);
	@Reference
	private FinPaymentConsoleFacade finPaymentConsoleFacade;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private RegUserService regUserService;

	/**
	 * @Description : 财务提现放款列表
	 * @Method_Name : searchWithdrawalsLoan;
	 * @param pager
	 * @param checkVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:50:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchWithdrawalsLoan")
	@ResponseBody
	public ResponseEntity<?> searchWithdrawalsLoan(Pager pager, PayCheckVo checkVo) {
		buildUserIds(checkVo);
		if (checkVo.getUserIds() != null && checkVo.getUserIds().size() == 0) {
			pager.setData(new ArrayList<PaymentVO>());
			return new ResponseEntity<>(Constants.SUCCESS, pager);
		}
		checkVo.setTradeType(PayStyleEnum.WITHDRAW.getValue());
		checkVo.setState(TradeStateConstants.WAIT_PAY_MONEY);
		return this.finPaymentConsoleFacade.findPayRecord(checkVo, pager);
	}

	/**
	 * @Description : 运营提现审核列表
	 * @Method_Name : searchWithdrawalsAudit;
	 * @param pager
	 * @param checkVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:50:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchWithdrawalsAudit")
	@ResponseBody
	public ResponseEntity<?> searchWithdrawalsAudit(Pager pager, PayCheckVo checkVo) {
		buildUserIds(checkVo);
		if (checkVo.getUserIds() != null && checkVo.getUserIds().size() == 0) {
			pager.setData(new ArrayList<PaymentVO>());
			return new ResponseEntity<>(Constants.SUCCESS, pager);
		}
		checkVo.setTradeType(PayStyleEnum.WITHDRAW.getValue());
		checkVo.setState(TradeStateConstants.PENDING_PAYMENT);
		return finPaymentConsoleFacade.findPayRecord(checkVo, pager);
	}

	/**
	 * @Description : 运营提现审核详情页
	 * @Method_Name : getPayRecord;
	 * @param id
	 *            提现记录ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月18日 上午10:17:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/getPayRecord")
	@ResponseBody
	ResponseEntity<?> getPayRecord(@RequestParam(value = "id", required = true) Integer id) {
		try {
			PayCheckVo vo = finPaymentConsoleFacade.getPayRecord(id);
			if (vo == null) {
				return new ResponseEntity<>(ERROR, "没有查询到该提现信息");
			}
			return new ResponseEntity<>(SUCCESS, vo);
		} catch (Exception e) {
			logger.error("getPayRecord: 查询提现信息, id: {}, 异常信息: ", id, e);
			return new ResponseEntity<>(ERROR, "查询提现信息出错");
		}

	}

	/**
	 * @Description :运营提现审核
	 * @Method_Name : auditWithdrawals;
	 * @param id
	 *            提现记录ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:41:15;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/auditWithdrawals")
	@ResponseBody
	@ActionLog(msg = "运营提现审核, 提现记录id: {args[0]}")
	ResponseEntity<?> auditWithdrawals(@RequestParam(value = "id", required = true) Integer id) {
		try {
			return finConsumptionService.auditWithdrawals(id, SystemTypeEnums.HKJF);
		} catch (Exception e) {
			logger.error("auditWithdrawals: 运营审核提现, id: {}, 异常信息: ", id, e);
			return new ResponseEntity<>(ERROR, "审核提现信息出现异常");
		}

	}

	/**
	 * @Description : 财务提现放款审核
	 * @Method_Name : loanWithdrawals;
	 * @param ids
	 *            提现记录ID集合
	 * @param state
	 *            状态
	 * @param reason
	 *            原因
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:43:56;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/loanWithdrawals")
	@ResponseBody
	@ActionLog(msg = "财务提现放款审核, 提现记录ids: {args[0]}, 状态: {args[1]}, 原因: {args[2]}")
	ResponseEntity<?> loanWithdrawals(@RequestParam(value = "ids", required = true) List<Integer> ids,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "reason", required = false) String reason) {
		try {
			// 财务提现放款拒绝，调用审核拒绝接口
			if (TradeStateConstants.FINANCE_AUDIT_REJECT.equals(state)) {
				return finPaymentConsoleFacade.loanRejectWithdrawals(ids, reason);
			}
			return finPaymentConsoleFacade.loanWithdrawals(ids);
		} catch (Exception e) {
			logger.error("loanWithdrawals: 财务放款审核提现, ids: {}, 异常信息: ", JsonUtils.toJson(ids), e);
			return new ResponseEntity<>(ERROR, "审核放款提现信息失败");
		}

	}

	/**
	 * @Description :运营提现审核拒绝
	 * @Method_Name : auditRejectWithdrawals;
	 * @param id
	 *            提现记录ID
	 * @param rejectInfo
	 *            原因
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:46:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/auditRejectWithdrawals")
	@ResponseBody
	@ActionLog(msg = "运营提现审核拒绝, 提现记录id: {args[0]}, 原因: {args[1]}")
	ResponseEntity<?> auditRejectWithdrawals(@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "rejectInfo", required = true) String rejectInfo) {
		try {
			return finPaymentConsoleFacade.auditRejectWithdrawals(id, rejectInfo);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("运营提现审核拒绝, id: {}, 异常信息:", id, e);
			}
			return new ResponseEntity<>(ERROR, "审核拒绝提现信息失败");
		}

	}

	/**
	 * @Description :财务放款审核拒绝
	 * @Method_Name : loanRejectWithdrawals;
	 * @param ids
	 * @param rejectInfo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月29日 下午5:47:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/loanRejectWithdrawals")
	@ResponseBody
	@ActionLog(msg = "财务放款审核拒绝, 提现记录ids: {args[0]}, 原因: {args[1]}")
	ResponseEntity<?> loanRejectWithdrawals(@RequestParam(value = "ids", required = true) List<Integer> ids,
			@RequestParam(value = "rejectInfo", required = true) String rejectInfo) {
		try {
			return finPaymentConsoleFacade.loanRejectWithdrawals(ids, rejectInfo);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("财务放款审核拒绝, ids: {}, 异常信息:", JsonUtils.toJson(ids), e);
			}
			return new ResponseEntity<>(ERROR, "审核放款提现信息出现错误");
		}

	}

	private void buildUserIds(PayCheckVo checkVo) {
		if (checkVo.getCreateTimeBegin() != null) {
			checkVo.setCreateTimeBegin(
					DateUtils.parse(DateUtils.format(checkVo.getCreateTimeBegin(), DateUtils.DATE) + " 00:00:00",
							DateUtils.DATE_HH_MM));
		}
		if (checkVo.getCreateTimeEnd() != null) {
			checkVo.setCreateTimeEnd(DateUtils.parse(
					DateUtils.format(checkVo.getCreateTimeEnd(), DateUtils.DATE) + " 23:59:59", DateUtils.DATE_HH_MM));
		}
		if (StringUtils.isNotEmpty(checkVo.getRealName()) || checkVo.getLogin() != null) {
			List<Integer> userIds = new ArrayList<>();
			UserVO userVO = new UserVO();
			userVO.setRealName(checkVo.getRealName());
			userVO.setLogin(checkVo.getLogin());
			List<UserVO> userVOList = regUserService.findUserWithDetailByInfo(userVO);
			if (CommonUtils.isNotEmpty(userVOList)) {
				userVOList.forEach(vo -> userIds.add(vo.getUserId()));
			}
			checkVo.setUserIds(userIds);
		}
	}
}
