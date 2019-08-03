package com.hongkun.finance.user.service.impl;

import com.hongkun.finance.user.utils.BaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.apache.commons.lang.math.NumberUtils.INTEGER_ONE;

/**
 * @Description : 处理中间表关系接口的模板方法
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.RelFunction
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class RelFunction {

   /**
   *  @Description    ：改变实体中间关系的模板方法
   *  @Method_Name    ：reBindRel
   *  @param orginRels
   *  @param newRels
   *  @param delteCons
   *  @param saveCons
   *  @return java.lang.Integer
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
       public static   Integer reBindRel(List<Integer> orginRels, Set<Integer> newRels, Consumer<List<Integer>> delteCons, Consumer<Integer> saveCons){
        List<Integer> menusShouldDelete = new ArrayList<>();
        if (!BaseUtil.collectionIsEmpty(orginRels)) {
            orginRels.stream().forEach((oid)->{boolean ignoreState=newRels.contains(oid) ? newRels.remove(oid) : menusShouldDelete.add(oid);});
        }

        //删除不在此次分析的关系
        if (!BaseUtil.collectionIsEmpty(menusShouldDelete)) {
            delteCons.accept(menusShouldDelete);
        }
        //插入新的关系
        if (!BaseUtil.collectionIsEmpty(newRels)) {
            newRels.stream().forEach(menuId ->  saveCons.accept(menuId));
        }
        return INTEGER_ONE;
    }
}
