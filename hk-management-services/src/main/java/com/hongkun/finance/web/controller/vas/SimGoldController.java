package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.FindSimRecordInfoFacade;
import com.hongkun.finance.vas.model.SimGoldVo;
import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.service.VasGoldRuleService;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description : 体验金记录controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.vas.SimGoldController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/simGoldController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class SimGoldController {
	private static final Logger logger = LoggerFactory.getLogger(SimGoldController.class);

	@Reference
	private FindSimRecordInfoFacade findSimRecordInfoFacade;
	@Reference
	private VasSimRecordService vasSimRecordService;
	@Reference
	private VasGoldRuleService vasGoldRuleService;

	/**
	 * @Description :用户体验金查询
	 * @Method_Name : searchByCondition;
	 * @param simGoldCondition
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月29日 下午3:25:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchSimGold")
	@ResponseBody
	public ResponseEntity<?> searchByCondition(SimGoldVo simGoldVo, Pager pager) {
		return findSimRecordInfoFacade.findSimRecordInfo(simGoldVo, pager);
	}

	/**
	 * @Description : 体验金发放
	 * @Method_Name : insertSimGrant;
	 * @param simGoldVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月27日 下午7:20:30;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/saveSimGrant")
	@ResponseBody
	public ResponseEntity<?> saveSimGrant(SimGoldVo simGoldVo) {
		try {
			ResponseEntity<?> result = vasSimRecordService.insertSimGrant(simGoldVo);
			if (result.getResStatus() == Constants.ERROR) {
				logger.error("体验金发放失败: {}", result.getResMsg().toString());
			}
			return result;
		} catch (Exception e) {
			logger.error("体验金发放失败: {}", simGoldVo.toString());
			return new ResponseEntity<>(Constants.ERROR, "发放失败!");
		}
	}

	/**
	 *  @Description    : 批量导入体验金
	 *  @Method_Name    : importUsers
	 *  @param request
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月17日 下午6:08:45
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("simGoldImport")
	@ResponseBody
	public ResponseEntity<?> simGoldImport(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile multipartFile){
		ResponseEntity<?> result = BaseUtil.saveFile(request, multipartFile);
		if(result.getResStatus() == Constants.SUCCESS){
			return this.findSimRecordInfoFacade.simGoldimport((String)result.getResMsg());
		}
		return result;
	}

	/***
	 * @Description : 根据体验金ID，更新体验金状态失效
	 * @Method_Name : updateSimGold;
	 * @param simGoldVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月29日 上午10:09:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateSimGold")
	@ResponseBody
	public ResponseEntity<?> updateSimGold(SimGoldVo simGoldVo) {
		try {
			ResponseEntity<?> result = vasSimRecordService.updateSimGold(simGoldVo);
			if (result.getResStatus() == Constants.ERROR) {
				logger.error("根据体验金ID,更新体验金状态失效, 更新失败: {}", result.getResMsg().toString());
			}
			return result;
		} catch (Exception e) {
			logger.error("根据体验金ID,更新体验金状态失效, 更新失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败!");
		}
	}

	/**
	 * @Description : 查询体验金统计信息
	 * @Method_Name : findSimGoldCountInfo;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月16日 上午10:41:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findSimGoldCountInfo")
	@ResponseBody
	public ResponseEntity<?> findSimGoldCountInfo() {
		return findSimRecordInfoFacade.findSimGoldCountInfo();
	}

	/**
	 * @Description : 根据事件类型查询当前事件类型的状态
	 * @Method_Name : findSimGoldRuleState;
	 * @param type
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月19日 下午5:39:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findSimGoldRuleState")
	@ResponseBody
	public ResponseEntity<?> findSimGoldRuleState(int type) {
		VasGoldRule vasGoldRule = vasGoldRuleService.findVasGoldRuleByTypeAndState(type,
				VasConstants.VAS_RULE_STATE_START);
		if (vasGoldRule == null) {
			return new ResponseEntity<>(Constants.ERROR);
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}
}
