package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.service.FinBankCardFrontService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 银行卡绑定操作管理类
 * @Project : hk-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.payment.BankCardBindingController.java
 * @Author : maruili on  2017/12/12 15:31
 */
@Controller
@RequestMapping("bankCardBindingController/")
public class BankCardBindingController {
    private static final Logger logger = LoggerFactory.getLogger(BankCardBindingController.class);
    @Reference
    private FinBankCardFrontService finBankCardFrontService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private FinBankCardService finBankCardService;
    @Reference
    private RegUserService regUserService;

    @RequestMapping("toBankCard")
    @Token(operate = Token.Ope.REFRESH)
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    @ResponseBody
    public ResponseEntity<?> toBankCard(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> responseEntity = null;
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        try {
            List<FinBankCard> list = finBankCardService.findByRegUserId(regUser.getId());
            if(list!=null && !list.isEmpty()){
                for(FinBankCard vo:list){
                    if(vo.getState() != TradeStateConstants.BANK_CARD_STATE_UNAUTH_FORBIDDEN && vo.getState() != TradeStateConstants.BANK_CARD_STATE_AUTH_FORBIDDEN ){
                    	if(StringUtils.isBlank(vo.getBankCard())){
                    		continue;
                    	}
                    	vo.setBankCard(toShowBankCardNo(vo.getBankCard()));
                        return new ResponseEntity<>(SUCCESS, vo);
                    }
                }

            }
                return new ResponseEntity<>(SUCCESS);
        } catch (Exception e) {
            logger.error("toBankCard, 获取用户绑卡数据, userId: {}",regUser.getId(),e);
            responseEntity = new ResponseEntity<>(ERROR, "获取用户绑卡数据出现异常！");
        }
        return responseEntity;
    }
    @RequestMapping("getRegUserDetail")
    @Token(operate = Token.Ope.REFRESH)
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    @ResponseBody
    public ResponseEntity<?> getRegUserDetail(HttpServletRequest request, HttpServletResponse response){
    	RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        String realName = regUserDetailService.findRegUserDetailNameByRegUserId(regUser.getId());
        char familyName = realName.charAt(0);
        String lastName = realName.substring(1);
        lastName=lastName.replaceAll("[^x00-xff]|\\w", "*");
        return new ResponseEntity<>(SUCCESS, familyName+lastName);
    }
    
    @RequestMapping("bindingCard")
    @Token(operate = Token.Ope.REFRESH)
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    @ResponseBody
    public ResponseEntity<?> bindingCard(FinBankCard bankCard){
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        try {
            bankCard.setRegUserId(regUser.getId());
            return finBankCardFrontService.bindBankCard(bankCard, SystemTypeEnums.HKJF, PlatformSourceEnums.PC, PayStyleEnum.RECHARGE,regUser);
        } catch (Exception e) {
            logger.error("bindingCard, 用户绑定银行卡, userId: {},bankCard: {}",regUser.getId(),bankCard.toString(),e);
            return new ResponseEntity<>(ERROR, "绑卡出现异常");
        }
    }
    @RequestMapping("getBankCard")
    @Token(operate = Token.Ope.REFRESH)
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    @ResponseBody
    public ResponseEntity<?> getBankCard(Integer id){
        ResponseEntity responseEntity = new ResponseEntity<>(SUCCESS);
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        try {
            String realName = regUserDetailService.findRegUserDetailNameByRegUserId(regUser.getId());
            Map<String,String> map = new HashMap();
            map.put("realName",realName);
            responseEntity.setParams(map);
            FinBankCard bankCard = finBankCardService.findById(id);
            if(bankCard!=null){
                if(bankCard.getRegUserId().compareTo(regUser.getId())==0){
                   if(bankCard.getState() != TradeStateConstants.BANK_CARD_STATE_INIT && bankCard.getState() != TradeStateConstants.BANK_CARD_STATE_UNAUTH ){
                       bankCard.setBankCard(toShowBankCardNo(bankCard.getBankCard()));
                   }
                    responseEntity.setResMsg(bankCard);
                    return responseEntity;
                }else{
                    return new ResponseEntity<>(ERROR, "请输入正确的银行卡查询信息");
                }
            }

            return new ResponseEntity<>(ERROR, "没有查到银行卡信息");
        } catch (Exception e) {
            logger.error("getBankCard, 获取银行卡信息, bankCardId: {}",id,e);
            return new ResponseEntity<>(ERROR, "查询银行卡信息出现异常");
        }
    }
    /**
     * @Description : 卡号隐藏中间部分
     * @Method_Name : toShowBankCardNo;
     * @param bankCardNo
     *            卡号
     * @return
     * @return : Map<String,Object>;
     */
    private String toShowBankCardNo(String bankCardNo) {
        Map<String, Object> cardNoMap = new HashMap<String, Object>();
        String cardNoHead = bankCardNo.substring(0, 4);
        String cardNoFoot = bankCardNo.substring(bankCardNo.length() - 4);
        String showCardNo = cardNoHead + "********" + cardNoFoot;
        return showCardNo;
    }
}
