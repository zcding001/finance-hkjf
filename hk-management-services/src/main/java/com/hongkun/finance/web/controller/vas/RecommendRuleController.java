package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.QdzRecommendConditionVo;
import com.hongkun.finance.vas.model.RebatesRuleChildItem;
import com.hongkun.finance.vas.model.RebatesRuleItem;
import com.hongkun.finance.vas.model.RecommendRuleVo;
import com.hongkun.finance.vas.model.RuleItemVo;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.model.VasRebatesRuleChild;
import com.hongkun.finance.vas.service.VasRebatesRuleChildService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 推荐规则的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.vas.RecommendRuleController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/recommendRuleController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class RecommendRuleController {
	private static final Logger logger = LoggerFactory.getLogger(RecommendRuleController.class);

	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private VasRebatesRuleChildService vasRebatesRuleChildService;
	@Reference
	private RecommendEarnFacade recommendEarnFacade;

	/**
	 * @Description :推荐规则查询
	 * @Method_Name : searchByCondition;
	 * @param vasRebatesRule
	 * @param pager
	 * @return
	 * @return : ResponseEntity;
	 * @Creation Date : 2017年6月26日 上午11:46:56;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/search")
	@ResponseBody
	public ResponseEntity<?> searchByCondition(VasRebatesRule vasRebatesRule, Pager pager) {
		vasRebatesRule.setType(VasRuleTypeEnum.RECOMMEND.getValue());
		vasRebatesRule.setSortColumns("create_time desc");
		return vasRebatesRuleService.findRulePager(vasRebatesRule, pager);
	}

	/**
	 * @Description : 通过规则ID，查询对应角色的规则信息
	 * @Method_Name : searchVasRuleById;
	 * @param vasRuleId
	 *            规则ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月10日 上午10:46:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchVasRuleById")
	@ResponseBody
	public ResponseEntity<?> searchVasRuleById(@RequestParam(value = "vasRuleId", required = true) Integer vasRuleId) {
		return vasRebatesRuleService.findRuleById(vasRuleId);
	}

	/**
	 * @Description :通过规则ID，更新角色对应的规则信息
	 * @Method_Name : updateVasRuleById;
	 * @param recommendRuleVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月10日 上午10:47:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateVasRuleById")
	@ResponseBody
	@ActionLog(msg = "通过规则ID更新角色对应的规则信息, 推荐规则id: {args[0].vasRebatesRuleId}")
	public ResponseEntity<?> updateVasRuleById(RecommendRuleVo recommendRuleVo) {
		try {
			ResponseEntity<?> resEntity = dealRuleChild(recommendRuleVo);
			if (resEntity.getResStatus() == Constants.ERROR) {
				return resEntity;
			}
			return vasRebatesRuleService.updateVasRuleById((RebatesRuleChildItem) resEntity.getResMsg(),
					recommendRuleVo);
		} catch (Exception e) {
			logger.error("推荐规则保存失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	/**
	 * @Description : 推荐规则保存
	 * @Method_Name : saveVasRebatesRule;
	 * @param recommendRuleVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月10日 上午10:57:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/save")
	@ResponseBody
	@Token
	@ActionLog(msg = "推荐规则保存, 推荐类型type: {args[0].type} , 用户类型userType: {args[0].userType}")
	public ResponseEntity<?> saveVasRebatesRule(RecommendRuleVo recommendRuleVo) {
		try {
			ResponseEntity<?> resEntity = dealRuleChild(recommendRuleVo);
			if (resEntity.getResStatus() == Constants.ERROR) {
				return resEntity;
			}
			return vasRebatesRuleService.insertVasRebatesRule((RebatesRuleChildItem) resEntity.getResMsg(),
					recommendRuleVo);
		} catch (Exception e) {
			logger.error("推荐规则保存失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	/**
	 * @Description :推荐规则更新
	 * @Method_Name : updateVasRebatesRule;
	 * @param vasRebatesRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月26日 下午3:22:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/update")
	@ResponseBody
	@ActionLog(msg = "推荐规则更新, 推荐规则Id: {args[0].vasRebatesRuleId}")
	public ResponseEntity<?> updateVasRebatesRule(QdzRecommendConditionVo qdzRecommendConditionVo) {
		try {
			if (qdzRecommendConditionVo.getVasRebatesRuleId() == null) {
				return new ResponseEntity<>(ERROR, "请选择需要更新的内容！");
			}
			// 获取规则内容信息
			RebatesRuleItem rebatesRuleItem = new RebatesRuleItem();
			ClassReflection.reflectionAttr(qdzRecommendConditionVo, rebatesRuleItem);
			String content = JsonUtils.toJson(rebatesRuleItem);
			// 更新规则
			vasRebatesRuleService.updateVasRebatesRule(content, qdzRecommendConditionVo.getBeginTime(),
					qdzRecommendConditionVo.getEndTime(), qdzRecommendConditionVo.getVasRebatesRuleId(),
					qdzRecommendConditionVo.getState());
			return new ResponseEntity<>(SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("推荐规则更新失败: ", e);
			return new ResponseEntity<>(ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 逻辑删除推荐规则
	 * @Method_Name : deleteVasRebatesRule;
	 * @param vasRebatesRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月26日 下午3:28:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@ActionLog(msg = "推荐规则状态更新, 推荐规则Id: {args[0].id}")
	public ResponseEntity<?> deleteVasRebatesRule(VasRebatesRule vasRebatesRule) {
		try {
			vasRebatesRule.setState(VasConstants.VAS_RULE_STATE_INVALID); // 逻缉删除状态
			vasRebatesRuleService.updateVasRebatesRule(vasRebatesRule);
			return new ResponseEntity<>(SUCCESS, "删除成功！");
		} catch (Exception e) {
			logger.error("逻辑删除推荐规则失败: ", e);
			return new ResponseEntity<>(ERROR, "删除失败！");
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
	@ActionLog(msg = "修改规则状态为启用状态, 推荐规则Id: {args[0]}")
	ResponseEntity<?> enableRule(@RequestParam(value = "id", required = true) Integer id) {
		return vasRebatesRuleService.enableRuleById(id);

	}

	/**
	 * @Description :处理解析推荐规则信息
	 * @Method_Name : dealRuleChild;
	 * @param recommendRuleVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月10日 上午11:06:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> dealRuleChild(RecommendRuleVo recommendRuleVo) {
		try {
			RebatesRuleChildItem item = new RebatesRuleChildItem();
			if (recommendRuleVo.getInvNumOne() != null && recommendRuleVo.getInvNumOne().size() > 0
					&& recommendRuleVo.getRebatesRateOne() != null && recommendRuleVo.getRebatesRateOne().size() > 0) {
				if (recommendRuleVo.getInvNumOne().size() == recommendRuleVo.getRebatesRateOne().size()) {
					List<RuleItemVo> friendLevelOne = new ArrayList<RuleItemVo>();
					int i = recommendRuleVo.getInvNumOne().size() - 1;
					for (; i >= 0; i--) {
						RuleItemVo ruleItemOne = new RuleItemVo();
						ruleItemOne.setInvestStrokeCount(recommendRuleVo.getInvNumOne().get(i));
						ruleItemOne.setRate(recommendRuleVo.getRebatesRateOne().get(i));
						friendLevelOne.add(ruleItemOne);
					}
					item.setFriendLevelOne(friendLevelOne);
				} else {
					return new ResponseEntity<>(Constants.ERROR, "请填写一级好友规则的完整信息！");
				}
			}
			if (recommendRuleVo.getInvNumTwo() != null && recommendRuleVo.getInvNumTwo().size() > 0
					&& recommendRuleVo.getRebatesRateTwo() != null && recommendRuleVo.getRebatesRateTwo().size() > 0) {
				List<RuleItemVo> friendLevelTwo = new ArrayList<RuleItemVo>();
				if (recommendRuleVo.getInvNumTwo().size() == recommendRuleVo.getRebatesRateTwo().size()) {
					int i = recommendRuleVo.getInvNumTwo().size() - 1;
					for (; i >= 0; i--) {
						RuleItemVo ruleItemTwo = new RuleItemVo();
						ruleItemTwo.setInvestStrokeCount(recommendRuleVo.getInvNumTwo().get(i));
						ruleItemTwo.setRate(recommendRuleVo.getRebatesRateTwo().get(i));
						friendLevelTwo.add(ruleItemTwo);
					}
					item.setFriendLevelTwo(friendLevelTwo);
				} else {
					return new ResponseEntity<>(Constants.ERROR, "请填写二级好友规则的完整信息！");
				}
			}
			ClassReflection.reflectionAttr(recommendRuleVo, item);
			return new ResponseEntity<>(Constants.SUCCESS, item);
		} catch (Exception e) {
			logger.error("处理推荐规则失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}
}
