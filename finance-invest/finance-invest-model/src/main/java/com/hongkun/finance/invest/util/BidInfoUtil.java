package com.hongkun.finance.invest.util;

import com.yirun.framework.core.utils.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static com.hongkun.finance.invest.constants.InvestConstants.*;

/**
 * @Description   : 标的工具类
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.util.BidInfoUtil.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInfoUtil {
	
	/**
	 *  @Description    : 查询优选/散标所有的产品类型
	 *  @Method_Name    : getProdutTypeList
	 *  @param bidType  1-优选 2-散标
	 *  @return
	 *  @return         : List<Integer>
	 *  @Creation Date  : 2017年7月20日 上午11:25:56 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static List<Integer> getProdutTypeList(int bidType){
		List<Integer> resultList = null;
		if(bidType== MATCH_BID_TYPE_COMMON){
			resultList =  Arrays.asList(0);
		}
		if(bidType==MATCH_BID_TYPE_GOOD){
			resultList =  Arrays.asList(2,3,4,6,9);
		}
		return resultList;
	}
	/**
	 *  @Description    : 根据产品类型 判断此产品是优选还是散标
	 *  @Method_Name    : matchBidTypeByProdutType
	 *  @param productType
	 *  @return
	 *  @return         : Integer 1-优选 2-散标
	 *  @Creation Date  : 2017年7月20日 上午11:39:25 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static Integer matchBidTypeByProdutType(int productType){
		Integer result = null;
		List<Integer> goodList = getProdutTypeList(MATCH_BID_TYPE_GOOD);
		List<Integer> commonList = getProdutTypeList(MATCH_BID_TYPE_COMMON);
		if(goodList.contains(productType)){
			result =  MATCH_BID_TYPE_GOOD;
		}
		if(commonList.contains(productType)){
			result =  MATCH_BID_TYPE_COMMON;
		}
		return result;
	}
	/**
	 *  @Description    : 判断标的是否是散标
	 *  @Method_Name    : isCommonBid
	 *  @param productType
	 *  @return
	 *  @return         : boolean
	 *  @Creation Date  : 2017年7月20日 下午4:27:57 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static boolean isCommonBid(int productType){
		Integer bidType = matchBidTypeByProdutType(productType);
		if(bidType!=null&&MATCH_BID_TYPE_COMMON==bidType){
			return true;
		}
		return false;
	}
	/**
	 *  @Description    : 判断标的是否是优选
	 *  @Method_Name    : isGoodBid
	 *  @param productType
	 *  @return
	 *  @return         : boolean
	 *  @Creation Date  : 2017年7月20日 下午4:28:15 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static boolean isGoodBid(int productType){
		if(MATCH_BID_TYPE_GOOD==matchBidTypeByProdutType(productType)){
			return true;
		}
		return false;
	}
	
	/**
	*  根据放款时间计算标的完成时间
	*  @Method_Name             ：getFinishTime
	*  @param lendingTime
	*  @param termUnit
	*  @param termValue
	*  @return java.util.Date
	*  @Creation Date           ：2018/5/8
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	public static Date getFinishTime(Date lendingTime, Integer termValue, Integer termUnit){
	    if(lendingTime != null && lendingTime.getTime() > 0){
	        if(termUnit == BID_TERM_UNIT_YEAR){
	            return DateUtils.addMonth(lendingTime, termValue * 12);
            }else if(termUnit == BID_TERM_UNIT_MONTH){
                return DateUtils.addMonth(lendingTime, termValue);
            }else{
	            return DateUtils.addDays(lendingTime, termValue);
            }
        }
	    return null;
    }

    /**
    *  获取标的期数
    *  @Method_Name             ：getPeriods
    *  @param termValue
    *  @param termUnit
    *  @return java.lang.Integer
    *  @Creation Date           ：2018/6/12
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    public static Integer getPeriods(Integer termValue, Integer termUnit){
        if(termUnit == BID_TERM_UNIT_YEAR){
            return termValue * 12;
        }else if(termUnit == BID_TERM_UNIT_MONTH){
            return termValue;
        }else{
            return 1;
        }
    }

	/**
	 *  @Description    ：获取APP展示类型
	 *  @Method_Name    ：getAppShowType
	 *  @param type
	 *  @param productType
	 *  @return java.lang.Integer
	 *  @Creation Date  ：2018年09月28日 09:49
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    public static Integer getAppShowType(Integer type,Integer productType){
		if(type.equals(BID_TYPE_HOT)){
			return APP_SHOW_TYPE_HOT;
		}else if(type.equals(BID_TYPE_RECOMMEND)){
			return APP_SHOW_TYPE_RECOMMEND;
		}else if(productType.equals(BID_PRODUCT_PREFERRED)){
			return APP_SHOW_TYPE_PREFERRED;
		}else if(productType.equals(BID_PRODUCT_WINMONTH)){
			return APP_SHOW_TYPE_WINMONTH;
		}else if(productType.equals(BID_PRODUCT_WINSEASON)){
			return APP_SHOW_TYPE_WINSEASON;
		}else if(productType.equals(BID_PRODUCT_WINYEAR)){
			return APP_SHOW_TYPE_WINYEAR;
		}else if(productType.equals(BID_PRODUCT_EXPERIENCE)) {
			return APP_SHOW_TYPE_EXPERIENCE;
		}else{
			return APP_SHOW_TYPE_COMMNE;
		}
	}



}
