package com.hongkun.finance.point.facade.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointProductFacade;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.hongkun.finance.point.service.PointProductImgService;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.point.support.CategoryIterator;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理积分商品模块的facade
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointProductFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class PointProductFacadeImpl implements PointProductFacade {
    private static final Logger logger = LoggerFactory.getLogger(PointProductFacadeImpl.class);
    @Autowired
    private PointProductService productService;

    @Autowired
    private PointProductCategoryService categoryService;
    @Autowired
    private PointCommonService pointCommonService;

    @Autowired
    private PointProductImgService pointProductImgService;

    @Override
    public ResponseEntity listPointProductList(PointProductVO pointProductVO, Pager pager) {
        //step 1:查询商品信息
        PointProduct queryProduct = new PointProduct();
        BeanPropertiesUtil.splitProperties(pointProductVO, queryProduct);
        queryProduct.setSortColumns("create_time DESC");
        Pager productPager = productService.findPointProductList(queryProduct, pager);

        return completeProduct(productPager);
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity savePointProduct(PointProductVO pointProductVO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: savePointProduct, 保存积分商品, 商品信息: {}", pointProductVO);
        }
        try {
           //step 1:分割属性
            PointProduct pointProduct = new PointProduct();
            BeanPropertiesUtil.splitProperties(pointProductVO, pointProduct);
           // step 2:设置其他属性
            pointProduct.setState(PointConstants.UN_CHECK);
           // step 3:设置积分价值
            pointProduct.setWorth(pointCommonService.pointToMoney(pointProductVO.getPoint()));
            //保存积分商品
            productService.insertPointProduct(pointProduct);
            // step 4:保存积分商品图片
            final int[] currentSort = {0};
            List<String> imgs = pointProductVO.getImgs();
            if (!BaseUtil.collectionIsEmpty(imgs)) {
                imgs.stream().forEach((img) -> {
                    String[] bigAndSmall = StringUtils.split(img, '-');
                    PointProductImg productImg = new PointProductImg();
                    productImg.setProductId(pointProduct.getId());
                    productImg.setBigImgUrl(bigAndSmall[0]);
                    productImg.setSmallImgUrl(bigAndSmall[1]);
                    if (StringUtils.isEquals(bigAndSmall[1], pointProductVO.getFirstImg())) {
                        //说明是首图
                        productImg.setHeadImg(1);
                    }
                    //设置sort
                    productImg.setSort(currentSort[0]++);
                    pointProductImgService.insertPointProductImg(productImg);
                    if (logger.isInfoEnabled()) {
                        logger.info("添加积分商品图片, 图片信息: {}", productImg);
                    }
                });

            }
            if (logger.isInfoEnabled()) {
                logger.info("添加积分商品, 商品详细信息: {}", pointProduct);
            }
            return new ResponseEntity(SUCCESS, "保存成功");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存积分商品失败, 商品信息: {}\n异常信息: ", pointProductVO, e);
            }
            throw new GeneralException("保存积分商品失败,请重试");
        }
    }

    @Override
    public ResponseEntity selectPointProductInfo(Integer productId) {
        //step 1:查询商品对应信息
        PointProduct product = this.productService.findPointProductById(productId);
        if (product == null) {
            return new ResponseEntity(ERROR, "没有查询到对应的商品");
        }
        //step 2:补全商品信息
        PointProductVO productVO = new PointProductVO();
        //拆分属性
        BeanPropertiesUtil.splitProperties(product, productVO);

        //商品图片信息
        PointProductImg productImg = new PointProductImg();
        productImg.setProductId(productId);
        List<PointProductImg> pointProductImgList = this.pointProductImgService.findPointProductImgList(productImg);
        List<String> imgs = null;
        if (!BaseUtil.collectionIsEmpty(pointProductImgList)) {
            imgs = pointProductImgList.stream().map((img) -> {
                String smallImgUrl = img.getSmallImgUrl();
                if (NumberUtils.INTEGER_ONE.equals(img.getHeadImg())) {
                    //设置首图
                    productVO.setFirstImg(smallImgUrl);
                }
                return smallImgUrl;
            }).collect(Collectors.toList());
        }
        //处理目录
        CategoryComponent singleNod = categoryService.findParentsFromChildId(product.getProductCategoryId());
        productVO.setProductCategoryName(constructCateStr(singleNod, new StringBuffer()));
        productVO.setImgs(imgs);
        return new ResponseEntity(SUCCESS, productVO);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity updatePointProduct(PointProductVO pointProductVO) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: updatePointProduct, 更新积分商品信息, 商品信息更新为: {}", pointProductVO);
        }
        try {
           //step 1:处理产品相关
            PointProduct unCleanProduct = new PointProduct();
            BeanPropertiesUtil.splitProperties(pointProductVO, unCleanProduct);
            //处理积分
            unCleanProduct.setWorth(pointCommonService.pointToMoney(pointProductVO.getPoint()));
            PointProduct pointProduct= productService.findPointProductById(unCleanProduct.getId());
            if(unCleanProduct.getNumber().equals(pointProduct.getNumber())){
                unCleanProduct.setNumber(null);
            }else{
                unCleanProduct.setNumber(pointProductVO.getNumber() -pointProduct.getNumber()); 
            }
            //更新商品
            productService.updatePointProduct(unCleanProduct);
            //step 2:处理图片相关，求出现存的图片和原有图片的交集
            PointProductImg productImg = new PointProductImg();
            productImg.setProductId(pointProductVO.getId());
            //新的未处理的图片
            List<String> unProcessImgs = pointProductVO.getImgs();

            List<PointProductImg> orginImgsObj = pointProductImgService.findPointProductImgList(productImg);
            Map<String, PointProductImg> orginImgs = orginImgsObj.stream().collect(Collectors.toMap((e) -> e.getSmallImgUrl(), (e) -> e));
            if (!BaseUtil.collectionIsEmpty(orginImgs.keySet())) {
                orginImgs.keySet().stream().filter((k) -> !unProcessImgs.contains(k)).forEach((e) -> {
                    PointProductImg unDeleted = orginImgs.get(e);
                    //逻辑删除现在没有的图片
                    unDeleted.setState(NumberUtils.INTEGER_ZERO);
                    //取消首图
                    pointProductImgService.updatePointProductImg(unDeleted);
                });
                //新插入新上传的图片
                unProcessImgs.stream().filter((e) -> !orginImgs.keySet().contains(e)).forEach((k) -> {
                    String[] bigAndSmall = StringUtils.split(k, '-');
                    PointProductImg unSaveImg = new PointProductImg();
                    unSaveImg.setProductId(pointProductVO.getId());
                    unSaveImg.setBigImgUrl(bigAndSmall[0]);
                    unSaveImg.setSmallImgUrl(bigAndSmall[1]);
                    //暂时性，因为数据库中没有固定项
                    unSaveImg.setSort(0);
                    pointProductImgService.insertPointProductImg(unSaveImg);
                });
                //处理图片的排序和首图
                Integer[] sort = {0};
                List<PointProductImg> newImgs = pointProductImgService.findPointProductImgList(productImg);
                newImgs.stream().forEach((e) -> {
                    if (StringUtils.isEquals(e.getSmallImgUrl(), pointProductVO.getFirstImg())) {
                        e.setHeadImg(NumberUtils.INTEGER_ONE);
                    } else {
                        e.setHeadImg(NumberUtils.INTEGER_ZERO);
                    }
                    e.setSort(sort[0]++);
                    pointProductImgService.updatePointProductImg(e);
                });

            }
            if (logger.isInfoEnabled()) {
                logger.info("更新商品，更新后商品详细信息:{}", pointProductVO);
            }
            return new ResponseEntity(SUCCESS, "更新商品成功");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("更新商品失败, 商品信息: {}\n异常信息: ", pointProductVO, e);
            }
            throw new GeneralException("保存积分商品失败,请重试");
        }
    }


  /**
  *  @Description    ：构造目录字符串
  *  @Method_Name    ：constructCateStr
  *  @param singleNod
  *  @param buffer
  *  @return java.lang.String
  *  @Creation Date  ：2018/4/24
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    private String constructCateStr(CategoryComponent singleNod, StringBuffer buffer) {
        Iterator<CategoryComponent> iterator = singleNod.createIterator();
        if (iterator instanceof CategoryIterator ) {
            buffer.append(singleNod.getTitle() + "->");
            ((CategoryIterator) iterator).iteratorAll((node)-> buffer.append(node.getTitle() + "->"));
            return buffer.toString().substring(0, buffer.lastIndexOf("->"));
        }
        return buffer.append(singleNod.getTitle()).toString();
    }

    /**
    *  @Description    ： 补全商品信息
    *  @Method_Name    ：completeProduct
    *  @param productPager
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private ResponseEntity completeProduct(Pager productPager) {
        List<PointProductVO> repalceVOs = new ArrayList<>(10);
        if (!BaseUtil.resultPageHasNoData(productPager)) {
            repalceVOs.addAll(
                    productPager.getData().stream().map((product) -> {
                        PointProduct pointProduct = PointProduct.class.cast(product);
                        CategoryComponent singleNod = categoryService.findParentsFromChildId(pointProduct.getProductCategoryId());
                        //拆分属性
                        PointProductVO transferVO = new PointProductVO();
                        BeanPropertiesUtil.splitProperties(pointProduct, transferVO);
                        //构建目录名称
                        transferVO.setProductCategoryName(constructCateStr(singleNod, new StringBuffer()));
                        return transferVO;
                    }).collect(Collectors.toList()));
        }
        productPager.setData(repalceVOs);
        return new ResponseEntity(SUCCESS, productPager);
    }
}
