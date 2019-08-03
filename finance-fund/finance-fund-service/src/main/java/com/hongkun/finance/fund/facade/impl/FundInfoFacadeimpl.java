package com.hongkun.finance.fund.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.fund.model.FundEvaluation;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundEvaluationService;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hongkun.finance.user.constants.UserConstants.*;

@Service
public class FundInfoFacadeimpl implements FundInfoFacade{

    private static final Logger logger = LoggerFactory.getLogger(FundInfoFacadeimpl.class);

	@Autowired
	private FundInfoService fundInfoService;
	@Reference
    private RegUserDetailService regUserDetailService;
	@Autowired
	private FundEvaluationService fundEvaluationService;
	
	@Override
	public ResponseEntity<?> findFundInfoVoByCondition(FundInfoVo contidion, Pager pager) {
		Pager result = fundInfoService.findFundInfoVoByCondition(contidion, pager);
		if(!BaseUtil.resultPageHasNoData(result)) {
			result.getData().forEach(tmp->{
				FundInfoVo infoVo = (FundInfoVo) tmp;
				if(infoVo.getModifyUserId()!=null && infoVo.getModifyUserId()!=0) {
					RegUserDetail modifyUser =this.regUserDetailService.findRegUserDetailByRegUserId(infoVo.getModifyUserId());
					if(modifyUser!=null) {
						infoVo.setModifyUserName(modifyUser.getRealName());
					}
				}
				if(infoVo.getCreateUserId()!=null && infoVo.getCreateUserId()!=0) {
					RegUserDetail createUser =this.regUserDetailService.findRegUserDetailByRegUserId(infoVo.getCreateUserId());
					if(createUser!=null) {
						infoVo.setCreateUserName(createUser.getRealName());;
					}
				}
			});
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}
	@Override
	public Integer getFundAuthentication(RegUser regUser) {
		Integer authenticationFlag = 0;//默认未登录
		if(regUser == null) {
			return authenticationFlag;
		}
		// 企业账户  -- 直接跳过认证及测评
		if(regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT){
			authenticationFlag = 4;
			return authenticationFlag;
		}
		// 个人用户
		RegUserDetail regUserDetail =this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		if(regUserDetail == null ||  (regUser.getType()== USER_TYPE_GENERAL && ( StringUtilsExtend.isEmpty(regUserDetail.getIdCard()) ||
				StringUtilsExtend.isEmpty(regUserDetail.getRealName())))) {
			authenticationFlag = 1;//未实名
			return authenticationFlag;
		}

		FundEvaluation fundEvaluation = new FundEvaluation();
		fundEvaluation.setRegUserId(regUser.getId());
		fundEvaluation.setType(FundConstants.EVALUTION_FUND);
		int evaluationResult = fundEvaluationService.findFundEvaluationCount(fundEvaluation);
		if(evaluationResult>0) {
			authenticationFlag = 2;//已测评
		}else{
			authenticationFlag = 3;//实名未测评
		}
		return authenticationFlag;
	}

}
