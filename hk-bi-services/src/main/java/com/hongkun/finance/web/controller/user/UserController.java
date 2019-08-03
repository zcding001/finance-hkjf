package com.hongkun.finance.web.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.constants.UserRegistSource;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.*;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.REG_AUDIT_PREFIX;
import static com.hongkun.finance.user.constants.UserConstants.REG_AUDIT_TYPE_IDENTIFY;

/**
 * @Description : 用户信息维护接口
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.UserController.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("userController")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserInfoService regUserInfoService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private UserFacade userFacade;
	@Reference
	private AppLoginLogService appLoginLogService;
	@Reference
	private RegAuditRuleService regAuditRuleService;
	@Reference
	private RegUserVipRecordService regUserVipRecordService;

	@Value(value = "${oss.url}")
	private String ossUrl;

	/**
	 * @Description : 预更新用户信息
	 * @Method_Name : preUpdateUserInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月23日 下午8:10:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("preUpdateUserInfo")
	@ResponseBody
	@Token(operate = Ope.ADD)
	public ResponseEntity<?> preUpdateUserInfo(int regUserId) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		// 从redis加载用户信息
		RegUser regUser = JedisClusterUtils.getObjectForJson(RegUser.class.getSimpleName() + regUserId, RegUser.class);
		if (regUser == null) {
			regUser = this.regUserService.findRegUserById(regUserId);
		}
		RegUserDetail regUserDetail = JedisClusterUtils.getObjectForJson(RegUserDetail.class.getSimpleName() + regUserId,
				RegUserDetail.class);
		if (regUserDetail == null) {
			regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUserId);
		}
		RegUserInfo regUserInfo = JedisClusterUtils.getObjectForJson(RegUserInfo.class.getSimpleName() + regUserId,
				RegUserInfo.class);
		if (regUserInfo == null) {
			regUserInfo = this.regUserInfoService.findRegUserInfoByRegUserId(regUserId);
		}
		result.getParams().put("regUser", regUser);
		result.getParams().put("regUserDetail", regUserDetail);
		result.getParams().put("regUserInfo", regUserInfo);
		// 从服务端查询用户信息
		return result;
	}

	/**
	 * @Description : 添加企业账户
	 * @Method_Name : addCompanyUser
	 * @param regUser
	 * @param regUserDetail
	 * @param regCompanyInfo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月2日 上午9:54:23
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("addCompanyUser")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	@ActionLog(msg = "添加企业账户, 手机号: {args[0].login}, 企业名称: {args[2].enterpriseName}")
	public ResponseEntity<?> addCompanyUser(RegUser regUser, RegUserDetail regUserDetail, RegCompanyInfo regCompanyInfo,
			FinBankCard finBankCard) {
		logger.info("管理员: {}, 添加企业账户. 用户信息: {}, 企业信息: {}, 银行卡信息: {}.", BaseUtil.getLoginUser(), regUser.toString(),
				regCompanyInfo.toString(), finBankCard.toString());
		// 验证手机号
		ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(String.valueOf(regUser.getLogin()),
				regUser.getPasswd());
		if (result.getResStatus() == Constants.ERROR) {
			return result;
		}
		String passwd = (String) result.getResMsg();
		if (StringUtils.isBlank(finBankCard.getBankCard()) || StringUtils.isBlank(finBankCard.getBankCode())
				|| StringUtils.isBlank(finBankCard.getBranchName())) {
			return new ResponseEntity<>(Constants.ERROR, "银行卡号、银行名称、支行名称不允许为空");
		}
		if (StringUtils.isBlank(finBankCard.getBankProvince()) || StringUtils.isBlank(finBankCard.getBankCity())) {
			return new ResponseEntity<>(Constants.ERROR, "银行的省市不允许为空");
		}
		JedisClusterLock lock = new JedisClusterLock();
		try {
			if (lock.lock("reg_" + regUser.getLogin())) {
				regUserDetail.setRegistSource(UserRegistSource.CXJ_PC.getValue());
				regUser.setPasswd(passwd);
				result = this.userFacade.insertRegUserForCompany(regUser, regUserDetail, regCompanyInfo, finBankCard);
			}
		} catch (Exception e) {
			logger.error("管理员: {}, 添加企业账户. 用户信息: {}, 企业信息: {}, 银行卡信息: {}.", BaseUtil.getLoginUser(), regUser.toString(),
					regCompanyInfo.toString(), finBankCard.toString(), e);
			return ResponseEntity.error("添加企业账户失败");
		} finally {
			lock.freeLock("reg_" + regUser.getLogin());
		}
		return result;
	}

	@RequestMapping("chgRegUserState")
	@ResponseBody
	@ActionLog(msg = "更新用户的状态, 用户标识: {args[0]}, 用户状态: {args[1] == 1 ? '启用' : '禁用'}")
	public ResponseEntity<?> chgRegUserState(Integer id, Integer state) {
		RegUser regUser = new RegUser();
		regUser.setState(state);
		regUser.setId(id);
		return this.regUserService.updateRegUser(regUser);
	}

	/**
	 * @Description : 条件检索用户信息
	 * @Method_Name : userList
	 * @param pager
	 * @param userWithDetailVO
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月18日 上午10:22:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("userList")
	@ResponseBody
	public ResponseEntity<?> userList(Pager pager, UserVO userWithDetailVO) {
		Pager resultPage = this.regUserService.listConditionPage(userWithDetailVO, pager);
		if (!BaseUtil.resultPageHasNoData(resultPage)) {
			List<UserVO> users = (List<UserVO>) resultPage.getData();
			// 给用户设置身份证信息
			users.stream()
					.forEach(
							user -> user
									.setIdentifyPics(
											OSSLoader.getInstance()
													.listPathFileName(OSSBuckets.HKJF,
															REG_AUDIT_PREFIX + user.getLogin() + "/"
																	+ REG_AUDIT_TYPE_IDENTIFY)
													.stream().map(picUrl -> ossUrl + picUrl)
													.collect(Collectors.toList())));
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultPage);
	}

	/**
	 * @Description : 用户管理-用户详情
	 * @Method_Name : userDetial
	 * @param regUserId
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年9月21日 下午5:58:31
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("userDetial")
	@ResponseBody
	public ResponseEntity<?> userDetial(Integer regUserId) {
		return this.userFacade.findUserDtailInfo(regUserId);
	}

	/**
	 * 查询用户资料
	 * 
	 * @Method_Name ：userAudit
	 * @param regUserId
	 *            : 用户id
	 * @return com.yirun.framework.core.model.ResponseEntity<?>
	 * @Creation Date ：2018/5/4
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("userAudit")
	@ResponseBody
	public ResponseEntity<?> userAudit(Integer regUserId) {
		return this.userFacade.findRegAuditList(regUserId);
	}

	/**
	 * 用户认证资料文件上传
	 * 
	 * @Method_Name ：userAuditUpload
	 * @param regUserId
	 *            ：用户id
	 * @param type
	 *            ：图片类型 1：身份证 2、房产证
	 * @return com.yirun.framework.core.model.ResponseEntity<?>
	 * @Creation Date ：2018/5/4
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("userAuditUpload")
	@ResponseBody
	@ActionLog(msg = "维护用户: {args[0]}, 资料类型: {args[1]}")
	public String userAuditUpload(@RequestParam("regUserId") Integer regUserId, @RequestParam("auditType") Integer type,
			MultipartFile[] file) {
		RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
		String path = REG_AUDIT_PREFIX + regUser.getLogin() + "/" + type;
		// 文件上传到阿里云服务器
		FileInfo fileInfo = OSSLoader.getInstance().setUseRandomName(true)
				.bindingUploadFile(BaseUtil.transferMultiPartFileToFileInfo(file[0]))
				.setAllowUploadType(FileType.EXT_TYPE_IMAGE).setFileState(FileState.UN_UPLOAD)
				.setBucketName(OSSBuckets.HKJF).setFilePath(path).doUpload();
		return fileInfo.getSaveKey();
	}

	/**
	 * @Method_Name ：delUserAudit
	 * @param picPath
	 *            : 删除用户资料数据
	 * @return java.lang.String
	 * @Creation Date ：2018/5/7
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("delUserAudit")
	@ResponseBody
	@ActionLog(msg = "删除用户认证资料: {args[0]}")
	public ResponseEntity<?> delUserAudit(@RequestParam("picPath") String picPath) {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileState(FileState.SAVED);
		fileInfo.setSaveKey(picPath);
		fileInfo.setBucketName(OSSBuckets.HKJF);
		OSSLoader.getInstance().bindingUploadFile(fileInfo).doDelete();
		return ResponseEntity.SUCCESS;
	}

	/**
	 * 账户资金查询
	 * 
	 * @param pager
	 * @param userVO
	 * @return
	 */
	@RequestMapping("userAccountMoney")
	@ResponseBody
	public ResponseEntity<?> userAccountMoney(Pager pager, UserVO userVO) {
		return this.userFacade.queryUserAccountMoney(pager, userVO);
	}

	/**
	 * @Description : 检索所有的企业员工
	 * @Method_Name : enterpriseUserList
	 * @return : List<UserVO>
	 * @Creation Date : 2017年9月29日 下午3:44:09
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("enterpriseUserList")
	@ResponseBody
	public List<UserVO> enterpriseUserList() {
		UserVO userVO = new UserVO();
		userVO.setTypes(Arrays.asList(UserConstants.USER_TYPE_ENTERPRISE, UserConstants.USER_TYPE_TENEMENT));
		return this.regUserService.findUserWithDetailByInfo(userVO);
	}

	/**
	 * @Description : 查询物业公司列表（用作数据字典）
	 * @Method_Name : findPropertyList
	 * @return
	 * @return : List<CommonDicData>
	 * @Creation Date : 2017年10月11日 上午11:32:44
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@RequestMapping("findPropert yDicDataList")
	@ResponseBody
	public List<Map<String, Object>> findPropertyDicDataList() {
		return this.regUserService.findPropertyDicDataList();
	}

	/**
	 * @Description : 更新用户邀请码
	 * @Method_Name : updateCommendNo
	 * @param ids
	 *            : 用户集合
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年10月10日 下午5:18:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateCommendNo")
	@ResponseBody
	@ActionLog(msg = "更新邀请码, 用户集合: {args[0]}, 验证码: {args[1]}")
	public ResponseEntity<?> updateInviteNo(String ids, String commendNo) {
		if (StringUtils.isBlank(ids) || StringUtils.isBlank(commendNo)) {
			new ResponseEntity<>(Constants.ERROR, "非法数据");
		}
		return this.regUserService.updateCommendNo(ids, commendNo);
	}

	/**
	 * @Description : app端用户登录日志查询
	 * @Method_Name : appLoginLogList
	 * @Date : 2018/3/22 18:02
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param pager
	 * @param appLoginLog
	 * @return
	 */
	@RequestMapping("appLoginLogList")
	@ResponseBody
	public ResponseEntity<?> appLoginLogList(Pager pager, AppLoginLog appLoginLog) {
		return this.userFacade.appLoginLogList(pager, appLoginLog);
	}

	/**
	 * VIP降级记录
	 * 
	 * @Method_Name ：vipRecordsList
	 * @param pager
	 * @param userVO
	 * @return com.yirun.framework.core.model.ResponseEntity<?>
	 * @Creation Date ：2018/6/7
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("vipRecordsList")
	@ResponseBody
	public ResponseEntity<?> vipRecordsList(Pager pager, UserVO userVO) {
		return new ResponseEntity<>(Constants.SUCCESS, this.regUserService.findRegUserVipRecordList(userVO, pager));
	}

	/**
	 * 降级详情
	 * 
	 * @Method_Name ：vipRecordsDetailList
	 * @param pager
	 * @param id
	 * @return com.yirun.framework.core.model.ResponseEntity<?>
	 * @Creation Date ：2018/6/7
	 * @Author ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("vipRecordsDetailList")
	@ResponseBody
	public ResponseEntity<?> vipRecordsDetailList(Pager pager, Integer id) {
		RegUserVipRecord regUserVipRecord = new RegUserVipRecord();
		regUserVipRecord.setRegUserId(id);
		regUserVipRecord.setSortColumns("create_time DESC");
		return new ResponseEntity<>(Constants.SUCCESS,
				this.regUserVipRecordService.findRegUserVipRecordList(regUserVipRecord, pager));
	}
}
