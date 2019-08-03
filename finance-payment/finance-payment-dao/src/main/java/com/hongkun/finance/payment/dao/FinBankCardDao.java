package com.hongkun.finance.payment.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinBankCardDao.java
 * @Class Name : FinBankCardDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinBankCardDao extends MyBatisBaseDao<FinBankCard, java.lang.Long> {
	/***
	 * @Description : 根据用户ID查询对应的银行卡信息
	 * @Method_Name : findByRegUserId&#13;
	 * @param regUserId
	 *            用户ID
	 * @return FinBankCard
	 * @return : FinBankCard&#13;
	 * @Creation Date : 2017年5月31日 上午11:20:11 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public List<FinBankCard> findByRegUserId(Integer regUserId);

	/**
	 * 
	 * @Description : 卡号查询
	 * @Method_Name : findByCradNo
	 * @param CradNo
	 *            用户银行卡号
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : FinBankCard
	 * @Creation Date : 2017年9月29日 下午7:00:03
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public FinBankCard findByCradNo(String cradNo, Integer regUserId);

	/**
	 * @Description :组合查询银行卡所有信息
	 * @Method_Name : findBankCardInfo;
	 * @param bankCardVo
	 * @return
	 * @return : List<BankCardVo>;
	 * @Creation Date : 2017年11月17日 下午5:27:47;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public List<BankCardVo> findBankCardInfo(BankCardVo bankCardVo);

	/**
	 *  @Description    ：根据用户id集合获取用户银行卡信息
	 *  @Method_Name    ：findBankCardInfoListByUserIds
	 *  @param payeeIdSet 用户id集合
	 *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.payment.model.FinBankCard>
	 *  @Creation Date  ：2018/4/18
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    Map<Integer,FinBankCard> findBankCardInfoListByUserIds(Set<Integer> payeeIdSet);

	/**
	 *  @Description    ：解绑银行卡更新(state=4&tel="")
	 *  @Method_Name    ：updateForUnBinding
	 *  @param bankCard
	 *  @return int
	 *  @Creation Date  ：2018年05月22日 13:48
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    int updateForUnBinding(FinBankCard bankCard);
}
