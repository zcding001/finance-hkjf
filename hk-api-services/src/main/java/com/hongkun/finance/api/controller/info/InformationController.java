package com.hongkun.finance.api.controller.info;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoFeedback;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;
import com.hongkun.finance.info.service.*;
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
import com.yirun.framework.core.utils.AppResultUtil.ExtendMap;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HtmlUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 资讯相关Controller
 * 
 * @author yanbinghuang
 *
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
	private InfoFeedbackService infoFeedbackService;
	@Reference
	private InfoQuestionnaireService infoQuestionnaireService;
	@Reference
	private InfoQuestionnaireAnswerService infoQuestionnaireAnswerService;
	@Value(value = "${oss.url.hkjf}")
    private String ossUrl;
	@Reference
	private BidInvestFacade bidInvestFacade;

	/**
	 * @Description : 根据资讯类型，位置，渠道查询资讯信息（目前用于单独查询弹出式广告类信息）
	 * @Method_Name : searchInformationByType;
	 * @param type
	 *            资讯类型  9—广告
	 * @param position
	 *            位置  0—其他(开屏) 1—首页
	 * @param channel
	 *            渠道
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 上午9:32:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInformationByType")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> searchInformationByType(HttpServletRequest request,@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "position", required = false) Integer position,
			@RequestParam(value = "channel", required = false) String channel) {
		Pager pager = new Pager();
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setType(type);
		infoInformationNews.setChannel(channel);
		infoInformationNews.setPosition(position);
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setSortColumns("sort asc,create_time desc");
		// 如果资讯类型是公告，并且位置是首页的话，只显示前三条
		if ((position == null ? -1 : position) == InfomationConstants.INFO_POSITION_FIRST_PAGE
				&& type == InfomationNewsEnum.COMPANY_NOTICE.getInfomationType()) {
			pager.setCurrentPage(1);
			pager.setPageSize(3);
		}
		// 广告类型：如果是开屏广告（位置为【其他】），或者弹屏广告（位置为【首页】），加载一条
		if (type == InfomationNewsEnum.ADVERTISEMENT.getInfomationType()) {
			pager.setCurrentPage(1);
			pager.setPageSize(1);
		}
		Pager resultPager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
		// 如果是首页的公告，只返回标题
		if ((position == null ? -1 : position) == InfomationConstants.INFO_POSITION_FIRST_PAGE
				&& (type == null ? -1 : type) == InfomationNewsEnum.COMPANY_NOTICE.getInfomationType()) {
			return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "title");
		}
		// 如果是轮播图，转换图片
		if ((type == null ? -1 : type) == InfomationNewsEnum.CAROUSEL_FIGURE.getInfomationType()) {
			List<InfoInformationNews> pagerInfo = (List<InfoInformationNews>) resultPager.getData();
			List<InfoInformationNews> informationNews = new ArrayList<InfoInformationNews>();
			for (InfoInformationNews pagers : pagerInfo) {
			    if(StringUtils.isNotBlank(pagers.getImgUrl())){
			        pagers.setIcon(ossUrl + pagers.getImgUrl());
                }
				pagers.setContent(this.getImgFromStr(pagers.getContent()));
				informationNews.add(pagers);
			}
			return AppResultUtil.successOfListInProperties(informationNews, "查询成功", "id"/** 资讯ID **/
					, "title"/** 标题 **/
					, "url" /** 链接 **/
					, "content"/** 资讯 内容 **/
			);
		}

		//如果是开屏广告（位置为其他），或者弹屏广告（位置传首页），转换图片
		if ((type == null ? -1 : type) == InfomationNewsEnum.ADVERTISEMENT.getInfomationType()) {
			List<InfoInformationNews> adStartList = (List<InfoInformationNews>) resultPager.getData();
			InfoInformationNews adStart = new InfoInformationNews();
			if(adStartList != null && adStartList.size() > 0){
				adStart = adStartList.get(0);
				adStart.setContent(this.getImgFromStr(adStart.getContent()));
			}
			return AppResultUtil.successOfInProperties(adStart, "查询成功", "id"/** 资讯ID **/
					, "title"/** 标题 **/
					, "url" /** 链接 **/
					, "content"/** 资讯 内容 **/
			);
		}
		return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id"/** 资讯ID **/
				, "title"/** 标题 **/
				, "url" /** 链接 **/
				, "content"/** 资讯 内容 **/
		);
	}

	/***
	 * @Description : 按照资讯类型、位置、渠道分页查询资讯信息
	 * @Method_Name : listInformationRecord;
	 * @param type
	 *            资讯类型
	 * @param position
	 *            位置
	 * @param channel
	 *            渠道
	 * @param resideSelect
	 *            所属版块
	 * @param pager
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 上午11:12:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/listInformationRecord")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> listInformationRecord(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "position", required = false) Integer position,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "resideSelect", required = false) Integer resideSelect, Pager pager) {
		pager.setInfiniteMode(true);
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setType(type);
		infoInformationNews.setChannel(channel);
		infoInformationNews.setPosition(position);
		infoInformationNews.setResideSelect(resideSelect);
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setSortColumns("sort asc,create_time desc");
		Pager resultPager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
		// 解析组装资讯图标信息
		if (resultPager != null && resultPager.getData() != null && resultPager.getData().size() > 0) {
			List<InfoInformationNews> infoInformationList = new ArrayList<InfoInformationNews>();
			for (InfoInformationNews infoInformation : (List<InfoInformationNews>) resultPager.getData()) {
				infoInformation.setIcon(ossUrl + infoInformation.getImgUrl());
				if (InfomationNewsEnum.COMPANY_NOTICE.getInfomationType() == infoInformation.getType()
						|| InfomationNewsEnum.CAROUSEL_FIGURE.getInfomationType() == infoInformation.getType()
						|| InfomationNewsEnum.AUDITING_INFO.getInfomationType() == infoInformation.getType()) {
					String content = "<span style=\'line-height:0.5;\'></br></span><p style=\'text-align:center;\'>\r\n\t<span style=\'font-size:22px;line-height:1.5;color:#222222;\'>"
							+ infoInformation.getTitle()
							+ "</span>\r\n</p>\r\n<p style=\'text-align:center;\'>\r\n\t<span style=\'font-size:17px;line-height:1;color:#222222;\'><span style=\font-size:17px;'color:#222222;\'>"
							+ DateUtils.format(infoInformation.getCreateTime(), DateUtils.DATE_HH_MM_SS)
							+ "</span><span style=\'color:#666666;\'></span></span>\r\n</p>"
							+ "<span style=\'font-size:17px;color:#666666;\'>" + infoInformation.getContent()
							+ "</span>";
					infoInformation.setContent(content);
				}
				// 如果是功能索引，则将特殊字符过滤掉
				if (InfomationNewsEnum.FUNCTION_GUIDE.getInfomationType() == infoInformation.getType()) {
				    String content = infoInformation.getContent().replace("<p>\n", "");
				    content = content.replace("<p>\r\n\t", "");
				    content = content.replace("<p>\r\n", "");
				    content = content.replace("\n</p>", "");
				    infoInformation.setContent(HtmlUtils.Html2Text(content));
				}
				infoInformationList.add(infoInformation);
			}
			resultPager.setData(infoInformationList);
		}
		// 如果类型是福利，活动的话，返回点赞数和浏览数的字段
		if ((type == null ? -1 : type) == InfomationNewsEnum.ACTIVITY.getInfomationType()
				|| (type == null ? -1 : type) == InfomationNewsEnum.WELFARE.getInfomationType()) {
			return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id"/** 资讯ID **/
					, "title"/** 资讯标题 **/
					, "source"/** 资讯来源 **/
					, "content"/** 资讯内容 **/
					, "eulogizeNum"/** 资讯点赞数 **/
					, "browseNum"/** 资讯浏览数 **/
					, "createTime"/** 资讯创建时间 **/
					, "icon"/** 资讯图标 **/
			);
		}
		return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id", /** 资讯ID **/
				"title", /** 资讯标题 **/
				"source"/** 资讯来源 **/
				, "content"/** 资讯内容 **/
				, "createTime"/** 资讯创建时间 **/
				, "icon"/** 资讯图标 **/
		);
	}

	/**
	 * @Description : 通过资讯ID查询资讯详情
	 * @Method_Name : searchInfomationsById;
	 * @param id
	 *            资讯ID
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 上午11:24:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("searchInfomationsById")
	@ResponseBody
	public Map<String, Object> searchInfomationsById(@RequestParam(value = "id", required = false) Integer id) {
		InfoInformationNews infoInformationNews = infoInformationNewsService.findInfoInformationNewsById(id);
		String content = "<span style=\'line-height:0.5;\'></br></span><p style=\'text-align:center;\'>\r\n\t<span style=\'font-size:30px;line-height:1.5;color:#111111;\'><strong>"
				+ infoInformationNews.getTitle()
				+ "</strong></span>\r\n</p>\r\n<p style=\'text-align:center;\'>\r\n\t<span style=\'font-size:10px;line-height:1;color:#000000;\'><span style=\'font-size:28px;\'color:#999999;\'>"
				+ DateUtils.format(infoInformationNews.getCreateTime(), DateUtils.DATE_HH_MM_SS)
				+ "</span><span style=\'color:#000000;\'></span></span>\r\n</p>" + infoInformationNews.getContent();
		infoInformationNews.setContent(content);
		// 通过ID,查询对应资讯信息
		return AppResultUtil.successOfInProperties(infoInformationNews, "查询成功", "id", /** 资讯ID **/
				"title"/** 资讯标题 **/
				, "content"/** 资讯内容 **/
				, "createTime"/** 资讯创建时间 **/
		);
	}

	/**
	 * @Description : 添加点赞或浏览的次数
	 * @Method_Name : plusInfomationsBrowsing;
	 * @param id
	 *            资讯ID
	 * @param type
	 *            资讯类型
	 * @param source
	 *            来源
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 上午11:31:41;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("plusInfomationsBrowsing")
	@ResponseBody
	public Map<String, Object> plusInfomationsBrowsing(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "source", required = false) Integer source) {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		return AppResultUtil.mapOfResponseEntity(
				informationNewsService.eulogizeInformations(id, type, source, regUser.getId()), "处理成功");
	}

	/**
	 * @Description : 保存意见反馈内容
	 * @Method_Name : saveInfoFeedback;
	 * @param content
	 *            意见内容
	 * @param source
	 *            来源 1-PC 2-IOS 3-ANDRIOD 4-WAP
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月13日 下午6:39:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("saveInfoFeedback")
	@ResponseBody
	public Map<String, Object> saveInfoFeedback(@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "source", required = false) Integer source) {
	    RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		// 判断提交的意见内容不能空
		if (StringUtils.isBlank(content)) {
			return AppResultUtil.errorOfMsg("意见内容不能为空!");
		}
		// 组装保存的意见信息
		InfoFeedback infoFeedback = new InfoFeedback();
		infoFeedback.setContent(content);
		infoFeedback.setRegUserId(regUser.getId());
		infoFeedback.setSource(source);
		infoFeedbackService.insertInfoFeedback(infoFeedback);
		return AppResultUtil.successOfMsg("保存成功");
	}

	/**
	 * @Description : 查询资讯对应的调查问卷
	 * @Method_Name : findInfoQuesetionById;
	 * @param infoInformationNewsId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午3:34:17;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findInfoQuesetionById")
	@ResponseBody
	public Map<String, Object> findInfoQuesetionById(
			@RequestParam(value = "informationNewsId", required = false) Integer informationNewsId) {
		InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
		infoQuestionnaire.setInfoInformationNewsId(informationNewsId);
		return AppResultUtil.successOf(infoQuestionnaireService.findInfoQuesetionById(informationNewsId).getResMsg());
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
	@RequestMapping("/saveInfoQuestionnaireAnswer")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	@ResponseBody
	public Map<String, Object> saveInfoQuestionnaireAnswer(
			@RequestParam(value = "questionnaireAnswer", required = false) String questionnaireAnswer,@RequestParam(value = "regUserId", required = false) Integer regUserId) {
		if (StringUtils.isBlank(questionnaireAnswer)) {
			return AppResultUtil.errorOfMsg("请填写答案后再提交！");
		}
		infoQuestionnaireAnswerService.saveInfoQuestionnaireAnswer(questionnaireAnswer, regUserId);
		return AppResultUtil.successOfMsg("保存成功！");
	}

	/**
	 * @Description :获取 HTML里第一个SRC信息
	 * @Method_Name : getImgFromStr;
	 * @param html
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年3月9日 下午6:41:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private String getImgFromStr(String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("img");
		if (elements != null && elements.size() > 0) {
			return StringUtils.isBlank(elements.get(0).attr("src")) ? "" : elements.get(0).attr("src");
		}
		return "";
	}

	/**
	 * @Description :转换img标签src路径
	 * @Method_Name : repairContent;
	 * @param content
	 * @param replaceHttp
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年5月17日 下午6:01:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String repairContent(String content, String replaceHttp) {
		String patternStr = "<img\\s*([^>]*)\\s*src=\"(.*?)\"\\s*([^>]*)>";
		Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		String result = content;
		while (matcher.find()) {
			String src = matcher.group(2);
			String replaceSrc = "";
			if (src.lastIndexOf(".") > 0) {
				replaceSrc = src.substring(0, src.lastIndexOf(".")) + src.substring(src.lastIndexOf("."));
			}
			if (!src.startsWith("http://") && !src.startsWith("https://")) {
				replaceSrc = replaceHttp + replaceSrc;
			}
			result = result.replaceAll(src, replaceSrc);
		}
		return result;
	}
	/**
	 *  @Description    : 位置是首页的话，只显示类型是公告、平台资讯、活动按时间倒序前三条
	 *  @Method_Name    : searchPageInformation;
	 *  @param position 位置
	 *  @param channel 渠道
	 *  @return
	 *  @return         : Map<String,Object>;
	 *  @Creation Date  : 2018年9月4日 下午5:56:53;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchPageInformation")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> searchPageInformation( @RequestParam(value = "position", required = false) Integer position,
	            @RequestParam(value = "channel", required = false) String channel,Pager pager) {
	    RegUser regUser = BaseUtil.getLoginUser();
	    ExtendMap infoMap = null;
        InfoInformationNews infoInformationNews = new InfoInformationNews();
        infoInformationNews.setChannel(channel);
        infoInformationNews.setPosition(position);
        infoInformationNews.setState(InfomationConstants.INFO_VALID);
        infoInformationNews.setSortColumns("create_time desc,sort asc");
        if(regUser == null){
            infoInformationNews.setShowFlag(InfomationConstants.INFO_SHOW_ZERO);
        }else{
            if(bidInvestFacade.validInvestQualification(regUser.getId()).getResStatus() == Constants.ERROR){
                infoInformationNews.setShowFlag(InfomationConstants.INFO_SHOW_ZERO);
            }
        }
        Pager resultInfoPager = infoInformationNewsService.findByCondition(Arrays.asList(InfomationNewsEnum.CAROUSEL_FIGURE.getInfomationType()), infoInformationNews, pager);
        //查询指定位置的轮播图
        List<InfoInformationNews> pagerInfo = (List<InfoInformationNews>) resultInfoPager.getData();
        List<InfoInformationNews> informationNews = new ArrayList<InfoInformationNews>();
        for (InfoInformationNews pagers : pagerInfo) {
            if(StringUtils.isNotBlank(pagers.getImgUrl())){
                pagers.setIcon(ossUrl + pagers.getImgUrl());
            }
            pagers.setContent(this.getImgFromStr(pagers.getContent()));
            informationNews.add(pagers);
        }
        ExtendMap carouselFigureMap = AppResultUtil.successOfListInProperties(informationNews, "查询成功", "id"/** 资讯ID **/
                , "title"/** 标题 **/
                , "url" /** 链接 **/
                , "content"/** 资讯 内容 **/
        );

		Pager appMenuBackGroundPager = infoInformationNewsService.findByCondition(Arrays.asList(InfomationNewsEnum.APP_MENU_BACKGROUND.getInfomationType()), infoInformationNews, pager);
		//查询指定位置的APP菜单背景图
		List<InfoInformationNews> appMenuBackGroundPagerData = (List<InfoInformationNews>) appMenuBackGroundPager.getData();
		InfoInformationNews appMenu = new InfoInformationNews();
		if(appMenuBackGroundPagerData != null && !appMenuBackGroundPagerData.isEmpty()){
			appMenu = appMenuBackGroundPagerData.get(0);
			if(StringUtils.isNotBlank(appMenu.getImgUrl())){
				appMenu.setIcon(ossUrl + appMenu.getImgUrl());
			}
			appMenu.setContent(this.getImgFromStr(appMenu.getContent()));
		}
		ExtendMap appMenuMap = AppResultUtil.successOfInProperties(appMenu, "查询成功", "id"/** 资讯ID **/
				, "title"/** 标题 **/
				, "url" /** 链接 **/
				, "content"/** 资讯 内容 **/
		);


        // 位置是首页的话，只显示类型是公告、平台资讯、活动按时间倒序前三条
        if ((position == null ? -1 : position) == InfomationConstants.INFO_POSITION_FIRST_PAGE) {
            Pager page = new Pager();
            page.setCurrentPage(1);
            page.setPageSize(3);
            Pager resultPager = infoInformationNewsService.findByCondition(Arrays.asList(InfomationNewsEnum.ACTIVITY.getInfomationType(),InfomationNewsEnum.COMPANY_NEWS.getInfomationType(),InfomationNewsEnum.COMPANY_NOTICE.getInfomationType()), infoInformationNews, page);
            // 如果是首页的公告，只返回标题
            infoMap = AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功"
                    , "title"/** 标题 **/
                    , "type" /** 类型 **/
            );
        }

		//位置首页，弹屏广告查询一条
//		ExtendMap adHomeMap = null;
//		if ((position == null ? -1 : position) == InfomationConstants.INFO_POSITION_FIRST_PAGE) {
//			Pager page = new Pager();
//			page.setCurrentPage(1);
//			page.setPageSize(1);
//			Pager resultPager = infoInformationNewsService.findByCondition(Arrays.asList(InfomationNewsEnum.ADVERTISEMENT.getInfomationType()), infoInformationNews, page);
//			List<InfoInformationNews> adHomeList = (List<InfoInformationNews>) resultPager.getData();
//			InfoInformationNews adHomeinfo = new InfoInformationNews();
//			if(adHomeList != null && adHomeList.size() > 0){
//				adHomeinfo = adHomeList.get(0);
//				if(StringUtils.isNotBlank(adHomeinfo.getImgUrl())){
//					adHomeinfo.setIcon(ossUrl + adHomeinfo.getImgUrl());
//				}
//				adHomeinfo.setContent(this.getImgFromStr(adHomeinfo.getContent()));
//			}
//			adHomeMap = AppResultUtil.successOfInProperties(adHomeinfo, "查询成功"
//					, "title"/** 标题 **/
//					, "url" /** 跳转链接 **/
//					, "content"/** 内容 图片链接 **/
//			);
//		}

        return AppResultUtil.successOfMsg("查询成功").addResParameter("carouseList", carouselFigureMap != null ? carouselFigureMap.get(AppResultUtil.DATA_LIST) : new ArrayList<Object>())
				.addResParameter("appMenuBankGround",appMenuMap != null ? appMenuMap : new HashMap<>())
                .addResParameter("infomationList", infoMap != null ? infoMap.get(AppResultUtil.DATA_LIST) : new ArrayList<Object>())
				//.addResParameter("adHomeMap" , adHomeMap != null ? adHomeMap : new HashMap())  从此接口抽出来放入searchInformationByType，方便app段控制
                .addResParameter("serviceHotline",  UserConstants.SERVICE_HOTLINE)
                .addResParameter("operationDay", DateUtils.getDaysBetween(new Date(), DateUtils.parse(PropertiesHolder.getProperty("hours.of.operation"))));
	}
	/***
	 *  @Description    : 通过ID查询资讯详情
	 *  @Method_Name    : searchInfomationDetail;
	 *  @param request
	 *  @param infomationId
	 *  @return
	 *  @return         : ModelAndView;
	 *  @Creation Date  : 2018年9月5日 上午9:56:46;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchInfomationDetail")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ModelAndView searchInfomationDetail(HttpServletRequest request, @RequestParam("infomationId") Integer infomationId) {
	    ModelAndView mav = new ModelAndView("info/infomationPage");
        InfoInformationNews infoInformationNews = infoInformationNewsService.findInfoInformationNewsById(infomationId);
        mav.addObject("infoInformationNews", infoInformationNews);
        return mav;
    }
	
	/***
     * @Description : 位置、渠道分页查询资讯信息
     * @Method_Name : findInformationRecord;
     * @param position
     *            位置
     * @param channel
     *            渠道
     * @param resideSelect
     *            所属版块
     * @param pager
     * @return
     * @return : Map<String,Object>;
     * @Creation Date : 2018年3月8日 上午11:12:42;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findInformationRecord")
    @ResponseBody
    public Map<String, Object> findInformationRecord(HttpServletRequest request,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "resideSelect", required = false) Integer resideSelect) {
        InfoInformationNews infoInformationNews = new InfoInformationNews();
        infoInformationNews.setType(InfomationNewsEnum.ACTIVITY.getInfomationType());
        infoInformationNews.setChannel(channel);
        infoInformationNews.setResideSelect(resideSelect);
        infoInformationNews.setState(InfomationConstants.INFO_VALID);
        infoInformationNews.setSortColumns("sort asc,create_time desc");
        //如果是友乾人的活动的话，只显示前三条
        Pager pager = new Pager();
        pager.setCurrentPage(1);
        pager.setPageSize(3);
        Pager activityPager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
        //如果是友乾人的公司公告的话，只显示前两条
        infoInformationNews.setPosition(InfomationConstants.INFO_POSITION_FIRST_PAGE);
        infoInformationNews.setType(InfomationNewsEnum.COMPANY_NOTICE.getInfomationType());
        pager.setCurrentPage(1);
        pager.setPageSize(2);
        Pager noticePager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
        //如果是友乾人的平台资讯的话，只显示一条
        pager.setCurrentPage(1);
        pager.setPageSize(1);
        infoInformationNews.setPosition(null);
        infoInformationNews.setType(InfomationNewsEnum.COMPANY_NEWS.getInfomationType());
        Pager newsPager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
        Map<String,Object> resultActivity = buildInfomationData(activityPager,request);
        Map<String,Object> resultNotice = buildInfomationData(noticePager,request);
        Map<String,Object> resultNews = buildInfomationData(newsPager,request);
        return AppResultUtil.successOfMsg("查询成功")
       .addResParameter("noticeList", resultNotice != null ? resultNotice.get(AppResultUtil.DATA_LIST) : new ArrayList<Object>())
        .addResParameter("newsList", resultNews != null ? resultNews.get(AppResultUtil.DATA_LIST) : new ArrayList<Object>())
        .addResParameter("activityList", resultActivity != null ? resultActivity.get(AppResultUtil.DATA_LIST) : new ArrayList<Object>());

    }
    /**
     *  @Description    : 组装返回前台数据
     *  @Method_Name    : buildInfomationData;
     *  @param pagerInfo
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年9月5日 上午10:56:03;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    private Map<String, Object> buildInfomationData(Pager pagerInfo,HttpServletRequest request){
        if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
            List<InfoInformationNews> infoInformationList = new ArrayList<InfoInformationNews>();
            for (InfoInformationNews infoInformation : (List<InfoInformationNews>) pagerInfo.getData()) {
                if(StringUtils.isNotBlank(infoInformation.getImgUrl())){
                    infoInformation.setIcon(ossUrl + infoInformation.getImgUrl());
                }
                if(StringUtils.isBlank(infoInformation.getUrl())){
                    infoInformation.setUrl(request.getScheme() + "://" + request.getServerName() + ":"
                            + request.getServerPort() + request.getContextPath() + "/informationController/searchInfomationDetail?infomationId=" + infoInformation.getId());
                }
                infoInformationList.add(infoInformation);
            }
            pagerInfo.setData(infoInformationList);
            return AppResultUtil.successOfListInProperties(pagerInfo.getData(), "查询成功", "id"/** 资讯ID **/
                    , "title"/** 资讯标题 **/
                    , "createTime"/** 资讯创建时间 **/
                    , "icon"/** 资讯图标 **/
                    , "url"/** 资讯图标链接 **/
            );
        }
        return null;
    }
    /***
     *  @Description    : 根据类型查询公司动态列表信息（公告，资讯，活动）
     *  @Method_Name    : listCompanyDynamicRecord;
     *  @param request
     *  @param type 资讯类型
     *  @param position 位置
     *  @param channel 渠道
     *  @param resideSelect
     *  @param pager
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年9月6日 上午10:31:18;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/listCompanyDynamicRecord")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> listCompanyDynamicRecord(HttpServletRequest request,@RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "position", required = false) Integer position,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "resideSelect", required = false) Integer resideSelect, Pager pager) {
        pager.setInfiniteMode(true);
        InfoInformationNews infoInformationNews = new InfoInformationNews();
        infoInformationNews.setType(type);
        infoInformationNews.setChannel(channel);
        infoInformationNews.setPosition(position);
        infoInformationNews.setResideSelect(resideSelect);
        infoInformationNews.setState(InfomationConstants.INFO_VALID);
        infoInformationNews.setSortColumns("sort asc,create_time desc");
        Pager resultPager = infoInformationNewsService.findInfoInformationNewsList(infoInformationNews, pager);
        // 解析组装资讯图标信息
        if (!BaseUtil.resultPageHasNoData(resultPager)) {
            List<InfoInformationNews> infoInformationList = new ArrayList<InfoInformationNews>();
            for (InfoInformationNews infoInformation : (List<InfoInformationNews>) resultPager.getData()) {
                   //设置ICON图标的URL路径
                   if(StringUtils.isNotBlank(infoInformation.getImgUrl())){
                        infoInformation.setIcon(ossUrl + infoInformation.getImgUrl());
                    }
                    if(StringUtils.isBlank(infoInformation.getUrl())){
                        //如果是调查问卷，因为页面展示信息不一样，所以跳转的URL需要单独处理
                        if ((type == null ? -1 : type) == InfomationNewsEnum.RESEARCH_QUESTION.getInfomationType()){
                            infoInformation.setUrl(request.getScheme() + "://" + request.getServerName() + ":"
                                    + request.getServerPort() + request.getContextPath() + "/informationController/searchQuestionnaireDetail?infomationId=" + infoInformation.getId() +"&channel="+channel);
                        }else{
                            infoInformation.setUrl(request.getScheme() + "://" + request.getServerName() + ":"
                                    + request.getServerPort() + request.getContextPath() + "/informationController/searchInfomationDetail?infomationId=" + infoInformation.getId());
                        }
                    }
                    if (InfomationNewsEnum.FUNCTION_GUIDE.getInfomationType() == infoInformation.getType()) {
                        infoInformation.setContent(HtmlUtils.Html2Text(infoInformation.getContent()));
                    }
                    infoInformationList.add(infoInformation);
            }
            resultPager.setData(infoInformationList);
        }
        // 如果类型是福利，活动的话，返回点赞数和浏览数的字段
        if ((type == null ? -1 : type) == InfomationNewsEnum.ACTIVITY.getInfomationType()
                || (type == null ? -1 : type) == InfomationNewsEnum.WELFARE.getInfomationType()) {
            return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id"/** 资讯ID **/
                    , "title"/** 资讯标题 **/
                    , "source"/** 资讯来源 **/
                    , "eulogizeNum"/** 资讯点赞数 **/
                    , "browseNum"/** 资讯浏览数 **/
                    , "createTime"/** 资讯创建时间 **/
                    , "icon"/** 资讯图标 **/
                    , "url"/** 资讯链接**/
            );
        }
        return AppResultUtil.successOfListInProperties(resultPager.getData(), "查询成功", "id", /** 资讯ID **/
                "title", /** 资讯标题 **/
                "source"/** 资讯来源 **/
                , "createTime"/** 资讯创建时间 **/
                , "icon"/** 资讯图标 **/
                , "url"/** 资讯链接 **/
                , "content"/** 资讯内容 **/
        );
    }
    /**
     *  @Description    : 调查问卷详情
     *  @Method_Name    : searchQuestionnaireDetail;
     *  @param request
     *  @param infomationId
     *  @return
     *  @return         : ModelAndView;
     *  @Creation Date  : 2018年10月19日 下午2:35:07;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/searchQuestionnaireDetail")
    @ResponseBody
    public ModelAndView searchQuestionnaireDetail(HttpServletRequest request, @RequestParam("infomationId") Integer infomationId,@RequestParam("channel") Integer channel) {
        RegUser regUser =BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
                () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId())); 
        ModelAndView mav = new ModelAndView("info/questionPage");
        mav.addObject("infomationId", infomationId);
        mav.addObject("channel", channel);
        mav.addObject("url", request.getScheme() + "://" + request.getServerName() + ":"
                                    + request.getServerPort() + request.getContextPath());
        mav.addObject("regUserId", regUser!=null?regUser.getId():0);
        return mav;
    }
    /**
     *  @Description    : 查询问卷调查详情
     *  @Method_Name    : findQuestionnaireInfo;
     *  @param request
     *  @param infomationId
     *  @param regUserId
     *  @return
     *  @return         : ModelAndView;
     *  @Creation Date  : 2018年10月30日 下午1:49:54;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findQuestionnaireInfo")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    @ResponseBody
    public ResponseEntity<?>  findQuestionnaireInfo(HttpServletRequest request, @RequestParam("infomationId") Integer infomationId,@RequestParam(value = "regUserId", required = false) Integer regUserId) {
        return informationNewsService.findQuestionnaireInfo(infomationId, regUserId);
    }
    /**
     *  @Description    : 关于我们
     *  @Method_Name    : aboutUsPage;
     *  @return
     *  @return         : ModelAndView;
     *  @Creation Date  : 2018年10月25日 下午5:31:42;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/aboutUsPage")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ModelAndView aboutUsPage() {
        ModelAndView mav = new ModelAndView("about_us/aboutUs");
        return mav;
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
    @RequestMapping("/findExistQuestion")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    @ResponseBody
    public ResponseEntity<?>  findExistQuestion(HttpServletRequest request, @RequestParam("infomationId") Integer infomationId,@RequestParam("regUserId") Integer regUserId) {
        return informationNewsService.findExistQuestion(infomationId, regUserId);
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
	public ResponseEntity<?> isAnswerQuestionnaire(HttpServletRequest request, @RequestParam("source") String source){
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		ResponseEntity<?> result = new ResponseEntity<>(UserConstants.USER_QUESTIONNAIRE_YES);

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
			// 校验用户是否做过风险测评/调查问卷
			InfoQuestionnaireAnswer infoQuestionnaireAnswer = new InfoQuestionnaireAnswer();
			infoQuestionnaireAnswer.setRegUserId(regUser.getId());
			infoQuestionnaireAnswer.setInfoQuestionnaireId(infoInformationNewsList.get(0).getId());
			int questionnaireAnswerCount = infoQuestionnaireAnswerService.findInfoQuestionnaireAnswerCount(infoQuestionnaireAnswer);
			if(questionnaireAnswerCount <= 0){	//没有做过调查问卷
				result.setResStatus(UserConstants.USER_QUESTIONNAIRE_NO);
				result.getParams().put("informationId",infoInformationNewsList.get(0).getId());
			}
		}
		return result;
	}
}
