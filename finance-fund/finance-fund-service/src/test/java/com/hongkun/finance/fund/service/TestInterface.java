package com.hongkun.finance.fund.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.yirun.framework.core.utils.pager.Pager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-fund.xml" })
public class TestInterface {

	@Autowired
	private FundInfoService fundInfoService;
	@Reference
	private FundInfoFacade fundInfoFacade;
	@Test
	public void testFindFundInfoVoByCondition() {
		FundInfoVo vo =new FundInfoVo();
		fundInfoService.findFundInfoVoByCondition(vo,new Pager());
	}
}
