package com.hongkun.finance.point.util;

import com.hongkun.finance.point.model.Category;
import com.hongkun.finance.point.model.CategoryItem;
import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.point.support.CategoryIterator;
import com.hongkun.finance.user.utils.BaseUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Description : 目录构造器
 * @Project : finance
 * @Program Name : com.hongkun.finance.point.util.CategoryBuilder
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class CategoryBuilder {

    /**
     * 所有的原始目录
     */
    private Set<PointProductCategory> orginCates =new LinkedHashSet<>(125);

    /**
     * 是否已经解析过所有的目录
     */
    private boolean orginCatesHasUsed=false;

    /**
     * 已经构建过的组件
     */
    private List<CategoryComponent> buildComponent;

    /**
     * 存储Id和Map的映射
     */
    private Map<Integer,PointProductCategory> groupedIdMap=Collections.emptyMap();

    /**
     * 根据parentId分的Map
     */
    private Map<Integer,List<PointProductCategory>> groupedParentCate=Collections.emptyMap();


    public CategoryBuilder() {
        /*
           empty
         */
    }

    public CategoryBuilder(Set<PointProductCategory> allCates) {
        this.orginCates = allCates;
        //使用构造器说明一次性传递进入所有的分类
        lockOrginCates();
    }

    /**
    *  @Description    ：添加原始目录
    *  @Method_Name    ：appendCategory
    *  @param node
    *  @return void
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void appendCategory(PointProductCategory node){
        if (orginCatesHasUsed) {
            throw new IllegalStateException("节点已经解析,不允许再添加分类");
        }
        orginCates.add(node);
    }

    /**
    *  @Description    ：构造节点
    *  @Method_Name    ：build
    *
    *  @return com.hongkun.finance.point.support.CategoryComponent
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public synchronized List<CategoryComponent> build(){
        Assert.notEmpty(orginCates,"没有目录节点可以构造!");
        return this.buildComponent = getCatalogs();
    }



    /**
    *  @Description    ：根据传入的某一级id构造单线
    *  @Method_Name    ：constructSingleTreeFormChild
    *  @param currentNode
    *  @return com.hongkun.finance.point.support.CategoryComponent
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public synchronized CategoryComponent buildSingleTreeFormChild(CategoryComponent currentNode) {
        /**
         * step 1:判断是否有父节点
         */
        Integer parentId = currentNode.getParentId();
        if (!NumberUtils.INTEGER_ZERO.equals(parentId)) {
            PointProductCategory parent = groupedIdMap.get(parentId);
            //此时一定是Category
            Category parentNode = (Category) buildSingleNode(parent);
            //当前加入到父类
            parentNode.addNode(currentNode);
            //继续构造
           return buildSingleTreeFormChild(parentNode);
        }
        //如果已经是顶级，返回
        return currentNode;
    }


    /**
     *  @Description    ：根据某一级的id找到它所有的叶子id
     *  @Method_Name    ：findLeafsCateIds
     *  @param startId
     *  @return java.util.List<java.lang.Integer>
     *  @Creation Date  ：2018/4/18
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public  List<Integer> buildLeafsCateIds(Integer startId) {
        List<Integer> leafIds = new ArrayList<>();
        CategoryIterator categoryIterator = new CategoryIterator(build().iterator());
        Iterator<CategoryComponent> tartIterator = categoryIterator.finComponectInTree(new Category(null, startId, null,null));
        //重置迭代栈
        categoryIterator.reSetIteratorStack(tartIterator);
        //迭代其所有的叶子
        categoryIterator.iteratorAll((categoryComponent)->{
            if (categoryComponent instanceof CategoryItem) {
                leafIds.add(categoryComponent.getId());
            }
        });
        return leafIds;
    }

    /**
     *  @Description    ：根据某个id找到所有的直接子id
     *  @Method_Name    ：findChildIds
     *  @param startId
     *  @return void
     *  @Creation Date  ：2018/4/18
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public  List<Integer> bulidChildIds(Integer startId) {
        List<Integer> childIds = new ArrayList<>();
        CategoryIterator categoryIterator = new CategoryIterator(build().iterator());
        Iterator<CategoryComponent> tartIterator = categoryIterator.finComponectInTree(new Category(null, startId, null,null));
        //重置迭代栈
        categoryIterator.reSetIteratorStack(tartIterator);
        //迭代其所有的叶子
        categoryIterator.iteratorAll((categoryComponent)-> childIds.add(categoryComponent.getId()));
        childIds.add(startId);  
        return childIds;
    }

    /**
     *  @Description    ：构造单个节点,如果是叶子,返回CategroyItem,否则返回Categroy
     *  @Method_Name    ：buildSingleNode
     * @param category
     * @return com.hongkun.finance.point.model.CategoryNode
     *  @Creation Date  ：2018/4/17
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public   CategoryComponent buildSingleNode(PointProductCategory category) {
        if (groupedParentCate.containsKey(category.getId())) {
            return new Category(category.getParentId(),category.getId(),category.getName(),category.getSort());
        }
        //如果是叶子节点
        return new CategoryItem(category.getParentId(),category.getId(), category.getName(),category.getSort());
    }


    /**
     *  @Description    ：抽取函数接口，假如对应节点有子或者没有的逻辑
     *  @Method_Name    ：doBizOnChild
     *  @param currentNodeId
     *  @param bizConsumerHasChild
     *  @param bizConsumerNoChild
     *  @return void
     *  @Creation Date  ：2018/4/18
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public void doBizOnChild(Integer currentNodeId,
                             Consumer<PointProductCategory> bizConsumerHasChild,
                             Consumer<Integer> bizConsumerNoChild) {
        //尝试获取子菜单
        List<PointProductCategory> childs = groupedParentCate.get(currentNodeId);
        //判断是否有子菜单
        if (!BaseUtil.collectionIsEmpty(childs) && bizConsumerHasChild != null) {
            childs.sort(Comparator.comparing(PointProductCategory::getSort).thenComparing(PointProductCategory::getCreateTime));
            childs.stream().forEach((e) -> bizConsumerHasChild.accept(e));
        } else if (BaseUtil.collectionIsEmpty(childs) && bizConsumerNoChild != null) {
            bizConsumerNoChild.accept(currentNodeId);
        }

    }

    /**
     *  @Description    ：标记所有的节点已经加入构造器
     *  @Method_Name    ：lockOrginCates
     *
     *  @return void
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    public void lockOrginCates(){
        orginCatesHasUsed = true;
        if (!BaseUtil.collectionIsEmpty(orginCates)) {
            groupedParentCate= orginCates.stream().collect(Collectors.groupingBy(k ->k.getParentId()));
            groupedIdMap = orginCates.stream().collect(Collectors.toMap(key -> key.getId(), value -> value));
        }
    }


    /**
     *  @Description    ：构造目录树
     *  @Method_Name    ：getCatalogs
     *  @return java.util.List
     *  @Creation Date  ：2018/4/18
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private   List<CategoryComponent> getCatalogs() {
        if (!groupedParentCate.isEmpty()) {
            //获取所有的最顶层的分类
            List<CategoryComponent> categoryList = groupedParentCate.get(NumberUtils.INTEGER_ZERO)
                                            .stream()
                                            .map((parent) -> constructCatalogs(parent))/*构造目录*/
                                            .collect(Collectors.toList());
            categoryList.sort(Comparator.comparing(CategoryComponent::getSort).thenComparing(CategoryComponent::getId));
            return categoryList;
        }
        return Collections.emptyList();
    }



    /**
     *  @Description    ： 利用递归方式构造子菜单
     *  @Method_Name    ：constructCatalogs
     *  @param currentNode
     *  @return com.hongkun.finance.point.support.CategoryComponent
     *  @Creation Date  ：2018/4/18
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    private  CategoryComponent constructCatalogs(PointProductCategory currentNode) {
        //构建当前节点,一级或者叶子
        CategoryComponent categoryOrItem = buildSingleNode(currentNode);
        //赋值
        categoryOrItem.setId(currentNode.getId());
        categoryOrItem.setParentId(currentNode.getParentId());
        categoryOrItem.setTitle(currentNode.getName());
        categoryOrItem.setSort(currentNode.getSort());
        //构造子节点
        doBizOnChild(
                categoryOrItem.getId(),
                //此时一定是category
                child -> categoryOrItem.addNode(constructCatalogs(child)),
                null);
        return categoryOrItem;
    }
}
