package com.hongkun.finance.user.dto;

import java.util.List;

/**
 * @Description : 角色和菜单的关系的dto
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.dto.MenuPriDTO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class RoleMenuDTO {

    private List<Integer> roleIds;

    private List<Integer> menuIds;

    public RoleMenuDTO() {
    }

    public RoleMenuDTO(List<Integer> roleIds, List<Integer> menuIds) {
        this.roleIds = roleIds;
        this.menuIds = menuIds;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Integer> menuIds) {
        this.menuIds = menuIds;
    }
}


