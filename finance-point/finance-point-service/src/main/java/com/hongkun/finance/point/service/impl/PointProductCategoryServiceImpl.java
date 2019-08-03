package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.dao.PointProductCategoryDao;
import com.hongkun.finance.point.dao.PointProductDao;
import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.point.util.CategoryBuilder;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointProductCategoryServiceImpl.java
 * @Class Name    : PointProductCategoryServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class PointProductCategoryServiceImpl implements PointProductCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(PointProductCategoryServiceImpl.class);

    /**
     * PointProductCategoryDAO
     */
    @Autowired
    private PointProductCategoryDao pointProductCategoryDao;

    @Autowired
    private PointProductDao pointProductDao;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public Integer insertPointProductCategory(PointProductCategory pointProductCategory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: insertPointProductCategory, 新增商品分类, 入参: pointProductCategory: {}", pointProductCategory);
        }
        try {
            return pointProductCategoryDao.save(pointProductCategory);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("新增商品分类异常, 需要保存的商品分类信息: {}\n异常信息: ", pointProductCategory, e);
            }
            throw new GeneralException("新增商品分类异常,请重试");
        }
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public Integer updatePointProductCategory(PointProductCategory pointProductCategory) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updatePointProductCategory, 更新商品分类, 入参: pointProductCategory: {}", pointProductCategory);
        }
        try {
            return this.pointProductCategoryDao.update(pointProductCategory);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新商品分类异常, 需要更新商品分类为: {}\n异常信息: ", pointProductCategory, e);
            }
            throw new GeneralException("更新商品分类异常,请重试");
        }

    }


    @Override
    public PointProductCategory findPointProductCategoryById(int id) {
        return this.pointProductCategoryDao.findByPK(Long.valueOf(id), PointProductCategory.class);
    }

    @Override
    public List<PointProductCategory> findPointProductCategoryList(PointProductCategory pointProductCategory) {
        return this.pointProductCategoryDao.findByCondition(pointProductCategory);
    }

    @Override
    public List<PointProductCategory> findPointProductCategoryList(PointProductCategory pointProductCategory, int start, int limit) {
        return this.pointProductCategoryDao.findByCondition(pointProductCategory, start, limit);
    }

    @Override
    public Pager findPointProductCategoryList(PointProductCategory pointProductCategory, Pager pager) {
        return this.pointProductCategoryDao.findByCondition(pointProductCategory, pager);
    }

    @Override
    public int findPointProductCategoryCount(PointProductCategory pointProductCategory) {
        return this.pointProductCategoryDao.getTotalCount(pointProductCategory);
    }


    @Override
    public CategoryComponent findParentsFromChildId(Integer childId) {
        List<PointProductCategory> allCategories = findPointProductCategoryList(new PointProductCategory());

       //step 1:所有的分类

        CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(allCategories));
       //step 2:递归构造父节点
        return categoryBuilder
                .buildSingleTreeFormChild(categoryBuilder.buildSingleNode(pointProductCategoryDao.findByPK(Long.valueOf(childId), PointProductCategory.class)));
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity saveCategory(PointProductCategory category) {
        Integer orginCategoryId = category.getParentId();
        if (!NumberUtils.INTEGER_ZERO.equals(orginCategoryId)) {
            //添加非一级菜单
            return addNonLeveOneCate(category, e -> insertPointProductCategory(category));
        }
        //添加一级菜单
        insertPointProductCategory(category);
        return new ResponseEntity(SUCCESS, "添加商品分类成功");
    }

   /**
   *  @Description    ：添加非一级菜单逻辑
   *  @Method_Name    ：addNonLeveOneCate
   *  @param category
   *  @param consumer
   *  @return com.yirun.framework.core.model.ResponseEntity
   *  @Creation Date  ：2018/4/24
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    private ResponseEntity addNonLeveOneCate(PointProductCategory category, Consumer<PointProductCategory> consumer) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: addNonLeveOneCate, 添加非一级菜单, 入参: category: {}", category);
        }
        final int[] childCount = {0};
        try {
            //统计是否有下一级菜单
            Integer orginCategoryId = category.getParentId();
            CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(findPointProductCategoryList(new PointProductCategory())));
            categoryBuilder.doBizOnChild(orginCategoryId, e -> childCount[0]++, null);
            //插入菜单
            consumer.accept(category);
            //如果是叶子节点
            if (childCount[0] == 0) {
                //如果是叶子节点，迁移商品
                pointProductDao.moveProductToNewCategory(orginCategoryId, category.getId());
            }
            return new ResponseEntity(SUCCESS, "添加商品分类成功");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("添加非一级菜单异常, 需要添加的非一级菜单为: {}\n异常信息: ", category, e);
            }
            throw new GeneralException("添加子菜单分类异常,请重试");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity deleteOnCascadeCate(Integer cateId) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: deleteOnCascadeCate, 级联删除菜单, 入参: 需要删除的菜单ID为: {}", cateId);
        }
        try {
            //判断该目录下面是否有商品，如果有商品，不允许删除
            CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(findPointProductCategoryList(new PointProductCategory())));
            List<Integer> leafIds = categoryBuilder.buildLeafsCateIds(cateId);
            //如果没有叶子节点，设置一个不存在的节点，为了方便查询
            if(leafIds.size() == 0){
                leafIds.add(0);
            }
            if (pointProductDao.productsCountFromCategory(leafIds)>0) {
                return ResponseEntity.error("分类下有商品，不允许删除");
            }
            List<Integer> unDeleteIds = categoryBuilder.bulidChildIds(cateId);
            if (!BaseUtil.collectionIsEmpty(unDeleteIds)) {
                pointProductCategoryDao.deleteOnCascade(unDeleteIds);
            }
            return new ResponseEntity(SUCCESS, "删除分类成功");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("级联删除菜单菜单异常, 需要删除的菜单ID为: {}\n异常信息: ", cateId, e);
            }
            throw new GeneralException("级联删除菜单菜单异常,请重试");
        }
    }

    @Override
    public List listCategories() {
        CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(findPointProductCategoryList(new PointProductCategory())));
        return categoryBuilder.build();
    }

}
