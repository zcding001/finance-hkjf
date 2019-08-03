package com.hongkun.finance.user.support.security.component.operationcommands;

import com.hongkun.finance.user.support.security.AbstractOperationCommand;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.hongkun.finance.user.support.security.OperationTypeEnum.DO_LOGIN;
/**
 * @Description : 执行登录操作的执行器,目前登录命令没有额外功能,所以作为保留命令
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.operationcommands.DoLoginCommand
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class DoLoginCommand extends AbstractOperationCommand {

    public DoLoginCommand() {
        super(Arrays.asList(DO_LOGIN));
    }


   /**
   *  @Description    ：登录操作描述
   *  @Method_Name    ：doOpertion
   *  @param resultResponseEntity
   *  @param typeEnum
   *  @param request
   *  @param response
   *  @param workInConsole
   *  @return boolean
   *  @Creation Date  ：2018/4/12
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    @Override
    public boolean doOpertion(Object resultResponseEntity, OperationTypeEnum typeEnum, HttpServletRequest request, HttpServletResponse response, boolean workInConsole) {
       /* 如果还没登录，后置处理器进行调用
        if (!userHasLogin(request)) {
            //返回没有登录
            notifyPostCallExecutor(request, typeEnum);
            return true;
        }*/

        return true;
    }
}
