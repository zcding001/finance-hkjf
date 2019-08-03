package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.PointProductImg;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointProductImgService.java
 * @Class Name    : PointProductImgService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointProductImgService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointProductImg 持久化的数据对象
	 * @return				: void
	 */
	void insertPointProductImg(PointProductImg pointProductImg);

	
	/**
	 * @Described			: 更新数据
	 * @param pointProductImg 要更新的数据
	 * @return				: void
	 */
	void updatePointProductImg(PointProductImg pointProductImg);

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointProductImg
	 */
	PointProductImg findPointProductImgById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductImg 检索条件
	 * @return	List<PointProductImg>
	 */
	List<PointProductImg> findPointProductImgList(PointProductImg pointProductImg);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductImg 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointProductImg>
	 */
	List<PointProductImg> findPointProductImgList(PointProductImg pointProductImg, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProductImg 检索条件
	 * @param pager	分页数据
	 * @return	List<PointProductImg>
	 */
	Pager findPointProductImgList(PointProductImg pointProductImg, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointProductImg 检索条件
	 * @return	int
	 */
	int findPointProductImgCount(PointProductImg pointProductImg);
	

	/**
	 *  @Description    : 自定义sql查询count
	 *  @Method_Name    : findPointProductImgList
	 *  @param pointProductImg  : 积分商品图片信息
	 *  @param pager  : 分页信息
	 *  @param sqlName  : 添加一个自定义的sql的名字
	 *  @return         :Pager
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	Pager findPointProductImgList(PointProductImg pointProductImg, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findPointProductImgCount(PointProductImg pointProductImg, String sqlName);
}
