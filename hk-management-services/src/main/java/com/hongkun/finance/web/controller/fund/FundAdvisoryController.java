package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.FundAdvisoryFacade;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundProject;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.hongkun.finance.fund.service.FundAdvisoryService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_INFO_STATE_DELETE;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 股权项目预约控制层
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.controller.fund.FundAdvisoryController
 * @Author : yunlongliu@yiruntz.com
 */
@Controller
@RequestMapping("/fundAdvisoryController")
public class FundAdvisoryController {

    @Reference
    private FundAdvisoryService fundAdvisoryService;

    @Reference
    private FundAdvisoryFacade fundAdvisoryFacade;

    /**
     * @Description ：条件检索股权类型信息列表
     * @Method_Name ：fundTypeList
     * @param project
     * @param pager
     * @return com.yirun.framework.core.model.ResponseEntity<?>
     * @Creation Date  ：2018年04月28日 09:28
     * @Author ：yunlongliu@hongkun.com.cn
     */
    @ResponseBody
    @RequestMapping("/fundTypeList")
    public ResponseEntity<?> fundTypeList(FundProject project, Pager pager) {
        return new ResponseEntity<>(SUCCESS, this.fundAdvisoryService.findFundTypeListWithCondition(project, pager));
    }


    /**
     * @Description ：条件查询股权咨询信息列表
     * @Method_Name ：fundAdvisoryList
     * @param advisoryVo
     * @param pager
     * @return com.yirun.framework.core.model.ResponseEntity<?>
     * @Creation Date  ：2018年04月28日 14:13
     * @Author ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/fundAdvisoryList")
    @ResponseBody
    public ResponseEntity<?> fundAdvisoryList(FundAdvisoryVo advisoryVo, Pager pager) {
        return new ResponseEntity<>(SUCCESS, this.fundAdvisoryFacade.findFundAdvisoryList(advisoryVo, pager));
    }


    /**
     * @Description ：删除信息
     * @Method_Name ：deleteFundAdvisory
     * @param advisory
     * @return com.yirun.framework.core.model.ResponseEntity<?>
     * @Creation Date  ：2018年05月02日 16:34
     * @Author ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/delete")
    @ResponseBody
    @ActionLog(msg = "删除股权咨询信息, 信息id: {args[0].id} , 信息状态state: {args[0].state}")
    public ResponseEntity<?> deleteFundAdvisory(FundAdvisory advisory) {
        advisory.setState(FUND_INFO_STATE_DELETE);
        this.fundAdvisoryService.updateFundAdvisory(advisory);
        return ResponseEntity.SUCCESS;
    }


    /**
     *  @Description    ：保存咨询项目信息
     *  @Method_Name    ：save
     *  @param advisory
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月03日 15:57
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/save")
    @ResponseBody
    @ActionLog(msg = "保存咨询项目信息, 预约股权项目ID：{args[0].infoIds}，客户姓名：{args[0].name}，客户电话：{args[0].tel}，" +
            "备注：{args[0].remark}")
    public ResponseEntity<?> save(FundAdvisory advisory){
        advisory.setModifyUserId(BaseUtil.getLoginUser().getId());
        advisory.setRegUserId(BaseUtil.getLoginUser().getId());
        return fundAdvisoryFacade.saveFundAdvisoryInfo(advisory);
    }

    /**
     *  @Description    ：咨询信息分配给销售
     *  @Method_Name    ：assignSales
     *  @param advisory
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月04日 17:22
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/assignSales")
    @ResponseBody
    @ActionLog(msg = "咨询信息分配给销售, 预约记录ID：{args[0].id}，销售人员ID：{args[0].assignUserId}")
    public ResponseEntity<?> assignSales(FundAdvisory advisory) {
        this.fundAdvisoryFacade.assignFundAdvisoryToSale(advisory);
        return ResponseEntity.SUCCESS;
    }

    /**
     *  @Description    ：更新咨询信息
     *  @Method_Name    ：updateFundAdvisory
     *  @param advisory
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月05日 14:41
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/update")
    @ResponseBody
    @ActionLog(msg = "更新股权咨询信息, 信息id: {args[0].id} , 信息状态state: {args[0].state}")
    public ResponseEntity<?> updateFundAdvisory(FundAdvisory advisory) {
        advisory.setModifyUserId(BaseUtil.getLoginUser().getId());
        advisory.setRegUserId(BaseUtil.getLoginUser().getId());
        this.fundAdvisoryService.updateFundAdvisory(advisory);
        return ResponseEntity.SUCCESS;
    }


    /**
     *  获取股权类型详情
     *  @Method_Name    ：findFundProjectInfo
     *  @param id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年05月11日 09:50
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("findFundProjectInfo")
    @ResponseBody
    public ResponseEntity<?> findFundProjectInfo(@RequestParam("id") Integer id){
        FundProject fundProject = this.fundAdvisoryService.findFundProjectInfo(id);
        if(null == fundProject){
            return new ResponseEntity<>(Constants.ERROR, "没有查询到股权类型详情！");
        }
        return new ResponseEntity<>(Constants.SUCCESS,fundProject);
    }

    /**
     *  @Description    ：海外基金状态审核
     *  @Method_Name    ：fundAgreementAudit
     *  @param advisoryVo
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年07月06日 15:49
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/fundAgreementAudit")
    @ResponseBody
    public ResponseEntity<?> fundAgreementAudit(FundAdvisoryVo advisoryVo) {
        this.fundAdvisoryFacade.fundAgreementAudit(advisoryVo);
        return ResponseEntity.SUCCESS;
    }

    @RequestMapping("/fundAgreementDownLoad")
    @ResponseBody
    public ResponseEntity fundAgreementDownLoad(@RequestParam("id") Integer id, HttpServletResponse response){
        ResponseEntity result = fundAdvisoryFacade.fundAgreementDownLoad(id);
        if (result.getResStatus() == ERROR){
            return result;
        }
        String zipName = (String) result.getParams().get("zipName");
        Map<String,String> keyFileNameMap = (Map<String, String>) result.getParams().get("keyFileNameMap");

        String classUrl = this.getClass().getResource("").getPath().replaceAll("%20", " ");
        //读取本地的pdf模板，先要区分使用什么模板
        String path = classUrl.substring(0,classUrl.indexOf("WEB-INF")) + "WEB-INF/" + zipName;
        //打包oss端相关下载文件
        DownLoadUtil.downOssFileToZip(path,keyFileNameMap);
        //将打包好的zip包下载
        DownLoadUtil.downLoadFile(path,response);

        return new ResponseEntity(SUCCESS);
    }

    /**
     *  @Description    ：获取股权预约信息详情
     *  @Method_Name    ：findFundAdvisoryInfo
     *  @param id
     *  @return com.yirun.framework.core.model.ResponseEntity
     *  @Creation Date  ：2018年08月03日 16:50
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    @RequestMapping("/findFundAdvisoryInfo")
    @ResponseBody
    public ResponseEntity findFundAdvisoryInfo(@RequestParam("id") Integer id){
        return fundAdvisoryService.findFundAdvisoryInfo(id);
    }




}
