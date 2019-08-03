package com.hongkun.finance.web.controller.bid;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.service.BidTransferManualService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;

import static com.hongkun.finance.invest.constants.InvestConstants.INVEST_USER_FLAG_TRANSFER;
import static com.hongkun.finance.invest.constants.InvestConstants.INVEST_USER_FLAG_BUYER;

/**
 * @Description : 手动债权转让
 * @Project : hk-financial-services
 * @Program Name :
 *          com.hongkun.finance.web.controller.TransferManualController.java
 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
 */
@Controller
@RequestMapping("transferManualController")
public class TransferManualController {
	
	private final Logger logger = LoggerFactory.getLogger(TransferManualController.class);
	@Reference
	private TransferManualFacade transferManualFacade;
	@Reference
	private BidTransferManualService bidTransferManualService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;

	/**
	 * @Description : 查询我的债权列表/我购买的债权列表
	 * @Method_Name : myCreditorList
	 * @param state
	 *            1：待转让2：已转让 4：转让中
	 * @param userFlag
	 *            要查询的功能1:转让人（我的债权） 2：购买人（我购买的债权）
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月13日 下午4:30:29
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@RequestMapping("myCreditorList")
	@ResponseBody
	public ResponseEntity<?> myCreditorList(Integer state, Pager pager, int userFlag) {
		try {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
			return transferManualFacade.myCreditorList(regUser,state, pager);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}

	/**
	 * @Description : 查询债权转让详情
	 * @Method_Name : showTransferManualDetail
	 * @param investId
	 *            投资记录id
	 * @param transferId
	 *            债权转让id （已发布转让时查询参数）如果债权未发布，此参数不需要传值
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午11:39:31
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@RequestMapping("showTransferManualDetail")
	@ResponseBody
	public ResponseEntity<?> showTransferManualDetail(Integer investId, Integer transferId) {
		logger.info("查询债权转让详情=====investId：{},transferId:{}",investId,transferId);
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		try {
			if (!CommonUtils.gtZero(investId) && !CommonUtils.gtZero(transferId)) {
				return new ResponseEntity<String>(Constants.ERROR, "请求参数异常");
			}
			RegUser regUser = BaseUtil.getLoginUser();
			if(CommonUtils.gtZero(transferId)){
				result = transferManualFacade.showTransferManualDetailByTransferId(transferId,regUser.getId());
			}else{
				result = transferManualFacade.showTransferManualDetailByInvestId(investId);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}
	/**
	 *
	 *  @Description    : 购买债权详情页
	 *  @Method_Name    : toBuyCreditor
	 *  @param transferId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月27日 上午9:07:21
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("toBuyCreditor")
	@ResponseBody
	public ResponseEntity<?> toBuyCreditor(Integer transferId) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		try {
			if (!CommonUtils.gtZero(transferId)) {
				return new ResponseEntity<String>(Constants.ERROR, "请求参数异常");
			}
			RegUser regUser = BaseUtil.getLoginUser();
			result = transferManualFacade.toBuyCreditor(transferId,regUser.getId());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}
	/**
	 * 
	 *  @Description    : 查询转让记录
	 *  @Method_Name    : transferRecordList
	 *  @param firstInvestId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月27日 下午4:22:57 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("transferRecordList")
	@ResponseBody
	public ResponseEntity<?> transferRecordList(Integer firstInvestId){
		if (!CommonUtils.gtZero(firstInvestId)) {
			return new ResponseEntity<String>(Constants.ERROR, "请求参数异常");
		}
		return bidTransferManualService.findTransferRecordList(firstInvestId);
	}

	/**
	 * @Description : 发布转让
	 * @Method_Name : saveTransferManual
	 * @param bidTransferManual
	 * @return
	 * @return : ResponseEntity<String>
	 * @Creation Date : 2017年6月14日 下午2:48:13
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@RequestMapping("saveTransferManual")
	@ResponseBody
	public ResponseEntity<String> saveTransferManual(BidTransferManual bidTransferManual) {
		try {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
			Integer oldInvestId = bidTransferManual.getOldInvestId();
			if (bidTransferManual == null || oldInvestId == null) {
				return new ResponseEntity<String>(Constants.ERROR, "请求参数有误");
			}
			return transferManualFacade.saveTransferManual(regUser.getId(), bidTransferManual);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}

	/**
	 * @Description : 购买债权
	 * @Method_Name : buyCreditor
	 * @param transferId
	 * @return
	 * @return : ResponseEntity<String>
	 * @Creation Date : 2017年6月14日 下午5:44:01
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@RequestMapping("buyCreditor")
	@ResponseBody
	// @AskForPermission(check = PermissionTypeEnum.LOGIN)
	public ResponseEntity<String> buyCreditor(int transferId) {
		try {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
			RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUser().getId()));
			//TODO xuhui.liu js处理
			if (regUser.getIdentify() == 0) {
				return new ResponseEntity<>(Constants.ERROR, "为保障您的资金安全，请进行实名认证之后再投资");
			}
			if(transferId<=0){
				return new ResponseEntity<>(Constants.ERROR, "请求参数有误");
			}
			return transferManualFacade.buyCreditor(regUser.getId(),regUserDetail.getRealName(), transferId);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(Constants.ERROR, "购买债权失败");
		} catch (Exception e) {
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}
	/**
	 *  @Description    : 计算转让价格和转让后利率
	 *  @Method_Name    : calTransferMoneyAndInterestRate
	 *  @param investId 投资id
	 *  @param creditorAmount  转让金额
	 *  @param transferRate    转让比率
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2017年12月7日 下午5:39:36 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("calTransferMoneyAndInterestRate")
	@ResponseBody
	public ResponseEntity<?> calTransferMoneyAndInterestRate(Integer investId,
			BigDecimal creditorAmount,BigDecimal transferRate) {
		try {
			return transferManualFacade.calTransferMoneyAndInterestRate(investId,creditorAmount,
					transferRate);
		} catch (Exception e) {
			logger.error("计算转让价格&转让后利率异常=====calTransferMoneyAndInterestRate investId:{},creditorAmount:{},transferRate:{}"
					,investId,creditorAmount,transferRate);
			return new ResponseEntity<String>(Constants.ERROR, "系统异常");
		}
	}
}
