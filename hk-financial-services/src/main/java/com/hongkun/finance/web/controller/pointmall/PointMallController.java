package com.hongkun.finance.web.controller.pointmall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.model.CategoryNode;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hongkun.finance.point.constants.PointConstants.CATES;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * 积分商城Controller
 */
@Controller
@RequestMapping("/pointMallController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class PointMallController {
    private static final Logger logger = LoggerFactory.getLogger(PointMallController.class);

    @Reference
    private PointProductCategoryService categoryService;
    @Reference
    private PointProductService productService;
    @Reference
    private PointAccountService pointAccountService;

    /**
     * 积分商城首页分类加载
     *
     * @return
     */
    @RequestMapping("/loadMallCategories")
    @ResponseBody
    public ResponseEntity loadMallCategories() {
        List result = (List)tryLoadDataFromRedis(CATES,true,categoryService::listCategories,(dataFormDataBase)->{
            if (!BaseUtil.collectionIsEmpty((Collection) dataFormDataBase)) {
                List<String> list =(List)((Collection) dataFormDataBase).stream().map(e -> JsonUtils.toJson(e)).collect(Collectors.toList());
                Collections.reverse(list);
                JedisClusterUtils.setList(CATES,  list);
            }
        });

        if (!BaseUtil.collectionIsEmpty(result)&&String.class.equals(CollectionUtils.findCommonElementType(result))) {
            return new ResponseEntity(SUCCESS
                    , result.stream().map(e -> JsonUtils.json2Object((String) e, CategoryNode.class, DateUtils.DATE)).collect(Collectors.toList()));
        }
        return new ResponseEntity(SUCCESS, Collections.emptyList());
    }


    /**
     * 限时抢购类栏目数据
     *
     * @return
     */
    @RequestMapping("/findIndexFlashSales")
    @ResponseBody
    public ResponseEntity findIndexFlashSales(PointMallQuery pointMallQuery, Pager pager) {
        return new ResponseEntity(SUCCESS, productService.findIndexFlashSales(pointMallQuery, pager));
    }


    /**
     * 推荐商品，热门兑换，其他商品
     *
     * @return
     */
    @RequestMapping("/findIndexProducts")
    @ResponseBody
    public ResponseEntity findIndexProducts() {
        return new ResponseEntity(SUCCESS, productService.findIndexProducts());
    }


    /**
     * 查询单件商品
     *
     * @return
     */
    @RequestMapping("/findProduct")
    @ResponseBody
    public ResponseEntity findProduct(@RequestParam("id") Integer id) {
        return new ResponseEntity(SUCCESS, productService.findPointProductWithImgById(id));
    }


    /**
     * 根据条件搜索商品
     *
     * @param pointMallQuery
     * @return
     */
    @RequestMapping("/filterProduct")
    @ResponseBody
    public ResponseEntity filterProduct(PointMallQuery pointMallQuery, Pager pager) {
        return new ResponseEntity(SUCCESS, productService.filterProduct(pointMallQuery, pager));
    }

    /**
     * 让用登录或者返回用户的积分
     *
     * @return
     */
    @RequestMapping("/loginOrReturnPoint")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public ResponseEntity loginOrReturnPoint() {
        ResponseEntity userInfo = new ResponseEntity(SUCCESS);
        RegUser loginUser = BaseUtil.getLoginUser();
        PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(loginUser.getId());
        userInfo.getParams().put("userName", loginUser.getNickName());
        userInfo.getParams().put("point", pointAccount.getPoint());
        userInfo.getParams().put("login_reg_user", loginUser);
        return userInfo;
    }


}
