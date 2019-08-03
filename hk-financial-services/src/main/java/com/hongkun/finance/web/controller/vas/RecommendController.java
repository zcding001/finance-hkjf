package com.hongkun.finance.web.controller.vas;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.vo.RecommendEarnVO;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 处理前台关于推荐好友相关的的业务逻辑
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.vas.RecommendController
 * @Author : yanbinghuang@yiruntz.com 黄艳兵
 */
@Controller
@RequestMapping("/recommendController")
public class RecommendController {
	@Reference
	private RecommendEarnFacade recommendEarnFacade;
	@Reference
	private VasBidRecommendEarnService vasBidRecommendEarnService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserFriendsService regUserFriendsService;

	/**
	 * @Description : 查询我的推荐奖金明细
	 * @Method_Name : findMyRecommendFriends;
	 * @param recommendEarnVO
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月29日 下午5:32:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findMyRecommendEarn")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> findMyRecommendEarn(RecommendEarnVO recommendEarnVO, Pager pager) {
		recommendEarnVO.setRecommendRegUserId(BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId())).getId());
		return recommendEarnFacade.findMyRecommendEarn(recommendEarnVO, pager);
	}

	/**
	 * @Description : 查询我的好友推荐关系
	 * @Method_Name : findMyRecommendFriends;
	 * @param recommendEarnVO
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月9日 下午3:03:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findMyRecommendFriends")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> findMyRecommendFriends(RegUserFriends regUserFriends, Pager pager) {
		regUserFriends.setRecommendId(BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId())).getId());
		return regUserFriendsService.findRegUserFriendsList(regUserFriends, pager);
	}

	/**
	 * @Description : 我的推荐好友--》立即推荐页面
	 * @Method_Name : toRecommend;
	 * @param request
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月29日 下午5:32:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/toRecommend")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> toRecommend(HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
		RegUserDetail userDetail = BaseUtil.getRegUserDetail(
				() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUser().getId()));
		return vasBidRecommendEarnService.recommendFriendInfo(basePath, userDetail.getInviteNo(),
				userDetail.getRegUserId());
	}
}
