package com.hongkun.finance.web.controller.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description : 平台对账
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.bi
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/balAccountController")
public class BalAccountController {


    @Reference
    private BalAccountFacade balAccountFacade;

    /**
    *  @Description    ：平台对账信息查询
    *  @Method_Name    ：balanceList
    *  @param tel  手机号
    *  @param balanceType   对账类型  1-实时对账 2-历史对账
    *  @param state  对账状态 1-账平 0-账不平 3-全部
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/5/3
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/balanceList")
    @ResponseBody
    public ResponseEntity<?> balanceList(String tel,Integer balanceType,Integer state,Pager pager){
          return    balAccountFacade.initBalAccount(tel,balanceType,state,pager);
    }

}
