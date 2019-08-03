package com.hongkun.finance.payment.dao;

import com.hongkun.finance.payment.model.FinBankCardBinding;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinBankCardBindingDao.java
 * @Class Name : FinBankCardBindingDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinBankCardBindingDao extends MyBatisBaseDao<FinBankCardBinding, java.lang.Long> {
	/**
	 * @Description : 查询用户银行卡信息
	 * @Method_Name : findBankCardBinding;
	 * @param bankCardId
	 *            银行卡ID
	 * @param regUserId
	 *            用户Id
	 * @param payChannel
	 *            支付渠道
	 * @return
	 * @return : FinBankCardBinding;
	 * @Creation Date : 2018年4月4日 下午1:01:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinBankCardBinding findBankCardBinding(Integer bankCardId, Integer regUserId, Integer payChannel);
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : updateFinBankCardBinding
	 *  @param finBankCardBinding
	 *  @param pager
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2018-05-15 19:46:53
	 *  @Author         : binliang@honghun.com.cn 梁彬
	 */
	Integer updateFinBankCardBinding(FinBankCardBinding finBankCardBinding);
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : insertFinBankCardBinding
	 *  @param finBankCardBinding
	 *  @param pager
	 *  @return
	 *  @return         : 
	 *  @Creation Date  : 2018-05-17 09:29:16
	 *  @Author         : binliang@honghun.com.cn 梁彬
	 */
	void insertFinBankCardBinding(FinBankCardBinding finBankCardBinding);

	/**
	 *  @Description    ：根据cardId更新状态
	 *  @Method_Name    ：updateFinBankCardBindingByCardId
	 *  @param bankCardId
	 *  @param state
	 *  @return int
	 *  @Creation Date  ：2018年05月18日 10:50
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    int updateFinBankCardBindingByCardId(Integer bankCardId, Integer state);
}
