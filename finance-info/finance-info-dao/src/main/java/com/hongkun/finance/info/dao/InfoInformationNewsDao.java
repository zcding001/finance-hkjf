package com.hongkun.finance.info.dao;

import java.util.List;

import com.hongkun.finance.info.model.InfoInformationNews;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.dao.InfoInformationNewsDao.java
 * @Class Name : InfoInformationNewsDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface InfoInformationNewsDao extends MyBatisBaseDao<InfoInformationNews, java.lang.Long> {
	/**
	 * @Description :资讯分页查询
	 * @Method_Name : findByCondition;
	 * @param type
	 *            资讯类型
	 * @param infoInformationNews
	 *            资讯对象
	 * @param pager
	 *            分页对象
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年9月21日 上午9:30:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findByCondition(List<Integer> type, InfoInformationNews infoInformationNews, Pager pager);
}
