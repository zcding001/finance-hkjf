package com.hongkun.finance.api.controller.bid;

import com.hongkun.finance.api.controller.ControllerImitatior;
import org.junit.Test;

import java.util.HashMap;

public class BidInfoControllerTest extends ControllerImitatior {

    @Override
    public String getBaseUrl() {
        return "/bidInfoController/";
    }

    @Test
    public void findIndexBidsInfo() {
        /***
         * 不需要任何参数
         */
        commitParams();
    }

    @Test
    public void findAppBidDetail() {
        commitParams(new HashMap(){
            {
                put("bidId", 3);
            }
        });
    }

    @Test
    public void filterProductBidInfo() {
        /***
         * 不需要任何参数
         */
        commitParams();
    }


}