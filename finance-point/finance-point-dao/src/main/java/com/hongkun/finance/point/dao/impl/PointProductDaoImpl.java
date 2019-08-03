package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.dao.PointProductDao;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashMap;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointProductDaoImpl.java
 * @Class Name    : PointProductDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointProductDaoImpl extends MyBatisBaseDaoImpl<PointProduct, Long> implements PointProductDao {
    /**
     * 级联删除菜单
     */

    public String MOVE_PRODUCT_TO_NEWCATEGORY = ".moveProductToNewCategory";
    public String PRODUCTS_COUNT_FROM_CATEGORY = ".productsCountFromCategory";
    public String FIND_INDEX_FLASHSALES = ".findIndexFlashSales";
    public String BEST_SELL_PRODUCTS = ".bestSellProducts";
    public String RECOMMEND_INDEX_PRODUCTS = ".recommendIndexProducts";
    public String OTHER_PRODUCTS = ".otherProducts";
    public String FILTER_PRODUCT = ".filterProduct";
    public String FIND_APP_POINT_MALLINDEX = ".findAppPointMallIndex";


    @Override
    public void moveProductToNewCategory(Integer orginCategoryId, Integer currentCategoryId) {
         getCurSqlSessionTemplate().update(PointProduct.class.getName() + MOVE_PRODUCT_TO_NEWCATEGORY, new HashMap() {
            {
                put("orginCategoryId", orginCategoryId);
                put("currentCategoryId", currentCategoryId);
            }
        });
    }

    @Override
    public Long productsCountFromCategory(List leafsCates) {
        return getCurSqlSessionTemplate().selectOne(PointProduct.class.getName() + PRODUCTS_COUNT_FROM_CATEGORY, leafsCates);

    }

    @Override
    public Pager findIndexFlashSales(PointMallQuery pointMallQuery, Pager pager) {
        return findByCondition(pointMallQuery, pager, PointProduct.class, FIND_INDEX_FLASHSALES);

    }

    @Override
    public List<PointProduct> bestSellProducts() {
        return getCurSqlSessionTemplate().selectList(PointProduct.class.getName() + BEST_SELL_PRODUCTS);
    }

    @Override
    public List<PointProduct> recommendIndexProducts() {
        return getCurSqlSessionTemplate().selectList(PointProduct.class.getName() + RECOMMEND_INDEX_PRODUCTS);
    }

    @Override
    public List<PointProduct> otherProducts() {
        return getCurSqlSessionTemplate().selectList(PointProduct.class.getName() + OTHER_PRODUCTS);
    }

    @Override
    public Pager filterProduct(PointMallQuery pointMallQuery, Pager pager) {
        pointMallQuery.setPointStart(null);
        pointMallQuery.setPointEnd(null);
        return findByCondition(pointMallQuery, pager, PointProduct.class, FILTER_PRODUCT);
    }

    @Override
    public Pager findAppPointMallIndex(PointMallQuery pointMallQuery, Pager pager) {
        return findByCondition(pointMallQuery, pager, PointProduct.class, FIND_APP_POINT_MALLINDEX);
    }

    @Override
    public List<PointProduct> recommendIndexProductsForApp() {
        return getCurSqlSessionTemplate().selectList(PointProduct.class.getName() + ".recommendIndexProductsForApp");
    }
}
