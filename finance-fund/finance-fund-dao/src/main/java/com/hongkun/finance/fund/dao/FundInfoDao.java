package com.hongkun.finance.fund.dao;

import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.FundInfoDao.java
 * @Class Name    : FundInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface FundInfoDao extends MyBatisBaseDao<FundInfo, java.lang.Long> {

    /**
     *  @Description    ：根据股权项目的id查询股权名称
     *  @Method_Name    ：findFundInfoByIds
     *  @param ids
     *  @return java.util.List<java.lang.String>
     *  @Creation Date  ：2018年04月28日 17:25
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    List<String> findFundInfoByIds(List<String> ids);

	/**
	 * 
	 *  @Description    : 查询股权项目信息
	 *  @Method_Name    : findFundInfoVoByCondition;
	 *  @param contidion
	 *  @param pager
	 *  @return
	 *  @return         : Pager;
	 *  @Creation Date  : 2018年4月28日 下午3:58:16;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	Pager findFundInfoVoByCondition(FundInfoVo contidion, Pager pager);
	/**
	 * 
	 *  @Description    : 获取FundInfo
	 *  @Method_Name    : getFundInfo;
	 *  @param fundInfo
	 *  @return
	 *  @return         : FundInfo;
	 *  @Creation Date  : 2018年5月7日 下午2:59:38;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	FundInfoVo getFundInfo(FundInfoVo fundInfoVo);
	
}
