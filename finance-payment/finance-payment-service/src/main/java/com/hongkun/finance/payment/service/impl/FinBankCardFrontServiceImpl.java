package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.*;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.service.FinBankCardFrontService;
import com.hongkun.finance.payment.util.ThirdPaymentUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.crypto.MD5;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.redis.JedisClusterUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 银行卡绑定，维护，充值页面展现银行卡服务类
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinBankCardFrontServiceImpl.
 *          java
 * @Author : maruili on 2018/1/26 15:45
 */
@Service
public class FinBankCardFrontServiceImpl implements FinBankCardFrontService {
	private static final Logger logger = LoggerFactory.getLogger(FinBankCardFrontServiceImpl.class);
	@Autowired
	private FinChannelGrantDao finChannelGrantDao;
	@Autowired
	private FinPlatformPaywayDao finPlatformPaywayDao;
	@Autowired
	private FinBankReferDao finBankReferDao;
	@Autowired
	private FinBankCardDao finBankCardDao;
	@Autowired
	private FinBankCardBindingDao finBankCardBindingDao;
	@Autowired
	private FinPayConfigDao finPayConfigDao;
	@Autowired
	private FinAccountDao finAccountDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> toRecharge(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum, Integer regUserId, String basePath, Integer userType) {
		logger.info(
				"方法: toRecharge, 初始化充值页面, 用户标识: {}, 入参: systemTypeEnums: {}, platformSourceEnums: {}, payStyleEnum: {}, regUserId: {}, basePath: {}",
				regUserId, systemTypeEnums, platformSourceEnums, payStyleEnum, regUserId, basePath);
		ResponseEntity<?> resEntity = new ResponseEntity<>(SUCCESS);
		try {
			Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
			// 获取当前系统下的,PC平台,启用的充值渠道
			List<FinChannelGrant> finChannelGrantList = finChannelGrantDao.findFinChannelGrantList(systemTypeEnums,
					platformSourceEnums, payStyleEnum);
			if (finChannelGrantList == null || finChannelGrantList.size() <= 0) {
				logger.error("初始化充值页面, 用户标识: {}, 当前系统暂没有开启充值渠道！", regUserId);
				return new ResponseEntity<>(ERROR, "当前系统暂无充值渠道！");
			}
			// 支付渠道集合
			resultMap.put("payChannelList", finChannelGrantList);
			logger.info("初始化充值页面, 用户标识: {}, 当前开启的支付渠道: {}", regUserId, JsonUtils.toJson(finChannelGrantList));
			// 判断用户类型（企业用户和物业用户统称为企业用户类型，一般用户为普通用户类型）
			String userTypes = String.valueOf(
					(userType == UserConstants.USER_TYPE_ENTERPRISE || userType == UserConstants.USER_TYPE_TENEMENT)
							? UserConstants.USER_TYPE_ENTERPRISE : UserConstants.USER_TYPE_GENERAL);
			// 组装前台支付渠道下的支付方式，以及支付方式对应的银行信息
			for (FinChannelGrant channel : finChannelGrantList) {
				HashMap<String, Object> payCardMaps = (HashMap<String, Object>) payCardIdentification(userTypes,
						PayChannelEnum.getPayChannelEnumByCode(channel.getChannelNameCode()), systemTypeEnums,
						platformSourceEnums);
				if (payCardMaps != null && payCardMaps.size() > 0) {
					resultMap.put(PayChannelEnum.getPayChannelEnumByCode(channel.getChannelNameCode()).getChannelKey(),
							payCardMaps);
				}
			}
			// 查询用户绑定了多少张银行卡，以及银行卡在平台的绑定状态
			Map<String, Object> entityNo = null;// 银行卡号
			Map<String, Object> bankMap = new LinkedHashMap<String, Object>();
			List<FinBankCard> findBankCardList = finBankCardDao.findByRegUserId(regUserId);
			if (findBankCardList != null && findBankCardList.size() > 0) {
				for (FinBankCard bankCard : findBankCardList) {
					if (bankCard != null) {
						// 如果银行卡号，银行CODE不为空，并且状态为已认证状态，则 认为是在第三方认证的状态
						if (StringUtils.isNotBlank(bankCard.getBankCard())
								&& StringUtils.isNotBlank(bankCard.getBankCode())
								&& bankCard.getState() == TradeStateConstants.BANK_CARD_STATE_AUTH) {
							bankMap.put("bankCode", bankCard.getBankCode());
							bankMap.put("tiedCardFlag", TradeStateConstants.BANK_CARD_STATE_AUTH);
						} else if (StringUtils.isNotBlank(bankCard.getBankCard())
								&& StringUtils.isNotBlank(bankCard.getBankCode())
								&& bankCard.getState() == TradeStateConstants.BANK_CARD_STATE_UNAUTH) {
							// 如果银行卡号，银行CODE不为空，并且状态为绑卡未认证状态，则 认为是在第三方未认证的状态
							bankMap.put("bankCode", bankCard.getBankCode());
							bankMap.put("tiedCardFlag", TradeStateConstants.BANK_CARD_STATE_UNAUTH);
						} else {
							// 则认为是未绑卡状态
							bankMap.put("tiedCardFlag", TradeStateConstants.BANK_CARD_STATE_INIT);
							bankMap.put("entityNo", entityNo);
						}
						// 如果银行卡号和银行CODE都不为空，则查询该卡在支付渠道下的支付限额
						if (StringUtils.isNotBlank(bankCard.getBankCard())
								&& StringUtils.isNotBlank(bankCard.getBankCode())) {
							PayStyleEnum payStyle = PayStyleEnum.RZ;
							String bankCode = bankCard.getBankCode();
							for (FinChannelGrant channel : finChannelGrantList) {
								// 判断当前支付渠道下是否有第三方协议号
								FinBankCardBinding bankCardBinding = new FinBankCardBinding();
								bankCardBinding.setFinBankCardId(bankCard.getId());
								bankCardBinding.setRegUserId(regUserId);
								bankCardBinding.setPayChannel(channel.getChannelNameCode());
								FinBankCardBinding finBankCardBinding = finBankCardBindingDao
										.findBankCardBinding(bankCard.getId(), regUserId, channel.getChannelNameCode());
								String channelCode = PayChannelEnum
										.getPayChannelEnumByCode(channel.getChannelNameCode()).getChannelKey();
								if (finBankCardBinding != null) {
									bankMap.put(channelCode + "_thirdAccount", finBankCardBinding.getThirdAccount());
								}
								// 查询该卡在支付渠道下的支付限额
								String key = channelCode + payStyle.getType() + String.valueOf(userType) + bankCode
										+ TradeStateConstants.START_USING_STATE;
								FinBankRefer bankRefer = finBankReferDao.findBankRefer(key, channelCode,
										payStyle.getType(), String.valueOf(userType), bankCode,
										TradeStateConstants.START_USING_STATE, null);
								bankMap.put(channelCode + "_bankRefer", bankRefer);

							}
							bankMap.put("entityNo", bankCardNoEncrypt(bankCard.getBankCard()));
						}
					} else {
						// 认为是未绑卡状态
						bankMap.put("tiedCardFlag", TradeStateConstants.BANK_CARD_STATE_INIT);
						bankMap.put("entityNo", entityNo);
					}
				}
			} else {
				// 认为是未绑卡状态
				bankMap.put("tiedCardFlag", TradeStateConstants.BANK_CARD_STATE_INIT);
				bankMap.put("entityNo", entityNo);
			}
			resultMap.put("bankMap", bankMap);
			resultMap.put("basePath", basePath);
			resEntity.setParams(resultMap);
		} catch (Exception e) {
			logger.error("用户标识: {}, 初始化充值页面失败: ", regUserId, e);
			return new ResponseEntity<>(ERROR, "银行系统维护中，请稍候再试！");
		}
		return resEntity;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> bindBankCard(FinBankCard finBankCard, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, RegUser regUser) throws Exception {
		logger.info("bindBankCard, 绑定银行卡, finBankCard: {}, userId: {}, platformSource: {}, systemType: {},payStyle: {}",
				finBankCard.toString(), regUser.getId(), platformSourceEnums.getValue(), systemTypeEnums.getValue(),
				payStyleEnum.getValue());
		ResponseEntity<?> responseEntity = null;
		try {
			boolean isNeedCarBin = true;
			boolean isAdd = true;
			if (finBankCard.getId() != null) {
				FinBankCard bankCardTemp = finBankCardDao.findByPK(new Long(finBankCard.getId()), FinBankCard.class);
				// 如果银行卡已认证(包括已认证，已认证禁用，未认证禁用（这种状态目前也规定为不能修改））
				// 则不能修改银行卡号，其它可以修改银行卡号
				if (!isAllowUpdateBankCode(bankCardTemp)) {
					finBankCard.setBankCard(null);
					finBankCard.setBankCode(null);
					finBankCard.setBankName(null);
					isNeedCarBin = false;
				}
				isAdd = false;
			} else {
				List<FinBankCard> bankCards = finBankCardDao.findByRegUserId(regUser.getId());
				if (!CollectionUtils.isEmpty(bankCards)) {
					for (FinBankCard bankCard : bankCards) {
						if (bankCard.getState() == TradeStateConstants.BANK_CARD_STATE_AUTH) {
							return new ResponseEntity<>(ERROR, "已绑定银行卡！");
						}
					}
				}
			}

			if (isNeedCarBin) {
				// 查询充值渠道
				List<FinChannelGrant> finChannelGrantList = finChannelGrantDao.findFinChannelGrantList(systemTypeEnums,
						platformSourceEnums, payStyleEnum);
				if (finChannelGrantList == null || finChannelGrantList.size() <= 0) {
					return new ResponseEntity<>(ERROR, "当前系统暂无渠道！");
				}
				// 根据支付渠道调用查询银行卡列表接口
				// 获取当前系统下的,PC平台,启用的充值渠道
				PayChannelEnum payChannelEnum = PayChannelEnum
						.getPayChannelEnumByCode(finChannelGrantList.get(0).getChannelNameCode());
				String key = ThirdPaymentUtil.getFinPayConfigKey(systemTypeEnums, PlatformSourceEnums.PC,
						PayStyleEnum.KBIN, payChannelEnum);
				FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, systemTypeEnums.getType(),
						PlatformSourceEnums.PC.getType(), payChannelEnum.getChannelKey(), PayStyleEnum.KBIN.getType());
				ResponseEntity<?> response = ThirdPaymentUtil
						.findCardBin(finBankCard.getBankCard() == null ? "" : finBankCard.getBankCard(), finPayConfig);
				if (response == null) {
					return new ResponseEntity<>(ERROR, "查询银行卡BIN信息出现异常");
				}
				if (response.getResStatus() == Constants.ERROR) {
					return new ResponseEntity<>(ERROR, response.getResMsg());
				}
				// 查询该通道银行CODE对应的平台银行CODE
				Map<String, Object> resMap = getPlatBankCode(regUser.getType(), response, payChannelEnum);
				if (StringUtils.isBlank(resMap.get("bankCode").toString())) {
					return new ResponseEntity<>(ERROR, "该银行维护中，暂不支持！");
				}
				// 设置银行卡信息
				finBankCard.setBankName(resMap.get("bankName").toString());
				finBankCard.setBankCode(resMap.get("bankCode").toString());

			}
			if (isAdd) {
				finBankCard.setState(TradeStateConstants.BANK_CARD_STATE_UNAUTH);
				finBankCardDao.save(finBankCard);
				responseEntity = new ResponseEntity<>(SUCCESS, "绑定银行卡成功");
			} else {
				finBankCardDao.update(finBankCard);
				responseEntity = new ResponseEntity<>(SUCCESS, "维护银行卡成功");
			}
		} catch (Exception e) {
			logger.error(
					"bindBankCard, 绑定银行卡, finBankCard: {}, userId: {}, platformSource: {}, systemType: {},payStyle: {}",
					finBankCard.toString(), regUser.getId(), platformSourceEnums.getValue(), systemTypeEnums.getValue(),
					payStyleEnum.getValue(), e);
			throw new BusinessException("绑定银行卡出现异常");
		}

		return responseEntity;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateBankCard(FinBankCard finBankCard) throws Exception {
		finBankCardDao.update(finBankCard);
		return new ResponseEntity<>(SUCCESS, "维护银行卡成功");
	}

	/**
	 * @Description : 组装前台支付渠道下的支付方式，以及支付方式对应的银行信息
	 * @Method_Name : payCardIdentification;
	 * @param userType
	 * @param payChannelEnum
	 *            支付渠道
	 * @param systemTypeEnums
	 *            系统名称
	 * @param platformSourceEnums
	 *            平台来源
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年12月21日 上午11:42:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Map<String, Object> payCardIdentification(String userType, PayChannelEnum payChannelEnum,
			SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums) throws Exception {
		// 返回结果的MAP
		Map<String, Object> resultBankCard = new HashMap<String, Object>();
		Integer state = TradeStateConstants.START_USING_STATE;
		// 1、查询支付渠道下有哪几种支付方式 格式Map<支付渠道,支付方式List>
		Map<String, List<FinPlatformPayway>> payStyleMap = new HashMap<String, List<FinPlatformPayway>>();
		String thirdNameCode = payChannelEnum.getChannelKey();
		FinPlatformPayway finPlatformPayway = new FinPlatformPayway();
		finPlatformPayway.setSysNameCode(systemTypeEnums.getType());
		finPlatformPayway.setPlatformName(platformSourceEnums.getType());
		finPlatformPayway.setState(state);
		finPlatformPayway.setThirdNameCode(thirdNameCode);
		finPlatformPayway.setSortColumns("payway_code asc");
		finPlatformPayway.setUserType(userType);
		// 查询某个支付渠道下有哪几种支付方式
		List<FinPlatformPayway> platformPayWayList = finPlatformPaywayDao.findByCondition(finPlatformPayway);
		if (platformPayWayList != null && platformPayWayList.size() >= 0) {
			payStyleMap.put(thirdNameCode, platformPayWayList);
		}
		logger.info("初始化充值页面, 支付渠道下有哪几种支付方式: {}", payStyleMap.toString());
		// 2、查询某支付渠道的支付方式下哪几种银行CODE信息 格式 Map<支付渠道_支付方式，银行卡CODE的LIST>
		Map<String, List<FinBankRefer>> finBankReferMap = new HashMap<String, List<FinBankRefer>>();
		for (String payChannel : payStyleMap.keySet()) {
			List<FinPlatformPayway> payStyleList = payStyleMap.get(payChannel);
			payStyleList.forEach(e -> {
				String payStyleCode = e.getPaywayCode();
				String key = payChannel + payStyleCode + userType + state;
				List<FinBankRefer> finBankReferList = finBankReferDao.findBankInfo(key, payChannel, payStyleCode,
						userType, state);
				if (finBankReferList != null && finBankReferList.size() > 0) {
					finBankReferMap.put(payChannel + "_" + payStyleCode, finBankReferList);
				}
			});
		}
		logger.info("初始化充值页面, 支付渠道下的支付方式包含的银行信息: {}", finBankReferMap.toString());
		// 支付渠道下包含的支付方式
		resultBankCard.put("payStyleMap", payStyleMap);
		// 支付方式下包含的银行卡CODE集合
		resultBankCard.put("finBankReferMap", finBankReferMap);
		return resultBankCard;
	}

	/**
	 * @Description : 卡号隐藏中间部分
	 * @Method_Name : banCardNoEncrypt;
	 * @param bankCardNo
	 *            卡号
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年12月22日 下午2:12:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Map<String, Object> bankCardNoEncrypt(String bankCardNo) {
		Map<String, Object> cardNoMap = new HashMap<String, Object>();
		String cardNoHead = bankCardNo.substring(0, 4);
		String cardNoFoot = bankCardNo.substring(bankCardNo.length() - 4);
		String showCardNo = cardNoHead + "********" + cardNoFoot;
		String index = MD5.encrypt(bankCardNo);
		JedisClusterUtils.set(index, bankCardNo);
		cardNoMap.put(index, showCardNo);
		return cardNoMap;
	}

	@Override
	public ResponseEntity<?> findBankCardBin(String cardNo, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, PayChannelEnum payChannelEnum,
			Integer userType) {
		logger.info(
				"方法: findBankCardBin, 根据银行卡号查询银行卡BIN, 入参: cardNo: {}, systemTypeEnums: {}, platformSourceEnums: {}, payStyleEnum: {}, payChannelEnum: {}",
				cardNo, systemTypeEnums, platformSourceEnums, payStyleEnum, payChannelEnum);
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		try {
			// 查询支付银行卡BIN配置文件
			String key = ThirdPaymentUtil.getFinPayConfigKey(systemTypeEnums, platformSourceEnums, payStyleEnum,
					payChannelEnum);
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, systemTypeEnums.getType(),
					platformSourceEnums.getType(), payChannelEnum.getChannelKey(), payStyleEnum.getType());
			// 查询卡BIN
			ResponseEntity<?> rsponseEntity = ThirdPaymentUtil.findCardBin(cardNo, finPayConfig);
			if (rsponseEntity.getResStatus() != Constants.SUCCESS) {
				logger.error("根据银行卡号查询银行卡BIN, 银行卡号: {}, 调用第三方查询卡BIN接口异常: {}", cardNo,
						rsponseEntity.getResMsg().toString());
				return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
			}
			// 根据第三方银行CODE，转换为平台的银行CODE
			Map<String, Object> resResult = getPlatBankCode(userType, rsponseEntity, payChannelEnum);
			if (StringUtils.isBlank(resResult.get("bankCode").toString())) {
				return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
			}
			result.setParams(resResult);
		} catch (Exception e) {
			logger.error("根据银行卡号查询银行卡BIN, 银行卡号: {}, 查询卡BIN异常: ", cardNo, e);
			return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
		}
		return result;
	}

	/**
	 * @Description : 根据第三方银行CODE，转换为平台的银行CODE
	 * @Method_Name : getPlatBankCode;
	 * @param userType
	 *            用户类型
	 * @param rsponseEntity
	 * @param payChannelEnum
	 *            支付渠道
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月22日 下午2:58:26;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getPlatBankCode(Integer userType, ResponseEntity<?> rsponseEntity,
			PayChannelEnum payChannelEnum) {
		Map<String, Object> bankCardMap = (Map<String, Object>) rsponseEntity.getParams().get("cardBin");
		String bankCode = bankCardMap.get("bankCode").toString();
		// 将查询回来的第三方银行CODE转换为平台的银行CODE返回
		String type = String.valueOf(
				(userType == UserConstants.USER_TYPE_ENTERPRISE || userType == UserConstants.USER_TYPE_TENEMENT)
						? UserConstants.USER_TYPE_ENTERPRISE : UserConstants.USER_TYPE_GENERAL);
		String keytemp = payChannelEnum.getChannelKey() + PayStyleEnum.RZ.getType() + type + bankCode
				+ TradeStateConstants.START_USING_STATE;
		FinBankRefer bankRefer = finBankReferDao.findBankRefer(keytemp, payChannelEnum.getChannelKey(),
				PayStyleEnum.RZ.getType(), String.valueOf(userType), null, TradeStateConstants.START_USING_STATE,
				bankCode);
		bankCardMap.put("bankCode", bankRefer == null ? "" : bankRefer.getBankCode());
		bankCardMap.put("bankName", bankCardMap.get("bankName").toString());
		return bankCardMap;
	}

	@Override
	public ResponseEntity<?> findBingBankCardInfo(PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum,
			Integer regUserId) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		// 查询当前的启动的支付渠道
		List<FinChannelGrant> channelList = this.finChannelGrantDao.findFinChannelGrantList(SystemTypeEnums.HKJF,
				platformSourceEnums, payStyleEnum);
		if (channelList == null || channelList.size() == 0) {
			return new ResponseEntity<>(ERROR, "当前系统暂无充值渠道！");
		}
		// 支付渠道集合
		resultMap.put("payChannelList", channelList);
		// 查询用户持有的银行卡列表
		List<FinBankCard> findBankCardList = finBankCardDao.findByRegUserId(regUserId);
		resultMap.put("bankCardlList", findBankCardList);
		responseEntity.setParams(resultMap);
		return responseEntity;
	}

	@Override
	public FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum) {
		return this.finChannelGrantDao.findFirstFinChannelGrant(systemTypeEnums, platformSourceEnums, payStyleEnum);
	}

	/**
	 * 判断是否允许修改银行卡号
	 * 
	 * @param finBankCard
	 * @return
	 */
	private boolean isAllowUpdateBankCode(FinBankCard finBankCard) {
		// 如果银行卡未认证可以修改银行卡号，其它情况不可以修改
		if (finBankCard.getState() == TradeStateConstants.BANK_CARD_STATE_INIT
				|| finBankCard.getState() == TradeStateConstants.BANK_CARD_STATE_UNAUTH) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<?> findRechargeInfo(Integer regUserId) {
		return new ResponseEntity<>(Constants.SUCCESS).addParam("bankCardList",
				finBankCardDao.findByRegUserId(regUserId));
	}

	@Override
	public ResponseEntity<?> findBankInfoByCardNo(String cardNo, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, PayChannelEnum payChannelEnum,
			Integer userType, Integer regUserId) {
		logger.info(
				"方法: findBankInfoByCardNo, 未绑卡时通过卡号查询银行卡BIN及银行信息, 入参: cardNo: {}, systemTypeEnums: {}, platformSourceEnums: {}, payStyleEnum: {}, payChannelEnum: {}, userType: {}, regUserId: {}",
				cardNo, systemTypeEnums, platformSourceEnums, payStyleEnum, payChannelEnum, userType, regUserId);
		// 1、查询银行卡BIN信息
		ResponseEntity<?> resEntity = findBankCardBin(cardNo, systemTypeEnums, platformSourceEnums, payStyleEnum,
				payChannelEnum, userType);
		if (resEntity.getResStatus() == Constants.ERROR) {
			return resEntity;
		}
		String type = String.valueOf(
				(userType == UserConstants.USER_TYPE_ENTERPRISE || userType == UserConstants.USER_TYPE_TENEMENT)
						? UserConstants.USER_TYPE_ENTERPRISE : UserConstants.USER_TYPE_GENERAL);
		// 2、查询银行限额信息
		String key = payChannelEnum.getChannelKey() + PayStyleEnum.RZ.getType() + type
				+ resEntity.getParams().get("bankCode").toString() + TradeStateConstants.START_USING_STATE;
		FinBankRefer bankRefer = finBankReferDao.findBankRefer(key, payChannelEnum.getChannelKey(),
				PayStyleEnum.RZ.getType(), String.valueOf(userType), resEntity.getParams().get("bankCode").toString(),
				TradeStateConstants.START_USING_STATE, null);
		// 3、查询可用余额
		FinAccount finAccount = finAccountDao.findByRegUserId(regUserId);
		resEntity.addParam("singleLimit", bankRefer == null ? BigDecimal.ZERO : bankRefer.getSingleLimit());// 单笔限额
		resEntity.addParam("singleDayLimit", bankRefer == null ? BigDecimal.ZERO : bankRefer.getSingleDayLimit());// 单日限额
		resEntity.addParam("singleMonthLimit", bankRefer == null ? BigDecimal.ZERO : bankRefer.getSingleMonthLimit());// 单月限额
		resEntity.addParam("bankIconAddress", bankRefer == null ? "" : bankRefer.getBankIconAddress().replace(".jpg", ".png"));// 银行地址
		resEntity.addParam("userAbleMoney", finAccount == null ? BigDecimal.ZERO : finAccount.getUseableMoney());
		return resEntity;
	}

	@Override
	public ResponseEntity<?> findBankInfo(PayChannelEnum payChannelEnum, Integer userType, Integer regUserId) {
		logger.info("方法: findBankInfo, 已绑卡的通过支付渠道获取银行信息, 入参: payChannelEnum: {}, userType: {}, regUserId: {}",
				payChannelEnum, userType, regUserId);
		String type = String.valueOf(
				(userType == UserConstants.USER_TYPE_ENTERPRISE || userType == UserConstants.USER_TYPE_TENEMENT)
						? UserConstants.USER_TYPE_ENTERPRISE : UserConstants.USER_TYPE_GENERAL);
		try {
			List<BankCardVo> bankList = new ArrayList<BankCardVo>();
			// 1、查询银行信息
			List<FinBankCard> findBankCardList = finBankCardDao.findByRegUserId(regUserId);
			for (FinBankCard finBankCard : findBankCardList) {
				if (StringUtils.isNotBlank(finBankCard.getBankCode())) {
					BankCardVo bankCardVo = new BankCardVo();
					// 2、查询银行限额信息
					String key = payChannelEnum.getChannelKey() + PayStyleEnum.RZ.getType() + type
							+ finBankCard.getBankCode() + TradeStateConstants.START_USING_STATE;
					FinBankRefer bankRefer = finBankReferDao.findBankRefer(key, payChannelEnum.getChannelKey(),
							PayStyleEnum.RZ.getType(), String.valueOf(userType), finBankCard.getBankCode(),
							TradeStateConstants.START_USING_STATE, null);
					// 两个对象之间相同Bean属性赋值
					ClassReflection.reflectionAttr(finBankCard, bankCardVo);
					bankCardVo.setFinBankCardId(finBankCard.getId());
					if (bankRefer == null) {
						bankRefer = new FinBankRefer();
						bankRefer.setSingleLimit(BigDecimal.ZERO);
						bankRefer.setSingleDayLimit(BigDecimal.ZERO);
						bankRefer.setSingleMonthLimit(BigDecimal.ZERO);
						bankRefer.setBankIconAddress("src/img/account/" + finBankCard.getBankCode() + ".png");
					}
					ClassReflection.reflectionAttr(bankRefer, bankCardVo);
					bankCardVo.setBankIconAddress(bankCardVo.getBankIconAddress().replace(".jpg", ".png"));
					bankList.add(bankCardVo);
				}
			}
			// 3、查询可用余额
			FinAccount finAccount = finAccountDao.findByRegUserId(regUserId);
			return new ResponseEntity<>(Constants.SUCCESS).addParam("bankList", bankList).addParam("userAbleMoney",
					finAccount == null ? BigDecimal.ZERO : finAccount.getUseableMoney());
		} catch (Exception e) {
			logger.error("已绑卡的通过支付渠道获取银行信息, 用户ID: {}, 失败: ", regUserId, e);
			return new ResponseEntity<>(Constants.ERROR, "系统繁忙请稍候再试");
		}
	}

	@Override
	public ResponseEntity<?> findPayChannel(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum) {
		logger.info(
				"方法: findPayChannel, 查询当前启用的支付渠道, 入参: systemTypeEnums: {}, platformSourceEnums: {}, payStyleEnum: {}",
				systemTypeEnums, platformSourceEnums, payStyleEnum);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
		// 查询当前的启动的支付渠道
		List<FinChannelGrant> channelList = this.finChannelGrantDao.findFinChannelGrantList(systemTypeEnums,
				platformSourceEnums, payStyleEnum);
		if (channelList == null || channelList.size() == 0) {
			return new ResponseEntity<>(ERROR, "当前系统暂无充值渠道！");
		}
		channelList.forEach(channel -> {
			if (PayChannelEnum.BaoFuProtocol.getChannelNameValue() == channel.getChannelNameCode()
					|| PayChannelEnum.BaoFuProtocolB.getChannelNameValue() == channel.getChannelNameCode()) {
				channel.setChannelName("宝付支付");
			}
		});
		logger.info("查询当前启用的支付渠道集合channelList: {}", JsonUtils.toJson(channelList));
		return responseEntity.addParam("payChannelList", channelList);
	}

	@Override
	public ResponseEntity<?> initCashDeshPage(RechargeCash rechargeCash) {
		logger.info("方法: initCashDeshPage, 初始化收银台页面信息, 入参: rechargeCash: {}", rechargeCash.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 1、 认证充值判断用户有没有绑定过卡
			FinBankCard finBankCard = null;
			FinBankRefer bankRefer = null;
			if (rechargeCash.getBankCardId() != null) {
				finBankCard = finBankCardDao.findByPK(Long.valueOf(rechargeCash.getBankCardId()), FinBankCard.class);
			}
			// 2、判断用户是否绑定过卡,如果用户没有绑定过卡，则插入银行信息
			if (finBankCard == null) {
				finBankCard = new FinBankCard();
				ResponseEntity<?> resultRes = dealBankCard(rechargeCash, finBankCard);
				if (resultRes.getResStatus() == Constants.ERROR) {
					return resultRes;
				}
				finBankCard = (FinBankCard) resultRes.getParams().get("bankCard");
				bankRefer = (FinBankRefer) resultRes.getParams().get("bankRefer");
				finBankCard.setRegUserId(rechargeCash.getUserId());
				finBankCardDao.save(finBankCard);
			} else {
				// 校验是否本人卡号
				if (!finBankCard.getRegUserId().equals(rechargeCash.getUserId())) {
					logger.error("初始化收银台页面信息, 卡号ID: {}, 用户ID: {}, 卡号: {} 获取银行卡信息校验失败!", finBankCard.getId(),
							rechargeCash.getUserId(), finBankCard.getBankCard());
					return new ResponseEntity<>(ERROR, "银行卡号加载失败！");
				}
				// 3、如果用户已经绑过卡，但是还没有认证,且用户输入的银行卡和之前绑定的不一致，则可以进行换绑
				if (TradeStateConstants.BANK_CARD_STATE_UNAUTH == finBankCard.getState()
						&& !rechargeCash.getBankCard().equals(finBankCard.getBankCard())) {
					// 如果用户输入的银行卡和之前绑定的不一致，则进行换绑
					FinBankCard bankCard = new FinBankCard();
					bankCard.setId(finBankCard.getId());
					ResponseEntity<?> resultRes = dealBankCard(rechargeCash, bankCard);
					if (resultRes.getResStatus() == Constants.ERROR) {
						return resultRes;
					}
					bankCard = (FinBankCard) resultRes.getParams().get("bankCard");
					bankRefer = (FinBankRefer) resultRes.getParams().get("bankRefer");
					finBankCardDao.update(bankCard);
				} else {
					// 通过平台CODE查询银行限额
					String bankKey = rechargeCash.getPayChannel() + rechargeCash.getPayStyle() + rechargeCash.getuType()
							+ finBankCard.getBankCode() + TradeStateConstants.START_USING_STATE;
					bankRefer = finBankReferDao.findBankRefer(bankKey, rechargeCash.getPayChannel(),
							rechargeCash.getPayStyle(), String.valueOf(rechargeCash.getuType()),
							finBankCard.getBankCode(), TradeStateConstants.START_USING_STATE, null);
					if (bankRefer == null) {
						return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
					}
				}
			}
			FinBankCard newBankCard = finBankCardDao.findByPK(Long.valueOf(finBankCard.getId()), FinBankCard.class);
			// 4、如果单笔限额为0，则提示银行维护中
			if (CompareUtil.eq(bankRefer.getSingleLimit(), BigDecimal.ZERO)) {
				return new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持此银行卡!");
			}
			// 5、组装返回给客户端的信息
			resultMap.put("bankCardId", newBankCard.getId());// 银行卡ID
			resultMap.put("userName", rechargeCash.getUserName());// 用户姓名
			resultMap.put("bankCard", newBankCard.getBankCard());// 银行卡号
			resultMap.put("bankTel", newBankCard.getBankTel());// 银行预留手机号
			resultMap.put("bankName", newBankCard.getBankName());// 银行名称
			resultMap.put("createTime", DateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS));// 创建时间
			resultMap.put("singLimit", bankRefer.getSingleLimit());// 单笔限额
			resultMap.put("singDayLimit", bankRefer.getSingleDayLimit());// 单日限额
			resultMap.put("bankIconAddress", "src/img/account/" + newBankCard.getBankCode() + ".png");// 银行图标
			ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
			result.setParams(resultMap);
			return result;
		} catch (Exception e) {
			logger.error("初始化收银台页面信息失败: ", e);
			return new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持此银行卡!");
		}
	}

	/**
	 * @Description : 处理银行信息
	 * @Method_Name : dealBankCard;
	 * @param rechargeCash
	 *            充值对象
	 * @param finBankCard
	 *            银行卡对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月15日 下午4:01:18;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> dealBankCard(RechargeCash rechargeCash, FinBankCard finBankCard) {
		try {
			// 首次绑卡认证充值，查询银行cardBin接口，校验卡是否存在
			String key = rechargeCash.getSystemTypeName() + rechargeCash.getPlatformSourceName()
					+ rechargeCash.getPayChannel() + PayStyleEnum.KBIN.getType();
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, rechargeCash.getSystemTypeName(),
					rechargeCash.getPlatformSourceName(), rechargeCash.getPayChannel(), PayStyleEnum.KBIN.getType());

			ResponseEntity<?> cardRes = ThirdPaymentUtil.findCardBin(rechargeCash.getBankCard(), finPayConfig);
			if (cardRes == null || cardRes.getResStatus() == ERROR) {
				return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
			}
			Map<String, Object> bankMap = (Map<String, Object>) cardRes.getParams().get("cardBin");
			// 插入用户银行卡信息
			finBankCard.setBankCard(rechargeCash.getBankCard());
			// 根据第三方银行CODE，支付渠道，支付方式，用户类型，银行编码，状态 查询平台银行编码信息
			String bankKey = rechargeCash.getPayChannel() + rechargeCash.getPayStyle() + rechargeCash.getuType()
					+ bankMap.get("bankCode").toString() + TradeStateConstants.START_USING_STATE;
			FinBankRefer bankRefer = finBankReferDao.findBankRefer(bankKey, rechargeCash.getPayChannel(),
					rechargeCash.getPayStyle(), String.valueOf(rechargeCash.getuType()), null,
					TradeStateConstants.START_USING_STATE, bankMap.get("bankCode").toString());
			if (bankRefer == null) {
				return new ResponseEntity<>(ERROR, "银行维护中,暂不支持此银行卡!");
			}
			finBankCard.setBankCode(bankRefer.getBankCode());
			finBankCard.setBankName(bankMap.get("bankName").toString());
			return new ResponseEntity<>(Constants.SUCCESS).addParam("bankCard", finBankCard).addParam("bankRefer",
					bankRefer);
		} catch (Exception e) {
			logger.error("根据卡号获取银行信息异常: ", e);
			return new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持此银行卡!");
		}
	}
}
