package com.hongkun.finance.user.service;

import com.hongkun.finance.user.dto.PrivilgeDTO;
import com.hongkun.finance.user.model.SysPrivilege;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Description : 权限元数据操作servcie
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.PrivilegeSrvice
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PrivilegeSrvice {

    /**
     *  @Description    : 保存或者修改一个权限
     *  @Method_Name    : saveOrUpdatePrivilege
     *   @param sysPrivilege: 权限信息
     *   @param saveFlag:  是否保存
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity saveOrUpdatePrivilege(SysPrivilege sysPrivilege, Boolean saveFlag);



    /**
     *  @Description    : 列出所有保存的权限
     *  @Method_Name    : listPrivileges
     *   @param sysPrivilege: 权限信息
     *   @param pager:  分页信息
     *  @return          : Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager listPrivileges(SysPrivilege sysPrivilege, Pager pager);


    /**
     *  @Description    : 获取不带分页的权限
     *  @Method_Name    : listPrivilegesNoPager
     *  @return          : Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<PrivilgeDTO> listPrivilegesNoPager();

    /**
     *  @Description    : 删除权限
     *  @Method_Name    : deletePrivilege
     *   @param sysPrivilege: 权限信息
     *  @return          : Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity deletePrivilege(SysPrivilege sysPrivilege);

    /**
    *  @Description    ：根据权限的集合找到对应url
    *  @Method_Name    ：finAuthUrlsByIds
    *  @param authIds
    *  @return java.util.List<java.lang.String>
    *  @Creation Date  ：2018/6/11
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<String> finAuthUrlsByIds(List<String> authIds);
}
