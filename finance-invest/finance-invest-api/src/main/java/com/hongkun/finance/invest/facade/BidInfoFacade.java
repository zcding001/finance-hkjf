package com.hongkun.finance.invest.facade;

import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.vo.AppIndexBidInfoVO;
import com.hongkun.finance.invest.model.vo.BidInfoSimpleVo;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.pager.Pager;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.mengyun.tcctransaction.api.Compensable;

import java.util.Map;

/**
 * @Description :标的信息相关接口
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.facade.BidInfoFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

public interface BidInfoFacade {

	/**
	 * @Description : 查询标的详情（info、detail、product）
	 * @Method_Name : findBidInfoDetailVoAndInvests
	 * @param bidInfoId
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月5日 下午2:27:56
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public ResponseEntity<?> findBidInfoDetailVo(int bidInfoId);

	/**
	 * @Description : 条件检索标的信息
	 * @Method_Name : findBidInfoDetail
	 * @param pager
	 * @param vo
	 * @return : Pager
	 * @Creation Date : 2017年9月5日 下午1:42:06
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findBidInfoDetailVoList(Pager pager, BidInfoVO vo);

	/**
	 * 
	 *  @Description    : 获取标的列表，添加是否存在匹配记录字段
	 *  @Method_Name    : findBidVoListForMatch
	 *  @param pager
	 *  @param vo
	 *  @return
	 *  @return         : Pager
	 *  @Creation Date  : 2017年9月29日 下午3:37:45 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	 Pager findBidVoListForMatch(Pager pager, BidInfoVO vo);


	/**
	 *  @Description    : 改变标的状态
	 *  @Method_Name    : updateBidState
	 * @param id        :标的ID
	 * @param chanState : 需要被改变的状
	 * @param reason    :原因
	 *  @return           ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	@Compensable
	ResponseEntity<?> updateBidState(Integer id, int chanState, String reason,Integer regUserId);
	/**
	 * 
	 *  @Description    : 修改债权转让信息
	 *  @Method_Name    : updateCreditorProperty
	 *  @param bidInfoDetail
	 *  @param regUser
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月14日 下午3:09:46 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> updateCreditorProperty(BidInfoDetail bidInfoDetail,RegUser regUser);
	
	
	/**
	 *  @Description    : 查询投资的标的详情
	 *  @Method_Name    : findBidForInvest
	 *  @param regUser	: 投资用户
	 *  @param id		: 标的ID
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月11日 下午4:31:31 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findBidForInvest(RegUser regUser, Integer id);
	/**
	 * 
	 *  @Description    : 查询借款人
	 *  @Method_Name    : findBidInfoByBorrowCode
	 *  @param  bidInfoSimpleVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月23日 上午11:15:55 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	public ResponseEntity<?> findBidInfoByBorrowCode(BidInfoSimpleVo bidInfoSimpleVo, Pager pager);
	
	/**
	 *  @Description    : 首页&我要投资项目列表查询，做投资黑白名单的校验
	 *  @Method_Name    : findBidVoListForInvest
	 *  @param pager
	 *  @param vo
	 *  @param regUser	: 当前登录用户
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月26日 上午10:28:18 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findBidVoListForInvest(Pager pager, BidInfoVO vo, RegUser regUser);

	/***
	 * 查询首页的标的信息
	 * @param currentUser
     * @return
	 */
    ResponseEntity<?> findIndexBidInfo(RegUser currentUser);

	/**
	 * 查询App端首页的标的信息
	 * @param loginUser
	 * @return
	 */
	Map<String,Object> findAppIndexBidInfo(RegUser loginUser);

	/**
	 * 查询App端标的的详细信息
	 * @param bidId
	 * @param fromPlace  从什么地方查询标的详情， 1-首页 2-投资记录
	 *                    如果是从首页不显示保障合同，从投资记录正常显示
	 *                   默认为从投资记录，不隐藏
	 * @return
	 */
    Map<String, Object> findAppBidDetail(Integer bidId, final Integer regUserId, Integer fromPlace);

	/**
	 * 查询App端的产品过滤标的信息
	 * @return
	 * @param termValue
	 * @param bidType
	 */
	Map<String,Object> filterAppProductBidInfo(Integer termValue, Integer bidType);

	/**
	 * 新建一个标的
	 * @param bidInfoVO
	 * @param currentUserId
	 * @return
	 */
    ResponseEntity saveBid(BidInfoVO bidInfoVO, Integer currentUserId);

	/**
	 * 更新标的
	 * @param bidInfoVO
	 * @param currentUserId
	 * @return
	 */
	ResponseEntity updateBidInfoAndDetail(BidInfoVO bidInfoVO, Integer currentUserId);

	/***
	*  @Description    ：找到鸿坤金服plus的接口
	*  @Method_Name    ：findPlusIndexBidsInfo
	*  @param loginUser
	*  @return com.hongkun.finance.invest.model.vo.AppIndexBidInfoVO
	*  @Creation Date  ：2018/5/2
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
	AppIndexBidInfoVO findPlusIndexBidsInfo(RegUser loginUser);

	/**
	*  @Description    ：为鸿坤金服查询产品页标的列表
	*  @Method_Name    ：filterProductBidInfoForPlus
	*  @param termValue
	*  @param bidType
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/5/7
	*  @Author         ：zhongpingtang@hongkun.com.cn
	*/
    Map<String,Object> filterProductBidInfoForPlus(Integer termValue, Integer bidType);
	/**
	 *  @Description    ：更改标的信息（标的审核）
	 *  @Method_Name    ：auditBid
	 *  @param bidInfo
	 *  @param chanState
	 *  @param reason
	 * @return void
	 *  @Creation Date  ：2018/5/29
	 *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	ResponseEntity auditBid(Integer bidInfoId, int chanState, String reason, Integer userId);

	/**
	 *  @Description    ：首页产品搜索
	 *  @Method_Name    ：productSearch
	 *  @param loginUser
	 *  @param type
	 *  @param keyWord
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年08月16日 09:44
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    ResponseEntity<?> productSearch(RegUser loginUser, Integer type, String keyWord,Integer fixFlag);

	/**
	 *  @Description    ：APP定期投资产品列表页
	 *  @Method_Name    ：findBidInfoList
	 *  @param loginUser
	 *  @param overseaFlag
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018年09月18日 15:56
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Map<String,Object> findBidInfoList(RegUser loginUser,int overseaFlag);
	/**
	 *  @Description    : 查询钱袋子页面，定期标的信息
	 *  @Method_Name    : findQdzPageBidInfo;
	 *  @param loginUser
	 *  @return
	 *  @return         : Map<String,Object>;
	 *  @Creation Date  : 2018年10月23日 上午10:21:57;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    Map<String,Object> findQdzPageBidInfo(RegUser loginUser);
}
