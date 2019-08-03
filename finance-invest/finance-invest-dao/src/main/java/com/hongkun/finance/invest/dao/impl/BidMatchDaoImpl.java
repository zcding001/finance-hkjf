package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.vo.BidMatchVo;

import java.util.List;

import com.hongkun.finance.invest.dao.BidMatchDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.BidMatchDaoImpl.java
 * @Class Name    : BidMatchDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidMatchDaoImpl extends MyBatisBaseDaoImpl<BidMatch, java.lang.Long> implements BidMatchDao {
	/** 查询匹配详情列表*/
	public  String FIND_MATCH_LIST_BY_CONTIDION = ".findMatchListByContidion";
	
	public  String FIND_MATCHVO_LIST_BY_CONTIDION = ".findMatchVoListByContidion";
	
	@Override
	public List<BidMatchVo> findMatchListByContidion(BidMatchVo contidion) {
		return super.getCurSqlSessionTemplate()
				.selectList(BidMatch.class.getName() + FIND_MATCH_LIST_BY_CONTIDION, contidion);
	}

	@Override
	public Pager findMatchVoListByContidion(BidMatchVo contidion, Pager pager) {
		return this.findByCondition(contidion, pager, BidMatch.class, FIND_MATCHVO_LIST_BY_CONTIDION);
	}

}
