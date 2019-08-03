package com.hongkun.finance.vas.facade;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Description : 卡券详情的facade
 * @Project : finance
 * @Program Name  : com.hongkun.finance.vas.facade.CouponFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface CouponFacade {
     /**
      * 卡券详情搜索
      * @param vasCouponDetailVO
      * @param pager
      * @return
      */
     Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO,Pager pager) ;

     /**
      * 执行卡券转赠
      * @param regUser      转赠人信息
      * @param acceptorId   卡券接收人
      * @param couponId     卡券id
      * @return : ResponseEntity
      * @Description : 获取用户的卡券详情列表
      * @Method_Name : couponDonation
      * @Creation Date  : 2017年12月27日 下午16:45:50
      * @Author : pengwu@hongkun.com.cn
      */
     ResponseEntity doCouponDonation(RegUser regUser, int acceptorId, int couponId);

     /**
      * @param couponId  卡券详情id
      * @return : ResponseEntity
      * @Description : 查看卡券详情的转赠记录
      * @Method_Name : getCouponDonationRecordList
      * @Creation Date  : 2017年11月09日 下午14:24:50
      * @Author : pengwu@hongkun.com.cn
      */

     ResponseEntity getCouponDonationRecordList(int couponId);

     /**
      * @param userId           用户id
      * @param bidId            标地id
      * @return : Map<String,Object>
      * @Description : 获取用户某标的可用卡券
      * @Method_Name : getBidCouponDetailList
      * @Creation Date  : 2018年03月09日 上午10:49:50
      * @Author : pengwu@hongkun.com.cn
      */
     ResponseEntity getBidCouponDetailList(int userId, int bidId);

     /**
      *  @Description    ：给用户补发卡券详情信息
      *  @Method_Name    ：reissueUserCouponDetail
      *  @param regUserId  用户id
      *  @param couponProductId  卡券产品id
      *  @param reason  补发卡券原因
      *  @return void
      *  @Creation Date  ：2018/5/30
      *  @Author         ：pengwu@hongkun.com.cn
      */
     void reissueUserCouponDetail(Integer regUserId, Integer couponProductId, String reason);
}
