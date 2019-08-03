package com.hongkun.finance.web.controller.privilege;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.AppMoreServe;
import com.hongkun.finance.user.service.AppMoreServeService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description: APP菜单管理Controller
 * @Author: hanghe@hongkunjinfu.com
 * @Date: 2019/1/15 11:05
 */
@Controller
@RequestMapping("/appMenuController")
public class APPMenuController {
    private static final Logger LOGGER = Logger.getLogger(APPMenuController.class);

    @Reference
    private AppMoreServeService appMoreServeService;

    /**
     * @Description: 分页列出所有的菜单
     * @param appMoreServe
     * @param pager
     * @return: com.yirun.framework.core.model.ResponseEntity
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2019/1/15 11:04
     */
    @RequestMapping("/listAppServes")
    @ResponseBody
    public ResponseEntity listAppServes(AppMoreServe appMoreServe, Pager pager) {
        return new ResponseEntity(SUCCESS, appMoreServeService.findAppMoreServeList(appMoreServe,pager));
    }

    /**
     * @Description: 添加App端的菜单
     * @param appMoreServe
     * @param unSavedFile
     * @return: com.yirun.framework.core.model.ResponseEntity
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2019/1/15 11:04
     */
    @RequestMapping("/addAppServe")
    @ResponseBody
    @ActionLog(msg = "添加APP菜单, 菜单名称: {args[0].serviceName}")
    public ResponseEntity addAppServe(AppMoreServe appMoreServe, @RequestParam("unSavedFile") MultipartFile unSavedFile) {
        if (setServeImg(appMoreServe, unSavedFile)){
            return ResponseEntity.error("菜单文件上传失败，请重试");
        }

        //执行插入
        appMoreServeService.insertAppMoreServe(appMoreServe);
        return ResponseEntity.SUCCESS;
    }

    /**
     * @Description: 启用App端的菜单
     * @param appMoreServe
     * @return: com.yirun.framework.core.model.ResponseEntity
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2019/1/15 11:04
     */
    @RequestMapping("/enableAppServe")
    @ResponseBody
    @ActionLog(msg = "禁用APP菜单, 菜单名称: {args[0].serviceName}")
    public ResponseEntity enableAppServe(@Validated(value = UPDATE.class) AppMoreServe appMoreServe) {
        return appMoreServeService.enableAppServe(appMoreServe);

    }

    /**
     * @Description: 禁用App端的菜单
     * @param appMoreServe
     * @return: com.yirun.framework.core.model.ResponseEntity
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2019/1/15 11:05
     */
    @RequestMapping("/disableAppServe")
    @ResponseBody
    @ActionLog(msg = "启用APP菜单, 菜单名称: {args[0].serviceName}")
    public ResponseEntity disableAppServe(@Validated(value = UPDATE.class) AppMoreServe appMoreServe) {
        return appMoreServeService.disableAppServe(appMoreServe);
    }

    /**
     * @Description: 给菜单设置文件
     * @param appMoreServe
     * @param unSavedFile
     * @return: boolean
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2019/1/15 11:05
     */
    private boolean setServeImg(AppMoreServe appMoreServe, MultipartFile unSavedFile) {
        //上传文件
        Object result = BaseUtil.uploadFile("hkjf", FileType.EXT_TYPE_IMAGE.getFileTypeName(), "/appMenu", unSavedFile);
        if (result instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) result;
            if (FileState.SAVED != fileInfo.getFileState()) {
                return true;
            }
            appMoreServe.setIcoUrl(fileInfo.getSaveKey());
        }
        return false;
    }


}
