package com.hongkun.finance.point.support;

import com.hongkun.finance.point.model.Category;

import java.util.Iterator;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @Description : 积分商城分类节点的迭代器,角色：迭代器
 * @Project : finance
 * @Program Name : com.hongkun.finance.point.support.CategoryIterator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class CategoryIterator implements Iterator<CategoryComponent> {

    /**
     * 记录当前有哪些迭代器在参与迭代
     */
    private Stack<Iterator<CategoryComponent>> iteratorStack = new Stack<>();

    public CategoryIterator(Iterator<CategoryComponent> iterator) {
        iteratorStack.push(iterator);
    }



    @Override
    public CategoryComponent next() {
        if (hasNext()) {
            CategoryComponent next = iteratorStack.peek().next();
            //如果是一个具有子目录的节点,加入其迭代器
            if (next instanceof Category) {
                iteratorStack.push(next.createIterator());
            }
            return next;
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        if (iteratorStack.isEmpty()) {
            return false;
        }
        //取出当前的迭代器
        Iterator<CategoryComponent> topIterator = iteratorStack.peek();
        if (!topIterator.hasNext()) {
            //如果当前迭代器已经全部迭代完毕,弹出当前迭代器
            iteratorStack.pop();
            //继续递判断当前栈中有没有其他的迭代器了
            return hasNext();
        }
        //有下一个返回true
        return true;
    }

    /**
    *  @Description    ：重置迭代器
    *  @Method_Name    ：setIteratorStack
    *  @param iteratorStack
    *  @return void
    *  @Creation Date  ：2018/4/18
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void reSetIteratorStack(Iterator<CategoryComponent> iteratorStack) {
        this.iteratorStack.clear();
        this.iteratorStack.push(iteratorStack);
    }
    /**
    *  @Description    ：迭代全对象
    *  @Method_Name    ：iteratorAll
    *  @param categoryComponentConsumer
    *  @return void
    *  @Creation Date  ：2018/4/18
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public void iteratorAll(Consumer<CategoryComponent> categoryComponentConsumer){
        while (hasNext()) {
            CategoryComponent next = next();
            categoryComponentConsumer.accept(next);
        }
    }

    /**
    *  @Description    ：在整个树中找到指定节点
    *  @Method_Name    ：finComponectInTree
    *  @param categoryComponent
    *  @return com.hongkun.finance.point.support.CategoryComponent
    *  @Creation Date  ：2018/4/18
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public Iterator<CategoryComponent> finComponectInTree(CategoryComponent categoryComponent) {
        while (hasNext()) {
            CategoryComponent next = next();
            if (categoryComponent.equals(next)) {
                return next.createIterator();
            }
        }
        //没找到返回空的迭代器
        return new NullIterator();
    }



}
/**
 * @Description : 构造一个空的迭代器
 * @Project : finance
 * @Program Name : com.hongkun.finance.point.support.CategoryComponent
 * @Author : zhongpingtang@hongkun.com.cn
 */
class NullIterator implements Iterator<CategoryComponent> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CategoryComponent next() {
        return null;
    }
}