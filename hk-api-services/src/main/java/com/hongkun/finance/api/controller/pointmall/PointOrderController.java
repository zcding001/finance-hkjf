package com.hongkun.finance.api.controller.pointmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointOrderFacade;
import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.hongkun.finance.point.service.PointProductOrderService;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static com.hongkun.finance.point.constants.PointConstants.POINT_ORDER_SENDWAY_POST;
import static com.hongkun.finance.point.constants.PointConstants.POINT_ORDER_SENDWAY_POST_OR_PICK_SELF;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * 积分订单接口
 * @author :zhongpingtang
 */
@Controller
@RequestMapping("/pointOrderController")
public class PointOrderController {
    private static final Logger logger = LoggerFactory.getLogger(PointOrderController.class);

    @Reference
    private PointProductCategoryService categoryService;

    @Reference
    private PointProductService productService;

    @Reference
    private PointAccountService pointAccountService;

    @Reference
    private PointOrderFacade pointOrderFacade;

    @Reference
    private PointProductOrderService productOrderService;
    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;

    /**
     * 创建一个订单
     * @param pointProductOrder
     * @return
     */
    @RequestMapping("/createOrderInfo")
    @ResponseBody
    public Map<String,Object> createOrderInfo(@Validated(value = SAVE.class) PointProductOrder pointProductOrder){
        try{
            //进入创建订单流程
            if(pointProductOrder.getSendWay() == PointConstants.POINT_ORDER_SENDWAY_NO_NEED_SEND){
                AppResultUtil.errorOfMsg("兑换码商品不允许上传地址！");
            }
            ResponseEntity result = pointOrderFacade.createOrderInfo(pointProductOrder, BaseUtil.getLoginUser());
            if (result.getResStatus() == SUCCESS){
                try {
                    VasVipGrowRecordMqVO vasVipGrowRecordMqVO = new VasVipGrowRecordMqVO();
                    vasVipGrowRecordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_EXCHANGE);
                    vasVipGrowRecordMqVO.setUserId(BaseUtil.getLoginUser().getId());
                    VipGrowRecordUtil.sendVipGrowRecordToQueue(vasVipGrowRecordMqVO);
                }catch (Exception e){
                    logger.error("createOrderInfo, 创建订单成功后发送mq异常, 订单信息: {}, 用户id: {}, 异常信息: ",
                            pointProductOrder,BaseUtil.getLoginUser().getId(),e);
                }
            }
            return AppResultUtil.mapOfResponseEntity(result);
        }catch(Exception e){
            logger.error("生成积分订单失败, 用户ID: {}, 异常信息: {}",BaseUtil.getLoginUser().getId(),e);
            if(e.getMessage().contains("积分不足")){
                return AppResultUtil.errorOfMsg(Constants.ERROR, "积分不足！"); 
            }else{
                return AppResultUtil.errorOfMsg(Constants.ERROR, "网络异常"); 
            }
        }
    }


    /**
     * 列出用户的订单
     * @return
     */
    @RequestMapping("/listUserPointProductOrder")
    @ResponseBody
    public Map<String,Object> listUserPointProductOrder(PointOrderQuery pointOrderQuery, Pager pager){
        pager.setInfiniteMode(true);
        pointOrderQuery.setRegUserId(BaseUtil.getLoginUser().getId());
        ResponseEntity<?> responseEntity = productOrderService.listUserPointProductOrder(pointOrderQuery, pager);
        //收货人，手机号，收货地址
        return AppResultUtil.successOfPagerInProperties((Pager) responseEntity.getResMsg(),
                "createTime", "smallImgUrl", "state", "point", "name", "sendWay", "courierNo", "address", "number")
                .processObjInList((orderMap)->{
                    Integer sdenWay = (Integer) orderMap.get("sendWay");
                    String smallImgUrl = (String) orderMap.get("smallImgUrl");
                    orderMap.put("smallImgUrl", ossUrl + smallImgUrl);
                    if (sdenWay==POINT_ORDER_SENDWAY_POST_OR_PICK_SELF||sdenWay==POINT_ORDER_SENDWAY_POST) {
                        String address = (String) orderMap.get("address");
                        String[] addressArr = StringUtils.split(address, ",");
                        int sendWayPackageFlagLength = 3;
                        if (addressArr.length>= sendWayPackageFlagLength) {
                            //处理邮寄地址
                            orderMap.put("userName", addressArr[2]);
                            orderMap.put("tel", addressArr[addressArr.length - 1]);
                            addressArr=ArrayUtils.remove(addressArr, 2);//排除userName
                            addressArr=ArrayUtils.remove(addressArr, addressArr.length - 1);
                            orderMap.put("address", StringUtils.join(addressArr, ""));
                            //1-自提，2-邮寄
                            orderMap.put("sendWay", 2);
                        }else{
                            orderMap.put("sendWay", 1);
                            orderMap.put("address", StringUtils.join(addressArr, "  "));
                        }
                    }

                });


    }



}
