package com.hongkun.finance.test.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.test.BaseTest;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.utils.pager.Pager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 测试会员降级
 *
 * @author zc.ding
 * @create 2018/6/7
 */
public class TestRegUserVipRecords extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(TestRegUserVipRecords.class);
    @Reference
    private UserFacade userFacade;
    @Reference
    private RegUserService regUserService;
    
    @Test
    public void testUpdateRegUserVip(){
        this.userFacade.updateRegUserVip();
    }
    
    @Test
    public void test(){
        UserVO userVO = new UserVO();
        Pager pager = new Pager();
        Pager result = regUserService.findRegUserVipRecordList(userVO, pager);
        logger.info("{}", result);
    }

    @Test
    public  void sysRoleTest(){
        RegUser loginUser = new RegUser();
        loginUser.setId(1139);
        loginUser.setType(2);
        List<String> stringList =  regUserService.findUserMenuIdsWithTemplateBackUp(loginUser);
        for (String s:stringList){
            System.out.print(s);
        }
    }
}
