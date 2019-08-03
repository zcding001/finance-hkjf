package com.hongkun.finance.web.controller.payment;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.hongkun.finance.payment.model.FinBankRefer;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.model.vo.PayCheckVo;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.payment.service.FinBankReferService;
import com.hongkun.finance.payment.service.FinChannelGrantService;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 支付模块业务
 * @Project : management-financial-services
 * @Program Name :
 *          com.hongkun.finance.web.controller.payment.PaymentController.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("paymentController/")
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Reference
	private FinPaymentConsoleFacade finPaymentConsoleFacade;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinChannelGrantService finChannelGrantService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private FinBankReferService finBankReferService;

	/**
	 * @Description : 条件检索交易记录
	 * @Method_Name : findPaymentVoList
	 * @param pager
	 * @param paymentVO
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月19日 下午1:51:29
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("tradeRecordList")
	@ResponseBody
	public ResponseEntity<?> tradeRecordList(Pager pager, PaymentVO paymentVO) {
		return new ResponseEntity<>(Constants.SUCCESS,
				this.finPaymentConsoleFacade.findPaymentVoList(pager, paymentVO));
	}

	/**
	 * @Description : 检索银行卡信息
	 * @Method_Name : bankCardDetail
	 * @param regUserId
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月22日 上午11:06:32
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("bankCardList")
	@ResponseBody
	public ResponseEntity<?> bankCardList(Pager pager, Integer regUserId) {
		List<?> list = this.finBankCardService.findByRegUserId(regUserId);
		pager.setData(list);
		pager.setTotalRows(list.size());
		return new ResponseEntity<>(Constants.SUCCESS, pager);
	}

	/**
	 * 
	 * @Description : 支付模式列表
	 * @Method_Name : paymentChannelList
	 * @param pager
	 * @param finChannelGrant
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月26日 下午3:52:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("paymentChannelList")
	@ResponseBody
	public ResponseEntity<?> paymentChannelList(Pager pager, FinChannelGrant finChannelGrant) {
		return new ResponseEntity<>(Constants.SUCCESS,
				this.finChannelGrantService.findFinChannelGrantList(finChannelGrant, pager));
	}

	/**
	 * 
	 * @Description : 更新支付模式状态
	 * @Method_Name : chgRegUserState
	 * @param id
	 * @param state
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月26日 下午3:56:39
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("paymentChannelState")
	@ResponseBody
	@ActionLog(msg = "更新支付模式状态, 支付模块Id: {args[0]}, 支付模式state: {args[1]}")
	public ResponseEntity<?> paymentChannelState(Integer id, Integer state) {
		FinChannelGrant finChannelGrant = new FinChannelGrant();
		finChannelGrant.setState(state);
		finChannelGrant.setId(id);
		try {
			this.finChannelGrantService.updateFinChannelGrant(finChannelGrant);
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
		return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
	}

	/**
	 * 
	 * @Description : 支付模式保存
	 * @Method_Name : insertPaymentChannel
	 * @param finChannelGrant
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月27日 下午4:27:14
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/insertPaymentChannel")
	@ResponseBody
	@Token
	@ActionLog(msg = "支付模式保存, 系统名称编码: {args[0].sysNameCode}, 支付渠道名称: {args[0].channelName}")
	public ResponseEntity<?> insertPaymentChannel(FinChannelGrant finChannelGrant) {
		return finPaymentConsoleFacade.insertPaymentChannel(finChannelGrant);
	}

	/**
	 * @Description : 支付模式更新
	 * @Method_Name : updatePaymentChannel
	 * @param finChannelGrant
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月27日 下午4:33:31
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("/updatePaymentChannel")
	@ResponseBody
	@ActionLog(msg = "支付模式更新, 支付模式id: {args[0].id}, 系统名称编码: {args[0].sysNameCode}, 支付渠道名称: {args[0].channelName}")
	public ResponseEntity<?> updatePaymentChannel(FinChannelGrant finChannelGrant) {
		return finPaymentConsoleFacade.updatePaymentChannel(finChannelGrant);
	}

	/**
	 * @Description : 通过ID查询支付模式
	 * @Method_Name : searchPaymentChannelById;
	 * @param id
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年11月2日 下午1:43:18;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchPaymentChannelById")
	@ResponseBody
	public ResponseEntity<?> searchPaymentChannelById(Integer id) {
		return new ResponseEntity<>(Constants.SUCCESS, finChannelGrantService.findFinChannelGrantById(id));
	}

	/**
	 * @Description : 查询银行维护搜索条件
	 * @Method_Name : findBankCondition;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月11日 上午10:31:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findBankCondition")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ResponseEntity<?> findBankCondition() {
		return finBankReferService.findBankCondition();
	}

	/**
	 * @Description : 分页条件查询第三方银行信息
	 * @Method_Name : findBankList;
	 * @param pager
	 * @param finBankRefer
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月11日 下午1:42:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findBankList")
	@ResponseBody
	public ResponseEntity<?> findBankList(Pager pager, FinBankRefer finBankRefer) {
		if (StringUtils.isBlank(finBankRefer.getThirdCode()) && StringUtils.isBlank(finBankRefer.getBankThirdCode())
				&& StringUtils.isBlank(finBankRefer.getPaywayCodes())) {
			finBankRefer.setThirdCode("-999");
			finBankRefer.setBankThirdCode("-999");
			finBankRefer.setPaywayCodes("-999");
		}
		return new ResponseEntity<>(Constants.SUCCESS, finBankReferService.findFinBankReferList(finBankRefer, pager));
	}

	/**
	 * @Description : 根据ID，查询第三方银行信息
	 * @Method_Name : findBankReferById;
	 * @param id
	 *            银行ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月11日 下午6:37:58;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findBankReferById")
	@ResponseBody
	public ResponseEntity<?> findBankReferById(Integer id) {
		return new ResponseEntity<>(Constants.SUCCESS, finBankReferService.findFinBankReferById(id));
	}

	/**
	 * @Description : 根据ID，修改第三方银行限额
	 * @Method_Name : updateBankReferById;
	 * @param finBankRefer
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月11日 下午6:38:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateBankReferById")
	@ResponseBody
	@ActionLog(msg = "根据ID修改第三方银行限额, 限额id: {args[0].id}, 单笔限额: {args[0].singleLimit}, 单日限额: {args[0].singleDayLimit}")
	public ResponseEntity<?> updateBankReferById(FinBankRefer finBankRefer) {
		try {
			return finBankReferService.updateFinBankRefer(finBankRefer);
		} catch (Exception e) {
			logger.error("修改银行信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "修改失败！");
		}
	}

	/**
	 * @Description :充值提现统计列表
	 * @Method_Name : findPaymentRecordCountList;
	 * @param pager
	 * @param payCheckVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年6月13日 下午6:29:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("findPaymentRecordCountList")
	@ResponseBody
	public ResponseEntity<?> findPaymentRecordCountList(Pager pager, PayCheckVo payCheckVo) {
		return finPaymentConsoleFacade.findPaymentRecordCountList(pager, payCheckVo);
	}
}
