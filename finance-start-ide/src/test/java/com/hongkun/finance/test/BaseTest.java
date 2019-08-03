package com.hongkun.finance.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext-dubbo-provider.xml", "classpath:spring/applicationContext-jms.xml"})
public class BaseTest {
    
}
