package com.hongkun.finance.info.dao;

import com.hongkun.finance.info.model.InfoQuestionnaireItem;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.dao.InfoQuestionnaireItemDao.java
 * @Class Name : InfoQuestionnaireItemDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface InfoQuestionnaireItemDao extends MyBatisBaseDao<InfoQuestionnaireItem, java.lang.Long> {
	/**
	 * @Description : 更新选项内容通过问题Id
	 * @Method_Name : updateQuestionItemByQuestionId;
	 * @param infoQuestionnaireItem
	 * @return : void;
	 * @Creation Date : 2018年3月15日 下午7:31:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public void updateQuestionItemByQuestionId(InfoQuestionnaireItem infoQuestionnaireItem);
}
