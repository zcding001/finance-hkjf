package com.hongkun.finance.contract.facade;

import com.hongkun.finance.contract.model.InvestPreServeTemplate;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.ConInfoService.java
 * @Class Name    : ConInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface InvestPreserveFacade {
	/**
     * @Described			: 投资数据保全处理
     * @param template 检索条件
     * @date 2019-01-24 18:20:37
     * @author binliang@hongkun.com.cn 梁彬
     * @return	
     */
	public void sendToAncunInvestData(InvestPreServeTemplate template);
}
