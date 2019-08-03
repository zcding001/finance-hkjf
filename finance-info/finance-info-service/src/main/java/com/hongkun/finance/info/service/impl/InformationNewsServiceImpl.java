package com.hongkun.finance.info.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.dao.InfoBrowsingRecordDao;
import com.hongkun.finance.info.dao.InfoInformationNewsDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireAnswerDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireItemDao;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.model.InfoBrowsingRecord;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;
import com.hongkun.finance.info.model.InfoQuestionnaireItem;
import com.hongkun.finance.info.service.InformationNewsService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class InformationNewsServiceImpl implements InformationNewsService {

	private static final Logger logger = LoggerFactory.getLogger(InformationNewsServiceImpl.class);
	@Autowired
	private InfoBrowsingRecordDao infoBrowsingRecordDao;
	@Autowired
	private InfoInformationNewsDao infoInformationNewsDao;
	@Autowired
    private InfoQuestionnaireDao infoQuestionnaireDao;
	@Autowired
    private InfoQuestionnaireItemDao infoQuestionnaireItemDao;
	@Autowired
    private InfoQuestionnaireAnswerDao infoQuestionnaireAnswerDao;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findInformationByType(int type, Pager pager) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Pager pagerInfo = null;
		List<InfoInformationNews> newInfoInformationNewsList = new ArrayList<InfoInformationNews>();
		try {
			List<InfoInformationNews> infoInformationNewsList = null;
			// 根据类型状态查询资讯分页信息
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			infoInformationNews.setType(type);
			infoInformationNews.setState(InfomationConstants.INFO_VALID);
			infoInformationNews.setSortColumns("id desc");
			pagerInfo = infoInformationNewsDao.findByCondition(infoInformationNews, pager);
			if (pagerInfo != null && pagerInfo.getData() != null) {
				infoInformationNewsList = (List<InfoInformationNews>) pagerInfo.getData();
				for (InfoInformationNews info : infoInformationNewsList) {
					// 资讯浏览数
					InfoBrowsingRecord infoBrowsingRecord = new InfoBrowsingRecord();
					infoBrowsingRecord.setType(InfomationConstants.BROWSE_NUM);
					infoBrowsingRecord.setInfoInformationNewsId(info.getId());
					int browseNum = infoBrowsingRecordDao.getTotalCount(infoBrowsingRecord);
					// 资讯点赞数
					InfoBrowsingRecord eulogizeNumRecord = new InfoBrowsingRecord();
					eulogizeNumRecord.setType(InfomationConstants.EULOGIZE_NUM);
					eulogizeNumRecord.setInfoInformationNewsId(info.getId());
					int eulogizeNum = infoBrowsingRecordDao.getTotalCount(eulogizeNumRecord);
					info.setEulogizeNum(info.getEulogizeNum() + eulogizeNum);
					info.setBrowseNum(info.getBrowseNum() + browseNum);
					newInfoInformationNewsList.add(info);
				}
			}
			pagerInfo.setData(newInfoInformationNewsList);
		} catch (Exception e) {
			logger.error("通过资讯类型查询资讯信息, 资讯类型: {}, 查询资讯信息失败: ", type, e);
		}
		resultMap.put("type", type);
		resultMap.put("pager", pagerInfo);
		responseEntity.setParams(resultMap);
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> eulogizeInformations(int infomationsNewsId, int type, int source, int regUserId) {
		// 通过资讯ID，及类型和用户ID，查询该用户是否已经点赞或浏览该资讯
		InfoBrowsingRecord browsingRecord = new InfoBrowsingRecord();
		browsingRecord.setType(type);
		browsingRecord.setInfoInformationNewsId(infomationsNewsId);
		browsingRecord.setRegUserId(regUserId);
		int nums = infoBrowsingRecordDao.getTotalCount(browsingRecord);
		// 如果查询结果大于0，说明该用户已经点赞，或浏览该资讯
		if (nums > 0 && type == InfomationConstants.EULOGIZE_NUM) {
			return new ResponseEntity<>(Constants.SUCCESS, "该用户已经点赞!");
		}
		if (nums > 0 && type == InfomationConstants.BROWSE_NUM) {
			return new ResponseEntity<>(Constants.SUCCESS, "该用户已经浏览!");
		}
		// 组装插入资讯记录信息的浏览或点赞记录
		InfoBrowsingRecord infoBrowsingRecord = new InfoBrowsingRecord();
		infoBrowsingRecord.setInfoInformationNewsId(infomationsNewsId);
		infoBrowsingRecord.setRegUserId(regUserId);
		infoBrowsingRecord.setSource(source);
		infoBrowsingRecord.setType(type);
		infoBrowsingRecord.setCreateTime(new Date());
		if (infoBrowsingRecordDao.save(infoBrowsingRecord) > 0) {
			// 每天第一个用户访问该资讯时，会生成该资讯的点赞或浏览的随机数
			buildInfomationRandom(infomationsNewsId, type);
			return new ResponseEntity<>(Constants.SUCCESS, "点赞或浏览成功！");
		}
		return new ResponseEntity<>(Constants.ERROR, "点赞或浏览失败!");
	}

	/**
	 * @Description :生成资数的点赞或浏览的随机数，并更新
	 * @Method_Name : buildInfomationRandom;
	 * @param infomationsNewsId
	 *            资讯ID
	 * @param type
	 *            类型
	 * @return : void;
	 * @Creation Date : 2017年8月22日 下午2:11:26;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void buildInfomationRandom(int infomationsNewsId, int type) {
		try {
			InfoBrowsingRecord infoBrowsingRecordCondition = new InfoBrowsingRecord();
			infoBrowsingRecordCondition.setInfoInformationNewsId(infomationsNewsId);
			infoBrowsingRecordCondition.setType(type);
			infoBrowsingRecordCondition.setCreateTimeBegin(DateUtils.getFirstTimeOfDay());
			infoBrowsingRecordCondition.setCreateTimeEnd(DateUtils.getLastTimeOfDay());
			// 查询当天最早时间与最晚时间段内的点赞或浏览数，如果当天是第一次点赞或浏览会生成点赞或浏览的随机数
			int count = infoBrowsingRecordDao.getTotalCount(infoBrowsingRecordCondition);
			// 如果count==1说明是当天第一次操作此资讯信息
			// 根据资讯ID,查询对应资讯信息
			InfoInformationNews infoInformationNews = infoInformationNewsDao.findByPK(Long.valueOf(infomationsNewsId),
					InfoInformationNews.class);
			if (count == 1) {
				// 如果是点赞，则生成点赞的随机数
				if (infoInformationNews != null && type == InfomationConstants.EULOGIZE_NUM) {
					int eulogizeNum = infoInformationNews.getEulogizeNum();// 随机点赞数
					eulogizeNum = randomNum(eulogizeNum, 20);
					if (updateInfomationById(eulogizeNum, infomationsNewsId, InfomationConstants.EULOGIZE_NUM) <= 0) {
						logger.info("更新资讯随机数, 资讯id:{}, 更新资讯点赞随机数失败", infomationsNewsId);
						throw new BusinessException("更新资讯点赞随机数失败!");
					}
				}
				// 如果是浏览，则生成浏览的随机数
				if (infoInformationNews != null && type == InfomationConstants.BROWSE_NUM) {
					int browseNum = infoInformationNews.getBrowseNum();// 随机浏览数
					browseNum = randomNum(browseNum, 30);
					if (updateInfomationById(browseNum, infomationsNewsId, InfomationConstants.BROWSE_NUM) <= 0) {
						logger.error("更新资讯随机数, 资讯id: {}, 更新资讯浏览随机数失败", infomationsNewsId);
						throw new BusinessException("更新资讯随机数失败!");
					}
				}
			}
		} catch (Exception e) {
			logger.error("更新资讯随机数, 资讯id: {}, 更新资讯随机数失败: ", infomationsNewsId, e);
			throw new GeneralException("更新资讯随机数失败!");
		}
	}

	/**
	 * @Description :通过ID更新资讯的点赞和浏览数
	 * @Method_Name : updateInfomationById;
	 * @param randomNum
	 *            随机数
	 * @param id
	 *            资讯 ID
	 * @param type
	 *            类型
	 * @return
	 * @return : int;
	 * @Creation Date : 2018年3月8日 上午11:06:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private int updateInfomationById(int randomNum, int id, int type) {
		logger.info("方法: updateInfomationById, 通过ID更新资讯的点赞和浏览数, 资讯ID: {}, 入参: id: {}, randomNum: {}, type: {}", id, id,
				randomNum, type);
		try {
			InfoInformationNews infoInformationNews = new InfoInformationNews();
			infoInformationNews.setId(id);
			if (type == InfomationConstants.EULOGIZE_NUM) {
				infoInformationNews.setEulogizeNum(randomNum);
			} else {
				infoInformationNews.setBrowseNum(randomNum);
			}
			logger.info("资讯信息Id: {}, 资讯类型:{}, 通过ID更新资讯的点赞和浏览数", id, type);
			return this.infoInformationNewsDao.update(infoInformationNews);
		} catch (Exception e) {
			logger.error("通过ID更新资讯的点赞和浏览数, 资讯ID: {}, 更新失败: ", id, e);
			throw new GeneralException("通过ID更新资讯的点赞和浏览数失败!");
		}
	}

	/**
	 * @Description :生成点赞的或浏览的随机数 点赞随机数每次增加 [20-30]之间，浏览随机数每次增加[30-40]之间
	 * @Method_Name : randomNum;
	 * @param beginNum
	 * @param plusNum
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年8月22日 上午10:52:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private int randomNum(int beginNum, int plusNum) {
		Random rand = new Random();
		int randNum = rand.nextInt(11) + plusNum;
		return randNum + beginNum;
	}

	@Override
	public ResponseEntity<?> findInformationNewsLists(String channel, int position,Integer showFlag) {
		// 返回到页面的MAP数据
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		// 组装查询资讯的条件
		InfoInformationNews infoInformationNews = new InfoInformationNews();
		infoInformationNews.setChannel(channel);
		infoInformationNews.setPosition(position);
		infoInformationNews.setState(InfomationConstants.INFO_VALID);
		infoInformationNews.setShowFlag(showFlag);
		infoInformationNews.setSortColumns("sort asc,create_time desc");
		// 1、查询banner图数据
		infoInformationNews.setType(InfomationNewsEnum.CAROUSEL_FIGURE.getInfomationType());
		List<InfoInformationNews> carsouleFigureList = infoInformationNewsDao.findByCondition(infoInformationNews);
		resultInfo.put("carsouleFigureList", carsouleFigureList);
		// 2、查询公告数据
		infoInformationNews.setShowFlag(null);
		infoInformationNews.setType(InfomationNewsEnum.COMPANY_NOTICE.getInfomationType());
		List<InfoInformationNews> noticeList = infoInformationNewsDao.findByCondition(infoInformationNews);
		resultInfo.put("noticeList", noticeList);
		// 如果位置不是积分商城，则查询公司动态数据、媒体报道数据信息
		if (InfomationConstants.INFO_POSITION_POINT != position) {
			// 3、查询公司动态数据
			infoInformationNews.setSortColumns("create_time desc limit 5");
			infoInformationNews.setType(InfomationNewsEnum.COMPANY_NEWS.getInfomationType());
			List<InfoInformationNews> companyDynamicList = infoInformationNewsDao.findByCondition(infoInformationNews);
			resultInfo.put("dynamicList", companyDynamicList);
			// 4、查询媒体报道数据
			infoInformationNews.setType(InfomationNewsEnum.MEDIA_REPORT.getInfomationType());
			List<InfoInformationNews> mediaReportList = infoInformationNewsDao.findByCondition(infoInformationNews);
			resultInfo.put("mediaReportList", mediaReportList);
		}

		return new ResponseEntity<>(Constants.SUCCESS, resultInfo);
	}

    @Override
    public ResponseEntity<?> findQuestionnaireInfo(Integer infomaationId, Integer regUserId) {
        ResponseEntity<?>  resEntity = null;
        Map<String,Object> resultMap =new HashMap<String,Object>();
        //查询调查问卷信息
        InfoInformationNews infoInformationNews =  infoInformationNewsDao.findByPK(Long.parseLong(infomaationId.toString()), InfoInformationNews.class);
        if(infoInformationNews ==  null){
            resEntity = new ResponseEntity<>(Constants.ERROR,"没有查询到调查问卷信息!");
            return resEntity;
        }
        //查询该调查问卷调查的问题
        InfoQuestionnaire  infoQuestionnaire = new InfoQuestionnaire();
        infoQuestionnaire.setInfoInformationNewsId(infoInformationNews.getId());
        infoQuestionnaire.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
        List<InfoQuestionnaire> questionnaireList = infoQuestionnaireDao.findByCondition(infoQuestionnaire);
        if(questionnaireList == null || questionnaireList.size() <=0){
            resEntity = new ResponseEntity<>(Constants.ERROR,"此调查没有调查问题!");
            return resEntity;
        }
        //查询调查问题的选项内容及答案
        for (InfoQuestionnaire questionnaire : questionnaireList) {
            InfoQuestionnaireItem infoQuestionnaireItem = new InfoQuestionnaireItem();
            infoQuestionnaireItem.setInfoQuestionnaireId(questionnaire.getId());
            infoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
            infoQuestionnaireItem.setSortColumns("id desc");
            List<InfoQuestionnaireItem> questionItemList = infoQuestionnaireItemDao.findByCondition(infoQuestionnaireItem);
            questionnaire.setQuestionnaireItemList(questionItemList);
            //如果用户ID不为空，查询问题的答案
            if(regUserId != null && regUserId !=0){
                InfoQuestionnaireAnswer infoQuestionnaireAnswer = infoQuestionnaireAnswerDao.findInfoQuestionnaireAnswer(regUserId, questionnaire.getId());
                questionnaire.setQuestionnaireAnswer(infoQuestionnaireAnswer);
            }
        }
        
        JSONArray problems = new JSONArray(questionnaireList.size());
        for(int i = 0 ; i < questionnaireList.size() ; i++){
            InfoQuestionnaire questionnaire = questionnaireList.get(i);
            JSONObject problem = new JSONObject();
            problem.put("problemId", questionnaire.getId());
            problem.put("problemAnswerType", questionnaire.getType());
            problem.put("problemAnswerTypeDsc", "1:单选 2:多选 3:文本");
            problem.put("problemContent", questionnaire.getContent());
            problem.put("problemSort", questionnaire.getSort());
            problem.put("problemDiaoChaId", questionnaire.getInfoInformationNewsId());
            
            //选项
            if(!"3".equals(questionnaire.getType())){
                JSONArray options = new JSONArray(questionnaire.getQuestionnaireItemList().size());
                for(int j = 0 ; j < questionnaire.getQuestionnaireItemList().size() ; j++){
                    InfoQuestionnaireItem item = questionnaire.getQuestionnaireItemList().get(j);
                    JSONObject option = new JSONObject();
                    option.put("optionId", item.getId());
                    option.put("optionProblemId", item.getInfoQuestionnaireId());
                    option.put("optionContent", item.getOptionContent());
                    option.put("optionTitle", item.getOptionTitle());
                    option.put("optionSort", item.getSort());
                    options.add(j, option);
                    problem.put("options", options);
                }
            }
            //答案
            InfoQuestionnaireAnswer answer = questionnaire.getQuestionnaireAnswer();
            if(answer == null){
                problem.put("answerId", "");
                problem.put("answer", "");
                problem.put("answerUser", regUserId == null ? "":regUserId);
                problem.put("answerProblemId", "");
            }else{
                problem.put("answerId", answer.getId());
                problem.put("answer", answer.getAnswer());
                problem.put("answerUser", answer.getRegUserId());
                problem.put("answerProblemId", answer.getInfoQuestionnaireId());
            }
            problems.add(i,problem);
        }
        resultMap.put("diaochaTitle", infoInformationNews.getTitle());
        resultMap.put("diaochaContent", infoInformationNews.getContent());
        resultMap.put("diaochaCreateTime", infoInformationNews.getCreateTime());
        resultMap.put("diaochaCreateUser", "鸿坤金服");
        resultMap.put("diaochaId", infoInformationNews.getId());
        resultMap.put("problems", problems);
        resEntity = new ResponseEntity<>(Constants.SUCCESS);
        resEntity.setParams(resultMap);
        return resEntity;
    }

    @Override
    public ResponseEntity<?> findExistQuestion(Integer infomationId, Integer regUserId) {
        ResponseEntity<?>  resEntity = null;
        //查询调查问卷信息
        InfoInformationNews infoInformationNews =  infoInformationNewsDao.findByPK(Long.parseLong(infomationId.toString()), InfoInformationNews.class);
        if(infoInformationNews ==  null){
            resEntity = new ResponseEntity<>(Constants.ERROR,"没有查询到调查问卷信息!");
            return resEntity;
        }
        if(infoInformationNews.getState() !=InfomationConstants.INFO_VALID){
            resEntity = new ResponseEntity<>(Constants.ERROR,"该调查问卷已下架!");
            return resEntity;
        }
        int count = infoQuestionnaireAnswerDao.findAnswerByInfomationId(regUserId, infomationId);
        if(count > 0){
            resEntity = new ResponseEntity<>(Constants.ERROR,"已经回答过此调查问卷，不能再进行调查!");
            return resEntity;
        }
        resEntity = new ResponseEntity<>(Constants.SUCCESS);
        return resEntity;
    }

}
