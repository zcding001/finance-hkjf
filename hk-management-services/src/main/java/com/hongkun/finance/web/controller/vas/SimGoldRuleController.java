package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.service.VasGoldRuleService;
import com.hongkun.finance.web.controller.qdz.QdzController;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.math.BigDecimal;

/**
 * @Description : 体验金规则的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.vas.SimGoldRuleController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/simGoldRuleController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class SimGoldRuleController {
	private static final Logger logger = LoggerFactory.getLogger(SimGoldRuleController.class);

	@Reference
	private VasGoldRuleService vasGoldRuleService;

	/**
	 * @Description :体验金规则查询
	 * @Method_Name : searchGoldRule;
	 * @param vasGoldRule
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午11:26:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchGoldRule")
	@ResponseBody
	public ResponseEntity<?> searchGoldRule(VasGoldRule vasGoldRule, Pager pager) {
		vasGoldRule.setSortColumns("create_time desc");
		return new ResponseEntity<>(SUCCESS, vasGoldRuleService.findVasGoldRuleList(vasGoldRule, pager));
	}

	/**
	 * @Description :保存体验金规则
	 * @Method_Name : saveVasGoldRule;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午11:32:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/saveVasGoldRule")
	@ResponseBody
	public ResponseEntity<?> saveVasGoldRule(VasGoldRule vasGoldRule) {
		try {
			// 保存体验金规则内容校验
			ResponseEntity<?> result = checkVasGoldRuleInfo(vasGoldRule);
			if (result.getResStatus() == ERROR) {
				logger.error("保存体验金规则失败: {}", result.getResMsg().toString());
				return result;
			}
			// 生成体验金规则记录
			vasGoldRuleService.insertVasGoldRule(vasGoldRule);
			return new ResponseEntity<>(SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("保存体验金规则失败: ", e);
			return new ResponseEntity<>(ERROR, "保存失败！");
		}
	}

	/**
	 * @Description :更新体验金
	 * @Method_Name : updateVasGoldRule;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午11:35:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResponseEntity<?> updateVasGoldRule(VasGoldRule vasGoldRule) {
		try {
			return vasGoldRuleService.updateVasGoldRuleById(vasGoldRule);
		} catch (Exception e) {
			logger.error("更新体验金规则失败: ", e);
			return new ResponseEntity<>(ERROR, "更新失败!");
		}
	}

	/**
	 * @Description :逻缉删除体验金规则
	 * @Method_Name : deleteVasGoldRule;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午11:37:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseEntity<?> deleteVasGoldRule(VasGoldRule vasGoldRule) {
		try {
			return vasGoldRuleService.deleteVasGoldRuleState(vasGoldRule);
		} catch (Exception e) {
			logger.error("逻缉删除体验金规则失败: ", e);
			return new ResponseEntity<>(ERROR, "删除失败!");
		}
	}

	/**
	 * @Description :保存体验金规则内容校验
	 * @Method_Name : checkVasGoldRuleInfo;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午11:32:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> checkVasGoldRuleInfo(VasGoldRule vasGoldRule) {
		if (vasGoldRule.getPeriod() == null) {
			return new ResponseEntity<>(ERROR, "体验金周期不能为空！");
		}
		if (vasGoldRule.getType() == null) {
			return new ResponseEntity<>(ERROR, "体验金事件不能为空！");
		}
		if (vasGoldRule.getMoney() == null || vasGoldRule.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
			return new ResponseEntity<>(ERROR, "体验金发放金额不能为空或小于等于0！");
		}
		return new ResponseEntity<>(SUCCESS);
	}

}
