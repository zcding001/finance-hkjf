package com.hongkun.finance.web.controller.privilege;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_CONSOLE;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ROOT;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 后台用户管理controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.MenuController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("/operatorManageController")
public class OperatorManageController {
    private static final Logger LOGGER = Logger.getLogger(OperatorManageController.class);

    @Reference
    private RegUserService userService;


    /**
     * 列出分页菜单
     * @return
     */
    @RequestMapping("/listAllOperators")
    @ResponseBody
    public ResponseEntity listAllOperators(RegUser regUser,Pager pager){
        //限定后台用户
        regUser.setType(USER_TYPE_CONSOLE);
        return new ResponseEntity(SUCCESS, userService.findRegUserList(regUser, pager));
    }

    /**
     * 保存后台用户
     * @return
     */
    @RequestMapping("/addOperator")
    @ResponseBody
    @ActionLog(msg = "添加后台操作用户, 用户登录账号: {args[0].login}")
    public ResponseEntity<?> addOperator(@Validated(SAVE.class) RegUser regUser, @RequestParam("email")String email){
    	ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(String.valueOf(regUser.getLogin()), regUser.getPasswd());
    	if(result.getResStatus() == Constants.SUCCESS) {
    		regUser.setPasswd(String.valueOf(result.getResMsg()));
    		regUser.setType(UserConstants.USER_TYPE_CONSOLE);
    		//对后台人员的手机号进行处理，隐去前三位，变成888
            String newLogin="888"+String.valueOf(regUser.getLogin()).substring(3);
            regUser.setLogin(Long.valueOf(newLogin));
    		return this.userService.saveOrUpdateOperator(regUser, email);
    	}
        return result;
    }


    /**
     * 修改后台用户
     * @return
     */
    @RequestMapping("/updateOperator")
    @ResponseBody
    @ActionLog(msg = "修改后台用户, 用户id: {args[0].id}, 用户登录账号: {args[0].login}")
    public ResponseEntity<?> updateOperator(@Validated(UPDATE.class) RegUser regUser, @RequestParam(value = "email",required = false)String email){
    	ResponseEntity<?> result = ValidateUtil.validateLogin(String.valueOf(regUser.getLogin()));
    	if(result.getResStatus() == Constants.SUCCESS) {
    		return this.userService.saveOrUpdateOperator(regUser, email);
    	}
        return result;
    }


    /**
     * 删除后台用户
     * @return
     */
    @RequestMapping("/deleteOperator")
    @ResponseBody
    @ActionLog(msg = "删除后台用户, 用户id: {args[0].id}, 用户登录账号: {args[0].login}")
    public ResponseEntity deleteOperator(@Validated(DELETE.class) RegUser regUser){
        return userService.deleteOperator(regUser);
    }



    /**
     * 给后台用户分配角色
     * @return
     */
    @RequestMapping("/bindRolesToUser")
    @ResponseBody
    @ActionLog(msg = "给后台用户分配角色, 用户id: {args[0]}, 绑定角色IDs: {args[1]}")
    public ResponseEntity bindRolesToUser(@RequestParam("id")Integer userId, @RequestParam("roleIds")Set<Integer> roleIds){
        return userService.bindRolesToUser(userId, roleIds,BaseUtil.getLoginUserId());
    }


    /**
     * 查询用户已经授予的权限
     * @return
     */
    @RequestMapping("/findRoleBindToUser")
    @ResponseBody
    public ResponseEntity findRoleBindToUser(@RequestParam("userId") Integer userId){
        return new ResponseEntity(SUCCESS, userService.findRoleBindToUser(userId));
    }
    /**
     * 后台用户修改密码
     * @return
     */
    @RequestMapping("/changePasswd")
    @ResponseBody
    @ActionLog(msg = "修改后台用户密码")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public ResponseEntity changePasswd(@RequestParam("passwd") String newPasswd){
        RegUser currentUser = BaseUtil.getLoginUser();
        //只允许超级管理员和后台管理人员修改密码
        if(equelsIntWraperPrimit(currentUser.getType(),USER_TYPE_CONSOLE) ||equelsIntWraperPrimit(currentUser.getType(),USER_TYPE_ROOT) ){
            return userService.changeOperatorPasswd(currentUser,newPasswd);
        }
        return  ResponseEntity.SUCCESS;
    }

}
