package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.model.FundEvaluation;
import com.hongkun.finance.fund.service.FundEvaluationService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 风险测评答案控制层
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.fund.FundEvaluationController
 * @Author : guyuze@yiruntz.com
 */
@Controller
@RequestMapping("/fundEvaluationController")
public class FundEvaluationController {

	private static final Logger logger = LoggerFactory.getLogger(FundEvaluationController.class);

    @Reference
    private FundEvaluationService fundEvaluationService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    /**
	 * 
	 *  @Description    : 查询风险测评答案列表
	 *  @Method_Name    : FundEvaluationList;
	 *  @param fundEvaluation
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月21日 下午16:59:10;
	 *  @Author         : guyuze@hongkun.com.cn;
	 */
	@RequestMapping("/fundEvaluationList")
	@ResponseBody
	ResponseEntity<?> fundEvaluationList(FundEvaluation fundEvaluation, Pager pager) {
		//根据手机号模糊查询
		if(fundEvaluation.getTel() != null){
			List<Integer> regUserIds = new ArrayList<>();
			regUserIds.add(0);
			regUserIds.addAll(regUserService.findUserIdsByTel(fundEvaluation.getTel()));
			fundEvaluation.setRegUserIdList(regUserIds);
		}
		Pager result = fundEvaluationService.findFundEvaluationList(fundEvaluation,pager);
		List<FundEvaluation> list = new ArrayList<>();
		//查询用户手机号和真实名字
		if (!BaseUtil.resultPageHasNoData(result)) {
			list = (List<FundEvaluation>) result.getData();
			for(FundEvaluation eva : list){
				int regUserId = eva.getRegUserId();
				eva.setName(BaseUtil.getRegUserDetail(regUserId, () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId)).getRealName());
				eva.setTel(BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId)).getLogin());
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	/**
	 *
	 *  @Description    : 风险测评答案详情
	 *  @Method_Name    : fundEvaluationDetail;
	 *  @param id
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月23日 下午16:59:10;
	 *  @Author         : guyuze@hongkun.com.cn;
	 */
	@RequestMapping("/fundEvaluationDetail")
	@ResponseBody
	ResponseEntity<?> fundEvaluationDetail(@RequestParam("id") Integer id) {
		System.out.println("FundEvaluationController fundEvaluationDetail start");
		FundEvaluation eva = fundEvaluationService.findFundEvaluationById(id);
		//UserVO userVO = regUserService.findUserWithDetailById(eva.getRegUserId());
		//eva.setName(userVO.getRealName());
		//eva.setTel(userVO.getLogin());
		//优化 先走redis查询实名信息
		int regUserId = eva.getRegUserId();
		eva.setName(BaseUtil.getRegUserDetail(regUserId, () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId)).getRealName());
		eva.setTel(BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId)).getLogin());
		return new ResponseEntity<>(Constants.SUCCESS, eva);
	}
}
