package com.hongkun.finance.invest.dao;

import java.util.List;

import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.vo.BidMatchVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.BidMatchDao.java
 * @Class Name    : BidMatchDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidMatchDao extends MyBatisBaseDao<BidMatch, java.lang.Long> {
	/**
	 *  @Description    : 查询匹配详情列表
	 *  @Method_Name    : findMatchListByContidion
	 *  @param contidion
	 *  @return
	 *  @return         : List<BidMatchVo>
	 *  @Creation Date  : 2017年7月20日 下午5:35:42 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidMatchVo> findMatchListByContidion(BidMatchVo contidion);
	/**
	 *  @Description    : 查询匹配详情列表(分页)
	 *  @Method_Name    : findMatchVoListByContidion
	 *  @param contidion
	 *  @param pager
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月30日 上午9:15:48 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager findMatchVoListByContidion(BidMatchVo contidion, Pager pager);
	
}
