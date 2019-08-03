package com.hongkun.finance.user.dto;

import com.hongkun.finance.user.model.SysMenu;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.model.sysmenu.java
 * @Class Name    : sysmenu.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class MenuDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 描述: 主键
     * 字段: id  INT(10)
     */
    private Integer id;

    /**
     * 描述: 上级菜单ID，第一级菜单pid为0
     * 字段: pid  INT(10)
     * 默认值: 0
     */
    private Integer pid;

    /**
     * 描述: 菜单名称
     * 字段: menu_name  VARCHAR(50)
     * 默认值: ''
     */
    private String menuName;

    /**
     * 描述: 菜单描述
     * 字段: menu_desc  VARCHAR(100)
     * 默认值: ''
     */
    private String menuDesc;

    /**
     * 描述: 菜单所链接到的地址
     * 字段: menu_url  VARCHAR(100)
     * 默认值: ''
     */
    private String menuUrl;

    /**
     * 描述: 排序号
     * 字段: seq_no  SMALLINT(5)
     * 默认值: 0
     */
    private Integer seqNo;

    /**
     * 描述: 菜单划分那个平台属性:0-乾坤袋,1-前生活,2-我的账户,3-鸿坤金服
     * 字段: type  TINYINT(3)
     * 默认值: 0
     */
    private Integer type;

    /**
     * 描述: 状态:0-删除,1-正常
     * 字段: state  TINYINT(3)
     * 默认值: 0
     */
    private Integer state;

    //【非数据库字段，父菜单名称】
    private String parentMenuName;

    public MenuDTO() {
    }

    public MenuDTO(SysMenu menu) {
        initDTO(menu);
    }

    /**
     * 初始化本对象
     * @param menu
     */
    private void initDTO(SysMenu menu) {
        BeanPropertiesUtil.splitProperties(menu, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

