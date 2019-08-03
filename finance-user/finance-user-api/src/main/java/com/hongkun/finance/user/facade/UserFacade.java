package com.hongkun.finance.user.facade;

import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Description :用户相关数据Facade
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.facade.UserFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface UserFacade {
    @SuppressWarnings("rawtypes")
	ResponseEntity queryUserAccountMoney(Pager pager, UserVO userVO);
    
    /**
     *  @Description    : 注册 
     *  @Method_Name    : insertRegUserForRegist
     *  @param regUser
     *  @param regUserDetail
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年1月10日 下午5:29:41 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> insertRegUserForRegist(RegUser regUser, RegUserDetail regUserDetail);
    
    /**
     *  @Description    : 查询用户详情
     *  @Method_Name    : findUserVOList
     *  @param pager
     *  @param userVO
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2017年10月11日 下午3:24:32 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> findUserVOList(Pager pager, UserVO userVO);
    
    /**
     *  @Description    : 导入用户信息
     *  @Method_Name    : importUsers
     *  @param filePath 上传的文件路径
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2017年10月18日 上午9:31:03 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> importUsers(String filePath);
    
    /**
    *  用户管理-用户详情
    *  @Method_Name             ：findUserDtailInfo
    *  @param regUserId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/5/7
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    ResponseEntity<?> findUserDtailInfo(Integer regUserId);
    
    /**
     *  @Description    : 查找用户我的账户数据信息
     *  @Method_Name    : findUserMyAccount
     *  @param regUserId
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年1月17日 上午11:51:49 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> findUserMyAccount(Integer regUserId);

    /**
    *  查询用户总资产
    *  @Method_Name             ：findUserTotalAccount
    *  @param regUserId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/8/17
    *  @Author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> findUserTotalAccount(Integer regUserId);
    
    /**
     *  @Description    : 实名认证
     *  @Method_Name    : updateUserForRealName
     *  @param regUserId	：实名用户
     *  @param idCard		：身份证
     *  @param realName		：真实姓名
     *  @param email		：邮箱
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年1月30日 下午4:32:53 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> updateUserForRealName(Integer regUserId, String idCard, String realName, String email);
    
    /**
     *  @Description    : 查询我的账户数据信息
     *  @Method_Name    : findUserMyAccountForApp
     *  @param regUserId
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年3月8日 下午5:31:34 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> findUserMyAccountForApp(Integer regUserId);
    
    /**
     *  @Description    : 更新用户推荐数据
     *  @Method_Name    : updateRegUserForSpread
     *  @param regUserId: 用户id
     *  @param nickName : 昵称
     *  @param commendNo: 推荐码
     *  @return         : ResponseEntity<?>
     *  @Creation Date  : 2018年3月9日 下午7:09:17 
     *  @Author         : zhichaoding@hongkun.com zc.ding
     */
    ResponseEntity<?> updateRegUserForSpread(Integer regUserId, String nickName, String commendNo);

    /**
     * @Description : app端用户登录日志查询
     * @Method_Name : appLoginLogList
     * @Date : 2018/3/23 9:07
     * @Author : ruilima@hongkun.com.cn 马瑞丽
     * @param pager
     * @param appLoginLog
     * @return
     */
    ResponseEntity<?> appLoginLogList(Pager pager, AppLoginLog appLoginLog);
    
    /**
    *  查询用户资料信息
    *  @Method_Name             ：findRegAuditList
    *  @param regUserId
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/5/7
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    ResponseEntity<?> findRegAuditList(Integer regUserId);


    ResponseEntity<?> insertRegUserForCompany(RegUser regUser, RegUserDetail regUserDetail,
                                              RegCompanyInfo regCompanyInfo, FinBankCard finBankCard);

    /**
     *  VIP用户降级处理
     *  @Method_Name             ：RegUserVipDemote
     *  @return void
     *  @Creation Date           ：2018/6/6
     *  @Author                  ：zhichaoding@hongkun.com.cn
     */
    void updateRegUserVip();
    
    /**
    *  插入注册用户在鸿坤金服的推荐人，并维护推荐关系
    *  @Method_Name             ：insertRecommendUserForRegist
    *  @param regUserId         : 注册用户
    *  @param regUser           ：注册用户的推荐人在鸿坤金服的用户信息
    *  @return void
    *  @Creation Date           ：2018/7/13
    *  @Author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> insertRecommendUserForRegist(Integer regUserId, RegUser regUser);
    
    /** 
    * @Description: 自定义服务操作
    * @Param: [regUserId, serves] 注册用户，服务
    * @return: void 
    * @Author: HeHang
    * @Date: 2018/9/26 
    */
    Map<String, Object> customizeServes(Integer regUserId, String serves);

    /** 
    * @Description: 获取用户自定义服务
    * @Param: [regUserId] 
    * @return: java.util.List<com.hongkun.finance.user.model.AppMoreServe> 
    * @Author: HeHang
    * @Date: 2018/9/26 
    */
    List<AppMoreServe> getUserServes(Integer regUserId);
}
