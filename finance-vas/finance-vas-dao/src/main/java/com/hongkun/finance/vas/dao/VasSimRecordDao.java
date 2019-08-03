package com.hongkun.finance.vas.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.vas.model.VasSimRecord;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.VasSimRecordDao.java
 * @Class Name : VasSimRecordDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasSimRecordDao extends MyBatisBaseDao<VasSimRecord, java.lang.Long> {

	Pager findVasSimRecordListByInfo(Map<String, Object> vasSimRecordMap, Pager pager);

	/**
	 * @Description : 查询MONEY总和
	 * @Method_Name : findSimSumMoney;
	 * @param vasSimRecord
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月12日 上午11:07:30;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findSimSumMoney(VasSimRecord vasSimRecord);

	/**
	 * @Description : 查询体验金发放信息
	 * @Method_Name : findSimGoldCountInfo;
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年10月16日 上午11:24:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Map<String, Object> findSimGoldCountInfo();
	/**
	 *  @Description    : 查询已过期的体验金记录
	 *  @Method_Name    : getExpiredSimgoldist;
	 *  @param currentDate
	 *  @return
	 *  @return         : List<VasSimRecord>;
	 *  @Creation Date  : 2018年12月6日 下午3:09:45;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<VasSimRecord> getExpiredSimgoldList(Date currentDate);
}
