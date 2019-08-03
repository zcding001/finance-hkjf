package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.model.ResponseEntity;

import java.math.BigDecimal;

/**
 * 积分模块通用服务
 */
public interface PointCommonService {
    /**
     *  @Description    : 积分兑换钱
     *  @Method_Name    : pointToMoney
     *  @param point
     *  @return
     *  @return         : BigDecimal
     *  @Creation Date  : 2017年8月16日 上午9:06:01
     *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
     */
    BigDecimal pointToMoney(int point);


    /**
     *  @Description    : 给用户赠送积分
     *  @Method_Name    : givePointToUser
     * @param pointVO   : 需要赠送的积分信息
     * @param currentUserId: 当前用户的ID
     *  @return                  ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity givePointToUser(PointVO pointVO, Integer currentUserId);


    /**
     *  @Description    : 签到送用户积分
     *  @Method_Name    : signInPoint
     * @param unSavedPointVO   : 需要被保存的积分信息
     * @param currentUserId:    签到用户的ID
     *  @return                  ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity signInPoint(PointVO unSavedPointVO, Integer currentUserId);

    /**
     *  @Description    ：判断用户是否已经签到
     *  @Method_Name    ：hasSign
     *  @param regUserId  判断用户是否已经签到
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/10/12
     *  @Author         ：pengwu@hongkun.com.cn
     */
    ResponseEntity<?> hasSign(Integer regUserId);
}
