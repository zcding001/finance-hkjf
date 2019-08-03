package com.hongkun.finance.web.controller.vas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.vo.RecommendEarnVO;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 推荐奖的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.vas.RecommendEarnController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/recommendEarnController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class RecommendEarnController {
	private static final Logger logger = LoggerFactory.getLogger(RecommendEarnController.class);

	@Reference
	private RecommendEarnFacade recommendEarnFacade;
	@Reference
	private VasBidRecommendEarnService vasBiddRecommendEarnService;
	@Reference
    private RegUserService regUserService;
	@Reference
    private RegUserDetailService regUserDetailService;

	/**
	 * @Description :查询审核好友推荐和查询好友推荐奖金发放 （用于多条件查询）
	 * @Method_Name : searchByCondition;
	 * @param RecommendEarnVO
	 * @param pager
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午10:55:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchRecommendEarnByCondition")
	@ResponseBody
	public ResponseEntity<?> searchByCondition(RecommendEarnVO recommendEarnVO, Pager pager) {
		return recommendEarnFacade.findRecommendEarnInfo(recommendEarnVO, pager);
	}

	/**
	 * @Description : 单表分页检索推荐奖信息
	 * @Method_Name : recommendEarnList
	 * @param pager
	 * @param vasBiddRecommendEarn
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月22日 下午2:24:51
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("/recommendEarnList")
	@ResponseBody
	public ResponseEntity<?> recommendEarnList(Pager pager, RecommendEarnVO recommendEarnVO) {
		return this.recommendEarnFacade.recommendEarnList(recommendEarnVO, pager);
	}

	/**
	 * @Description :好友推荐审核
	 * @Method_Name : updateRecommendEarnByIds;
	 * @param recommendEarnVO
	 * @param opinion
	 *            备注
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月17日 下午6:24:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateRecommendEarnByIds")
	@ResponseBody
	public ResponseEntity<?> updateRecommendEarnByIds(RecommendEarnVO recommendEarnVO, String opinion) {
		return recommendEarnFacade.updateRecommendEarnByIds(recommendEarnVO.getRecommendEarnIds(),
				recommendEarnVO.getState(), opinion);
	}

	/***
	 * @Description :发放佣金
	 * @Method_Name : sendReconmmendEarn;
	 * @param recommendEarnVO
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月18日 下午2:32:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/sendReconmmendEarn")
	@ResponseBody
	public ResponseEntity<?> sendReconmmendEarn(RecommendEarnVO recommendEarnVO) {
		return recommendEarnFacade.sendReconmmendEarn(recommendEarnVO.getRecommendEarnIds());
	}

	/**
	 * @Description : 佣金查询及统计
	 * @Method_Name : findRecommendEarnCountInfo;
	 * @param recommendEarnVO
	 * @param page
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月19日 上午11:15:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findRecommendEarnCountInfo")
	@ResponseBody
	public ResponseEntity<?> findRecommendEarnCountInfo(RecommendEarnVO recommendEarnVO, Pager page) {
		return recommendEarnFacade.findRecommendEarnCountInfo(recommendEarnVO, page);
	}
	/**
	 *  @Description    : 查询推广来源信息
	 *  @Method_Name    : findSpreadSourceInfo;
	 *  @param userVO
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年8月20日 下午5:02:02;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findSpreadSourceInfo")
    @ResponseBody
	public ResponseEntity<?> findSpreadSourceInfo(UserVO userVO,Pager pager){
	   userVO.setTypes(Arrays.asList(UserConstants.USER_TYPE_GENERAL,UserConstants.USER_TYPE_ENTERPRISE,UserConstants.USER_TYPE_TENEMENT));
	   return new ResponseEntity<>(Constants.SUCCESS, this.regUserService.listConditionPage(userVO, pager));
	}
	/**
	 *  @Description    : 更新推广来源
	 *  @Method_Name    : updateSpreadSource;
	 *  @param regUserId
	 *  @param groupCode
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年8月20日 下午5:45:01;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateSpreadSource")
    @ResponseBody
    public ResponseEntity<?> updateSpreadSource(Integer regUserId,String groupCode){
	    return this.regUserService.updateSpreadSource(regUserId, groupCode);
    }
	/***
	 *  @Description    : 查询物业公司机构编码
	 *  @Method_Name    : findGroupCodeInfo;
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年8月20日 下午5:14:13;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    @RequestMapping("/findGroupCodeInfo")
    @ResponseBody
    public ResponseEntity<?> findGroupCodeInfo(@RequestParam(value = "regUserId", required = false) Integer regUserId){
       UserVO userVO = new UserVO();
       userVO.setTypes(Arrays.asList(UserConstants.USER_TYPE_TENEMENT));
       List<UserVO> newUserVo=this.regUserService.findUserWithDetailByInfo(userVO);
       UserVO regUser = null;
       if(regUserId != null){
           regUser=this.regUserService.findUserWithDetailById(regUserId);
       }
       return new ResponseEntity<>(Constants.SUCCESS).addParam("groupCode", regUser==null?"":regUser.getExtenSource()).addParam("login", regUser == null ? "" : regUser.getLogin()).addParam("groupCodeList", newUserVo);
    }
    /**
     *  @Description    : 物业业绩查询
     *  @Method_Name    : findPropertyAchievement;
     *  @param recommendEarnVO
     *  @param pager
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年8月21日 下午5:32:33;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findPropertyAchievement")
    @ResponseBody
    public ResponseEntity<?> findPropertyAchievement(RecommendEarnVO recommendEarnVO,Pager pager){
      return recommendEarnFacade.findPropertyAchievement(recommendEarnVO, pager);
    }
    
}
