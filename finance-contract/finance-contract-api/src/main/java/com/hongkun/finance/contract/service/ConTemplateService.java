package com.hongkun.finance.contract.service;

import com.hongkun.finance.contract.model.ConTemplate;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.ConTemplateService.java
 * @Class Name    : ConTemplateService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface ConTemplateService {
	
	/**
	 * @Described			: 条件检索数据
	 * @param conTemplate 检索条件
	 * @return	List<ConTemplate>
	 */
	List<ConTemplate> findConTemplateList(ConTemplate conTemplate);

	/**
	 * @Described			: 条件检索数据
	 * @param conTemplate 检索条件
	 * @param pager	分页数据
	 * @return	Pager
	 */
	Pager findConTemplateList(ConTemplate conTemplate, Pager pager);

	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	ConTemplate
	 */
	ConTemplate findConTemplateById(int id);

	/**
	 *  @Description    : 添加合同内容模板记录
	 *  @Method_Name    : addContractTemplate
	 *  @param userId
	 *  @param conTemplate
	 *  @return
	 *  @Creation Date  : 2017年6月28日 上午11:13:50
	 *  @Author         : pengwu@hongkun.com.cn
	 */
    ResponseEntity addContractTemplate(Integer userId, ConTemplate conTemplate);

	/**
	 *  @Description    : 修改合同内容模板记录
	 *  @Method_Name    : updateContractTemplate
	 *  @param userId
	 *  @param conTemplate
	 *  @return
	 *  @Creation Date  : 2017年6月28日 上午10:15:30
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity updateContractTemplate(Integer userId, ConTemplate conTemplate);

	/**
	 *  @Description    : 启用合同模板
	 *  @Method_Name    : enableContractTemplate
	 *  @param userId
	 *  @param id
	 *  @return
	 *  @Creation Date  : 2017年10月24日 上午09:42:30
	 *  @Author         : pengwu@hongkun.com.cn
	 */
	ResponseEntity enableContractTemplate(Integer userId, int id);

	/**
	 *  @Description    ：获取当前合同类型正启用的模板
	 *  @Method_Name    ：findConTemplateByType
	 *  @param contractType  合同类型
	 *  @return com.hongkun.finance.contract.model.ConTemplate
	 *  @Creation Date  ：2018/4/17
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	ConTemplate findConTemplateByType(Integer contractType);

	/**
	 *  @Description    ：获取所有已启用的合同模板信息，key为合同类型type，value为ConTemplate合同模板信息
	 *  @Method_Name    ：findEnableConTemplateList
	 *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.contract.model.ConTemplate>
	 *  @Creation Date  ：2018/4/18
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    Map<Integer,ConTemplate> findEnableConTemplateList();

    /**
     *  @Description    ：禁用合同模板
     *  @Method_Name    ：disableContractTemplate
     *  @param userId  用户id
     *  @param id  合同模板id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/5/10
     *  @Author         ：pengwu@hongkun.com.cn
     */

    ResponseEntity<?> disableContractTemplate(Integer userId, int id);
}
