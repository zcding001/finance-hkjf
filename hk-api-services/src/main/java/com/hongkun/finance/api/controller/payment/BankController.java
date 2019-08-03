package com.hongkun.finance.api.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.BankConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinBankCardUpdate;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinBankCardFrontService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.payment.service.FinBankCardUpdateService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.user.constants.UserConstants.*;

/**
 * @Description : 银行卡绑定，维护，查询等接口
 * @Project : finance
 * @Program Name  : com.hongkun.finance.api.controller.payment.BankController.java
 * @Author : maruili on  2018/3/13 14:06
 */
@Controller
@RequestMapping("/bankController")
public class BankController {
    private static final Logger logger = LoggerFactory.getLogger(BankController.class);
    @Reference
    private FinBankCardService finBankCardService;
    @Reference
    private FinBankCardFrontService finBankCardFrontService;
    @Reference
    private FinBankCardUpdateService finBankCardUpdateService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private FinAccountService finAccountService;
    @Reference
    private RegUserDetailService regUserDetailService;

    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;
    /**
     * 查询用户是否绑定了银行卡
     * @return
     */
    @RequestMapping("isBindBank")
    @ResponseBody
    public Map<String,Object> isBindBank() {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        Integer isBind = 0;
        List<FinBankCard> list = finBankCardService.findByRegUserId(regUser.getId());
        if(list!=null && !list.isEmpty()){
            for(FinBankCard vo:list) {
                if (vo.getState() != TradeStateConstants.BANK_CARD_STATE_UNAUTH_FORBIDDEN && vo.getState() != TradeStateConstants.BANK_CARD_STATE_AUTH_FORBIDDEN) {
                    isBind = 1;
                    break;
                }
            }
        }
        return AppResultUtil.successOf(isBind,"查询成功");

    }

    /**
     * 查询用户绑定的银行卡
     * @param state
     * @return
     */
    @RequestMapping("getBankCardList")
    @ResponseBody
    public Map<String,Object> getBankCardList(Integer state) {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        FinBankCard finBankCard = new FinBankCard();
        finBankCard.setRegUserId(regUser.getId());
        if(state != null){
            if (state == 1) { // 查询绑定的未禁用银行卡
                finBankCard.setStateList(Arrays.asList(0, 1, 2));
            } else if (state == 2) { // 查询绑定的禁用银行卡
                finBankCard.setStateList(Arrays.asList(4, 5));
            }
        }
        List<FinBankCard> list = finBankCardService.findByCondition(finBankCard);
        return AppResultUtil.successOfList(list);

    }

    /**
     * 查询银行卡信息
     * @param id
     * @return
     */
    @RequestMapping("getBankInfo")
    @ResponseBody
    public Map<String,Object> getBankInfo(Integer id) {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        FinBankCard  finBankCard = finBankCardService.findById(id);
        if(finBankCard==null){
            return AppResultUtil.errorOfMsg("查询的银行卡不存在");
        }
        if(finBankCard.getRegUserId().compareTo(regUser.getId())==0){
            return AppResultUtil.successOf(finBankCard, "查询成功");
        }else{
            return AppResultUtil.errorOfMsg("没有权限查询该银行卡");
        }

    }
    /**
     * 绑定银行卡信息
     * @param finBankCard
     * @return
     */
    @RequestMapping("bindingCard")
    @ResponseBody
    @ActionLog(msg = "绑定银行卡, 银行卡号：{args[0].bankCard}， 银行编码：{args[0].bankCode}，银行名称：{args[0].bankName}" +
            "，开户行省：{args[0].bankProvince}，开户行市：{args[0].bankCity}，开户支行：{args[0].branchName}" +
            "，平台来源：{args[1]}，系统类型：{args[2]}")
    public Map<String,Object> bindingCard(FinBankCard finBankCard,@RequestParam("platformSource") Integer platformSource,
                                          @RequestParam("systemType")Integer systemType) {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
            return AppResultUtil.errorOfMsg("暂不支持维护银行卡，请联系客服!");
        }
        try {
            finBankCard.setRegUserId(regUser.getId());
            SystemTypeEnums systemTypeEnum = SystemTypeEnums.getSystemTypeEnumsByValue(systemType);
            PlatformSourceEnums platformSourceEnum = PlatformSourceEnums.typeByValue(platformSource);
            ResponseEntity<?>  responseEntity =finBankCardFrontService.bindBankCard(finBankCard, systemTypeEnum, platformSourceEnum, PayStyleEnum.RECHARGE,regUser);
            if(responseEntity.getResStatus()== Constants.SUCCESS){
                return  AppResultUtil.successOfMsg((String) responseEntity.getResMsg());
            }else{
                return  AppResultUtil.errorOfMsg(responseEntity.getResMsg());
            }
        } catch (Exception e) {
            logger.error("用户绑定银行卡出现异常：",e);
            return  AppResultUtil.errorOfMsg("用户绑定银行卡失败");
        }
    }
    /**
     * 维护银行卡信息
     * @param finBankCard
     * @return
     */
    @RequestMapping("updateBankCard")
    @ResponseBody
    @ActionLog(msg = "维护银行卡, 银行卡号：{args[0].bankCard}， 银行编码：{args[0].bankCode}，银行名称：{args[0].bankName}" +
            "，开户行省：{args[0].bankProvince}，开户行市：{args[0].bankCity}，开户支行：{args[0].branchName}" +
            "，平台来源：{args[1]}，系统类型：{args[2]}")
    public Map<String,Object> updateBankCard(FinBankCard finBankCard,@RequestParam("platformSource")Integer platformSource,@RequestParam("systemType")Integer systemType) {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
            return AppResultUtil.errorOfMsg("暂不支持维护银行卡，请联系客服!");
        }
        try {
            SystemTypeEnums systemTypeEnum = SystemTypeEnums.getSystemTypeEnumsByValue(systemType);
            PlatformSourceEnums platformSourceEnum = PlatformSourceEnums.typeByValue(platformSource);
            if(finBankCard.getId()!=null){
                FinBankCard bankCardTemp = finBankCardService.findById(finBankCard.getId());
                if(bankCardTemp.getRegUserId().compareTo(regUser.getId())!=0){
                    return  AppResultUtil.errorOfMsg("您没有权限修改该银行卡信息");
                }
                ResponseEntity<?> responseEntity = finBankCardFrontService.bindBankCard(finBankCard, systemTypeEnum, platformSourceEnum, PayStyleEnum.RECHARGE, regUser);
                if(responseEntity.getResStatus()== Constants.SUCCESS){
                    return  AppResultUtil.successOfMsg((String) responseEntity.getResMsg());
                }else{
                    return  AppResultUtil.errorOfMsg(responseEntity.getResMsg());
                }
            }else{
                return  AppResultUtil.errorOfMsg("修改的银行卡不存在");
            }


        } catch (Exception e) {
            logger.error("用户维护银行卡出现异常：",e);
            return  AppResultUtil.errorOfMsg("用户维护银行卡失败");
        }
    }


    /**
     *  @Description    ：获取解绑银行卡申请记录
     *  @Method_Name    ：getApplyRecord
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 17:26
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("getApplyRecord")
    @ResponseBody
    public Map<String, Object> getApplyRecord(@RequestParam("bankCardId") Integer bankCardId){
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        FinBankCardUpdate bankCardUpdate = finBankCardUpdateService.findFinBankCardUpdateByBankIdAndUserId(regUser.getId(),bankCardId);
        if(null == bankCardUpdate) {
            return AppResultUtil.errorOfMsg("暂无申请记录");
        }else{
            Map<String,Object> data = new HashMap<>();
            data.put("id",bankCardUpdate.getId());
            data.put("createTime",bankCardUpdate.getCreateTime().getTime());
            data.put("state",bankCardUpdate.getState());
            data.put("tel",bankCardUpdate.getTel());
            data.put("cardUp",ossUrl + bankCardUpdate.getCardUp());
            data.put("cardDown",ossUrl + bankCardUpdate.getCardDown());
            data.put("holdingCardUp",ossUrl + bankCardUpdate.getHoldingCardUp());
            data.put("holdingCardDown",ossUrl + bankCardUpdate.getHoldingCardDown());
            data.put("householdRegister",ossUrl + bankCardUpdate.getHouseholdRegister());
            data.put("reason",bankCardUpdate.getReason());
            data.put("remark",bankCardUpdate.getRemark());
            return AppResultUtil.successOf(data,"查询申请记录成功");
        }


    }

    /**
     *  @Description    ：跳转到银行卡解绑页面
     *  @Method_Name    ：toBankCardUpdate
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 18:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("toBankCardUpdate")
    @ResponseBody
    public Map<String, Object> toBankCardUpdate(@RequestParam("bankCardId")Integer bankCardId){
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
            return AppResultUtil.errorOfMsg("暂不支持更换银行卡，请联系客服!");
        }
        if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
            return AppResultUtil.errorOfMsg("解绑银行卡未实名!");
        }
        int type = -1;
        FinBankCardUpdate bankCardUpdate = finBankCardUpdateService.findFinBankCardUpdateByBankIdAndUserId(regUser.getId(),bankCardId);
        if(null != bankCardUpdate){
            type = bankCardUpdate.getState();
        }
        return AppResultUtil.successOfMsg("成功").addResParameter("type",type);
    }


    /**
     *  @Description    ：校验登录密码信息
     *  @Method_Name    ：verifyUserPassword
     *  @param password
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 19:07
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("verifyUserPassword")
    @ResponseBody
    public Map<String,Object> verifyUserPassword(@RequestParam("password") String password){
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
            return AppResultUtil.errorOfMsg("校验登录密码未实名!");
        }
        String login = String.valueOf(regUser.getLogin());
        ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(login, password);
        if(result.getResStatus() == Constants.SUCCESS) {
            result = this.regUserService.validateLoginAndPasswd(login, String.valueOf(result.getResMsg()));
            if(result.getResStatus() == Constants.SUCCESS) {
                return AppResultUtil.successOfMsg("验证密码成功");
            }
        }
        return AppResultUtil.errorOfMsg("验证密码失败");
    }


    /**
     *  @Description    ：解绑银行卡
     *  @Method_Name    ：applyCardUpdate
     *  @param finBankCardUpdate
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 19:16
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("applyCardUpdate")
    @ResponseBody
    @ActionLog(msg = "解绑银行卡, 银行卡号：{args[0].bankCard}， 本人身份证正面：{args[0].cardUp}，本人身份证反面：{args[0].cardDown}" +
            "，手持身份证半身照正面：{args[0].holdingCardUp}，手持身份证半身照反面：{args[0].holdingCardDown}，户口本照片（本人页）：{args[0].householdRegister}" +
            "，解绑原因：{args[0].reason}，银行卡id：{args[1]}")
    public Map<String, Object> applyCardUpdate(FinBankCardUpdate finBankCardUpdate,@RequestParam("bankCardId") Integer bankCardId) {
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
        if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
            return AppResultUtil.errorOfMsg("解绑银行卡未实名!");
        }
        FinBankCard bankCard = finBankCardService.findById(bankCardId);
        if(null == bankCard || TradeStateConstants.BANK_CARD_STATE_AUTH != bankCard.getState()){
            return AppResultUtil.errorOfMsg("银行卡信息不存在!");
        }

        FinBankCardUpdate bankCardUpdateTemp = new FinBankCardUpdate();
        bankCardUpdateTemp.setTel(regUser.getLogin());
        bankCardUpdateTemp.setRegUserId(regUser.getId());
        bankCardUpdateTemp.setBankCard(bankCard.getBankCard());
        bankCardUpdateTemp.setState(BankConstants.BANK_UPDATE_WAIT_AUDIT);
        int finBankCardUpdateCount = finBankCardUpdateService.findFinBankCardUpdateCount(bankCardUpdateTemp);
        if(finBankCardUpdateCount > 0){
            return AppResultUtil.errorOfMsg("尚有未审核的记录!");
        }
        finBankCardUpdate.setRegUserId(regUser.getId());
        finBankCardUpdate.setRealName(regUserDetail.getRealName());
        finBankCardUpdate.setTel(regUser.getLogin());
        finBankCardUpdate.setBankCard(bankCard.getBankCard());
        finBankCardUpdate.setBankName(bankCard.getBankName());
        this.finBankCardUpdateService.insertFinBankCardUpdate(finBankCardUpdate);
        return AppResultUtil.successOfMsg("申请成功！");

    }

    /**
     *  @Description    ：上传图片
     *  @Method_Name    ：uploadImg
     *  @param picFlow
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 20:44
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/uploadImg")
    @ResponseBody
    public Map<String,Object> uploadImg(String picFlow){
    	logger.info("bankController uploadImg..");
        if (picFlow == null ){
            return AppResultUtil.errorOfMsg("图片流不能为空！");
        }
        RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(), () -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
        //处理图片流
        //解码
        byte[] decode = Base64.decode(picFlow.getBytes());
		InputStream input = new ByteArrayInputStream(decode);
		//图片名称
		String fileName = System.currentTimeMillis() + "-" + regUser.getId() + ".jpg";
		FileInfo fileInfo = OSSLoader.getInstance()
		        .setUseRandomName(false)
		        .setAllowUploadType(FileType.EXT_TYPE_IMAGE)
		        .bindingUploadFile(new FileInfo(input))
		        .setFileState(FileState.UN_UPLOAD)
		        .setBucketName(OSSBuckets.HKJF)
		        .setFilePath("/bank/cardUpdate/")
		        .setFileName(fileName)
		        .doUpload();
		if (!fileInfo.getFileState().equals(FileState.SAVED)){
		    return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
		}
		Map<String,Object> result = new HashMap<>(1);
		result.put("picUrl",fileInfo.getSaveKey());
		return AppResultUtil.successOf(result);
    }



    /**
     *  @Description    ：删除图片
     *  @Method_Name    ：delImg
     *  @param picUrl
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018年05月16日 20:45
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/delImg")
    @ResponseBody
    public Map<String,Object> delImg(String picUrl){
        if (StringUtils.isBlank(picUrl)){
            return AppResultUtil.errorOfMsg("图片地址不能为空！");
        }
        int index = picUrl.indexOf("bank/");
        String fileKey = picUrl.substring(index,picUrl.length());
        FileInfo delFile = new FileInfo();
        delFile.setSaveKey(fileKey);
        FileInfo fileInfo = OSSLoader.getInstance()
                .bindingUploadFile(delFile)
                .setFileState(FileState.SAVED)
                .setBucketName(OSSBuckets.HKJF)
                .doDelete();
        if (!fileInfo.getFileState().equals(FileState.DELETE)){
            return AppResultUtil.errorOfMsg(fileInfo.getUploadMessage());
        }
        return AppResultUtil.successOfMsg("删除成功");
    }


}
