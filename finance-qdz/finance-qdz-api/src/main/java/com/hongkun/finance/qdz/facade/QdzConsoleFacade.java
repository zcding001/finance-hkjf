package com.hongkun.finance.qdz.facade;

import com.hongkun.finance.qdz.vo.QdzInterestInfoVo;
import com.hongkun.finance.qdz.vo.QdzTransferRecordVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.Date;

public interface QdzConsoleFacade {
	/**
	 * @Description : 钱袋子利息对账查询
	 * @Method_Name : qdzInterestBalance;
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月18日 下午4:31:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> qdzInterestBalance(Date startTime, Date endTime, Pager pager);

	/**
	 * @Description : 根据日期和用户类型查询某一天的利息明细
	 * @Method_Name : qdzInterestDetail;
	 * @param userFlag：0-平台;1-第三方账户
	 * @param day：格式yyyy-MM-dd
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 上午11:02:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> qdzInterestDetail(Integer userFlag, Date day, Pager pager);

	/**
	 * @Description : 当前债权查询处理
	 * @Method_Name : findCurrentCreditor;
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 下午1:59:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findCurrentCreditor();

	/**
	 * @Description : 根据标的名称，查询当前对账债权信息
	 * @Method_Name : searchCreditorBalanceInfo;
	 * @param bidName
	 *            标的名称
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月19日 下午5:06:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findCreditorBalanceInfo(String bidName,Pager pager);

	/**
	 * @Description :查询钱袋子账单流水
	 * @Method_Name : findQdzBillWater;
	 * @param qdzTransferRecordVo
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月20日 上午9:31:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findQdzBillWater(QdzTransferRecordVo qdzTransferRecordVo, Pager pager);

	/**
	 * @Description : 查询活期产品信息
	 * @Method_Name : findHqProductInfo;
	 * @param bidName
	 *            标的名称
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月20日 上午11:33:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findHqProductInfo(String bidName, Pager pager);

	/**
	 * 
	 * @Description : 债券通知
	 * @Method_Name : creditorNotice
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月20日 下午1:50:32
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> creditorNotice();

	/**
	 * @Description : 每日生成利息记录查询
	 * @Method_Name : findQdzInterestInfo
	 * @Date : 2017/9/25 17:19
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param qdzInterestInfoVo
	 * @param pager
	 * @return
	 */
	ResponseEntity<?> findQdzInterestInfo(QdzInterestInfoVo qdzInterestInfoVo, Pager pager);
}
