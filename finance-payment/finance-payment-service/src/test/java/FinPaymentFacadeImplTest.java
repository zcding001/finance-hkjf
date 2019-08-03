

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
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
public class FinPaymentFacadeImplTest {
	
    @Reference
    private FinPaymentFacade finPaymentFacade;
    
    @Test
    public void paymentVerificationCode(){  //短验
    	RechargeCash rechargeCash = new RechargeCash();
    	rechargeCash.setBankCardId(23);
    	rechargeCash.setUserId(78);
    	rechargeCash.setRechargeFlag(0);
    	rechargeCash.setTransMoney(new BigDecimal("1"));
    	rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt("4")));
    	rechargeCash.setPlatformSourceName("PC");
    	rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
    	rechargeCash.setBankCard("6222020200106704618");
    	rechargeCash.setTel("15011101965");
    	rechargeCash.setUserName("梁彬");
    	rechargeCash.setIdCard("370921198804075739");
    	rechargeCash.setuType(1);
    	rechargeCash.setBankCode("ICBC");
    	rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
    	this.finPaymentFacade.paymentVerificationCode(rechargeCash);
    }
    
	  @Test
	  public void confirmPay(){ //协议支付
	  	RechargeCash rechargeCash = new RechargeCash();
	  	rechargeCash.setOperateType(1);
	  	rechargeCash.setPayUnionCode("201805181715283099772");
	  	rechargeCash.setVerificationCode("541019");
	  	rechargeCash.setBankCardId(23);
	  	rechargeCash.setUserId(78);
	  	rechargeCash.setRechargeFlag(0);
	  	rechargeCash.setTransMoney(new BigDecimal("0.01"));
	  	rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt("4")));
	  	rechargeCash.setPlatformSourceName("PC");
	  	rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
	  	rechargeCash.setBankCard("6222020200106704618");
	  	rechargeCash.setTel("15011101965");
	  	rechargeCash.setUserName("梁彬");
	  	rechargeCash.setIdCard("370921198804075739");
	  	rechargeCash.setuType(1);
	  	rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
	  	rechargeCash.setBankCode("ICBC");

	  	this.finPaymentFacade.confirmPay(rechargeCash);
	  }
    
}
