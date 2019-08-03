package com.hongkun.finance.web.support.authvalidators;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.TimeRuledAuthValidator;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FakePreValidator implements TimeRuledAuthValidator {

    @Override
    public boolean doPreAuthValidate(RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
      /*  System.out.println("\n\n\n--------------------->验证开始");
        System.out.println("当验证操作:" + AuthUtil.cleanUserRequestResource(request)+"\n" +
                "验证时间:"+(isPostValidate()?"后置\n":"前置\n")+
                "优先级:"+getOrder());
        System.out.println("--------------------->I am FakePreValidator");
        System.out.println("--------------------->验证结束\n\n\n");*/
        return true;
    }
}
