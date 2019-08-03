package com.hongkun.finance.roster.facade.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.roster.constants.ExcelType;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.facade.RosterPoiFacade;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.model.ResponseEntity;

@Service
public class RosterPoiFacadeImpl implements RosterPoiFacade {
	private static final Logger logger = LoggerFactory.getLogger(RosterPoiFacadeImpl.class);
	
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private RegCompanyInfoService regCompanyInfoService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	
	@Override
	public Workbook downloadTemplate(ExcelType excelType) {
		Workbook workbook = new HSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet(excelType.getName());
		Row descRow = sheet.createRow(0);
		descRow.createCell(0).setCellValue(createHelper.createRichTextString("PS: [*]开头为必填项"));
		Row row = sheet.createRow(1);
		switch (excelType) {
		case ROSTER_STAFF:
			this.createRosterStaffCell(row, createHelper);
			break;
		case ROSTER_DEPOSIT:
			this.createRosterDepositCell(row, createHelper);
			break;
		default:
			this.createRosterCell(row, createHelper);
			break;
		}
		return workbook;
	}
	
	@Override
	public ResponseEntity<?> uploadRoster(ExcelType excelType) {
		try (
				FileInputStream fis = new FileInputStream("C:/yrtz/test/poi/roster_tempalte.xls");
				Workbook wb = new HSSFWorkbook(fis);
				){
			int sheets = wb.getNumberOfSheets();
			for(int i = 0; i < sheets; i++){
				Sheet sheet = wb.getSheetAt(i);
				switch (excelType) {
				case ROSTER_STAFF:
					this.parseRowForSaveRosterStaff(sheet);
					break;
				case ROSTER_DEPOSIT:
					this.parseRowForSaveRosterDeposit(sheet);
					break;
				default:
					this.paserRowForSaveRosInfo(sheet);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 *  @Description    : 创建黑白名单描述
	 *  @Method_Name    : createRosterCell
	 *  @param row
	 *  @param createHelper
	 *  @return         : void
	 *  @Creation Date  : 2017年7月14日 下午2:53:04 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void createRosterCell(Row row, CreationHelper createHelper) {
		int i = 0;
		row.createCell(i++).setCellValue("*手机号");
		row.createCell(i++).setCellValue(createHelper
				.createRichTextString("*功能类型(0：积分增值，1：积分转金额，2：金额转积分，3：积分投资，4：债权转让，5：新手标投资，6：钱袋子转入转出，7：钱袋子推荐奖)"));
		row.createCell(i).setCellValue(createHelper.createRichTextString("*名单类型(0：黑名单，1：白名单)"));
	}
	
	/**
	 *  @Description    : 创建员工关系模板描述
	 *  @Method_Name    : createRosterStaffCell
	 *  @param row
	 *  @param createHelper
	 *  @return         : void
	 *  @Creation Date  : 2017年7月14日 下午2:53:30 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void createRosterStaffCell(Row row, CreationHelper createHelper) {
		int i = 0;
		row.createCell(i++).setCellValue("*手机号");
		row.createCell(i++).setCellValue("企业手机号");
		row.createCell(i).setCellValue(createHelper.createRichTextString("*员工类型(类型 1：物业，2：销售员工，3：内部员工，4：物业员工)"));
	}
	
	/**
	 *  @Description    : 创建意向金模板描述
	 *  @Method_Name    : createRosterDepositCell
	 *  @param row
	 *  @param createHelper
	 *  @return         : void
	 *  @Creation Date  : 2017年7月14日 下午2:53:51 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private void createRosterDepositCell(Row row, CreationHelper createHelper) {
		int i = 0;
		row.createCell(i++).setCellValue("*手机号");
		row.createCell(i++).setCellValue("*标的名称(唯一)");
		row.createCell(i++).setCellValue("*意向金(100整数倍)");
		row.createCell(i).setCellValue(createHelper.createRichTextString("*类型(1:购房宝，2：物业宝)"));
	}
	
	/**
	 *  @Description    : 解析并保存黑白名单信息
	 *  @Method_Name    : paserRowForRoster
	 *  @param sheet
	 *  @return         : String
	 *  @Creation Date  : 2017年7月14日 下午3:57:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private String paserRowForSaveRosInfo(Sheet sheet){
		List<RosInfo> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int rows = sheet.getPhysicalNumberOfRows();
		for(int j = 2; j < rows; j++){
			Row row = sheet.getRow(j);
			int i = 0;
			String tel = row.getCell(i++).getStringCellValue();
			String type = row.getCell(i++).getStringCellValue();
			String flag = row.getCell(i).getStringCellValue();
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(tel));
			if(regUser == null || regUser.getId() <= 0){
				sb.append(tel).append(",");
				continue;
			}
			RosInfo rosInfo = new RosInfo();
			rosInfo.setType(Integer.parseInt(type));
			rosInfo.setFlag(Integer.parseInt(flag));
			rosInfo.setRegUserId(regUser.getId());
			list.add(rosInfo);
		}
		this.rosInfoService.insertRosInfoBatch(list);
		logger.info("未持久化的名单列表", sb);
		return sb.toString().length() > 0 ? sb.toString().substring(0, sb.length() - 1) : sb.toString();
	}
	
	/**
	 *  @Description    : 解析并持久化员工关系表
	 *  @Method_Name    : parseRowForSaveRosterStaff
	 *  @param sheet
	 *  @return         : String
	 *  @Creation Date  : 2017年7月14日 下午4:46:09 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private String parseRowForSaveRosterStaff(Sheet sheet){
		List<RosStaffInfo> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int rows = sheet.getPhysicalNumberOfRows();
		for(int j = 2; j < rows; j++){
			Row row = sheet.getRow(j);
			RosStaffInfo rosStaffInfo = new RosStaffInfo();
			int i = 0;
			String tel = row.getCell(i++).getStringCellValue();
			String enterpriseTle = row.getCell(i++).getStringCellValue();
			String type = row.getCell(i).getStringCellValue();
			//判断用户是否存在
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(tel));
			if(regUser == null || regUser.getId() <= 0){
				sb.append(tel).append(",");
				continue;
			}
			rosStaffInfo.setRegUserId(regUser.getId());
			rosStaffInfo.setLogin(regUser.getLogin());
			if(Integer.parseInt(type) == RosterConstants.ROSTER_STAFF_TYPE_PROPERTY){
				//判断企业用户是否存在
				RegUser enterpriseRegUser = this.regUserService.findRegUserByLogin(Long.parseLong(enterpriseTle));
				if(enterpriseRegUser == null || enterpriseRegUser.getId() <= 0){
					sb.append(tel).append(",").append(enterpriseTle).append(",");
					continue;
				}
				//获得企业详细信息
				RegCompanyInfo regCompanyInfo = this.regCompanyInfoService.findRegCompanyInfoByRegUserId(enterpriseRegUser.getId());
				rosStaffInfo.setEnterpriseRegUserId(enterpriseRegUser.getId());
				rosStaffInfo.setEnterpriseRealName(regCompanyInfo.getEnterpriseName());
			}
			rosStaffInfo.setType(Integer.parseInt(type));
			list.add(rosStaffInfo);
		}
		logger.info("未持久化的名单列表", sb);
		this.rosStaffInfoService.insertRosStaffInfoBatch(list);
		return sb.toString().length() > 0 ? sb.toString().substring(0, sb.length() - 1) : sb.toString();
	}
	
	/**
	 *  @Description    : 解析并持久意向金数据
	 *  @Method_Name    : parseRowForSaveRosterDeposit
	 *  @param sheet
	 *  @return         : String
	 *  @Creation Date  : 2017年7月14日 下午5:01:47 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private String parseRowForSaveRosterDeposit(Sheet sheet){
		List<RosDepositInfo> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int rows = sheet.getPhysicalNumberOfRows();
		for(int j = 2; j < rows; j++){
			Row row = sheet.getRow(j);
			RosDepositInfo rosDepositInfo = new RosDepositInfo();
			int i = 0;
			String tel = row.getCell(i++).getStringCellValue();
			String bidName = row.getCell(i++).getStringCellValue();
			String money = row.getCell(i++).getStringCellValue();
			String type = row.getCell(i).getStringCellValue();
			//判断用户是否存在
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(tel));
			if(regUser == null || regUser.getId() <= 0){
				sb.append(tel).append(",");
				continue;
			}
			BidInfo bidInfoCdt = new BidInfo();
			bidInfoCdt.setName(bidName);
			List<BidInfo> bidInfoList = this.bidInfoService.findBidInfoList(bidInfoCdt);
			if(bidInfoList == null || bidInfoList.size() != 1){
				sb.append(tel).append(",").append(bidName).append(",");
				continue;
			}
			rosDepositInfo.setRegUserId(regUser.getId());
			rosDepositInfo.setBidId(bidInfoList.get(0).getId());
			rosDepositInfo.setMoney(BigDecimal.valueOf(Long.parseLong(money)));
			rosDepositInfo.setType(Integer.parseInt(type));
			list.add(rosDepositInfo);
		}
		logger.info("未持久化的意向金列表列表", sb);
		this.rosDepositInfoService.insertRosDepositInfoBatch(list);
		return sb.toString().length() > 0 ? sb.toString().substring(0, sb.length() - 1) : sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
		RosterPoiFacadeImpl r = new RosterPoiFacadeImpl();
		Workbook wb = r.downloadTemplate(ExcelType.ROSTER_DEPOSIT);
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("C:/yrtz/test/poi/roster_tempalte.xls");
		wb.write(fileOut);
		fileOut.close();
		wb.close();
		logger.info("okokok...");
	}

}
