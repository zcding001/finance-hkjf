package com.hongkun.finance.web.controller.qdz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzConsoleFacade;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.qdz.vo.QdzInterestInfoVo;
import com.hongkun.finance.qdz.vo.QdzTransferRecordVo;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.QdzRecommendConditionVo;
import com.hongkun.finance.vas.model.QdzVasRuleItem;
import com.hongkun.finance.vas.model.RebatesRuleItem;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description : 钱袋子后台功能的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.qdz.QdzConsoleController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/qdzController")
public class QdzController {
	private static final Logger logger = LoggerFactory.getLogger(QdzController.class);
	@Reference
	private QdzConsoleFacade qdzConsoleFacade;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private QdzTaskJobFacade qdzTaskJobFacade;
	@Reference
	private RecommendEarnFacade recommendEarnFacade;

	/**
	 * @Description : 查询钱袋子利息对账信息
	 * @Method_Name : searchByCondition;
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 上午9:46:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInterestBalance")
	@ResponseBody
	public ResponseEntity<?> searchInterestBalance(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, Pager pager) {
		// 查询钱袋子利息对账记录信息
		return qdzConsoleFacade.qdzInterestBalance(
				StringUtils.isNotBlank(startTime) ? DateUtils.parse(startTime, DateUtils.DATE) : null,
				StringUtils.isNotBlank(endTime) ? DateUtils.parse(endTime, DateUtils.DATE) : null, pager);
	}

	/**
	 * @Description : 查询钱袋子第三方与 平台利息明细
	 * @Method_Name : searchInterestInfo;
	 * @param userFlag:0-平台;1-第三方账户
	 * @param day:格式yyyy-MM-dd
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 上午10:56:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInterestInfo")
	@ResponseBody
	public ResponseEntity<?> searchInterestInfo(@RequestParam(value = "userFlag", required = false) String userFlag,
			@RequestParam(value = "day", required = false) String day, Pager pager) {
		// 查询条件判断
		if (StringUtils.isBlank(userFlag)) {
			return new ResponseEntity<>(Constants.ERROR, "用户标识不能为空！");
		}
		if (StringUtils.isBlank(day)) {
			return new ResponseEntity<>(Constants.ERROR, "查询日期不能为空！");
		}
		// 查询钱袋子第三方与 平台每日利息明细
		return qdzConsoleFacade.qdzInterestDetail(Integer.parseInt(userFlag),
				StringUtils.isNotBlank(day) ? DateUtils.parse(day, DateUtils.DATE) : null, pager);
	}

	/**
	 * @Description : 当前债权查询
	 * @Method_Name : searchCreditorInfo;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 下午4:54:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchCreditorInfo")
	@ResponseBody
	public ResponseEntity<?> searchCreditorInfo() {
		// 查询当前债权
		return qdzConsoleFacade.findCurrentCreditor();
	}

	/**
	 * @Description : 债权对账查询
	 * @Method_Name : searchCreditorBalanceInfo;
	 * @param bidName
	 *            标的名称
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 下午6:11:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchCreditorBalanceInfo")
	@ResponseBody
	public ResponseEntity<?> searchCreditorBalanceInfo(
			@RequestParam(value = "bidName", required = false) String bidName,Pager pager) {
		
		return qdzConsoleFacade.findCreditorBalanceInfo(bidName,pager);
	}

	/**
	 * @Description : 查询钱袋子账单流水信息
	 * @Method_Name : searchQdzBillWater;
	 * @param qdzTransferRecordVo
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月20日 上午11:10:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchQdzBillWater")
	@ResponseBody
	public ResponseEntity<?> searchQdzBillWater(QdzTransferRecordVo qdzTransferRecordVo, Pager pager) {
		return qdzConsoleFacade.findQdzBillWater(qdzTransferRecordVo, pager);
	}

	/**
	 * @Description : 活期产品查询
	 * @Method_Name : searchHqProductInfo;
	 * @param bidName
	 *            标的名称
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月20日 下午5:42:36;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchHqProductInfo")
	@ResponseBody
	public ResponseEntity<?> searchHqProductInfo(@RequestParam(value = "bidName", required = false) String bidName,
			Pager pager) {
		return qdzConsoleFacade.findHqProductInfo(bidName, pager);
	}

	/**
	 * @Description :保存钱袋子规则
	 * @Method_Name : inserQdzRule;
	 * @param qdzVasRuleItem
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月21日 下午4:01:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertQdzRule")
	@ResponseBody
    @Token
	public ResponseEntity<?> insertQdzRule(@Valid QdzVasRuleItem qdzVasRuleItem) {
		try {
			if (qdzVasRuleItem == null) {
				return new ResponseEntity<>(Constants.ERROR, "钱袋子规则信息为空！");
			}
			// 插入钱袋子规则
			String content = JsonUtils.toJson(qdzVasRuleItem);
			vasRebatesRuleService.insertVasRebatesRule(content, VasRuleTypeEnum.QDZ, null, null);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("保存钱袋子规则失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	/**
	 * @Description : 更新钱袋子规则
	 * @Method_Name : updateQdzRule;
	 * @param qdzVasRuleItem
	 * @param qdzRuleId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月21日 下午5:48:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateQdzRule")
	@ResponseBody
    @Token
	@ActionLog(msg = "更新钱袋子规则, id: {args[0].qdzRuleId}, 状态: {args[0].state")
	public ResponseEntity<?> updateQdzRule(QdzVasRuleItem qdzVasRuleItem,
			@RequestParam(value = "qdzRuleId", required = false) String qdzRuleId,
			@RequestParam(value = "state", required = false) String state) {
		try {
			// 判断是否存在规则ID，如果不存在的话，则不能更新
			if (StringUtils.isBlank(qdzRuleId)) {
				return new ResponseEntity<>(Constants.ERROR, "请选择要更新的内容！");
			}
			// 插入钱袋子规则
			vasRebatesRuleService.updateVasRebatesRule(qdzVasRuleItem != null ? JsonUtils.toJson(qdzVasRuleItem) : null,
					null, null, Integer.parseInt(qdzRuleId),
					StringUtils.isBlank(state) ? null : Integer.parseInt(state));
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("更新钱袋子规则失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	/**
	 * @Description : 插入钱袋子推荐奖规则
	 * @Method_Name : inserQdzRecommendEarnRule;
	 * @param qdzRecommendConditionVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月21日 下午4:26:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertQdzRecommendEarnRule")
	@ResponseBody
	@Token
	public ResponseEntity<?> insertQdzRecommendEarnRule(QdzRecommendConditionVo qdzRecommendConditionVo) {
		try {
			// 获取规则内容信息
			RebatesRuleItem rebatesRuleItem = new RebatesRuleItem();
			ClassReflection.reflectionAttr(qdzRecommendConditionVo, rebatesRuleItem);
			String content = JsonUtils.toJson(rebatesRuleItem);
			if (StringUtils.isBlank(content)) {
				return new ResponseEntity<>(ERROR, "规则内容不能为空！");
			}
			// 插入钱袋子推荐奖规则
			vasRebatesRuleService.insertVasRebatesRule(content, VasRuleTypeEnum.QDZRECOMMONEY,
					qdzRecommendConditionVo.getBeginTime(), qdzRecommendConditionVo.getEndTime());
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("保存钱袋子推荐规则失败: ", e);
			return new ResponseEntity<>(ERROR, "保存失败！");
		}
	}

	/**
	 * @Description : 更新钱袋子推荐奖规则
	 * @Method_Name : updateQdzRecommendEarnRule;
	 * @param qdzRecommendConditionVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月21日 下午5:43:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateQdzRecommendEarnRule")
	@ResponseBody
	@Token
	public ResponseEntity<?> updateQdzRecommendEarnRule(QdzRecommendConditionVo qdzRecommendConditionVo) {
		try {
			if (qdzRecommendConditionVo.getVasRebatesRuleId() == null) {
				return new ResponseEntity<>(ERROR, "请选择要更新的内容！");
			}
			// 获取规则内容信息
			RebatesRuleItem rebatesRuleItem = new RebatesRuleItem();
			ClassReflection.reflectionAttr(qdzRecommendConditionVo, rebatesRuleItem);
			String content = JsonUtils.toJson(rebatesRuleItem);
			// 更新钱袋子推荐奖规则
			vasRebatesRuleService.updateVasRebatesRule(content, qdzRecommendConditionVo.getBeginTime(),
					qdzRecommendConditionVo.getEndTime(), qdzRecommendConditionVo.getVasRebatesRuleId(),
					qdzRecommendConditionVo.getState());
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("更新钱袋子推荐奖规则失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	/**
	 * @Description : 设置 债权池金额
	 * @Method_Name : insertQdzCreditorPool;
	 * @param vasRebatesRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月21日 下午5:54:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertQdzCreditorPool")
	@ResponseBody
	public ResponseEntity<?> insertQdzCreditorPool(VasRebatesRule vasRebatesRule) {
		try {
			if (StringUtils.isBlank(vasRebatesRule.getContent())) {
				return new ResponseEntity<>(Constants.ERROR, "债权池金额不能为空！");
			}
			// 设置债权池金额
			vasRebatesRuleService.insertVasRebatesRule(vasRebatesRule);
			// 更新当前redis中债权池金额
			String vasKey = VasConstants.VAR_RULE_KEY + VasRuleTypeEnum.CREDITORMONEY.getValue()
					+ VasConstants.VAS_RULE_STATE_START;
			JedisClusterUtils.setAsJson(vasKey, vasRebatesRule);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("设置债权池金额失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败!");
		}
	}

	/**
	 * @Description : 修改债权池金额
	 * @Method_Name : updateQdzCreditorRule
	 * @Date : 2017/10/16 14:56
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param vasRebatesRule
	 * @return
	 */
	@RequestMapping("/updateQdzCreditorRule")
	@ResponseBody
	public ResponseEntity<?> updateQdzCreditorRule(VasRebatesRule vasRebatesRule) {
		if (StringUtils.isBlank(vasRebatesRule.getContent())) {
			return new ResponseEntity<>(Constants.ERROR, "债权池金额不能为空！");
		}
		// 设置债权池金额
		int result = vasRebatesRuleService.updateVasRebatesRule(vasRebatesRule.getContent(), null, null,
				vasRebatesRule.getId(), null);
		if (result <= 0) {
			return new ResponseEntity<>(Constants.ERROR, "修改失败！");
		}
		// 更新当前redis中债权池金额
		String vasKey = VasConstants.VAR_RULE_KEY + VasRuleTypeEnum.CREDITORMONEY.getValue()
				+ VasConstants.VAS_RULE_STATE_START;
		JedisClusterUtils.setAsJson(vasKey, vasRebatesRule);
		return new ResponseEntity<>(Constants.SUCCESS, "修改成功！");
	}

	/**
	 * @Description : 交易失败记录查询
	 * @Method_Name : findTransRecordFailList
	 * @Date : 2017/9/25 16:51
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param qdzTransferRecordVo
	 * @return
	 */
	@RequestMapping("/searchTransRecordFailList")
	@ResponseBody
	ResponseEntity<?> searchTransRecordFailList(QdzTransferRecordVo qdzTransferRecordVo, Pager pager) {
		qdzTransferRecordVo.setState(2);
		return qdzConsoleFacade.findQdzBillWater(qdzTransferRecordVo, pager);
	}

	/**
	 * @Description : 每日利息计算失败记录查询
	 * @Method_Name : searchInterestDayFailList
	 * @Date : 2017/9/26 15:22
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param interestInfoVo
	 * @param pager
	 * @return
	 */
	@RequestMapping("/searchInterestDayFailList")
	@ResponseBody
	ResponseEntity<?> searchInterestDayFailList(QdzInterestInfoVo interestInfoVo, Pager pager) {
		interestInfoVo.setState(QdzConstants.QDZ_INTEREST_DAY_STATE_FAIL);
		// 查询钱袋子利息失败记录
		return qdzConsoleFacade.findQdzInterestInfo(interestInfoVo, pager);
	}

	/**
	 * @Description : 重新计算利息
	 * @Method_Name : repairCalcInterest
	 * @Date : 2017/9/26 15:18
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param qdzInterestDayId
	 * @return
	 */
	@RequestMapping("/repairCalcInterest")
	@ResponseBody
	@Token
	@ActionLog(msg = "重新计算利息, id: {args[0].id}")
	ResponseEntity<?> repairCalcInterest(@RequestParam(value = "id", required = true) String qdzInterestDayId) {
		return qdzTaskJobFacade.repairCalcInterest(qdzInterestDayId);
	}

	/**
	 * @Description : 查询钱袋子规则
	 * @Method_Name : searchQdzRule
	 * @Date : 2017/9/27 16:22
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	@RequestMapping("/searchQdzRule")
	@ResponseBody
	ResponseEntity<?> searchQdzRule(@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, Pager pager) {
		VasRebatesRule vasRebatesRule = new VasRebatesRule();
		vasRebatesRule.setSortColumns("state asc,create_time desc");
		vasRebatesRule.setType(VasRuleTypeEnum.QDZ.getValue());
		Date startDate = StringUtils.isNotBlank(startTime) ? DateUtils.parse(startTime, DateUtils.DATE) : null;
		Date endDate = StringUtils.isNotBlank(endTime) ? DateUtils.parse(endTime, DateUtils.DATE) : null;
		vasRebatesRule.setCreateTimeBegin(startDate);
		vasRebatesRule.setCreateTimeEnd(endDate);
		Pager pagerInfo = null;
		try {
			pagerInfo = vasRebatesRuleService.findVasRebatesRuleList(vasRebatesRule, pager);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("searchQdzRule has error:", e);
			}
			return new ResponseEntity<>(ERROR, "查询钱袋子规则出错");
		}

		return new ResponseEntity<>(SUCCESS, pagerInfo);
	}

	/**
	 * @Description : 查询钱袋子推荐奖规则
	 * @Method_Name : searchQdzRecommendEarnRule
	 * @Date : 2017/9/27 16:22
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	@RequestMapping("/searchQdzRecommendEarnRule")
	@ResponseBody
	ResponseEntity<?> searchQdzRecommendEarnRule(@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, Pager pager) {
		VasRebatesRule vasRebatesRule = new VasRebatesRule();
		vasRebatesRule.setSortColumns("state asc,create_time desc");
		vasRebatesRule.setType(VasRuleTypeEnum.QDZRECOMMONEY.getValue());
		Date startDate = StringUtils.isNotBlank(startTime) ? DateUtils.parse(startTime, DateUtils.DATE) : null;
		Date endDate = StringUtils.isNotBlank(endTime) ? DateUtils.parse(endTime, DateUtils.DATE) : null;
		vasRebatesRule.setCreateTimeBegin(startDate);
		vasRebatesRule.setCreateTimeEnd(endDate);
		Pager pagerInfo = null;
		try {
			pagerInfo = vasRebatesRuleService.findVasRebatesRuleList(vasRebatesRule, pager);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("searchQdzRecommendEarnRule has error:", e);
			}
			return new ResponseEntity<>(ERROR, "查询钱袋子推荐奖规则出错");
		}

		return new ResponseEntity<>(SUCCESS, pagerInfo);
	}

	/**
	 * @Description : 查询债权池规则
	 * @Method_Name : searchQdzCreditorRule
	 * @Date : 2017/10/10 10:07
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @return
	 */
	@RequestMapping("/searchQdzCreditorRule")
	@ResponseBody
	ResponseEntity<?> searchQdzCreditorRule() {
		VasRebatesRule vasRebatesRule = new VasRebatesRule();
		vasRebatesRule.setType(VasRuleTypeEnum.CREDITORMONEY.getValue());
		Map map = new HashMap<String, Object>();
		List<VasRebatesRule> list = null;
		try {
			list = vasRebatesRuleService.findVasRebatesRuleList(vasRebatesRule);
			if (list != null && list.size() > 0) {
				VasRebatesRule rule = list.get(0);
				String content = rule.getContent();
				map.put("ruleId", rule.getId());
				map.put("ruleContent", content);
			}

			// 从redis中获取当前规则，如果没有，则从数据库中查询
			VasRebatesRule creditorRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
					VasRuleTypeEnum.CREDITORMONEY.getValue(), VasConstants.VAS_RULE_STATE_START);
			if (creditorRule == null) {
				return new ResponseEntity<>(Constants.ERROR, "获取当前债权池异常!");
			}
			logger.info("当前债权金额：{}", creditorRule.getContent());
			map.put("remainMoney", creditorRule.getContent());
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("searchQdzCreditorRule has error:", e);
			}
			return new ResponseEntity<>(ERROR, "查询资金池规则出错");
		}

		return new ResponseEntity<>(SUCCESS, map);
	}

	/**
	 * @Description : 根据id查询规则信息
	 * @Method_Name : getQdzRule
	 * @Date : 2017/9/29 18:11
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @return
	 */
	@RequestMapping("/getQdzRule")
	@ResponseBody
	ResponseEntity<?> getQdzRule(@RequestParam(value = "id", required = true) Integer id) {
		try {
			VasRebatesRule rule = vasRebatesRuleService.findVasRebatesRuleById(id);
			if (rule == null) {
				return new ResponseEntity<>(ERROR, "没有查询到该规则信息");
			}
			return new ResponseEntity<>(SUCCESS, rule);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("getQdzRule id {} has error:", id, e);
			}
			return new ResponseEntity<>(ERROR, "查询规则信息出错");
		}

	}

	/**
	 * @Description : 修改规则状态为启用状态，同时将目前启用的修改为失效
	 * @Method_Name : enableRule
	 * @Date : 2017/10/17 10:55
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param id
	 * @return
	 */
	@RequestMapping("/enableRule")
	@ResponseBody
	@Token
	@ActionLog(msg = "修改规则状态为启用状态，同时将目前启用的修改为失效, id: {args[0].id}")
	ResponseEntity<?> enableRule(@RequestParam(value = "id", required = true) Integer id) {
		return vasRebatesRuleService.enableRuleById(id);

	}
}
