package com.hongkun.finance.invest.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.model.vo.TransferAutoVo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.service.BidTransferAutoService.java
 * @Class Name    : BidTransferAutoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidTransferAutoService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidTransferAuto 持久化的数据对象
	 * @return				: void
	 */
	void insertBidTransferAuto(BidTransferAuto bidTransferAuto);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidTransferAuto> 批量插入的数据
	 * @return				: void
	 */
	void insertBidTransferAutoBatch(List<BidTransferAuto> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidTransferAuto> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidTransferAutoBatch(List<BidTransferAuto> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidTransferAuto 要更新的数据
	 * @return				: void
	 */
	void updateBidTransferAuto(BidTransferAuto bidTransferAuto);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidTransferAuto 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidTransferAutoBatch(List<BidTransferAuto> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidTransferAuto
	 */
	BidTransferAuto findBidTransferAutoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidTransferAuto 检索条件
	 * @return	List<BidTransferAuto>
	 */
	List<BidTransferAuto> findBidTransferAutoList(BidTransferAuto bidTransferAuto);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidTransferAuto 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidTransferAuto>
	 */
	List<BidTransferAuto> findBidTransferAutoList(BidTransferAuto bidTransferAuto, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidTransferAuto 检索条件
	 * @param pager	分页数据
	 * @return	List<BidTransferAuto>
	 */
	Pager findBidTransferAutoList(BidTransferAuto bidTransferAuto, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidTransferAuto 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidTransferAutoCount(BidTransferAuto bidTransferAuto);
	
	/**
	 *  @Description    : 添加债权转让记录
	 *  @Method_Name    : addCreditorTransfer
	 *  @param oldInvest
	 *  @param newInvest
	 *  @param comnBidInfo
	 *  @param creditorAmount
	 *  @return         : void
	 *  @Creation Date  : 2017年6月17日 下午2:48:22 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	void addCreditorTransfer(BidInvest oldInvest, BidInvest newInvest,BidInfo comnBidInfo, BigDecimal creditorAmount,
			Date transferTime,int adminUserId) ;
	/**
	 *  @Description    :  债权转让（后台匹配）
	 *  @Method_Name    : addTransferAuto
	 *  @param comnBidInfo          散标标的
	 *  @param transferInvestList   待转让投资记录
	 *  @param receiveInvestList    接受投资记录
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月17日 下午2:47:52 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	ResponseEntity<?> addTransferAuto(BidInfo comnBidInfo,List<BidInvest> transferInvestList,
			List<BidInvest> receiveInvestList,Date transferTime,int adminUserId) ;
	/**
	 *  @Description    : 散标二次匹配时，生成优选标之间的转让记录
	 *  @Method_Name    : addTransferRecord
	 *  @param transferBids  待转让的优选标的列表
	 *  @param newGoodBids   待接受的优选标列表
	 *  @param transferDate  转让时间
	 *  @param comnBidInfo   散标标的
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月19日 上午11:51:18 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> addTransferRecord(List<BidInfo> transferBids, List<BidInfo> newGoodBids, Date transferDate,
			BidInfo comnBidInfo);
	/**
	 *  @Description    : 匹配---数据处理
	 *  @Method_Name    : match
	 *  @param bidInvestList         不用债权转让的投资记录集合,散标首次匹配
	 *  @param transferAutoVo        债权转让参数
	 *  @param matchList             匹配记录
	 *  @param updateGoodInvestList  需要更新的优选投资记录
	 *  @param bidInfoDetailList     需要更新的标的列表
	 *  @param adminUserId           操作人id
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月31日 下午2:14:57 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> match(List<BidInvest> bidInvestList,List<TransferAutoVo> transferAutoVo,List<BidMatch> matchList,
			List<BidInvest> updateGoodInvestList, List<BidInfoDetail> bidInfoDetailList,int adminUserId);
	
}
