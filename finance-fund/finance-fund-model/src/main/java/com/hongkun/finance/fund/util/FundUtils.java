package com.hongkun.finance.fund.util;

import com.hongkun.finance.fund.model.vo.FundInfoVo;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.hongkun.finance.fund.constants.FundConstants.*;

/**
 * @Description : 股权工具类
 * @Project : finance-fund-model
 * @Program Name : com.hongkun.finance.fund.util.FundUtils.java
 * @Author : xinbangcao@hongkun.com.cn 曹新帮
 */
public class FundUtils {

    /**
     * 自动拒绝原因
     */
    public final static String refuseReson = "您填写的购买协议不具备投资者购买资格，可以拨打客服电话：4009009630咨询。";

    /**
     *  @Description    ：校验是否在工作日内
     *  @Method_Name    ：checkOpenDate
     *  @param fundInfo
     *  @return boolean
     *  @Creation Date  ：2018年05月08日 10:22
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static boolean checkOpenDate(FundInfoVo fundInfo) {
        String[] weekTime = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        boolean flag = true;
        Date currentDate = new Date();
        if (fundInfo.getInfoExist()== 1) {
            if (null != fundInfo.getOpendayType()) {
                // 工作日
                if (fundInfo.getOpendayType() == FUND_INFO_OPENDAY_TYPE_WORKDAY) {
                    if (getDayInDate(currentDate).equals("周六")
                            || getDayInDate(currentDate).equals("周日")) {
                        flag = false;
                    }

                } else if (fundInfo.getOpendayType() == FUND_INFO_OPENDAY_TYPE_CUSTOM) {
                    // 每周
                    if (null != fundInfo.getCustomizeType() && fundInfo.getCustomizeType() == FUND_INFO_CUSTOMIZE_TYPE_WEEK) {
                        String dataString = getDayInDate(currentDate);
                        if (!dataString.equals(weekTime[fundInfo.getCustomizeValue() - 1])) {
                            flag = false;
                        }
                    // 每月
                    } else if (!StringUtilsExtend.isEmpty(fundInfo.getCustomizeType()) && fundInfo.getCustomizeType() == FUND_INFO_CUSTOMIZE_TYPE_MONTH) {
                        int dayInt = getDayInString(DateUtils.format(currentDate,DateUtils.DATE_HH_MM_SS));
                        if (dayInt != fundInfo.getCustomizeValue()) {
                            flag = false;
                        }
                    }
                // 自定义时间
                } else if (fundInfo.getOpendayType() == FUND_INFO_OPENDAY_TYPE_RANGE) {
                    if (!StringUtilsExtend.isEmpty(fundInfo.getStartTime()) && !StringUtilsExtend.isEmpty(fundInfo.getEndTime())) {
                        if (currentDate.before(fundInfo.getStartTime())
                                || currentDate.after(fundInfo.getEndTime())) {
                            flag = false;
                        }

                    }

                }

            }

        }
        return flag;
    }

    /**
     *  @Description    ：获取某一日期为周几
     *  @Method_Name    ：getDayInDate
     *  @param date
     *  @return java.lang.String
     *  @Creation Date  ：2018年05月08日 10:38
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String getDayInDate(Date date) {
        int i = 1;
        Calendar dar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String strDate = format.format(date);
            Date dateTemp = format.parse(strDate);
            dar.setTime(dateTemp);
        } catch (Exception e) {
            date = new Date();
            dar.setTime(date);
        }
        i = dar.get(Calendar.DAY_OF_WEEK) - 1;
        String[] day = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        return day[i];
    }

    /**
     *  @Description    ：从字符串中获取天
     *  @Method_Name    ：getDayInString
     *  @param str
     *  @return int
     *  @Creation Date  ：2018年05月08日 10:51
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static int getDayInString(String str) {
        String day = "01";
        int i = 0;
        if (str != null) {
            i = str.substring(5, str.length()).indexOf("-");
            if (str.length() > 5 + i + 3) {
                day = str.trim().substring(5 + i + 1, 5 + i + 3);
            } else {
                day = str.trim().substring(5 + i + 1, str.length());
            }
        }
        return Integer.parseInt(day.trim());
    }



    /**
     *  @Description    ：转换显示项目名称
     *  @Method_Name    ：transferFundInfoName
     *  @param infoName
     *  @return java.lang.String
     *  @Creation Date  ：2018年05月08日 13:05
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String transferFundInfoName(String infoName){
        if(StringUtils.isEmpty(infoName)){
            return "";
        }
        if(infoName.length() > 9){
            StringBuffer tempStr = new StringBuffer();
            int len = infoName.length();
            tempStr.append(infoName.substring(0,2));
            tempStr.append("***");
            tempStr.append(infoName.substring(len-3,len));

            return tempStr.toString();
        }
        return infoName;
    }

    /**
     *  @Description    ：转换电话格式
     *  @Method_Name    ：transferTel
     *  @param tel
     *  @return java.lang.String
     *  @Creation Date  ：2018年05月08日 13:05
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String transferTel(String tel){
        if(StringUtils.isEmpty(tel)){
            return "";
        }
        StringBuffer tempStr = new StringBuffer();
        int len = tel.length();
        tempStr.append(tel.substring(0,3));
        tempStr.append("****");
        tempStr.append(tel.substring(len-4,len));
        return tempStr.toString();
    }


    /**
     *  @Description    ：转换身份证号码
     *  @Method_Name    ：transferIdCard
     *  @param idCard
     *  @return java.lang.String
     *  @Creation Date  ：2018年05月09日 10:00
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String transferIdCard(String idCard){
        if(StringUtils.isEmpty(idCard)){
            return "";
        }
        StringBuffer tempStr = new StringBuffer();
        int len = idCard.length();
        tempStr.append(idCard.substring(0,3));
        tempStr.append("********");
        if(len == 18){
            tempStr.append(idCard.substring(14,18));
        }else if(len == 15){
            tempStr.append(idCard.substring(11,15));
        }
        return tempStr.toString();
    }


    /**
     * 
     *  @Description    : 获取开放日
     *  @Method_Name    : getFundOpenTime;
     *  @param fundInfoVo
     *  @return
     *  @return         : String;
     *  @Creation Date  : 2018年5月9日 上午10:51:15;
     *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
     */
    public static String getFundOpenTime(FundInfoVo fundInfoVo) {
        String[] weekTime = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		StringBuffer fundOpenTime = new StringBuffer();
        Integer opendayType = fundInfoVo.getOpendayType();
        if(opendayType == FUND_INFO_OPENDAY_TYPE_WORKDAY){
        	fundOpenTime.append("工作日");
        }else if(opendayType == FUND_INFO_OPENDAY_TYPE_CUSTOM){
            if(fundInfoVo.getCustomizeType() != null && fundInfoVo.getCustomizeValue()!= null){
                Integer customizeType = fundInfoVo.getCustomizeType() ;
                Integer customizeValue = fundInfoVo.getCustomizeValue();
                if (customizeType == FUND_INFO_CUSTOMIZE_TYPE_WEEK){
                	fundOpenTime.append("每周");
                    for (int i = 0; i < weekTime.length; i++) {
                        if (customizeValue == i + 1) {
                        	fundOpenTime.append(weekTime[i]);
                        }
                    }
                }else if (customizeType == FUND_INFO_CUSTOMIZE_TYPE_MONTH){
                	fundOpenTime.append("每月").append(customizeValue + "号");
                }
            }
        }else if(opendayType == FUND_INFO_OPENDAY_TYPE_RANGE){
            if(!StringUtilsExtend.isEmpty(fundInfoVo.getStartTime())){
                fundOpenTime.append(DateUtils.format(fundInfoVo.getStartTime()));
            }
            fundOpenTime.append("~");
            if(!StringUtilsExtend.isEmpty(fundInfoVo.getEndTime())){
                fundOpenTime.append(DateUtils.format(fundInfoVo.getEndTime()));
            }
        }
    return fundOpenTime.toString();
	}

    /**
     *  @Description    ：从身份证号中提取出生日期
     *  @Method_Name    ：idCardTransferToBir
     *  @param idCard
     *  @return java.lang.String
     *  @Creation Date  ：2018年07月04日 09:37
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String idCardTransferToBir(String idCard) {
        if(StringUtils.isEmpty(idCard)) return "";
        return idCard.substring(10,12) + "/" + idCard.substring(12,14) + "/" + idCard.substring(6,10);
    }

    /**
     *  @Description    ：对象属性转化为字符串
     *  @Method_Name    ：formatToStr
     *  @param obj
     *  @param propertyName
     *  @return java.lang.String
     *  @Creation Date  ：2018年07月05日 11:11
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static String formatToStr(Object obj,String propertyName){
        if(null == obj || null == propertyName) return "";
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(obj) == null ? "" :(String)field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *  @Description    ：身份证中提取性别
     *  @Method_Name    ：getSexFromIdCard
     *  @param idCard
     *  @return java.lang.Integer  0：女  1：男
     *  @Creation Date  ：2018年07月17日 15:02
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    public static Integer getSexFromIdCard(String idCard) {
        if(StringUtils.isEmpty(idCard)) return 0;
        return Integer.valueOf(idCard.length() == 15 ? idCard.substring(14,15) : idCard.substring(16,17)) % 2 == 0 ? 0 : 1;
    }

    /**
     *
     *  @Description    : 获取存续期限描述
     *  @Method_Name    : getTermDes;
     *  @param fundInfoVo
     *  @return
     *  @return         : String;
     *  @Creation Date  : 2018年8月13日16:04:45
     *  @Author         : yuzegu@hongkun.com.cn;
     */
    public static String getTermDes(FundInfoVo fundInfoVo) {
        StringBuffer termDes = new StringBuffer();
        Integer termUnit = fundInfoVo.getTermUnit();
        Integer termValue = fundInfoVo.getTermValue();
        termDes.append(termValue);
        if(FUND_INFO_TERM_UNIT_YEAR == termUnit){
            termDes.append("年");
        }else if (FUND_INFO_TERM_UNIT_MONTH == termUnit){
            termDes.append("个月");
        }else if(FUND_INFO_TERM_UNIT_DAY == termUnit){
            termDes.append("天");
        }
        return termDes.toString();
    }
}
