package com.hongkun.finance.payment.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinPayConfigDao;
import com.hongkun.finance.payment.dao.FinPaymentRecordDao;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinPayConfig;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.security.Base64Util;
import com.hongkun.finance.payment.service.FinPayCheckAccountService;
import com.hongkun.finance.payment.util.FinPayCheckUtil;
import com.hongkun.finance.payment.util.ThirdPaymentUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.SftpUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.service.impl.
 *          FinPayCheckAccountServiceImpl. java
 * @Class Name : FinPayCheckAccountServiceImpl.java
 * @Description : 与第三方对账类
 * @Author : yanbinghuang
 */
@Service
public class FinPayCheckAccountServiceImpl implements FinPayCheckAccountService {
	private static final Logger logger = LoggerFactory.getLogger(FinPayCheckAccountServiceImpl.class);

	@Autowired
	private FinPayConfigDao finPayConfigDao;
	@Autowired
	private FinPaymentRecordDao finPaymentRecordDao;

	@Override
	public void excuteLianLianReconciliation(String reconciliationDate) {
		try {
			// 样验对账日期是否合法
			boolean dateFlag = FinPayCheckUtil.dateMatcher(reconciliationDate);
			if (!dateFlag) {
				logger.info("对账日期格式错误，对账失败！");
			}
			// 查询支付配置信息
			String key = SystemTypeEnums.HKJF.getType() + PayChannelEnum.LianLian.getChannelKey()
					+ PayStyleEnum.PAYDZ.getType();
			FinPayConfig payConfig = finPayConfigDao.findPayConfigInfo(key, SystemTypeEnums.HKJF.getType(), "",
					PayChannelEnum.LianLian.getChannelKey(), PayStyleEnum.PAYDZ.getType());
			if (payConfig == null) {
				logger.info("查询支付配置信息为空,对账失败！");
				return;
			}
			// 连接SFTP
			SftpUtils sftpUtils = SftpUtils.getInstance(payConfig.getPayUrl(),
					Integer.parseInt(payConfig.getSignStyle()), payConfig.getPublicKey(), payConfig.getPrivateKey());
			// 远程SFTP充值文件名称
			StringBuffer remoteRechargeFileName = new StringBuffer("JYMX_").append(payConfig.getMerchantNo())
					.append("_").append(reconciliationDate.replaceAll("-", "")).append(".txt");
			// 充值对账文件下载文件到本地目录
			File rechargeFile = sftpUtils.downloadFile(payConfig.getPayMd5Key(), remoteRechargeFileName.toString(),
					payConfig.getBackUrl(), remoteRechargeFileName.toString());
			// 远程SFTP提现文件名称
			StringBuffer remoteWithDrawsFileName = remoteRechargeFileName.replace(0, 4, "FKMX");
			// 提现对账文件下载文件到本地目录
			File withDrawsFile = sftpUtils.downloadFile(payConfig.getPayMd5Key(), remoteWithDrawsFileName.toString(),
					payConfig.getBackUrl(), remoteWithDrawsFileName.toString());
			// 处理充值对账数据
			// 构造一个BufferedReader类来读取文件
			readFileAndDealReconciliation(new BufferedReader(new FileReader(rechargeFile)), PayChannelEnum.LianLian,
					null);
			// 处理提现对账数据
			readFileAndDealReconciliation(new BufferedReader(new FileReader(withDrawsFile)), PayChannelEnum.LianLian,
					null);
			// 调用支付查证接口，查询未对账的数据，进行对账处理
			reconciliationByInterface(reconciliationDate,PayChannelEnum.LianLian.getChannelNameValue());
		} catch (Exception e) {
			logger.error("对账失败，失败原因: ", e);
		}
	}

	@Override
	public void excuteBaoFuReconciliation(Date settleDate) {
		logger.info("FinPayCheckAccountServiceImpl excuteBaoFuReconciliation settleDate: {}", settleDate);
		String reconciliationDate = DateUtils.format(settleDate, DateUtils.DATE);
		// 样验对账日期是否合法
		boolean dateFlag = FinPayCheckUtil.dateMatcher(reconciliationDate);
		if (!dateFlag) {
			logger.error("对账日期格式错误，对账失败！");
		}
		// 下载对账文件到本地
		File fileFi = this.downloadAccountFile(reconciliationDate, BAOFU_FILE_TYPE_INCOME);
		File fileFo = this.downloadAccountFile(reconciliationDate, BAOFU_FILE_TYPE_PAY);
		// 解压对账文件，进行对账处理
		if(fileFi != null){
			this.readZipFile(fileFi, fileFi.getParent(), BAOFU_FILE_TYPE_INCOME);
		}
		if(fileFo != null){
			this.readZipFile(fileFo, fileFo.getParent(), BAOFU_FILE_TYPE_PAY);
		}
		// 调用支付查证接口，查询未对账的数据，进行对账处理
		int settDays = 3; // 查询3天未对账日期的记录
		for (int i = 0; i < settDays; i++) {
			if (i == 0) {
				// 查询当天遗漏数据，分开进行对账
				reconciliationByInterface(reconciliationDate, PayChannelEnum.BaoFu.getChannelNameValue());
			} else {
				Date useSettleDate = DateUtils.addDays(settleDate, -i);
				reconciliationByInterface(DateUtils.format(useSettleDate, DateUtils.DATE),PayChannelEnum.BaoFu.getChannelNameValue());
			}
		}

	}
	@Override
	public void excuteYeepayReconciliation(Date settleDate) {
		logger.info("FinPayCheckAccountServiceImpl excuteBaoFuReconciliation settleDate: {}", settleDate);
		String reconciliationDate = DateUtils.format(settleDate, DateUtils.DATE);
		// 调用支付查证接口，查询未对账的数据，进行对账处理
		int settDays = 3; // 查询3天未对账日期的记录
		for (int i = 0; i < settDays; i++) {
			if (i == 0) {
				// 查询当天遗漏数据，分开进行对账
				reconciliationByInterface(reconciliationDate, PayChannelEnum.Yeepay.getChannelNameValue());
			} else {
				Date useSettleDate = DateUtils.addDays(settleDate, -i);
				reconciliationByInterface(DateUtils.format(useSettleDate, DateUtils.DATE),PayChannelEnum.Yeepay.getChannelNameValue());
			}
		}
	}
	/**
	 * @Description : 解压ZIP文件，获取TXT文件,进行对账处理
	 * @Method_Name : readZipFile;
	 * @param file
	 *            文件对象
	 * @param filePath
	 *            文件路径
	 * @param fileType
	 *            对账类型
	 * @return : void;
	 * @Creation Date : 2018年1月25日 下午5:17:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void readZipFile(File file, String filePath, String fileType) {
		if (file == null) {
			return;
		}
		FileInputStream inputStream = null;
		ZipInputStream zins = null;
		BufferedReader bufferReader = null;
		ZipEntry zipEntry = null;
		try {
			inputStream = new FileInputStream(file);
			zins = new ZipInputStream(inputStream);
			while ((zipEntry = zins.getNextEntry()) != null) {
				logger.info("解压zip对账文件后的txt文件名称: {}" + zipEntry.getName());
				bufferReader = new BufferedReader(new InputStreamReader(zins));
				readFileAndDealReconciliation(bufferReader, PayChannelEnum.BaoFuProtocol, fileType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (zins != null) {
					zins.close();
				}
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	/**
	 * @Description :下载宝付对账文件
	 * @Method_Name : downloadAccountFile;
	 * @param reconciliationDate
	 * @param fileType
	 * @return
	 * @return : File;
	 * @Creation Date : 2018年1月25日 下午2:35:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private File downloadAccountFile(String reconciliationDate, String fileType) {
		InputStream dataByte = null;
		OutputStream fielOutPut = null;
		try {
			Map<String, String> reqCheckData = new HashMap<String, String>();
			// 查询支付配置信息，组装查询对账文件的MAP
			String key = SystemTypeEnums.HKJF.getType() + PayChannelEnum.BaoFuProtocol.getChannelKey()
					+ PayStyleEnum.PAYDZ.getType();
			FinPayConfig payConfig = finPayConfigDao.findPayConfigInfo(key, SystemTypeEnums.HKJF.getType(), "",
					PayChannelEnum.BaoFuProtocol.getChannelKey(), PayStyleEnum.PAYDZ.getType());
			if (payConfig == null) {
				logger.info("查询支付配置信息为空,对账失败！");
				return null;
			}
			reqCheckData.put("version", payConfig.getPayVersion());
			reqCheckData.put("member_id", payConfig.getMerchantNo());
			reqCheckData.put("file_type", fileType);
			reqCheckData.put("client_ip", payConfig.getIpAddress());
			reqCheckData.put("settle_date", reconciliationDate);
			// 调用第三方接口获取对账文件
			logger.info("调用宝付获取对账文件请求数据: {}", reqCheckData);
			String resData = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqCheckData);
			logger.info("调用宝付获取对账文件响应数据: {}", resData);
			// 解析返回的文件参数
			if (StringUtils.isBlank(resData) || resData.indexOf("resp_code=0000") < 0) {
				logger.info("调用宝付接口获取对账文件的数据为空,对账失败!");
				return null;
			}
			// 返回的报文内容包含：(resp_code 返回码)(resp_msg 返回信息)
			// (resp_body 返回文件体)(reserved 预留域)
			String[] splitStr = resData.split("=");
			byte[] resByte = Base64Util.decodes(splitStr[3]);// 进行base64解码，解密后为byte类型
			// 设置宝付文件存放在本地路径
			String dirPath = payConfig.getBackUrl() + fileType + "/";
			// 设置保存的文件名
			String fileName = reconciliationDate + ".zip";
			// 将宝付的对账文件通过流存放在本地
			File file = SftpUtils.mkdirs(dirPath + fileName);
			dataByte = new ByteArrayInputStream(resByte);
			fielOutPut = new FileOutputStream(file);
			byte[] byteSize = new byte[1024];
			while (dataByte.available() > 0) {
				dataByte.read(byteSize); // 读取接收的文件流
				fielOutPut.write(byteSize); // 写入文件
			}
			logger.info("下载宝付对账文件成功！");
			return file;
		} catch (Exception e) {
			logger.error("调用宝付接口获取对账文件失败: ", e);
			return null;
		} finally {
			try {
				if (dataByte != null) {
					dataByte.close();
				}
				if (fielOutPut != null) {
					fielOutPut.flush();
					fielOutPut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description : 调用支付查证接口，查询未对账的数据，进行对账处理
	 * @Method_Name : reconciliationByInterface;
	 * @param reconciliationDate
	 * @return : void;
	 * @Creation Date : 2017年8月14日 下午4:57:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void reconciliationByInterface(String reconciliationDate, int payChannel) {
		// 查询对账日期往后以及对账状态为未对账的记录
		FinPaymentRecord finPaymentRecordCondition = new FinPaymentRecord();
		finPaymentRecordCondition.setCreateTimeBegin(DateUtils.parse(reconciliationDate, DateUtils.DATE));
		finPaymentRecordCondition
				.setCreateTimeEnd(DateUtils.addDays(DateUtils.parse(reconciliationDate, DateUtils.DATE), 1));
		finPaymentRecordCondition.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_WAIT);
		if(payChannel != PayChannelEnum.BaoFu.getChannelNameValue()){
			finPaymentRecordCondition.setPayChannel(payChannel);
		}
		List<FinPaymentRecord> finPaymentRecordList = this.finPaymentRecordDao
				.findByCondition(finPaymentRecordCondition);
		logger.info("reconciliationByInterface, 用支付查证接口查询未对账的数据进行对账处理, 支付记录集合: finPaymentRecordList",
				JsonUtils.toJson(finPaymentRecordList));
		for (FinPaymentRecord finPaymentRecord : finPaymentRecordList) {
			logger.info("reconciliationByInterface 用支付查证接口查询未对账的数据进行对账处理, 用户Id: {}, flowId: {}",
					finPaymentRecord.getRegUserId(), finPaymentRecord.getFlowId());
			// 调用支付查证接口对账,如果是提现操作，提现的状态必须是 交易成功、冲正、划转中、划转失败中的一个,才可以进行对账
			if (PayStyleEnum.WITHDRAW.getValue() == finPaymentRecord.getTradeType()
					&& !(finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT
							|| finPaymentRecord.getState() == TradeStateConstants.CORRECT_MONEY
							|| finPaymentRecord.getState() == TradeStateConstants.BANK_TRANSFER
							|| finPaymentRecord.getState() == TradeStateConstants.TRANSIT_FAIL)) {
				logger.info("调用支付查证接口, 支付流水号: {}, 用户Id: {}, 提现状态: {}, 不符合查证条件", finPaymentRecord.getFlowId(),
						finPaymentRecord.getRegUserId(), finPaymentRecord.getState());
				continue; 
			}
			// 根据渠道判断查证接口的参数
			String payStyle = "";// 查证方式
			if (PayStyleEnum.RECHARGE.getValue() == finPaymentRecord.getTradeType()) {
				payStyle = PayStyleEnum.typeByValue(finPaymentRecord.getRechargeSource())
						+ PayStyleEnum.PAYCHECK.getType();
			} else {
				payStyle = PayStyleEnum.WITHDRAW.getType() + PayStyleEnum.PAYCHECK.getType();
			}
			
			// 调用支付查询接口
			String key = SystemTypeEnums.HKJF.getType() + PlatformSourceEnums.PC.getType()
					+ PayChannelEnum.fromChannelCode(finPaymentRecord.getPayChannel()) + payStyle;
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.fromChannelCode(finPaymentRecord.getPayChannel()),
					payStyle);
			ResponseEntity<?> responseEntity = ThirdPaymentUtil.findPayCheck(finPaymentRecord.getFlowId(),
					finPaymentRecord.getTradeType(), finPaymentRecord.getRechargeSource(), finPayConfig,
					finPaymentRecord.getCreateTime());
			if (responseEntity != null && responseEntity.getResStatus() == Constants.SUCCESS) {
				Map<String, Object> resultMap = (Map<String, Object>) responseEntity.getParams().get("resPayCheck");
				// 处理对账
				FinPaymentRecord paymentRecord = reconciliationByInterface(resultMap, finPaymentRecord);
				if (paymentRecord != null) {
					this.finPaymentRecordDao.updateByFlowId(paymentRecord);
				}
			} else {
				FinPaymentRecord payRecord = new FinPaymentRecord();
				payRecord.setFlowId(finPaymentRecord.getFlowId());
				payRecord.setReconciliationDesc(responseEntity.getResMsg().toString());
				this.finPaymentRecordDao.updateByFlowId(payRecord);
			}
		}

	}

	/**
	 * @Description : 通过查证结果，处理对账
	 * @Method_Name : reconciliationByInterface;
	 * @param resultMap
	 *            第三方查证返回的状态
	 * @param finPaymentRecord
	 *            支付记录对象
	 * @return
	 * @return : FinPaymentRecord;
	 * @Creation Date : 2018年1月26日 下午2:05:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private FinPaymentRecord reconciliationByInterface(Map<String, Object> resultMap,
			FinPaymentRecord finPaymentRecord) {
		logger.info("reconciliationByInterface method , resultMap:{}, finPaymentRecord.flowId:{}", resultMap,
				finPaymentRecord.getFlowId());
		// 第三方支付系统转换成平台状态为四种状态：{0、支付成功 5、支付失败2、银行支付处理中 3、等待支付、4订单不存在}
		int ret_code = Integer.parseInt(resultMap.get("transState").toString());
		FinPaymentRecord newFinPaymentRecord = new FinPaymentRecord();
		newFinPaymentRecord.setFlowId(finPaymentRecord.getFlowId());
		// 提现特殊状态，不予对账
		if (PayStyleEnum.WITHDRAW.getValue() == finPaymentRecord.getTradeType()
				&& !(finPaymentRecord.getState() == TradeStateConstants.BANK_TRANSFER
						|| finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT
						|| finPaymentRecord.getState() == TradeStateConstants.TRANSIT_FAIL
						|| finPaymentRecord.getState() == TradeStateConstants.FINANCE_AUDIT_SUCCESS)) {
			logger.info("reconciliationByInterface method 提现状态不属于  5、6、7 9 ， 不予对账！");
			return newFinPaymentRecord;
		}
		// 如果用户点击充值，跳转到连连支付页面，但是没有支付，则关闭了页面，有可能会出现等待支付的状态，平台订单状态为待审核状态，此时应该将对账状态设置为对账成功
		if (PayStyleEnum.RECHARGE.getValue() == finPaymentRecord.getTradeType() && PAY_STATE_SUCCESS != ret_code
				&& TradeStateConstants.PENDING_PAYMENT == finPaymentRecord.getState()) {
			logger.info("平台订单号: {}, 第三方支付订单号: {}, 支付查证,我方待审核，宝付非成功状态。对账成功", finPaymentRecord.getFlowId(),
					resultMap.get("orderId").toString());
			newFinPaymentRecord.setState(TradeStateConstants.TRANSIT_FAIL);
			newFinPaymentRecord.setReconciliationDesc("第三方为等待支付状态，平台状态为待审核，则将订单状态置为失败,记录对账成功！");
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS);
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return newFinPaymentRecord;
		}
		// 如果交易状态为成功，则一定会返回订单流水号，及交易金额
		if (PAY_STATE_SUCCESS == ret_code) {
			// 如果第三方查证结果返回的订单号与平台订单号不一致，则认为对账失败
			if (!finPaymentRecord.getFlowId().equals(resultMap.get("orderId").toString())) {
				logger.info("平台订单号: {}, 第三方支付订单号: {}, 支付查证,第三方支付返回的订单号与平台订单号不一致", finPaymentRecord.getFlowId(),
						resultMap.get("orderId").toString());
				newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
				newFinPaymentRecord.setReconciliationDesc("第三方支付返回的订单号与平台订单号不一致,记录对账失败！");
				newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
				return newFinPaymentRecord;
			}
			// 如果交易状态为成功，则先对交易金额进行对账，查看平台与第三方支付订单对应的交易金额是否相等
			boolean resultFlag = payCheckMoney(new BigDecimal(resultMap.get("transMoney").toString()),
					newFinPaymentRecord, finPaymentRecord);
			if (!resultFlag) {
				return newFinPaymentRecord;
			}
			// 如果第三方状态为成功，平台流水状态为成功，则认为对账成功
			if (PAY_STATE_SUCCESS == ret_code && finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT) {
				newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS);
				newFinPaymentRecord.setReconciliationDesc("对账成功！");
				newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
				return newFinPaymentRecord;
			}
		}
		// 根据支付结果状态，进行对账
		boolean resultStateFlag = payCheckByState(newFinPaymentRecord, finPaymentRecord, ret_code);
		if (!resultStateFlag) {
			return newFinPaymentRecord;
		} else {
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS);
			newFinPaymentRecord.setReconciliationDesc("对账成功！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return newFinPaymentRecord;
		}
	}

	/**
	 * @Description : 读取文件信息，并进行对账
	 * @Method_Name : readFileAndDealReconciliation;
	 * @param file
	 * @param payChannelEnum
	 * @param fileType
	 * @return : void;
	 * @Creation Date : 2018年1月25日 下午4:12:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void readFileAndDealReconciliation(BufferedReader bufferedReader, PayChannelEnum payChannelEnum,
			String fileType) {
		try {
			String resultStr = null;
			int line = 0;
			// 使用readLine方法，一次读一行
			while ((resultStr = bufferedReader.readLine()) != null) {
				// 第一行标题不记录，仅仅记录真正交易数据
				if (line == 0) {
					line++;
					continue;
				}
				line++;
				// 解析对账文件，并处理对账，更新对账状态
				if (PayChannelEnum.BaoFuProtocol.getChannelKey().equals(payChannelEnum.getChannelKey())
						|| PayChannelEnum.BaoFu.getChannelKey().equals(payChannelEnum.getChannelKey())
						|| PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannelEnum.getChannelKey())) {
					this.analysisAndDealBfAccount(resultStr, fileType);
				} else {
					this.analysisAndDealReconciliation(resultStr);
				}
			}
		} catch (Exception e) {
			logger.error("读取文件信息进行对账失败: ", e);
		}
	}

	/**
	 * @Description :解析并处理宝付的对账文件
	 * @Method_Name : analysisAndDealBfAccount;
	 * @param resultStr
	 *            文件内容
	 * @param fileType
	 *            文件类型
	 * @return : void;
	 * @Creation Date : 2018年1月25日 下午4:30:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void analysisAndDealBfAccount(String resultStr, String fileType) {
		FinPaymentRecord finPaymentRecord = null;
		logger.info("宝付文件对账类型: {}, 读取的文件内容: {}", fileType, resultStr);
		try {
			// 判断文件内容是否合法
			if (StringUtils.isBlank(resultStr) || !resultStr.contains("|")) {
				return;
			}
			// 解析对账数据，获取对账字段值
			String[] resultContent = resultStr.split("[|]");
			if (resultContent.length < 10 || isChinese(resultContent[0])) {
				logger.info("宝付对账,文件内容长度: {}, 宝付对账文件格式错误", resultContent.length);
				return;
			}
			// 处理对账
			if (BAOFU_FILE_TYPE_INCOME.equals(fileType)) {
				// 处理收款的对账
				finPaymentRecord = this.dealReconciliation(resultContent[5], PAYMENT_INCOME,
						String.valueOf(BAOFU_PAY_STATE_SUCCESS).equals(resultContent[7]) ? PAY_STATE_SUCCESS : PAY_STATE_FAIL,
						new BigDecimal(resultContent[8]));
			} else {
				// 处理付款的对账
				finPaymentRecord = this.dealReconciliation(resultContent[5], PAYMENT_OUT,
						String.valueOf(BAOFU_PAY_STATE_SUCCESS).equals(resultContent[8]) ? PAY_STATE_SUCCESS : PAY_STATE_FAIL,
						new BigDecimal(resultContent[9]));
			}
			// 更新对账状态
			if (finPaymentRecord != null) {
				this.finPaymentRecordDao.updateByFlowId(finPaymentRecord);
			}
		} catch (Exception e) {
			logger.error("宝付对账失败:", e);
		}
	}

	/**
	 * @Description : 解析并处理对账文件
	 * @Method_Name : analysisAndDealReconciliation;
	 * @param resultStr
	 * @return : void;
	 * @Creation Date : 2018年1月25日 下午3:58:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void analysisAndDealReconciliation(String resultStr) {
		logger.info("连连对账读取的文件内容：{}", resultStr);
		try {
			if (StringUtils.isBlank(resultStr) || !resultStr.contains(",")) {
				return;
			}
			// 解析对账数据，获取对账字段值
			String[] result = resultStr.split(",");
			// 如果长度小于13，则认为连连的文件格式有问题，不进行对账
			if (result.length < 13) {
				logger.info("连连对账,文件长度: {}, 连连对账文件格式错误", result.length);
				return;
			}
			// 处理对账
			FinPaymentRecord finPaymentRecord = this.dealReconciliation(result[0], Integer.parseInt(result[7]),
					Integer.parseInt(result[8]), new BigDecimal(result[6]));
			// 更新对账状态
			if (finPaymentRecord != null) {
				this.finPaymentRecordDao.updateByFlowId(finPaymentRecord);
			}
		} catch (Exception e) {
			logger.error("连连对账失败: ", e);
		}
	}

	/**
	 * @Description :处理对账，并更新平台对账状态
	 * @Method_Name : dealReconciliation;
	 * @param transFlowId
	 *            交易流水号 (平台流水号、其它第三方记录商户号)
	 * @param payReceiveCashFlag
	 *            商户收款标志(0收款 1付款)
	 * @param thirdTransState
	 *            交易状态(0-成功 5-已退款)
	 * @param transMoney
	 *            交易金额
	 * @return : void;
	 * @Creation Date : 2017年8月11日 下午5:09:31;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private FinPaymentRecord dealReconciliation(String transFlowId, int payReceiveCashFlag, int thirdTransState,
			BigDecimal transMoney) {
		logger.info(
				"方法: dealReconciliation, 处理对账并更新平台对账状态, transFlowId: {}, payReceiveCashFlag: {}, thirdTransState: {}",
				transFlowId, payReceiveCashFlag, thirdTransState);
		FinPaymentRecord newFinPaymentRecord = null;
		try {
			// 根据第三方提供的流水号，查询平台流水信息
			FinPaymentRecord finPaymentRecord = this.finPaymentRecordDao.findFinPaymentRecordByFlowId(transFlowId);
			if (finPaymentRecord == null) {
				logger.info("流水标识: {}, 支付对账, 第三方支付平台有流水,平台无此订单", transFlowId);
				return null;
			}
			logger.info(" 处理对账并更新平台对账状态, 查询的平台支付订单信息finPaymentRecord: {}", JsonUtils.toJson(finPaymentRecord));
			/** 对账状态标记 1未对账， 2 对账成功 ， 3对账失败 **/
			// 如果此笔订单已经对账，则不再进行对账
			if (finPaymentRecord.getReconciliationState() == TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS) {
				logger.info("流水标识: {}, 支付对账, 此订单已经对账不再进行对账", transFlowId);
				return null;
			}
			// 初始化要更新的对账状态
			newFinPaymentRecord = new FinPaymentRecord();
			newFinPaymentRecord.setFlowId(finPaymentRecord.getFlowId());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			/** 1 、根据流水状态进行对账 **/
			// 如果连连状态为成功，平台流水状态为不成功，则认为是错账，记录为失败
			boolean resultStateFlag = payCheckByState(newFinPaymentRecord, finPaymentRecord, thirdTransState);
			if (!resultStateFlag) {
				return newFinPaymentRecord;
			}
			/** 2、根据交易类型进行对账 ，判断交易金额是否相等，充值和提现分别判断，因为提现有优惠券 **/
			boolean resultFlag = payCheckMoney(transMoney, newFinPaymentRecord, finPaymentRecord);
			if (!resultFlag) {
				return newFinPaymentRecord;
			}
			// 如果上面情况都不是，则认为对账成功
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS);
			newFinPaymentRecord.setReconciliationDesc("对账成功！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return newFinPaymentRecord;
		} catch (Exception e) {
			logger.error("流水标识: {}, 支付对账, 对账异常: {}", newFinPaymentRecord.getFlowId(), e);
			return newFinPaymentRecord;
		}
	}

	/**
	 * @Description : 判断交易金额是否相等
	 * @Method_Name : payCheckMoney;
	 * @param transMoney
	 *            第三方返回的 交易金额
	 * @param newFinTradeFlow
	 *            要更新的流水对象
	 * @param finTradeFlow
	 *            旧的流水对象信息
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年8月15日 上午10:52:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private boolean payCheckMoney(BigDecimal transMoney, FinPaymentRecord newFinPaymentRecord,
			FinPaymentRecord finPaymentRecord) {
		// 交易金额判断,第三方支付充值金额与平台充值金额不相等，则认为是错账，记录为失败
		if ((finPaymentRecord.getTransMoney().compareTo(transMoney) != 0 || CompareUtil.lteZero(transMoney))) {
			logger.info("流水标识: {}, 支付对账,交易金额: {}, 平台交易金额: {}, 平台与第三方支付系统交易金额不相等即对账失败", finPaymentRecord.getFlowId(),
					transMoney, finPaymentRecord.getTransMoney());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			newFinPaymentRecord.setReconciliationDesc(
					"第三方支付金额:{" + transMoney + "}与平台订单金额:{" + finPaymentRecord.getTransMoney() + "}不相等！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		return true;
	}

	/**
	 * @Description : 根据第三方支付结果状态，进行对账
	 * @Method_Name : payCheckByState;
	 * @param newFinTradeFlow
	 *            要更新的流水
	 * @param finTradeFlow
	 *            旧的流水信息
	 * @param thirdTransState
	 *            第三方支付结果状态
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年8月15日 上午11:12:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private boolean payCheckByState(FinPaymentRecord newFinPaymentRecord, FinPaymentRecord finPaymentRecord,
			int thirdTransState) {
		newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
		// 如果第三方状态为订单不存在，我方存在状态为成功，则对账失败
		if (PAY_STATE_NO_ORDER == thirdTransState
				&& finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT) {
			logger.info("流水标识: {}, 第三方支付记录不存在: {}, 平台记录成功状态: {}, 即对账失败", finPaymentRecord.getFlowId(), thirdTransState,
					finPaymentRecord.getState());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			newFinPaymentRecord.setReconciliationDesc("第三方支付记录不存在，平台记录成功状态！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		// 如果第三方状态为成功，平台流水状态为不成功，则认为是错账，记录为失败
		if (PAY_STATE_SUCCESS == thirdTransState
				&& !(finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT)) {
			logger.info("流水标识: {}, 第三方支付记录成功: {}, 平台记录非成功状态: {}, 即对账失败", finPaymentRecord.getFlowId(), thirdTransState,
					finPaymentRecord.getState());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			newFinPaymentRecord.setReconciliationDesc("第三方支付记录成功，平台记录非成功状态！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		// 如果第三方支付状态为已退款或失败，平台流水状态为成功，则认为是错账，记录为失败
		if (PAY_STATE_FAIL == thirdTransState && finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT) {
			logger.info("流水标识: {}, 第三方支付记录记录失败: {}, 平台记录状态为成功: {}, 即对账失败", finPaymentRecord.getFlowId(),
					thirdTransState, finPaymentRecord.getState());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			newFinPaymentRecord.setReconciliationDesc("第三方支付记录失败，平台记录成功状态！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		// 如果第三方支付记录状态为已退款或失败，平台流水状态是待审核，修改订单状态为失败，对账记录为成功
		if (PAY_STATE_FAIL == thirdTransState && (finPaymentRecord.getState() == TradeStateConstants.PENDING_PAYMENT)) {
			logger.info("流水标识: {}, 第三方支付记录状态为退款,平台流水状态为待审核,修改订单状态为失败,对账记录为成功", finPaymentRecord.getFlowId(),
					thirdTransState, finPaymentRecord.getState());
			newFinPaymentRecord.setState(TradeStateConstants.TRANSIT_FAIL);
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_SUCCESS);
			newFinPaymentRecord.setReconciliationDesc("第三方支付记录失败，平台记录待审核状态！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		// 如果第三方支付记录状态为已退款或失败，平台流水状态不是已冲正，或划转失败状态，则认为是错账，记录为失败
		if (PAY_STATE_FAIL == thirdTransState && !(finPaymentRecord.getState() == TradeStateConstants.CORRECT_MONEY
				|| finPaymentRecord.getState() == TradeStateConstants.TRANSIT_FAIL)) {
			logger.info("流水标识: {}, 第三方支付记录失败: {}, 平台记录状态不是已冲正或划转失败状态: {}, 即对账失败", finPaymentRecord.getFlowId(),
					thirdTransState, finPaymentRecord.getState());
			newFinPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
			newFinPaymentRecord.setReconciliationDesc("第三方支付记录失败，平台记录未退款状态！");
			newFinPaymentRecord.setReconciliationTime(DateUtils.getCurrentDate());
			return false;
		}
		return true;
	}

	/**
	 * @Description : 字符串中是否包含汉字
	 * @Method_Name : isChinese;
	 * @param str
	 *            校验的字符串
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018年2月2日 上午11:31:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static boolean isChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flag = false;
		if (matcher.find())
			flag = true;
		return flag;
	}

	@Override
	public ResponseEntity<?> findUserPayCheckAccount(Integer tradeType, SystemTypeEnums systemTypeEnums,
			Integer regUserId, Pager pager) {
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TradeStateConstants.ALREADY_PAYMENT);
		paymentRecord.setRegUserId(regUserId);
		paymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_WAIT);
		paymentRecord.setTradeType(tradeType);
		Pager pagerInfo = this.finPaymentRecordDao.findByCondition(paymentRecord, pager);
		List<FinPaymentRecord> paymentRecordList = (List<FinPaymentRecord>) pagerInfo.getData();
		List<FinPaymentRecord> resultList = new ArrayList<FinPaymentRecord>();
		for (FinPaymentRecord finPaymentRecord2 : paymentRecordList) {
			// 根据渠道判断查证接口的参数
			String payStyle = "";// 查证方式
			if (PayChannelEnum.BaoFuProtocol.getChannelNameValue() == finPaymentRecord2.getPayChannel()
					|| PayChannelEnum.BaoFuProtocolB.getChannelNameValue() == finPaymentRecord2.getPayChannel()) {
				if (PayStyleEnum.RECHARGE.getValue() == finPaymentRecord2.getTradeType()) {
					payStyle = PayStyleEnum.typeByValue(finPaymentRecord2.getRechargeSource())
							+ PayStyleEnum.PAYCHECK.getType();
				} else {
					payStyle = PayStyleEnum.WITHDRAW.getType() + PayStyleEnum.PAYCHECK.getType();
				}
			} else {
				payStyle = PayStyleEnum.PAYCHECK.getType();
			}
			// 调用支付查询接口
			String key = systemTypeEnums.getType() + PlatformSourceEnums.PC.getType()
					+ PayChannelEnum.fromChannelCode(finPaymentRecord2.getPayChannel()) + payStyle;
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, systemTypeEnums.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.fromChannelCode(finPaymentRecord2.getPayChannel()),
					payStyle);
			ResponseEntity<?> responseEntity = ThirdPaymentUtil.findPayCheck(finPaymentRecord2.getFlowId(),
					finPaymentRecord2.getTradeType(), finPaymentRecord2.getRechargeSource(), finPayConfig,
					finPaymentRecord2.getCreateTime());
			if (responseEntity != null && responseEntity.getResStatus() == Constants.SUCCESS) {
				Map<String, Object> resultMap = (Map<String, Object>) responseEntity.getParams().get("resPayCheck");
				// 处理对账
				FinPaymentRecord finPaymentRecords = reconciliationByInterface(resultMap, finPaymentRecord2);
				if (finPaymentRecords != null) {
					finPaymentRecords.setTransMoney(finPaymentRecord2.getTransMoney());
					finPaymentRecords.setCreateTime(finPaymentRecord2.getCreateTime());
					finPaymentRecords.setCommission(finPaymentRecord2.getCommission());
					finPaymentRecords.setPayChannel(finPaymentRecord2.getPayChannel());
					resultList.add(finPaymentRecords);
				} else {
					resultList.add(finPaymentRecord2);
				}
			} else {
				finPaymentRecord2.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_FAIL);
				finPaymentRecord2.setReconciliationDesc(responseEntity.getResMsg().toString());
				resultList.add(finPaymentRecord2);
			}
		}
		pagerInfo.setData(resultList);
		return new ResponseEntity<>(Constants.SUCCESS, pagerInfo);
	}


}
