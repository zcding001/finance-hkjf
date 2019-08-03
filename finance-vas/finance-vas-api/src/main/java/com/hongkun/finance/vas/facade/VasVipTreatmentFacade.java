package com.hongkun.finance.vas.facade;

import java.util.Date;
import java.util.Map;

/**
 * @Description : 会员待遇facade服务接口
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.facade.VasVipTreatMentFacade
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public interface VasVipTreatmentFacade {

    /**
     *  @Description    ：每天用户生日发送卡券
     *  @Method_Name    ：vipTreatmentPerBirthDay
     *  @param currentDate  当前时间
     *  @param shardingItem  分片项
     *  @Creation Date  ：2018/4/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    void vipTreatmentPerBirthDay(Date currentDate, int shardingItem);

    /**
     *  @Description    ：每月1号发送卡券
     *  @Method_Name    ：vipTreatmentPerMonth
     *  @param currentDate  当前时间
     *  @param shardingItem  分片项
     *  @Creation Date  ：2018/4/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    void vipTreatmentPerMonth(Date currentDate, int shardingItem);

    /**
     *  @Description    ：每天用户会员降级，降级的条件（近三个月没有回款计划&近三个没有成长值记录），降级后为下个等级最高成长值
     *  @Method_Name    ：VipDownGrade
     *  @param currentDate  当前时间
     *  @param shardingItem  分片项
     *  @Creation Date  ：2018/4/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    void vipDownGrade(Date currentDate, int shardingItem);

    /**
     *  @Description    ：获取符合当前用户创建时间的所有会员待遇信息
     *  @Method_Name    ：getAllVipTreatMentList
     *  @param registTime 用户创建时间
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/11
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<String, Object> getAllVipTreatMentList(Date registTime);
}
