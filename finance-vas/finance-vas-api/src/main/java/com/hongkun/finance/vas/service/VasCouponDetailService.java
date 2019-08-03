package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mengyun.tcctransaction.api.Compensable;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.service.VasCouponDetailService.java
 * @Class Name : VasCouponDetailService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasCouponDetailService {

	/**
	 * @param vasCouponDetail
	 *            持久化的数据对象
	 * @Described : 单条插入
	 * @return : void
	 */
	int insertVasCouponDetail(VasCouponDetail vasCouponDetail);

	/**
	 * @param vasCouponDetail
	 *            要更新的数据
	 * @Described : 更新数据
	 * @return : void
	 */
	@Compensable
	int updateVasCouponDetail(VasCouponDetail vasCouponDetail);

	/**
	 * @param count
	 *            多少条数提交一次
	 * @Described : 批量更新数据
	 * @return : void
	 */
	@Compensable
	void updateVasCouponDetailBatch(List<VasCouponDetail> list);

	/**
	 * @param id
	 *            id值
	 * @Described : 通过id查询数据
	 * @return VasCouponDetail
	 */
	VasCouponDetail findVasCouponDetailById(int id);

	/**
	 * @param vasCouponDetail
	 *            检索条件
	 * @Described : 条件检索数据
	 * @return List<VasCouponDetail>
	 */
	List<VasCouponDetail> findVasCouponDetailList(VasCouponDetail vasCouponDetail);

	/**
	 * @param vasCouponDetail
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @Described : 条件检索数据
	 * @return List<VasCouponDetail>
	 */
	List<VasCouponDetail> findVasCouponDetailList(VasCouponDetail vasCouponDetail, int start, int limit);

	/**
	 * @param vasCouponDetail
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @Described : 条件检索数据
	 * @return List<VasCouponDetail>
	 */
	Pager findVasCouponDetailList(VasCouponDetail vasCouponDetail, Pager pager);

	/**
	 * @param vasCouponDetail
	 *            检索条件
	 * @Described : 统计条数
	 * @return int
	 */
	int findVasCouponDetailCount(VasCouponDetail vasCouponDetail);


	/**
	 *  @Description    : 给用户派发卡券
	 *  @Method_Name    : distributeCouponToUser
	 * @param userIds   : 用户id集合
	 * @param couponIds   : 卡券id
	 * @param reason   : 派发原因
	 *  @return          : ResponseEntity
	 *  @Creation Date  : 2018年4月8日10:26:34
	 *  @Author         : zhongpingtang@hongkun.com.cn
	 */
	boolean distributeCouponToUser(List<Integer> userIds, List<Integer> couponIds, String reason);

	/**
	 * @param product
	 * @param num
	 * @return : List<VasCouponDetail>
	 * @Description : 根据卡券产品生成卡券数据
	 * @Method_Name : generateCouponDetail
	 * @Creation Date : 2017年07月08日 下午20:36:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	List<VasCouponDetail> generateCouponDetail(VasCouponProduct product, Integer num);

	/**
	 *  @Description    : 验证卡券的有效性
	 *  @Method_Name    : validateCoupon
	 *  @param redPacketId
	 *  @param raiseInterestId
	 *  @param regUserId
	 *  @param type
	 *  @param allowCoupon
	 *  @param money
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2018年1月29日 下午2:59:39 
	 *  @Author         : zhongpingtang@hongkun.com
	 */
	ResponseEntity validateCoupon(Integer redPacketId, Integer raiseInterestId, Integer regUserId, Integer type, Integer allowCoupon, BigDecimal money);
	/**
	 * @param param{acceptorUserId  用户的id(必填)
	 * 				 state				获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(非必填)
	 * 				 type               获取用户对应的卡券产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券(非必填)}
	 * @return : List<VasCouponDetailVO>
	 * @Description : 获取用户对的卡券信息
	 * @Method_Name : getUserCouponDetailList
	 * @Creation Date : 2017年12月14日 下午15:36:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	List<VasCouponDetailVO> getUserCouponDetailList(Map<String, Object> param);


	/**
	 * @param productType  标的产品类型
	 * @param regUserId  	  用户id
	 * @return : Map{
	 *     interestCouponList : 	加息券集合
	 *     redPacketsCouponList :	投资红包集合
	 * }
	 * @Description : 获取用户投资可用的加息券和投资红包集合
	 * @Method_Name : getUserInvestUsableCoupon
	 * @Creation Date : 2017年12月15日 下午15:36:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	Map<String,Object> getUserInvestUsableCoupon(int productType,int regUserId);

	/**
	 * @param regUserId  用户id
	 * @param avtKey  		卡券激活码
	 * @return : ResponseEntity
	 * @Description : 激活卡券
	 * @Method_Name : activeCouponDetail
	 * @Creation Date  : 2017年12月15日 上午09:45:50
	 * @Author : pengwu@hongkun.com.cn
	 */
    ResponseEntity activeCouponDetail(int regUserId,String avtKey);

	/**
	 * @param param{acceptorUserId  用户的id(必填)
	 * 				 state				获取用户对应:1-未使用，2-已使用，3-已过期，4-可转赠的卡券信息(非必填)
	 * 				 type               获取用户对应的卡券产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券(非必填)}
	 * @return : Integer
	 * @Description : 获取用户对的卡券信息的数量
	 * @Method_Name : getUserCouponDetailListCount
	 * @Creation Date : 2017年12月14日 下午15:36:50
	 * @Author : pengwu@hongkun.com.cn
	 */
	Integer getUserCouponDetailListCount(Map<String, Object> param);

	/**
	 * @param couponId
	 * @param num
	 * @return : ResponseEntity
	 * @Description : 生成卡券数据
	 * @Method_Name : generateCouponDetailList
	 * @Creation Date  : 2017年07月07日 下午17:24:50
	 * @Author : pengwu@hongkun.com.cn
	 */
    ResponseEntity generateCouponDetailList(Integer couponId, Integer num);

	/**
	 *  @Description    ：跑批将已过期的卡券、投资红包修改为已过期状态
	 *  @Method_Name    ：couponDetailOverDue
	 *  @param currentDate  当前时间
	 *  @param shardingItem  分片项
	 *  @return void
	 *  @Creation Date  ：2018/4/25
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	void couponDetailOverDue(Date currentDate, int shardingItem);

	/**
	 *  @Description    ：对用户生成相应的卡券
	 *  @Method_Name    ：generateUserCouponDetail
	 *  @param regUserId  当前用户
	 *  @param productId  卡券产品
	 *  @param num  生成的数量
	 *  @param reason  生成卡券原因
	 *  @Creation Date  ：2018/4/28
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	void generateUserCouponDetail(Integer regUserId, Integer productId, int num,String reason);

	/**
	 *  @Description    ：根据卡券id集合获取卡券信息
	 *  @Method_Name    ：findVasCouponDetailByIds
	 *  @param couponIds 卡券id集合
	 *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasCouponDetail>
	 *  @Creation Date  ：2018/10/23
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    Map<Integer, VasCouponDetail> findVasCouponDetailByIds(Set<Integer> couponIds);

    /**
     * @Description      ：获取用户当前时间可用的提现券列表--将查询用户可用卡券信息和查询当前时间用户可用卡券信息拆分开 
     * @Method_Name      ：getUserWithdrawUsableCoupon 
     * @param regUserId
     * @return java.util.List<com.hongkun.finance.vas.model.vo.VasCouponDetailVO>    
     * @Creation Date    ：2019/1/25        
     * @Author           ：pengwu@hongkunjinfu.com
     */
    List<VasCouponDetailVO> getUserWithdrawUsableCoupon(Integer regUserId);
}
