package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 股权项目信息控制层
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.fund.FundInfoController
 * @Author : yunlongliu@yiruntz.com
 */
@Controller
@RequestMapping("/fundInfoController")
public class FundInfoController {

	private static final Logger logger = LoggerFactory.getLogger(FundInfoController.class);

    @Reference
    private FundInfoService fundInfoService;
    @Reference
    private FundInfoFacade fundInfoFacade;
    @Reference
    private RegUserService regUserDetailService;
	/**
	 * 
	 *  @Description    : 查询股权项目信息列表
	 *  @Method_Name    : searchFundInfoList;
	 *  @param fundInfo
	 *  @param pager
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年4月28日 下午2:59:10;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/searchFundInfoList")
	@ResponseBody
	ResponseEntity<?> searchFundInfoList(FundInfoVo fundInfoVo, Pager pager) {
		return fundInfoFacade.findFundInfoVoByCondition(fundInfoVo, pager);
	}
	/**
	 * 
	 *  @Description    : 股权项目信息
	 *  @Method_Name    : deleteFundInfo;
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月2日 下午3:37:40;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/updateFundInfoState")
	@ResponseBody
    @Token
	@ActionLog(msg = "股权项目信息, 信息id: {args[0]} , 信息状态state: {args[1]}")
	ResponseEntity<?> updateFundInfoState(@RequestParam("id")Integer id,@RequestParam("state")Integer state){
		if(id==null || state==null) {
			return new ResponseEntity<>(Constants.ERROR);
		}
		FundInfo fundInfo = new FundInfo();
		fundInfo.setId(id);
		fundInfo.setModifyTime(new Date());
		fundInfo.setModifyUserId(BaseUtil.getLoginUserId());
		fundInfo.setState(state);
		fundInfoService.updateFundInfo(fundInfo);
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	/**
	 * 
	 *  @Description    : 修改股权项目信息
	 *  @Method_Name    : updateFundInfo;
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月2日 下午3:37:40;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/updateFundInfo")
	@ResponseBody
    @Token
	@ActionLog(msg = "修改股权项目信息, 信息id: {args[0].id} , 信息状态state: {args[0].state}")
	ResponseEntity<?> updateFundInfo(@Valid FundInfo fundInfo){
		fundInfo.setModifyTime(new Date());
		fundInfo.setModifyUserId(BaseUtil.getLoginUserId());
		if(StringUtilsExtend.isEmpty(fundInfo.getStepValue())){
			fundInfo.setStepValue(BigDecimal.ZERO);
		}
		fundInfoService.updateFundInfo(fundInfo);
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	/**
	 * 
	 *  @Description    : 新增股权项目信息
	 *  @Method_Name    : updateFundInfo;
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月2日 下午3:37:40;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/addFundInfo")
	@ResponseBody
    @Token
	@ActionLog(msg = "新增股权项目信息, 产品类型: {args[0].projectId}, 是否有项目信息: {args[0].infoExist}, " +
			"无项目时的项目描述: {args[0].introduction}, 项目信息名称: {args[0].name}, 起投金额: {args[0].lowestAmount}, " +
			"产品亮点: {args[0].highlights}, 投资范围: {args[0].investRange}, 运作方式: {args[0].operationStyle}, " +
			"备注: {args[0].remark}")
		ResponseEntity<?> addFundInfo(@Valid FundInfo fundInfo){
		fundInfo.setModifyTime(new Date());
		fundInfo.setModifyUserId(BaseUtil.getLoginUserId());
		fundInfo.setCreateTime(new Date());
		fundInfo.setState(FundConstants.FUND_INFO_STATE_WAIT_SHELF);
		fundInfo.setCreateUserId(BaseUtil.getLoginUserId());
		if(StringUtilsExtend.isEmpty(fundInfo.getMinRate())) {
			fundInfo.setMinRate(0.0);
		}
		if(StringUtilsExtend.isEmpty(fundInfo.getMaxRate())) {
			fundInfo.setMaxRate(0.0);
		}
		if(StringUtilsExtend.isEmpty(fundInfo.getIntroduction())){
			fundInfo.setIntroduction("");
		}
		if(StringUtilsExtend.isEmpty(fundInfo.getRemark())){
			fundInfo.setRemark("");
		}
		if(StringUtilsExtend.isEmpty(fundInfo.getInvestRange())){
			fundInfo.setInvestRange("无");
		}
		fundInfoService.insertFundInfo(fundInfo);
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	/**
	 * 
	 *  @Description    : 获取股权项目详情
	 *  @Method_Name    : updateFundInfo;
	 *  @param id
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月2日 下午3:37:40;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/findFundInfoById")
	@ResponseBody
	ResponseEntity<?> findFundInfoById(@RequestParam("id") Integer id){
		return new ResponseEntity<>(Constants.SUCCESS,fundInfoService.findFundInfoById(id));
	}
	/**
	 * 
	 *  @Description    : 更新股权项目预约状态
	 *  @Method_Name    : deleteFundInfo;
	 *  @param id
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年5月2日 下午3:37:40;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	@RequestMapping("/updateFundInfoSubscribeState")
	@ResponseBody
    @Token
	@ActionLog(msg = "更新股权项目预约状态, 信息id: {args[0]} , 信息状态subscribeState: {args[1]}")
	ResponseEntity<?> updateFundInfoSubscribeState(@RequestParam("id")Integer id,@RequestParam("subscribeState")Integer subscribeState){
		if(id==null || subscribeState==null) {
			return new ResponseEntity<>(Constants.ERROR);
		}
		FundInfo fundInfo = new FundInfo();
		fundInfo.setId(id);
		fundInfo.setModifyTime(new Date());
		fundInfo.setModifyUserId(BaseUtil.getLoginUserId());
		fundInfo.setSubscribeState(subscribeState);
		fundInfoService.updateFundInfo(fundInfo);
		return new ResponseEntity<>(Constants.SUCCESS);
	}
}
