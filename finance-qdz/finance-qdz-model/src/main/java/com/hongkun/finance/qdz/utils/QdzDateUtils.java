
/**
 * 
 */
package com.hongkun.finance.qdz.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;

public abstract class QdzDateUtils {
	/***
	 * @Description : 转换为yyyy-MM-dd HH:mm:ss格式
	 * @Method_Name : format;
	 * @param date
	 *            转换的日期
	 * @param pattern
	 *            转换的格式
	 * @param days
	 *            days>0为增天数，days<0为减少天数
	 * @param hmsStyle
	 *            要转换的时分秒
	 * @return
	 * @return : Date;
	 * @Creation Date : 2017年8月7日 上午9:25:15;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Date format(Date date, String pattern, int days, String hmsStyle) {
		if (date == null) {
			return null;
		}
		return DateUtils.parse(DateUtils.format(DateUtils.addDays(date, days)) + " " + hmsStyle,
				StringUtils.isBlank(pattern) ? DateUtils.DATE_HH_MM_SS : pattern);
	}

	/**
	 * @Description :判断转入转出时间
	 * @Method_Name : judgeTransferInfo;
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结否时间
	 * @return
	 * @return : Boolean;
	 * @Creation Date : 2017年7月16日 下午7:46:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Boolean judgeTransferInfo(String startTime, String endTime) {
		String current = DateUtils.format(new Date(), "HH:mm:ss");
		if (startTime.compareTo(endTime) >= 0) {
			if (current.compareTo(startTime) >= 0) {
				return true;
			}
			if (current.compareTo(startTime) <= 0 && current.compareTo(endTime) <= 0) {
				return true;
			}
		}
		if (startTime.compareTo(endTime) <= 0) {
			if (current.compareTo(startTime) >= 0 && current.compareTo(endTime) <= 0) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(
				QdzDateUtils.format(new Date(), DateUtils.DATE, -1, PropertiesHolder.getProperty("qdz_income_time")));
	}
}
