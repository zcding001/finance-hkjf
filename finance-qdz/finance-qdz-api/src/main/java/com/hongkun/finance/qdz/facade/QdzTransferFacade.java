package com.hongkun.finance.qdz.facade;

import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description :钱袋子接口
 * @Project : finance-qdz
 * @Program Name : com.hongkun.finance.qdz.facade.QdzTransferFacade.java
 * @Author : yanbinghuang@hongkun.com
 */
public interface QdzTransferFacade {
	/**
	 * @Description :钱袋子转入接口
	 * @Method_Name : transferIn;
	 * @param regUser
	 *            乾坤袋用户
	 * @param transMoney
	 *            转入金额
	 * @param transferSource
	 *            交易来源 0-PC 1-WAP 2-Android 3-IOS
	 * @param turnOutType
	 *            1:正常转入 2:投资转入
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月14日 下午4:33:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> transferIn(RegUser regUser, BigDecimal transMoney, String transferSource, Integer turnOutType);

	/**
	 * @Description : 钱袋子转出接口
	 * @Method_Name : transferOut;
	 * @param regUser乾坤袋用户
	 * @param transMoney转出金额
	 * @param transferSource交易来源
	 *            10-PC 11-IOS 12-Android 13-WAP
	 * @param turnOutType
	 *            1:正常转出 2:投资转出
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月17日 下午2:26:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> transferOut(RegUser regUser, BigDecimal transMoney, String transferSource, Integer turnOutType);

	/**
	 * 
	 * @Description : 获取钱袋子基本信息
	 * @Method_Name : findQdzInfo
	 * @param regUserId
	 * @throws Exception
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月8日 上午10:04:05
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> findQdzInfo(Integer regUserId);

	/**
	 * 
	 * @Description : 获取我的钱袋子信息
	 * @Method_Name : findMyQdzInfo
	 * @param regUser
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月8日 下午4:53:34
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> findMyQdzInfo(RegUser regUser);

	/**
	 * 
	 * @Description : 计算钱袋子转出手续费
	 * @Method_Name : findMyQdzInfo
	 * @param regUser
	 * @param money
	 * @param type
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月15日 下午3:24:11
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> calculateTransferOutFee(RegUser regUser, BigDecimal money, int type);

	/**
	 * 
	 * @Description : 转出到银行卡
	 * @Method_Name : transferOutToBank
	 * @param source
	 * @param money
	 * @param sign
	 * @param signType
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年3月16日 上午10:26:04
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	Map<String, Object> transferOutToBank(RegUser regUser, int source, BigDecimal money, String sign, String signType);

	/**
	 * @Description : 转出 自动债权转让MQ
	 * @Method_Name : autoCreditorMQ;
	 * @param map
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月16日 下午4:05:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> autoCreditorMQ(Map<String, Object> map);
	/**
	 *  @Description    : 获取用户可用余额，及钱袋子可转入金额
	 *  @Method_Name    : getQdzUseableAmount;
	 *  @param regUser
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年10月24日 上午9:50:29;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> getQdzUseableAmount(RegUser regUser);
	
}
