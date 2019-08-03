package com.hongkun.finance.property.facade;

import java.math.BigDecimal;
import java.util.List;

import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description   : 物业交费接口
 * @Project       : finance-property-api
 * @Program Name  : com.hongkun.finance.property.facade.PropertyFacade.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface PropertyPaymentFacade {
	/**
	 *  @Description    :物业缴费
	 *  @Method_Name    :propertyPayment
	 *  @param proPayRecord 缴费记录信息
	 *  @param regUser      当前用户
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年8月9日 下午2:08:36 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> propertyPayment(RegUser regUser,ProPayRecord proPayRecord,Integer usePoints);
	/**
	 *  @Description    : 物业缴费审核
	 *  @Method_Name    : auditingProPayment
	 *  @param proPayRecordId 缴费记录
	 *  @param regUser  审核人
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年8月10日 下午3:21:43 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> auditProPayment(int proPayRecordId,String opinion,int state,RegUser regUser);
	/**
	 *  @Description    : 查询物业缴费记录
	 *  @Method_Name    : findPropertyRecordList
	 *  @param proPayRecord 查询条件
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年8月14日 下午1:58:03 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findPropertyRecordList(ProPayRecord proPayRecord,Pager pager);
	/**
	 *  @Description    : 查询物业缴费记录
	 *  @Method_Name    : findPropertyRecordVoList
	 *  @param proPayRecordVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月10日 上午9:29:11 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findPropertyRecordVoList(ProPayRecord proPayRecordVo,Pager pager);
	/**
	 *  @Description    : 查询物业账户销售推荐
	 *  @Method_Name    : findPropertyStaffList
	 *  @param rosStaffInfo 查询条件
	 *  @param regUser  当前登录用户
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年8月14日 下午5:58:00 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> findPropertyStaffList(RosStaffInfo rosStaffInfo,Pager pager,RegUser regUser);
	/**
	 * 
	 *  @Description    : 查询某个物业公司下的缴费记录
	 *  @Method_Name    : findPropertyRecordVoListByPropertyId
	 *  @param proPayRecordVo
	 *  @param pager
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月12日 下午2:27:43 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	ResponseEntity<?> findPropertyRecordListByPropertyId(ProPayRecord proPayRecord, Pager pager);
	/**
	 * 
	 *  @Description    : 查询缴费记录(用于导出)
	 *  @Method_Name    : findPropertyRecordForExport
	 *  @param proPayRecordVo
	 *  @return
	 *  @return         : List<ProPayRecordVo>
	 *  @Creation Date  : 2018年1月15日 上午11:49:32 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	List<ProPayRecord> findPropertyRecordForExport(ProPayRecord proPayRecordVo,int start,int pageSize);

	/**
	 *  @Description    : 根据Id查询物业缴费详情
	 *  @Method_Name    : findPropertyRecordById
	 *  @param 			: propayRecordId
	 *  @return         : ProPayRecordVo
	 *  @Creation Date  : 2018年3月19日 上午16:49:19 
	 *  @Author         : binliang@hongkun.com.cn 梁彬
	 */
	ProPayRecord findPropertyRecordById(Integer propayRecordId);
	/**
	 * 
	 *  @Description    : 根据缴费金额获取抵扣积分、剩余积分、积分抵扣金额
	 *  @Method_Name    : getPointMoney
	 *  @param regUserId:用户id
	 *  @param money	:需要缴纳物业费金额
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月19日18:02:21 
	 *  @Author         : binliang@honghun.com.cn	梁彬
	 */
	ResponseEntity<?> getPointMoney(Integer regUserId,BigDecimal money);


	/**
	 *  @Description    :物业缴费--充值回调--处理充值缴费业务
	 *  @Method_Name    :propertyPayment
	 *  @param proPayRecordId 缴费记录id
	 *  @param regUser      当前用户
	 *  @param paymentFlowId    支付流水id
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年8月9日 下午2:08:36
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> payPropertyCallBack(RegUser regUser,Integer proPayRecordId,String paymentFlowId);
}
