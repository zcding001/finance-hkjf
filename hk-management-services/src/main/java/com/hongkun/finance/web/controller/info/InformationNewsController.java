package com.hongkun.finance.web.controller.info;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.service.InfoInformationNewsService;
import com.hongkun.finance.info.service.InfoQuestionnaireService;
import com.hongkun.finance.info.vo.InformationVo;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 资讯后台功能的controller
 * @Project : management-financial-services
 * @Program Name : com.hongkun.finance.web.controller.info.
 *          InformationNewsController
 * @Author : yanbinghuang
 */
@Controller
@RequestMapping("/informationNewsController")
public class InformationNewsController {
	private static final Logger logger = LoggerFactory.getLogger(InformationNewsController.class);

	@Reference
	private InfoInformationNewsService infoInformationNewsService;
	@Reference
	private InfoQuestionnaireService infoQuestionnaireService;

	/**
	 * @Description : 插入公告类资讯信息
	 * @Method_Name : insertInformationNews;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertInformationNews")
	@ResponseBody
	@Token
	@ActionLog(msg = "插入公告类资讯信息, 资讯类型: {args[0].type}")
	public ResponseEntity<?> insertInformationNews(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 插入资讯信息
			infoInformationNewsService.insertInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("插入公告类资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存资讯信息失败！");
		}
	}

	/**
	 * @Description : 插入广告轮播图类资讯信息
	 * @Method_Name : insertCarouselFigure;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertCarouselFigure")
	@ResponseBody
	@Token
	@ActionLog(msg = "插入广告轮播图类资讯信息, 资讯类型: {args[0].type}")
	public ResponseEntity<?> insertCarouselFigure(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 插入资讯信息
			infoInformationNewsService.insertInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("插入广告轮播图类资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存资讯信息失败！");
		}
	}

	/**
	 * @Description : 插入新闻类资讯信息
	 * @Method_Name : insertNews;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertNews")
	@ResponseBody
	@Token
	@ActionLog(msg = "插入新闻类资讯信息, 资讯类型: {args[0].type}")
	public ResponseEntity<?> insertNews(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 插入资讯信息
			infoInformationNewsService.insertInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("插入新闻类资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "保存资讯信息失败！");
		}
	}

	/**
	 * @Description : 根据ID，查看资讯信息
	 * @Method_Name : searchInformationNewsById;
	 * @param id
	 *            资讯ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInformationNewsById")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ResponseEntity<?> searchInformationNewsById(@RequestParam(value = "id", required = false) Integer id) {
		// 通过ID,查询对应资讯信息
		InfoInformationNews infoInformationNews = infoInformationNewsService.findInfoInformationNewsById(id);
		if (infoInformationNews == null) {
			return new ResponseEntity<>(Constants.ERROR, "没有查询到对应资讯信息！");
		}
		return new ResponseEntity<>(Constants.SUCCESS, infoInformationNews);
	}

	/**
	 * @Description : 通过资讯ID，修改广告轮播类资讯的状态
	 * @Method_Name : updateCarouselFigureById;
	 * @param id
	 *            资讯ID
	 * @param state
	 *            状态
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateCarouselFigureById")
	@ResponseBody
	@ActionLog(msg = "通过资讯ID修改广告轮播类资讯的状态, 资讯id: {args[0]} , 资讯状态state:  {args[1]}")
	public ResponseEntity<?> updateCarouselFigureById(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "state", required = false) Integer state) {
		try {
			// 通过ID,更新对应资讯信息状态
			infoInformationNewsService.updateInfomationStateById(id, state);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("通过资讯ID修改广告轮播类资讯的状态, 资讯ID: {}, 更新失败: ", id, e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 通过资讯ID，修改新闻类资讯的状态
	 * @Method_Name : updateNewsById;
	 * @param id
	 *            资讯ID
	 * @param state
	 *            状态
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateNewsById")
	@ResponseBody
	@ActionLog(msg = "通过资讯ID修改新闻类资讯的状态, 资讯id: {args[0]} , 资讯状态state:  {args[1]}")
	public ResponseEntity<?> updateNewsById(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "state", required = false) Integer state) {
		try {
			// 通过ID,更新对应资讯信息状态
			infoInformationNewsService.updateInfomationStateById(id, state);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("通过资讯ID修改新闻类资讯的状态, 资讯ID: {}, 更新失败: ", id, e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 通过资讯ID，修改公告类资讯的状态
	 * @Method_Name : updateInformationNewsById;
	 * @param id
	 *            资讯ID
	 * @param state
	 *            状态
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:57:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateInformationNewsById")
	@ResponseBody
	@ActionLog(msg = "通过资讯ID修改公告类资讯的状态, 资讯id: {args[0]} , 资讯状态state:  {args[1]}")
	public ResponseEntity<?> updateInformationNewsById(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "state", required = false) Integer state) {
		try {
			// 通过ID,更新对应资讯信息状态
			infoInformationNewsService.updateInfomationStateById(id, state);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("通过资讯ID,修改公告类资讯的状态, 资讯ID: {}, 更新失败: ", id, e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 更新公告类资讯信息
	 * @Method_Name : updateInformationNews;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:56:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateInformationNews")
	@ResponseBody
	@ActionLog(msg = "更新公告类资讯信息, 资讯id: {args[0].infomationId}")
	public ResponseEntity<?> updateInformationNews(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 通过ID,更新对应资讯信息
			infoInformationNewsService.updateInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("更新公告类资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 更新广告轮播类资讯信息
	 * @Method_Name : updateCarouselFigure;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:56:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateCarouselFigure")
	@ResponseBody
	@ActionLog(msg = "更新广告轮播类资讯信息, 资讯id: {args[0].infomationId}")
	public ResponseEntity<?> updateCarouselFigure(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 通过ID,更新对应资讯信息
			infoInformationNewsService.updateInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("更新资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 更新新闻类资讯信息
	 * @Method_Name : updateNews;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 下午3:56:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateNews")
	@ResponseBody
	@ActionLog(msg = "更新新闻类资讯信息, 资讯id: {args[0].infomationId}")
	public ResponseEntity<?> updateNews(InformationVo informationCondition) {
		try {
			// 校验资讯参数合法性
			ResponseEntity<?> responseEntity = checkInformations(informationCondition);
			if (responseEntity.getResStatus() == Constants.ERROR) {
				return responseEntity;
			}
			// 通过ID,更新对应资讯信息
			infoInformationNewsService.updateInfomationsNews(informationCondition);
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("更新新闻类资讯信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	/**
	 * @Description : 查询资讯新闻管理分页信息
	 * @Method_Name : searchInfoNewsByCondition;
	 * @param infoInformationNew
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月21日 上午9:58:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInfoNewsByCondition")
	@ResponseBody
	public ResponseEntity<?> searchInfoNewsByCondition(InfoInformationNews infoInformationNew, Pager pager) {
		List<Integer> type = new ArrayList<Integer>();
		// 如果资讯类型为空，则默认查询以下几种资讯类型
		if (infoInformationNew.getType() == null || (infoInformationNew.getType() == -999)) {
			type.add(InfomationNewsEnum.MEDIA_REPORT.getInfomationType());
			type.add(InfomationNewsEnum.COMPANY_NEWS.getInfomationType());
			type.add(InfomationNewsEnum.WELFARE.getInfomationType());
			type.add(InfomationNewsEnum.RESEARCH_QUESTION.getInfomationType());
			type.add(InfomationNewsEnum.ACTIVITY.getInfomationType());
			type.add(InfomationNewsEnum.FUNCTION_GUIDE.getInfomationType());
			type.add(InfomationNewsEnum.AUDITING_INFO.getInfomationType());
		} else {
			type.add(infoInformationNew.getType());
		}
		return new ResponseEntity<>(Constants.SUCCESS,
				infoInformationNewsService.findByCondition(type, infoInformationNew, pager));
	}

	/**
	 * @Description : 广告轮播分页查询
	 * @Method_Name : searchCarouselFigureByCondition;
	 * @param infoInformationNew
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年2月7日 上午10:10:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchCarouselFigureByCondition")
	@ResponseBody
	public ResponseEntity<?> searchCarouselFigureByCondition(InfoInformationNews infoInformationNew, Pager pager) {
		List<Integer> type = new ArrayList<Integer>();
		// 如果资讯类型为空，则默认查询以下几种资讯类型
		if (infoInformationNew.getType() == null || (infoInformationNew.getType() == -999)) {
			type.add(InfomationNewsEnum.CAROUSEL_FIGURE.getInfomationType());
			type.add(InfomationNewsEnum.ADVERTISEMENT.getInfomationType());
		} else {
			type.add(infoInformationNew.getType());
		}
		return new ResponseEntity<>(Constants.SUCCESS,
				infoInformationNewsService.findByCondition(type, infoInformationNew, pager));
	}

	/**
	 * @Description : 查询资讯分页信息
	 * @Method_Name : searchInfomationByCondition;
	 * @param infoInformationNew
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年9月19日 下午5:52:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInfomationByCondition")
	@ResponseBody
	public ResponseEntity<?> searchInfomationByCondition(InfoInformationNews infoInformationNew, Pager pager) {
		infoInformationNew.setSortColumns("create_time desc");
		return new ResponseEntity<>(Constants.SUCCESS,
				infoInformationNewsService.findInfoInformationNewsList(infoInformationNew, pager));
	}

	/**
	 * @Description : 校验插入,更新资讯信息的合法性
	 * @Method_Name : checkInformations;
	 * @param informationCondition
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月21日 上午9:58:41;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> checkInformations(@RequestBody InformationVo informationCondition) {
		if (informationCondition.getType() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "资讯类型不能为空！");
		}
		if (StringUtils.isBlank(informationCondition.getTitle())) {
			return new ResponseEntity<>(Constants.ERROR, "标题不能为空！");
		}
		if (StringUtils.isBlank(informationCondition.getChannel())) {
			return new ResponseEntity<>(Constants.ERROR, "渠道不能为空！");
		}
		// 友情链接不需要判断正文内容信息
		if (InfomationNewsEnum.FRIENDLY_URL.getInfomationType() != informationCondition.getType()) {
			if (StringUtils.isBlank(informationCondition.getContent())) {
				return new ResponseEntity<>(Constants.ERROR, "正文内容不能为空！");
			}
		}
		// 媒体报道和公司动态需要判断以下两个参数不能为空
		if (InfomationNewsEnum.MEDIA_REPORT.getInfomationType() == informationCondition.getType()
				|| InfomationNewsEnum.COMPANY_NEWS.getInfomationType() == informationCondition.getType()) {
			if (StringUtils.isBlank(informationCondition.getKeyword())) {
				return new ResponseEntity<>(Constants.ERROR, "关键字不能为空！");
			}
			if (informationCondition.getResideSelect() == null) {
				return new ResponseEntity<>(Constants.ERROR, "所属版块不能为空！");
			}
		}
		// 如果是公司公告，需要判断位置不能为空
		if (InfomationNewsEnum.COMPANY_NOTICE.getInfomationType() == informationCondition.getType()) {
			if (informationCondition.getPosition() == null) {
				return new ResponseEntity<>(Constants.ERROR, "位置不能为空！");
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	/**
	 * @Description : 添加调查问卷
	 * @Method_Name : insertInfoQuestionnaire;
	 * @param questionnaireJson
	 *            调查问卷问题JSON
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午2:42:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/insertInfoQuestionnaire")
	@ResponseBody
	@Token
	@ActionLog(msg = "添加调查问卷, 问卷内容: {args[0]}")
	public ResponseEntity<?> insertInfoQuestionnaire(
			@RequestParam(value = "questionnaireJson", required = false) String questionnaireJson) {
		try {
			return infoQuestionnaireService.insertInfoQuestionnaire(questionnaireJson);
		} catch (Exception e) {
			logger.error("添加调查问卷失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "添加失败!");
		}
	}

	/**
	 * @Description : 根据资讯ID查询是否此资讯有调查问卷
	 * @Method_Name : findQuestionnaireByInfoId;
	 * @param infoInformationNewsId
	 *            资讯ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午2:50:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findQuestionnaireByInfoId")
	@ResponseBody
	public ResponseEntity<?> findQuestionnaireByInfoId(
			@RequestParam(value = "infoInformationNewsId", required = false) Integer infoInformationNewsId) {
		InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
		infoQuestionnaire.setInfoInformationNewsId(infoInformationNewsId);
		return new ResponseEntity<>(Constants.SUCCESS,
				infoQuestionnaireService.findInfoQuestionnaireList(infoQuestionnaire));
	}

	/**
	 * @Description : 查询资讯对应的调查问卷
	 * @Method_Name : findInfoQuesetionById;
	 * @param infoInformationNewsId
	 *            资讯ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午3:34:17;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findInfoQuesetionById")
	@ResponseBody
	public ResponseEntity<?> findInfoQuesetionById(
			@RequestParam(value = "informationNewsId", required = false) Integer informationNewsId) {
		InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
		infoQuestionnaire.setInfoInformationNewsId(informationNewsId);
		return infoQuestionnaireService.findInfoQuesetionById(informationNewsId);
	}

	/**
	 * @Description : 查询调查问卷及选项内容
	 * @Method_Name : findQuestionAndItem;
	 * @param questionnaireId
	 *            调查问卷问题ID
	 * @param infomationId
	 *            资讯ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午6:55:15;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findQuestionAndItem")
	@ResponseBody
	public ResponseEntity<?> findQuestionAndItem(
			@RequestParam(value = "questionnaireId", required = false) Integer questionnaireId,
			@RequestParam(value = "infomationId", required = false) Integer infomationId) {
		return infoQuestionnaireService.findQuestionAndItem(infomationId, questionnaireId);
	}

	/**
	 * @Description : 更新调查问卷及选项内容
	 * @Method_Name : updateInfoQuestionnaire;
	 * @param questionnaireJson
	 * @param questionId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午7:41:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateInfoQuestionnaire")
	@ResponseBody
	@ActionLog(msg = "更新调查问卷及选项内容, 问卷内容: {args[0]} , 问卷id: {args[1]}")
	public ResponseEntity<?> updateInfoQuestionnaire(
			@RequestParam(value = "questionnaireJson", required = false) String questionnaireJson,
			@RequestParam(value = "questionId", required = false) Integer questionId) {
		try {
			return infoQuestionnaireService.updateInfoQuestionnaire(questionnaireJson, questionId);
		} catch (Exception e) {
			logger.error("更新调查问卷及选项内容, 更新失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}
}
