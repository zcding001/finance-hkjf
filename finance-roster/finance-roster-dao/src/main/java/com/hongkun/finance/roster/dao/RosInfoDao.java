package com.hongkun.finance.roster.dao;

import com.hongkun.finance.roster.model.RosInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.dao.RosInfoDao.java
 * @Class Name    : RosInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RosInfoDao extends MyBatisBaseDao<RosInfo, java.lang.Long> {
	/**
	 *  @Description    : 查询短信的黑白名单约束
	 *  @Method_Name    : findRosInfoForSmsTel
	 *  @param rosInfo
	 *  @return
	 *  @return         : Long
	 *  @Creation Date  : 2018年1月26日 下午4:27:10 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Long findRosInfoForSmsTel(RosInfo rosInfo);
	
}
