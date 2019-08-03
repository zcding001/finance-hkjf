package com.hongkun.finance.roster.dao.impl;

import com.hongkun.finance.roster.dao.RosStaffInfoDao;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.roster.dao.impl.RosStaffInfoDaoImpl.java
 * @Class Name : RosStaffInfoDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class RosStaffInfoDaoImpl extends MyBatisBaseDaoImpl<RosStaffInfo, java.lang.Long> implements RosStaffInfoDao {

	@Override
	public List<RosStaffInfoVo> findRosStaffInfoList(RosStaffInfoVo rosStaffInfoContidion) {
		return getCurSqlSessionTemplate().selectList(RosStaffInfo.class.getName() + ".findRosStaffInfoList",
				rosStaffInfoContidion);
	}

	@Override
	public RosStaffInfo findRosStaffInfo(Integer regUserId, Integer type, Integer state,Integer recommendState) {
		RosStaffInfo rosStaffInfo = new RosStaffInfo();
		rosStaffInfo.setRegUserId(regUserId);
		rosStaffInfo.setType(type);
		rosStaffInfo.setState(state);
		rosStaffInfo.setRecommendState(recommendState);
		return getCurSqlSessionTemplate().selectOne(RosStaffInfo.class.getName() + ".findRosStaffInfo", rosStaffInfo);
	}

	@Override
	public List<Integer> findRosStaffInfoList(List<Integer> regUserIdList, Integer type, Integer state) {
		RosStaffInfo rosStaffInfo = new RosStaffInfo();
		rosStaffInfo.setRegUserIdList(regUserIdList);
		rosStaffInfo.setType(type);
		rosStaffInfo.setState(state);
		return getCurSqlSessionTemplate().selectList(RosStaffInfo.class.getName() + ".findRosStaffInfoByCondition",
				rosStaffInfo);
	}

	@Override
	public List<Integer> findRosStaffInfoByTypes(List<Integer> types) {
		RosStaffInfo rosStaffInfo = new RosStaffInfo();
		rosStaffInfo.setTypes(types);
		return getCurSqlSessionTemplate().selectList(RosStaffInfo.class.getName() + ".findRosStaffInfoByTypes",
				rosStaffInfo);
	}
}
