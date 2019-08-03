package com.hongkun.finance.bi.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.yirun.framework.core.utils.pager.Pager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description : 平台对账服务测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-bi.xml"})
public class BalAccountFacadeImplTest {

    @Reference
    private BalAccountFacade balAccountFacade;

    @Test
    public void initBalAccount(){
        balAccountFacade.initBalAccount("13611037040",1,1,new Pager());
    }
    @Test
    public void initBalAccountForChange(){
        balAccountFacade.initBalAccountForChange();
    }
}
