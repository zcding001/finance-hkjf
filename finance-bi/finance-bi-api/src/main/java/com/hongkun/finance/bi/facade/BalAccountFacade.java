package com.hongkun.finance.bi.facade;


import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 平台对账服务
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.facade
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface BalAccountFacade {
    /**
    *  @Description    ：对账查询
    *  @Method_Name    ：initBalAccountInActualTime
    *  @param tel  用户手机号
    *  @param balanceType 对账类型：1-实时对账 2-历史对账
    *  @param state    状态：0 账不平 1-平 3-全部 （只针对历史对账）
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date  ：2018/4/28
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity<?> initBalAccount(String tel,Integer balanceType,Integer state,Pager pager);

    /**
    *  @Description    ：每日跑批处理存在账户变动的用户
    *  @Method_Name    ：initBalAccountForChange
    *  @return void
    *  @Creation Date  ：2018/4/28
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    void  initBalAccountForChange();

}
