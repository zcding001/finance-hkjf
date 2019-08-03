package com.hongkun.finance.point.service;

import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointProductService.java
 * @Class Name    : PointProductService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointProductService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointProduct 持久化的数据对象
	 * @return				: void
	 */
	Integer insertPointProduct(PointProduct pointProduct);

	
	/**
	 * @Described			: 更新数据
	 * @param pointProduct 要更新的数据
	 * @return				: void
	 */
	Integer updatePointProduct(PointProduct pointProduct);

	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointProduct
	 */
	PointProduct findPointProductById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProduct 检索条件
	 * @return	List<PointProduct>
	 */
	List<PointProduct> findPointProductList(PointProduct pointProduct);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProduct 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointProduct>
	 */
	List<PointProduct> findPointProductList(PointProduct pointProduct, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointProduct 检索条件
	 * @param pager	分页数据
	 * @return	List<PointProduct>
	 */
	Pager findPointProductList(PointProduct pointProduct, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointProduct 检索条件
	 * @return	int
	 */
	int findPointProductCount(PointProduct pointProduct);
	
	/**
	 * 自定义sql查询count
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findPointProductList(PointProduct pointProduct, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findPointProductCount(PointProduct pointProduct, String sqlName);



	/**
	 *  @Description    : 判断商品list中产品的数量
	 *  @Method_Name    : productsCountFromCategory
	 *  @param leafsCates  : 商品分类叶子ID
	 *  @return         :Pager
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	Boolean productsCountFromCategory(List leafsCates);



	/**
	 *  @Description    : 商品上架或者下架操作
	 *  @Method_Name    : productUpOrOff
	 *  @param id       : 商品ID
	 *  @param state  : 需要更改的商品状态
	 *  @return         :ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
    ResponseEntity productUpOrOff(Integer id, Integer state);


	/**
	 *  @Description    : 查询首页的限时栏目
	 *  @Method_Name    : findIndexFlashSales
	 *  @param pointMallQuery   : 查询条件
	 *  @param pager  : 分页信息
	 *  @return         :ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	Pager findIndexFlashSales(PointMallQuery pointMallQuery, Pager pager);

	/**
	 * 推荐商品，热门兑换，其他商品
	 * @return
	 */
	Map<String,List<PointProductVO>> findIndexProducts();

	/**
	 * 查找带图片的商品
	 * @param id
	 * @return
	 */
	PointProductVO findPointProductWithImgById(Integer id);

	/**
	 * 根据搜索条件查询商品图片
	 * @param pointMallQuery
	 * @param pager
	 * @return
	 */
    Pager filterProduct(PointMallQuery pointMallQuery, Pager pager);


	/**
	 * 审核积分商品
	 * @param pointProductVO
	 * @return
	 */
	ResponseEntity checkPointProduct(PointProductVO pointProductVO);

	/**
	 * 查询积分商城APP端首页的信息
	 * @param pointMallQuery
	 * @param pager
	 * @return
	 */
	Pager findAppPointMallIndex(PointMallQuery pointMallQuery, Pager pager);
    /**
     *  @Description    : 查询APP端友乾人积分商品
     *  @Method_Name    : recommendProductsForApp;
     *  @return
     *  @return         : List<PointProductVO>;
     *  @Creation Date  : 2018年9月28日 下午4:34:29;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    List<PointProductVO>  recommendProductsForApp();
}
