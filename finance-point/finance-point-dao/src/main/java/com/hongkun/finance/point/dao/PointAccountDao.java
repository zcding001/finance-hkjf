package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointAccount;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointAccountDao.java
 * @Class Name    : PointAccountDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointAccountDao extends MyBatisBaseDao<PointAccount, Long> {
	/**
	 *  @Description    : 通过userId查询积分账户
	 *  @Method_Name    : findPointAccountByRegUserId
	 *  @param regUserId
	 *  @return
	 *  @return         : PointAccount
	 *  @Creation Date  : 2017年8月16日 下午5:13:50 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	PointAccount findPointAccountByRegUserId(int regUserId);
	
	void updateByRegUserId(PointAccount pointAccount);






}
