package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegCompanyInfo;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegCompanyInfoService.java
 * @Class Name    : RegCompanyInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegCompanyInfoService {
	
	/**
	*  @Description    ：更新用户注册公司信息
	*  @Method_Name    ：updateRegCompanyInfo
	*  @param regCompanyInfo
	*  @return void
	*  @Creation Date  ：2018/4/17
	*  @Author         ：zhichaoding@hongkun.com.cn
	*/
	void updateRegCompanyInfo(RegCompanyInfo regCompanyInfo);
	
	/**
	 *  @Description    : 通过用户id检索企业账户信息
	 *  @Method_Name    : findRegCompanyInfoByRegUserId
	 *  @param regUserId
	 *  @return         : RegCompanyInfo
	 *  @Creation Date  : 2017年7月14日 下午4:33:02 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	RegCompanyInfo findRegCompanyInfoByRegUserId(int regUserId);

	/** 
	* @Description: 通过法人手机号检索账户信息
	* @Param: [legalTel]
	* @return: com.hongkun.finance.user.model.RegCompanyInfo 
	* @Author: HeHang
	* @Date: 2018/9/12 
	*/
	List<RegCompanyInfo> findRegCompanyInfoByLegalTel(String legalTel);
}
