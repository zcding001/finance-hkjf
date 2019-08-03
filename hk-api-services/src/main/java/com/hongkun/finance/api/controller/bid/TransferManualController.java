package com.hongkun.finance.api.controller.bid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.TransferManualDetailAppVo;
import com.hongkun.finance.invest.service.BidTransferManualService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description : 债权转让控制接口
 * @Project : finance
 * @Program Name  : com.hongkun.finance.api.controller.bid
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/transferManualController")
public class TransferManualController {
    private final Logger logger = LoggerFactory.getLogger(TransferManualController.class);
    @Reference
    private TransferManualFacade transferManualFacade;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private BidTransferManualService bidTransferManualService;

    /**
    *  @Description    ：获取我的债权
    *  @Method_Name    ：myCreditorList
    *  @param state   0：待转让 1：转让中 2：已转让
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/6/19
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("myCreditorList")
    @ResponseBody
    public Map<String, Object> myCreditorList(Integer state, Pager pager) {
            RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
            return AppResultUtil.successOfPager((Pager) transferManualFacade.myCreditorList(regUser,state, pager).getResMsg(),
                    "systemName","sortColumns","queryColumnId");
    }

    /**
    *  @Description    ：进入发布转让页面（债权详情）
    *  @Method_Name    ：showPreTransferCreditorDetails
    *  @param investId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/6/20
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("showPreTransferCreditorDetails")
    @ResponseBody
    public Map<String, Object>  showPreTransferCreditorDetails(Integer investId){
        return AppResultUtil.mapOfResponseEntity(transferManualFacade.showTransferManualDetailByInvestId(investId));
    }

    /**
    *  @Description    ：发布债权
    *  @Method_Name    ：saveTransferManual
    *  @param oldInvestId  投资记录id
    *  @param creditorAmount 转让金额
    *  @param transferDays 筹集天数
    *  @return com.yirun.framework.core.model.ResponseEntity<java.lang.String>
    *  @Creation Date  ：2018/6/20
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("saveTransferManual")
    @ResponseBody
    public Map<String,Object> saveTransferManual(Integer oldInvestId, BigDecimal creditorAmount,Integer transferDays) {
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        if (CommonUtils.gtZero(oldInvestId) && CompareUtil.gtZero(creditorAmount)) {
            //初始化参数
            BidTransferManual bidTransferManual = new BidTransferManual(oldInvestId,creditorAmount,transferDays);
            ResponseEntity result = transferManualFacade.saveTransferManual(regUser.getId(), bidTransferManual);
            if (BaseUtil.error(result)){
                return AppResultUtil.errorOfMsg(result.getResMsg());
            }
            return AppResultUtil.successOfMsg("发布成功");
        }
        return AppResultUtil.errorOfMsg("请求参数有误");
    }

    /**
    *  @Description    ：计算购买人和转让人转让后利率
    *  @Method_Name    ：calTransferMoneyAndInterestRate
    *  @param investId
    *  @param creditorAmount
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/6/20
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("calTransferMoneyAndInterestRate")
    @ResponseBody
    public ResponseEntity<?> calTransferMoneyAndInterestRate(Integer investId,BigDecimal creditorAmount) {
        return transferManualFacade.calTransferMoneyAndInterestRate(investId,creditorAmount,new BigDecimal(100));
    }

    /**
    *  @Description    ：债权转让--产品列表
    *  @Method_Name    ：creditorTransferList
    *  @param pager
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/6/21
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("creditorTransferList")
    @ResponseBody
    public Map<String, Object> creditorTransferList(Pager pager) {
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        return AppResultUtil.successOfPager((Pager) transferManualFacade.creditorTransferList(pager).getResMsg(),
                "systemName","sortColumns","queryColumnId","endDate");
    }


    /**
    *  @Description    ：债权详情页面（购买页）
    *  @Method_Name    ：showCreditorTransferDetails
    *
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/6/21
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("showCreditorTransferDetails")
    @ResponseBody
    public Map<String,Object> showCreditorTransferDetails(Integer transferId,Integer bidInvestId){
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        if (transferId==null||transferId==0){
            BidTransferManual bidTransferManual = bidTransferManualService.findBidTransferByNewInvestId(bidInvestId);
            if (bidTransferManual!=null){
                transferId = bidTransferManual.getId();
            }else{
                return AppResultUtil.mapOfResponseEntity(new ResponseEntity<>(Constants.ERROR,"请求参数有误"));
            }
        }
        ResponseEntity response = transferManualFacade.showTransferManualDetailByTransferId(transferId,regUser.getId());
        return AppResultUtil.mapOfResponseEntity(response);
    }


    /**
    *  @Description    ：购买债权
    *  @Method_Name    ：buyCreditor
    *  @param transferId
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date  ：2018/7/4
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("buyCreditor")
    @ResponseBody
    public Map<String,Object> buyCreditor(Integer transferId) {
        try {
            RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
            RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
            if (regUser.getIdentify() == 0) {
                return AppResultUtil.errorOfMsg("为保障您的资金安全，请进行实名认证之后再投资");
            }
            if(CommonUtils.gtZero(transferId)){
                ResponseEntity result = transferManualFacade.buyCreditor(regUser.getId(),regUserDetail.getRealName(), transferId);
                if (BaseUtil.error(result)){
                    return AppResultUtil.errorOfMsg(result.getResMsg());
                }
                return AppResultUtil.successOfMsg("购买成功");
            }
            return AppResultUtil.errorOfMsg("请求参数有误");
        }catch (Exception e) {
            return AppResultUtil.errorOfMsg("购买债权失败,请联系客服");
        }
    }
}
