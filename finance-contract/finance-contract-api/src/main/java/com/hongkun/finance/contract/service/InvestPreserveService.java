package com.hongkun.finance.contract.service;

import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.ConInfoService.java
 * @Class Name    : ConInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface InvestPreserveService {
	/**
     * @Described			: 投资数据保全处理
     * @param template 检索条件
     * @date 2019-01-24 18:20:37
     * @author binliang@hongkun.com.cn 梁彬
     * @return	
     */
	public ResponseEntity<?> sendToAncunInvestData(InvestPreServeTemplate template);
}
