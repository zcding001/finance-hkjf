package com.hongkun.finance.invest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidProduct;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.service.BidProductService.java
 * @Class Name    : BidProductService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidProductService {


	/**
	 * @Described			: 更新数据
	 * @param bidProduct 要更新的数据
	 * @return				: void
	 */
	int updateBidProduct(BidProduct bidProduct);



	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidProduct
	 */
	BidProduct findBidProductById(int id);

	/**
	 * @Described			: 条件检索数据
	 * @param bidProduct 检索条件
	 * @return	List<BidProduct>
	 */
	List<BidProduct> findBidProductList(BidProduct bidProduct);


	/**
	 * @Described			: 统计条数
	 * @param bidProduct 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidProductCount(BidProduct bidProduct);


	/**
	 * 返回带条件的分页数据
	 * @param bidProduct
	 * @param pager
	 * @return
	 */
	Pager findBidProductWithCondition(BidProduct bidProduct, Pager pager);


	/**
	 *  @Description    : 保存标的产品
	 *  @Method_Name    : saveInfoAndDetail
	 *  @param bidProduct:          标的产品信息
	 *  @return                  ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
    ResponseEntity saveBidProduct(BidProduct bidProduct);


	/**
	 *  @Description    : 更新标的产品信息
	 *  @Method_Name    : saveInfoAndDetail
	 *  @param bidProduct :         标的产品信息
	 *  @return                  ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity updateBidProductInfo(BidProduct bidProduct);


	/**
	 *  @Description    : 上架或下架一个标的产品
	 *  @Method_Name    : saveInfoAndDetail
	 *  @param productId :  标的产品ID
	 *  @param productStateOn:  上下或者下架的状态
	 *  @return           ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	ResponseEntity turnOnOrDownProduct(Integer productId, Integer productStateOn);

	/**
	*  @Description    ：产品id排序，首先根据产品是否一次本息排序，然后通过产品创建时间排序
	*  @Method_Name    ：orderedProductIds
	*  @param orderedProductId
	*  @return java.util.List<java.lang.Integer>
	*  @Creation Date  ：2018/7/4
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
	List<Integer> orderedProductIds(List<Integer> orderedProductId);

	/**
	 *  @Description    ：产品id排序，首先根据产品是否一次本息排序，然后通过产品创建时间排序
	 *  @Method_Name    ：orderedProductIds
	 *  @param orderedProductId
	 *  @return java.util.List<java.lang.Integer>
	 *  @Creation Date  ：2018/7/4
	 *  @Author         ：zhongpingtang@hongkun.com.cn
	 */
	List<Integer> orderedByPayemntAndCreateTime(List<Integer> orderedProductId);
}
