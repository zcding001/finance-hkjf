package com.hongkun.finance.vas.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.facade.VasVipTreatmentFacade;
import com.hongkun.finance.vas.model.dto.CouponDetailMqDTO;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static java.lang.System.out;

/**
 * @Description : VasCouponDetailService测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.service.TestVasCouponDetailService
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-vas.xml" })
public class TestVasCouponDetailService {

    @Reference
    private VasCouponDetailService vasCouponDetailService;

    @Autowired
    private VasVipTreatmentFacade vasVipTreatmentFacade;

    @Autowired
    private CouponFacade couponFacade;
    @Autowired
    private JmsService jmsService;
    @Test
    public void TestCouponDetailOverDue(){
        Date currentDate = DateUtils.getCurrentDate();
        vasCouponDetailService.couponDetailOverDue(currentDate,3);
    }

    @Test
    public void TestVipTreatmentPerBirthDay(){
        Date currentDate = DateUtils.getCurrentDate();
        vasVipTreatmentFacade.vipTreatmentPerBirthDay(currentDate,3);
    }

    @Test
    public void TestVipDownGrade(){
        Date currentDate = DateUtils.getCurrentDate();
        vasVipTreatmentFacade.vipDownGrade(currentDate,3);
    }

    @Test
    public void TestReissueUserCouponDetail(){
//        couponFacade.reissueUserCouponDetail(68,61,"提现审核拒绝，补发提现券");
        CouponDetailMqDTO couponDetailMqDTO = new CouponDetailMqDTO();
        couponDetailMqDTO.setReason("提现审核拒绝，补发提现券");
        couponDetailMqDTO.setRegUserId(68);
        couponDetailMqDTO.setCouponProductId(61);
        jmsService.sendMsg(VasCouponConstants.MQ_QUEUE_VAS_COUPON_DETAIL, DestinationType.QUEUE, couponDetailMqDTO,
                JmsMessageType.OBJECT);

        out.println("out");
    }
}
