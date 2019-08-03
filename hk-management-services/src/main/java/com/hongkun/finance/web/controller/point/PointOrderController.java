package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.facade.PointOrderFacade;
import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.point.service.PointProductOrderService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 积分兑换商品的controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.PointOrderController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@RequestMapping("/pointOrderController")
@Controller
public class PointOrderController {


    @Reference
    private PointOrderFacade pointOrderFacade;

    @Reference
    private PointProductOrderService orderService;


    /**
     * 查询积分订单的分页数据
     * @param pointOrderQuery
     * @param pager
     * @return
     */
    @RequestMapping("/listPointOrder")
    @ResponseBody
    public ResponseEntity listPointOrder(PointOrderQuery pointOrderQuery, Pager pager){
        return new ResponseEntity(SUCCESS, pointOrderFacade.listPointOrder(pointOrderQuery, pager));
    }


    /**
     * 审核订单
     * @param orderIds
     * @return
     */
    @RequestMapping("/checkOrder")
    @ResponseBody
    public ResponseEntity checkOrder(@RequestParam("checkIds") List<Integer> orderIds, @RequestParam("state") Integer state){
        return this.pointOrderFacade.checkOrder(orderIds,state,BaseUtil.getLoginUser().getId());
    }


    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @RequestMapping("/cancelOrder")
    @ResponseBody
    public ResponseEntity cancelOrder(@RequestParam("orderId") Integer orderId){
        return this.pointOrderFacade.cancelOrder(orderId,BaseUtil.getLoginUser().getId());
    }


    /**
     * 修改快递单号
     * @param pointProductOrder
     * @return
     */
    @RequestMapping("/updateCourierNo")
    @ResponseBody
    public ResponseEntity updateCourierNo(PointProductOrder pointProductOrder){
        if (pointProductOrder.getId()==null) {
            return new ResponseEntity(ERROR, "请指定订单");
        }
        return this.pointOrderFacade.updateCourierNo(pointProductOrder,BaseUtil.getLoginUser().getId());
    }





}
