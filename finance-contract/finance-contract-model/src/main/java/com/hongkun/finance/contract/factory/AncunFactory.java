package com.hongkun.finance.contract.factory;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ancun.aosp.client.AospClient;
import com.ancun.aosp.dto.AospRequest;
import com.ancun.aosp.dto.AospResponse;
import com.ancun.aosp.dto.ClientUser;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.contract.util.AospClientUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;

public class AncunFactory {
	private static final Logger logger = LoggerFactory.getLogger(AncunFactory.class);
	
	/** 投资的业务编号 **/
	private static final String itemKey = "I-0126001";
	
	public ResponseEntity<?> sendToAncun(InvestPreServeTemplate templet){
		logger.info("方法：sendToAncun,params templet：{}",templet);
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		//初始化保全数据连接
		AospClient aospClient=AospClientUtil.getAospClient();
		//组装保全请求数据
		AospRequest aospRequest = new AospRequest();
		aospRequest.setItemKey(itemKey);//业务编号，平台分配
		aospRequest.setFlowNo(templet.getFlowNo());//流程号,平台分配
		if(StringUtils.isNotBlank(templet.getRecordNo())){
			aospRequest.setRecordNo(templet.getRecordNo());
		}
		String dateType = ContractConstants.DATETYPE;
		if("3".equals(dateType)){
			aospRequest.setDataType(3);//数据类型，1正式 3测试 默认1正式，这行代码仅在测试的时候写上
		}
		aospRequest.setClientDataId(templet.getInvestCode());//传入数据在接入者系统中的标识，同一个itemKey下必须是唯一标识，该数据将来可以作为对账用
		/**业务数据**/
		//投资人信息
		if(StringUtils.isNotBlank(templet.getInvestUserName())){
			aospRequest.addField("investUserName", templet.getInvestUserName());
		}
		if(StringUtils.isNotBlank(templet.getInvestPhoneId())){
			aospRequest.addField("investPhoneId", templet.getInvestPhoneId());
		}
		if(StringUtils.isNotBlank(templet.getInvestIdCard())){
			aospRequest.addField("investIdCard", templet.getInvestIdCard());
		}
		if(StringUtils.isNotBlank(templet.getInvestRegTime())){
			aospRequest.addField("investRegTime", templet.getInvestRegTime());
		}
		if(templet.getInvestAuthTime()!=null){
			aospRequest.addField("investAuthTime", templet.getInvestAuthTime());
		}
		
		//项目信息
		if(StringUtils.isNotBlank(templet.getBiddTitle())){
			aospRequest.addField("loanNo", templet.getBiddTitle());
		}
		if(StringUtils.isNotBlank(templet.getLoanRate())){
			aospRequest.addField("loanRate", templet.getLoanRate());
		}
		if(templet.getLoanMoney()!=null){
			aospRequest.addField("loanMoney", templet.getLoanMoney());
		}
		if(StringUtils.isNotBlank(templet.getLoanDay())){
			aospRequest.addField("loanDay", templet.getLoanDay());
		}
		if(StringUtils.isNotBlank(templet.getLoanRepayType())){
			aospRequest.addField("loanRepayType", templet.getLoanRepayType());
		}
		if(templet.getReleaseTime()!=null){
			aospRequest.addField("releaseTime", templet.getReleaseTime());
		}
		if(templet.getInterestTime()!=null){
			aospRequest.addField("interestTime", templet.getInterestTime());
		}
		if(templet.getRaiseEndTime()!=null){
			aospRequest.addField("raiseEndTime", templet.getRaiseEndTime());
		}
		if(templet.getSmallBeginMoney()!=null){
			aospRequest.addField("smallBeginMoney", templet.getSmallBeginMoney());
		}
		if(StringUtils.isNotBlank(templet.getEnsureCompany())){
			aospRequest.addField("ensureCompany", templet.getEnsureCompany());
		}
		
		//借款人信息
		if(StringUtils.isNotBlank(templet.getLoanUserName())){
			aospRequest.addField("loanUserName", templet.getLoanUserName());
		}
		if(StringUtils.isNotBlank(templet.getLoanIdCard())){
			aospRequest.addField("loanIdCard", templet.getLoanIdCard());
		}
		if(StringUtils.isNotBlank(templet.getLoanPhoneId())){
			aospRequest.addField("loanPhoneId", templet.getLoanPhoneId());
		}
		if(StringUtils.isNotBlank(templet.getLoanUserAddress())){
			aospRequest.addField("loanUserAddress", templet.getLoanUserAddress());
		}
		if(StringUtils.isNotBlank(templet.getLoanMortgage())){
			aospRequest.addField("loanMortgage", templet.getLoanMortgage());
		}
		
		//投资信息
		if(StringUtils.isNotBlank(templet.getInvestUserId())){
			aospRequest.addField("investUserId", templet.getInvestUserId());
		}
		if(templet.getInvestMoeny()!=null){
			aospRequest.addField("investMoeny", templet.getInvestMoeny());
		}
		if(templet.getRedDeduction()!=null){
			aospRequest.addField("redDeduction", templet.getRedDeduction());
		}
		if(templet.getInvestTime()!=null){
			aospRequest.addField("investTime", templet.getInvestTime());
		}
		if(templet.getInvestPaymentTime()!=null){
			aospRequest.addField("investPaymentTime", templet.getInvestPaymentTime());
		}
		//实际回款清单（追加数据）
		if(StringUtils.isNotBlank(templet.getRepayId())){
			aospRequest.addField("repayId", templet.getRepayId());
		}
		if(templet.getRepayTime()!=null){
			aospRequest.addField("repayTime", templet.getRepayTime());
		}
		if(StringUtils.isNotBlank(templet.getRepayMoney())){
			aospRequest.addField("repayMoney", templet.getRepayMoney());
		}
		
		//附件合同追加
		if(StringUtils.isNotBlank(templet.getAttachmentPath())){
			aospRequest.addFileByUrl(templet.getAttachmentPath(),templet.getAttachDesc());
		}
		//以下是业务数据的用户信息
		ClientUser clientUser = new ClientUser();
		if(StringUtils.isNotBlank(templet.getInvestUserName())){
			clientUser.setTruename(templet.getInvestUserName());
		}
		if(StringUtils.isNotBlank(templet.getInvestIdCard())){
			clientUser.setIdcard(templet.getInvestIdCard());
		}
		if(StringUtils.isNotBlank(templet.getInvestPhoneId())){
			clientUser.setMobile(templet.getInvestPhoneId());
		}
		aospRequest.setClientUser(clientUser);
		
		logger.info("发送保全报文数据："+aospRequest.toString());
		
		AospResponse aospResponse = null;
		try {
			aospResponse = aospClient.save(aospRequest);
			logger.info("保全返回信息 ，aospResponse："+aospResponse);
		} catch (Exception e) {
			logger.error("发送保全数据失败, e:{}" , e);
			return new ResponseEntity<>(Constants.ERROR,"保全号为空");
		}
		
		//判断是否有保全号，如果没有保全号，则获取并更新
		if(StringUtils.isBlank(templet.getRecordNo())){
			Map<String, String> resultMap = aospResponse.getData();
			//返回的结果得到保全号
			String ancunNo = resultMap.get("recordNo");	
			if(StringUtils.isBlank(ancunNo)){
				logger.error("投资记录："+templet.getInvestCode()+"，获得的保全号为：【"+ancunNo+"】");
				return new ResponseEntity<>(Constants.ERROR,"保全号为空");
			}
		}
		
		
		return result;
	}
	
}
