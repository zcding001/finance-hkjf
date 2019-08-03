package com.hongkun.finance.web.support.authvalidators;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.TimeRuledAuthValidator;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 */
public class FakePostValidator implements TimeRuledAuthValidator {


    @Override
    public boolean isPostValidate() {
        return true;
    }

    @Override
    public boolean doPostAuthValidate(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
      /*  System.out.println("\n\n\n--------------------->验证开始");
        System.out.println("当验证操作:" + AuthUtil.cleanUserRequestResource(request)+"\n" +
                "验证时间:"+(isPostValidate()?"后置\n":"前置\n")+
                "优先级:"+getOrder());
        System.out.println("--------------------->I am FakePostValidator");
        System.out.println("--------------------->验证结束\n\n\n");*/
         return true;
    }
}
