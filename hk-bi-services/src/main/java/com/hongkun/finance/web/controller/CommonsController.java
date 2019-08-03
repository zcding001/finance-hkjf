package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 独立于业务之外的通用请求方法
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.CommonsController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Controller
@RequestMapping("/commonsController")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class CommonsController {

    @Reference
    private PrivilegeSrvice privilegeSrvice;



    /**
     * 检测用户是否已经登录(前端用户)
     *
     * @return
     */
    @RequestMapping("/userIsLogin")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public ResponseEntity<String> userIsLogin() {
        return ResponseEntity.SUCCESS;
    }



    @RequestMapping("/uploadFile")
    @ResponseBody
    public ResponseEntity uploadFile(
            @RequestParam("platform") String platform,
            @RequestParam("filePath") String filePath,
            @RequestParam("fileType") String fileType,
            @RequestParam("unUpFile") MultipartFile multipartFile) {

        Object resutlt=BaseUtil.uploadFile(platform, fileType, filePath, multipartFile);

        if (resutlt instanceof ResponseEntity) {
            return ResponseEntity.class.cast(resutlt);
        }

        FileInfo fileInfo = FileInfo.class.cast(resutlt);
        /**
         * step 4:校验文件状态，确定是否上传成功
         */
        if (fileInfo.getFileState()== FileState.SAVED) {
            ResponseEntity successResponse = new ResponseEntity(SUCCESS, "上传成功");
            successResponse.getParams().put("saveKey", fileInfo.getSaveKey());
            return successResponse;
        }else{
            return new ResponseEntity(201, "上传失败，请重试");
        }


    }


}
