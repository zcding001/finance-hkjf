package com.hongkun.finance.vas.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.vas.dao.VasSimRecordDao;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.impl.VasSimRecordDaoImpl.java
 * @Class Name : VasSimRecordDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasSimRecordDaoImpl extends MyBatisBaseDaoImpl<VasSimRecord, java.lang.Long> implements VasSimRecordDao {
	/**
	 * 根据条件分页查询
	 */
	public String FIND_VASSIMRECORD_INFO = ".findListPage";
	/**
	 * 通过条件，查询用户体验金总额
	 */
	public String FIND_SIM_SUM_MONEY = ".findSimSumMoney";
	/**
	 * 查询体验金统计信息
	 */
	public String FIND_SIM_GOLD_COUNT_INFO = ".findSimGoldCountInfo";

	@Override
	public Pager findVasSimRecordListByInfo(Map<String, Object> vasSimRecordMap, Pager pager) {
		return this.findByCondition(vasSimRecordMap, pager, VasSimRecord.class, FIND_VASSIMRECORD_INFO);
	}

	@Override
	public BigDecimal findSimSumMoney(VasSimRecord vasSimRecord) {
		return getCurSqlSessionTemplate().selectOne(VasSimRecord.class.getName() + FIND_SIM_SUM_MONEY, vasSimRecord);
	}

	@Override
	public Map<String, Object> findSimGoldCountInfo() {
		return getCurSqlSessionTemplate().selectOne(VasSimRecord.class.getName() + FIND_SIM_GOLD_COUNT_INFO);
	}

    @Override
    public List<VasSimRecord> getExpiredSimgoldList(Date currentDate) {
        return this.getCurSqlSessionTemplate().selectList(VasSimRecord.class.getName() +
                ".getExpiredSimgoldList", currentDate);
    }

}
