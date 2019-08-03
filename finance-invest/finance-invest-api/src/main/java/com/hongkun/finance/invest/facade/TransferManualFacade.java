package com.hongkun.finance.invest.facade;

import java.math.BigDecimal;

import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 债权转让业务处理
 * @Project       : finance-invest-api
 * @Program Name  : com.hongkun.finance.invest.facade.TransferManualFacade.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface TransferManualFacade {
	
	/**
	 *  @Description    : 查询我的债权列表
	 *  @Method_Name    : myCreditorList
	 *  @param state    : 1：待转让2：已转让 4：转让中
	 *  @param userFlag : 要查询的功能1:转让人（我的债权） 2：购买人（我购买的债权）
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月7日 下午3:40:49 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
//	ResponseEntity<?> myCreditorList(RegUser regUser,Integer state,Integer userFlag,Pager pager);

	/**
	*  @Description    ：查询我的债权列表
	*  @Method_Name    ：myCreditorList
	*  @param regUser
	*  @param state    1：待转让2：已转让 4：转让中
	*  @param pager
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/6/20
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	ResponseEntity<?> myCreditorList(RegUser regUser,Integer state,Pager pager);

	/**
	 *  @Description    : 查询债权转让详情
	 *  @Method_Name    : showTransferManualDetailByInvestId
	 *  @param investId  投资记录id
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月7日 下午1:46:22 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> showTransferManualDetailByInvestId(Integer investId);
	/**
	 *  @Description    : 查询债权转让详情
	 *  @Method_Name    : showTransferManualDetailByTransferId
	 *  @param transferId 债权转让id
	 *  @param investId 
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月7日 下午1:46:48 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> showTransferManualDetailByTransferId(Integer transferId,Integer regUserId);
	/**
	 *  @Description    : 发布转让
	 *  @Method_Name    : saveTransferManual
	 *  @param bidTransferManual
	 *  @param userId    转让人id
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2017年7月7日 下午2:37:23 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<String> saveTransferManual(int userId,BidTransferManual bidTransferManual);
	/**
	 *  @Description    : 购买债权
	 *  @Method_Name    : buyCreditor
	 *  @param transferId
	 *  @return
	 *  @return         : ResponseEntity<String>
	 *  @Creation Date  : 2017年7月7日 下午2:45:04 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<String> buyCreditor(int regUserId,String realName,int transferId);
	/**
	 * 
	 *  @Description    : 计算转让价格和转让后利率
	 *  @Method_Name    : calTransferMoneyAndInterestRate
	 *  @param investId
	 *  @param transferAmount
	 *  @param transferRate
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月7日 下午5:46:34 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> calTransferMoneyAndInterestRate(Integer investId, BigDecimal transferAmount,
			BigDecimal transferRate);
	/**
	 * 
	 *  @Description    : 债权转让投资记录列表
	 *  @Method_Name    : investListForCreditor
	 *  @param bidTransferManual
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月26日 下午5:08:26 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> investListForCreditor(BidTransferManual bidTransferManual, Pager pager);
	
	/**
	 *  @Description    : 购买债权详情页
	 *  @Method_Name    : investListForCreditor
	 *  @param bidTransferManual
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月26日 下午5:08:26 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> toBuyCreditor(Integer transferId, Integer regUserId);

	/**
	*  @Description    ：债权转让-产品列表
	*  @Method_Name    ：creditorTransferList
	*  @param pager
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/6/21
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	ResponseEntity<?> creditorTransferList(Pager pager);
	/**
	*  @Description    ：转让超时处理
	*  @Method_Name    ：dealTransferTimeOut
	*
	*  @return void
	*  @Creation Date  ：2018/6/26
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    void dealTransferTimeOut();
}
