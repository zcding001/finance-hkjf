package com.hongkun.finance.fund.dao.impl;

import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.fund.dao.FundInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.impl.FundInfoDaoImpl.java
 * @Class Name    : FundInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class FundInfoDaoImpl extends MyBatisBaseDaoImpl<FundInfo, java.lang.Long> implements FundInfoDao {


    /**
     * 根据项目的ids查询项目名称names
     */
    private static final String FIND_PROJECT_NAMES_BY_IDS = ".findProjectNamesByIds";

    @Override
    public List<String> findFundInfoByIds(List<String> ids) {
        Map<String,Object> params = new HashMap<>();
        params.put("ids",ids);
        return this.getCurSqlSessionTemplate().selectList(FundInfo.class.getName() + FIND_PROJECT_NAMES_BY_IDS, params);
    }

	public String FIND_FUND_INFO_VO_BY_CONDITION = ".findFundInfoVoByCondition";
	@Override
	public Pager findFundInfoVoByCondition(FundInfoVo vo, Pager pager) {
		return this.findByCondition(vo, pager, FundInfo.class, FIND_FUND_INFO_VO_BY_CONDITION);
		
	}
	@Override
	public FundInfoVo getFundInfo(FundInfoVo fundInfoVo) {
		return  getCurSqlSessionTemplate().selectOne(FundInfo.class.getName()+".getFundInfo", fundInfoVo);
	}

}
