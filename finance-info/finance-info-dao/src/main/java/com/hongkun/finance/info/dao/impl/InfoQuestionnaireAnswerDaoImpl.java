package com.hongkun.finance.info.dao.impl;

import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;

import java.util.HashMap;
import java.util.Map;

import com.hongkun.finance.info.dao.InfoQuestionnaireAnswerDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.dao.impl.InfoQuestionnaireAnswerDaoImpl.java
 * @Class Name    : InfoQuestionnaireAnswerDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class InfoQuestionnaireAnswerDaoImpl extends MyBatisBaseDaoImpl<InfoQuestionnaireAnswer, java.lang.Long> implements InfoQuestionnaireAnswerDao {

    @Override
    public InfoQuestionnaireAnswer findInfoQuestionnaireAnswer(Integer regUserId,
            Integer questionnaireId) {
        InfoQuestionnaireAnswer infoQuestionnaireAnswer=new InfoQuestionnaireAnswer();
        infoQuestionnaireAnswer.setRegUserId(regUserId);
        infoQuestionnaireAnswer.setInfoQuestionnaireId(questionnaireId);
        infoQuestionnaireAnswer.setSortColumns("id desc");
        return (InfoQuestionnaireAnswer) getCurSqlSessionTemplate()
                .selectOne(InfoQuestionnaireAnswer.class.getName() + ".findInfoQuestionnaireAnswer", infoQuestionnaireAnswer);
    }

    @Override
    public int findAnswerByInfomationId(Integer regUserId, Integer infomationId) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("regUserId", regUserId);
        parameter.put("infomationId", infomationId);
        return  getCurSqlSessionTemplate().selectOne(InfoQuestionnaireAnswer.class.getName() + ".findAnswerByInfomationId", parameter);
    }

}
