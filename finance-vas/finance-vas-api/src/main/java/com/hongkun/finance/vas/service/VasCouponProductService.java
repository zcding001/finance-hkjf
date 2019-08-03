package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.VasCouponProductService.java
 * @Class Name    : VasCouponProductService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasCouponProductService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasCouponProduct 持久化的数据对象
	 * @return				: void
	 */
	int insertVasCouponProduct(VasCouponProduct vasCouponProduct);
	
	/**
	 * @Described			: 批量插入
	 * @param List<VasCouponProduct> 批量插入的数据
	 * @return				: void
	 */
	void insertVasCouponProductBatch(List<VasCouponProduct> list);
	

	
	/**
	 * @Described			: 更新数据
	 * @param vasCouponProduct 要更新的数据
	 * @return				: int
	 */
	int updateVasCouponProduct(VasCouponProduct vasCouponProduct);
	

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasCouponProduct
	 */
	VasCouponProduct findVasCouponProductById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponProduct 检索条件
	 * @return	List<VasCouponProduct>
	 */
	List<VasCouponProduct> findVasCouponProductList(VasCouponProduct vasCouponProduct);
	

	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponProduct 检索条件
	 * @param pager	分页数据
	 * @return	List<VasCouponProduct>
	 */
	Pager findVasCouponProductList(VasCouponProduct vasCouponProduct, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param vasCouponProduct 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findVasCouponProductCount(VasCouponProduct vasCouponProduct);

	/**
	 * 查询卡券数据的分页数据
	 * @param vasCouponDetailVO
	 * @param limitsUserIds
	 * @param pager
	 * @return
	 */
	Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO, List<Integer> limitsUserIds, Pager pager);

	/**
	 *  @Description    : 获取会员待遇中的卡券产品
	 *  @Method_Name    : getCouponProduct
	 *  @param vasCouponProduct
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年11月10日 下午17:12:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity getCouponProduct(VasCouponProduct vasCouponProduct);

	/**
	 *  @Description    : 更新卡券产品记录
	 *  @Method_Name    : updateCouponProduct
	 *  @param vasCouponProduct
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年10月31日 下午15:28:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity updateCouponProduct(VasCouponProduct vasCouponProduct);

	/**
	 *  @Description    : 禁用卡券产品记录
	 *  @Method_Name    : disableCouponProduct
	 *  @param id
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年11月02日 上午09:48:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity disableCouponProduct(int id);

	/**
	 * @Description      ：卡券统计 
	 * @Method_Name      ：staCouponList 
	 * @param vasCouponProduct
	 * @param pager
	 * @return Pager    
	 * @Creation Date    ：2019/1/7        
	 * @Author           ：pengwu@hongkunjinfu.com
	 */
	Pager staCouponList(VasCouponProduct vasCouponProduct, Pager pager);
}
