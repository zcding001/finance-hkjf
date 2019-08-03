package com.hongkun.finance.contract.service;

import com.hongkun.finance.contract.model.ConInfo;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.ConInfoService.java
 * @Class Name    : ConInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface ConInfoService {
	
	/**
	 * @Described			: 条件检索数据
	 * @param conInfo 检索条件
	 * @return	List<ConInfo>
	 */
	List<ConInfo> findConInfoList(ConInfo conInfo);

	/**
	 * @Described			: 单条插入
	 * @param conInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertConInfo(ConInfo conInfo);

	/**
	 * @Described			: 批量插入
	 * @param list 批量插入的数据
	 * @return				: void
	 */
	void insertConInfoBatch(List<ConInfo> list);

	/**
	 *  @Description    ：批量更新合同信息
	 *  @Method_Name    ：updateConInfoBatch
	 *  @param list      合同信息集合
	 *  @param count     合同信息集合数量
	 *  @Creation Date  ：2018/7/3
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	void updateConInfoBatch(List<ConInfo> list,int count);

	/**
	 *  @Description    ：根据条件获取单条合同信息
	 *  @Method_Name    ：findConInfo
	 *  @param conInfo  查询条件
	 *  @return java.util.List<com.hongkun.finance.contract.model.ConInfo>
	 *  @Creation Date  ：2018/4/17
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ConInfo findConInfo(ConInfo conInfo);

	/**
	 *  @Description    ：根据投资记录集合获取合同信息集合
	 *  @Method_Name    ：findConInfoListByInvestIds
	 *  @param listInvestIds 投资记录集合
	 *  @return java.util.List<com.hongkun.finance.contract.model.ConInfo>
	 *  @Creation Date  ：2018/6/1
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    List<ConInfo> findConInfoListByInvestIds(List<Integer> listInvestIds);

	/**
	*  @Description    ：初始化合同信息
	*  @Method_Name    ：initContracts
	*  @param contract
	*  @param fromPlace
	*  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	*  @Creation Date  ：2018/7/4
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	List<Map<String,Object>> initContracts(String contract, Integer fromPlace);
}
