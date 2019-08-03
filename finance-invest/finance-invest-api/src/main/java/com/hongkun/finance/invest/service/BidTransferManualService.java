package com.hongkun.finance.invest.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hongkun.finance.invest.model.vo.TransferManualAppPreVo;
import com.hongkun.finance.invest.model.vo.TransferManualDetailAppVo;
import org.mengyun.tcctransaction.api.Compensable;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.CreditorTransferDetailVo;
import com.hongkun.finance.invest.model.vo.CreditorTransferVo;
import com.hongkun.finance.user.model.RegUserDetail;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.invest.service.BidTransferManualService.java
 * @Class Name : BidTransferManualService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface BidTransferManualService {

	/**
	 * @Described : 单条插入
	 * @param bidTransferManual
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertBidTransferManual(BidTransferManual bidTransferManual);

	/**
	 * @Described : 批量插入
	 * @param List<BidTransferManual>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertBidTransferManualBatch(List<BidTransferManual> list);

	/**
	 * @Described : 批量插入
	 * @param List<BidTransferManual>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertBidTransferManualBatch(List<BidTransferManual> list, int count);

	/**
	 * @Described : 更新数据
	 * @param bidTransferManual
	 *            要更新的数据
	 * @return : void
	 */
	@Compensable
	int updateBidTransferManual(BidTransferManual bidTransferManual);

	/**
	 * @Described : 批量更新数据
	 * @param bidTransferManual
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateBidTransferManualBatch(List<BidTransferManual> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return BidTransferManual
	 */
	BidTransferManual findBidTransferManualById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param bidTransferManual
	 *            检索条件
	 * @return List<BidTransferManual>
	 */
	List<BidTransferManual> findBidTransferManualList(BidTransferManual bidTransferManual);

	/**
	 * @Described : 条件检索数据
	 * @param bidTransferManual
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<BidTransferManual>
	 */
	List<BidTransferManual> findBidTransferManualList(BidTransferManual bidTransferManual, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param bidTransferManual
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<BidTransferManual>
	 */
	Pager findBidTransferManualList(BidTransferManual bidTransferManual, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param bidTransferManual
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findBidTransferManualCount(BidTransferManual bidTransferManual);

	/**
	 * @Description : 购买债权： 1、添加购买人投资记录 2、更新债权转让关系状态 3、添加对应散标的投资记录
	 *              4、添加散标投资记录之间的转让关系
	 * @Method_Name : buyCreditor
	 * @param transferId
	 *            手动转让记录id
	 * @param investUserId
	 *            购买人id
	 * @param investUserName
	 *            购买人name
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月12日 下午7:06:00
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	@Compensable
	ResponseEntity<?> buyCreditor(BidTransferManual bidTransferManual, int investUserId, String investUserName, BigDecimal userRate);

	/**
	 * @Description : 查询【我的债权】
	 * @Method_Name : myCreditor
	 * @param contidion
	 * @param pager
	 * @return
	 * @return : List<CreditorTransferVo>
	 * @Creation Date : 2017年6月13日 下午5:59:26
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	Pager myCreditor(BidTransferManual contidion, Pager pager);

	/**
	 * @Description : 根据投资记录id 查询手动转让详情（发布转让之前详情）
	 * @Method_Name : findBidTransferDetailByInvestId
	 * @param investId
	 * @return
	 * @return : TransferManualAppPreVo
	 * @Creation Date : 2017年6月14日 上午11:59:19
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	TransferManualAppPreVo findBidTransferDetailByInvestId(Integer investId);

	/**
	 * @Description : 根据转让记录id 查询手动转让详情（发布转让之后详情）
	 * @Method_Name : findBidTransferDetailByTransferId
	 * @param transferId
	 * @return
	 * @return : CreditorTransferDetailVo
	 * @Creation Date : 2017年6月14日 下午2:16:22
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	TransferManualDetailAppVo findBidTransferDetailByTransferId(Integer transferId);

	/**
	 * @Description : 根据唯一字段 查询对应结果
	 * @Method_Name : findBidTransferManualByUnique
	 * @param id
	 * @param field
	 *            字段名称
	 * @return
	 * @return : BidTransferManual
	 * @Creation Date : 2017年6月14日 下午3:42:54
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	BidTransferManual findBidTransferManualByUnique(Integer id, String field);

	/**
	 * @Description : 已转让金额+转让中金额
	 * @Method_Name : findSumTransferedMoney
	 * @param investId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月26日 下午3:39:54
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	BigDecimal findSumTransferedMoney(Integer investId);

	/**
	 * 
	 * @Description :处理债券转让关系数据
	 * @Method_Name : updateTransferManualRecords
	 * @param insertInvest
	 * @param updateInvests
	 * @param tranferAuto
	 * @return:返回插入投资记录Id
	 * @return : int
	 * @Creation Date : 2017年7月26日 上午10:43:52
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> updateTransferManualRecords(BidInvest insertInvest, List<BidInvest> updateInvests,
			BidTransferAuto tranferAuto) throws Exception;

	/**
	 * 
	 * @Description :回滚投资记录及债权转让关系
	 * @Method_Name : rollBackManualRecords
	 * @param thirdBiddInvest
	 *            第三方投资记录
	 * @param qdzBidInvest
	 *            投资人的投资记录
	 * @param tranferAuto
	 *            债权转入关系
	 * @param creditorFlag
	 *            1-购买债权 2-释放债权
	 * @return
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月28日 下午1:45:26
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	void rollBackManualRecords(BidInvest thirdBiddInvest, BidInvest qdzBidInvest, BidTransferAuto tranferAuto,
			int creditorFlag) throws Exception;

	Pager myCreditorForTransfer(BidTransferManual tranferContidion, Pager pager);
	/**
	 * 
	 *  @Description    : 转让--投资记录
	 *  @Method_Name    : findTransferRecordList
	 *  @param investId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月27日 下午4:33:33 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findTransferRecordList(Integer investId);
	/**
	 * 
	 *  @Description    : 查询转让记录
	 *  @Method_Name    : findBidTransferByNewInvestId
	 *  @param newInvestId
	 *  @return
	 *  @return         : BidTransferManual
	 *  @Creation Date  : 2017年12月29日 下午2:22:14 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidTransferManual findBidTransferByNewInvestId(Integer newInvestId);
	/**
	 * 
	 *  @Description    : 发布转让，存储转让信息&修改投资状态
	 *  @Method_Name    : saveBidTransferManual
	 *  @param saveManual
	 *  @return         : void
	 *  @Creation Date  : 2018年1月31日 下午1:58:29 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	void saveBidTransferManual(BidTransferManual saveManual);
	/**
	 * 
	 * @Description : 处理债券转让关系
	 * @Method_Name : buyCreditorMatchTransferManualRecords
	 * @param qdzBidInvest
	 * @param thirdBiddInvest
	 * @param thirdInvestAmount
	 * @param detail
	 * @param bidInfo
	 * @param regUserId
	 * @param userTransMoney
	 * @param qdzRate
	 * @param repairTime
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月26日 上午11:45:43
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Compensable
	ResponseEntity<?> dealCreditorMatchRecords(BidInvest qdzBidInvest, BidInvest thirdBidInvest,
			BidTransferAuto tranferAuto,BidInfo bidInfo,BigDecimal userTransMoney, BigDecimal qdzRate, Date repairTime,int creditorFlag)throws Exception;
	/**
	 * 
	 * @param qdzNewBidInvest
	 * @param thirdNewBidInvest
	 * @param tranferAuto
	 * @param regUserDetail
	 * @param bidInfo
	 * @param qdzRate
	 * @param repairTime
	 * @param userTransMoney
	 * @return
	 * @throws Exception
	 */
	@Compensable
	ResponseEntity<?> dealSellCreditorMatchRecords(BidInvest qdzNewBidInvest, BidInvest thirdNewBidInvest,
			BidTransferAuto tranferAuto,RegUserDetail regUserDetail, BidInfo bidInfo,
			BigDecimal userTransMoney, BigDecimal qdzRate, Date repairTime,int creditorFlag) throws Exception;
	/**
	*  @Description    ：查询转让中债权列表
	*  @Method_Name    ：findTransferingCreditorList
	*  @param pager
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date  ：2018/6/21
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	Pager findInvestTransfering( BidTransferManual contidion,Pager pager);
	/**
	*  @Description    ：处理超时债权转让
	*  @Method_Name    ：dealTransferTimeOut
	*  @param transferIds
	*  @param investIds
	*  @return void
	*  @Creation Date  ：2018/6/26
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    void dealTransferTimeOut(Set<Integer> transferIds, List<BidInvest> investIds);
}
