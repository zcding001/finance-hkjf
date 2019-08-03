package com.hongkun.finance.api.controller.pointmall;

import com.hongkun.finance.api.controller.ControllerImitatior;
import com.hongkun.finance.point.model.query.PointMallQuery;
import org.junit.Before;
import org.junit.Test;

public class PointMallControllerTest extends ControllerImitatior {

    private PointMallQuery pointMallQuery;
    @Before
    public void init() {
        pointMallQuery = new PointMallQuery();
        pointMallQuery.setSortType(2);
        pointMallQuery.setRecommend(1);
    }

    @Test
    public void loadMallCategories() {
        /**
         * 不需要参数
         */
        commitParams();

    }

    @Test
    public void filterProduct() {
        commitParams(pointMallQuery);
        pointMallQuery.setCateId(2);
        commitParams(pointMallQuery);
        commitParams();
    }

    @Test
    public void productsICanPay() {
        /**
         * 不需要参数
         */
        commitParams();
    }

    @Test
    public void getUserPoint() {
        /**
         * 不需要参数
         */
        commitParams();
    }

    @Test
    public void findFlashSales() {
        /**
         * 不需要参数
         */
        commitParams();
    }

    @Test
    public void findProductDetail() {
        /**
         * 不需要参数
         */
        commitParams();
    }

    @Override
    public String getBaseUrl() {
        return "/pointMallController/";
    }
}