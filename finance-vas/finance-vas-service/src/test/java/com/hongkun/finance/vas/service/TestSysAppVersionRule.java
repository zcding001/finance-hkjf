package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.constants.AppVersionConstants;
import com.hongkun.finance.vas.model.SysAppVersionRule;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class TestSysAppVersionRule extends BaseTest{
	
	private final Logger LOG = Logger.getLogger(TestSysAppVersionRule.class);
	
	@Autowired
	private SysAppVersionRuleService sysAppVersionRuleService;
	
	private SysAppVersionRule getSysAppVersionRule(){
		SysAppVersionRule model = new SysAppVersionRule();
		return model;
	}

	/**
	 * @Described			: 测试单条插入
	 * @return				: void
	 */
	@Test
	public void testAdd(){
		SysAppVersionRule model = getSysAppVersionRule();
		model.setMinVersion("2.5.1");
		model.setMaxVersion("2.5.8");
		this.sysAppVersionRuleService.insertSysAppVersionRule(model);
	}

	/**
	 * @Described			: 测试批量插入
	 * @return				: void
	 */
	@Test
	public void testAddBatch(){
		List<SysAppVersionRule> list = new ArrayList<>();
		SysAppVersionRule model = getSysAppVersionRule();
		model.setMinVersion("2.6.1");
		model.setMaxVersion("2.6.8");
		model.setPlatform("1");
		SysAppVersionRule model1 = getSysAppVersionRule();
		model1.setMinVersion("2.4.1");
		model1.setMaxVersion("2.4.8");
		model1.setPlatform("2");
		list.add(model);
		list.add(model1);
		int result = this.sysAppVersionRuleService.insertSysAppVersionRuleBatch(list);
		LOG.info("result: " + result );
	}

	@Test
	public void testFindSysAppVersionRuleCount(){
		SysAppVersionRule condition = new SysAppVersionRule();
		condition.setPlatform("2");
		condition.setVersion("2.5.0");
		condition.setState(AppVersionConstants.APP_VERSION_STATE_UP);
		int result = this.sysAppVersionRuleService.findSysAppVersionRuleCount(condition,".findRule");
		LOG.info("result: " + result );
	}
	/**
	 * @Described			: 测试更新
	 * @return				: void
	 */
	@Test
	public void testUpdate(){
		SysAppVersionRule model = this.sysAppVersionRuleService.findSysAppVersionRuleById(1);
		this.sysAppVersionRuleService.updateSysAppVersionRule(model);
	}

	/**
	 * @Described			: 测试分页条件检索
	 * @return				: void
	 */
	@Test
	public void testFindByCondition(){
		SysAppVersionRule model = this.sysAppVersionRuleService.findSysAppVersionRuleById(1);
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(10);
		pager.setStartNum(1);
		Pager result = this.sysAppVersionRuleService.findSysAppVersionRuleList(model, pager);
		LOG.debug(result.getData());
	}

	@Test
	public void testFindRuleCountByVersion(){
		sysAppVersionRuleService.findRuleCountByVersion("1","2.5.2");
	}
}