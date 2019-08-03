package com.hongkun.finance.user.dto;

import java.util.List;

/**
 * @Description : 用户与角色之间的DTO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.dto.UserRoleDTO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class UserRoleDTO {
    private List<Integer>   userIds;
    private List<Integer>   roleIds;

    public UserRoleDTO() {
    }

    public UserRoleDTO(List<Integer> userIds, List<Integer> roleIds) {
        this.userIds = userIds;
        this.roleIds = roleIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
