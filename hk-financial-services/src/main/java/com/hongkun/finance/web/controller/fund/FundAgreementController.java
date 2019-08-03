package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.FundInfo;
import com.hongkun.finance.fund.model.FundUserInfo;
import com.hongkun.finance.fund.service.FundAgreementService;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.fund.service.FundUserInfoService;
import com.hongkun.finance.fund.util.FundUtils;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_ADVISORY_STATE_INIT;
import static com.hongkun.finance.fund.constants.FundConstants.FUND_ADVISORY_STATE_QULICATION_REJECT;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.yirun.framework.core.commons.Constants.ERROR;

/**
 * @Description : 海外基金签署协议相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.FundAgreementController
 * @Author : yuzegu@hongkun.com.cn googe
 */
@Controller
@RequestMapping("/fundAgreementController")
public class FundAgreementController {

    private static final Logger logger = LoggerFactory.getLogger(FundAgreementController.class);

    @Reference
    RegUserDetailService regUserDetailService;

    @Reference
    private FundInfoService fundInfoService;

    @Reference
    private FundAgreementService fundAgreementService;

    @Reference
    private FundUserInfoService fundUserInfoService;

    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;


    /**
     * 海外基金签约第一步初始化用户最近一次协议记录及页面需要的其他元素
     *
     * @param fundInfoId 预约的基金id
     * @return
     */
    @RequestMapping("/initUserAgreement")
    @ResponseBody
    public ResponseEntity initUserAgreement(Integer fundInfoId) {
        logger.info("getUserAgreement海外基金跳转认购协议页面获取最近一次用户签约记录,股权id:{}", fundInfoId);
        RegUser loginUser = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
        FundInfo fundInfo = fundInfoService.findFundInfoById(fundInfoId);
        if (fundInfo == null) {
            return new ResponseEntity(ERROR, null);
        }
        FundAgreement fundAgreement = new FundAgreement();
        fundAgreement.setRegUserId(loginUser.getId());
        FundAgreement info = fundAgreementService.findFundAgreementInfo(fundAgreement);
        if (info == null) {
            info = new FundAgreement();
        }
        info.setFundInfoId(fundInfoId);
        String birthday = FundUtils.idCardTransferToBir(regUserDetail.getIdCard());
        info.setBirthday(birthday);
        info.setRealName(regUserDetail.getRealName());
        //设置最低起投和步长
        info.setLowestAmount(fundInfo.getLowestAmount());
        info.setStepValue(fundInfo.getStepValue());
        info.setLowestAmountUnit(fundInfo.getLowestAmountUnit());
        return new ResponseEntity(SUCCESS, info);
    }

    /**
     * 海外基金签约第一步上传保存协议信息
     *
     * @param fundAgreement 协议内容
     * @return
     */
    @RequestMapping("/saveUserAgreement")
    @ResponseBody
    public ResponseEntity saveUserAgreement(FundAgreement fundAgreement, @RequestParam(value = "file", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        System.out.println(request.getParameter("investAmount"));
        logger.info("saveUserAgreement海外基金保存用户协议内容,股权id:{}", fundAgreement.getFundInfoId());
        RegUser loginUser = BaseUtil.getLoginUser();
        FundInfo fundInfo = fundInfoService.findFundInfoById(fundAgreement.getFundInfoId());
        if (fundInfo == null) {
            return new ResponseEntity(ERROR, "产品不存在重新选择");
        }
        ResponseEntity successResponse = new ResponseEntity(SUCCESS, "上传成功");
        //判断是否需要重新上传图片，multipartFile不为空
        if (multipartFile != null && multipartFile.getSize() != 0) {
            //上传签名图片  随机命名
            Object resutlt = BaseUtil.uploadFile("hkjf", "image", "/fund/agreement/", multipartFile);
            if (resutlt instanceof ResponseEntity) {
                return ResponseEntity.class.cast(resutlt);
            }
            FileInfo fileInfo = FileInfo.class.cast(resutlt);
            /**
             * 校验文件状态，确定是否上传成功
             */
            if (fileInfo.getFileState() == FileState.SAVED) {
                successResponse.getParams().put("saveKey", fileInfo.getSaveKey());
                fundAgreement.setSignature(fileInfo.getSaveKey());

            } else {
                return new ResponseEntity(201, "上传失败，请重试");
            }
            //重新设置新上传的图片
            //fundAgreement.setSignature(signature);
        }
        /**
         * 检查默认选项
         */
        if (!"1".equals(String.valueOf(fundAgreement.getPayFlag()))) {
            fundAgreement.setState(FUND_ADVISORY_STATE_QULICATION_REJECT);
            fundAgreement.setReason(FundUtils.refuseReson);
        } else {
            fundAgreement.setState(FUND_ADVISORY_STATE_INIT);
            fundAgreement.setReason("");
        }
        //保存协议信息
        fundAgreement.setRegUserId(loginUser.getId());
        int agreementId = fundAgreementService.insertFundAgreement(fundAgreement);
        if (agreementId <= 0) {
            return new ResponseEntity(202, "协议保存失败，请重试");
        }
        successResponse.getParams().put("agreementId", agreementId);
        successResponse.getParams().put("uId", loginUser.getId());
        return successResponse;
    }

    /**
     * 海外基金签约第二步上传更新协议信息 其他选项
     *
     * @param fundAgreement 协议内容
     * @return
     */
    @RequestMapping("/updateUserAgreement")
    @ResponseBody
    public ResponseEntity updateUserAgreement(FundAgreement fundAgreement, HttpServletRequest request) {
        System.out.println(request.getParameter("investAmount"));
        logger.info("updateUserAgreement海外基金保存用户协议内容,协议id:{}", fundAgreement.getId());
        RegUser loginUser = BaseUtil.getLoginUser();
        FundAgreement fa = new FundAgreement();
        fa.setRegUserId(loginUser.getId());
        fa.setId(fundAgreement.getId());
        FundAgreement myFa = fundAgreementService.findFundAgreementInfo(fa);
        if (myFa == null) {
            return new ResponseEntity(ERROR, "协议记录异常请重新选择要预约的产品");
        }
        ResponseEntity successResponse = new ResponseEntity(SUCCESS, "更新成功");
        /**
         * 检查默认项
         */
        if (!"1".equals(String.valueOf(myFa.getPayFlag()))
                || fundAgreement.getControlStatementFlag() != 1
                || !"1".equals(fundAgreement.getInvestStatementFlag())
                || fundAgreement.getInvestAwareFlag() != 0
                || fundAgreement.getSignedSwapFlag() != 0
                || fundAgreement.getProxyFlag() != 0
                || fundAgreement.getAmericanTaxFlag() != 1) {
            //考虑上第一步的选项是否正确
            fundAgreement.setState(FUND_ADVISORY_STATE_QULICATION_REJECT);
            fundAgreement.setReason(FundUtils.refuseReson);
        } else {
            fundAgreement.setState(FUND_ADVISORY_STATE_INIT);
            fundAgreement.setReason("");
        }
        //更新协议信息
        fundAgreement.setRegUserId(loginUser.getId());
        fundAgreementService.updateFundAgreement(fundAgreement);
        return successResponse;
    }

    /**
     * 海外基金签约第三步初始化用户资料信息
     *
     * @return
     */
    @RequestMapping("/initUserData")
    @ResponseBody
    public ResponseEntity initUserData(Integer aId) {
        logger.info("initUserData海外基金第三步获取用户资料,协议id:{}" + aId);
        RegUser loginUser = BaseUtil.getLoginUser();
        FundAgreement fa = new FundAgreement();
        fa.setRegUserId(loginUser.getId());
        fa.setId(aId);
        if (fundAgreementService.findFundAgreementInfo(fa) == null) {
            return new ResponseEntity(ERROR, "协议记录异常请重新选择要预约的产品");
        }
        FundUserInfo fundUserInfo = new FundUserInfo();
        fundUserInfo.setRegUserId(loginUser.getId());
        fundUserInfo.setSortColumns("id desc");
        FundUserInfo info = fundUserInfoService.findFundUserInfo(fundUserInfo);
        if (info == null) {
            info = new FundUserInfo();
        }
        return new ResponseEntity(SUCCESS, info);
    }

    /**
     * 海外基金签约第三步上传用户资料信息
     *
     * @param aId 协议内容id
     * @return
     */
    @RequestMapping("/uploadOrUpdateUserInfo")
    @ResponseBody
    public ResponseEntity uploadOrUpdateUserInfo(@RequestParam Integer aId, @RequestParam(value = "userInfoId", required = false) Integer userInfoId,
                                                 @RequestParam(value = "file", required = false) MultipartFile[] multipartFiles, HttpServletRequest request) {
        logger.info("uploadUserInfo海外基金上传或更新用户资料,协议内容id:{}", aId);
        ResponseEntity successResponse = new ResponseEntity(SUCCESS, "资料长传/更新成功");
        RegUser loginUser = BaseUtil.getLoginUser();
        FundAgreement fa = new FundAgreement();
        fa.setRegUserId(loginUser.getId());
        fa.setId(aId);
        FundAgreement agreement = fundAgreementService.findFundAgreementInfo(fa);
        if (null == agreement) {
            return new ResponseEntity(ERROR, "协议记录异常请重新选择要预约的产品");
        }
        FundUserInfo fundUserInfo = new FundUserInfo();
        /**
         * 遍历上传的多个文件
         */
        if (multipartFiles != null && multipartFiles.length > 0) {
            for (int i = 0; i < multipartFiles.length; i++) {
                if (multipartFiles[i] != null && multipartFiles[i].getSize() != 0) {
                    //上传签名图片  随机命名
                    Object resutlt = BaseUtil.uploadFile("hkjf", "image", "/fund/agreement/", multipartFiles[i]);
                    if (resutlt instanceof ResponseEntity) {
                        return ResponseEntity.class.cast(resutlt);
                    }
                    FileInfo fileInfo = FileInfo.class.cast(resutlt);
                    /**
                     * 校验文件状态，确定是否上传成功
                     */
                    if (fileInfo.getFileState() == FileState.SAVED) {
                        if (i == 0) {
                            fundUserInfo.setIdUpUrl(fileInfo.getSaveKey());
                        }
                        if (i == 1) {
                            fundUserInfo.setIdDownUrl(fileInfo.getSaveKey());
                        }
                        if (i == 2) {
                            fundUserInfo.setPassportUrl(fileInfo.getSaveKey());
                        }
                    } else {
                        return new ResponseEntity(201, "上传用户资料失败，请重试");
                    }
                }
            }
        }

        fundUserInfo.setId(userInfoId);
        RegUserDetail userDetail = regUserDetailService.findRegUserDetailByRegUserId(agreement.getRegUserId());
        fundAgreementService.insertOrUpdateAdvisoryAndFundUserInfo(fundUserInfo, agreement, userDetail);
        return successResponse;
    }

}
