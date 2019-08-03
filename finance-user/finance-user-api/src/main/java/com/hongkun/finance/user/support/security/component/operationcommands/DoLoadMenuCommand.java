package com.hongkun.finance.user.support.security.component.operationcommands;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.AbstractLoginNeededOperationCommand;
import com.hongkun.finance.user.support.security.DefaultLoginCalculator;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.hongkun.finance.user.support.security.AuthUtil.responseRequestBreak;
import static com.hongkun.finance.user.support.security.OperationTypeEnum.DO_LOAD_MENU;
import static com.yirun.framework.core.commons.Constants.SUCCESS;


/**
 * @Description :加载菜单执行器
 * DO_LOAD_MENU：执行菜单加载
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.operationcommands.DoLoadMenuCommand
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class DoLoadMenuCommand extends AbstractLoginNeededOperationCommand {
    private static final Logger logger = LoggerFactory.getLogger(DoLoadMenuCommand.class);


    public DoLoadMenuCommand() {
        super(Arrays.asList(DO_LOAD_MENU), new DefaultLoginCalculator());
    }



    /**
    *  @Description    ：执行加载菜单的命令描述
    *  @Method_Name    ：afterLoginIntecepter
    *  @param resultResponseEntity
    *  @param operationType 操作类型
    *  @param request
    *  @param response
    *  @param workInConsole 是否工作在后台
    *  @return boolean  验证是否通过(作为拦截器的一部分)
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Override
    public boolean afterLoginIntecepter(Object resultResponseEntity,RegUser loginUser, OperationTypeEnum operationType, HttpServletRequest request, HttpServletResponse response, boolean workInConsole) {
        //加载菜单操作
        findReceiver(loginUser.getType()).map((receiver) -> {
            //直接返回用户菜单
            String sysType = (String) request.getAttribute("sysType");
            return responseRequestBreak(response, new ResponseEntity(SUCCESS, receiver.loadUserSendUserMenus(loginUser,Integer.parseInt((sysType) ) )));
        });
        return false;
    }


}
