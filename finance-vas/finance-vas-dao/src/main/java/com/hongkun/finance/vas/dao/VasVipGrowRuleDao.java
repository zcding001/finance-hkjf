package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Date;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.VasVipGrowRuleDao.java
 * @Class Name    : VasVipGrowRuleDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasVipGrowRuleDao extends MyBatisBaseDao<VasVipGrowRule, Long> {

    /**
     *  @Description    : 获取会员成长值规则适用用户注册时间段有交集的规则
     *  @Method_Name    : findVasVipGrowRuleTimeCount
     *  @param growRule
     *  @return         : int
     *  @Creation Date  : 2018年01月30日 下午14:28:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    int findVasVipGrowRuleTimeCount(VasVipGrowRule growRule);

    /**
     *  @Description    ：根据成长值渠道类型、用户注册时间获取对应的成长值规则
     *  @Method_Name    ：getVipGrowRuleByTypeAndRegistTime
     *  @param type  获取成长值渠道类型:1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠
     *  @param registTime  用户注册时间
     *  @return com.hongkun.finance.vas.model.VasVipGrowRule
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    VasVipGrowRule getVipGrowRuleByTypeAndRegistTime(int type, Date registTime);
}
