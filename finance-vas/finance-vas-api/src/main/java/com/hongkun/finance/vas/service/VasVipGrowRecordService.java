package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import javax.jms.JMSException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.VasVipGrowRecordService.java
 * @Class Name    : VasVipGrowRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasVipGrowRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasVipGrowRecord 持久化的数据对象
	 * @return				: int
	 */
	int insertVasVipGrowRecord(VasVipGrowRecord vasVipGrowRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGrowRecord 检索条件
	 * @return	List<VasVipGrowRecord>
	 */
	List<VasVipGrowRecord> findVasVipGrowRecordList(VasVipGrowRecord vasVipGrowRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGrowRecord 检索条件
	 * @param pager	分页数据
	 * @return	Pager
	 */
	Pager findVasVipGrowRecordList(VasVipGrowRecord vasVipGrowRecord, Pager pager);

	/**
	 *  @Description    : 获取MQ传递信息插入用户成长值记录
	 *  @Method_Name    : insertVasVipGrowRecord
	 *  @param registTime
	 *  @param vasVipGrowRecordMqVO
	 *  @Creation Date  : 2017年6月29日 下午14:55:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	void insertVasVipGrowRecord(Date registTime, VasVipGrowRecordMqVO vasVipGrowRecordMqVO) throws JMSException;

	/**
	 *  @Description    : 获取用户当前成长值
	 *  @Method_Name    : findUserCurrentGrowValue
	 *  @param userId
	 *  @return         : int
	 *  @Creation Date  : 2017年6月29日 下午16:51:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	int findUserCurrentGrowValue(int userId);

	/**
	 *  @Description    : 获取所有用户成长值记录
	 *  @Method_Name    : findVasVipGrowRecordList
	 *  @param userIds
	 *  @param growthValMin
	 *  @param growthValMax
	 *  @param pager
	 *  @return         : Pager
	 *  @Creation Date  : 2017年07月05日 下午10:37:55
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	Pager findVasVipGrowRecordList(List<Integer> userIds,Integer growthValMin,Integer growthValMax,Pager pager);
	
	/**
	 *  @Description    : 查询用户的成长值及等级
	 *  @Method_Name    : findUserGrowValueAndLevel
	 *  @param regUserId
	 *  @return         : Map<String, Object>
	 *  @Creation Date  : 2018年1月2日 下午3:12:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Map<String, Object> findUserGrowValueAndLevel(Integer regUserId);

	/**
	 * @Description : 获取用户成长值记录
	 * @Method_Name : userGrowRecordDetail
	 * @param userId
	 * @param pager
	 * @return : ResponseEntity
	 * @Creation Date : 2017年7月03日 下午17:40:50
	 * @Author : pengwu@hongkun.com.cn
	 */
    ResponseEntity getUserGrowRecordDetail(int userId, Pager pager);

    /**
     *  @Description    ：根据用户id集合获取会员成长值
     *  @Method_Name    ：findUserGrowValueMap
     *  @param userIdList  用户id集合
     *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasVipGrowRecord>
     *  @Creation Date  ：2018/4/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<Integer,VasVipGrowRecord> findUserGrowValueMap(List<Integer> userIdList);

	/**
	 *  @Description    ：获取会员等级大于0级的用户集合
	 *  @Method_Name    ：findUserLevelGtZeroMap
	 *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.vas.model.VasVipGrowRecord>
	 *  @Creation Date  ：2018/5/2
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	Map<Integer, VasVipGrowRecord> findUserLevelGtZeroMap();

	/**
	 *  @Description    ：近三个月进行过降级的用户id集合
	 *  @Method_Name    ：findUserThreeMonthHasDown
	 *  @param userIdList  用户id集合
	 *  @return java.util.List<java.lang.Integer>
	 *  @Creation Date  ：2018/5/2
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	List<Integer> findUserThreeMonthHasDown(Set<Integer> userIdList);
}
