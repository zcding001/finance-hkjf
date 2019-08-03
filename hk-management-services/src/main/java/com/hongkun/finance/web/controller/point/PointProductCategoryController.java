package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.service.PointProductCategoryService;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.redis.JedisClusterUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hongkun.finance.point.constants.PointConstants.CATES;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 商品类目管理Controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.PointProductCategoryController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/pointProductCategoryController")
public class PointProductCategoryController {

    @Reference
    private PointProductCategoryService categoryService;

    /**
     * 查询目录树
     *
     * @return
     */
    @RequestMapping("/listCategories")
    @ResponseBody
    public ResponseEntity listCategories() {
        return new ResponseEntity(SUCCESS, categoryService.listCategories());
    }

    /**
     * 刷新当前redis中的菜单
     *
     * @return
     */
    @RequestMapping("/refreshCategories")
    @ResponseBody
    public ResponseEntity refreshCategories() {
        JedisClusterUtils.delete(CATES);
        return ResponseEntity.SUCCESS;
    }

    /**
     * 添加菜单
     *
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @Token
    public ResponseEntity save(PointProductCategory category) {
        return categoryService.saveCategory(category);
    }

    /**
     * 删除菜单
     *
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @Token
    public ResponseEntity delete(@RequestParam("id") Integer id) {
        return categoryService.deleteOnCascadeCate(id);
    }

    /**
     * 修改菜单
     *
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @Token
    public ResponseEntity delete(PointProductCategory category) {
        categoryService.updatePointProductCategory(category);
        return new ResponseEntity(SUCCESS, "修改分类成功");
    }
}
