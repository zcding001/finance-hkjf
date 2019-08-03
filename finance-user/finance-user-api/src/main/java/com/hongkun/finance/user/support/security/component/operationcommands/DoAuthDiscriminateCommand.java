package com.hongkun.finance.user.support.security.component.operationcommands;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.AbstractLoginNeededOperationCommand;
import com.hongkun.finance.user.support.security.DefaultLoginCalculator;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.hongkun.finance.user.support.security.OperationTypeEnum.*;
/**
 * @Description :抽象鉴权命令鉴权命令,支持的命令:
 * DISCRIMINATE_AUTHORITY:执行权限鉴别
 * DISCRIMINATE_LOGIN:需要登录级别的权限
 * DISCRIMINATE_NO_LOGIN:不需要登录的权限
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.operationcommands.DoAuthDiscriminateCommand
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class DoAuthDiscriminateCommand extends AbstractLoginNeededOperationCommand {
    public DoAuthDiscriminateCommand() {
        super(Arrays.asList(
                DISCRIMINATE_AUTHORITY,
                DISCRIMINATE_LOGIN,
                DISCRIMINATE_NO_LOGIN),new DefaultLoginCalculator());
    }


    /**
    *  @Description    ：权限鉴别描述
    *  @Method_Name    ：afterLoginIntecepter
    *  @param resultResponseEntity controller方法返回结果
    *  @param operationType 操作类型
    *  @param request
    *  @param response
    *  @param workInConsole 是否工作在后台
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Override
    public boolean afterLoginIntecepter(Object resultResponseEntity,RegUser currentUser, OperationTypeEnum operationType, HttpServletRequest request, HttpServletResponse response, boolean workInConsole) {
        return findReceiver(currentUser.getType()).map(receiver -> receiver.validateUserHasPermission(resultResponseEntity,currentUser,operationType, workInConsole, request, response)).orElse(false);
    }
}
