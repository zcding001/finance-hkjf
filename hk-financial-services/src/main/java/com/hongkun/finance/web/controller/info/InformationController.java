package com.hongkun.finance.web.controller.info;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;
import com.hongkun.finance.info.service.InfoInformationNewsService;
import com.hongkun.finance.info.service.InfoQuestionnaireAnswerService;
import com.hongkun.finance.info.service.InfoQuestionnaireService;
import com.hongkun.finance.info.service.InformationNewsService;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description :公司资讯，公告，动态相关
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.info.InformationController.
 *          java
 * @Author : yanbinghuang@hongkun.com
 */
@Controller
@RequestMapping("/informationController")
public class InformationController {
	@Reference
	private InfoInformationNewsService infoInformationNewsService;
	@Reference
	private InformationNewsService informationNewsService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInvestFacade bidInvestFacade;
	@Reference
	private InfoQuestionnaireAnswerService infoQuestionnaireAnswerService;
	@Reference
	private InfoQuestionnaireService infoQuestionnaireService;

	/***
	 * @Description : 首页查询广告轮播、公告、公司动态、媒体报道信息
	 * @Method_Name : findInfomations;
	 * @param position
	 *            位置
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月5日 下午3:42:17;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("findInfomations")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ResponseEntity<?> findInfomations(@RequestParam(value = "position", required = false) Integer position) {
	    RegUser regUser = BaseUtil.getLoginUser();
	    Integer showFlag = null;//用户判断首页的BANNER图，是否对投资用户展示
	    if(regUser == null){
	        showFlag = InfomationConstants.INFO_SHOW_ZERO;
        }else{
            if(bidInvestFacade.validInvestQualification(regUser.getId()).getResStatus() == Constants.ERROR){
                showFlag = InfomationConstants.INFO_SHOW_ZERO;
            }
        }
	    return informationNewsService.findInformationNewsLists(InfomationConstants.CHANNEL_PC, position,showFlag);
	}

	/**
	 * @Description : 根据资讯类型查询分页信息
	 * @Method_Name : searchInfomationsPage;
	 * @param type
	 *            资讯类型
	 * @param position
	 *            位置
	 * @param pager
	 *            分页对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月5日 下午3:45:44;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("searchInfomationsPage")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> searchInfomationsPage(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "position", required = false) Integer position, Pager pager) {
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setChannel(InfomationConstants.CHANNEL_PC);
		infoInformationNews.setPosition(position);
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setType(type);
		return new ResponseEntity<>(Constants.SUCCESS,
				infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager));
	}

	/**
	 * @Description : 通过资讯ID，查询资讯信息
	 * @Method_Name : searchInfomationsById;
	 * @param id
	 *            资讯ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月22日 下午2:23:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("searchInfomationsById")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> searchInfomationsById(@RequestParam(value = "id", required = false) Integer id) {
		// 通过ID,查询对应资讯信息
		return new ResponseEntity<>(Constants.SUCCESS, infoInformationNewsService.findInfoInformationNewsById(id));
	}

	/***
	 * @Description :查询分页信息
	 * @Method_Name : searchInfomation;
	 * @param type
	 *            资讯类型
	 * @param channel
	 *            渠道
	 * @param position
	 *            位置
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月29日 下午2:25:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("searchInfomation")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?> searchInfomation(@RequestParam(value = "type", required = false) int type,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "position", required = false) Integer position) {
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setType(type);
		infoInformationNews.setChannel(channel);
		infoInformationNews.setPosition(position);
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setSortColumns("sort asc,create_time desc");
		return new ResponseEntity<>(Constants.SUCCESS,
				infoInformationNewsService.findInfoInformationNewsList(infoInformationNews));
	}

	/**
	 *  @Description    : 查询问卷调查详情
	 *  @Method_Name    : findQuestionnaireInfo;
	 *  @param infomationId
	 *  @param regUserId
	 *  @return
	 *  @return         : ModelAndView;
	 *  @Creation Date  : 2018年10月30日 下午1:49:54;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("findQuestionnaireInfo")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public ResponseEntity<?>  findQuestionnaireInfo(@RequestParam("infomationId") Integer infomationId,@RequestParam(value = "regUserId", required = false) Integer regUserId) {
		return informationNewsService.findQuestionnaireInfo(infomationId, regUserId);
	}

	/**
	 *  @Description    : 查询是否存此调查问题
	 *  @Method_Name    : findExistQuestion;
	 *  @param request
	 *  @param infomationId
	 *  @param regUserId
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年10月31日 上午10:57:48;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("findExistQuestion")
	@ResponseBody
	public ResponseEntity<?>  findExistQuestion(HttpServletRequest request, @RequestParam("infomationId") Integer infomationId, @RequestParam("regUserId") Integer regUserId) {
		return informationNewsService.findExistQuestion(infomationId, regUserId);
	}

	/**
	 * @Description : 问卷试题答案提交
	 * @Method_Name : saveInfoQuestionnaireAnswer;
	 * @param questionnaireAnswer
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月26日 下午2:36:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("saveInfoQuestionnaireAnswer")
	@ResponseBody
	public Map<String, Object> saveInfoQuestionnaireAnswer(
			@RequestParam(value = "questionnaireAnswer", required = false) String questionnaireAnswer) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (StringUtils.isBlank(questionnaireAnswer)) {
			return AppResultUtil.errorOfMsg("请填写答案后再提交！");
		}
		infoQuestionnaireAnswerService.saveInfoQuestionnaireAnswer(questionnaireAnswer, regUser.getId());
		return AppResultUtil.successOfMsg("保存成功！");
	}

	/** 
	* @Description: 查询用户是否做过投资测评
	 * @param request
	 * @param source 来源 1-PC 2-IOS 3-ANDRIOD 4-WAP
	* @return: com.yirun.framework.core.model.ResponseEntity<?> 
	* @Author: hanghe@hongkunjinfu.com
	* @Date: 2018/11/22 16:27
	*/
	@RequestMapping("isAnswerQuestionnaire")
	@ResponseBody
	public ResponseEntity<?> isAnswerQuestionnaire(HttpServletRequest request, String source){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		ResponseEntity<?> result = new ResponseEntity<>(UserConstants.USER_QUESTIONNAIRE_YES);

		// 查询用户是否存在投资记录
		boolean investFlag = bidInvestFacade.getUserSelfHasInvest(regUser.getId());

		// 查询投资调查问卷
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setType(InfomationNewsEnum.RESEARCH_QUESTION.getInfomationType());
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setPosition(InfomationConstants.INFO_POSITION_INVEST);
		List<InfoInformationNews> infoInformationNewsList =  infoInformationNewsService.findInfoInformationNewsList(infoInformationNews);
		if(infoInformationNewsList!= null && !infoInformationNewsList.isEmpty()){
			// 来源筛选
			infoInformationNewsList = infoInformationNewsList.stream().filter(e ->e
					.getChannel().contains(source))
					.collect(Collectors.toList());
			// 校验用户是否做过测评

			// 根据调查问卷id查询调查问卷问题
			InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
			infoQuestionnaire.setState(1);
			infoQuestionnaire.setInfoInformationNewsId(infoInformationNewsList.get(0).getId());
			List<InfoQuestionnaire> infoQuestionnaireList = infoQuestionnaireService.findInfoQuestionnaireList(infoQuestionnaire);

			// 根据调查问卷问题查询调查问卷答案数量
			InfoQuestionnaireAnswer infoQuestionnaireAnswer = new InfoQuestionnaireAnswer();
			infoQuestionnaireAnswer.setRegUserId(regUser.getId());
			infoQuestionnaireAnswer.setInfoQuestionnaireId(infoQuestionnaireList.get(0).getId());
			int questionnaireAnswerCount = infoQuestionnaireAnswerService.findInfoQuestionnaireAnswerCount(infoQuestionnaireAnswer);
			if(!investFlag && questionnaireAnswerCount <= 0){	//没有做过调查问卷
				result.setResStatus(UserConstants.USER_QUESTIONNAIRE_NO);
				result.getParams().put("informationId",infoInformationNewsList.get(0).getId());
				result.getParams().put("regUserId",regUser.getId());
			}
		}
		return result;
	}

}
