package com.hongkun.finance.api.controller.pointmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.CategoryNode;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HtmlUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import static com.hongkun.finance.point.constants.PointConstants.CATES;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;

/**
 * 积分商城相关接口
 *
 * @author :zhongpingtang
 */
@Controller
@RequestMapping("/pointMallController")
public class PointMallController {

    @Reference
    private PointProductCategoryService categoryService;

    @Reference
    private PointProductService productService;

    @Reference
    private PointAccountService pointAccountService;

    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;
    //返回商品包含的字段
    private static final String[] commonProductIncludeProperties = {"id", "name", "point", "number","firstImg", "prductInfo", "flashSale", "sendWay", "salesCount", "discountPoint", "showTimeEnd"};

    /**
     * 处理积分商品列表的通用逻辑
     */
    private Consumer<Map<String, Object>> processPointProductForApp = tempMap -> {
        //处理完全图片，覆盖原来的地址
        String completedImg = (String) tempMap.get("firstImg");
        if (StringUtils.isNotEmpty(completedImg)) {
            tempMap.put("firstImg", ossUrl + completedImg);
        }
        //处理价格
        Integer flashSale = (Integer) tempMap.get("flashSale");
        Object orginPoint = tempMap.get("point");
        if (NumberUtils.INTEGER_ONE.equals(flashSale)) {
            //如果是限时商品
            tempMap.put("point", tempMap.get("discountPoint"));
            tempMap.put("orginPoint", orginPoint);

        } else {
            //普通商品
            tempMap.put("orginPoint", orginPoint);
        }
        tempMap.remove("discountPoint");
    };

    /**
     * 查询积分商城首页的商品分类信息
     * App端只精确到一级目录
     *
     * @author :zhongpingtang
     */
    @RequestMapping("/loadMallCategories")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> loadMallCategories() {
        List result = (List) tryLoadDataFromRedis(CATES, true, categoryService::listCategories, (dataFormDataBase) -> {
            if (!BaseUtil.collectionIsEmpty((Collection) dataFormDataBase)) {
                List<String> list =(List)((Collection) dataFormDataBase).stream().map(e -> JsonUtils.toJson(e)).collect(Collectors.toList());
                Collections.reverse(list);
                JedisClusterUtils.setList(CATES,  list);
            }
        });

        List resultData = Collections.EMPTY_LIST;
        if (!BaseUtil.collectionIsEmpty(result)) {
            resultData = (List) result.stream().map(e -> JsonUtils.json2Object((String) e, CategoryNode.class, DateUtils.DATE)).collect(Collectors.toList());
            //添加全部
            resultData.add(0, new CategoryNode(0, "全部", null));
        }

        return AppResultUtil.successOfList(resultData, "nodes", "parentId");
    }

    /**
     * 根据条件筛选出符合结果的商品
     *
     * @param pointMallQuery
     * @return
     */
    @RequestMapping("/filterProduct")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> filterProduct(PointMallQuery pointMallQuery, Pager pager) {
        pager.setInfiniteMode(true);
        pointMallQuery.setQueryColumnId("forAppPointMallFlash");
        return AppResultUtil.successOfPagerInProperties(productService.findAppPointMallIndex(pointMallQuery, pager),commonProductIncludeProperties)
                            .processObjInList(processPointProductForApp)
                            .addResParameter("systemTime", new Date());
    }

    /**
     * 查询我能兑换的商品
     *
     * @param pointMallQuery
     * @return
     */
    @RequestMapping("/productsICanPay")
    @ResponseBody
    public Map<String, Object> productsICanPay(PointMallQuery pointMallQuery, Pager pager) {
        pager.setInfiniteMode(true);
        //设置起始积分(账户积分)
        RegUser loginUser = BaseUtil.getLoginUser();
        PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(loginUser.getId());
        Integer accountPoint = pointAccount.getPoint();
        pointMallQuery.setPointEnd(accountPoint);
        //返回用户商品
        return filterProduct(pointMallQuery, pager);
    }

    /**
     * 获取用户积分值
     *
     * @return
     */
    @RequestMapping("/getUserPoint")
    @ResponseBody
    public Map<String, Object> getUserPoint() {

        PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(BaseUtil.getLoginUser().getId());
        Integer accountPoint = pointAccount.getPoint();
        return AppResultUtil.successOfMsg("查询成功").addResParameter("accountPoint", accountPoint);
    }


    /**
     * 查询限时抢购商品
     *
     * @return
     */
    @RequestMapping("/findFlashSales")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> findFlashSales(PointMallQuery pointMallQuery, Pager pager) {
        pointMallQuery.setQueryColumnId("forAppPointMallFlash");
        pager.setInfiniteMode(true);
        return AppResultUtil.successOfPagerInProperties(productService.findIndexFlashSales(pointMallQuery, pager),commonProductIncludeProperties).processObjInList(processPointProductForApp).addResParameter("systemTime", new Date());
    }


    /**
     * 查询商品的详细信息
     *
     * @return
     */
    @RequestMapping("/findProductDetail")
    @ResponseBody
    public Map<String, Object> findProductDetail(@RequestParam("productId") Integer productId) {
        PointProductVO signleProduct = productService.findPointProductWithImgById(productId);

        if (signleProduct == null) {
            return AppResultUtil.errorOfMsg("商品不存在");
        }

        List<PointProductImg> productImgList = signleProduct.getProductImgList();
        List<String> imgsList = new ArrayList<>();
        if (productImgList != null) {
            imgsList = productImgList.stream().map(e -> ossUrl + e.getBigImgUrl()).collect(Collectors.toList());
        }

        PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(BaseUtil.getLoginUser().getId());
        Integer accountPoint= pointAccount.getPoint();//可用积分
        Integer originPoint= signleProduct.getPoint();//原有积分
        String productInfo =signleProduct.getPrductInfo();
        productInfo = productInfo.replace("\t", "");
        productInfo = productInfo.replace("\r", "");
        productInfo = productInfo.replace("<p>\r\n\t", "");
        productInfo = productInfo.replace("<p>\n", "");
        productInfo = productInfo.replace("<p>\r\n", "");
        productInfo = productInfo.replace("\n</p>", "");
        signleProduct.setPrductInfo(HtmlUtils.Html2Text(productInfo));
        if(signleProduct.getFlashSale() ==PointConstants.POINT_FLASH_SALE_ONE){
            signleProduct.setPoint(signleProduct.getDiscountPoint());//当前所需积分
        }
        AppResultUtil.ExtendMap extendMap = AppResultUtil.successOfInProperties(signleProduct, "查询成功", commonProductIncludeProperties)
                                                         .addResParameter("imgsList", imgsList)
                                                         .addResParameter("firstImg", imgsList.get(0))
                                                         .addResParameter("accountPoint", accountPoint)
                                                        .addResParameter("originPoint", originPoint);
        extendMap.addResParameter("systemTime", new Date());
        return extendMap;
    }
    /**
     *  @Description    : 查询友乾人页面推荐的积分商品
     *  @Method_Name    : findRecomProductsForApp;
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年9月28日 下午4:52:45;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findRecomProductsForApp")
    @ResponseBody
    public Map<String, Object> findRecomProductsForApp(HttpServletRequest request) {
        return AppResultUtil.successOfListInProperties(productService.recommendProductsForApp(), "查询成功","id","name","firstImg","point")
                .processObjInList(processPointProductForApp);
    }

}
