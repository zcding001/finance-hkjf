package com.hongkun.finance.fund.facade;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description : 房产投资facade
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.fund.facade
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface HouseInfoFacade {
    ResponseEntity showHouseInfoDetail(Integer regUserId,Integer infoId);
    /**
    *  @Description    ：房产项目预约
    *  @Method_Name    ：reservation
     * @param regUserDetail
    *  @param infoId
    *  @param personNum
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/10/10
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity reservation(RegUserDetail regUserDetail, Integer infoId, Integer personNum,String tel);
}
