package com.hongkun.finance.point.model;

import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.point.support.CategoryIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Description : 代表了分类中具有子节点的目录
 * @Project : finance
 * @Program Name : com.hongkun.finance.point.model.Category
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class Category  extends CategoryComponent {

    /**
     *子节点
     */
    Set<CategoryComponent> nodes = new LinkedHashSet<>();


    public Category(Integer parentId, Integer id, String title,Integer sort) {
        super(parentId, id, title,sort);
    }


    @Override
    public Set<CategoryComponent> getNodes() {
        return nodes;
    }

    public void addNode(CategoryComponent child){
        nodes.add(child);
    }

    @Override
    public Iterator<CategoryComponent> createIterator() {
        return new CategoryIterator(nodes.iterator());
    }

}
