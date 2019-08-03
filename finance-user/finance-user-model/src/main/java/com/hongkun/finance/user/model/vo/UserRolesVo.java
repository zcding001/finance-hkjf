package com.hongkun.finance.user.model.vo;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Description : TODO
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class UserRolesVo extends BaseModel {

    private Integer  id ;
    private Integer  regUserId;
    private String login;
    private Integer sysRoleId;
    private Integer state;
    private String realName;
    private String sysRoleName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(Integer regUserId) {
        this.regUserId = regUserId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Integer sysRoleId) {
        this.sysRoleId = sysRoleId;
    }



    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getSysRoleName() {
        return sysRoleName;
    }

    public void setSysRoleName(String sysRoleName) {
        this.sysRoleName = sysRoleName;
    }
}
