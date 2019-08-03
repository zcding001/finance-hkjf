package com.hongkun.finance.api.controller.version;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.vas.service.SysAppVersionRuleService;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : app版本更新控制类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.api.controller.version.VersionController
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Controller
@RequestMapping("/versionController")
public class VersionController {

    @Reference
    private SysAppVersionRuleService sysAppVersionRuleService;
    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;
    private final static Logger logger = LoggerFactory.getLogger(VersionController.class);

    /**
     *  @Description    ：判断app版本是否需要更新
     *  @Method_Name    ：getVersionRule 获取app版本更新规则
     *  @param platform  平台：1-ios，2-android
     *  @param version   app版本
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/5/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getVersionRule")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> getVersionRule(String platform, String version){
        ResponseEntity result = sysAppVersionRuleService.findRuleCountByVersion(platform,version);
        //拼接安卓app下载url
        if (result.getParams().get("androidApkKey") != null && StringUtils.isNotBlank(result.getParams().
                get("androidApkKey").toString())){
            result.getParams().put("androidApkUrl",ossUrl + result.getParams().get("androidApkKey").toString());
        }
        return AppResultUtil.mapOfResponseEntity(result,"androidApkKey");
    }

    /**
     *  @Description    ：根据ios传递的版本判断该版本是否在审核中
     *  @Method_Name    ：getIosAuditVersion
     *  @param version  ios当前版本号
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/6/12
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getIosAuditVersion")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String,Object> getIosAuditVersion(String version){
        return AppResultUtil.mapOfResponseEntity(sysAppVersionRuleService.getIosAuditVersion(version));
    }

    /**
     *  @Description    ：获取安卓app补丁信息
     *  @Method_Name    ：getAndroidPatch
     *  @param packageName  包名称：测试环境cxj.test；预发布环境cxj.preonline；生成环境cxj.online；以安卓端配置为准
     *  @param version  app版本号
     *  @param patchNum  补丁版本号
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/7/18
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getAndroidPatch")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String,Object> getAndroidPatch(String packageName,String version,Integer patchNum){
        Map<String,Object> result = new HashMap<>(2);
        result.put("patchUrl","");
        if (StringUtils.isBlank(packageName) || StringUtils.isBlank(version) || patchNum == null){
            return AppResultUtil.errorOfMsg("参数丢失，请检查参数！");
        }
        //补丁包文件名前缀
        String pre = "android/patch/" + packageName + "_" + version + "_";
        String suffix = ".jar";
        //初始版本号
        int initPatchNum = patchNum;
        //补丁包文件key从补丁号 patchNum + 1 开始
        patchNum++;
        String key = pre + patchNum + suffix;
        //获取该版本最高版本的补丁，最终补丁号为(patchNum-1)
        while (OSSLoader.getInstance().doesObjectExist(OSSBuckets.HKJF,key)){
            patchNum++;
            key = pre + patchNum + suffix;
        }
        //如果最终的（patchNum-1）> initPatchNum 说明有更高的补丁需要更新,否则说明不需要进行补丁更新
        if ((patchNum - 1) > initPatchNum){
            //返回该补丁的下载地址
            result.put("patchUrl", ossUrl + pre + (patchNum - 1) + suffix);
        }
        return result;
    }
}
