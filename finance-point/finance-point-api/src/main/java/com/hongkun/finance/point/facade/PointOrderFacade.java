package com.hongkun.finance.point.facade;

import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.facade.PointOrderFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointOrderFacade {

    /**
     *  @Description    : 列出所有的积分订单
     *  @Method_Name    : listPointOrder
     *  @param pointProductOrder  :条件商户信息
     *  @param pager        :分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager listPointOrder(PointOrderQuery pointProductOrder, Pager pager);


    /**
     *  @Description    : 取消订单操作
     *  @Method_Name    : cancelOrder
     *  @param orderId  :订单ID
     *  @param adminId        :操作用户ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity cancelOrder(Integer orderId, Integer adminId);



    /**
     *  @Description    : 修改快递单号
     *  @Method_Name    : updateCourierNo
     *  @param pointProductOrder  :订单信息
     *  @param adminId        :操作用户ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity updateCourierNo(PointProductOrder pointProductOrder, Integer adminId);


    /**
     *  @Description    : 创建订单信息
     *  @Method_Name    : createOrderInfo
     *  @param pointProductOrder  :订单信息
     *  @param loginUser        :当前登录用户
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity createOrderInfo(PointProductOrder pointProductOrder, RegUser loginUser);



    /**
     *  @Description    : 审核订单操作
     *  @Method_Name    : checkOrder
     *  @param orderId  :订单ID
     *  @param state        :审核之后的状态
     *  @param userId        :当前用户ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity checkOrder(List<Integer> orderId, Integer state, Integer userId);
}
