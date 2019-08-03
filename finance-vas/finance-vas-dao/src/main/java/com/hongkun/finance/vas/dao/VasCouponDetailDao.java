package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.dao.VasCouponDetailDao.java
 * @Class Name    : VasCouponDetailDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasCouponDetailDao extends MyBatisBaseDao<VasCouponDetail, Long> {

    /**
     * @param param{acceptorUserId  用户的id(必填)
     * 				 state				获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(非必填)
     * 				 type               获取用户对应的卡券产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券(非必填)}
     * @return : List<VasCouponDetailVO>
     * @Description : 获取用户对的卡券信息
     * @Method_Name : getUserCouponDetailList
     * @Creation Date : 2017年12月14日 下午15:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    List<VasCouponDetailVO> getUserCouponDetailList(Map<String, Object> param);

    /**
     * @param param{acceptorUserId  用户的id(必填)
     * 				 state				获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(非必填)
     * 				 type               获取用户对应的卡券产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券(非必填)}
     * @return : Integer
     * @Description : 获取用户对的卡券信息的数量
     * @Method_Name : getUserCouponDetailListCount
     * @Creation Date : 2017年12月14日 下午15:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    Integer getUserCouponDetailListCount(Map<String, Object> param);

    /**
     * @param avtKey             卡券key
     * @return : VasCouponDetail
     * @Description : 根据卡券key获取对应的卡券信息
     * @Method_Name : findVasCouponDetailByKey
     * @Creation Date : 2018年04月03日 下午19:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    VasCouponDetail findVasCouponDetailByKey(String avtKey);

    /**
     *  @Description    ：获取用户各类卡券的数量
     *  @Method_Name    ：getUserCouponDetailCountGroupByType
     *  @param regUserId
     *  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *  @Creation Date  ：2018/4/13
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<Map<String,Object>> getUserCouponDetailCountGroupByType(int regUserId);

    /**
     *  @Description    ：获取已过期的卡券集合
     *  @Method_Name    ：getExpiredCouponDetailList
     *  @param currentDate  当前时间
     *  @return java.util.List<com.hongkun.finance.vas.model.VasCouponDetail>
     *  @Creation Date  ：2018/4/25
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<VasCouponDetail> getExpiredCouponDetailList(Date currentDate);

    /**
     *  @Description    ：获取卡券列表信息
     *  @Method_Name    ：findVasCouponDetailByIds
     *  @param couponIds 卡券id集合
     *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasCouponDetail>
     *  @Creation Date  ：2018/10/23
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<Integer, VasCouponDetail> findVasCouponDetailByIds(Set<Integer> couponIds);
}
