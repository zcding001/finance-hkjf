package com.hongkun.finance.api.controller.pointmall;

import com.hongkun.finance.api.controller.ControllerImitatior;
import com.hongkun.finance.user.model.RegUserAddress;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class AddressControllerTest extends ControllerImitatior {


    private RegUserAddress unSavedAddress;

    @Before
    public void init() {
        unSavedAddress = new RegUserAddress();
        unSavedAddress.setCounty("120101");
        unSavedAddress.setCity("120100");
        unSavedAddress.setProvince("120000");
        unSavedAddress.setConsignee("唐忠平55");
        unSavedAddress.setRegUserId(3);
        unSavedAddress.setDistrict("南庭新苑南区1403");
        unSavedAddress.setMobilePhone(15910540028L);
        unSavedAddress.setAlias("家庭2");
    }

    @Test
    public void saveUserAddress() {
        commitParams(unSavedAddress);
    }

    @Test
    public void updateUserAddress() {
        unSavedAddress.setId(35);
        unSavedAddress.setAlias("家庭3");
        commitParams(unSavedAddress);
    }

    @Test
    public void getUserAddressList() {
        /**
         * 不需要参数
         */
        commitParams();
    }

    @Test
    public void setDefaultAddress() {
        commitParams(new HashMap(){
            {
                put("addressId", "35");
            }
        });
    }

    @Test
    public void delAddress() {
        commitParams(new HashMap(){
            {
                put("addressId", "25");
            }
        });
    }

    @Test
    public void loadProperties() {
        /**
         * 参数为空
         */
        commitParams();
    }

    @Test
    public void loadOfflineStoreAddressByPropertyId() {
        commitParams(new HashMap(){
            {
                put("propertyId", "22");
            }
        });
    }

    @Override
    public String getBaseUrl() {
        return "/addressController/";
    }
}