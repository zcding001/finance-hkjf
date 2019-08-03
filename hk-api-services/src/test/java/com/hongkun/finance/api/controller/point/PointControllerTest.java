package com.hongkun.finance.api.controller.point;

import com.hongkun.finance.api.controller.BaseControllerTest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 积分控制类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.user.PointControllerTest
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class PointControllerTest extends BaseControllerTest {

    private static final String POINT_CONTROLLER = "/pointController/";

    @Test
    public void getMerchantInfo() throws Exception{
        doTest(POINT_CONTROLLER + "getMerchantInfo",4);
    }

    @Test
    public void getMerchantNameByCode() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("merchantCode","SN555702");
        doTest(POINT_CONTROLLER + "getMerchantNameByCode",param);
    }

    @Test
    public void pointPayment() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("merchantCode","SN555702");
        param.put("money","5");
        param.put("payPass","12345");
        param.put("sourceType","11");
        doTest(POINT_CONTROLLER + "pointPayment",param,32);
    }

    @Test
    public void moneyTransferPoint() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("money","0.01");
        doTest(POINT_CONTROLLER + "moneyTransferPoint",param,32);
    }

    @Test
    public void pointTransfer() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("point","300");
        param.put("receiveTel","17001279697");
        doTest(POINT_CONTROLLER + "pointTransfer",param,32);
    }

    @Test
    public void getMerchantInfoState() throws Exception{
        doTest(POINT_CONTROLLER + "getMerchantInfoState",32);
    }

    @Test
    public void getCheckMerchantInfo() throws Exception{
        doTest(POINT_CONTROLLER + "getCheckMerchantInfo",32);
    }

    @Test
    public void getRecentFailMerchantInfo() throws Exception{
        doTest(POINT_CONTROLLER + "getRecentFailMerchantInfo",32);
    }

    @Test
    public void getMerchantFundtransferList() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("isToday","0");
        param.put("currentPage","1");
        param.put("pageSize", "20");
        doTest(POINT_CONTROLLER + "getMerchantFundtransferList",param,32);
    }

    @Test
    public void saveMerchantInfo() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("merchantName","鸿坤生活广场");
        param.put("businessLicenseUrl","");
        param.put("permitUrl", "");
        param.put("idcardFrontUrl","");
        param.put("idcardBackUrl","");
        param.put("hygieneLicenseUrl","");
        param.put("businessType","0");
        doTest(POINT_CONTROLLER + "saveMerchantInfo",param,32);
    }

    @Test
    public void uploadMerchantImage() throws Exception{
        String path = PointControllerTest.class.getResource("/").getPath();
        File imageFile = new File(path + "image.txt");
        Long fileLength = imageFile.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        InputStream inputStream = new FileInputStream(imageFile);
        inputStream.read(fileContent);
        inputStream.close();

        Map<String,String> param = new HashMap<>();
        param.put("picFlow",new String(fileContent,"utf-8"));
        param.put("picType","1");
        doTest(POINT_CONTROLLER + "uploadMerchantImage",param,32);
    }

    @Test
    public void delMerchantImage() throws Exception{
        Map<String,String> param = new HashMap<>();
        param.put("picUrl","point/merchant/1521689226147-32-1.jpg");
        doTest(POINT_CONTROLLER + "delMerchantImage",param,32);
    }
}
