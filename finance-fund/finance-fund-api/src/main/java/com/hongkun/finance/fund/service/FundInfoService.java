package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundInfoService.java
 * @Class Name    : FundInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertFundInfo(FundInfo fundInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertFundInfoBatch(List<FundInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundInfoBatch(List<FundInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundInfo 要更新的数据
	 * @return				: void
	 */
	void updateFundInfo(FundInfo fundInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundInfoBatch(List<FundInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundInfo
	 */
	FundInfo findFundInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundInfo 检索条件
	 * @return	List<FundInfo>
	 */
	List<FundInfo> findFundInfoList(FundInfo fundInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundInfo>
	 */
	List<FundInfo> findFundInfoList(FundInfo fundInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<FundInfo>
	 */
	Pager findFundInfoList(FundInfo fundInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundInfoCount(FundInfo fundInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundInfoList(FundInfo fundInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findFundInfoCount(FundInfo fundInfo, String sqlName);

	/**
	 *  @Description    ：根据股权项目的id查询股权名称
	 *  @Method_Name    ：findFundInfoByIds
	 *  @param ids
	 *  @return java.util.List<java.lang.String>
	 *  @Creation Date  ：2018年04月28日 17:23
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
	 * @Described			: 条件检索数据
	 * @param fundInfo 检索条件
	 * @return	FundInfo
	 */
	FundInfoVo getFundInfo(FundInfoVo fundInfoVo);
    /***
     * 
     *  @Description    : 获取首页股权信息
     *  @Method_Name    : findFundInfos;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年5月7日 下午2:29:22;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
	List<FundInfoVo> findIndexFundInfos();

}
