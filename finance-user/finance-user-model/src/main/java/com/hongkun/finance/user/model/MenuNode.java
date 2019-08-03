package com.hongkun.finance.user.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 菜单节点
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.model.MenuNode
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class MenuNode implements Serializable {

    private Integer seqNo;
    private String levelOneName;
    private List<SysMenu> childNode;

    public MenuNode() {
    }

    public MenuNode(String levelOneName,Integer seqNo) {
        this.levelOneName = levelOneName;
        this.seqNo = seqNo;
    }

    public String getLevelOneName() {
        return levelOneName;
    }

    public void setLevelOneName(String levelOneName) {
        this.levelOneName = levelOneName;
    }

    public List<SysMenu> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<SysMenu> childNode) {
        this.childNode = childNode;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
