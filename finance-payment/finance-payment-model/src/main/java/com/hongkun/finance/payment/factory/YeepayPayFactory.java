package com.hongkun.finance.payment.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cfca.util.pki.PKIException;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.cfca.util.pki.cipher.Session;
import com.hongkun.finance.payment.client.yeepay.utils.AES;
import com.hongkun.finance.payment.client.yeepay.utils.CallbackUtils;
import com.hongkun.finance.payment.client.yeepay.utils.Digest;
import com.hongkun.finance.payment.client.yeepay.utils.DigestUtil;
import com.hongkun.finance.payment.client.yeepay.utils.EncryUtil;
import com.hongkun.finance.payment.client.yeepay.utils.HttpUtils;
import com.hongkun.finance.payment.client.yeepay.utils.RSA;
import com.hongkun.finance.payment.client.yeepay.utils.RandomUtil;
import com.hongkun.finance.payment.client.yeepay.utils.XStreamUtil;
import com.hongkun.finance.payment.client.yeepay.vo.TransferItem;
import com.hongkun.finance.payment.client.yeepay.vo.TransferSingleReq;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description : 易宝支付工厂类
 * @Project : finance-payment-modelReqPay
 * @Program Name : com.hongkun.finance.payment.factory.YeepayPayFactory.java
 * @Author : binliang@hongkunjinfu.com.cn 梁彬
 */
public class YeepayPayFactory {
	
private static final Logger logger = LoggerFactory.getLogger(YeepayPayFactory.class);
    
    public static Random random = new Random();
    
    /********************* 查询卡BIN交易类型及子类型 *******************************************/
    /**
     * 预交易， 首次充值 接口地址
     */
    private static final String firstPayRequestURL = "https://jinrong.yeepay.com/tzt-api/api/firstpay/request";
    /**
     * 预交易， 首次充值 短验重发接口地址
     */
    private static final String firstPayResendsmsURL = "https://jinrong.yeepay.com/tzt-api/api/firstpay/resendsms";
    /**
     * 短验确认-下订单 
     */
    private static final String fisrstPayConfirmURL = "https://jinrong.yeepay.com/tzt-api/api/firstpay/confirm";
    /**
     * 查询认证充值订单地址
     */
    private static final String rechargeQueryUrl = "https://jinrong.yeepay.com/tzt-api/api/firstpay/record";
    /**
     * 提现订单结果查询地址
     */
    private static final String rushRecordQueryUrl = "https://jinrong.yeepay.com/tzt-api/api/withdraw/record";
    /**
     * 网银查询地址
     */
    private static final String rechargeWyQueryUrl = "https://cha.yeepay.com/app-merchant-proxy/command";
    /**
     * 支付成功标识信息 success
     */
    public static String SUCCESS_FLAG_NAME = "success";
    
    
    /**
     * @Description : 易宝支付发送短信验证码(首次充值接口 即下订单)
     * @Method_Name : paymentSendSms;
     * @param rechargeCash
     *            充值对象
     * @param payConfig
     *            支付配置信息
     * @return
     * @throws Exception
     * @return : Map<String,Object>;
     * @Creation Date : 2018-09-13 10:40:32;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static Map<String, Object> paymentSendSms(RechargeCash rechargeCash, FinPayConfig payConfig)
            throws Exception {
        logger.info("方法: paymentSendSms, 易宝支付发送短信验证码api, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
                rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
        String resStatus = "";// 结果码
        String resMsg = ""; //返回描述
        String orderId = "";//第三方返回的订单号
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            String merchantAESKey = getMerchantAESKey();
            Map<String, String> result = new HashMap<String, String>();
            TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
            dataMap.put("merchantno", payConfig.getMerchantNo());
            dataMap.put("requestno", formatString(rechargeCash.getPaymentFlowId()));
            dataMap.put("identityid", formatString(rechargeCash.getIdCard()));
            dataMap.put("identitytype", formatString("ID_CARD"));
            dataMap.put("cardno", formatString(rechargeCash.getBankCard()));
            dataMap.put("idcardno", formatString(rechargeCash.getIdCard()));
            dataMap.put("idcardtype", formatString("ID"));
            dataMap.put("username", formatString(rechargeCash.getUserName()));
            dataMap.put("phone", formatString(rechargeCash.getTel()));
            dataMap.put("amount", formatString(rechargeCash.getTransMoney().toString()));
            dataMap.put("callbackurl", formatString(payConfig.getAsyncNoticeUrl()));
            dataMap.put("requesttime", formatString(DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS)));
            dataMap.put("terminalid", formatString(rechargeCash.getLoginTel()));
            dataMap.put("registtime", formatString(DateUtils.format(rechargeCash.getRegisterTime(), DateUtils.DATE_HH_MM_SS)));
            dataMap.put("issetpaypwd", formatString("0"));
            String sign = EncryUtil.handleRSA(dataMap, payConfig.getPrivateKey());
            dataMap.put("sign", sign);
            logger.info("易宝支付发送短信验证码, 用户Id: {}, 表单参数: {}",rechargeCash.getUserId(),dataMap);
            
            String jsonStr = JSON.toJSONString(dataMap);
            String data = com.hongkun.finance.payment.client.yeepay.utils.AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey = RSA.encrypt(merchantAESKey, payConfig.getPublicKey());
            Map<String, String> reqDataMap = new HashMap<String, String>();
            reqDataMap.put("merchantno",  payConfig.getMerchantNo());// 版本号
            reqDataMap.put("data", data);// 数据类型
            reqDataMap.put("encryptkey", encryptkey);
            logger.info("易宝支付发送短信验证码, 请求第三方支付接口, 请求报文, 用户Id: {}, reqDataMap: {}", rechargeCash.getUserId(),reqDataMap);
            String resJson = HttpClientUtils.httpsPost(firstPayRequestURL, reqDataMap);
            result = parseHttpResponseBody(resJson, payConfig);
            logger.info("易宝支付发送短信验证码, 请求第三方支付接口, 响应报文, 用户Id: {}, result: {}",rechargeCash.getUserId(), result);
            
            String status = result.get("status");
            orderId = result.get("requestno");
            String errorcode = result.get("errorcode");
            String errormsg = result.get("errormsg");
            //如果状态为TO_VALIDATE：待短验确认 则认为短信下发成功
            if("TO_VALIDATE".equals(status)){
                resStatus = String.valueOf(Constants.SUCCESS);
                resMsg = "发送成功";
                if (!rechargeCash.getPaymentFlowId() .equals(orderId)){
                    logger.error("方法，paymentSendSms, 易宝支付发送短信验证码订单号不一致, flowId: {}, 易宝返回订单号orderId: {} ", rechargeCash.getFlowId(),
                            orderId);
                    resStatus = String.valueOf(Constants.ERROR);
                    resMsg = "订单流水异常，请稍候重试";
                }
            }else{
                if("TZ1001050".equals(errorcode)){
                    resMsg = "超出验证码获取次数";
                }else{
                    resMsg = errormsg;
                }
                resStatus = String.valueOf(Constants.ERROR);
            }
        }catch(Exception e){
           logger.error("易宝支付发送短信验证码异常, 用户标识: {}, 异常信息: {}",rechargeCash.getUserId(),e); 
           resStatus = String.valueOf(Constants.ERROR);
           resMsg = "发送失败,请稍候再试";
        }
        resultMap.put("resStatus", resStatus);
        resultMap.put("resMsg", resMsg);
        resultMap.put("flowId", orderId);
        resultMap.put("paymentFlowId", orderId);
        logger.info("方法: paymentSendSms, 易宝支付发送短信验证码api 返回 resultMap: {}", resultMap);
        return resultMap;
    }
    
    /**
     * @Description : 易宝支付短验重发
     * @Method_Name : paymentResendSms;
     * @param rechargeCash
     *            充值对象
     * @param payConfig
     *            支付配置信息
     * @return
     * @throws Exception
     * @return : Map<String,Object>;
     * @Creation Date : 2018-09-13 17:40:32;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static Map<String, Object> paymentResendSms(RechargeCash rechargeCash, FinPayConfig payConfig){
        logger.info("方法: paymentResendSms, 易宝支付短信重发api, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
                rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
        String resStatus = "";// 结果码
        String resMsg = ""; //返回描述
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
            dataMap.put("merchantno", payConfig.getMerchantNo());
            dataMap.put("requestno",    rechargeCash.getPaymentFlowId());
            dataMap.put("sign", EncryUtil.handleRSA(dataMap, payConfig.getPrivateKey()));
            logger.info("易宝支付短信重发, 请求参数, 用户Id: {}, dataMap : {} ",rechargeCash.getUserId(), dataMap);
           
            //组装请求第三方接口参数
            String merchantAESKey = getMerchantAESKey();
            String jsonStr = JSON.toJSONString(dataMap);
            String data = AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey = RSA.encrypt(merchantAESKey, payConfig.getPublicKey());
            Map<String, String> reqDataMap = new HashMap<String, String>();
            reqDataMap.put("merchantno",  payConfig.getMerchantNo());// 版本号
            reqDataMap.put("data", data);// 数据类型
            reqDataMap.put("encryptkey", encryptkey);
            logger.info("易宝支付短信重发, 请求第三方接口, 请求报文, 用户Id: {}, reqDataMap: {}",rechargeCash.getUserId(),reqDataMap);
            String resJson = HttpClientUtils.httpsPost(firstPayResendsmsURL, reqDataMap);
            Map<String, String> result = parseHttpResponseBody(resJson, payConfig);
            logger.info("易宝支付短信重发, 请求第三方接口, 响应报文, 用户Id: {}, result: {}",rechargeCash.getUserId(),result);
            
            String status = result.get("status");//订单状态
            String orderId = result.get("requestno");//第三方返回的订单流水号
            //String errorcode = result.get("errorcode");
            String errormsg = result.get("errormsg");//第三方返回的描述信息
            //如果状态为TO_VALIDATE：待短验确认 则认为短信下发成功
            if("TO_VALIDATE".equals(status)){
                resStatus = String.valueOf(Constants.SUCCESS);
                resMsg = "重发短信成功";
                if (!rechargeCash.getFlowId().equals(orderId)){
                    logger.error("方法，paymentSendSms, 易宝支付重发短信验证码订单号不一致，flowId: {}, 易宝返回订单号orderId: {} ", rechargeCash.getFlowId(),
                            orderId);
                    resStatus = String.valueOf(Constants.ERROR);
                    resMsg = "订单流水异常，请稍候重试";
                }
            }else{
                resStatus = String.valueOf(Constants.ERROR);
                resMsg = errormsg;
            }
            
        } catch (Exception e) {
            logger.error("易宝支付短信重发, 请求第三方接口异常, 用户Id: {}, 异常信息: {}",rechargeCash.getUserId(), e);
            resStatus = String.valueOf(Constants.ERROR);
            resMsg = "发送失败,请稍候再试";
        }
        resultMap.put("resStatus", resStatus);
        resultMap.put("resMsg", resMsg);
        logger.info("方法: paymentResendSms, 易宝支付短验重发api 返回 resultMap: {}", resultMap);
        return resultMap;
        
    }
    /**
     * @Description : 易宝支付确认支付
     * @Method_Name : paymentConfirmPay;
     * @param rechargeCash
     *            充值对象
     * @param payConfig
     *            支付配置信息
     * @return
     * @throws Exception
     * @return : Map<String,Object>;
     * @Creation Date : 2018-09-13 17:40:32;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static Map<String, Object> paymentConfirmPay(RechargeCash rechargeCash, FinPayConfig payConfig){
        logger.info("方法: paymentConfirmPay, 易宝支付确认支付api, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
                rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resStatus = "";// 结果码
        String resMsg = ""; //返回描述
        try {
            String merchantAESKey = getMerchantAESKey();
            TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
            dataMap.put("merchantno", payConfig.getMerchantNo());
            dataMap.put("requestno", rechargeCash.getPaymentFlowId());
            dataMap.put("validatecode", rechargeCash.getVerificationCode());
            dataMap.put("sign", EncryUtil.handleRSA(dataMap, payConfig.getPrivateKey()));
            logger.info("易宝支付确认支付, 用户Id: {}, 请求参数dataMap : {} ", rechargeCash.getUserId(),dataMap);
            
            String jsonStr = JSON.toJSONString(dataMap);
            String data = AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey = RSA.encrypt(merchantAESKey, payConfig.getPublicKey());
            Map<String, String> reqDataMap = new HashMap<String, String>();
            reqDataMap.put("merchantno",  payConfig.getMerchantNo());// 版本号
            reqDataMap.put("data", data);// 数据类型
            reqDataMap.put("encryptkey", encryptkey);
            logger.info("易宝支付确认支付, 请求第三方接口, 用户Id: {}, 请求报文, reqDataMap: {}",rechargeCash.getUserId(),reqDataMap);
            String resJson = HttpClientUtils.httpsPost(fisrstPayConfirmURL, reqDataMap);
            Map<String, String> result = parseHttpResponseBody(resJson, payConfig);
            logger.info("易宝支付确认支付, 请求第三方接口, 用户Id: {}, 响应报文, result: {}",rechargeCash.getUserId(),result);
            
            String status = result.get("status");
            String orderId = result.get("requestno");
            String amount = result.get("amount");
            String errormsg = result.get("errormsg");
            if ("FAIL".equals(status)){
                resStatus = String.valueOf(Constants.ERROR);
                resMsg = errormsg;
            } else if ("PROCESSING".equals(status)){
                resStatus = String.valueOf(Constants.SUCCESS);
                resultMap.put("amount",amount);
                resMsg = "支付处理中";
            } else if ("PAY_FAIL".equals(status) || "TO_VALIDATE".equals(status)){
                resStatus = String.valueOf(Constants.ERROR);
                resMsg = errormsg;
            }else{
                resStatus = String.valueOf(Constants.ERROR);
                resMsg = errormsg;
            }
            resultMap.put("flowId", orderId);
        } catch(Exception e) {
            resStatus = String.valueOf(Constants.ERROR);
            resMsg = "支付失败";
            logger.error("易宝支付确认支付, 用户Id: {} , 异常描述: {}",rechargeCash.getUserId(),e);
        } 
        resultMap.put("resStatus", resStatus);
        resultMap.put("resMsg", resMsg);
        logger.info("方法: paymentConfirmPay, 易宝支付确认下订单 返回 resultMap: {}", resultMap);
        return resultMap;
    }
    /**
     * @Description : 判断走网银还是认证（易宝仅用于网银）
     * @Method_Name : paymentPcInfo;
     * @param rechargeCash
     *            充值对象
     * @param finAccount
     *            账户
     * @param payConfig
     *            支付配置文件对象
     * @return
     * @return : Map<String,Object>;
     * @Creation Date : 2018年10月23日 下午2:06:56;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static Map<String, Object> paymentPcInfo(RechargeCash rechargeCash, FinAccount finAccount,
            FinPayConfig payConfig) throws Exception {
        // 1、判断是否是网银支付
        if (PayStyleEnum.WY.getType().equals(rechargeCash.getPayStyle()) || 
                PayStyleEnum.QYWY.getType().equals(rechargeCash.getPayStyle()) ) {
            // 易宝网银支付组装支付报文
            return paymentWyPCInfo(rechargeCash, finAccount, payConfig);
        }
        return null;
    }
    /**
     * @Description : 组装易宝网银支付请求报文
     * @Method_Name : paymentWyPCInfo;
     * @param rechargeCash
     *            充值对象
     * @param finAccount
     *            账户
     * @param payConfig
     *            支付配置文件对象
     * @return
     * @return : Map<String,Object>;
     * @Creation Date : 2018年10月19日 下午2:12:18;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    private static Map<String, Object> paymentWyPCInfo(RechargeCash rechargeCash, FinAccount finAccount,
            FinPayConfig payConfig) throws Exception {
        logger.info("方法: paymentWyPCInfo, 构造易宝网银支付的充值对象, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
                rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
        // 宝付网银支付，加签名字段
        Map<String, Object> reqDataMap = new LinkedHashMap<String, Object>();
        try{
            String p0_Cmd = formatString("Buy");
            String p1_MerId = payConfig.getMerchantNo();
            String p2_Order = formatString(rechargeCash.getPaymentFlowId());
            String p3_Amt = formatString(String.valueOf(rechargeCash.getTransMoney()));
            String p4_Cur = formatString("CNY");
            String p8_Url = formatString(payConfig.getBackUrl()); //页面通知地址，后台地址由商户后台控制
            String pb_ServerNotifyUrl = formatString(payConfig.getAsyncNoticeUrl());// 和p8_Url 一样是  回调地址，服务器异步地址
            String pd_FrpId = formatString(rechargeCash.getBankCode());
            String keyValue = payConfig.getPrivateKey(); // 网银的密钥和其他产品不一样
            String[] strArr = new String[] {p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, 
                    p8_Url,pb_ServerNotifyUrl,pd_FrpId};
            
            String hmac = DigestUtil.getHmac(strArr, keyValue);
            String hmac_safe=  DigestUtil.getHmac_safe(strArr,keyValue);
            hmac_safe  = URLEncoder.encode(hmac_safe,"UTF-8");
            logger.info("构造易宝网银支付的充值对象, 充值流水标识: {}, 网银充值渠道: {}, 充值MD5加密前报文: {}", rechargeCash.getPaymentFlowId(),
                    rechargeCash.getPlatformSourceName(), reqDataMap.toString());
            // 组装支付报文
            Map<String, Object> reqCommMap = new HashMap<String, Object>();
            reqCommMap.put("p0_Cmd", formatString("Buy"));
            reqCommMap.put("p1_MerId", p1_MerId);
            reqCommMap.put("p2_Order", p2_Order);
            reqCommMap.put("p3_Amt", p3_Amt);
            reqCommMap.put("p4_Cur", p4_Cur);
            reqCommMap.put("p8_Url", p8_Url);
            reqCommMap.put("pb_ServerNotifyUrl", pb_ServerNotifyUrl);
            reqCommMap.put("pd_FrpId", pd_FrpId);
            reqCommMap.put("hmac", hmac);
            reqCommMap.put("hmac_safe", hmac_safe);
            reqDataMap.putAll(reqCommMap);
            logger.info("构造易宝网银支付的充值对象, 充值流水标识: {}, 网银充值渠道: {}, 充值请求报文: {}", rechargeCash.getPaymentFlowId(),
                    rechargeCash.getPlatformSourceName(), reqDataMap.toString());
        } catch(Exception e) {
            logger.error("构造易宝网银支付的充值对象异常, 充值流水标识: {}, 网银充值渠道: {}",rechargeCash.getPaymentFlowId(),rechargeCash.getPlatformSourceName());
        }
        return reqDataMap;
    }
    
    /**
     * @Description : 查询卡BIN信息
     * @Method_Name : findCardBin;
     * @param cardNo
     *            卡号
     * @param payConfig
     *            支付配置对象
     * @return
     * @return : ResponseEntity<?>;
     * @throws UnsupportedEncodingException
     * @Creation Date : 2018-09-20 15:06:31;
     * @Author : binliang@hongkunjinfu.com 梁彬;
     */
    public static ResponseEntity<?> findCardBin(String cardNo, FinPayConfig payConfig) {
        logger.info("方法: findCardBin, 易宝查询卡BIN, 入参: cardNo: {}, payConfig: {}", cardNo, payConfig.toString());
        Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
        ResponseEntity<?> responseEntity = null;
        try {
            String merchantAESKey = getMerchantAESKey();
            TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
            dataMap.put("merchantno", payConfig.getMerchantNo());
            dataMap.put("cardno", cardNo);
            dataMap.put("sign", EncryUtil.handleRSA(dataMap, payConfig.getPrivateKey()));
            logger.info("易宝查询卡BIN, cardNo: {}, 请求参数: dataMap: {}",cardNo,dataMap);
            
            String jsonStr = JSON.toJSONString(dataMap);
            String data = AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey = RSA.encrypt(merchantAESKey, payConfig.getPublicKey());
            Map<String, String> reqDataMap = new HashMap<String, String>();
            reqDataMap.put("merchantno",  payConfig.getMerchantNo());// 版本号
            reqDataMap.put("data", data);// 数据类型
            reqDataMap.put("encryptkey", encryptkey);
            
            logger.info("易宝查询卡BIN, 请求第三方接口, cardNo: {}, 请求报文, reqDataMap: {}",cardNo,reqDataMap);
            String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqDataMap);
            Map<String, String> result = parseHttpResponseBody(resJson, payConfig);
            logger.info("易易宝查询卡BIN, 请求第三方接口, cardNo: {}, 响应报文, result: {}",cardNo,result);
            
            String isvalid = result.get("isvalid");
            if ("INVALID".equals(isvalid)){  // 无效银行卡号
                return new ResponseEntity<>(Constants.ERROR, "无效银行卡号");
            }
            if (StringUtils.isNotBlank(result.get("errorcode"))){  //错误信息
                return new ResponseEntity<>(Constants.ERROR, result.get("errormsg"));
            }
            responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
            Map<String, Object> resultCardBinMap = new HashMap<String, Object>();
            resultCardBinMap.put("bankName", result.get("bankname"));
            resultCardBinMap.put("bankCode", result.get("bankcode"));
            resultPay.put("cardBin", resultCardBinMap);
            responseEntity.setParams(resultPay);
        } catch(Exception e) {
            logger.error("易宝查询卡BIN, 银行卡号标识: {}, 易宝银行卡卡bin查询异常信息: ", cardNo, e);
            responseEntity = new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持！");
        } 
        return responseEntity ;
    }
    
    /**
     /**
     * @Description :易宝提现接口
     * @param : finCityRefer
     * @param : finBankCard
     * @param : withDrawCashInfo
     * @param : finPaymentRecord
     * @param : payConfig
     * @return : ResponseEntity<?>;
     * @Creation Date : 2018年10月15日 下午1:41:32;
     * @Author : binliang@hongkun.com.cn 梁彬;
     * @return
     * @throws Exception
     */
    public static ResponseEntity<?> withDrawPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
            WithDrawCash cashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
        logger.info(
                "方法: withDrawPay, 易宝提现, 用户标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}",
                finPaymentRecord.getRegUserId(), finCityRefer.toString(), finBankCard.toString(),
                cashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
        Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
        ResponseEntity<?> responseEntity = null;
        
        TransferSingleReq tsreq  = new TransferSingleReq();
        tsreq.setCmd("TransferSingle");
        tsreq.setVersion("1.1");
        tsreq.setGroup_Id(payConfig.getMerchantNo());
        tsreq.setMer_Id(payConfig.getMerchantNo());
        tsreq.setBatch_No(finPaymentRecord.getFlowId().substring(3,finPaymentRecord.getFlowId().length()));
//      tsreq.setBank_Code("");
        tsreq.setBank_Name(finBankCard.getBankName());
        tsreq.setAmount(finPaymentRecord.getTransMoney().toString());
        tsreq.setOrder_Id(finPaymentRecord.getFlowId());
        tsreq.setBranch_Bank_Name(finBankCard.getBranchName());
        if(cashInfo.getUserType() == PaymentConstants.BUSINESS_USER){
            tsreq.setAccount_Type("pu");
        }else{
            tsreq.setAccount_Type("pr");
        }
        tsreq.setFee_Type("SOURCE");
        tsreq.setUrgency("1");
        
        Map<String, Object> result = signAndCallHttp(tsreq , payConfig);
        resultPay.put("ret_code",(String) result.get("ret_code"));
        resultPay.put("ret_msg",(String) result.get("ret_msg"));
        logger.info("易宝提现支付withDrawPay resultPay: {}", resultPay);
        responseEntity =new ResponseEntity<>(Constants.SUCCESS);
        responseEntity.setParams(resultPay);
        return responseEntity;
    }
    
    private static Map<String,Object> signAndCallHttp(TransferSingleReq tsreq, FinPayConfig finPayConfig) throws PKIException{
        Map<String, Object> mapResult=new HashMap<String, Object>();
        
        /*** 因为 密钥、证书路径、密码 字段较多，超出数据库的字段个数，， 故  易宝提现密钥等 和 数据库字段名 不一一对应***/
        Map xmlBackMap = new LinkedHashMap();
        //第一步:将请求的数据和商户自己的密钥拼成一个字符串,
        StringBuffer hmacStr = new StringBuffer();
        if(tsreq.getCmd().equals("BatchDetailQuery")){
            //按 顺 序 将 cmd 、 mer_Id 、 batch_No 、order_Id 、page_No 的参数值+商户密钥组 成字符串，并采用商户证书进行签名
            hmacStr.append(tsreq.getCmd()).append(tsreq.getMer_Id()).append(tsreq.getBatch_No()).append(tsreq.getOrder_Id())
            .append(tsreq.getPage_No()).append(finPayConfig.getPrivateKey());
        }
        if(tsreq.getCmd().equals("TransferSingle")){
            //按顺序将cmd，mer_Id,batch_No，order_Id,amount,account_Number 参数值+商户密钥组成字符串，并采用商户证书进行签名
            hmacStr.append(tsreq.getCmd()).append(tsreq.getMer_Id()).append(tsreq.getBatch_No()).append(tsreq.getOrder_Id())
            .append(tsreq.getAmount()).append(tsreq.getAccount_Number()).append(finPayConfig.getPrivateKey());
        }
        logger.info("易宝签名之前的数据为: {}" , hmacStr.toString());
        
        //进行数字签名
        Session tempsession = null;
        String ALGORITHM = SignatureUtil.SHA1_RSA;
        JCrypto jcrypto =null;
        if(tempsession==null){
            try {
                //初始化加密库，获得会话session
                //多线程的应用可以共享一个session,不需要重复,只需初始化一次
                //初始化加密库并获得session。
                //系统退出后要jcrypto.finalize()，释放加密库
                jcrypto = JCrypto.getInstance();
                jcrypto.initialize(JCrypto.JSOFT_LIB, null);
                tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
            } catch (Exception ex) {
                logger.error("易宝加密出错:{}",ex);
                mapResult.put("ret_code", "1111");
                mapResult.put("ret_msg", "易宝加密出错");
                return mapResult;
            }
        }
        /*** 因为 密钥、证书路径、密码 字段较多，超出数据库的字段个数，， 故  易宝提现密钥等 和 数据库字段名 不一一对应***/
        String keyStorePath = finPayConfig.getIpAddress();
        String keyStorePassword = finPayConfig.getPayMd5Key();
        String cerStorePath = keyStorePath;//经测试和demo上看，用同一个证书
        String cerStorePassword = keyStorePassword;
        JKey jkey = KeyUtil.getPriKey(keyStorePath, keyStorePassword);
        X509Cert cert = CertUtil.getCert(cerStorePath, cerStorePassword);
        logger.info("cer.subject=={}",cert.getSubject());
        X509Cert[] cs=new X509Cert[1];
        cs[0]=cert;
        String signMessage ="";
        SignatureUtil signUtil =null;
        try{
            // 第二步:对请求的串进行MD5对数据进行签名
            String yphs = Digest.hmacSign(hmacStr.toString());
            signUtil = new SignatureUtil();
            byte[] b64SignData;
            // 第三步:对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
            b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, tempsession);
             if(jcrypto!=null){
                 jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
             }
            signMessage = new String(b64SignData, "UTF-8");
        }catch(Exception e){
            logger.error("易宝数字证书签名出现异常:{}",e);
            mapResult.put("ret_code", "1111");
            mapResult.put("ret_msg", "易宝数字证书签名出现异常");
            return mapResult;
        }
        logger.info("经过md5和数字证书签名之后的数据为---||{}||",signMessage); 
        
        tsreq.setHmac(signMessage);
        
        String bean2XmlString =XStreamUtil.beanToXml(tsreq);
        
        logger.info("易宝提现完整的请求报文为：{}",bean2XmlString);
        
        //第四步:发送https请求
        String responseMsg = CallbackUtils.httpRequest(finPayConfig.getPayUrl(), bean2XmlString, "POST", "gbk","text/xml ;charset=gbk", false);
        logger.info("提交返回报文为---||{}||" , responseMsg);
        tsreq = (TransferSingleReq) XStreamUtil.xmlToBean(responseMsg);
        
        String hmacValue = tsreq.getHmac();
        
        //第五步:对服务器响应报文进行验证签名
        boolean sigerCertFlag = false;
        if(hmacValue!=null && !hmacValue.isEmpty()){
            sigerCertFlag = signUtil.p7VerifySignMessage(hmacValue.getBytes(), tempsession);
            String backmd5hmac = xmlBackMap.get("hmac") + "";
            if(sigerCertFlag){
                logger.info("易宝证书验签成功");
                backmd5hmac = new String(signUtil.getSignedContent());
                logger.info("证书验签获得的MD5签名数据为----{}" , backmd5hmac);
                logger.info("证书验签获得的证书dn为----{}" , new String(signUtil.getSigerCert()[0].getSubject()));
                //第六步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
                
                StringBuffer backHmacStr = new StringBuffer();
                                
                if(tsreq.getCmd().equals("BatchDetailQuery")){
                    //按 顺序 将 cmd、ret_Code、batch_No、total_Num、end_Flag 的参数值+商户密钥组成字符串，并采用易宝支付证书进行签名。
                    backHmacStr.append(tsreq.getCmd()).append(tsreq.getRet_Code()).append(tsreq.getBatch_No())
                    .append(tsreq.getTotal_Num()).append(tsreq.getEnd_Flag()).append(finPayConfig.getPrivateKey());
                }
                if(tsreq.getCmd().equals("TransferSingle")){
                    //按顺序将 cmd、ret_Code、r1_Code 字段值+商户密钥组成字符串，并采用易宝支付证书进行签名。
                    backHmacStr.append(tsreq.getCmd()).append(tsreq.getRet_Code()).append(tsreq.getR1_Code()).append(finPayConfig.getPrivateKey());
                }
                
                String newmd5hmac = Digest.hmacSign(backHmacStr.toString());
                logger.info("提交返回源数据为---||{}||" , backHmacStr.toString());
                logger.info("经过md5签名后的验证返回hmac为---||{}||" , newmd5hmac);
                logger.info("提交返回的hmac为---||{}||" , backmd5hmac);
                if(newmd5hmac.equals(backmd5hmac)){
                    logger.info("md5验签成功");
                    //第七步:判断该证书DN是否为易宝
                    if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
                        logger.info("证书DN是易宝的");
                        //第八步:加上业务逻辑
                        if(tsreq.getRet_Code().equals("1")){//如果返回码为1，则请求成功，我们认为易宝已经受理

                            mapResult.put("ret_code", "0000");
                            mapResult.put("ret_msg", "提现已经请求第三方易宝进行处理!");
                            mapResult.put("transfer", tsreq);
                            return mapResult;
                        }else{
                            mapResult.put("ret_code", tsreq.getRet_Code());
                            mapResult.put("ret_msg", tsreq.getError_Msg());
                            return mapResult;
                        }
                        
                    }else{
                        logger.error("证书DN不是易宝的");
                        mapResult.put("ret_code", "1111");
                        mapResult.put("ret_msg", "证书DN不是易宝的");
                        return mapResult;
                    }
                    
                }else{
                    logger.error("md5验签失败");
                    mapResult.put("ret_code", "1111");
                    mapResult.put("ret_msg", "易宝md5验签失败");
                    return mapResult;
                }
            }else{
                logger.error("证书验签失败....");
                mapResult.put("ret_code", "1111");
                mapResult.put("ret_msg", "易宝证书研签失败");
                return mapResult;
            }
        }else{
            mapResult.put("ret_code", tsreq.getRet_Code());
            mapResult.put("ret_msg", tsreq.getError_Msg());
            return mapResult;
        }
    }
    
    
    /**
     * @Description : 易宝支付查证接口
     * @Method_Name : payCheck;
     * @param flowId
     *            支付流水ID
     * @param tradeType
     *            交易类型
     * @param rechargeSource
     *            充值来源
     * @param payConfig
     *            支付配置信息
     * @param createTime
     *            订单生成时间
     * @return
     * @return : ResponseEntity<?>;
     * @throws Exception
     * @Creation Date : 2018-10-29 10:39:37;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static ResponseEntity<?> payCheck(String flowId, Integer tradeType, Integer rechargeSource,
            FinPayConfig payConfig, Date createTime) throws Exception {
        logger.info("方法: payCheck, 易宝支付查证, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, payConfig: {}", flowId,
                tradeType, rechargeSource, payConfig.toString());
        // 判断交易类型是充值，还是提现操作
        if (PayStyleEnum.RECHARGE.getValue() == tradeType) {
            if (rechargeSource == PayStyleEnum.WY.getValue()) {
                return rechargeWyPayCheck(flowId, payConfig);
            } else if (rechargeSource == PayStyleEnum.RZ.getValue()) {
                return rechargeRzPayCheck(flowId, payConfig);
            } else {
                logger.info("易宝支付查证, 查证流水标识: {}, 暂无此支付查证方式: {}", flowId, rechargeSource);
            }
        } else if (PayStyleEnum.WITHDRAW.getValue() == tradeType) {
            return withDrawPayCheck(flowId, payConfig);
        } else {
            logger.info("易宝支付查证, 查证流水标识: {}, 暂无此交易类型: {}", flowId, tradeType);
        }
        return null;
    }
    
    /**
     * @Description : 易宝认证充值订单结果查询
     * @Method_Name : rechargeRzPayCheck;
     * @param flowId
     *          订单号
     * @param finPayConfig
     *          支付配置信息
     * @return : ResponseEntity<?>;
     * @throws Exception
     * @Creation Date : 2018-10-29 11:06:26;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static ResponseEntity<?> rechargeRzPayCheck(String flowId, FinPayConfig finPayConfig){
        logger.info("方法: rechargeRzPayCheck, 易宝认证支付查证, 入参: flowId: {}, payConfig: {}", flowId,finPayConfig.toString());
        Map<String , Object> resultMap = new HashMap<String , Object>();
        String orderId = "";// 第三方返回的订单号
        String status = ""; //第三方返回的状态码
        String errormsg = ""; //错误描述信息
        int state = 9; //订单结果转化为外围所需要的结果：0、支付成功 5、支付失败2、银行支付处理中 3、等待支付、4订单不存在     9订单异常
        String amount = "0";//交易金额
        try {
            String merchantno = finPayConfig.getMerchantNo();
            String merchantPrivateKey = finPayConfig.getPrivateKey();
            String merchantAESKey = getMerchantAESKey();
            String yeepayPublicKey = finPayConfig.getPublicKey();
            TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
            dataMap.put("merchantno", merchantno);
            dataMap.put("requestno", flowId);
            dataMap.put("yborderid", "");
            dataMap.put("sign", EncryUtil.handleRSA(dataMap, merchantPrivateKey));
            logger.info("易宝认证支付查证, 流水标识: flowId: {}, 请求参数: dataMap: {}", flowId,dataMap);
            //组装请求第三方接口的参数信息
            String jsonStr = JSON.toJSONString(dataMap);
            String data = AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptKey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
            Map<String,String> reqDataMap = new HashMap<String,String>();
            reqDataMap.put("merchantno", URLEncoder.encode(merchantno, "UTF-8"));
            reqDataMap.put("data", URLEncoder.encode(data, "UTF-8"));
            reqDataMap.put("encryptkey", URLEncoder.encode(encryptKey, "UTF-8"));
            logger.info("易宝认证支付查证, 流水标识: flowId: {}, 请求报文: reqDataMap: {}",flowId,reqDataMap);
            String resJson = HttpClientUtils.httpGet(rechargeQueryUrl, reqDataMap);
            Map<String, String> result = parseHttpResponseBody(resJson, finPayConfig);
            logger.info("易宝认证支付查证, 流水标识: flowId: {}, 响应报文: result: {}",flowId,result);
          
            amount = result.get("amount");//交易金额
            errormsg = result.get("errormsg");//错误描述信息
            status = result.get("status");//状态 
            orderId = result.get("requestno");  // 返回单号
            if("FAIL".equals(status)){
                state = PaymentConstants.PAY_STATE_FAIL;//支付失败
            }else if("ACCEPT".equals(status)||"PROCESSING".equals(status)){
                state = PaymentConstants.PAY_STATE_PROCESSING;//交易处理中
            }else if("PAY_SUCCESS".equals(status)){
                state = PaymentConstants.PAY_STATE_SUCCESS;//支付成功
            }else if("TIME_OUT".equals(status) || "PAY_FAIL".equals(status)){
                state = PaymentConstants.PAY_STATE_FAIL;//支付失败
            }else if("TO_VALIDATE".equals(status)){
                state = PaymentConstants.PAY_STATE_WAIT;//等待支付
            }else{
                logger.info("易宝充值订单查询返回状态异常, 第三方返回订单号: {}, 描述: {}", orderId , errormsg);
                return new ResponseEntity<>(Constants.ERROR, "查证异常: " +status + "|" + errormsg);
            }
        } catch (Exception e) {
            logger.info("易宝充值订单查询异常, 第三方返回订单号：{}, 异常描述: {}", orderId , e);
            return new ResponseEntity<>(Constants.ERROR,"订单查询异常");
        }
        resultMap.put("transState", String.valueOf(state));
        resultMap.put("orderId", orderId);
        resultMap.put("transMoney", amount);
        logger.info("易宝认证支付查证, 流水标识: flowId: {}, 返回结果: resultMap: {}", flowId,resultMap);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME)
                .addParam("resPayCheck", resultMap);
        return responseEntity;
    }
    
    /**
     * @Description : 易宝网银充值订单结果查询
     * @Method_Name : rechargeWyPayCheck;
     * @param flowId
     *          订单号
     * @param finPayConfig
     *          支付配置信息
     * @return : ResponseEntity<?>;
     * @throws Exception
     * @Creation Date : 2018-10-29 14:06:26;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static ResponseEntity<?> rechargeWyPayCheck(String flowId, FinPayConfig finPayConfig){
        logger.info("方法: rechargeWyPayCheck, 易宝网银支付查证, 入参: flowId: {}, payConfig: {}", flowId,finPayConfig.toString());
        ResponseEntity<?> responseEntity = null;
        StringBuilder sb = new StringBuilder();
        String tagStr=",";
        String r1_Code = "";//第三方返回的响应码
        String r3_Amt = "";//交易金额
        String r6_Order = "";//第三方返回的订单号
        String rb_PayStatus = "";//第三方返回的支付结果
        String hmacFromYeepay = "";
        String hmac_safeFromYeepay = "";
        try {
            //组装请求第三方接口报文
            String p0_Cmd = "QueryOrdDetail";
            String p1_MerId = finPayConfig.getMerchantNo();
            String p2_Order = flowId;
            String keyValue = finPayConfig.getPayMd5Key(); 
            String pv_Ver = "3.0";
            String p3_ServiceType = "2";
            String[] strArr = {p0_Cmd, p1_MerId, p2_Order, pv_Ver, p3_ServiceType};
            String hmac = DigestUtil.getHmac(strArr, keyValue);
            String hmac_safe = DigestUtil.getHmac_safe(strArr, keyValue);
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("p0_Cmd", p0_Cmd);
            queryParams.put("p1_MerId", p1_MerId);
            queryParams.put("p2_Order", p2_Order);
            queryParams.put("pv_Ver", pv_Ver);
            queryParams.put("p3_ServiceType", p3_ServiceType);
            queryParams.put("hmac_safe", hmac_safe);
            queryParams.put("hmac", hmac);
            
            logger.info("易宝网银支付查证, 流水标识: flowId: {}, 请求报文: queryParams: {}",flowId,queryParams);
            List responseList = HttpUtils.URLGet(rechargeWyQueryUrl, queryParams);
            logger.info("易宝网银支付查证, 流水标识: flowId: {}, 响应报文: responseList: {}",flowId,responseList);
            if(responseList == null) {
                return new ResponseEntity<>(Constants.ERROR,"易宝查询接口返回参数为空!"); 
            } else {
                Iterator iter   = responseList.iterator();
                while(iter.hasNext()) {
                    String temp = formatString((String)iter.next());
                    if(temp.equals("")) {
                        continue;
                    }
                    int i = temp.indexOf("=");
                    int j = temp.length();
                    if(i >= 0) {
                        String tempKey = temp.substring(0, i);
                        String tempValue = temp.substring(i+1, j);
                        if("r0_Cmd".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("r1_Code".equals(tempKey)) {
                            r1_Code = tempValue;
                            sb.append(tempValue).append(tagStr);
                        } else if("r2_TrxId".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("r3_Amt".equals(tempKey)) {
                            r3_Amt = tempValue;
                            sb.append(tempValue).append(tagStr);
                        } else if("r4_Cur".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("r5_Pid".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("r6_Order".equals(tempKey)) {
                            r6_Order = tempValue;
                            sb.append(tempValue).append(tagStr);
                        } else if("r8_MP".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("rw_RefundRequestID".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("rx_CreateTime".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("ry_FinshTime".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("rz_RefundAmount".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("rb_PayStatus".equals(tempKey)) {
                            rb_PayStatus = tempValue;
                            sb.append(tempValue).append(tagStr);
                        } else if("rc_RefundCount".equals(tempKey)) {
                            sb.append(tempValue).append(tagStr);
                        } else if("rd_RefundAmt".equals(tempKey)) {
                            sb.append(tempValue);
                        } else if("hmac".equals(tempKey)) {
                            hmacFromYeepay = tempValue;
                        } else if("hmac_safe".equals(tempKey)){
                            hmac_safeFromYeepay = tempValue;
                        }
                    }
                }
                
                String[] stringArr  = sb.toString().split(tagStr);
                String localHmac    = DigestUtil.getHmac(stringArr, keyValue);
                boolean ishmac_safe = EncryUtil.verifyCallbackHmac_safe_wy(p1_MerId,stringArr, hmac_safeFromYeepay);
                if(!localHmac.equals(hmacFromYeepay) || !ishmac_safe) {
                    StringBuffer temp = new StringBuffer();
                    for(int i = 0; i < stringArr.length; i++) {
                        temp.append(stringArr[i]);
                    }
                }
                Map<String, String> resultMap = new HashMap<String, String>();
                //订单结果转化为外围所需要的结果：0、支付成功 5、支付失败2、银行支付处理中 3、等待支付、4订单不存在     9订单异常
                String ret_msg = "待查询结果";
                int state = 9;
                BigDecimal amount=BigDecimal.ZERO;
                if("50".equals(r1_Code)){ //1：查询正常；50：订单不存在.
                    state = PaymentConstants.PAY_STATE_NO_ORDER;
                    ret_msg ="订单不存在";
                }
                if("INIT".equals(rb_PayStatus)||"CANCELED".equals(rb_PayStatus)){//对于充值来讲，未支付就是失败
                    state = PaymentConstants.PAY_STATE_WAIT;
                    ret_msg = "易宝网银是未支付、已取消状态";
                }else if("SUCCESS".equals(rb_PayStatus)){
                    state = PaymentConstants.PAY_STATE_SUCCESS;
                    ret_msg = "易宝网银已支付";
                }
                if(StringUtils.isNotBlank(r3_Amt)){
                    amount=new BigDecimal(r3_Amt);
                }
                resultMap.put("transState", String.valueOf(state));
                resultMap.put("orderId", r6_Order);
                resultMap.put("transMoney", amount.toString());
                resultMap.put("ret_msg", ret_msg);
                logger.info("查询支付订单 rechargeRzPayCheck resultMap: {}", resultMap);
                responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME)
                        .addParam("resPayCheck", resultMap);
                return responseEntity;
            }
        } catch(Exception e) {
            logger.error("易宝网银支付查证, 流水标识: {}, 订单查询结果异常: {}",flowId,e);
            return new ResponseEntity<>(Constants.ERROR,"易宝查询异常"); 
        }
    }
    
    /**
     * @Description : 易宝提现订单结果查询
     * @Method_Name : withDrawPayCheck;
     * @param flowId
     *          订单号
     * @param finPayConfig
     *          支付配置信息
     * @return : ResponseEntity<?>;
     * @throws Exception
     * @Creation Date : 2018-10-30 14:54:55;
     * @Author : binliang@hongkun.com.cn 梁彬;
     */
    public static ResponseEntity<?> withDrawPayCheck(String flowId, FinPayConfig finPayConfig){
        ResponseEntity<?> responseEntity = null;
        finPayConfig.setPayUrl(rushRecordQueryUrl);
        logger.info("方法: withDrawPayCheck, 易宝提现支付查证, 入参: flowId: {}, finPayConfig: {}", flowId,finPayConfig.toString());
        TransferSingleReq tsreq  = new TransferSingleReq();
        tsreq.setCmd("BatchDetailQuery");
        tsreq.setVersion("1.0");
        tsreq.setGroup_Id(finPayConfig.getMerchantNo());
        tsreq.setMer_Id(finPayConfig.getMerchantNo());
        tsreq.setQuery_Mode("1");
        tsreq.setBatch_No(flowId.substring(2,22));
        tsreq.setOrder_Id(flowId);
        tsreq.setPage_No("1");
        BigDecimal amount = BigDecimal.ZERO;
        
        Map<String, String> resultMap = new HashMap<String, String>();
        String order_Id = "";
        String ret_msg = "";
        int state=3;//经过处理后的 ，返回给外围接口状态 0、支付成功 5、支付失败2、银行支付处理中 3、等待支付、4订单不存在     9订单异常
        try {
            Map<String, Object> returnMap = signAndCallHttp(tsreq,finPayConfig);
            
            if(returnMap.get("ret_code").equals("0000")){
                //打款状态码 0025:已接收, 0026: 已汇出,  0027: 已退款, 0028: 已拒绝, 0029:待复核 ,0030:未知
                TransferSingleReq  tres = (TransferSingleReq) returnMap.get("transfer");
                List<TransferItem> items = tres.getList().getItems();
                if(items == null || items.size()==0){
                    state = PaymentConstants.PAY_STATE_NO_ORDER;
                    ret_msg="提现没有匹配到任何结果状态";
                }else{
                    TransferItem  item = items.get(0);//目前都是单独调用，每个批次都是单个订单
                    if(StringUtils.isNotBlank(item.getReal_pay_amount())){
                        amount = new BigDecimal(item.getReal_pay_amount());// 取订单实际支付款
                    }
                    String r1_code = item.getR1_Code();
                    if(r1_code.equals("0025")||r1_code.equals("0029") ||r1_code.equals("0030")){
                        state = PaymentConstants.PAY_STATE_PROCESSING;
                        ret_msg="易宝处理中";
                    }
                    if(r1_code.equals("0026")){
                        state = PaymentConstants.PAY_STATE_SUCCESS;
                        ret_msg="提现成功";
                    }
                    if(r1_code.equals("0027")||r1_code.equals("0028")){
                        state = PaymentConstants.PAY_STATE_FAIL;
                        ret_msg="提现失败";
                    }
                }
                order_Id = returnMap.get("order_Id").toString();
            }else{
                return new ResponseEntity<>(Constants.ERROR,"易宝查询订单异常"); 
            }
        } catch (Exception e) {
            logger.error("易宝查询订单异常 , flowId: {}, 异常信息 : {}", flowId, e);;
            return new ResponseEntity<>(Constants.ERROR,"易宝查询订单异常"); 
        }
        
        resultMap.put("transState", String.valueOf(state));
        resultMap.put("orderId", order_Id);
        resultMap.put("transMoney", amount == null ? "0":amount.toString());
        resultMap.put("ret_msg", ret_msg);
        logger.info("查询支付订单 withDrawPayCheck resultMap: {}", resultMap);
        responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME)
                .addParam("resPayCheck", resultMap);
        return responseEntity;
    }
    
    /**
     /**
     * @Description :易宝充值延签并解析参数接口
     * @param : map
     * @param : finPayConfig
     * @return : ResponseEntity<?>;
     * @Creation Date : 2018-10-31 10:35:26;
     * @Author : binliang@hongkun.com.cn 梁彬;
     * @return
     * @throws Exception
     */
    public static ResponseEntity<?> checkSignRechargeRz(Map<String,String> map, FinPayConfig finPayConfig){
        logger.info(
                "方法: checkSignRechargeRz, 易宝验签并解析参数,入参: map: {}, finPayConfig: {}" , map , finPayConfig);
        ResponseEntity<?> responseEntity = null;
        Map<String, Object> callbackResult  = new HashMap<String, Object>(); //解析后的参数
        try {
            boolean signMatch = EncryUtil.checkDecryptAndSign(map.get("data"), map.get("encryptkey"),
                    finPayConfig.getPublicKey(), finPayConfig.getPrivateKey());
            if(!signMatch) {
                logger.info("易宝认证支付验签失败");
                return new ResponseEntity<>(Constants.ERROR,"易宝认证支付验签失败");
            }
            String yeepayAESKey = RSA.decrypt(map.get("encryptkey"), finPayConfig.getPrivateKey());
            String decryptData  = AES.decryptFromBase64(map.get("data"), yeepayAESKey);
            callbackResult      = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, Object>>() {});
        } catch (Exception e) {
            logger.error("易宝认证支付验签异常, 异常信息: {}", e.getMessage());
            return new ResponseEntity<>(Constants.ERROR,"易宝认证支付验签异常");
        }
        responseEntity = new ResponseEntity<>(Constants.SUCCESS);
        responseEntity.setParams(callbackResult);
        return responseEntity;
    }
    
    /**
     /**
     * @Description :易宝网银回调验证hmac
     * @param : map
     * @param : finPayConfig
     * @return : ResponseEntity<?>;
     * @Creation Date : 2018-10-31 14:47:31;
     * @Author : binliang@hongkun.com.cn 梁彬;
     * @return
     * @throws Exception
     */
    public static ResponseEntity<?> checkHmacRechargeWy(String[] strArr,String hmac,String hmac_safe, FinPayConfig finPayConfig){
        logger.info(
                "方法: checkHmacRechargeWy, 易宝网银验hmac,入参: strArr: {}, hmac: {} , hmac_safe: {} , finPayConfig: {}" , strArr , hmac , hmac_safe, finPayConfig);
        ResponseEntity<?> responseEntity = null;
        try {
            // 验证hmac
            Boolean verifyMac = EncryUtil.verifyCallbackHmac(finPayConfig.getPayMd5Key(), strArr, hmac);
            if (!verifyMac) {
                logger.info("checkHmacRechargeWy  易宝网银 验证 Mac 失败");
                return new ResponseEntity<>(Constants.ERROR, "验hmac失败,请检查");
            }
            //验证hmac_safe
            Boolean verifyHmacSafe = EncryUtil.verifyCallbackHmac_safe(finPayConfig.getPayMd5Key(), strArr, hmac_safe);
            if (!verifyHmacSafe) {
                logger.info("checkHmacRechargeWy  易宝网银 验证 hmac 失败");
                return new ResponseEntity<>(Constants.ERROR, "验hmac失败,请检查");
            } 
        } catch (Exception e) {
            logger.info("checkHmacRechargeWy  易宝网银 验证 hmac 异常");
            return new ResponseEntity<>(Constants.ERROR, "验hmac异常,请检查");
        }
        responseEntity = new ResponseEntity<>(Constants.SUCCESS);
        return responseEntity;
    }
    
    
    /**
     /**
     * @Description :易宝提现回调验签并解析参数
     * @param : map
     * @param : finPayConfig
     * @return : ResponseEntity<?>;
     * @Creation Date : 2018-10-31 14:47:31;
     * @Author : binliang@hongkun.com.cn 梁彬;
     * @return
     * @throws Exception
     */
    public static ResponseEntity<?> checkSignRush(String paramStr, FinPayConfig finPayConfig){
        logger.info(
                "方法: checkHmacRechargeWy, 易宝网银验hmac,入参: paramStr: {}, finPayConfig: {}" , paramStr, finPayConfig);
        ResponseEntity<?> responseEntity = null;
        TransferSingleReq res = new TransferSingleReq(); //需要回显的数据
        res.setCmd("TransferNotify");
        
        TransferSingleReq req = (TransferSingleReq) XStreamUtil.xmlToBean(paramStr);
        String cmdValue = req.getHmac();
        res.setMer_Id(req.getMer_Id());
        res.setBatch_No(req.getBatch_No());
        res.setOrder_Id(req.getOrder_Id());
        
        //String keyStorePath = finPayConfig.getPayMd5Key(); // 表字段不够，用空余字段  PayMd5Key 存放证书 pfx路径地址
        //String keyStorePassword = finPayConfig.getTerminalId();// 表字段不够，用空余字段 terminalId 存放 password 
        String cerStorePath = finPayConfig.getPayMd5Key();
        String cerStorePassword = finPayConfig.getTerminalId();
        
        //对服务器响应报文进行验证签名
        com.cfca.util.pki.cipher.Session tempsession = null;
        //String ALGORITHM = SignatureUtil.SHA1_RSA;
        JCrypto jcrypto =null;
        try {
            if(tempsession==null){
                //初始化加密库，获得会话session
                //多线程的应用可以共享一个session,不需要重复,只需初始化一次
                //初始化加密库并获得session。
                //系统退出后要jcrypto.finalize()，释放加密库
                jcrypto = JCrypto.getInstance();
                jcrypto.initialize(JCrypto.JSOFT_LIB, null);
                tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
            }
            
            //JKey jkey = KeyUtil.getPriKey(keyStorePath, keyStorePassword);
            X509Cert cert = CertUtil.getCert(cerStorePath, cerStorePassword);
            
            X509Cert[] cs=new X509Cert[1];
            cs[0]=cert;
            boolean sigerCertFlag = false;
            SignatureUtil signUtil = new SignatureUtil();
            
            if(cmdValue!=null){
                sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), tempsession);
                String backmd5hmac = "";
                if(sigerCertFlag){
                    logger.info("证书验签成功");
                    backmd5hmac = new String(signUtil.getSignedContent());
                    backmd5hmac = new String(signUtil.getSignedContent());
                    logger.info("证书验签获得的MD5签名数据为: {}" , backmd5hmac);
                    logger.info("证书验签获得的证书dn为: {}" , new String(signUtil.getSigerCert()[0].getSubject()));
                    //将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
                    StringBuffer backHmacStr = new StringBuffer();
                    backHmacStr.append(req.getCmd()).append(req.getMer_Id()).append(req.getBatch_No())
                    .append(req.getOrder_Id()).append(req.getStatus()).append(req.getMessage()).append(finPayConfig.getPrivateKey());
                    
                    String newmd5hmac = Digest.hmacSign(backHmacStr.toString());
                
                    logger.info("提交返回源数据为---||{}||" , backHmacStr);
                    logger.info("经过md5签名后的验证返回hmac为---||{}||" , newmd5hmac);
                    logger.info("提交返回的hmac为---||{}||" , backmd5hmac);
                    
                    if(newmd5hmac.equals(backmd5hmac)){
                        logger.info("md5验签成功");
                        if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
                            logger.info("证书DN是易宝的");
                            responseEntity = new ResponseEntity<>(Constants.SUCCESS).addParam("transferSingleReq", req);
                            return responseEntity;
                        }else{
                            logger.info("易宝提现证书DN不是易宝的");
                            return new ResponseEntity<>(Constants.ERROR,"易宝提现证书DN不是易宝的");
                        }
                    }else{
                        logger.info("易宝提现md5验签失败");
                        return new ResponseEntity<>(Constants.ERROR,"易宝提现md5验签失败");
                    }
                    
                }else{
                    logger.info("易宝提现证书验签失败");
                    return new ResponseEntity<>(Constants.ERROR,"易宝提现证书验签失败");
                }
            }else {
                logger.info("易宝提现参数为空");
                return new ResponseEntity<>(Constants.ERROR,"易宝提现参数为空");
            }
        } catch (Exception e) {
            logger.info("checkHmacRechargeWy  易宝网银 验证 hmac 异常");
            return new ResponseEntity<>(Constants.ERROR, "验hmac异常,请检查");
        }
    }
    
    
    /**
     * 取得商户AESKey
     */
    public static String getMerchantAESKey() {
        return (RandomUtil.getRandom(16));
    }
    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }
    public static String getRandom(int length) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length; i++) {
            boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
            if (isChar) { // 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                ret.append((char) (choice + random.nextInt(26)));
            } else { // 数字
                ret.append(Integer.toString(random.nextInt(10)));
            }
        }
        return ret.toString();
    }
    /**
     *  @Description    : 解析支付响应报文信息
     *  @Method_Name    : parseHttpResponseBody;
     *  @param responseBody 响应报文
     *  @param finPayConfig 支付配置信息
     *  @return
     *  @throws Exception
     *  @return         : Map<String,String>;
     *  @Creation Date  : 2018年12月28日 下午5:59:42;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public static Map<String, String> parseHttpResponseBody(String responseBody,FinPayConfig finPayConfig) throws Exception {
        logger.info("parseHttpResponseBody, 易宝支付,请求第三方接口响应报文解析, responseBody: {}, finPayConfig: {}",responseBody,finPayConfig);
        //判断请求第三方接口是否正常返回
        Map<String, String> result = new HashMap<String, String>();
        if(PaymentConstants.ERROR_FLAG_NAME.equals(responseBody)) {
            logger.error("易宝支付,请求第三方接口异常:",responseBody);
            result.put("customError", "请求第三方接口失败");
            return result;
        }
        Map<String, String> jsonMap = JSON.parseObject(responseBody, new TypeReference<TreeMap<String, String>>() {});
        if(jsonMap.containsKey("errorcode")) {
            logger.error("易宝支付,请求第三方接口异常, errorcode: {}",jsonMap);
            return jsonMap;
        }
        //验证签名
        String merchantPrivateKey = finPayConfig.getPrivateKey();
        String yeepayPublicKey = finPayConfig.getPublicKey();
        String dataFromYeepay = formatString(jsonMap.get("data"));
        String encryptkeyFromYeepay = formatString(jsonMap.get("encryptkey"));
        boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, yeepayPublicKey, merchantPrivateKey);
        if(!signMatch) {
            logger.error("易宝支付,请求第三方支付接口,返回响应报文,验证签名失败");
            result.put("customError", "请求第三方接口验签失败");
            return result;
        }
        //解密响应报文
        String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
        String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
        result  = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});
        logger.info("parseHttpResponseBody, 易宝支付,请求第三方支付接口响应报文:", result);
        return result;
    }
	
}
