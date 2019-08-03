package com.hongkun.finance.user.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.model.SysRole.java
 * @Class Name    : SysRole.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class SysRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 描述: 主键
     * 字段: id  INT(10)
     */

    @NotNull(message = "请指定角色",groups = {DELETE.class,UPDATE.class})
    private java.lang.Integer id;

    /**
     * 描述: 角色名称
     * 字段: role_name  VARCHAR(50)
     * 默认值: ''
     */
    @NotNull(message = "请指定角色名称",groups = {SAVE.class,UPDATE.class})
    private java.lang.String roleName;

    /**
     * 描述: 角色描述
     * 字段: role_desc  VARCHAR(100)
     * 默认值: '0'
     */
    @NotNull(message = "请指定角色描述",groups = {SAVE.class,UPDATE.class})
    private java.lang.String roleDesc;

    /**
     * 描述: 状态:0-删除,1-正常
     * 字段: state  TINYINT(3)
     * 默认值: 0
     */
    private Integer state;

    /**
     * 描述: 创建时间
     * 字段: create_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date createTime;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeEnd;
    /**
     * 描述: 修改时间
     * 字段: modify_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeEnd;

    public SysRole() {
    }

    public SysRole(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }

    public java.lang.String getRoleName() {
        return this.roleName;
    }

    public void setRoleDesc(java.lang.String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public java.lang.String getRoleDesc() {
        return this.roleDesc;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTimeBegin(java.util.Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public java.util.Date getCreateTimeBegin() {
        return this.createTimeBegin;
    }

    public void setCreateTimeEnd(java.util.Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public java.util.Date getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public java.util.Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
        this.modifyTimeBegin = modifyTimeBegin;
    }

    public java.util.Date getModifyTimeBegin() {
        return this.modifyTimeBegin;
    }

    public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
        this.modifyTimeEnd = modifyTimeEnd;
    }

    public java.util.Date getModifyTimeEnd() {
        return this.modifyTimeEnd;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

