package com.hongkun.finance.invest.facade;

import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BidInvestFacade {
    
    /**
    *  预投资，查询用户余额，钱袋子余额、卡卷福利
    *  @param regUserId
    *  @param bidId
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @date                    ：2018/11/12
    *  @author                  ：zc.ding@foxmail.com
    */
    Map<String, Object> preInvest(final Integer regUserId, Integer bidId);

	/**
	 *  @Description    : 投资操作
	 *  @Method_Name    : invest
	 *  @param regUser 投资人
	 *  @param investRedPacketId 投资红包id
	 *  @param investRaiseInterestId 加息券id
	 *  @param money 投资金额
	 *  @param bidId 标的信息
	 *  @param investWay 投资类型    1101:投资  1102:体验金投资 1103:钱袋子投资  1104:充值投资  1105:匹配投资
	 *  @param investType 是否为自动投资
	 *  @param platformSourceEnums 平台
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月18日 上午9:35:06 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public ResponseEntity<?> invest(RegUser regUser, int investRedPacketId, int investRaiseInterestId, BigDecimal money,
			int bidId, int investWay, Integer investType, PlatformSourceEnums platformSourceEnums);

	/**
	 *  @Description    : 标地匹配
	 *  @Method_Name    : match
	 *  @param oneBidId      匹配标的id
	 *  @param moreBidIdList    待匹配标的id集合
	 *  @param matchType     标地类型：1-优选标，优选匹配散标、2-散标，散标匹配优选
	 *  @param userId        当前操作用户id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月21日 下午16:56:30
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity<?> match(Integer oneBidId, List<Integer> moreBidIdList, Integer matchType, Integer userId);

	/**
	*  app端条件检索投资记录
	*  @param vo
	*  @return com.yirun.framework.core.utils.pager.Pager
	*  @date                    ：2018/11/2
	*  @author                  ：zc.ding@foxmail.com
	*/
	List<BidInvestDetailVO> findBidInvestListForApp(BidInvestDetailVO vo);
	
	
	/**
	 * 
	 *  @Description    : 查询被推荐人列表&投资情况
	 *  @Method_Name    : findRecommendListForInvest
	 *  @param pager
	 *  @param regUserId  推荐人
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2018年1月11日 下午6:24:15 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	Pager findRecommendListForInvest(Pager pager,Integer regUserId);
	
	/**
	 *  @Description    : 自动投资
	 *  @Method_Name    : autoInvest
	 *  @Creation Date  : 2018年1月24日 下午3:15:51 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public void autoInvest();
	/**
	 * 
	 *  @Description    : 查詢用戶好友投資記錄（发放推荐奖励的投资记录+活期）
	 *  @Method_Name    : findBidInvestListForApp
	 *  @param regUserId
	 *  @param friendUserId
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月16日 下午3:47:20 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	public ResponseEntity<?> findBidInvestListForApp(Integer regUserId, Integer friendUserId, Pager pager);
	/**
	 * 
	 *  @Description    : 查询好友投资详情（只对销售）
	 *  @Method_Name    : friendInvestListForSales
	 *  @param regUserId 销售id
	 *  @param friendUserId 好友id
	 *  @param pager 
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月21日 下午4:17:34 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	public ResponseEntity<?> friendInvestListForSales(Integer regUserId, Integer state,Integer friendUserId, Pager pager);


	/**
	*  @Description    ：验证用户是否vip用户
	*  @Method_Name    ：validVipStatus
	*  @param regUser
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/6/29
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
    @SuppressWarnings("unchecked")
	public ResponseEntity<?> validVipStatus(RegUser regUser);
	/**
	*  @Description    ：为某笔投资记录设置债权（允许转让/不允许转让）
	*  @Method_Name    ：updateTransState
	*  @param bidInvestId
	*  @param transState
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/7/5
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    ResponseEntity<?> updateTransState(Integer bidInvestId, Integer transState);
    
    /**
    *  验证用户投资资格
    *  @Method_Name             ：validInvestQualification
    *  @param regUserId
    *  @return ResponseEntity<?>
    *  @Creation Date           ：2018/8/1
    *  @Author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> validInvestQualification(Integer regUserId);

	/**
	 *  @Description    ：验证用户是否为海外投资用户
	 *  @Method_Name    ：validOverseaInvestor
	 *  @param regUserId
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年10月09日 17:26
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    ResponseEntity<?> validOverseaInvestor(Integer regUserId);
	/**
	*  @Description    ：查询好友可用余额
	*  @Method_Name    ：findFriendsUseAbleMoney
	*  @param regUserId
	*  @param friendUserId
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/10/16
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
    ResponseEntity<?> findFriendsUseAbleMoney(Integer regUserId, Integer friendUserId);

    Integer validQdzEnable(Integer regUserId);

	/**
	 * @Description: 查询当前用户是否有投资记录
	 * @param regUserId
	 * @return: boolean
	 * @Author: hanghe@hongkunjinfu.com
	 * @Date: 2018/11/26 10:39
	 */
	boolean getUserSelfHasInvest(Integer regUserId);
}
