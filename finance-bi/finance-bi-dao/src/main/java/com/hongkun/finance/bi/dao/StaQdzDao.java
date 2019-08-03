package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaQdz;
import com.hongkun.finance.bi.model.vo.StaQdzInOutVo;
import com.hongkun.finance.bi.model.vo.StaWithdrawVo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.StaQdzDao.java
 * @Class Name    : StaQdzDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaQdzDao extends MyBatisBaseDao<StaQdz, java.lang.Long> {
	/**
	 *  @Description    : 查询钱袋子转入转出总金额，总次数
	 *  @Method_Name    : findStaQdzSum;
	 *  @param staQdz
	 *  @return
	 *  @return         : StaWithdrawVo;
	 *  @Creation Date  : 2018年9月21日 上午10:15:46;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    StaQdzInOutVo findStaQdzSum(StaQdz staQdz);
}
