package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.dao.PointActivityRecordDao;
import com.hongkun.finance.point.dao.PointProductCategoryDao;
import com.hongkun.finance.point.dao.PointProductDao;
import com.hongkun.finance.point.dao.PointProductImgDao;
import com.hongkun.finance.point.model.PointActivityRecord;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.point.util.CategoryBuilder;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.hongkun.finance.point.constants.PointConstants.FIRST_IMG;
import static com.hongkun.finance.user.utils.BaseUtil.equelsIntWraperPrimit;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointProductServiceImpl.java
 * @Class Name    : PointProductServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointProductServiceImpl implements PointProductService {

	private static final Logger logger = LoggerFactory.getLogger(PointProductServiceImpl.class);
	
	/**
	 * PointProductDAO
	 */
	@Autowired
	private PointProductDao pointProductDao;
	@Autowired
	private PointProductImgDao productImgDao;

	@Autowired
    private PointProductCategoryDao pointProductCategoryDao;
	@Autowired
	private PointActivityRecordDao pointActivityRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer insertPointProduct(PointProduct pointProduct) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: insertPointProduct, 保存积分商品, 入参: 商品信息: {}", pointProduct);
		}
		try {
			return this.pointProductDao.save(pointProduct);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("保存积分商品异常, 需要保存的积分商品为: {}\n异常信息: ", pointProduct, e);
			}
			throw new GeneralException("保存积分商品异常,请重试");
		}
	}

	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer updatePointProduct(PointProduct pointProduct) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: updatePointProduct, 更新积分商品, 入参: 商品信息: {}", pointProduct);
		}
		try {
			return this.pointProductDao.update(pointProduct);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("更新积分商品异常, 需要更新积分商品为: {}\n异常信息: ", pointProduct, e);
			}
			throw new GeneralException("更新积分商品异常,请重试");
		}

	}

	@Override
	public PointProduct findPointProductById(int id) {
		return this.pointProductDao.findByPK(Long.valueOf(id), PointProduct.class);
	}

	@Override
	public List<PointProduct> findPointProductList(PointProduct pointProduct) {
		return this.pointProductDao.findByCondition(pointProduct);
	}
	
	@Override
	public List<PointProduct> findPointProductList(PointProduct pointProduct, int start, int limit) {
		return this.pointProductDao.findByCondition(pointProduct, start, limit);
	}
	
	@Override
	public Pager findPointProductList(PointProduct pointProduct, Pager pager) {
		return this.pointProductDao.findByCondition(pointProduct, pager);
	}
	
	@Override
	public int findPointProductCount(PointProduct pointProduct){
		return this.pointProductDao.getTotalCount(pointProduct);
	}
	
	@Override
	public Pager findPointProductList(PointProduct pointProduct, Pager pager, String sqlName){
		return this.pointProductDao.findByCondition(pointProduct, pager, sqlName);
	}
	
	@Override
	public Integer findPointProductCount(PointProduct pointProduct, String sqlName){
		return this.pointProductDao.getTotalCount(pointProduct, sqlName);
	}



	@Override
	public Boolean productsCountFromCategory(List leafsCates) {
		return pointProductDao.productsCountFromCategory(leafsCates)> NumberUtils.LONG_ZERO;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity productUpOrOff(Integer id, Integer state) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: productUpOrOff, 上架或者下架商品, 入参: 商品ID: {}, 状态: {}", id,state);
		}
		try {
		    //商品上架的时候，查询此商品的信息
		    PointProduct pointPro=pointProductDao.findByPK(Long.valueOf(id), PointProduct.class);
		    //判断是否为活动商品
		    if(pointPro != null && pointPro.getFlashSale() == 1 && PointConstants.ON_SALE == state){
		        PointActivityRecord pointActivityRecord =new PointActivityRecord();
		        pointActivityRecord.setProductId(id);
		        pointActivityRecord.setSortColumns("id desc");
		        List<PointActivityRecord> activityRecordList=pointActivityRecordDao.findByCondition(pointActivityRecord);
		        //如果是活动商品，并且没有促俏记录，则插入一条促俏记录
		        if(activityRecordList !=null && activityRecordList.size() ==0 ){
		            pointActivityRecord.setSales(0);
		            pointActivityRecord.setBeginTime(pointPro.getShowTimeStart());
	                pointActivityRecord.setEndTime(pointPro.getShowTimeEnd());
	                pointActivityRecord.setPoint(pointPro.getDiscountPoint());
	                pointActivityRecordDao.save(pointActivityRecord);
		        }else{
		            //如果有促俏记录，但是活动还没有开始，则判断促俏记录的开始时间，结束时间，还有积分值是否是当前商品的促俏活动，如果有一样不相等地，则更新当前促俏记录，否则
		            PointActivityRecord pointRecord = activityRecordList.get(0);
		            if(DateUtils.betDate(new Date(), pointPro.getShowTimeStart()) < 0){
		                //判断促俏记录的开始时间，结束时间，还有积分值是否是当前商品的促俏活动,如果有一个不符合，则更新当前的促俏记录
		                if(DateUtils.betDate(pointPro.getShowTimeStart(),pointRecord.getBeginTime()) !=0 || DateUtils.betDate(pointPro.getShowTimeEnd(),pointRecord.getEndTime()) !=0 || !pointPro.getDiscountPoint().equals(pointRecord.getPoint())){
		                    pointRecord.setBeginTime(pointPro.getShowTimeStart());
	                        pointRecord.setEndTime(pointPro.getShowTimeEnd());
	                        pointRecord.setPoint(pointPro.getDiscountPoint());
	                        pointRecord.setSales(0);
	                        pointActivityRecordDao.update(pointRecord);
		                }
		            }else{
		                //如果活动已经开始，判断促俏记录的开始时间，结束时间，还有积分值是否是当前商品的促俏活动,如果有一个不符合，则插入一条新的促俏记录
	                    if(DateUtils.betDate(pointPro.getShowTimeStart(),pointRecord.getBeginTime()) !=0 || DateUtils.betDate(pointPro.getShowTimeEnd(),pointRecord.getEndTime()) !=0 || !pointPro.getDiscountPoint().equals(pointRecord.getPoint())){
    		                pointActivityRecord.setSales(0);
    	                    pointActivityRecord.setBeginTime(pointPro.getShowTimeStart());
    	                    pointActivityRecord.setEndTime(pointPro.getShowTimeEnd());
    	                    pointActivityRecord.setPoint(pointPro.getDiscountPoint());
    	                    pointActivityRecordDao.save(pointActivityRecord);
	                    }
		            }
		        }
		    }
			PointProduct product = new PointProduct();
			product.setId(id);
			product.setState(state);
			pointProductDao.update(product);
			return new ResponseEntity(SUCCESS, "操作成功");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("上架或下架商品异常, 商品ID: {}, 状态: {}\n异常信息: ", id, state, e);
			}
			throw new GeneralException("上架或下架商品异常,请重试");
		}

	}



	@Override
	public Pager findIndexFlashSales(PointMallQuery pointMallQuery, Pager pager) {
		Pager indexFlashSales = pointProductDao.findIndexFlashSales(pointMallQuery, pager);
		if (!BaseUtil.resultPageHasNoData(indexFlashSales)) {
			indexFlashSales.setData(fillMajorImgToProduct((List<PointProduct>) indexFlashSales.getData()));
		}
		return indexFlashSales;
	}

	/**
	 * 填充商品首图到商品中
	 * @param indexFlashSales
	 * @return
	 */
    private List<PointProductVO> fillMajorImgToProduct(List<PointProduct> indexFlashSales) {
        if (!BaseUtil.collectionIsEmpty(indexFlashSales)) {
            List<Integer> productIds = indexFlashSales.stream().map(PointProduct::getId).collect(Collectors.toList());
            List<PointProductImg> imgs=productImgDao.findProductHeadImgsByProductIds(productIds);
            Map<Integer, String> productImgs=BaseUtil.collectionIsEmpty(imgs)? Collections.emptyMap():
                    imgs.stream().filter(e->e!=null).collect(Collectors.toMap(key -> key.getProductId(), value -> value.getSmallImgUrl()));
            //赋值到图片返回
            return indexFlashSales.stream().map(e -> {
                PointProductVO tempVo = BeanPropertiesUtil.mergeAndReturn(new PointProductVO(), e);
                tempVo.setFirstImg(productImgs.get(e.getId()));
                return tempVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

	/**
	*  @Description    ：填充商品所有的图片到商品中
	*  @Method_Name    ：fillImgsToProduct
	*  @param product
	*  @return com.hongkun.finance.point.model.vo.PointProductVO
	*  @Creation Date  ：2018/4/24
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
	private PointProductVO fillImgsToProduct(PointProduct product) {
		if (product!=null) {
			PointProductVO pointProductVO = BeanPropertiesUtil.mergeAndReturn(new PointProductVO(), product);
			PointProductImg img = new PointProductImg();
			img.setProductId(product.getId());
			img.setSortColumns("sort desc");
			List<PointProductImg> imgs = productImgDao.findByCondition(img);
			PointProductImg firstImg = imgs.stream().filter(e -> equelsIntWraperPrimit(e.getHeadImg(),FIRST_IMG)).findFirst().get();
			pointProductVO.setProductImgList(imgs);
			pointProductVO.setHeadImg(firstImg);
			return pointProductVO;
		}
		return null;
	}

    @Override
	public Map<String, List<PointProductVO>> findIndexProducts() {
		return new HashMap<String, List<PointProductVO>>(3) {
			{
				//热门商品
				put("bestSellProducts", fillMajorImgToProduct(pointProductDao.bestSellProducts()));
				//推荐商品
				put("recommendIndexProducts", fillMajorImgToProduct(pointProductDao.recommendIndexProducts()));
				//其他商品
				put("otherProducts", fillMajorImgToProduct(pointProductDao.otherProducts()));
			}
		};

	}

	@Override
	public PointProductVO findPointProductWithImgById(Integer id) {
		return fillImgsToProduct(findPointProductById(id));
	}

	@Override
	public Pager filterProduct(PointMallQuery pointMallQuery, Pager pager) {
		//查询除了限时商品的所有的商品
		return fillPagerProductImage(pointMallQuery,()-> pointProductDao.filterProduct(pointMallQuery, pager));
	}


	@Override
	public ResponseEntity checkPointProduct(PointProductVO pointProductVO) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: checkPointProduct, 审核积分商品, 入参: 商品信息: {}", pointProductVO);
		}
		try {
			Integer changeState = pointProductVO.getState();
			//step 1:校验原来的商品的状态
			if (pointProductVO.getCheckIds().stream().map((e) -> {
				PointProduct orginProduct = findPointProductById(e);
				return PointConstants.UN_CHECK != orginProduct.getState();
			}).findAny().get()) {
				return ResponseEntity.error("选择商品中有非待审核商品");
			}
			//step 2:进行状态变更
			pointProductVO.getCheckIds().stream().forEach((e) -> {
				PointProduct product = new PointProduct();
				product.setId(e);
				product.setState(changeState);
				if (changeState.equals(PointConstants.CHECK_REJECT)) {
					product.setRefuseCause(pointProductVO.getRefuseCause());
				}
				updatePointProduct(product);
			});
			if (logger.isInfoEnabled()) {
				logger.info("审核商品成功, 商品信息为: {}", pointProductVO);
			}
			return ResponseEntity.SUCCESS;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("审核积分商品异常, 商品信息: {}\n异常信息: ", pointProductVO, e);
			}
			throw new GeneralException("审核积分商品异常,请重试");
		}


	}

	@Override
	public Pager findAppPointMallIndex(PointMallQuery pointMallQuery, Pager pager) {
		if (NumberUtils.INTEGER_ZERO.equals(pointMallQuery.getCateId())) {
			//0代表查询所有的产品类型
			pointMallQuery.setCateId(null);
		}
		return fillPagerProductImage(pointMallQuery,()-> pointProductDao.findAppPointMallIndex(pointMallQuery, pager));
	}

	/**
	*  @Description    ： getPageResult提供product，然后填充商品图片
	*  @Method_Name    ：fillPagerProductImage
	*  @param pointMallQuery
	*  @param getPageResult
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date  ：2018/4/24
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
	private Pager fillPagerProductImage(PointMallQuery  pointMallQuery, Supplier<Pager> getPageResult){
		//查出所选的类目的所有的叶子
		Integer cateId = pointMallQuery.getCateId();
		if (cateId!=null) {
			CategoryBuilder categoryBuilder = new CategoryBuilder(new HashSet<>(pointProductCategoryDao.findByCondition(new PointProductCategory())));
			List<Integer> catesIds = categoryBuilder.buildLeafsCateIds(cateId);
			if(!CollectionUtils.isEmpty(catesIds)){
				pointMallQuery.setCateId(null);
				pointMallQuery.setCatesIds(catesIds);
			}else{
			    pointMallQuery.setCatesIds(Arrays.asList(pointMallQuery.getCateId()));
			}
		}
	    PointMallQuery tempVo = BeanPropertiesUtil.mergeAndReturn(new PointMallQuery(),pointMallQuery);
		Pager result = getPageResult.get();
		//填充商品的图片
		if (!BaseUtil.resultPageHasNoData(result)) {
		    List<PointProduct> productList = (List<PointProduct>) result.getData();
		    //按照商品是否限购分组，并按照积分价格过滤限购商品
		    Map<Integer, List<PointProduct>> productImgs=BaseUtil.collectionIsEmpty(productList)? Collections.emptyMap():
		    productList.stream().filter(e->e!=null).collect(Collectors.groupingBy(PointProduct :: getFlashSale));
		    List<PointProduct> saleProList = productImgs.get(PointConstants.POINT_FLASH_SALE_ONE)!=null?productImgs.get(PointConstants.POINT_FLASH_SALE_ONE):Collections.emptyList();
		    List<PointProduct> proList =  productImgs.get(PointConstants.POINT_FLASH_SALE_ZERO)!=null? productImgs.get(PointConstants.POINT_FLASH_SALE_ZERO):Collections.emptyList();
            if(tempVo.getPointStart()!=null && tempVo.getPointEnd()!=null){
               saleProList =saleProList.stream().filter(pro -> (pro.getDiscountPoint()>=tempVo.getPointStart() && pro.getDiscountPoint()<=tempVo.getPointEnd() && DateUtils.betDate(new Date(), pro.getShowTimeStart())>=0 && DateUtils.betDate(new Date(), pro.getShowTimeEnd())<=0)).collect(Collectors.toList());
               proList = proList.stream().filter(pro -> (pro.getPoint()>=tempVo.getPointStart() && pro.getPoint()<=tempVo.getPointEnd())).collect(Collectors.toList());
            }else{
                if(tempVo.getPointStart()!=null && tempVo.getPointEnd()==null){
                    saleProList = saleProList.stream().filter(pro -> pro.getDiscountPoint()>=tempVo.getPointStart() && DateUtils.betDate(new Date(), pro.getShowTimeStart())>=0 && DateUtils.betDate(new Date(), pro.getShowTimeEnd())<=0).collect(Collectors.toList());
                    proList = productImgs.get(PointConstants.POINT_FLASH_SALE_ZERO).stream().filter(pro -> pro.getPoint()>=tempVo.getPointStart()).collect(Collectors.toList());
                }else if(tempVo.getPointStart()==null && tempVo.getPointEnd()!=null){
                    saleProList = saleProList.stream().filter(pro -> pro.getDiscountPoint()<=tempVo.getPointEnd() && DateUtils.betDate(new Date(), pro.getShowTimeStart())>=0 && DateUtils.betDate(new Date(), pro.getShowTimeEnd())<=0).collect(Collectors.toList());
                    proList = proList.stream().filter(pro -> pro.getPoint()<=tempVo.getPointEnd()).collect(Collectors.toList());
                }else{
                    saleProList = saleProList.stream().filter(pro -> DateUtils.betDate(new Date(), pro.getShowTimeStart())>=0 && DateUtils.betDate(new Date(), pro.getShowTimeEnd())<=0).collect(Collectors.toList());
                }
            }
		    saleProList.addAll(proList);
		    result.setData(fillMajorImgToProduct(saleProList));
		}
		return result;
	}


    @Override
    public List<PointProductVO> recommendProductsForApp() {
        return fillMajorImgToProduct(pointProductDao.recommendIndexProductsForApp());
    }
}
