package com.hongkun.finance.api.controller.pointmall;

import com.hongkun.finance.api.controller.ControllerImitatior;
import com.hongkun.finance.point.model.PointProductOrder;
import org.junit.Before;
import org.junit.Test;

public class PointOrderControllerTest extends ControllerImitatior {

    PointProductOrder pointProductOrder;

    @Before
    public void init() {
        pointProductOrder = new PointProductOrder();
        pointProductOrder.setProductId(43);
        pointProductOrder.setNumber(2);
        pointProductOrder.setAddress("北京,北京,唐忠平,大兴区，西红门，瑞海二区,15910540028");
    }

    @Test
    public void createOrderInfo() {
        commitParams(pointProductOrder);

    }

    @Test
    public void listUserPointProductOrder() {

        /**
         * 不需要参数
         */
        commitParams();
    }

    @Override
    public String getBaseUrl() {
        return "/pointOrderController/";
    }
}