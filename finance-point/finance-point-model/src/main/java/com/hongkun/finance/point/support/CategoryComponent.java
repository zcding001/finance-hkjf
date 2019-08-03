package com.hongkun.finance.point.support;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * @Description : 抽象的目录节点-组合模式, 角色,节点
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.model.CategoryComponent
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public abstract class CategoryComponent implements Serializable {

    /**
     *parentId默认为0
     */
    private Integer parentId;

    /**
     *当前节点id
     */
    private Integer id;

    /**
     *当前菜单的名称
     */
    private String title;
    /**
     * 菜单排序号
     */
    private Integer sort;



    public CategoryComponent(Integer parentId, Integer id, String title,Integer sort) {
        this.parentId = parentId;
        this.id = id;
        this.title = title;
        this.sort = sort;
    }
    /**      
     *  @Description    : 获取排序号
     *  @Method_Name    : getSort;
     *  @return
     *  @return         : Integer;
     *  @Creation Date  : 2018年11月1日 下午3:12:18;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public Integer getSort() {return sort;}
    /**
     *  @Description    : 设置排序号
     *  @Method_Name    : setSort;
     *  @param sort
     *  @return         : void;
     *  @Creation Date  : 2018年11月1日 下午3:16:12;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public void setSort(Integer sort) {this.sort = sort;}

    /**
    *  @Description    ：返回当前分类的id
    *  @Method_Name    ：getId
    *
    *  @return java.lang.Integer
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public Integer getId() {return id;}

    /**
    *  @Description    ：获取当前节点的标题(分类名称)
    *  @Method_Name    ：getTitle
    *
    *  @return java.lang.String
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public String getTitle() {
        return title;
    }
    /**
    *  @Description    ：设置id
    *  @Method_Name    ：setId
    *  @param id
    *  @return void
    *  @Creation Date  ：2018/4/18
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
    *  @Description    ：设置标题
    *  @Method_Name    ：setTitle
    *  @param title
    *  @return void
    *  @Creation Date  ：2018/4/18
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
    *  @Description    ：获取子节点
    *  @Method_Name    ：getNodes
    *
    *  @return java.util.List<com.hongkun.finance.point.support.CategoryComponent>
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public Set<CategoryComponent> getNodes() {
        return null;
    }


    /**
    *  @Description    ：维护一个指向父节点的指针
    *  @Method_Name    ：getParentId
    *
    *  @return java.lang.Integer
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public Integer getParentId() {
        return parentId;
    }

    /**
    *  @Description    ：设置父节点
    *  @Method_Name    ：setParentId
    *  @param parentId
    *  @return void
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
    *  @Description    ：为当前节点添加子节点
    *  @Method_Name    ：addNode
    *  @param child
    *  @return void
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void addNode(CategoryComponent child){}


    /**
    *  @Description    ：创建一个迭代器
    *  @Method_Name    ：createIterator
    *
    *  @return java.util.Iterator<com.hongkun.finance.point.support.CategoryComponent>
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public Iterator<CategoryComponent> createIterator(){return new NullIterator();}


    /**
     *复写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryComponent)) return false;
        CategoryComponent that = (CategoryComponent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
