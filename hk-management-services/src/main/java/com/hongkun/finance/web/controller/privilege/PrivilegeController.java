package com.hongkun.finance.web.controller.privilege;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.SysPrivilege;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.user.support.security.SecurityConstants.*;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理权限元数据以及其他相关的控制器
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.PrivilegeController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/privilegeController")
public class PrivilegeController {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
    @Reference
    private PrivilegeSrvice privilegeSrvice;


    /**
     * 获取所有的reids扫描的url
     * @return
     */
    @RequestMapping("/scannedPrivileges")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public ResponseEntity scannedPrivileges(){
        List<String> managePris = JedisClusterUtils.getList(AUTH_URLS,0,-1);
        List<String> hkjfPris = JedisClusterUtils.getList(AUTH_URLS_HKJF,0,-1);
        List<String> biPris = JedisClusterUtils.getList(AUTH_URLS_BI,0,-1);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("managePris",managePris);
        params.put("hkjfPris",hkjfPris);
        params.put("biPris",biPris);
        return new ResponseEntity(SUCCESS,"获取权限列表成功",params );
    }

    /**
     * 获取所有已经存入数据库的url
     * @return
     */
    @RequestMapping("/listPrivileges")
    @ResponseBody
    public ResponseEntity listPrivileges(SysPrivilege sysPrivilege, Pager pager){
        return new ResponseEntity(SUCCESS,this.privilegeSrvice.listPrivileges(sysPrivilege,pager));
    }

    /**
     * 获取所有已经存入数据库的url,不带分页
     * @return
     */
    @RequestMapping("/listPrivilegesNoPager")
    @ResponseBody
    public ResponseEntity listPrivilegesNoPager(){
        return new ResponseEntity(SUCCESS,this.privilegeSrvice.listPrivilegesNoPager());
    }


    /**
     * 保存一个权限
     * @param sysPrivilege
     * @return
     */
    @RequestMapping("/savePrivilege")
    @ResponseBody
    @ActionLog(msg = "保存权限, 权限名称: {args[0].privName}, 权限url: {args[0].privUrl}")
    public ResponseEntity savePrivilege(@Validated(SAVE.class) SysPrivilege sysPrivilege){
        ResponseEntity result = privilegeSrvice.saveOrUpdatePrivilege(sysPrivilege, true);
        return result;
    }

    /**
     * 修改一个权限
     * @param sysPrivilege
     * @return
     */
    @RequestMapping("/updatePrivilege")
    @ResponseBody
    @ActionLog(msg = "修改权限,修改权限id: {args[0].id} 修改后权限名称: {args[0].privName}, 修改后权限url: {args[0].privUrl}")
    public ResponseEntity updatePrivilege(@Validated(UPDATE.class) SysPrivilege sysPrivilege){
        return privilegeSrvice.saveOrUpdatePrivilege(sysPrivilege,false);
    }

    /**
     * 删除一个权限
     * @return
     */
    @RequestMapping("/deletePrivilege")
    @ResponseBody
    @ActionLog(msg = "删除权限, 权限id: {args[0].id}")
    public ResponseEntity deletePrivilege(@Validated(DELETE.class) SysPrivilege sysPrivilege){
        return privilegeSrvice.deletePrivilege(sysPrivilege);
    }





}
