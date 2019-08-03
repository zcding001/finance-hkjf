package com.hongkun.finance.web.controller.sta;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.model.StaIncome;
import com.hongkun.finance.bi.service.StaIncomeService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.sta
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Controller
@RequestMapping("/staIncomeController")
public class StaIncomeController {

    private static final Logger logger = LoggerFactory.getLogger(StaIncomeController.class);
    @Reference
    private StaIncomeService staIncomeService;
    /**
    *  @Description    ：收入统计查询
    *  @Method_Name    ：findStaIncomeList
    *  @param staIncome
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/27
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/staIncomeList")
    @ResponseBody
    public ResponseEntity staIncomeList(StaIncome staIncome, Pager pager){
        if(StringUtils.isNotBlank(staIncome.getCreateTimeBegin())){
            String thisMoth = staIncome.getCreateTimeBegin() + "-01";
            Date nextMonth = DateUtils.addMonth(DateUtils.parse(thisMoth),1);
            staIncome.setCreateTimeBegin(DateUtils.getFirstDayStrOfMonth(nextMonth));
            staIncome.setCreateTimeEnd(DateUtils.getLastDayStrOfMonth(nextMonth));
        }
        Pager resultPager = staIncomeService.findStaIncomeList(staIncome,pager);
        Map<String,Object> params =  staIncomeService.findSumStaCharges(staIncome);
        return new ResponseEntity<>(Constants.SUCCESS,resultPager,params);
    }
    /**
    *  @Description    ：收入统计导出
    *  @Method_Name    ：exportExcelForStaIncomeList
    *  @param staIncome
    *  @return void
    *  @Creation Date  ：2018/6/7
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/exportExcelForStaIncomeList")
    @ResponseBody
    public void exportExcelForStaIncomeList(StaIncome staIncome, HttpServletResponse response){
        try {
            String exportMonth = StringUtils.isNotBlank(staIncome.getCreateTimeBegin())?staIncome.getCreateTimeBegin():DateUtils.getCurrentMoth();
            if(StringUtils.isNotBlank(staIncome.getCreateTimeBegin())){
                String thisMoth = staIncome.getCreateTimeBegin() + "-01";
                Date nextMonth = DateUtils.addMonth(DateUtils.parse(thisMoth),1);
                staIncome.setCreateTimeBegin(DateUtils.getFirstDayStrOfMonth(nextMonth));
                staIncome.setCreateTimeEnd(DateUtils.getLastDayStrOfMonth(nextMonth));
            }
            List<StaIncome> staIncomeList = staIncomeService.findStaIncomeList(staIncome);
            if (CommonUtils.isNotEmpty(staIncomeList)){
                String fileName = "收入统计"+exportMonth;
                String sheetName = exportMonth;
                LinkedHashMap<String,String> params = initParamsForExcel();
                staIncomeList.forEach(sta->{
                    sta.setTransTimeStr(DateUtils.format(sta.getTransTime(),"yyyy-MM-dd HH:mm:ss"));
                });
                ExcelUtil.exportExcel(fileName,sheetName,staIncomeList,params,65535,response);
            }
        } catch (UnsupportedEncodingException e) {
            logger.info("exportExcelForStaIncomeList, 收入统计导出: {}, 异常信息:\n", staIncome.toString(),e);
        }
    }

    /**
    *  @Description    ：初始化导出excel列
    *  @Method_Name    ：initParamsForExcel
    *
    *  @return java.util.LinkedHashMap<java.lang.String,java.lang.String>
    *  @Creation Date  ：2018/6/7
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private LinkedHashMap<String,String> initParamsForExcel(){
        LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
        params.put("flowId","流水id");
        params.put("borrowerName","借款人姓名");
        params.put("borrowerTel","借款人手机号");
        params.put("tradeType","交易类型");
        params.put("transMoney","交易金额");
        params.put("pureMoney","扣息后交易金额");
        params.put("transTimeStr","交易日期");
        return params;
    }

}
