package com.hongkun.finance.info.dao.impl;

import com.hongkun.finance.info.model.InfoInformationNews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.info.dao.InfoInformationNewsDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.dao.impl.InfoInformationNewsDaoImpl.java
 * @Class Name : InfoInformationNewsDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class InfoInformationNewsDaoImpl extends MyBatisBaseDaoImpl<InfoInformationNews, java.lang.Long>
		implements InfoInformationNewsDao {
	/**
	 * 资讯分页查询
	 */
	public String FIND_INFOMATION_NEWS = ".findInfomationNewsPage";

	@Override
	public Pager findByCondition(List<Integer> type, InfoInformationNews infoInformationNews, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("title", infoInformationNews.getTitle());
		type.remove(new Integer(-999));
		paramMap.put("infoTypes", type);
		paramMap.put("state", infoInformationNews.getState());
		paramMap.put("id", infoInformationNews.getId());
		paramMap.put("channel", infoInformationNews.getChannel());
		paramMap.put("position", infoInformationNews.getPosition());
		paramMap.put("showFlag", infoInformationNews.getShowFlag());
		paramMap.put("resideSelect", infoInformationNews.getResideSelect());
		paramMap.put("createTimeBegin", infoInformationNews.getCreateTimeBegin());
		paramMap.put("createTimeEnd", infoInformationNews.getCreateTimeEnd());
		paramMap.put("sortColumns", "create_time desc,sort asc");
		return this.findByCondition(paramMap, pager, InfoInformationNews.class, FIND_INFOMATION_NEWS);
	}

}
