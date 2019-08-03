package com.hongkun.finance.qdz.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.vo.QdzAutoCreditorVo;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description :钱袋子定时接口
 * @Project : finance-qdz
 * @Program Name : com.hongkun.finance.qdz.facade.QdzTaskJobFacade.java
 * @Author : yanbinghuang@hongkun.com
 */
public interface QdzTaskJobFacade {
	/**
	 * 
	 * @Description : 跑批债券
	 * @Method_Name : creditorMatch
	 * @param date
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月18日 下午5:58:39
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> creditorMatch(Date date);

	/**
	 * 
	 * @Description : 跑批利息
	 * @Method_Name : calculateInterest
	 * @param date
	 * @param shardingItem:分片项
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月18日 下午5:59:00
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> calculateInterest(Date date, int shardingItem);

	/**
	 * 
	 * @Description :批量恢复利息
	 * @Method_Name : calculateInterest
	 * @param qdzInterestDayId:钱袋子利息ID
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月18日 下午5:59:00
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> repairCalcInterest(String qdzInterestDayIds);
	/**
	 * 
	 * @param qdzInterestDay
	 * @param rateRecord
	 * @param recordId
	 * @param day
	 * @return
	 */
	public boolean dealCalcInterest(QdzInterestDay qdzInterestDay, QdzRateRecord rateRecord,Date day); 

	/**
	 * 
	 * @Description : 钱袋子利率记录
	 * @Method_Name : qdzRateRecord
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月20日 下午1:53:29
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> qdzRateRecord();

	/**
	 * @Description : 第三方垫息
	 * @Method_Name : thirdAccountPadBearing
	 * @return : void
	 * @Creation Date : 2017年9月21日 上午10:30:37
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	void thirdAccountPadBearing(Date day);

	/**
	 * @Description : MQ处理转出的债权转让逻辑
	 * @Method_Name : sellAutoCreditorByMQ;
	 * @param qdzAutoCreditorVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月6日 上午11:36:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> sellAutoCreditorByMQ(QdzAutoCreditorVo qdzAutoCreditorVo);
	/**
	 * 
	 *  @Description    : 处理购买债券
	 *  @Method_Name    : dealBuyCreditorMatch;
	 *  @param creditorTransMoney
	 *  @return
	 *  @throws Exception
	 *  @return         : boolean;
	 *  @Creation Date  : 2018年4月19日 下午2:07:31;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	ResponseEntity<?> dealBuyCreditorMatch(QdzAccount qdzAccount,BigDecimal creditorTransMoney,
			BigDecimal thirdInvestAmount,BigDecimal qdzRate,Date dealTime,Map creditorInfoMap) throws Exception;
	
	/**
	 * 
	 *  @Description    : 处理释放债券
	 *  @Method_Name    : dealBuyCreditorMatch;
	 *  @param creditorTransMoney
	 *  @return
	 *  @throws Exception
	 *  @return         : boolean;
	 *  @Creation Date  : 2018年4月19日 下午2:07:31;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	ResponseEntity<?> dealSellCreditorMatch(QdzAutoCreditorVo qdzAutoCreditorVo,BigDecimal userTransMoney,
			BigDecimal qdzRate,Date dealTime,Map creditorInfoMap) throws Exception;
}
