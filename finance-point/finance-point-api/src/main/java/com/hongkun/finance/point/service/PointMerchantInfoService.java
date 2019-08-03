package com.hongkun.finance.point.service;

import java.util.List;
import java.util.Map;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointMerchantInfo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointMerchantInfoService.java
 * @Class Name    : PointMerchantInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointMerchantInfoService {

	
	/**
	 * @Described			: 更新数据
	 * @param pointMerchantInfo 要更新的数据
	 * @return				: void
	 */

	void updatePointMerchantInfo(PointMerchantInfo pointMerchantInfo);

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointMerchantInfo
	 */
	PointMerchantInfo findPointMerchantInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointMerchantInfo 检索条件
	 * @return	List<PointMerchantInfo>
	 */
	List<PointMerchantInfo> findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo);

	
	/**
	 * @Described			: 条件检索数据
	 * @param pointMerchantInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<PointMerchantInfo>
	 */
	Pager findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointMerchantInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findPointMerchantInfoCount(PointMerchantInfo pointMerchantInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findPointMerchantInfoList(PointMerchantInfo pointMerchantInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 */
	Integer findPointMerchantInfoCount(PointMerchantInfo pointMerchantInfo, String sqlName);

	/**
	 * 保存商户信息
	 * @param pointMerchantInfo
	 * @return
	 */
    ResponseEntity savePointMerchantInfo(PointMerchantInfo pointMerchantInfo);


	/**
	 * 审核商户信息
	 * @param pointMerchantInfo
	 * @return
	 */
	ResponseEntity checkPointMerchant(PointMerchantInfo pointMerchantInfo);

	/**
	 *  @Description    : 获取用户商户信息
	 *  @Method_Name    : getMerchantInfo
	 *  @return
	 *  @Creation Date  : 2018年03月15日 下午17:11:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	PointMerchantInfo getMerchantInfo(Integer regUserId);

	/**
	 * @param merchantCode 商户编号
	 * @return : Map<String,Object>
	 * @Description : 根据商户编号获取商户信息
	 * @Method_Name : getMerchantNameByCode
	 * @Creation Date  : 2018年03月15日 下午17:11:55
	 * @Author : pengwu@hongkun.com.cn
	 */
	PointMerchantInfo getMerchantInfoByCode(String merchantCode);

	/**
	 * @param regUserId    用户id
	 * @return : Map<String,Object>
	 * @Description : 获取商户状态为1-待审核，3-审核成功的商户信息
	 * @Method_Name : getPointMerchantInfoByUserId
	 * @Creation Date  : 2018年03月16日 上午09:41:55
	 * @Author : pengwu@hongkun.com.cn
	 */
	PointMerchantInfo getPointMerchantInfoByUserId(Integer regUserId);

	/**
	 * @param regUserId    用户id
	 * @return : Map<String,Object>
	 * @Description : 获取用户待审核商户信息
	 * @Method_Name : getCheckMerchantInfo
	 * @Creation Date  : 2018年03月16日 上午09:41:55
	 * @Author : pengwu@hongkun.com.cn
	 */
    PointMerchantInfo getCheckMerchantInfo(Integer regUserId);

	/**
	 * @return : PointMerchantInfo
	 * @Description : 获取用户最近审核失败商户信息
	 * @Method_Name : getRecentFailMerchantInfo
	 * @Creation Date  : 2018年03月20日 下午16:11:55
	 * @Author : pengwu@hongkun.com.cn
	 */
    PointMerchantInfo getRecentFailMerchantInfo(Integer regUserId);

	/**
	 * @param regUserId    用户id
	 * @return : Map<String,Object> {
	 *     state:状态：1待审核，3审核成功，4未申请
	 * }
	 * @Description : 获取用户商户状态
	 * @Method_Name : getMerchantInfoState
	 * @Creation Date  : 2018年03月16日 上午09:41:55
	 * @Author : pengwu@hongkun.com.cn
	 */
	Map<String,Object> getMerchantInfoState(Integer regUserId);

	/**
	 * @Described			: 根据条件查询商户id集合
	 * @param pointMerchantInfo 检索条件
	 * @return	List<PointMerchantInfo>
	 */
	List<Integer> findMerchantIdsByInfo(PointMerchantInfo pointMerchantInfo);
}
