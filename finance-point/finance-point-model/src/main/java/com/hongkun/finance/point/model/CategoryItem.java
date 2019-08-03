package com.hongkun.finance.point.model;

import com.hongkun.finance.point.support.CategoryComponent;

/**
 * @Description : 代表了分类中的一级目录
 * @Project : finance
 * @Program Name : com.hongkun.finance.point.model.Category
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class CategoryItem extends CategoryComponent {


    public CategoryItem(Integer parentId, Integer id, String title,Integer sort) {
        super(parentId, id, title,sort);
    }
}
