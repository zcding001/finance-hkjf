package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.RegUserDetailDao;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Date;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserDetailDaoImpl.java
 * @Class Name    : RegUserDetailDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserDetailDaoImpl extends MyBatisBaseDaoImpl<RegUserDetail, java.lang.Long> implements RegUserDetailDao {

	public static final String FIND_REGUSERDETAIL_NAME_BY_REGUSERID = ".findRegUserDetailNameByRegUserId";

	@Override
	public RegUserDetail findRegUserDetailByGroupCode(String groupCode) {
		RegUserDetail regUserDetail = new RegUserDetail();
		regUserDetail.setGroupCode(groupCode);
		return this.findSingleUserDetial(regUserDetail);
	}

	@Override
	public RegUserDetail findRegUserDetailByExtenSource(String extenSource) {
		RegUserDetail regUserDetail = new RegUserDetail();
		regUserDetail.setExtenSource(extenSource);
		return this.findSingleUserDetial(regUserDetail);
	}
	
	@Override
	public RegUserDetail findRegUserDetailByInviteNo(String inviteNo) {
		RegUserDetail regUserDetail = new RegUserDetail();
		regUserDetail.setInviteNo(inviteNo);
		return this.findSingleUserDetial(regUserDetail);
	}

	@Override
	public RegUserDetail findRegUserDetailByCommendNo(String commendNo) {
		RegUserDetail regUserDetail = new RegUserDetail();
		regUserDetail.setInviteNo(commendNo);
		return this.findSingleUserDetial(regUserDetail);
	}

	@Override
	public RegUserDetail findRegUserDetailByRegUserId(int regUserId) {
		RegUserDetail regUserDetail = new RegUserDetail();
		regUserDetail.setRegUserId(regUserId);
		return findSingleUserDetial(regUserDetail);
	}
	
	/**
	 *  @Description    : 检索唯一数据
	 *  @Method_Name    : findSingleUserDetial
	 *  @param regUserDetail
	 *  @return
	 *  @return         : RegUserDetail
	 *  @Creation Date  : 2017年5月24日 上午11:42:50 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private RegUserDetail findSingleUserDetial(RegUserDetail regUserDetail){
		List<RegUserDetail> list = this.findByCondition(regUserDetail);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public String findRegUserDetailNameByRegUserId(int regUserId) {
		return getCurSqlSessionTemplate().selectOne(RegUserDetail.class.getName() +
				FIND_REGUSERDETAIL_NAME_BY_REGUSERID, regUserId);
	}

	@Override
	public List<RegUser> findRegUserDetailListByBirthDay(Date currentDate) {
		return getCurSqlSessionTemplate().selectList(RegUserDetail.class.getName() +
				".findRegUserDetailListByBirthDay", currentDate);
	}

	@Override
	public void updateInvestFlagByRegUserIds(List<Integer> regUserIds) {
		super.getCurSqlSessionTemplate().update(RegUserDetail.class.getName()
				+ ".updateInvestFlagByRegUserIds", regUserIds);
	}

}
