package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasVipTreatment;
import com.yirun.framework.core.model.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.VasVipTreatmentService.java
 * @Class Name    : VasVipTreatmentService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasVipTreatmentService {

	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasVipTreatment
	 */
	VasVipTreatment findVasVipTreatmentById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipTreatment 检索条件
	 * @return	List<VasVipTreatment>
	 */
	List<VasVipTreatment> findVasVipTreatmentList(VasVipTreatment vasVipTreatment);

	/**
	 *  @Description    : 保存会员等待遇记录
	 *  @Method_Name    : addVipTreatment
	 *  @param vasVipTreatment
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2018年01月12日 上午09:16:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity addVipTreatment(VasVipTreatment vasVipTreatment);

	/**
	 *  @Description    : 更新会员待遇记录
	 *  @Method_Name    : update
	 *  @param vasVipTreatment
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 下午13:45:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity updateVipTreatment(VasVipTreatment vasVipTreatment);

	/**
	 *  @Description    : 根据用户注册时间获取其对应的会员待遇
	 *  @Method_Name    : getVipTreatMentByRegistTime
	 *  @param registTime
	 *  @return         : List<VasVipTreatment>
	 *  @Creation Date  : 2018年01月30日 下午16:21:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	List<VasVipTreatment> getVipTreatMentByRegistTime(Date registTime);

	/**
	 *  @Description    : 根据会员待遇类型和用户注册时间获取其对应的会员待遇
	 *  @Method_Name    : getVipTreatMentByTypeAndRegistTime
	 *  @param type
	 *  @param registTime
	 *  @return         : VasVipTreatment
	 *  @Creation Date  : 2018年01月30日 下午16:49:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	VasVipTreatment getVipTreatMentByTypeAndRegistTime(int type,Date registTime);

	/**
	 *  @Description    : 获取适用于当前用户的会员待遇集合描述信息
	 *  @Method_Name    : getVipTreatMentListDescription
	 *  @param regUserId    用户id
	 *  @param registTime   用户注册时间
	 *  @return         : List<Map<String,Object>>
	 *  @Creation Date  : 2018年03月12日 下午16:49:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	List<Map<String,Object>> getVipTreatMentListDescription(int regUserId, Date registTime);

	/**
	 *  @Description    ：根据等级和待遇信息获取其对应的会员待遇信息
	 *  @Method_Name    ：getVipTreatMentListDescriptionByLevel
	 *  @param level  会员等级
	 *  @param list   会员待遇列表
	 *  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 *  @Creation Date  ：2018/10/11
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	public List<Map<String,Object>> getVipTreatMentListDescriptionByLevel(int level,List<VasVipTreatment> list);
}
