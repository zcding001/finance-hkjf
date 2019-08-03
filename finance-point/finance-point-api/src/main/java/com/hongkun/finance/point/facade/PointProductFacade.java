package com.hongkun.finance.point.facade;

import com.hongkun.finance.point.model.vo.PointProductVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.service.PointProductFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface PointProductFacade {


    /**
     *  @Description    : 列出所有的商品列表
     *  @Method_Name    : listPointProductList
     *  @param pointProductVO  :积分商品VO
     *  @param pager        :分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity listPointProductList(PointProductVO pointProductVO, Pager pager);



    /**
     *  @Description    : 保存积分商品
     *  @Method_Name    : savePointProduct
     *  @param pointProductVO  :积分商品VO
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity savePointProduct(PointProductVO pointProductVO);


    /**
     *  @Description    : 查询单个商品的信息信息
     *  @Method_Name    : selectPointProductInfo
     *  @param productId  :商品ID
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity selectPointProductInfo(Integer productId);

    /**
     *  @Description    : 更新商品信息
     *  @Method_Name    : updatePointProduct
     *  @param pointProductVO  :积分商品VO
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity updatePointProduct(PointProductVO pointProductVO);




}
