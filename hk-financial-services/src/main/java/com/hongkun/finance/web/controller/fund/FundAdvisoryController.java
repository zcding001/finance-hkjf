package com.hongkun.finance.web.controller.fund;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.FundAdvisoryFacade;
import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.FundAgreement;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.hongkun.finance.fund.service.FundAdvisoryService;
import com.hongkun.finance.fund.service.FundAgreementService;
import com.hongkun.finance.fund.service.FundInfoService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.OSSBuckets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.fund.constants.FundConstants.FUND_PROJECT_PARENT_TYPE_ABROAD;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.hongkun.finance.fund.constants.FundConstants.FUND_ADVISORY_SOURCE_PLATFORM;

/**
 * @Description : 股权基金预约相关controller
 * @Project : framework
 * @Program Name  : com.hongkun.finance.web.controller.fund.FundAdvisoryController
 * @Author : yuzegu@hongkun.com.cn googe
 */
@Controller
@RequestMapping("/fundAdvisoryController")
public class FundAdvisoryController {

    @Reference
    private FundAdvisoryService fundAdvisoryService;

    @Reference
    private RegUserDetailService regUserDetailService;

    @Reference
    private RegUserService regUserService;

    @Reference
    private FundAgreementService fundAgreementService;

    @Reference
    private FundInfoService fundInfoService;

    @Reference
    private FundAdvisoryFacade fundAdvisoryFacade;

    @Value(value = "${oss.url.hkjf}")
    private String ossUrl;

    /**
     * 预约产品
     * @return
     */
    @RequestMapping("/reservationFund")
    @ResponseBody
    public ResponseEntity reservationFund(FundAdvisoryVo fundAdvisoryVo) {
        RegUser user = BaseUtil.getLoginUser();
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUserId()));
        String infoIds = fundAdvisoryVo.getInfoIds();
        if(StringUtils.isEmpty(infoIds) || fundAdvisoryVo.getProjectParentType() == null){
            return new ResponseEntity(ERROR, "noselect");
        }
        //校验产品及父类型是否合法及可预约
        //校验每天三次
        if(!(FUND_PROJECT_PARENT_TYPE_ABROAD == fundAdvisoryVo.getProjectParentType()) ){
            int count = this.fundAdvisoryService.findAdvisoryCount(user.getId(),fundAdvisoryVo.getProjectParentType());
            if(count >= 3 ){
                return new ResponseEntity(ERROR, "more");
            }
        }else {
            return new ResponseEntity(SUCCESS, "success");
        }
        FundAdvisory fa = new FundAdvisory();
        if(StringUtils.isEmpty(fundAdvisoryVo.getName()) || StringUtils.isEmpty(fundAdvisoryVo.getTel())){
            fa.setTel(String.valueOf(user.getLogin()));
            fa.setName(regUserDetail.getRealName());
        }
        fa.setName(fundAdvisoryVo.getName());
        fa.setTel(fundAdvisoryVo.getTel());
        fa.setSex(2);//未知
        fa.setSource(FUND_ADVISORY_SOURCE_PLATFORM);
        fa.setRegUserId(user.getId());
        fa.setModifyUserId(user.getId());
        fa.setProjectParentType(fundAdvisoryVo.getProjectParentType());
        fa.setInfoIds(fundAdvisoryVo.getInfoIds());
        if(!StringUtils.isEmpty(fundAdvisoryVo.getRemark())){
            if(fundAdvisoryVo.getRemark().length() >100){
                fa.setRemark(fundAdvisoryVo.getRemark().substring(0,100));
            }else {
            fa.setRemark(fundAdvisoryVo.getRemark());
        }
        }
        fundAdvisoryService.insertFundAdvisory(fa);
        return new ResponseEntity(SUCCESS, "success");
    }

    /**
     * 我的账户——股权基金预约记录查询
     * @param pager
     * @param advisoryVo
     * @return
     */
    @RequestMapping("listReservationRecord")
    @ResponseBody
    public ResponseEntity<?> listReservationRecord(Pager pager, FundAdvisoryVo advisoryVo) {
        RegUser regUser = BaseUtil
                .getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        advisoryVo.setRegUserId(regUser.getId());
        if(advisoryVo.getCreateTimeEnd()!=null){
            advisoryVo.setCreateTimeEnd(DateUtils.addDays(advisoryVo.getCreateTimeEnd(), 1));
        }
        advisoryVo.setSortColumns("create_time desc");
        pager = this.fundAdvisoryFacade.findFundAdvisoryList(advisoryVo, pager);
        return new ResponseEntity(Constants.SUCCESS, pager);
    }

    /**
     * 从oss下载pdf
     * @param response
     * @param contractUrl
     * @throws IOException
     */
    @RequestMapping("downloadPdf")
    public void downloadPdf(HttpServletResponse response, String contractUrl) throws IOException {
        //oss下载
        //key = fileUrl.toString();
        contractUrl = contractUrl.replaceAll("\\\\", "/");
        BufferedInputStream inputStream = new BufferedInputStream(OSSLoader.getInstance().getOSSObject(OSSBuckets.HKJF,contractUrl).getObjectContent());
        //URL url = new URL(ossUrl + contractUrl);
        //HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        //conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        //InputStream inputStream = conn.getInputStream();  //返回400改用oss下载
        String[] names = contractUrl.split("/");
        BufferedInputStream br = new BufferedInputStream(inputStream);
        byte[] bs = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        // 纯下载方式
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(names[3].getBytes("gb2312"),"ISO8859-1"));
        OutputStream out = response.getOutputStream();
        while ((len = br.read(bs)) > 0) {
            out.write(bs, 0, len);
        }
        out.flush();
        out.close();
        br.close();
    }


}
