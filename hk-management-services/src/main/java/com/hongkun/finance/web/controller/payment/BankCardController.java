package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.model.FinBankCardUpdate;
import com.hongkun.finance.payment.service.FinBankCardUpdateService;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 银行卡管理预约控制层
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.payment.BankCardController
 * @Author : yunlongliu@yiruntz.com
 */
@Controller
@RequestMapping("/bankCardController")
public class BankCardController {
    @Reference
    private FinBankCardUpdateService finBankCardUpdateService;

    @Reference
    private RegUserDetailService regUserDetailService;

    /**
     *  @Description    ：条件检索股权类型信息列表
     *  @Method_Name    ：findBankCardUpdateList
     *  @param finBankCardUpdate
     *  @param pager
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018年05月16日 16:12
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("findBankCardUpdateList")
    @ResponseBody
    public ResponseEntity<?> findBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager) {
        return new ResponseEntity<>(SUCCESS, this.finBankCardUpdateService.findBankCardUpdateList(finBankCardUpdate, pager));
    }


    /**
     *  @Description    ：审核银行卡变更信息
     *  @Method_Name    ：auditBankCardUpdate
     *  @param finBankCardUpdate
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月16日 16:53
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("auditBankCardUpdate")
    @ResponseBody
    public ResponseEntity<?> auditBankCardUpdate(FinBankCardUpdate finBankCardUpdate) {
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
        FinBankCardUpdate cardUpdate = new FinBankCardUpdate();
        if(StringUtilsExtend.isEmpty(finBankCardUpdate.getReason())) {
        	return new ResponseEntity<>(ERROR, "申请原因不能为空!");
        }
        if(StringUtilsExtend.isEmpty(finBankCardUpdate.getRemark())) {
            return new ResponseEntity<>(ERROR, "审核信息不能为空!");
        }
        cardUpdate.setReason(finBankCardUpdate.getReason());
        cardUpdate.setId(finBankCardUpdate.getId());
        cardUpdate.setModifyUserId(BaseUtil.getLoginUser().getId());
        cardUpdate.setModifyUserName(regUserDetail.getRealName());
        cardUpdate.setState(finBankCardUpdate.getState());
        return this.finBankCardUpdateService.auditBankCardUpdate(finBankCardUpdate);

    }


    /**
     *  @Description    ：查询解绑因银行卡详情
     *  @Method_Name    ：findBankCardUpdateInfo
     *  @param id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月16日 17:13
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("findBankCardUpdateInfo")
    @ResponseBody
    public ResponseEntity<?> findBankCardUpdateInfo(@RequestParam("id") Integer id){
        FinBankCardUpdate finBankCardUpdate = this.finBankCardUpdateService.findBankCardUpdateInfo(id);
        if(null == finBankCardUpdate){
            return new ResponseEntity<>(Constants.ERROR, "没有查询到解绑银行卡信息！");
        }
        return new ResponseEntity<>(Constants.SUCCESS,finBankCardUpdate);
    }



}
