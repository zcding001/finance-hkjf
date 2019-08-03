package com.hongkun.finance.bi.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.StaIncomeFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.facade.impl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-bi.xml"})
public class StaIncomeFacadeImplTest {

        @Reference
        private StaIncomeFacade staIncomeFacade;

        @Test
        public  void incomSta(){
            staIncomeFacade.initStaIncomes();
        }
}
