package com.hongkun.finance.contract.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.model.ConInfo;
import com.hongkun.finance.contract.model.ConTemplate;
import com.hongkun.finance.contract.model.ConType;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO;
import com.hongkun.finance.contract.service.ConInfoService;
import com.hongkun.finance.contract.service.ConTemplateService;
import com.hongkun.finance.contract.service.ConTypeService;
import com.hongkun.finance.contract.util.ContractUtils;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.contract.constants.ContractConstants.CONTRACT_TYPE_CREDITOR_TRANSFER;
import static com.hongkun.finance.contract.constants.ContractConstants.CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR;
import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * @Description : 合同信息facade层实现类
 * @Project : framework
 * @Program Name : com.hongkun.finance.contract.facade.impl.ConInfoFacadeImpl
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Service
public class ConInfoFacadeImpl implements ConInfoFacade {

	private final Logger logger = LoggerFactory.getLogger(ConInfoFacadeImpl.class);

	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private BidProductService bidProductService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private ConInfoService conInfoService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private ConTemplateService conTemplateService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserInfoService regUserInfoService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private ConTypeService conTypeService;
	@Reference
	private BidTransferAutoService bidTransferAutoService;
	@Reference
	private VasCouponDetailService couponDetailService;

	@Override
	public void generateContract(List<Integer> list){
		generateContractList(list, null);
	}

	@Override
	public void generateContract(List<Integer> list, Integer contractType){
		generateContractList(list, contractType);
	}

	private void generateContractList(List<Integer> investIdList, Integer contractType){
		logger.info("generateContractList, 生成合同信息, 投资记录id集合: {}, 合同类型: {}", JSON.toJSON(investIdList), contractType);
		List<ConInfo> conInfoList = new ArrayList<>();
		List<ConInfo> updateConInfoList = new ArrayList<>();
		try{
			//根据投资记录id集合获取投资记录集合、标地信息Map、投资人&借款人&本金接收人id集合、收款人id集合
			Map<String,Object> param = bidInvestService.findBidInvestAndBidInfoByIdList(investIdList);
			//根据投资记录id集合获取投资记录集合
			List<BidInvest> bidInvestList = (List<BidInvest>) param.get("bidInvestList");
			//投资记录对应标地信息Map:key为标地id，value为标地信息BidInfoVO
			Map<Integer, BidInfoVO> bidInfoVOMap = (Map<Integer, BidInfoVO>) param.get("bidInfoVOMap");
			//投资人&借款人&本金接收人id集合
			Set<Integer> userIdSet = (Set<Integer>) param.get("userIdSet");
			//收款人id集合
			Set<Integer> payeeIdSet = (Set<Integer>) param.get("payeeIdSet");
			//根据用户id集合获取用户信息，key为用户id，value为UserSimpleVo用户信息
			Map<Integer,UserSimpleVo> userSimpleVoMap = regUserService.findUserSimpleVoByIdList(userIdSet);
			//获取所有已启用的合同模板信息，key为合同类型type，value为ConTemplate合同模板信息
			Map<Integer,ConTemplate> conTemplateMap = conTemplateService.findEnableConTemplateList();
			//根据收款人id集合获取收款人银行卡信息，key为用户id，value为FinBankCard银行卡信息
			Map<Integer,FinBankCard> finBankCardMap = finBankCardService.findBankCardInfoListByUserIds(payeeIdSet);
			//获取投资记录使用的加息券或投资红包记录，key为卡券id，value为卡券的值
			Set<Integer> couponIds = new HashSet<>();
			bidInvestList.stream().forEach(bidInvest -> {
				if (bidInvest.getCouponIdK() != 0){
					couponIds.add(bidInvest.getCouponIdK());
				}
				if (bidInvest.getCouponIdJ() != 0){
					couponIds.add(bidInvest.getCouponIdJ());
				}
			});
			Map<Integer, VasCouponDetail> couponDetailMap = new HashMap<>();
			if (couponIds.size() > 0){
				couponDetailMap = couponDetailService.findVasCouponDetailByIds(couponIds);
			}
			// 组装合同信息
			for(BidInvest invest : bidInvestList){
				// 投资金额 = investAmount - transAmount
				BigDecimal investAmount = invest.getInvestAmount().subtract(invest.getTransAmount());

				BidInfoVO bidInfoVO = bidInfoVOMap.get(invest.getBidInfoId());
				List<Integer> contractList = new ArrayList<>();
				// 判断是否传递合同类型
				if (contractType == null) {
					// 获取合同信息
					String contract = bidInfoVO.getContract();
					contractList = Arrays.stream(contract.split(",")).map(s->Integer.parseInt(s.trim())).
							collect(Collectors.toList());
				} else {
					//债权转让协议-转让，合同信息中的投资金额即为已转让金额
					if (Objects.equals(contractType,CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR)){
						investAmount = invest.getTransAmount();
					}
					contractList.add(contractType);
				}
				// 投资记录金额<=0不作生成合同处理
				if (investAmount.compareTo(BigDecimal.ZERO) != 1){
					continue;
				}
				//获取投资人用户信息
				UserSimpleVo investUserInfo = userSimpleVoMap.get(invest.getRegUserId());
				//获取借款人用户信息
				UserSimpleVo borrowUserInfo = userSimpleVoMap.get(bidInfoVO.getBorrowerId());
				// 获取收款人账户信息
				FinBankCard finBankCard;
				// 收款人名称
				String payeeName;
				//若有本金接收人，收款人名称则为本金接收人，否则为借款人
				if (bidInfoVO.getReceiptUserId() != 0) {
					payeeName = userSimpleVoMap.get(bidInfoVO.getReceiptUserId()).getRealName();
					finBankCard = finBankCardMap.get(bidInfoVO.getReceiptUserId());
				} else {
					finBankCard = finBankCardMap.get(bidInfoVO.getBorrowerId());
					payeeName = borrowUserInfo.getRealName();
				}
				//获取投资红包和加息券的金额
				BigDecimal couponKValue = BigDecimal.ZERO;
				BigDecimal couponJValue = BigDecimal.ZERO;
				if (couponDetailMap.containsKey(invest.getCouponIdK())){
					couponKValue = couponDetailMap.get(invest.getCouponIdK()).getWorth();
				}
				if (couponDetailMap.containsKey(invest.getCouponIdJ())){
					couponJValue = couponDetailMap.get(invest.getCouponIdJ()).getWorth();
				}
				for (Integer type : contractList) {
					// 查询当前合同启用的合同模板
					ConTemplate conTemplate = conTemplateMap.get(type);
					// 该合同类型没有对应已启用的合同模板，则不保存合同信息
					if (conTemplate == null) {
						continue;
					}
					//判断该投资记录是否已存在合同信息
					ConInfo condition = new ConInfo();
					condition.setBidInvestId(invest.getId());
					condition.setContractType(type);
					condition.setState(ContractConstants.CONTRACT_STATE_Y);
					ConInfo investConInfo = conInfoService.findConInfo(condition);
					if (investConInfo != null){
						ConInfo updateConInfo = new ConInfo();
						updateConInfo.setId(investConInfo.getId());
						updateConInfo.setState(ContractConstants.CONTRACT_STATE_N);
						updateConInfo.setModifyTime(new Date());
						updateConInfoList.add(updateConInfo);
					}
					ConInfo conInfo = new ConInfo();
					conInfo.setContractCode(ContractUtils.createContractNo(String.valueOf(type)));// 合同编号/协议编号
					conInfo.setBidId(invest.getBidInfoId());// 标地id
					conInfo.setBidInvestId(invest.getId());// 投资记录id
					conInfo.setContractType(type);// 合同类型

					conInfo.setContractTemplateId(conTemplate.getId());// 合同模板id
					conInfo.setEffectiveDate(bidInfoVO.getLendingTime());// 合同生效日期
					// 根据标的期限单位:1-年,2-月,3-天，设置合同截止日期
					if (bidInfoVO.getTermUnit() == 3) {
						conInfo.setExpireDate(DateUtils.addDays(bidInfoVO.getLendingTime(), bidInfoVO.getTermValue()));
					} else if (bidInfoVO.getTermUnit() == 2) {
						conInfo.setExpireDate(DateUtils.addMonth(bidInfoVO.getLendingTime(), bidInfoVO.getTermValue()));
					} else if (bidInfoVO.getTermUnit() == 1) {
						conInfo.setExpireDate(DateUtils.addMonth(bidInfoVO.getLendingTime(), bidInfoVO.getTermValue() * 12));
					}
					conInfo.setInvestUserId(invest.getRegUserId());// 投资用户ID
					conInfo.setInvestName(invest.getRealName());// 投资人真实姓名
					conInfo.setInvestCardId(investUserInfo.getIdCard());// 投资人身份证号
					conInfo.setInvestAmount(investAmount);//投资金额
					conInfo.setInvestCouponKValue(couponKValue);//投资使用投资红包金额
					conInfo.setInvestCouponJValue(couponJValue);//投资使用加息券的值
					conInfo.setBidName(bidInfoVO.getTitle());// 标地名称
					conInfo.setBidRate(bidInfoVO.getInterestRate());// 标地利率
					conInfo.setBidAmount(bidInfoVO.getTotalAmount());// 标地总金额
					conInfo.setBidTermValue(bidInfoVO.getTermValue());// 标地期限值
					conInfo.setBidTerm(bidInfoVO.getTermUnit());// 期限单位:1-年,2-月,3-天
					conInfo.setBorrowName(borrowUserInfo.getRealName());// 借款人真实姓名
					conInfo.setBorrowCardId(borrowUserInfo.getIdCard());// 借款人身份证号
					conInfo.setBidLoanUse(bidInfoVO.getLoanUse());// 借款用途:1-短期周转,2-项目贷款,3-临时倒短,4-扩大生产
					conInfo.setBiddRepaymentWay(bidInfoVO.getBiddRepaymentWay());// 还款方式:1-等额本息,2-按月付息，到期还本,3-到期还本付息,4-到期付息，本金回收,5-每月付息，到期本金回收,
					// 6-按月付息，本金划归企业
					conInfo.setPayeeName(payeeName);// 收款人名称
					if (finBankCard != null){
						conInfo.setPayeeBankCard(finBankCard.getBankCard());// 收款人银行卡号
						conInfo.setPayeeBankName(finBankCard.getBankName());// 收款人银行名称
					}
					conInfo.setBidProjectNo(bidInfoVO.getBidCode());// 标地项目编号
					conInfo.setInvestTel(investUserInfo.getLogin());// 投资人电话
					conInfo.setInvestEmail(investUserInfo.getEmail());// 投资人邮箱
					conInfo.setBorrowAddress(borrowUserInfo.getContactAddress());// 借款人住所
					conInfo.setBidBillNo(bidInfoVO.getBillNo());// 标地票据号码
					conInfo.setState(ContractConstants.CONTRACT_STATE_Y);// 状态：0-禁用，1-启用
					conInfo.setCreateTime(new Date());
					conInfoList.add(conInfo);
				}
			}
			//把投资记录已存在的合同信息作废,将其状态修改为0
			if (updateConInfoList.size() > 0){
				conInfoService.updateConInfoBatch(updateConInfoList,updateConInfoList.size());
			}
			if (conInfoList.size() > 0){
				conInfoService.insertConInfoBatch(conInfoList);
			}else {
				logger.error("generateContractList, 没有可生成的合同信息, 投资记录id集合: {}, 合同类型: {}, 异常信息: ",
						JSON.toJSON(investIdList), contractType);
			}
		}catch (Exception e){
			logger.error("generateContractList, 生成合同信息异常, 投资记录id集合: {}, 合同类型: {}, 异常信息: ",
					JSON.toJSON(investIdList), contractType, e);
			throw new GeneralException("生成合同信息异常！");
		}

		//合同生成成功，生成其对应的pdf信息
		try {
			conInfoToPdf(conInfoList);
		} catch (Exception e) {
			logger.error("generateContractList, 合同转换为pdf异常, 投资记录id集合: {}, 合同类型: {}, 异常信息: ",
					JSON.toJSON(investIdList), contractType, e);
		}
	}

	@Override
	public String showContractTemplate(int contractType) {
		String content = "";
		ConTemplate template = conTemplateService.findConTemplateByType(contractType);
		if (template != null) {
			content = template.getContent();
			content = getContractInfo(content, null);
		}
		return content;
	}

	private String showInvestAndLoanContract(ConInfo conInfo, int contractType,Integer bidTimeId) {
		String result;
		ConInfo contractInfo = conInfoService.findConInfo(conInfo);
		if (contractInfo != null) {
			// 获取该合同信息对应的合同模板内容
			ConTemplate template = conTemplateService.findConTemplateById(contractInfo.getContractTemplateId());
			if (template != null) {
				//生效时间的标的id不为null，则生效时间以该标地为准
				if (null != bidTimeId){
					contractInfo.setBidId(bidTimeId);
				}
				String content = template.getContent();
				// 组装展示合同的数据
				result = getContractInfo(content, contractInfo);
			} else {
				logger.info("没有获取到合同信息对应的合同模板内容，合同信息为：" + contractInfo.toString());
				result = "暂无合同内容";
			}
		} else {
			// 标地未放款，将展示合同模板内容
			result = showContractTemplate(contractType);
		}
		return result;
	}

	private String getContractInfo(String content, ConInfo conInfo) {
		String contractInfo = "";
		// 处理合同模板内容中的参数信息
		StringWriter writer = new StringWriter();
		Configuration configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		content = content.replace("&lt;","<").replace("&gt;",">");
		stringLoader.putTemplate("contract", content);
		configuration.setTemplateLoader(stringLoader);
		try {
			Template template = configuration.getTemplate("contract", "utf-8");
			// 合同参数
			Map<String, Object> root = new HashMap<>();
			if (conInfo == null) {
				// 没有合同信息返回合同模板内容
				root.put("contractCode", "******");// 协议编号
				root.put("investName", "******");// 投资人真实姓名
				root.put("investCardId", "******");// 投资人身份证号
				root.put("effectiveDate", "******");// 合同生效日期
				root.put("expireDate", "******");// 合同截止日期
				root.put("investAmount", "******");// 投资金额
				root.put("investAmountRMB", "******");// 投资金额人民币中文格式
				root.put("investCouponKValue","******");//投资使用投资红包金额
				root.put("bidName", "******");// 标地名称
				root.put("bidRate", "******");// 标地利率
				root.put("bidAmount", "******");// 标的总金额
				root.put("bidPeriod", "******");// 标的周期
				root.put("borrowName", "******");// 借款人真实姓名
				root.put("borrowCardId", "******");// 借款人身份证号
				root.put("bidLoanUse", "******");// 借款用途
				root.put("biddRepaymentWay", "******");// 还款方式
				root.put("payeeName", "******");// 收款人名称
				root.put("payeeBankCard", "******");// 收款人银行卡号
				root.put("payeeBankName", "******");// 收款人银行名称
				root.put("bidProjectNo", "******");// 标地项目编号
				root.put("investTel", "******");// 投资人电话
				root.put("investEmail", "******");// 投资人邮箱
				root.put("borrowAddress", "******");// 借款人住所
				root.put("bidBillNo", "******");// 标地票据号码
				root.put("investReceivables", "******");// 投资全部应收
				//如果为债权转让协议，查询债权转让人列表
				List<Map<String,String>> transferList = new ArrayList<>(2);
				Map<String,String> userZ = new HashMap<>();
				userZ.put("transferUserRealName","******");
				userZ.put("transferUserIdCard","******");
				Map<String,String> userL = new HashMap<>();
				userL.put("transferUserRealName","******");
				userL.put("transferUserIdCard","******");
				transferList.add(userZ);
				transferList.add(userL);
				root.put("transferList",transferList);
				root.put("companyName", "******");// 公司名称
				root.put("companyAddr", "******");// 公司地址
				root.put("companyTel", "******");// 公司电话
				root.put("revenueProcess", "******");// 收益处理方式
			} else {
				// 展示合同信息
				root.put("contractCode", conInfo.getContractCode());// 协议编号
				root.put("investName", conInfo.getInvestName());// 投资人真实姓名
				root.put("investCardId", conInfo.getInvestCardId());// 投资人身份证号
				BidInfo bidInfo = bidInfoService.findBidInfoById(conInfo.getBidId());
				conInfo.setEffectiveDate(bidInfo.getLendingTime());
				// 根据标的期限单位:1-年,2-月,3-天，设置合同截止日期
				if (bidInfo.getTermUnit() == 3) {
					conInfo.setExpireDate(DateUtils.addDays(bidInfo.getLendingTime(), bidInfo.getTermValue()));
				} else if (bidInfo.getTermUnit() == 2) {
					conInfo.setExpireDate(DateUtils.addMonth(bidInfo.getLendingTime(), bidInfo.getTermValue()));
				} else if (bidInfo.getTermUnit() == 1) {
					conInfo.setExpireDate(DateUtils.addMonth(bidInfo.getLendingTime(), bidInfo.getTermValue() * 12));
				}
				root.put("effectiveDate", DateUtils.format(conInfo.getEffectiveDate(),"yyyy年MM月dd日"));// 合同生效日期
				root.put("expireDate", DateUtils.format(conInfo.getExpireDate(),"yyyy年MM月dd日"));// 合同截止日期
				root.put("investCouponKValue",conInfo.getInvestCouponKValue() + "元");//投资使用投资红包金额
				root.put("investAmount", conInfo.getInvestAmount() + "元");// 投资金额
				root.put("investAmountRMB", ContractUtils.toBigMode(conInfo.getInvestAmount().doubleValue()));// 投资金额人民币格式
				root.put("bidName", conInfo.getBidName());// 标地名称
				root.put("bidRate",
						conInfo.getBidRate() + "%" + (conInfo.getInvestCouponJValue().compareTo(BigDecimal.ZERO) > 0
								? ("+" + conInfo.getInvestCouponJValue() + "%") : ""));// 标地利率
				root.put("bidAmount", conInfo.getBidAmount() + "元");// 标的总金额
				root.put("bidPeriod", conInfo.getBidTermValue()
						+ dicDataService.findNameByValue(INVEST, TERM_UNIT, conInfo.getBidTerm()));// 标的周期
				root.put("borrowName", conInfo.getBorrowName());// 借款人真实姓名
				root.put("borrowCardId", conInfo.getBorrowCardId());// 借款人身份证号
				root.put("bidLoanUse", dicDataService.findNameByValue(INVEST, LOAN_USE, conInfo.getBidLoanUse()));// 借款用途
				root.put("biddRepaymentWay",
						dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, conInfo.getBiddRepaymentWay()));// 还款方式
				root.put("payeeName", conInfo.getPayeeName());// 收款人名称
				root.put("payeeBankCard", conInfo.getPayeeBankCard());// 收款人银行卡号
				root.put("payeeBankName", conInfo.getPayeeBankName());// 收款人银行名称
				root.put("bidProjectNo", conInfo.getBidProjectNo());// 标地项目编号
				root.put("investTel", conInfo.getInvestTel());// 投资人电话
				root.put("investEmail", conInfo.getInvestEmail());// 投资人邮箱
				root.put("borrowAddress", conInfo.getBorrowAddress());// 借款人住所
				root.put("bidBillNo", conInfo.getBidBillNo());// 标地票据号码
				root.put("investReceivables", conInfo.getInvestAmount().add(CalcInterestUtil.calcInterest(conInfo.getBidTerm(),
						conInfo.getBidTermValue(),conInfo.getInvestAmount(), conInfo.getBidRate())) + "元");// 投资全部应收
				//如果为债权转让协议，查询债权转让人列表;如果为债权转让协议-转让人，查询债权接收人列表
				if (Objects.equals(conInfo.getContractType(),CONTRACT_TYPE_CREDITOR_TRANSFER) ||
						Objects.equals(conInfo.getContractType(),CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR)){
					List<Map<String,String>> transferList = new ArrayList<>();
					BidTransferAuto condition = new BidTransferAuto();
					//债权转让协议获取原债权人，债权转让协议-转让方获取债权接收人
					if (Objects.equals(conInfo.getContractType(),CONTRACT_TYPE_CREDITOR_TRANSFER)){
						condition.setNewInvestId(conInfo.getBidInvestId());
					}else {
						condition.setOldInvestId(conInfo.getBidInvestId());
					}
//					condition.setState(INVEST_TRANSFER_AUTO_STATE_MATCH);
					List<BidTransferAuto> list = bidTransferAutoService.findBidTransferAutoList(condition);
					//获取原债权人
					Set<Integer> transferUserIdList = list.stream().map(bidTransferAuto -> {
						return Objects.equals(conInfo.getContractType(),CONTRACT_TYPE_CREDITOR_TRANSFER) ?
								bidTransferAuto.getInvestUserId() : bidTransferAuto.getBuyUserId();
					}).collect(Collectors.toSet());
					if (transferUserIdList.size() > 0){
						Map<Integer, UserSimpleVo> userInfo = regUserService.findUserSimpleVoByIdList(transferUserIdList);
						userInfo.forEach((k,v) -> {
							Map<String,String> transferUser = new HashMap<>(2);
							transferUser.put("transferUserRealName",v.getRealName());
							transferUser.put("transferUserIdCard",v.getIdCard());
							transferList.add(transferUser);
						});
					}
					root.put("transferList",transferList);
				}
				//根据合同生成时间判断是否深圳理想还是霍尔果斯,2018.1.5 23:59:59 之前均为深圳理想，之后为霍尔果斯
				Date referenceDate = DateUtils.parse("2018-01-15 23:59:59");
				if (conInfo.getCreateTime().compareTo(referenceDate) <= 0){
					root.put("companyName", "鸿坤理想（深圳）科技发展有限公司");// 公司名称
					root.put("companyAddr", "");// 公司地址
					root.put("companyTel", ContractConstants.COMPANY_TEL);// 公司电话
				}else {
					root.put("companyName", ContractConstants.COMPANY_NAME);// 公司名称
					root.put("companyAddr", ContractConstants.COMPANY_ADDRESS);// 公司地址
					root.put("companyTel", ContractConstants.COMPANY_TEL);// 公司电话
				}
				root.put("revenueProcess", "收益返还");// 收益处理方式
			}
			try {
				template.process(root, writer);
				contractInfo = writer.toString();
			} catch (TemplateException e) {
				logger.error("获取合同模板内容模板元素替换异常, 合同信息: {}, 异常信息: ",conInfo,e);
				e.printStackTrace();
			}
		} catch (IOException e) {
			logger.error("获取合同模板内容IO异常, 合同信息: {}, 异常信息: ",conInfo,e);
			e.printStackTrace();
		}
		return contractInfo;
	}

	@Override
	public ResponseEntity<?> findContractTemplateDetail(int id) {
		ConTemplate conTemplate = conTemplateService.findConTemplateById(id);
		if(conTemplate == null){
			return new ResponseEntity<>(ERROR,"合同模板不存在！");
		}
		String content = getContractInfo(conTemplate.getContent(), null);
		if ("".equals(content)){
			return new ResponseEntity<>(ERROR,"合同模板内容生成错误，请检查元数据是否正确！");
		}
		return new ResponseEntity(SUCCESS,getContractInfo(conTemplate.getContent(), null));
	}

	@Override
	public String getContractContent(RegUser regUser, int contractType, int location, Integer bidInvestId, Integer bidId) {
		String content = "";
		//根据用户查看合同的位置进行不同的逻辑处理
		if (regUser == null || location ==  ContractConstants.CONTRACT_LOCATION_BIDD){
			//用户未登录或从标地详情页查看合同，展示合同的模板内容
			content = this.showContractTemplate(contractType);
		}else if (location == ContractConstants.CONTRACT_LOCATION_INVEST || location == ContractConstants
				.CONTRACT_LOCATION_ORIGIN_PROJECT || location == ContractConstants.CONTRACT_LOCATION_BORROWER_CONTRACT){
			//从投资记录或查看源项目处查看合同，若查询不到合同信息则展示模板内容，否则展示数据内容
			ConInfo conInfo = new ConInfo();
			conInfo.setContractType(contractType);
			conInfo.setBidInvestId(bidInvestId);
			if (location != ContractConstants.CONTRACT_LOCATION_BORROWER_CONTRACT){
				conInfo.setInvestUserId(regUser.getId());
			}
			conInfo.setState(ContractConstants.CONTRACT_STATE_Y);
			Integer bidTimeId = null;
			if (location == ContractConstants.CONTRACT_LOCATION_ORIGIN_PROJECT){
				//从查看源项目处查看合同时，合同生效的日期以优选标为准，其他以当前标的生效时间为准
				BidInvest bidInvest = bidInvestService.findBidInvestById(bidInvestId);
				bidTimeId = bidInvest.getGoodBidId();
			}
			content = this.showInvestAndLoanContract(conInfo,contractType,bidTimeId);
		}
		else {
			logger.info("查看位置不详，值为："+location);
		}
		return content;
	}

	@Override
	public List<CommonInvestConInfoVO> findGoodInvestMatchContractInfo(Integer regUserId, Integer bidId, Integer investId) {
		List<CommonInvestConInfoVO> list = new ArrayList<>();
		//1.获取优选投资记录对应散标投资记录集合
		BidInvest condition = new BidInvest();
		condition.setRegUserId(regUserId);
		condition.setGoodInvestId(investId);
		condition.setGoodBidId(bidId);
		condition.setStateList(Arrays.asList(INVEST_STATE_SUCCESS,INVEST_STATE_MANUAL,INVEST_STATE_AUTO));
		List<BidInvest> commonInvestList =  bidInvestService.findBidInvestList(condition);
		if (commonInvestList.size() > 0){
			//根据优选标 当前匹配期数=当前时间-放款时间，过滤出正在匹配的散标记录集合
			BidInfo bidInfo = bidInfoService.findBidInfoById(bidId);
			if (bidInfo != null && InvestConstants.BID_STATE_WAIT_REPAY == bidInfo.getState()){
				int term = DateUtils.getDaysBetween(bidInfo.getLendingTime(), new Date()) + 1;
				List<Integer> listIds = commonInvestList.stream().filter(invest ->
					(term >= Integer.valueOf(invest.getGoodInvestTerm().split(":")[0])
							&& term <= Integer.valueOf(invest.getGoodInvestTerm().split(":")[1]))
				).map(record -> {return record.getId();}).collect(Collectors.toList());
				if (listIds.size() > 0){
					//根据散标投资记录id集合获取对应的合同信息
					getCommonInvestConInfoVOList(listIds,list);
				}
			}
		}
		return list;
	}

	@Override
	public List<CommonInvestConInfoVO> findBorrowerContractInfo(Integer regUserId, Integer bidId) {
		List<CommonInvestConInfoVO> list = new ArrayList<>();
		//获取散标借款人的合同
		BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(bidId);
		//散标&&收益中&&为当前用户的借款标
		if (bidInfoVO != null && InvestConstants.MATCH_BID_TYPE_COMMON.equals(BidInfoUtil.matchBidTypeByProdutType(bidInfoVO
				.getType())) && InvestConstants.BID_STATE_WAIT_REPAY.equals(bidInfoVO.getState()) && regUserId.equals
				(bidInfoVO.getBorrowerId())){
			BidInvest condition = new BidInvest();
			condition.setBidInfoId(bidId);
			//包含投资成功和已转让的投资记录
			condition.setStateList(Arrays.asList(INVEST_STATE_SUCCESS,INVEST_STATE_MANUAL,INVEST_STATE_TRANSFER,INVEST_STATE_SUCCESS_BUYER));
			condition.setMatchState(MATCH_STATE_NONE);
			//获取该散标的投资记录，直投散标获取投资记录，匹配散标获取投资记录
			if (InvestConstants.BID_MATCH_TYPE_YES == bidInfoVO.getMatchType()){
				condition.setInvestChannel(BID_INVEST_CHANNEL_MATCH);
			}else {
				condition.setInvestChannel(BID_INVEST_CHANNEL_IMMEDIATE);
			}
			List<BidInvest> commonInvestList = bidInvestService.findBidInvestListByCondition(condition);
			if (commonInvestList.size() > 0){
				List<Integer> listIds = commonInvestList.stream().map(record -> {return record.getId();}).collect(Collectors.toList());
				getCommonInvestConInfoVOList(listIds,list);
			}
		}

		return list;
	}

	private void getCommonInvestConInfoVOList(List<Integer> listIds,List<CommonInvestConInfoVO> list){
		//根据散标投资记录id集合获取对应的合同信息
		List<ConInfo> conInfoList = conInfoService.findConInfoListByInvestIds(listIds);
		conInfoList.forEach((conInfo) -> {
			ConType queryConType = new ConType();
			queryConType.setType(conInfo.getContractType());
			//查看合同的展示名称
			List<ConType> conTypes = conTypeService.findConTypeList(queryConType);
			if (conTypes.size() > 0){
				CommonInvestConInfoVO conInfoVO = new CommonInvestConInfoVO();
				conInfoVO.setContractCode(conInfo.getContractCode());
				conInfoVO.setBidInvestId(conInfo.getBidInvestId());
				conInfoVO.setBorrowName(StringUtils.isEmpty(conInfo.getBorrowName()) ? "" : StringUtilsExtend
						.getEllipsis(conInfo.getBorrowName(),0,conInfo.getBorrowName().length() - 1));
				conInfoVO.setContractName(conTypes.get(0).getShowName());
				conInfoVO.setContractType(conInfo.getContractType());
				conInfoVO.setInvestAmount(conInfo.getInvestAmount());
				conInfoVO.setInvestName(StringUtils.isEmpty(conInfo.getInvestName()) ? "" : StringUtilsExtend
						.getEllipsis(conInfo.getInvestName(),0,conInfo.getInvestName().length() - 1));
				list.add(conInfoVO);
			}
		});
		//通过投资记录进行排序，使得相同投资记录id合同信息排列在一起
		list.sort(Comparator.comparing(CommonInvestConInfoVO::getBidInvestId));
	}

	/**
	 * @Description      ：批量生成合同信息后，批量处理生成的合同记录，进行转换成pdf，pdf转换成功后将其发送至生成投资记录安存信息
	 * 						生成的pdf路径为 contractType+investId.pdf 因此pdf路径值不往合同信息里面存储
	 * @Method_Name      ：conInfoToPdf 
	 * @param list        合同信息记录
	 * @return void    
	 * @Creation Date    ：2019/1/22        
	 * @Author           ：pengwu@hongkunjinfu.com
	 */
	private void conInfoToPdf(List<ConInfo> list){
		//初始化需要进行安存处理的合同信息记录
		List<ConInfo> processList = new ArrayList<>();
		//按照现在的规则，只对优选计划的合同信息进行生成pdf并进行安存信息保存
		for (ConInfo conInfo:list){
			if (Objects.equals(ContractConstants.CONTRACT_TYPE_GOOD_PLAN,conInfo.getContractType())){
				//开始html转换成pdf，获取html的内容
				String content = this.getContentByConInfo(conInfo);
				if (StringUtils.isBlank(content)){
					continue;
				}
				//转换生成pdf，pdf名称按照规则bidId-contractType-userId-investId生成其文件名称
				String pdfName = conInfo.getBidId() + "-" + conInfo.getContractType() + "-" + conInfo.getInvestUserId() + "-" + conInfo.getBidInvestId() + ".pdf";
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					 PdfWriter pdfWriter = new PdfWriter(baos)){
					ConverterProperties props = new ConverterProperties();
					FontProvider fp = new FontProvider();
					fp.addStandardPdfFonts();
					String path = ConInfoFacadeImpl.class.getResource("/").getPath();
					fp.addDirectory(path);

					props.setFontProvider(fp);
					props.setBaseUri(path);
					HtmlConverter.convertToPdf(content,pdfWriter,props);
					//上传至oss
					FileInfo fileInfo = OSSLoader.getInstance()
							.setUseRandomName(false)
							.setAllowUploadType(FileType.NO_LIMIT)
							.bindingUploadFile(new FileInfo(new ByteArrayInputStream(baos.toByteArray())))
							.setFileState(FileState.UN_UPLOAD)
							.setBucketName(OSSBuckets.HKJF)
							.setFilePath("/contract/pdf")
							.setFileName(pdfName)
							.doUpload();
					if (fileInfo.getFileState().equals(FileState.SAVED)){
						processList.add(conInfo);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		InvestPreServeTemplate template = null;
		ConInfo conInfo = null;
		String  basicUrl =  PropertiesHolder.getProperty("oss.url");
		//mq发送至安存处理方法
		if(processList != null ){
			for(int i = 0 ; i<processList.size(); i++){
				conInfo = processList.get(i);
				template = new InvestPreServeTemplate();
				String fileName = conInfo.getBidId() + "-" + conInfo.getContractType() + "-" + conInfo.getInvestUserId() + "-" + conInfo.getBidInvestId() + ".pdf";
				template.setFlowNo(ContractConstants.CONTRACT_ANCUN_FLOWNO);
				BidInvest bidInvest = this.bidInvestService.findBidInvestById(conInfo.getBidInvestId());
				if(bidInvest ==null || StringUtils.isBlank(bidInvest.getAncunNo())){
					continue;
				}
				template.setRecordNo(bidInvest.getAncunNo());
				template.setAttachmentPath(basicUrl+"/contract/pdf"+fileName);
				
			}
		}
		

	}

	private String getContentByConInfo(ConInfo conInfo){
		String result = "";
		if (conInfo != null) {
			// 获取该合同信息对应的合同模板内容
			ConTemplate template = conTemplateService.findConTemplateById(conInfo.getContractTemplateId());
			if (template != null) {
				String content = template.getContent();
				// 组装展示合同的数据
				result = getContractInfo(content, conInfo);
			} else {
				logger.info("没有获取到合同信息对应的合同模板内容，合同信息为：" + conInfo.toString());
			}
		}
		return result;
	}

}
