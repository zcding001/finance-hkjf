package com.hongkun.finance.roster.facade;

import org.apache.poi.ss.usermodel.Workbook;

import com.hongkun.finance.roster.constants.ExcelType;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description   : 黑名单导入导出功能
 * @Project       : finance-roster-api
 * @Program Name  : com.hongkun.finance.roster.facade.RosterPoiService.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public interface RosterPoiFacade {

	/**
	 *  @Description    : 下载excel模板
	 *  @Method_Name    : downloadTemplate
	 *  @param type	模板类型	1：
	 *  @return         : void
	 *  @Creation Date  : 2017年7月14日 下午1:53:03 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public Workbook downloadTemplate(ExcelType excelType);
	
	/**
	 *  @Description    : 上传exel数据
	 *  @Method_Name    : uploadRoster
	 *  @param excelType
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月18日 上午8:51:23 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public ResponseEntity<?> uploadRoster(ExcelType excelType);
}
