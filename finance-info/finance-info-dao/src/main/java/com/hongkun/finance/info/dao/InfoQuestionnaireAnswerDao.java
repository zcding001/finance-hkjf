package com.hongkun.finance.info.dao;

import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.dao.InfoQuestionnaireAnswerDao.java
 * @Class Name    : InfoQuestionnaireAnswerDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface InfoQuestionnaireAnswerDao extends MyBatisBaseDao<InfoQuestionnaireAnswer, java.lang.Long> {
    /**
     *  @Description    : 根据用户ID，查询问题的答案
     *  @Method_Name    : findInfoQuestionnaireAnswer;
     *  @param regUserId
     *  @param questionnaireId
     *  @return
     *  @return         : InfoQuestionnaireAnswer;
     *  @Creation Date  : 2018年10月30日 下午3:08:39;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    InfoQuestionnaireAnswer findInfoQuestionnaireAnswer(Integer regUserId,Integer questionnaireId);
    /**
     *  @Description    : 获取该用户是否回答过改调查问卷 
     *  @Method_Name    : findAnswerByInfomationId;
     *  @param regUserId
     *  @param infomationId
     *  @return
     *  @return         : int;
     *  @Creation Date  : 2018年10月31日 上午11:49:01;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    int  findAnswerByInfomationId(Integer regUserId,Integer infomationId);
}
