package com.hongkun.finance.payment.service;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.FinBankCardService.java
 * @Class Name : FinBankCardService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface FinBankCardService {

	/**
	 * @Described : 单条插入
	 * @param finBankCard
	 *            持久化的数据对象
	 * @return : void
	 */
	int insert(FinBankCard finBankCard);

	/**
	 * @Described : 批量插入
	 * @param List<FinBankCard>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBatch(List<FinBankCard> list);

	/**
	 * @Described : 更新数据
	 * @param finBankCard
	 *            要更新的数据
	 * @return : void
	 */
	int update(FinBankCard finBankCard);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return FinBankCard
	 */
	FinBankCard findById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCard
	 *            检索条件
	 * @return List<FinBankCard>
	 */
	List<FinBankCard> findByCondition(FinBankCard finBankCard);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCard
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<FinBankCard>
	 */
	List<FinBankCard> findByCondition(FinBankCard finBankCard, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param finBankCard
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<FinBankCard>
	 */
	Pager findByCondition(FinBankCard finBankCard, Pager pager);

	/**
	 * 
	 * @Description : 根据用户ID获取银行卡信息
	 * @Method_Name : findByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : List<FinBankCard>;
	 * @Creation Date : 2017年6月9日 下午2:03:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public List<FinBankCard> findByRegUserId(Integer regUserId);

	/**
	 * @Description :查询某支付渠道下的用户银行卡信息
	 * @Method_Name : findBankCardInfo;
	 * @param regUserId
	 * @param finBankCardId
	 * @param payChannelEnum
	 * @return
	 * @return : BankCardVo;
	 * @Creation Date : 2017年12月7日 下午4:52:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public BankCardVo findBankCardInfo(Integer regUserId, Integer finBankCardId, PayChannelEnum payChannelEnum);

	/**
	 * @Description : 组合查询用户的银行卡信息
	 * @Method_Name : findBankCardInfoList;
	 * @param bankCardVo
	 * @return
	 * @return : List<BankCardVo>;
	 * @Creation Date : 2017年12月7日 下午4:55:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public List<BankCardVo> findBankCardInfoList(BankCardVo bankCardVo);

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
	 *  @Creation Date  ：2018年05月22日 13:45
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	int updateForUnBinding(FinBankCard bankCard);
}
