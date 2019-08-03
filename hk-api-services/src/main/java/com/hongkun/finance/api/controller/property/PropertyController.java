package com.hongkun.finance.api.controller.property;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.factory.YeepayPayFactory;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinBankCardFrontService;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.property.constants.ProAddressContants;
import com.hongkun.finance.property.constants.PropertyContants;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.model.ProAddress;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.property.service.ProAddressService;
import com.hongkun.finance.property.service.ProPayRecordService;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserCommunity;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserCommunityService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;                               
import java.util.Map;
import static com.yirun.framework.core.commons.Constants.*;

@Controller
@RequestMapping("/propertyController")
public class PropertyController {
	private static final Logger logger = LoggerFactory.getLogger(YeepayPayFactory.class);
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private PointAccountService pointAccountService;
	@Reference
	private RegUserCommunityService regUserCommunityService;
	@Reference
	private ProAddressService proAddressService;
	@Reference
	private ProPayRecordService propayRecordService;
	@Reference
	private PropertyPaymentFacade propertyPaymentFacade;
	@Reference
	private PointCommonService pointCommonService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinBankCardFrontService finBankCardFrontService;
	@Reference
	private RegCompanyInfoService regCompanyInfoService;

	/**
	*  @Description    ：进入缴费页面（判断是否实名，是否存在缴费地址）
	*  @Method_Name    ：propertyIndex
	*
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/10/24
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	@RequestMapping(value="propertyIndex")
	@ResponseBody
	public Map<String,Object> propertyIndex(){
		logger.info("方法，propertyIndex,进入缴费页面。");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		//是否实名
		resultMap.put("identify",regUser.getIdentify());
		//是否存在缴费地址
		ProAddress proAddress=new ProAddress();
		proAddress.setRegUserId(regUser.getId());
		proAddress.setState(ProAddressContants.ADDRESS_STATE_NOT_DELETE);
		List<ProAddress> list=proAddressService.findProAddressList(proAddress);
		if (CommonUtils.isNotEmpty(list)){
			resultMap.put("address",ProAddressContants.ADDRESS_USER_EXIST);
		}else{
			resultMap.put("address",ProAddressContants.ADDRESS_USER_NO_EXIST);
		}
		resultMap.put("resStatus",String.valueOf(Constants.SUCCESS));
		logger.info("方法，propertyIndex,resultMap:{}",resultMap);
		return resultMap;
	}

	/**
	 * 
	 *  @Description    : 添加物业缴费地址
     * 	 *  @Method_Name    : addProAddress
     * 	 *  @param communityId  小区id
     * 	 *  @param floor  楼栋
     * 	 *  @param unit   单元
     * 	 *  @param door   户号
     * 	 *  @param note   备注
     * 	 *  @return
     * 	 *  @return         : ResponseEntity<?>
     * 	 *  @Creation Date  : 2018年3月20日 上午9:32:11
     * 	 *  @Author         : binliang@honghun.com.cn 梁彬
	 */
	@RequestMapping(value="addProAddress")
	@ResponseBody
	public Map<String,Object> addProAddress(Integer communityId,String floor,
			String unit,String door,String note){
		RegUserDetail regUser = BaseUtil.getRegUserDetail(null);
		if(!CommonUtils.gtZero(regUser.getId())||!CommonUtils.gtZero(communityId)||StringUtils.isBlank(floor)
				||StringUtils.isBlank(unit)||StringUtils.isBlank(door)){
			return AppResultUtil.errorOfMsg(ERROR,"请完善地址信息");
		}
		//根据小区id 查询小区名称&物业名称
		RegUserCommunity  ruc = regUserCommunityService.findRegUserCommunityById(communityId);
		if(ruc!=null){
			ProAddress proAddress = new ProAddress();
			proAddress.setRegUserId(regUser.getRegUserId());
			proAddress.setUserName(regUser.getRealName());
			proAddress.setCommunityId(communityId);
			proAddress.setCommunityName(ruc.getCommunityName());
			if(regUser!=null){
				proAddress.setProId(ruc.getRegUserId());
				//查询物业名称
				RegCompanyInfo companyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(ruc.getRegUserId());
				if (companyInfo!= null ){
					proAddress.setProName(companyInfo.getEnterpriseName());
				}
				proAddress.setFloor(floor);
				proAddress.setUnit(unit);
				proAddress.setDoor(door);
				proAddress.setNote(note);
				//拼接具体地址
				String address = ruc.getCommunityName() + floor+"号楼"+unit+"单元"+door;
				proAddress.setAddress(address);
			}
			int addressId = proAddressService.insertProAddress(proAddress);
			return AppResultUtil.successOfMsg("添加缴费地址成功").addResParameter("addressId",addressId);
		}
		return AppResultUtil.errorOfMsg("没有找到小区信息");
	}
	/**
		 * @Description    : 删除物业缴费地址
		 * @Method_Name    : delProAddress
		 * @param addressId	地址id
		 * @return         : ResponseEntity<?>
		 * @Creation Date  : 2018年3月15日 14:32:21
		 * @Author         : binliang@honghun.com.cn 梁彬
		 */
		@RequestMapping(value="delProAddress")
		@ResponseBody
		public Map<String,Object> delProAddress(Integer addressId){
			RegUser regUser = BaseUtil.getLoginUser();
			if(!CommonUtils.gtZero(addressId)){
				return AppResultUtil.errorOfMsg("未收到要删除物业账户地址的ID");
			}
			ProAddress proAddress=this.proAddressService.findProAddressById(addressId);
			if(proAddress==null){
				return AppResultUtil.errorOfMsg("物业账户地址不存在");
			}
			if(proAddress.getRegUserId().intValue()!=regUser.getId().intValue()){
				return AppResultUtil.errorOfMsg("当前登录人不能删改其他人缴费地址");
			}
			proAddress.setState(ProAddressContants.ADDRESS_STATE_DELETE);//0删除，逻辑删除
			proAddressService.updateProAddress(proAddress);
			return AppResultUtil.successOfMsg("删除成功");
	}
	
	
	/**
	 * @Description    : 获取用户的物业缴费地址列表
	 * @Method_Name    : getPropertyAccountList
	 * @return         : Map<String,Object>
	 * @Creation Date  : 2018年3月19日 11:34:21
	 * @Author         : binliang@honghun.com.cn 梁彬
	 */
	@RequestMapping("/getProAddressList")
    @ResponseBody
    public Map<String,Object> getProAddressList(){
		Pager pager = new Pager();
		RegUser regUser = BaseUtil.getLoginUser();
		ProAddress proAddress=new ProAddress();
		proAddress.setRegUserId(regUser.getId());
//		proAddress.setRegUserId(1146);
		proAddress.setState(ProAddressContants.ADDRESS_STATE_NOT_DELETE);
		//List<ProAddress> list=proAddressService.findProAddressList(proAddress);
		pager = proAddressService.findProAddressList(proAddress, pager);
		if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
			pager.getData().stream().forEach((e) -> {
				ProAddress temp = (ProAddress)e;
				RegUserCommunity  communityVo = regUserCommunityService.findRegUserCommunityById(temp.getCommunityId());//根据小区id找物业公司
				if(temp.getProId().equals(communityVo.getRegUserId())){
					temp.setIsValid(ProAddressContants.ADDRESS_IS_VALID_YES);
				}else{
					temp.setIsValid(ProAddressContants.ADDRESS_IS_VALID_NO);
				}
			});
		}
		String[] includeProperteis={"id","proId","proName","communityId","communityName","floor","unit","door","note","isValid"};
        return AppResultUtil.successOfListInProperties(pager.getData(), "查询成功", includeProperteis);
    }
	/**
	 * @Description    : 获取用户物业缴费记录列表
	 * @Method_Name    : getPropertypayRecordList
	 * @param pager
	 * @return         : Map<String,Object>
	 * @Creation Date  : 2018年3月19日 16:06:35
	 * @Author         : binliang@honghun.com.cn 梁彬
	 */
	@RequestMapping("/getPropertypayRecordList")
    @ResponseBody
    public Map<String,Object> getPropertypayRecordList(Integer payType , Pager pager){
		logger.info("方法, getPropertypayRecordList , 入参 : payType : {}, pager : {}" ,payType, pager);
		RegUser regUser = BaseUtil.getLoginUser();
//		RegUser regUser = this.regUserService.findRegUserById(1127);
		pager.setInfiniteMode(true);
		if(!CommonUtils.gtZero(regUser.getId())){
			return AppResultUtil.ERROR_MAP;
		}
		ProPayRecord record=new ProPayRecord();
		record.setRegUserId(regUser.getId());
		record.setSortColumns("created_time desc ");
		if(payType != null && payType != 0 ){
			record.setPayType(payType);
		}
		pager = this.propayRecordService.findProPayRecordList(record,pager);
		if (!(pager == null || pager.getData() == null || pager.getData().size() == 0)) {
			pager.getData().stream().forEach((e) -> {
				ProPayRecord temp = (ProPayRecord)e;
				if (temp.getPayType() == PropertyContants.PROPERTY_PAY_TYPE_CAR) {
					temp.setPayTypeDesc(PropertyContants.PROPERTY_PAY_TYPE_CAR_DESC);
				} else if (temp.getPayType() == PropertyContants.PROPERTY_PAY_TYPE_PROPERTY) {
					temp.setPayTypeDesc(PropertyContants.PROPERTY_PAY_TYPE_PROPERTY_DESC);
				}
				if (temp.getState() == PropertyContants.PROPERTY_RECORD_STATE_ING ){
					temp.setStateDesc(PropertyContants.PROPERTY_RECORD_STATE_PROCESSING_DESC);
				} else if (temp.getState() == PropertyContants.PROPERTY_RECORD_STATE_FREE) {
					temp.setStateDesc(PropertyContants.PROPERTY_RECORD_STATE_AUDITED_DESC);
				}else if (temp.getState() == PropertyContants.PROPERTY_RECORD_STATE_FAIL ||
						temp.getState() == PropertyContants.PROPERTY_RECORD_STATE_REFUSE) {
					temp.setStateDesc(PropertyContants.PROPERTY_RECORD_STATE_FAIL_DESC);
				}else if (temp.getState() == PropertyContants.PROPERTY_RECORD_STATE_SUCCESS){
					temp.setStateDesc(PropertyContants.PROPERTY_RECORD_STATE_SUCCESS_DESC);
				}
			});
		}
		String[] includeProperteis={"id","money","payType","payTypeDesc","state","createdTime","stateDesc"};
		return AppResultUtil.successOfListInProperties(pager.getData(), "查询成功", includeProperteis);
	}
    /**
     * @Description    : 获取物业缴费详情记录
	 * @Method_Name    : proPayDetail 
     * @param 		   : payRecordId
     * @Creation Date  : 2018年3月19日 16:06:35
	 * @Author         : binliang@honghun.com.cn 梁彬
	 */
    @RequestMapping("/proPayDetail")
    @ResponseBody
    public Map<String,Object> proPayDetail(Integer payRecordId){
    	if(!CommonUtils.gtZero(payRecordId)){
			return AppResultUtil.errorOfMsg("找不到需要查看的缴费ID");
		}
		ProPayRecord result=this.propertyPaymentFacade.findPropertyRecordById(payRecordId);
		if(result==null){
    		return AppResultUtil.errorOfMsg("没有此记录");
    	}
//		ProAddress address =  proAddressService.findProAddressById(result.getAddressId());
//		result.setAddress(address.getAddress());
//		result.setPropertyName(address.getProName());
//		result.setCommunityName(address.getCommunityName());
//		result.setUserName(address.getUserName());
//		result.setNote(address.getNote());
    	String[] includeProperteis={"id","money","payType","createdTime","state","useableMoney","rechargeMoney","point","pointMoney"
				,"communityName","proName","startTime","endTime","userName","address","note"};
		return AppResultUtil.successOfInProperties(result,"查询成功",includeProperteis);
    }
    /**
     * @Description    : 根据金额获取抵扣积分、剩余积分、积分抵扣金额
	 * @Method_Name    : getPointMoney
     * @param 		   : userId
	 * @param 		   : money
	 * @Creation Date  : 2018年3月19日 18:48:14
	 * @Author         : binliang@honghun.com.cn 梁彬
	 */
	@RequestMapping("/getPointMoney")
	@ResponseBody
	public Map<String,Object> getPointMoney(BigDecimal money){
		RegUser regUser = BaseUtil.getLoginUser();
		if(!CompareUtil.gt(money, BigDecimal.ZERO)){
			return AppResultUtil.errorOfMsg("缴费金额不能为0");
		}
		Map<String,Object> result=(Map<String,Object>)(this.propertyPaymentFacade.getPointMoney(regUser.getId(), money).getResMsg());
		return AppResultUtil.successOf(result,"查询成功");
	}
	/**
	 *
	 *  @Description    : 缴纳物业费
	 *  @Method_Name    : payProperty
	 *  @param proAddressId 缴费地址
	 *  @param money 缴费金额
	 *  @param usePoints 是否使用积分 1-使用 0-不使用
	 *  @param payType 缴费类型 1-物业费 2-车位费
	 *  @param startTime 缴费开始时间
	 *  @param endTime   缴费结束时间
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月19日 下午4:42:47
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping(value="payProperty")
	@ResponseBody
	public Map<String,Object> payProperty(Integer proAddressId,BigDecimal money,
										  Integer usePoints,Integer payType,String startTime ,String endTime,String note){
		RegUser regUser = BaseUtil.getLoginUser();
//		RegUser regUser = this.regUserService.findRegUserById(1146);
		String userName = this.regUserDetailService.findRegUserDetailNameByRegUserId(regUser.getId());
		ProPayRecord proPayRecord = new ProPayRecord();
		proPayRecord.setAddressId(proAddressId);
		proPayRecord.setMoney(money);
		proPayRecord.setStartTime(DateUtils.parse(startTime));
		proPayRecord.setEndTime(DateUtils.parse(endTime));
		proPayRecord.setRegUserId(regUser.getId());
		proPayRecord.setNote(note);
		proPayRecord.setUserName(userName);
		ProAddress proAddress = proAddressService.findProAddressById(proAddressId);
		proPayRecord.setCommunityId(proAddress.getCommunityId());
		proPayRecord.setCommunityName(proAddress.getCommunityName());
		proPayRecord.setProId(proAddress.getProId());
		proPayRecord.setProName(proAddress.getProName());
		proPayRecord.setAddress(proAddress.getAddress());
		ResponseEntity<?> result = propertyPaymentFacade.propertyPayment(regUser, proPayRecord,usePoints);
		if(BaseUtil.error(result)){
			return AppResultUtil.errorOfMsg(result.getResMsg());
		}
		return AppResultUtil.successOf(result.getResMsg());
	}
	
	/**
	 *
	 *  @Description    : 查询小区&物业列表
	 *  @Method_Name    : payProperty
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月19日 下午4:42:47 
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping(value="findCommunityList")
	@ResponseBody
	public Map<String,Object> findCommunityList(){
		List<RegUserCommunity> resultList =  regUserCommunityService.findAllCommunityList();
		return AppResultUtil.successOfListInProperties(resultList, "查询成功", "id","communityName","regUserId","proName");
	}
	
	/**
	 *
	 *  @Description    : 缴费页面-查询积分和抵扣金额信息
	 *  @Method_Name    : payProperty
	 *  @param platformSource  来源  11-ios  12-Android
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月19日 下午4:42:47
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping(value="toPropertyInfo")
	@ResponseBody
	public Map<String,Object> toPropertyInfo(Integer platformSource){
		RegUser regUser = BaseUtil.getLoginUser();
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
		FinAccount account = finAccountService.findByRegUserId(regUser.getId());
		PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUser.getId());
		if(account == null || pointAccount == null){
			return AppResultUtil.errorOfMsg("未查询到用户账户或积分账户");
		}
		BigDecimal pointMoney = pointCommonService.pointToMoney(pointAccount.getPoint());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("useableMoney", account==null?BigDecimal.ZERO:account.getUseableMoney());
		resultMap.put("point", pointAccount==null?BigDecimal.ZERO:pointAccount.getPoint());
		resultMap.put("pointMoney", pointMoney);
		resultMap.put("realName", regUserDetail.getRealName());
		//查询支付渠道
		FinChannelGrant  fcg = finBankCardFrontService.findFirstFinChannelGrant(SystemTypeEnums.HKJF,
				platformSource == PlatformSourceEnums.ANDROID.getValue()?PlatformSourceEnums.ANDROID:PlatformSourceEnums.IOS, PayStyleEnum.RECHARGE);
		Integer channelNameCode = fcg == null ? 0: fcg.getChannelNameCode();
		resultMap.put("channelNameCode", channelNameCode);
		return AppResultUtil.successOf(resultMap);
	}


	/**
	 *
	 *  @Description    : 缴纳物业费--充值后回调，处理充值缴费业务
	 *  @Method_Name    : payProperty
	 *  @param proPayRecordId 缴费记录id
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月19日 下午4:42:47
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	@RequestMapping(value="payPropertyCallBack")
	@ResponseBody
	public Map<String,Object> payPropertyCallBack(Integer proPayRecordId,String paymentFlowId){
		RegUser regUser = BaseUtil.getLoginUser();
		ResponseEntity<?> result = propertyPaymentFacade.payPropertyCallBack(regUser, proPayRecordId,paymentFlowId);
		if(BaseUtil.error(result)){
			return AppResultUtil.errorOfMsg(result.getResMsg());
		}
		return AppResultUtil.successOf(result.getResMsg());
	}
}
