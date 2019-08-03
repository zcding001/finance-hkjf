package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.support.CategoryComponent;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointProductCategoryService.java
 * @Class Name    : PointProductCategoryService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointProductCategoryService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointProductCategory 持久化的数据对象
	 * @return				: void
	 */
	Integer insertPointProductCategory(PointProductCategory pointProductCategory);


	/**
	 * @Described			: 更新数据
	 * @param pointProductCategory 要更新的数据
	 * @return				: void
	 */
	Integer updatePointProductCategory(PointProductCategory pointProductCategory);
	

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointProductCategory
	 */
	PointProductCategory findPointProductCategoryById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductCategory 检索条件
	 * @return	List<PointProductCategory>
	 */
	List<PointProductCategory> findPointProductCategoryList(PointProductCategory pointProductCategory);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductCategory 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointProductCategory>
	 */
	List<PointProductCategory> findPointProductCategoryList(PointProductCategory pointProductCategory, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductCategory 检索条件
	 * @param pager	分页数据
	 * @return	List<PointProductCategory>
	 */
	Pager findPointProductCategoryList(PointProductCategory pointProductCategory, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointProductCategory 检索条件
	 * @return	int
	 */
	int findPointProductCategoryCount(PointProductCategory pointProductCategory);


	/**
	 *  @Description    : 从子类开始构造single树
	 *  @Method_Name    : findParentsFromChildId
	 * @param childId   :子类节点的ID
	 *  @return         :CategoryComponent
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	CategoryComponent findParentsFromChildId(Integer childId);

	/**
	 *  @Description    : 保存目录
	 *  @Method_Name    : saveCategory
	 *  @param category  :目录信息
	 *  @return         :ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity saveCategory(PointProductCategory category);


	/**
	 *  @Description    : 级联删除菜单
	 *  @Method_Name    : deleteOnCascadeCate
	 *  @param id  : 需要被删除的目录ID
	 *  @return         :ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity deleteOnCascadeCate(Integer id);

	/**
	 * 列出所有的目录
	 * @return
	 */
	List listCategories();
}
