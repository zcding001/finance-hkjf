package com.hongkun.finance.payment.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.yirun.framework.core.enums.SystemTypeEnums;

/**
 * @Description : 协议支付服务测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.service.impl
 * @Author : binliang@hongkun.com.cn 梁彬
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-payment.xml"})
public class FinConsumptionServiceImplTest {
	
    @Reference
    private FinConsumptionService finConsumptionService;
    
    @Test
    public void rechargeCash(){  //认证第三方收银台
    	RechargeCash rechargeCash = new RechargeCash();
    	rechargeCash.setBankCardId(48);
    	rechargeCash.setUserId(1146);
    	rechargeCash.setRechargeFlag(0);
    	rechargeCash.setTransMoney(new BigDecimal("1"));
    	rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt("3")));
    	rechargeCash.setPlatformSourceName("IOS");
    	rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
    	rechargeCash.setBankCard("6222020200106704618");
    	rechargeCash.setTel("15011101965");
    	rechargeCash.setUserName("梁彬");
    	rechargeCash.setIdCard("370921198804075739");
    	rechargeCash.setuType(1);
    	rechargeCash.setBankCode("ICBC");
    	rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
    	rechargeCash.setRegisterTime(new Date());
    	rechargeCash.setLoginTel("15011101965");
    	this.finConsumptionService.rechargeCash(rechargeCash);
    }
    
}
