package com.hongkun.finance.web.controller.point;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.facade.PointProductFacade;
import com.hongkun.finance.point.model.PointActivityRecord;
import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.vo.PointProductVO;
import com.hongkun.finance.point.service.PointActivityRecordService;
import com.hongkun.finance.point.service.PointProductService;
import com.hongkun.finance.point.support.CategoryComponent;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.img.ImageHelper;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 处理后台积分商品相关Controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.point.ProductController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/pointProductController")
public class PointProductController {
    private static final Logger logger = LoggerFactory.getLogger(PointProductController.class);
    @Reference
    private PointProductFacade productFacade;

    @Reference
    private PointProductService pointProductService;
    @Reference
    private PointActivityRecordService  pointActivityRecordService;


    /**
     * 加载商品列表
     * @param pointProductVO
     * @param pager
     * @return
     */
    @RequestMapping("/pointProductList")
    @ResponseBody
    public ResponseEntity pointProductList(PointProductVO pointProductVO, Pager pager){
        return productFacade.listPointProductList(pointProductVO,pager);
    }

    /**
     * 查询单个商品的详细信息
     * @param productId
     * @return
     */
    @RequestMapping("/pointProductInfo")
    @ResponseBody
    public ResponseEntity pointProductInfo(@RequestParam("id")Integer productId){
        return  productFacade.selectPointProductInfo(productId);
    }


    /**
     * 加载商品列表
     * @param pointProductVO
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    @Token
    public ResponseEntity save(@Validated({PointProductVO.SAVE.class}) PointProductVO pointProductVO){
       return  productFacade.savePointProduct(pointProductVO);
    }

    /**
     * 更新指定商品
     * @param pointProductVO
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @Token
    public ResponseEntity update(@Validated({PointProductVO.UPDATE.class})PointProductVO pointProductVO){
        return  productFacade.updatePointProduct(pointProductVO);
    }

    /**
     * 审核指定商品
     * @param pointProductVO
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    @Token
    public ResponseEntity check(@Validated({PointProductVO.CHECK.class})PointProductVO pointProductVO){
        return  pointProductService.checkPointProduct(pointProductVO);
    }

    /**
     * 商品上架操作
     * @param id
     * @return
     */
    @RequestMapping("/productUp")
    @ResponseBody
    @Token
    public ResponseEntity productUp(@RequestParam("id") Integer id){
        return pointProductService.productUpOrOff(id, PointConstants.ON_SALE);
    }

    /**
     * 商品下架架操作
     * @param id
     * @return
     */
    @RequestMapping("/productOff")
    @ResponseBody
    @Token
    public ResponseEntity productOff(@RequestParam("id") Integer id){
        return pointProductService.productUpOrOff(id, PointConstants.CHECK_PASS);
    }

    /**
     * 积分商城同时上传大图和小图
     * @param platform
     * @param filePath
     * @param fileType
     * @param multipartFile
     * @return
     */
    @RequestMapping("/saveAndResizeImg")
    @ResponseBody
    @Token
    public ResponseEntity saveAndResizeImg(
            @RequestParam("platform") String platform,
            @RequestParam("filePath") String filePath,
            @RequestParam("fileType") String fileType,
            @RequestParam("unUpFile") MultipartFile multipartFile){
        /**
         * step 1:转换文件
         */
        File bigImg = BaseUtil.transferMultiPartFileToFile(multipartFile);
        File tempSmallFile = BaseUtil.createTempFile(StringUtils.getFilenameExtension(bigImg.getName()));

        try {
            FileCopyUtils.copy(bigImg, tempSmallFile);
        } catch (IOException e) {
            logger.error("复制图片异常，异常信息为：{}",e);
        }
        /**
         * step 2:上传大图
         */
        Object bigResult= BaseUtil.uploadFile(platform, fileType, filePath, bigImg);
        if (bigResult instanceof ResponseEntity) {
            return ResponseEntity.class.cast(bigResult);
        }
        FileInfo BigImgInfo = FileInfo.class.cast(bigResult);
        /**
         * step 3:校验文件状态，确定是否上传成功
         */
        if (BigImgInfo.getFileState()== FileState.SAVED) {
            /**
             * step 4:转换成小图
             */
            try {

                File smallFile = BaseUtil.createTempFile(StringUtils.getFilenameExtension(bigImg.getName()));
                ImageHelper.aliResizeImage(tempSmallFile, smallFile, 350, 350);
                    /**
                     * step 5:上传小图
                     */
                    Object  smallResult= BaseUtil.uploadFile(platform, fileType, filePath, smallFile);
                    //出错直接返回
                    if (smallResult instanceof ResponseEntity) {
                        return ResponseEntity.class.cast(smallResult);
                    }
                    FileInfo smallFileInfo = FileInfo.class.cast(smallResult);
                    if (smallFileInfo.getFileState()== FileState.SAVED) {
                        ResponseEntity successResponse = new ResponseEntity(SUCCESS, "上传成功");
                        successResponse.getParams().put("saveKey",BigImgInfo.getSaveKey() + "-" + smallFileInfo.getSaveKey() );
                        return successResponse;
                    }

            } catch (Exception e) {
                logger.error("上传图片异常，异常信息为：{}",e);
            }


        }
        return new ResponseEntity(ERROR, "上传失败，请重试");
    }
    /**
     *  @Description    : 根据商品ID，查询促俏记录
     *  @Method_Name    : findSellingRecord;
     *  @param id
     *  @return
     *  @return         : ResponseEntity<?>;
     *  @Creation Date  : 2018年9月17日 上午11:38:57;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/findSellingRecord")
    @ResponseBody
    public ResponseEntity<?> findSellingRecord(@RequestParam("id") Integer id,Pager pager){
        return pointActivityRecordService.findSellingRecordList(id, pager);
    }
    /***
     *  @Description    : 逻缉删除商品信息
     *  @Method_Name    : delete;
     *  @param pointProductVO
     *  @return
     *  @return         : ResponseEntity;
     *  @Creation Date  : 2018年11月19日 上午10:24:51;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    @RequestMapping("/delete")
    @ResponseBody
    @Token
    public ResponseEntity<?> delete(@RequestParam("id") Integer id){
        PointProduct pointProduct = new PointProduct();
        pointProduct.setId(id);
        pointProduct.setState(PointConstants.LOGICAL_DEL);
        pointProductService.updatePointProduct(pointProduct);
        return new ResponseEntity<>(Constants.SUCCESS,"删除成功");
    }

}
