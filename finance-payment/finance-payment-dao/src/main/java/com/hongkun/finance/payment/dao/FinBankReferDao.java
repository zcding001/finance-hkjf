package com.hongkun.finance.payment.dao;

import java.util.List;

import com.hongkun.finance.payment.model.FinBankRefer;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinBankReferDao.java
 * @Class Name : FinBankReferDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinBankReferDao extends MyBatisBaseDao<FinBankRefer, java.lang.Long> {
	/**
	 * @Description : 查询各渠道公共的银行卡信息，用作充值展示
	 * @Method_Name : findBankInfo;
	 * @param key
	 *            银行卡缓存KEY
	 * @param thirdCode
	 *            支付渠道 CODE
	 * @param payWayCode
	 *            支付方式
	 * @param regUserType
	 *            用户类型
	 * @param state
	 *            状态
	 * @return
	 * @return : List<FinBankRefer>;
	 * @Creation Date : 2017年12月22日 下午6:01:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	List<FinBankRefer> findBankInfo(String key, String thirdCode, String payWayCode, String regUserType, Integer state);

	/**
	 * @Description :根据平台银行CODE,或第三方银行CODE，查询银行信息
	 * @Method_Name : findBankRefer;
	 * @param key
	 *            缓存KEY
	 * @param thirdCode
	 *            渠道代码
	 * @param payWayCode
	 *            支付方式
	 * @param regUserType
	 *            用户类型
	 * @param bankCode
	 *            平台银行编码
	 * @param state
	 *            状态
	 * @param thirdBankCode
	 *            第三方银行编码
	 * @return
	 * @return : FinBankRefer;
	 * @Creation Date : 2018年4月4日 下午2:07:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	FinBankRefer findBankRefer(String key, String thirdCode, String payWayCode, String regUserType, String bankCode,
			Integer state, String thirdBankCode);
}
