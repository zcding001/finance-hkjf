package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundProject;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundAdvisoryService.java
 * @Class Name    : FundAdvisoryService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundAdvisoryService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundAdvisory 持久化的数据对象
	 * @return				: void
	 */
	void insertFundAdvisory(FundAdvisory fundAdvisory);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundAdvisory> 批量插入的数据
	 * @return				: void
	 */
	void insertFundAdvisoryBatch(List<FundAdvisory> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundAdvisory> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundAdvisoryBatch(List<FundAdvisory> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundAdvisory 要更新的数据
	 * @return				: void
	 */
	void updateFundAdvisory(FundAdvisory fundAdvisory);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundAdvisory 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundAdvisoryBatch(List<FundAdvisory> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundAdvisory
	 */
	FundAdvisory findFundAdvisoryById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAdvisory 检索条件
	 * @return	List<FundAdvisory>
	 */
	List<FundAdvisory> findFundAdvisoryList(FundAdvisory fundAdvisory);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAdvisory 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundAdvisory>
	 */
	List<FundAdvisory> findFundAdvisoryList(FundAdvisory fundAdvisory, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundAdvisory 检索条件
	 * @param pager	分页数据
	 * @return	List<FundAdvisory>
	 */
	Pager findFundAdvisoryList(FundAdvisory fundAdvisory, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundAdvisory 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundAdvisoryCount(FundAdvisory fundAdvisory);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundAdvisoryList(FundAdvisory fundAdvisory, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findFundAdvisoryCount(FundAdvisory fundAdvisory, String sqlName);

	/**
	 *  @Description    ：条件检索股权类型信息
	 *  @Method_Name    ：findFundTypeListWithCondition
	 *  @param project
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年04月28日 09:53
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Pager findFundTypeListWithCondition(FundProject project, Pager pager);

	/**
	 *  @Description    ：条件查询股权咨询信息
	 *  @Method_Name    ：findFundAdvisoryListWithCondition
	 *  @param advisoryVo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年04月28日 14:14
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Pager findFundAdvisoryListWithCondition(FundAdvisoryVo advisoryVo, Pager pager);

	/**
	 *  @Description    ：咨询信息分配给销售
	 *  @Method_Name    ：updateFundAdvisoryForSale
	 *  @param advisory
	 *  @return int
	 *  @Creation Date  ：2018年05月05日 16:16
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	int updateFundAdvisoryForSale(FundAdvisory advisory);

	/**
	 *  @Description    ：查询用户当日预约次数
	 *  @Method_Name    ：findAdvisoryCount
	 *  @param id
	 *  @param parentType
	 *  @return int
	 *  @Creation Date  ：2018年05月07日 16:09
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	int findAdvisoryCount(Integer id,Integer parentType);


	/**
	 *  @Description    ：根据项目类型查询父类型
	 *  @Method_Name    ：findProjectParentTypeByType
	 *  @param projectId
	 *  @return int
	 *  @Creation Date  ：2018年05月07日 16:09
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Integer findProjectParentTypeByType(Integer projectId);

	/**
	 *  @Description    ：获取股权类型详情
	 *  @Method_Name    ：findFundProjectInfo
	 *  @param id
	 *  @return com.hongkun.finance.fund.model.FundProject
	 *  @Creation Date  ：2018年05月11日 09:56
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    FundProject findFundProjectInfo(Integer id);

	/**
	 *  @Description    ：获取股权预约详情
	 *  @Method_Name    ：findFundAdvisoryInfo
	 *  @param id
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018年08月03日 16:52
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity findFundAdvisoryInfo(Integer id);
	/**
	*  @Description    ：查询某个用户对某个项目是否预约过
	*  @Method_Name    ：findFundAdvisoryCount
	*  @param infoId
	*  @param projectParentType
	 * @param  regUserId
	*  @return int
	*  @Creation Date  ：2018/10/10
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	int findFundAdvisoryCount(Integer regUserId,Integer infoId,Integer projectParentType);
}
