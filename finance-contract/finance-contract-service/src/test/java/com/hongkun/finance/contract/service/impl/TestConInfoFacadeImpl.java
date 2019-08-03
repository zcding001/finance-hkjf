package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : ConInfoFacadeImpl测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.service.impl.TestConInfoFacadeImpl
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class TestConInfoFacadeImpl extends TestConInfo{

    @Reference
    private ConInfoFacade conInfoFacade;

    private static final Logger logger = LoggerFactory.getLogger(TestConInfoFacadeImpl.class);

    @Test
    public void testGenerateContract(){
        List<Integer> list = new ArrayList<>();
        list.add(657);
        list.add(658);
        list.add(659);
        list.add(660);
        conInfoFacade.generateContract(list);
    }
}
