package com.hongkun.finance.user.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.facade.RegUserFriendsFacade;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : TODO
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Service
public class RegUserFriendsFacadeImpl implements RegUserFriendsFacade {
    @Reference
    private RegUserFriendsService regUserFriendsService;
    @Reference
    private BidReceiptPlanService bidReceiptPlanService;
    @Override
    public ResponseEntity findUserFriendsForInvestDue(Integer regUserId) {
        //查询所有好友
        RegUserFriends cdt = new RegUserFriends();
        cdt.setRecommendId(regUserId);
        cdt.setGrade(UserConstants.USER_FRIEND_GRADE_FIRST);
        List<RegUserFriends> regUserFriendsList =  regUserFriendsService.findRegUserFriendsAndGroup(cdt);
        List<RegUserFriends> resultList = new ArrayList<RegUserFriends>();
        //过滤回款计划今天明天到期得用户
        if (CommonUtils.isNotEmpty(regUserFriendsList)){
            for (RegUserFriends friends:regUserFriendsList){
                //查询进行中得最小回款计划
                Date minDate = bidReceiptPlanService.findMinPlanTimeDue(friends.getRegUserId());
                if (minDate!=null){
                    int betweenDays = DateUtils.getDays(minDate,new Date());
                    if (betweenDays<=1 || betweenDays >=0){
                        String realName = friends.getRealName();
                        if (StringUtils.isNotBlank(friends.getRealName())){
                            if(realName.length()>=3){
                                realName = realName.substring(0, 1)+"*"+realName.substring(2, realName.length());
                            }else{
                                realName = realName.substring(0, 1)+"*";
                            }
                        }else{
                            friends.setRealName("鸿坤金服用户（未实名）");
                        }
                        resultList.add(friends);
                    }
                }
            }
            return new ResponseEntity<>(Constants.SUCCESS,resultList);
        }
        return new ResponseEntity<>(Constants.ERROR,"未查询到相关好友信息");
    }
}
