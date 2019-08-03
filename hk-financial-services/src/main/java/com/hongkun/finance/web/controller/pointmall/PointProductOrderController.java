package com.hongkun.finance.web.controller.pointmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.facade.PointOrderFacade;
import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.point.service.PointProductOrderService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserCommunityService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * 积分商品订单
 */
@Controller
@RequestMapping("/pointProductOrderController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
public class PointProductOrderController {

    private Logger LOGGER = LoggerFactory.getLogger(PointProductOrderController.class);
    @Reference
    private RegUserCommunityService communityService;
    @Reference
    private PointProductOrderService orderService;
    @Reference
    private PointOrderFacade pointOrderFacade;

    /**
     * 查询门店地址
     * @return
     */
    @RequestMapping("/loadOfflineStoreAddress")
    @ResponseBody
    public ResponseEntity loadOfflineStoreAddress(){
        return new ResponseEntity(SUCCESS, communityService.loadOfflineStoreAddress());
    }


    /**
     * 创建一个订单
     * @param pointProductOrder
     * @return
     */
    @RequestMapping("/createOrderInfo")
    @ResponseBody
    @Token
    public ResponseEntity createOrderInfo(@Validated(value = SAVE.class) PointProductOrder pointProductOrder){
        RegUser regUser = BaseUtil.getLoginUser();
        //进入创建订单流程
        ResponseEntity result = pointOrderFacade.createOrderInfo(pointProductOrder,regUser);
        if (result.getResStatus() == SUCCESS){
            try {
                VasVipGrowRecordMqVO vasVipGrowRecordMqVO = new VasVipGrowRecordMqVO();
                vasVipGrowRecordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_EXCHANGE);
                vasVipGrowRecordMqVO.setUserId(regUser.getId());
                VipGrowRecordUtil.sendVipGrowRecordToQueue(vasVipGrowRecordMqVO);
            }catch (Exception e){
                LOGGER.error("createOrderInfo, 创建订单成功后发送mq异常, 订单信息: {}, 用户id: {}, 异常信息: ",
                        pointProductOrder,regUser.getId(),e);
            }
        }
        return result;
    }


    /**
     * 列出用户的订单
     * @return
     */
    @RequestMapping("/listUserPointProductOrder")
    @ResponseBody
    public ResponseEntity listUserPointProductOrder(PointOrderQuery pointOrderQuery,Pager pager){
        pointOrderQuery.setRegUserId(BaseUtil.getLoginUser().getId());
        return orderService.listUserPointProductOrder(pointOrderQuery,pager);
    }





}
