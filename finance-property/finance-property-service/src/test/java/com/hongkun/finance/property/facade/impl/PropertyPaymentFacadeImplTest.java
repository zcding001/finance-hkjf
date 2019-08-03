package com.hongkun.finance.property.facade.impl;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.utils.pager.Pager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo-provider.xml",
		"classpath:spring/applicationContext-property.xml" })
public class PropertyPaymentFacadeImplTest {
	@Reference
	private PropertyPaymentFacade propertyPaymentFacade;
	@Test
	@Ignore
	public void propertyPayment(){
		RegUser regUser = new RegUser();
//		regUser.setId(14);
//		ProPayRecord proPayRecord = new ProPayRecord();
//		proPayRecord.setPropertyId(17);
//		proPayRecord.setRegUserId(14);
//		proPayRecord.setPoint(100);
//		proPayRecord.setPayType(1);
//		proPayRecord.setAddress("123");
//		proPayRecord.setMoney(new BigDecimal(100));
//		propertyPaymentFacade.propertyPayment(regUser, proPayRecord,"");
	}
	@Test
	@Ignore
	public void auditProPayment(){
		RegUser regUser = new RegUser();
		regUser.setId(13);
		propertyPaymentFacade.auditProPayment(10, "审核通过", 4, regUser);
	}
	@Test
	public void findPropertyRecordList(){
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setStartNum(1);
		pager.setPageSize(10);
		ProPayRecord proPayRecord = new ProPayRecord();
		propertyPaymentFacade.findPropertyRecordList(proPayRecord, pager);
	}
	@Test
	@Ignore
	public void findPropertyStaffList(){
		RegUser regUser = new RegUser();
		regUser.setId(17);
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setStartNum(1);
		pager.setPageSize(10);
		
		RosStaffInfo rosStaffInfo = new RosStaffInfo();
		propertyPaymentFacade.findPropertyStaffList(rosStaffInfo, pager, regUser);
	}
}
