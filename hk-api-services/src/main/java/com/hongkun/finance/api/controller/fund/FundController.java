package com.hongkun.finance.api.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.facade.FundAdvisoryFacade;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.FundUserInfo;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.service.*;
import com.hongkun.finance.fund.util.FundUtils;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

import static com.hongkun.finance.fund.constants.FundConstants.*;
import static com.hongkun.finance.user.constants.UserConstants.*;
import static com.hongkun.finance.user.support.security.SecurityConstants.NO_LOGIN_MESSAGE;


/**
 * @Description : 股权
 * @Project : hk-api-services
 * @Program Name : com.hongkun.finance.api.controller.fund.FundConroller.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@Controller
@RequestMapping("/fundController")
public class FundController {
	private static final Logger logger = LoggerFactory.getLogger(FundController.class);
	
    @Reference
    private FundInfoService fundInfoService;
    @Reference
    private FundAdvisoryService fundAdvisoryService;
    @Reference
	private FundAdvisoryFacade fundAdvisoryFacade;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private FundEvaluationService fundEvaluationService;
    @Reference
	private RegUserService regUserService;
    @Reference
    private FundInfoFacade fundInfoFacade;
    @Reference
	private FundAgreementService fundAgreementService;
    @Reference
    private FundUserInfoService fundUserInfoService;
	@Value(value = "${oss.url.hkjf}")
	private String ossUrl;


    /***
     * 
     *  @Description    : 获取首页股权信息
     *  @Method_Name    : findFundInfos;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/findIndexFundInfos")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> findIndexFundInfos() {
		RegUser regUser = BaseUtil.getLoginUser();
		List<FundInfoVo> fundInfoVos = fundInfoService.findIndexFundInfos();
        Map<String,Object>  resultData = new HashMap<String,Object>();
		for (FundInfoVo fundInfoVo : fundInfoVos) {
			fundInfoVo.setLowestAmount(fundInfoVo.getLowestAmount().divide(BigDecimal.valueOf(10000),2,RoundingMode.HALF_UP));
			if(fundInfoVo.getParentType() == FUND_PROJECT_PARENT_TYPE_PRIVATE) {//私募
				resultData.put("fund_private", fundInfoVo);
			}else if(fundInfoVo.getParentType() == FUND_PROJECT_PARENT_TYPE_ABROAD) {//海外
				resultData.put("fund_abroad", fundInfoVo);
			}else if(fundInfoVo.getParentType() == FUND_PROJECT_PARENT_TYPE_TRUST) {//股权
				resultData.put("fund_trust", fundInfoVo);
			}
		}
		AppResultUtil.ExtendMap extendMap = AppResultUtil.mapOfObjectInPropertiesNullable(resultData,Constants.SUCCESS,"查询成功！", false,
				"id",//id
				"parentType",//父类型
				"name",//项目名称
				"termUnit",//续存单位
				"termValue",//续存期限
				"lowestAmount",//起投金额
				"lowestAmountUnit",//起投单位
				"management",//管理机构
				"establishedTime",//成立日期
				"revenueType",//固定收益类方式
				"openDateFlag",//是否开放日
				"openDateDescribe",//开放日描述				
				"highlights",//产品亮点
				"minRate",//年化率
				"maxRate").addResParameter("authenticationFlag",fundInfoFacade.getFundAuthentication(regUser));

		if (!MapUtils.isEmpty(extendMap)) {
			extendMap.forEach((key, value) -> {
				if(!"authenticationFlag".equals(key)){
					//结果标识过滤
					if (value instanceof Map) {
						AppResultUtil.ExtendMap tempMap = (AppResultUtil.ExtendMap)value;
						if(null != tempMap.get("minRate")){
							tempMap.put("minRate",new BigDecimal(tempMap.get("minRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
									.stripTrailingZeros().toPlainString());
						}
						if(null != tempMap.get("maxRate")){
							tempMap.put("maxRate",new BigDecimal(tempMap.get("maxRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
									.stripTrailingZeros().toPlainString());
						}
					}
				}
			});
		}
		return extendMap;
			
	}

	/***
	 *  @Description    : 针对无投资资质用户获取首页股权描述
	 *  @Method_Name    : findIndexFundInfos4Black;
	 *  @return
	 *  @return         : Map<String,Object>;
	 *  @Creation Date  : 2018-12-19 18:17:01
	 *  @Author         : xgyuze;
	 */
	@RequestMapping("/findIndexFundInfos4Black")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> findIndexFundInfos4Black() {
		logger.info("findIndexFundInfos4Black无资质用户查询基金信息");
		RegUser regUser = BaseUtil.getLoginUser();
		List<Map<String,Object>> proList = new ArrayList<>();
		Map<String,Object>  pro = new HashMap<String,Object>();
		pro.put("type" , 1);
		pro.put("introduction","金融市场中常说的“私募基金”或“地下基金”，是一种非公开宣传的，私下向特定投资人募集资金进行的一种集合投资。其方式基本有两种，一是基于签订委托投资合同的契约型集合投资基金，二是基于共同出资入股成立股份公司的公司型集合投资基金。");
		proList.add(pro);
		pro = new HashMap<>();
		pro.put("type" , 2);
		pro.put("introduction","海外基金(OverseaFund)：由国外投资信托公司发行的基金。透过海外基金的方式进行投资，不但可分享全球投资机会和利得，亦可达到分散风险、专业管理、节税与资产移转的目的。");
		proList.add(pro);
		pro = new HashMap<>();
		pro.put("type" , 3);
		pro.put("introduction","信托产品是指一种为投资者提供了低风险、稳定收入回报的金融理财产品。信托品种在产品设计上非常多样，各自都会有不同的特点。各个信托品种在风险和收益潜力方面可能会有很大的分别。");
		proList.add(pro);
		pro = new HashMap<>();
		pro.put("type" , 4);
		pro.put("introduction","房产投资是指以房地产为对象来获取收益的投资行为。投资房产的对象按地段分投资市区房和郊区房；按交付时间分投资现房和期房；按卖主分投资一手房和二手房；按房产类别分投资住宅和投资商铺。");
		proList.add(pro);
		AppResultUtil.ExtendMap extendMap = AppResultUtil.successOfListInProperties(proList,"查询成功！").addResParameter("authenticationFlag",fundInfoFacade.getFundAuthentication(regUser));
		return extendMap;

	}
	
    /***
     * 
     *  @Description    : 获取产品股权信息列表
     *  @Method_Name    : findFundInfoList;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/findFundInfoList")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> findFundInfoList(@RequestParam("parentType") Integer parentType,Pager pager) {
		logger.info("findFundInfoList获取产品股权信息列表,股权父类型:{}",parentType);
		FundInfoVo condition = new FundInfoVo();
		condition.setState(FUND_INFO_STATE_SHELF);
		condition.setInfoExist(FUND_INFO_EXIST_YES);
		condition.setParentType(parentType);
		condition.setSortColumns(" info.subscribe_state = 1 DESC,info.create_time");
		Pager resultPager = fundInfoService.findFundInfoVoByCondition(condition, pager);
		if(resultPager == null || resultPager.getData().isEmpty()) {
			return AppResultUtil.errorOfMsg("暂无股权信息！");
		}
		List<FundInfoVo> resultList = new ArrayList<FundInfoVo>();
		resultPager.getData().forEach(fundInfoVo -> {
			FundInfoVo infoVo = (FundInfoVo) fundInfoVo;
			if(CompareUtil.gtZero(infoVo.getLowestAmount())) {
				infoVo.setLowestAmount(infoVo.getLowestAmount().divide(BigDecimal.valueOf(10000),2,RoundingMode.HALF_UP));
			}
			resultList.add(infoVo);
		});
		return  AppResultUtil.successOfListInProperties(resultList, "查询成功!",
				"id",//id
				"parentType",//父类型
				"name",//项目名称
				"termUnit",//续存单位
				"termValue",//续存期限
				"lowestAmount",//起投金额
				"lowestAmountUnit",//起投单位
				"management",//管理机构
				"minRate",//年化率
				"maxRate").processObjInList((tempMap) ->{
					if(null != tempMap.get("minRate")){
						//tempMap.put("minRate",tempMap.get("minRate").toString());
						//将小数末尾0去掉
						tempMap.put("minRate",new BigDecimal(tempMap.get("minRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
								.stripTrailingZeros().toPlainString());
					}
					if(null != tempMap.get("maxRate")){
						//tempMap.put("maxRate",tempMap.get("maxRate").toString());
						tempMap.put("maxRate",new BigDecimal(tempMap.get("maxRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
								.stripTrailingZeros().toPlainString());
					}
		});
	}
	
    /***
     *
     *  @Description    : 获取产品股权信息详情
     *  @Method_Name    : getFundInfoDetail;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/getFundInfoDetail")
	@ResponseBody
	public Map<String, Object> getFundInfoDetail(@RequestParam("id") Integer id) {
		logger.info("getFundInfoDetail获取产品股权信息列表,id:{}",id);
		RegUser regUser = BaseUtil.getLoginUser();
		FundInfoVo fundInfoVoDtc = new FundInfoVo();
		fundInfoVoDtc.setId(id);
		FundInfoVo fundInfoVo = fundInfoService.getFundInfo(fundInfoVoDtc);
		if(fundInfoVo == null) {
			return AppResultUtil.errorOfMsg("股权信息不存在！");
		}
		fundInfoVo.setOpenDateFlag(FundUtils.checkOpenDate(fundInfoVo)== true ? 1:0);
		fundInfoVo.setOpenDateDescribe(FundUtils.getFundOpenTime(fundInfoVo));
		fundInfoVo.setInvestRange(StringUtilsExtend.delHTMLTag(fundInfoVo.getInvestRange()));
		if(CompareUtil.gtZero(fundInfoVo.getLowestAmount())) {
			fundInfoVo.setLowestAmount(fundInfoVo.getLowestAmount().divide(BigDecimal.valueOf(10000),2,RoundingMode.HALF_UP));
		}
		// 处理海外基金详情
		if(fundInfoVo.getState() == FUND_INFO_STATE_OFF_SHELF){
			fundInfoVo.setSubscribeState(FUND_INFO_SUBSCRIBE_STATE_OFF);
		}
		fundInfoVo.setAuditState(FUND_ADVISORY_STATE_INIT);
		if(fundInfoVo.getParentType().equals(FUND_PROJECT_PARENT_TYPE_ABROAD)){
			FundAgreement agreement = new FundAgreement();
			agreement.setRegUserId(regUser.getId());
			agreement.setFundInfoId(id);
			FundAgreement info = fundAgreementService.findFundAgreementInfo(agreement);
			if(null != info){
				if(info.getState().equals(FUND_ADVISORY_STATE_UNDER_AUDIT)
						|| info.getState().equals(FUND_ADVISORY_STATE_QULICATION_PASS)){
					fundInfoVo.setAuditState(FUND_ADVISORY_STATE_UNDER_AUDIT);
				}
				if(info.getFundAdvisoryId() == 0){
					fundInfoVo.setAuditState(FUND_ADVISORY_STATE_INIT);
				}
			}
		}
		AppResultUtil.ExtendMap extendMap = AppResultUtil.successOfInProperties(fundInfoVo,
				"id",//id
				"parentType",//父类型
				"name",//项目名称
				"termUnit",//续存单位
				"termValue",//续存期限
				"lowestAmount",//起投金额
				"lowestAmountUnit",//起投单位
				"management",//管理机构
				"investRange",//项目介绍
				"subscribeState",//预约状态
				"opendayType",//工作日类型
				"customizeType",//期限类型
				"customizeValue",//期限值
				"operationStyle",//运作方式
				"highlights",//产品亮点
				"startTime",//开始时间
				"endTime",//结束时间
				"establishedTime",//成立日期
				"revenueType",//固定收益类方式
				"openDateFlag",//是否开放日
				"openDateDescribe",//开放日描述
				"minRate",//年化率
				"maxRate",
				"auditState"//审核状态
		);
		// double 返回精度处理
		if(null != extendMap.get("minRate")){
			//extendMap.put("minRate",extendMap.get("minRate").toString());
			extendMap.put("minRate",new BigDecimal(extendMap.get("minRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
					.stripTrailingZeros().toPlainString());
		}
		if(null != extendMap.get("maxRate")){
			//extendMap.put("maxRate",extendMap.get("maxRate").toString());
			extendMap.put("maxRate",new BigDecimal(extendMap.get("maxRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
					.stripTrailingZeros().toPlainString());
		}

		return extendMap;
	}
    /***
     * 
     *  @Description    : 获取股权信息详情认证权限
     *  @Method_Name    : getFundAuthentication;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/getFundAuthentication")
	@ResponseBody
	public Map<String, Object> getFundAuthentication() {
		RegUser regUser = BaseUtil.getLoginUser();
		return AppResultUtil.successOfMsg("成功").addResParameter("authenticationFlag",fundInfoFacade.getFundAuthentication(regUser));
	}
    /***
     * 
     *  @Description    : 预约股权项目
     *  @Method_Name    : reservationFund;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/reservationFund")
	@ResponseBody
	@ActionLog(msg = "预约股权项目, 项目id: {args[0]}, 电话号码: {args[1]}")
	public Map<String, Object> reservationFund(@RequestParam("infoIds") String infoIds,@RequestParam("tel") String tel) {
		logger.info("reservationFund预约股权项目,项目id:{},电话号码:{}",infoIds,tel);
		RegUser regUser = BaseUtil.getLoginUser();
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
		FundInfoVo fundInfoVoDtc = new FundInfoVo();
		fundInfoVoDtc.setId((Integer.valueOf(infoIds)));
		fundInfoVoDtc.setState(FUND_INFO_STATE_SHELF);
		FundInfoVo fundInfoVo = fundInfoService.getFundInfo(fundInfoVoDtc);
		if(null == fundInfoVo){
			return AppResultUtil.errorOfMsg("该项目信息不存在!");
		}
		if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
			return AppResultUtil.errorOfMsg(ERROR_CODE_FORBID_ENTERPRISE,ERROR_MSG_FORBID_ENTERPRISE);
		}
		if(StringUtils.isEmpty(tel) ){
			return AppResultUtil.errorOfMsg("预约信息不完整!");
		}
		if(fundInfoVo.getSubscribeState() == FUND_INFO_SUBSCRIBE_STATE_OFF){
			return AppResultUtil.errorOfMsg("该产品已停约，请后续关注!");
		}
		if(!FundUtils.checkOpenDate(fundInfoVo)){
			return AppResultUtil.errorOfMsg("该产品未在开放日，请后续关注！");
		}
		if(!(FUND_PROJECT_PARENT_TYPE_ABROAD == fundInfoVo.getParentType()) ){
			int count = this.fundAdvisoryService.findAdvisoryCount(regUser.getId(),fundInfoVo.getParentType());
			if(count >= 3 ){
				return AppResultUtil.errorOfMsg("每种产品每天只能预约3次!");
			}
		}
		Integer projectParentType = this.fundAdvisoryService.findProjectParentTypeByType(fundInfoVo.getProjectId());
		FundAdvisory fundAdvisory = new FundAdvisory();
		fundAdvisory.setRegUserId(regUser.getId());
		fundAdvisory.setInfoIds(infoIds);
		fundAdvisory.setName(regUserDetail.getRealName());
		fundAdvisory.setTel(tel);
		fundAdvisory.setModifyUserId(regUser.getId());
		fundAdvisory.setProjectParentType(projectParentType);
		this.fundAdvisoryService.insertFundAdvisory(fundAdvisory);
		return AppResultUtil.successOfMsg("预约成功！");
	}

    /***
     *  @Description    : 预约记录
     *  @Method_Name    : getAdvisoryRecord;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	@RequestMapping("/getAdvisoryRecord")
	@ResponseBody
	public Map<String, Object> getAdvisoryRecord(@RequestParam(value = "type", required = false) Integer type,Pager pager) {
		logger.info("getAdvisoryRecord预约记录,项目父类型:{}",type);
		RegUser loginUser = BaseUtil.getLoginUser();
		pager.setInfiniteMode(true);
		if(null == type){
			type = FUND_PROJECT_PARENT_TYPE_PRIVATE;
		}
		FundAdvisoryVo fundAdvisoryVo = new FundAdvisoryVo();
		fundAdvisoryVo.setProjectParentType(type);
		fundAdvisoryVo.setRegUserId(loginUser.getId());
		fundAdvisoryVo.setSortColumns("create_time desc");
		Pager resultPager = this.fundAdvisoryFacade.findFundAdvisoryList(fundAdvisoryVo,pager);
		// 部分字段特殊处理
		if(resultPager != null && resultPager.getData() != null && resultPager.getData().size() > 0){
			List<FundAdvisoryVo> fundAdvisories = (List<FundAdvisoryVo>) resultPager.getData();
			fundAdvisories.stream().forEach((e) ->{
				e.setProjectNames(e.getProjectNames());
				if(e.getProjectParentType() != FundConstants.FUND_PROJECT_PARENT_TYPE_ABROAD){
					e.setTel(FundUtils.transferTel(e.getTel()));
				}
			});
			resultPager.setData(fundAdvisories);
		}
		return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id", "projectNames","name",
				"tel","createTime","auditState","reason","infoIds","fundAgreementId","investAmount","minRate","maxRate",
					"termUnit","termValue","lowestAmountUnit").processObjInList((tempMap) ->{
						if(null != tempMap.get("minRate")){
							//tempMap.put("minRate",tempMap.get("minRate").toString());
							tempMap.put("minRate",new BigDecimal(tempMap.get("minRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
									.stripTrailingZeros().toPlainString());
						}
						if(null != tempMap.get("maxRate")){
							//tempMap.put("maxRate",tempMap.get("maxRate").toString());
							tempMap.put("maxRate",new BigDecimal(tempMap.get("maxRate").toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN)
									.stripTrailingZeros().toPlainString());
						}
		});
	}


	/**
	 *  @Description    ：测评获取用户信息
	 *  @Method_Name    ：getUserInfoForRiskEvaluation
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年05月09日 09:47
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("getUserInfoForRiskEvaluation")
	public ModelAndView getUserInfoForRiskEvaluation(@RequestParam("source") String source){
		logger.info("getUserInfoForRiskEvaluation测评获取用户信息");
		RegUser loginUser = BaseUtil.getLoginUser();
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
		ModelAndView mv = new ModelAndView("fund/riskEvaluation");
		mv.addObject("loginName",regUserDetail.getRealName());
		mv.addObject("tel",loginUser.getLogin());
		mv.addObject("idCard",FundUtils.transferIdCard(regUserDetail.getIdCard()));
		mv.addObject("currentTime",DateUtils.getCurrentDate(DateUtils.DATE));
		mv.addObject("regUserId",loginUser.getId());
		mv.addObject("source",source);
		return mv;
	}


	/**
	 *  @Description    ：保存股权测评信息
	 *  @Method_Name    ：saveRiskEvaluation
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年05月09日 10:40
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("saveRiskEvaluation")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ActionLog(msg = "保存股权测评信息, 用户id: {args[0]}, 选项: {args[1]}, 得分: {args[2]}")
	public Map<String,Object> saveRiskEvaluation(@RequestParam("regUserId") Integer regUserId,
												 @RequestParam("answers") String answers,@RequestParam("score") Integer score){
		logger.info("saveRiskEvaluation保存股权测评信息,用户id:{},选项:{},得分:{}",regUserId,answers,score);
		return AppResultUtil.mapOfResponseEntity(
				fundEvaluationService.saveRiskEvalution(regUserId,answers, score));
	}


	/**
	 *  @Description    ：海外基金预约前项目校验
	 *  @Method_Name    ：validateFundBeforeSubmit
	 *  @param infoIds
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月05日 16:14
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("/validateFundBeforeSubmit")
	@ResponseBody
	public Map<String, Object> validateFundBeforeSubmit(@RequestParam("infoIds") String infoIds) {
		logger.info("validateFundBeforeSubmit海外基金预约前项目校验,项目id:{},电话号码:{}",infoIds);
		RegUser regUser = BaseUtil.getLoginUser();
		if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
			return AppResultUtil.errorOfMsg(ERROR_CODE_FORBID_ENTERPRISE,ERROR_MSG_FORBID_ENTERPRISE);
		}
		FundInfoVo fundInfoVoDtc = new FundInfoVo();
		fundInfoVoDtc.setId((Integer.valueOf(infoIds)));
		fundInfoVoDtc.setState(FUND_INFO_STATE_SHELF);
		FundInfoVo fundInfoVo = fundInfoService.getFundInfo(fundInfoVoDtc);
		if(null == fundInfoVo){
			return AppResultUtil.errorOfMsg("该项目信息不存在!");
		}

		if(fundInfoVo.getSubscribeState() == FUND_INFO_SUBSCRIBE_STATE_OFF){
			return AppResultUtil.errorOfMsg("该产品已停约，请后续关注!");
		}
		if(!FundUtils.checkOpenDate(fundInfoVo)){
			return AppResultUtil.errorOfMsg("该产品未在开放日，请后续关注！");
		}
		return AppResultUtil.successOfMsg("成功！");
	}

	/**
	 *  @Description    ：海外基金跳转认购协议页面
	 *  @Method_Name    ：toFundAgreementPage
	 *  @param source
	 *  @param fundInfoId
	 *  @return org.springframework.web.servlet.ModelAndView
	 *  @Creation Date  ：2018年07月04日 09:28
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("toFundAgreementPage")
	@Token(operate = Ope.ADD)
	public ModelAndView toFundAgreementPage(HttpServletRequest request,@RequestParam("source") String source, @RequestParam("fundInfoId") Integer fundInfoId ){
		logger.info("toFundAgreementPage海外基金跳转认购协议页面,来源source:{},股权id:{}",source,fundInfoId);
		RegUser loginUser = BaseUtil.getLoginUser();
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
		ModelAndView mv = new ModelAndView("fund/fundAgreementStep1");
		FundInfo fundInfo = fundInfoService.findFundInfoById(fundInfoId);
		FundAgreement fundAgreement = new FundAgreement();
		fundAgreement.setRegUserId(loginUser.getId());
		FundAgreement info  = fundAgreementService.findFundAgreementInfo(fundAgreement);
		//info.setLowestAmountUnit(fundInfo.getLowestAmountUnit());  没有预约过 info可能为空
		mv.addObject("sessionId",request.getParameter("sessionId"));
		mv.addObject("info",info);
		mv.addObject("regUserId",loginUser.getId());
		mv.addObject("source",source);
		mv.addObject("fundInfoId",fundInfoId);
		mv.addObject("birthday",FundUtils.idCardTransferToBir(regUserDetail.getIdCard()));
		mv.addObject("bottomAmount",fundInfo.getLowestAmount());
		mv.addObject("stepValue",fundInfo.getStepValue());
		mv.addObject("lowestAmountUnit",fundInfo.getLowestAmountUnit());
		return mv;
	}



	/**
	 *  @Description    ：初始化海外基金预约协议
	 *  @Method_Name    ：initFundAgreement
	 *  @param fundAgreement
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月04日 10:00
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("initFundAgreement")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@Token(operate = Ope.REFRESH)
	public Map<String,Object> initFundAgreement(@ModelAttribute FundAgreement fundAgreement,HttpServletRequest request,
												@RequestParam("sessionId") String sessionId) {
		logger.info("initFundAgreement:初始化海外基金预约协议,用户登录信息sessionId:{}", sessionId);
		RegUser regUser = JedisClusterUtils.getObjectForJson(Constants.SESSION_ID_KEY_PREFIX + sessionId, RegUser.class);
		if (regUser == null) {
			return AppResultUtil.errorOfMsg(UserConstants.NO_LOGIN,NO_LOGIN_MESSAGE);
		}
		RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		String realName = regUserDetail.getRealName();
		if(StringUtils.isNotEmpty(realName)){
			if(!realName.contains(fundAgreement.getUserName()) || !realName.contains(fundAgreement.getUserSurname())){
				return AppResultUtil.errorOfMsg(ERROR_CODE_NAME,ERROR_MSG_NAME);
			}
		}

		// 上传签名
		String signature = "";
		try {
			String signatureBytes = request.getParameter("signatureBytes");
			//使用java.util.Base64 优化转码 （BASE64Decoder可能在以后发行版删除）
			//BASE64Decoder decoder = new BASE64Decoder();
			Base64.Decoder d = Base64.getDecoder();
			//byte[] b = decoder.decodeBuffer(signatureBytes);
			byte[] b = d.decode(signatureBytes);
			for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
			//原图片
			ByteArrayInputStream is = new ByteArrayInputStream(b);
			BufferedImage preImage = ImageIO.read(is);
			//最终生成的图片
			BufferedImage newImage = new BufferedImage(preImage.getWidth(), preImage.getHeight(), BufferedImage
                    .TYPE_INT_ARGB);
			//图片填充白色的背景
			newImage.getGraphics().fillRect(0, 0, preImage.getWidth(), preImage.getHeight());
			//图片填充原图片内容
			newImage.getGraphics().drawImage(preImage, 0, 0, null);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(newImage, "png", os);
			InputStream input = new ByteArrayInputStream(os.toByteArray());
			//图片名称
			String fileName = System.currentTimeMillis() + "-" + regUser.getId() + ".png";
			FileInfo fileInfo = OSSLoader.getInstance()
					.setUseRandomName(false)
					.setAllowUploadType(FileType.EXT_TYPE_IMAGE)
					.bindingUploadFile(new FileInfo(input))
					.setFileState(FileState.UN_UPLOAD)
					.setBucketName(OSSBuckets.HKJF)
					.setFilePath("/fund/agreement/")
					.setFileName(fileName)
					.doUpload();
			if (!fileInfo.getFileState().equals(FileState.SAVED)){
				return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
			}
			signature = fileInfo.getSaveKey();
			is.close();
			os.close();
			input.close();
		} catch (IOException e) {
			logger.error("上传签名失败！",e);
		}

		fundAgreement.setSignature(signature);
		int agreementId = fundAgreementService.insertFundAgreement(fundAgreement);
		if(agreementId <= 0){
			return AppResultUtil.errorOfMsg("预约失败！");
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("resStatus", Constants.SUCCESS);
		result.put("resMsg", "提交成功！");
		result.put("regUserId",regUser.getId());
		result.put("agreementId",agreementId);
		return result;
	}


	/**
	 *  @Description    ：跳转到海外基金协议 --step2
	 *  @Method_Name    ：toFundAgreementStep2
	 *  @param request
	 *  @return org.springframework.web.servlet.ModelAndView
	 *  @Creation Date  ：2018年07月05日 10:58
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("toFundAgreementStep2")
	public ModelAndView toFundAgreementStep2(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("fund/fundAgreementStep2");
		try{
			mv.addObject("sessionId",request.getParameter("sessionId"));
			mv.addObject("source",request.getParameter("source"));
			mv.addObject("agreementId",request.getParameter("agreementId"));
		}catch (Exception e){
			logger.error("跳转海外协议step2失败！",e);
		}
		return mv;
	}



	/**
	 *  @Description    ：跳转到上传图片页面 --step3
	 *  @Method_Name    ：toFundAgreementStep3
	 *  @param request
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月05日 11:04
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("toFundAgreementStep3")
	@ResponseBody
	public Map<String,Object> toFundAgreementStep3(@ModelAttribute FundAgreement updateFundAgreement,HttpServletRequest request){
		logger.info("toFundAgreementStep3:海外基金跳转到第三步-用户信息获取");
		String sessionId = request.getParameter("sessionId");
		String agreementId = request.getParameter("agreementId");
		HashMap<String, Object> result = new HashMap<>();
		RegUser regUser = JedisClusterUtils.getObjectForJson(Constants.SESSION_ID_KEY_PREFIX + sessionId, RegUser.class);
		if (regUser == null) {
			result.put("resStatus", String.valueOf(UserConstants.NO_LOGIN));
			result.put("resMsg", NO_LOGIN_MESSAGE);
			return result;
		}
		// 更新默认选项设置
		FundAgreement agreement = fundAgreementService.findFundAgreementById(Integer.valueOf(agreementId));
		if(agreement.getPayFlag().equals(FUND_AGREEMENT_NO)){
			updateFundAgreement.setState(FUND_ADVISORY_STATE_QULICATION_REJECT);
		}
		updateFundAgreement.setId(Integer.valueOf(agreementId));
		updateFundAgreement.setModifyTime(new Date());
		fundAgreementService.updateFundAgreement(updateFundAgreement);
		FundUserInfo fundUserInfo = new FundUserInfo();
		fundUserInfo.setRegUserId(regUser.getId());
		fundUserInfo.setSortColumns("id desc");
		FundUserInfo info = fundUserInfoService.findFundUserInfo(fundUserInfo);
		result.put("agreementId",agreementId);
		result.put("saveImgUrl",ossUrl);
		result.put("userInfoId",info == null ? 0:info.getId());
		result.put("idUpUrl",FundUtils.formatToStr(info,"idUpUrl"));
		result.put("idDownUrl",FundUtils.formatToStr(info,"idDownUrl"));
		result.put("passportUrl",FundUtils.formatToStr(info,"passportUrl"));
		result.put("regUserId",regUser.getId());
		result.put("resStatus", String.valueOf(Constants.SUCCESS));
		result.put("resMsg", "提交成功！");
		return result;
	}


	/**
	 *  @Description    ：保存或更新股权个人信息
	 *  @Method_Name    ：saveOrUpdateFundUserInfo
	 *  @param agreementId
	 *  @param fundUserInfo
	 *  @param userInfoId
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月05日 11:42
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("saveOrUpdateFundUserInfo")
	@ResponseBody
	public Map<String,Object> saveOrUpdateFundUserInfo(@RequestParam Integer agreementId,FundUserInfo fundUserInfo,
													   @RequestParam(value = "userInfoId",required = false)Integer userInfoId){
		FundAgreement agreement = fundAgreementService.findFundAgreementById(agreementId);
		if(null == agreement){
			return AppResultUtil.errorOfMsg(ERROR_CODE_NO_ADVISORY,ERROR_MSG_NO_ADVISORY);
		}
		fundUserInfo.setId(userInfoId);
		RegUserDetail userDetail = regUserDetailService.findRegUserDetailByRegUserId(agreement.getRegUserId());
		fundAgreementService.insertOrUpdateAdvisoryAndFundUserInfo(fundUserInfo, agreement,userDetail);
		return AppResultUtil.successOfMsg("提交成功！");
	}




	/**
	 *  @Description    ：上传图片
	 *  @Method_Name    ：uploadImg
	 *  @param picFlow
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月5日 10:44
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("/uploadImg")
	@ResponseBody
	public Map<String,Object> uploadImg(String picFlow){
		if (StringUtils.isBlank(picFlow)){
			return AppResultUtil.errorOfMsg("图片流不能为空！");
		}
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		//处理图片流
		//解码
		byte[] decode = org.springframework.security.crypto.codec.Base64.decode(picFlow.getBytes());
		InputStream input = new ByteArrayInputStream(decode);
		//图片名称
		String fileName = System.currentTimeMillis() + "-" + regUser.getId() + ".jpg";
		FileInfo fileInfo = OSSLoader.getInstance()
				.setUseRandomName(false)
				.setAllowUploadType(FileType.EXT_TYPE_IMAGE)
				.bindingUploadFile(new FileInfo(input))
				.setFileState(FileState.UN_UPLOAD)
				.setBucketName(OSSBuckets.HKJF)
				.setFilePath("/fund/agreement/")
				.setFileName(fileName)
				.doUpload();
		if (!fileInfo.getFileState().equals(FileState.SAVED)){
			return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
		}
		Map<String,Object> result = new HashMap<>(1);
		result.put("picUrl",fileInfo.getSaveKey());
		return AppResultUtil.successOf(result);
	}


	/**
	 *  @Description    ：删除图片
	 *  @Method_Name    ：delImg
	 *  @param picUrl
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年07月5日 10:48
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	@RequestMapping("/delImg")
	@ResponseBody
	public Map<String,Object> delImg(String picUrl){
		if (StringUtils.isBlank(picUrl)){
			return AppResultUtil.errorOfMsg("图片地址不能为空！");
		}
		int index = picUrl.indexOf("fund/");
		String fileKey = picUrl.substring(index,picUrl.length());
		FileInfo delFile = new FileInfo();
		delFile.setSaveKey(fileKey);
		FileInfo fileInfo = OSSLoader.getInstance()
				.bindingUploadFile(delFile)
				.setFileState(FileState.SAVED)
				.setBucketName(OSSBuckets.HKJF)
				.doDelete();
		if (!fileInfo.getFileState().equals(FileState.DELETE)){
			return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
		}
		return AppResultUtil.successOfMsg("删除成功");
	}


	@RequestMapping("/showPdf")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ModelAndView showPdf(int id){
		ModelAndView mv = new ModelAndView("fund/showPdf");
		//获取协议记录
		mv.addObject("pdfUrl",fundAgreementService.findFundAgreementById(id).getContractUrl());
		return mv;
	}

	@RequestMapping("/ossPdfToInputStream")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public void ossPdfToInputStream(@RequestParam String url,HttpServletResponse response){
		try(InputStream is = OSSLoader.getInstance().getOSSObject(OSSBuckets.HKJF,url).getObjectContent();
			OutputStream out = response.getOutputStream()
		){
			String fileName = URLEncoder.encode("view.pdf","UTF-8");
			response.setHeader("Content-Disposition","filename=" + fileName);
			response.setContentType("application/pdf;charset=UTF-8");
			//写文件
			int b;
			while ((b = is.read()) != -1){
				out.write(b);
			}
		} catch (IOException e) {
			logger.error("ossPdfToInputStream, 在线预览pdf异常, pdfUrl: {}, 异常信息: ",url,e);
		}
	}

	public static void main(String[] args) {
		double dd = 8.2;
		System.out.println(new BigDecimal(dd).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

	}
}


