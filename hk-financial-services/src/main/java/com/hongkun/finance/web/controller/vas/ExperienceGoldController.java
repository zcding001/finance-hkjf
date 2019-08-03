package com.hongkun.finance.web.controller.vas;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 体验金相关的的业务逻辑
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.vas.ExperienceGoldController
 * @Author : yanbinghuang@yiruntz.com 黄艳兵
 */
@Controller
@RequestMapping("/experienceGoldController")
public class ExperienceGoldController {
	@Reference
	private VasSimRecordService vasSimRecordService;
	@Reference
	private RegUserService regUserService;

	@RequestMapping("/findSimRecord")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> findSimRecord(VasSimRecord vasSimRecord, Pager pager) {
		vasSimRecord.setRegUserId(BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId())).getId());
		return new ResponseEntity<>(Constants.SUCCESS, vasSimRecordService.findVasSimRecordList(vasSimRecord, pager));
	}
}
