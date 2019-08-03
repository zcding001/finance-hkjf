package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.service.VasVipGradeStandardService.java
 * @Class Name    : VasVipGradeStandardService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasVipGradeStandardService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasVipGradeStandard 持久化的数据对象
	 * @return				: int
	 */
	int insertVasVipGradeStandard(VasVipGradeStandard vasVipGradeStandard);
	
	/**
	 * @Described			: 更新数据
	 * @param vasVipGradeStandard 要更新的数据
	 * @return				: int
	 */
	int updateVasVipGradeStandard(VasVipGradeStandard vasVipGradeStandard);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasVipGradeStandard
	 */
	VasVipGradeStandard findVasVipGradeStandardById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGradeStandard 检索条件
	 * @return	List<VasVipGradeStandard>
	 */
	List<VasVipGradeStandard> findVasVipGradeStandardList(VasVipGradeStandard vasVipGradeStandard);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasVipGradeStandard 检索条件
	 * @param pager	分页数据
	 * @return	Pager
	 */
	Pager findVasVipGradeStandardList(VasVipGradeStandard vasVipGradeStandard, Pager pager);

	/**
	 *  @Description    : 根据会员等级获取会员等级标准
	 *  @Method_Name    : findVasVipGradeStandardByLevel
	 *  @param level
	 *  @return         : VasVipGradeStandard
	 *  @Creation Date  : 2017年07月05日 上午09:31:05
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	VasVipGradeStandard findVasVipGradeStandardByLevel(int level);

	/**
	 *  @Description    : 根据成长值获取其对应的会员等级
	 *  @Method_Name    : findLevelByGrowValue
	 *  @param growValue
	 *  @return         : Integer
	 *  @Creation Date  : 2017年07月05日 上午11:21:05
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	Integer findLevelByGrowValue(Integer growValue);

	/**
	 *  @Description    : 保存会员等级标准记录
	 *  @Method_Name    : save
	 *  @param vasVipGradeStandard
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 上午10:24:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity addVipGradeStandard(VasVipGradeStandard vasVipGradeStandard);

	/**
	 *  @Description    : 更新会员等级标准记录
	 *  @Method_Name    : update
	 *  @param vasVipGradeStandard
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2017年6月30日 上午10:45:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity updateVipGradeStandard(VasVipGradeStandard vasVipGradeStandard);

	/**
	 *  @Description    : 获取所有的会员等级标准
	 *  @Method_Name    : getAllGradeList
	 *  @return         : List<VasVipGradeStandard>
	 *  @Creation Date  : 2018年03月12日 下午16:45:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	List<VasVipGradeStandard> getAllGradeList();
}
