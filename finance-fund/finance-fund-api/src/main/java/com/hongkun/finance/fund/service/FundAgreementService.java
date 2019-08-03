package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.FundUserInfo;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundAgreementService.java
 * @Class Name    : FundAgreementService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundAgreementService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundAgreement 持久化的数据对象
	 * @return				: void
	 */
	int insertFundAgreement(FundAgreement fundAgreement);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundAgreement> 批量插入的数据
	 * @return				: void
	 */
	void insertFundAgreementBatch(List<FundAgreement> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundAgreement> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundAgreementBatch(List<FundAgreement> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundAgreement 要更新的数据
	 * @return				: void
	 */
	void updateFundAgreement(FundAgreement fundAgreement);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundAgreement 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundAgreementBatch(List<FundAgreement> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundAgreement
	 */
	FundAgreement findFundAgreementById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAgreement 检索条件
	 * @return	List<FundAgreement>
	 */
	List<FundAgreement> findFundAgreementList(FundAgreement fundAgreement);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAgreement 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundAgreement>
	 */
	List<FundAgreement> findFundAgreementList(FundAgreement fundAgreement, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAgreement 检索条件
	 * @param pager	分页数据
	 * @return	List<FundAgreement>
	 */
	Pager findFundAgreementList(FundAgreement fundAgreement, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundAgreement 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundAgreementCount(FundAgreement fundAgreement);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundAgreementList(FundAgreement fundAgreement, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findFundAgreementCount(FundAgreement fundAgreement, String sqlName);

	/**
	 *  @Description    ：条件获取海外基金预约详情
	 *  @Method_Name    ：findFundAgreementInfo
	 *  @param fundAgreement
	 *  @return com.hongkun.finance.fund.model.FundAgreement
	 *  @Creation Date  ：2018年07月04日 09:46
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    FundAgreement findFundAgreementInfo(FundAgreement fundAgreement);

	/**
	 *  @Description    ：保存或更新股权个人信息
	 *  @Method_Name    ：insertOrUpdateAdvisoryAndFundUserInfo
	 *  @param fundUserInfo
	 *  @param agreement
	 *  @param userDetail
	 *  @return int
	 *  @Creation Date  ：2018年07月05日 13:36
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    int insertOrUpdateAdvisoryAndFundUserInfo(FundUserInfo fundUserInfo, FundAgreement agreement, RegUserDetail userDetail);
}
