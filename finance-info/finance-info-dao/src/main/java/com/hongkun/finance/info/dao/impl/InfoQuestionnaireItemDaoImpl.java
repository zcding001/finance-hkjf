package com.hongkun.finance.info.dao.impl;

import com.hongkun.finance.info.model.InfoQuestionnaireItem;
import com.hongkun.finance.info.dao.InfoQuestionnaireItemDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.dao.impl.InfoQuestionnaireItemDaoImpl.java
 * @Class Name : InfoQuestionnaireItemDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class InfoQuestionnaireItemDaoImpl extends MyBatisBaseDaoImpl<InfoQuestionnaireItem, java.lang.Long>
		implements InfoQuestionnaireItemDao {
	public String UPDATE_QUESTIONITEM_BY_QUESTIONID = ".updateQuestionItemByQuestionId";

	@Override
	public void updateQuestionItemByQuestionId(InfoQuestionnaireItem infoQuestionnaireItem) {
		this.getCurSqlSessionTemplate().update(
				InfoQuestionnaireItem.class.getName() + UPDATE_QUESTIONITEM_BY_QUESTIONID, infoQuestionnaireItem);
	}

}
