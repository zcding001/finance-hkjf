package com.hongkun.finance.fund.facade;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface FundInfoFacade {
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
	 ResponseEntity<?> findFundInfoVoByCondition(FundInfoVo contidion, Pager pager);
	 /**
	  * 
	  *  @Description    : 股权认证
	  *  @Method_Name    : getFundAuthentication;
	  *  @param regUser
	  *  @return
	  *  @return         : Integer;
	  *  @Creation Date  : 2018年5月9日 上午11:43:38;
	  *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	  */
	 Integer getFundAuthentication(RegUser regUser);
}
