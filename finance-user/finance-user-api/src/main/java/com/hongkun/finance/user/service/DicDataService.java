package com.hongkun.finance.user.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.DicData;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.DicDataService.java
 * @Class Name    : DicDataService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface DicDataService {
	
	
	/**
	 * @Described			: 条件检索数据
	 * @param dicData 检索条件
	 * @return	List<DicData>
	 */
	List<DicData> findDicDataList(DicData dicData);
	
	List<DicData> findDicDataList();
	/**
	 *  @Description    : xuhui.liu 加载数据字典到缓存中（redis）
	 *  @Method_Name    : initCache
	 *  @return         : void
	 *  @Creation Date  : 2017年5月26日 下午2:39:51 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	void initCache();
	/**
	 *  @Description    : 根据businessName，subjectName，value获取vaule对应的描述
	 *  @Method_Name    : findNameByValue
	 *  @param businessName
	 *  @param subjectName
	 *  @param value
	 *  @return
	 *  @return         : DicData
	 *  @Creation Date  : 2017年6月13日 上午9:44:51 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	String findNameByValue(String businessName,String subjectName,int value);
	/**
	 *  @Description    : 获取某一功能的字典列表
	 *  @Method_Name    : findDicDataBySubjectName
	 *  @param businessName
	 *  @param subjectName
	 *  @return
	 *  @return         : List<DicData>
	 *  @Creation Date  : 2017年6月13日 上午9:45:48 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	List<DicData> findDicDataBySubjectName(String businessName,String subjectName);
	/**
	 *  @Description    : 通过businessName刷新缓存
	 *  @Method_Name    : refreshDicData
	 *  @param businessName
	 *  @return         : void
	 *  @Creation Date  : 2017年7月20日 下午4:02:28 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	void refreshDicData(String businessName);

	/**
	*  @Description    ：通过value获取数据字典名称
	*  @Method_Name    ：findNamesByValues
	*  @param businessName
	*  @param subjectName
	*  @param values    数据字典value集合，默认用逗号分隔
	*  @param splitStr  分隔符 默认是逗号
	*  @return java.lang.String
	*  @Creation Date  ：2018/11/13
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	String findNamesByValues(String businessName,String subjectName,String values,String splitStr);
	
}
