package com.hongkun.finance.point.facade;

import com.hongkun.finance.point.model.PointMerchantInfo;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;

/**
 * @Description : 积分商户处理逻辑Facade
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.PointMerchantFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointMerchantFacade {

    /**
     *  @Description    : 根据相关信息查询pointMerchantInfoList
     *  @Method_Name    : pointMerchantList
     *  @param pointMerchantInfo        :条件商户信息
     *  @param pager : 分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity pointMerchantList(PointMerchantInfo pointMerchantInfo, Pager pager);




    /**
     *  @Description    : 根据商户id查询商户详情
     *  @Method_Name    : selectPointMerchantInfoDetail
     *  @param merchantId        :商户ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity selectPointMerchantInfoDetail(Integer merchantId);

    /**
     * 积分支付
     * @param regUserId         用户id
     * @param payPass            支付密码
     * @param merchantCode      商户号
     * @param money              支付金额
     * @param platformSourceEnums              支付来源
     * @return : Map<String,Object>
     * @Description : 积分支付
     * @Method_Name : pointPayment
     * @Creation Date  : 2018年03月18日 下午15:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity pointPayment(int regUserId,String payPass,String merchantCode, BigDecimal money,
                                PlatformSourceEnums platformSourceEnums);
}
