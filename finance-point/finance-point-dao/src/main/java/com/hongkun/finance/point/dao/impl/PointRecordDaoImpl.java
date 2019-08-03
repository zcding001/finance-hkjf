package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.dao.PointRecordDao;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointRecordDaoImpl.java
 * @Class Name    : PointRecordDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointRecordDaoImpl extends MyBatisBaseDaoImpl<PointRecord, Long> implements PointRecordDao {
	
	private final String DELETE_BY_CONTIDION = ".deleteByContidion";
	private final String USER_SIGN_COUNT = ".userSignCount";

    @Override
    public Pager listPointRecord(PointVO pointVo, Pager pager) {
        return this.findByCondition(pointVo,pager,PointRecord.class,".listPointRecord");
    }

	@Override
	public int deleteByContidion(PointRecord pointRecord) {
		return getCurSqlSessionTemplate().delete(PointRecord.class.getName()+DELETE_BY_CONTIDION,pointRecord);
	}

	@Override
	public Long userSignCount(Integer currentUserId) {
		return getCurSqlSessionTemplate().selectOne(PointRecord.class.getName() + USER_SIGN_COUNT, currentUserId);
	}
}
