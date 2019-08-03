package com.hongkun.finance.user.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.model.sysmenu.java
 * @Class Name    : sysmenu.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class SysMenu extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 描述: 主键
     * 字段: id  INT(10)
     */
    @NotNull(message = "请指定菜单",groups = {UPDATE.class, DELETE.class})
    private java.lang.Integer id;

    /**
     * 描述: 上级菜单ID，第一级菜单pid为0
     * 字段: pid  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer pid;

    /**
     * 描述: 菜单名称
     * 字段: menu_name  VARCHAR(50)
     * 默认值: ''
     */
    @NotNull(message = "请输入菜单名称",groups = {SAVE.class,UPDATE.class})
    private java.lang.String menuName;

    /**
     * 描述: 菜单描述
     * 字段: menu_desc  VARCHAR(100)
     * 默认值: ''
     */
    @NotNull(message = "请指定菜单描述",groups = {SAVE.class,UPDATE.class} )
    private java.lang.String menuDesc;

    /**
     * 描述: 菜单所链接到的地址
     * 字段: menu_url  VARCHAR(100)
     * 默认值: ''
     */
    private java.lang.String menuUrl;

    /**
     * 描述: 排序号
     * 字段: seq_no  SMALLINT(5)
     * 默认值: 0
     */
    @NotNull(message = "请指定排序号",groups = {SAVE.class,UPDATE.class})
    private Integer seqNo;

    /**
     * 描述: 菜单划分那个平台属性: 1-鸿坤金服后台 ,2-我的账户
     * 字段: type  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请指定菜单所属平台",groups = {SAVE.class,UPDATE.class})
    private Integer type;

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


    //【非数据库字段，构造级联菜单时候使用】
    private List<SysMenu> childMenu;

    //【非数据库字段，父菜单名称】
    private String parentMenuName;


    public SysMenu() {
    }

    public SysMenu(Integer id,Integer pid) {
         this.id=id;
        this.pid = pid;
    }

    public SysMenu(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setPid(java.lang.Integer pid) {
        this.pid = pid;
    }

    public java.lang.Integer getPid() {
        return this.pid;
    }

    public void setMenuName(java.lang.String menuName) {
        this.menuName = menuName;
    }

    public java.lang.String getMenuName() {
        return this.menuName;
    }

    public void setMenuDesc(java.lang.String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public java.lang.String getMenuDesc() {
        return this.menuDesc;
    }

    public void setMenuUrl(java.lang.String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public java.lang.String getMenuUrl() {
        return this.menuUrl;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getSeqNo() {
        return this.seqNo;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
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

    public List<SysMenu> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<SysMenu> childMenu) {
        this.childMenu = childMenu;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

