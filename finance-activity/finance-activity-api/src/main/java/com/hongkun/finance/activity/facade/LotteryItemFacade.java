package com.hongkun.finance.activity.facade;

import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;

public interface LotteryItemFacade {

    /**
     *  @Description    ：保存抽奖活动选项
     *  @Method_Name    ：saveLotteryItems
     *  @param groupsJSON
     *  @param loginUser
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年09月29日 17:32
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    ResponseEntity<?> saveLotteryItems(String groupsJSON, RegUser loginUser);
}
