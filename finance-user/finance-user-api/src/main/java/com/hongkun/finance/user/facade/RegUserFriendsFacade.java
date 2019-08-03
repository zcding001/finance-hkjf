package com.hongkun.finance.user.facade;

import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description : 好友关系facade
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.facade
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface RegUserFriendsFacade {
    /**
    *  @Description    ：查询用户投资记录即将到期得好友列表
    *  @Method_Name    ：findUserFriendsForInvestDue
    *  @param regUserId
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/10/17
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    public ResponseEntity findUserFriendsForInvestDue(Integer regUserId);
}
