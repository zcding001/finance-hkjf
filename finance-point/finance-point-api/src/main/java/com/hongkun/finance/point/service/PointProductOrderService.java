package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointProductOrder;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointProductOrderService.java
 * @Class Name    : PointProductOrderService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointProductOrderService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointProductOrder 持久化的数据对象
	 * @return				: void
	 */
	Integer insertPointProductOrder(PointProductOrder pointProductOrder);


	
	/**
	 * @Described			: 更新数据
	 * @param pointProductOrder 要更新的数据
	 * @return				: void
	 */
	Integer updatePointProductOrder(PointProductOrder pointProductOrder);

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointProductOrder
	 */
	PointProductOrder findPointProductOrderById(int id);
	


	/**
	 * @Described			: 统计条数
	 * @param pointProductOrder 检索条件
	 * @return	int
	 */
	int findPointProductOrderCount(PointProductOrder pointProductOrder);


	/**
	 * 查询订单数据的分页
	 * @param pointProductOrder
	 * @param pager
	 * @return
	 */
    Pager listPointOrder(PointOrderQuery pointProductOrder, Pager pager);


	/**
	 * 列出用户的订单
	 * @param pointOrderQuery
	 * @param pager
	 * @return
	 */
	ResponseEntity listUserPointProductOrder(PointOrderQuery pointOrderQuery, Pager pager);
}
