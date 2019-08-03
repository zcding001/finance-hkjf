package com.hongkun.finance.fund.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.constants.FundConstants;
import com.hongkun.finance.fund.facade.FundAdvisoryFacade;
import com.hongkun.finance.fund.model.*;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.hongkun.finance.fund.service.*;
import com.hongkun.finance.fund.util.PdfOperateUtil;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_ADVISORY_SOURCE_INPUT;

@Service
public class FundAdvisoryFacadeImpl implements FundAdvisoryFacade {
    private static final Logger logger = LoggerFactory.getLogger(FundAdvisoryFacadeImpl.class);

    @Autowired
    private FundAdvisoryService fundAdvisoryService;

    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private RegUserService regUserService;

    @Reference
    private FundInfoService fundInfoService;

    @Reference
    private FundAgreementService fundAgreementService;

    @Reference
    private FundUserInfoService fundUserInfoService;

    @Reference
    private HouseProInfoService houseProInfoService;

    @Override
    public Pager findFundAdvisoryList(FundAdvisoryVo advisoryVo, Pager pager) {
        Pager result = fundAdvisoryService.findFundAdvisoryListWithCondition(advisoryVo, pager);
        if (!(result == null || result.getData() == null || result.getData().size() == 0)) {
            result.getData().stream().forEach(tmp -> {
                FundAdvisoryVo voTemp = (FundAdvisoryVo) tmp;
                RegUserDetail regUserDetail = null;
                if(voTemp.getModifyUserId() != null){
                    regUserDetail = this.regUserDetailService
                            .findRegUserDetailByRegUserId(voTemp.getModifyUserId());
                }
                if (regUserDetail != null) {
                    voTemp.setModifyUserName(regUserDetail.getRealName());
                }
                String infoIds = voTemp.getInfoIds();
                String infoNames = "";
                FundInfo fundInfo = null;

                if (voTemp.getProjectParentType() == FundConstants.FUND_PROJECT_PARENT_TYPE_HOUSE ){
                    //房产投资   设置项目名称
                    if (!StringUtils.isEmpty(infoIds)){
                        HouseProInfo  proInfo = houseProInfoService.findHouseProInfoById(Integer.parseInt(infoIds));
                        if (proInfo!=null){
                            voTemp.setProjectNames(proInfo.getName());
                        }
                    }
                    return ;
                }

                //私募信托基金存在预约多个产品情况，遍历查询产品名称
                if(!StringUtils.isEmpty(infoIds)){
                    if(voTemp.getProjectParentType() == FundConstants.FUND_PROJECT_PARENT_TYPE_ABROAD){
                        fundInfo = this.fundInfoService.findFundInfoById(Integer.valueOf(infoIds));
                        if(null != fundInfo){
                            voTemp.setProjectNames(fundInfo.getName());
                        }
                    }else{
                        String[] idArr = infoIds.split(",");
                        for(String id : idArr){
                            fundInfo = this.fundInfoService.findFundInfoById(Integer.valueOf(id));
                            if(!StringUtils.isEmpty(id) && null != fundInfo){
                                //私募基金判断项目名称是否为空，为空则取项目类型名
                                if(voTemp.getProjectParentType() == FundConstants.FUND_PROJECT_PARENT_TYPE_PRIVATE ){
                                    if(StringUtils.isEmpty(fundInfo.getName())){
                                        if(fundInfo.getProjectId() == FundConstants.FUND_PROJECT_TYPE_STAR_UNICORN){
                                            fundInfo.setName("明星独角兽系列");
                                        }else if (fundInfo.getProjectId() == FundConstants.FUND_PROJECT_TYPE_PRE_IPO){
                                            fundInfo.setName("PRE-IPO系列");
                                        }else if(fundInfo.getProjectId() == FundConstants.FUND_PROJECT_TYPE_INDUSTRIAL_SYNERGY){
                                            fundInfo.setName("产业协同系列");
                                        }else if(fundInfo.getProjectId() == FundConstants.FUND_PROJECT_TYPE_LAND){
                                            fundInfo.setName("地产基金");
                                        }
                                    }
                                }
                                if("".equals(infoNames)){
                                    infoNames = fundInfo.getName();
                                }else {
                                    infoNames += ("," + fundInfo.getName());
                                }
                            }
                        }
                        voTemp.setProjectNames(infoNames);
                    }
                }

                // 添加海外基金预约信息
                if(voTemp.getProjectParentType() == FundConstants.FUND_PROJECT_PARENT_TYPE_ABROAD && null != fundInfo){
                    FundAgreement agreement = new FundAgreement();
                    agreement.setRegUserId(voTemp.getRegUserId());
                    agreement.setFundInfoId(fundInfo.getId());
                    agreement.setFundAdvisoryId(voTemp.getId());
                    FundAgreement agreementInfo = fundAgreementService.findFundAgreementInfo(agreement);
                    voTemp.setAuditState(null!= agreementInfo ? agreementInfo.getState() : FundConstants.FUND_ADVISORY_AUDIT_STATE_HISTORY);
                    if(voTemp.getAuditState() == FundConstants.FUND_ADVISORY_AUDIT_STATE_HISTORY){
                        voTemp.setReason("请重新填写认购协议进行线上预约。");
                    }else {
                        voTemp.setReason(agreementInfo.getReason() );
                    }
                    if(null != agreementInfo){
                        voTemp.setFundAgreementId(agreementInfo.getId());
                        voTemp.setInvestAmount(agreementInfo.getInvestAmount());
                        //设置协议地址用于我的预约记录下载pdf
                        if(voTemp.getAuditState() == FundConstants.FUND_ADVISORY_STATE_INMONEY_PASS){
                            voTemp.setContractUrl(agreementInfo.getContractUrl());
                        }
                        //判断自动拒绝autoRejection 用于后台显示备注及判断是否显示详情按钮
                        if(!"1".equals(String.valueOf(agreementInfo.getPayFlag()))
                                || agreementInfo.getControlStatementFlag() != 1
                                || !"1".equals(agreementInfo.getInvestStatementFlag())
                                || agreementInfo.getInvestAwareFlag() != 0
                                || agreementInfo.getSignedSwapFlag() != 0
                                || agreementInfo.getProxyFlag() != 0
                                || agreementInfo.getAmericanTaxFlag() != 1){
                            voTemp.setRemark("autoRejection");
                        }
                    }else{
                        voTemp.setFundAgreementId(0);
                        voTemp.setInvestAmount(BigDecimal.ZERO);
                    }
                    voTemp.setMinRate(fundInfo.getMinRate());
                    voTemp.setMaxRate(fundInfo.getMaxRate());
                    voTemp.setTermUnit(fundInfo.getTermUnit());
                    voTemp.setTermValue(fundInfo.getTermValue());
                    voTemp.setLowestAmountUnit(fundInfo.getLowestAmountUnit());
                }
            });
        }
        return result;
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> saveFundAdvisoryInfo(FundAdvisory advisory) {

        if (logger.isInfoEnabled()) {
            logger.info("方法名: saveFundAdvisoryInfo, 保存咨询信息, 咨询信息: {}", advisory);
        }
        try {
            advisory.setSource(FUND_ADVISORY_SOURCE_INPUT);
            fundAdvisoryService.insertFundAdvisory(advisory);

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存咨询信息异常, 咨询信息: {}\n异常信息: ", advisory, e);
            }
            throw new GeneralException("保存咨询信息出错,请重试");
        }
        return ResponseEntity.SUCCESS;
    }



    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void assignFundAdvisoryToSale(FundAdvisory advisory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: assignFundAdvisoryToSale, 咨询信息分配销售, 更新为咨询信息: {}", advisory.toString());
        }
        try {
            int count = fundAdvisoryService.updateFundAdvisoryForSale(advisory);
            FundAdvisory fundAdvisory = fundAdvisoryService.findFundAdvisoryById(advisory.getId());
            FundInfo fundInfo = this.fundInfoService.findFundInfoById(Integer.valueOf(fundAdvisory.getInfoIds().split(",")[0]));
            // 发短信
            if(count > 0){
                /** 销售信息 **/
                RegUserDetail regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(advisory.getAssignUserId());
                RegUser regUser = this.regUserService.findRegUserById(advisory.getAssignUserId());
                if(null != regUser && null != regUserDetail && null != fundInfo ){
                    long tel = regUser.getLogin();
                    String sex = "";
                    if(fundAdvisory.getSex() != null){
                        sex = fundAdvisory.getSex() == 1 ? "先生" : "女士";
                    }
                    String msg = "%s，你好，客户%s%s，%s，在%s预约过%s，请及时联系。";
                    int type = 1;
                    Object[] params = new Object[]{regUserDetail.getRealName(),fundAdvisory.getName(),sex,fundAdvisory.getTel(),
                            DateUtils.format(fundAdvisory.getCreateTime(),"yyyy-MM-dd HH:mm"),fundInfo.getName()};

                    SmsTelMsg smsTelMsg = new SmsTelMsg(tel,msg,type,params);
                    SmsSendUtil.sendTelMsgToQueue(smsTelMsg);
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("咨询信息分配销售异常, 咨询信息: {}, 异常信息: {}", advisory.toString(), e);
            }
            throw new GeneralException("咨询信息分配销售出错,请重试");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void fundAgreementAudit(FundAdvisoryVo advisoryVo) {
        try {
            FundAgreement agreement = new FundAgreement();
            agreement.setId(advisoryVo.getFundAgreementId());
            agreement.setState(advisoryVo.getAuditState());
            agreement.setReason(advisoryVo.getReason());
            //审核为打款成功之前，判断用户pdf协议是否已经生成，没有则先生成pdf协议
            if (advisoryVo.getAuditState() == FundConstants.FUND_ADVISORY_STATE_INMONEY_PASS){
                ResponseEntity generateResult = fundAgreementPdfGenerate(advisoryVo.getFundAgreementId());
                if (generateResult.getResStatus() == Constants.ERROR){
                    throw new GeneralException(generateResult.getResMsg().toString());
                }
            }
            fundAgreementService.updateFundAgreement(agreement);
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("海外基金状态审核异常, 审核信息: {}, 异常信息: {}", advisoryVo.toString(), e);
            }
            throw new GeneralException("海外基金状态审核出错,请重试");
        }
    }

    public ResponseEntity fundAgreementPdfGenerate(Integer id){
        //获取预约记录信息
        FundAgreement fundAgreement = fundAgreementService.findFundAgreementById(id);
        if (fundAgreement == null){
            return new ResponseEntity<>(Constants.ERROR,"海外基金记录还未生成，无法下载!");
        }
        //获取海外基金项目，区分使用那个pdf协议
        FundInfo fundInfo = fundInfoService.findFundInfoById(fundAgreement.getFundInfoId());
        if (fundInfo == null || fundInfo.getContractType() == null){
            return new ResponseEntity<>(Constants.ERROR,"海外基金记录项目不存在或没有配置协议类型，无法下载!");
        }
        fundAgreement.setFundContractType(fundInfo.getContractType());
        //判断该预约记录是否已生成协议信息，没有则先生成，有则直接打包下载
        if (org.apache.commons.lang.StringUtils.isBlank(fundAgreement.getContractUrl())){
            if (org.apache.commons.lang.StringUtils.isBlank(fundAgreement.getSignature())){
                return new ResponseEntity<>(Constants.ERROR,"用户没有签名图片，无法下载!");
            }
            //获取用户身份证号
            RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(fundAgreement.getRegUserId());
            if (org.apache.commons.lang.StringUtils.isBlank(regUserDetail.getIdCard())){
                return new ResponseEntity(Constants.ERROR,"用户没有实名，无法下载!");
            }
            fundAgreement.setIdCard(regUserDetail.getIdCard());
            Map<String,Object> param = PdfOperateUtil.generatePdf(fundAgreement);
            if (param.get("resStatus").equals("1000")){
                String key = param.get("picUrl").toString();
                fundAgreement.setContractUrl(key);
                //更新协议信息
                FundAgreement updateFund = new FundAgreement();
                updateFund.setId(fundAgreement.getId());
                updateFund.setContractUrl(key);
                fundAgreementService.updateFundAgreement(updateFund);
            }else {
                return new ResponseEntity(Constants.ERROR,param.get("resMsg").toString());
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("fundAgreement",fundAgreement);
        return new ResponseEntity<>(Constants.SUCCESS,"",params);
    }
    @Override
    public ResponseEntity fundAgreementDownLoad(Integer id) {
        ResponseEntity generateResult = fundAgreementPdfGenerate(id);
        if (generateResult.getResStatus() == Constants.ERROR){
            return generateResult;
        }
        FundAgreement fundAgreement = (FundAgreement) generateResult.getParams().get("fundAgreement");
        //将用户的身份证正反面图片、护照图片、协议pdf打包下载
        Map<String,String> keyFileNameMap = new HashMap<>(4);
        FundUserInfo condition = new FundUserInfo();
        condition.setRegUserId(fundAgreement.getRegUserId());
        FundUserInfo fundUserInfo = fundUserInfoService.findFundUserInfo(condition);

        if (fundUserInfo == null || org.apache.commons.lang.StringUtils.isBlank(fundUserInfo.getIdUpUrl()) || org.apache.commons.lang.StringUtils.isBlank
                (fundUserInfo.getIdDownUrl()) || org.apache.commons.lang.StringUtils.isBlank(fundUserInfo.getPassportUrl())){
            return new ResponseEntity<>(Constants.ERROR,"用户身份证、护照图片缺失，无法下载!");
        }

        //pdf协议地址
        keyFileNameMap.put(fundAgreement.getContractUrl(),fundAgreement.getFundContractType().equals(1) ? "认购法律文件 Class A.pdf" :
                "认购法律文件 Class B.pdf");
        keyFileNameMap.put(fundUserInfo.getIdUpUrl(), "身份证正面.jpg");
        keyFileNameMap.put(fundUserInfo.getIdDownUrl(), "身份证反面.jpg");
        keyFileNameMap.put(fundUserInfo.getPassportUrl(), "本人护照.jpg");

        //zip包在服务端临时位置和名称，下载完成后直接删除
        String zipName = fundAgreement.getUserSurname() + fundAgreement.getUserName() + DateUtils
                .getFormatedTime(new Date()) + ".zip";

        Map<String,Object> params = new HashMap<>(2);
        params.put("zipName",zipName);
        params.put("keyFileNameMap",keyFileNameMap);

        return new ResponseEntity(Constants.SUCCESS,"",params);
    }
}
