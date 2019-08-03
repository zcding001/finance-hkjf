package com.hongkun.finance.point.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 目录结构实体类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.model.CategoryComponent
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class CategoryNode implements Serializable {

    private Integer id;

    private String title;

    private Integer parentId;

    private List<CategoryNode> nodes = new ArrayList<>();

    public CategoryNode() {
    }

    public CategoryNode(Integer id, String title,Integer parentId) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CategoryNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<CategoryNode> nodes) {
        this.nodes = nodes;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
