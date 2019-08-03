package com.hongkun.finance.invest.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.CreditorTransferDetailVo;
import com.hongkun.finance.invest.model.vo.CreditorTransferVo;
import com.hongkun.finance.invest.model.vo.TransferManualAppPreVo;
import com.hongkun.finance.invest.model.vo.TransferManualDetailAppVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.BidTransferManualDao.java
 * @Class Name    : BidTransferManualDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidTransferManualDao extends MyBatisBaseDao<BidTransferManual, java.lang.Long> {
	/**
	 *  @Description    : 查询我的债权列表
	 *  @Method_Name    : findMyCreditor
	 *  @param contidion
	 *  @param pager
	 *  @return
	 *  @return         : List<CreditorTransferVo>
	 *  @Creation Date  : 2017年6月13日 下午6:04:40 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	Pager findMyCreditor(BidTransferManual contidion, Pager pager);
	/**
	 *  @Description    : 查询转让详情（转让前）
	 *  @Method_Name    : findBidTransferDetailByInvestId
	 *  @param investId 投资记录id
	 *  @return
	 *  @return         : TransferManualAppPreVo
	 *  @Creation Date  : 2017年6月14日 下午1:32:40 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	TransferManualAppPreVo findBidTransferDetailByInvestId(Integer investId);
	/**
	 *  @Description    : 查询转让详情（转让后）
	 *  @Method_Name    : findBidTransferDetailByTransferId
	 *  @param transferId
	 *  @return
	 *  @return         : TransferManualDetailAppVo
	 *  @Creation Date  : 2017年6月14日 下午2:17:48 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	TransferManualDetailAppVo findBidTransferDetailByTransferId(Integer transferId);
	BigDecimal findSumTransferableMoney(Integer investId);
	/**
	 * 
	 *  @Description    : 查询我的债权（转让中/已转让）
	 *  @Method_Name    : myCreditorForTransfer
	 *  @param tranferContidion
	 *  @param pager
	 *  @return
	 *  @return         : List<CreditorTransferVo>
	 *  @Creation Date  : 2017年12月1日 上午10:11:20 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	Pager myCreditorForTransfer(BidTransferManual tranferContidion, Pager pager);
	/**
	 * 
	 *  @Description    : 查询转让投资记录
	 *  @Method_Name    : findTransferRecordList
	 *  @param investId
	 *  @return
	 *  @return         : List<BidInvest>
	 *  @Creation Date  : 2017年12月27日 下午4:37:24 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	List<BidInvest> findTransferRecordList(Integer investId);
	/**
	 *  @Description    : 通过newInvestId 获取转让记录
	 *  @Method_Name    : findBidTransferByNewInvestId
	 *  @param newInvestId
	 *  @return
	 *  @return         : BidTransferManual
	 *  @Creation Date  : 2017年12月29日 下午2:23:41 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	BidTransferManual findBidTransferByNewInvestId(Integer newInvestId);

	/**
	*  @Description    ：查询某个用户可以转让的债权列表
	*  @Method_Name    ：findInvestForTransfer
	*  @param investUserId
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @Creation Date  ：2018/6/20
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    Pager findInvestForTransfer(Integer investUserId, Pager pager);

	/**
	 * 查询某个用户转让中/已转让债权列表
	 * @param contidion
	 * @param pager
	 * @return
	 */
	Pager findInvestTransfering(BidTransferManual contidion, Pager pager);
	/**
	*  @Description    ：修改状态为已失效
	*  @Method_Name    ：updateTimeOutStateByIds
	*  @param transferIds
	*  @return void
	*  @Creation Date  ：2018/6/26
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    void updateTimeOutStateByIds(Set<Integer> transferIds);
}
