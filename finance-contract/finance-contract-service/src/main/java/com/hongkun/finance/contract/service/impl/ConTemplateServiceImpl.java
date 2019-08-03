package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.dao.ConTemplateDao;
import com.hongkun.finance.contract.model.ConTemplate;
import com.hongkun.finance.contract.service.ConTemplateService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.impl.ConTemplateServiceImpl.java
 * @Class Name    : ConTemplateServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class ConTemplateServiceImpl implements ConTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(ConTemplateServiceImpl.class);
	
	/**
	 * ConTemplateDAO
	 */
	@Autowired
	private ConTemplateDao conTemplateDao;
	
	@Override
	public List<ConTemplate> findConTemplateList(ConTemplate conTemplate) {
		return this.conTemplateDao.findByCondition(conTemplate);
	}

	@Override
	public Pager findConTemplateList(ConTemplate conTemplate, Pager pager) {
		return this.conTemplateDao.findByCondition(conTemplate, pager);
	}

	@Override
	public ConTemplate findConTemplateById(int id) {
		return this.conTemplateDao.findByPK(Long.valueOf(id), ConTemplate.class);
	}

	@Override
	public ResponseEntity addContractTemplate(Integer userId, ConTemplate conTemplate) {
		try{
			conTemplate.setCreateUserId(userId);
			conTemplate.setModifyUserId(userId);
			//初始状态为不启用
			conTemplate.setState(ContractConstants.CONTRACT_STATE_N);
			if (this.conTemplateDao.save(conTemplate) > 0){
				return new ResponseEntity(SUCCESS,"添加合同内容模板成功");
			}
		}catch (Exception e){
			logger.error("addContractTemplate, 添加合同内容模板异常, 用户标识: {}, 合同内容模板信息: {}, 异常信息: ",
					userId, conTemplate.toString(), e);
			throw new GeneralException("添加合同内容模板异常！");
		}
        return new ResponseEntity(ERROR,"添加合同内容模板失败");
	}

	@Override
	public ResponseEntity updateContractTemplate(Integer userId, ConTemplate conTemplate) {
		try{
			if (conTemplate == null || conTemplate.getId() == null ||
					this.findConTemplateById(conTemplate.getId()) == null){
				return new ResponseEntity(ERROR,"请选择正确的合同内容模板进行修改");
			}
			//只更新合同模板内容
			ConTemplate updateCon = new ConTemplate();
			updateCon.setId(conTemplate.getId());
			updateCon.setModifyUserId(userId);
			updateCon.setModifyTime(new Date());
			updateCon.setContent(conTemplate.getContent());
			if (this.conTemplateDao.update(updateCon) > 0){
				return new ResponseEntity(SUCCESS,"修改合同内容模板成功");
			}
		}catch (Exception e){
			logger.error("updateContractTemplate, 更新合同内容模板异常, 用户标识: {}, 合同内容模板信息: {}, 异常信息: ",
					userId, conTemplate.toString(), e);
			throw new GeneralException("更新合同内容模板异常！");
		}
        return new ResponseEntity(ERROR,"修改合同内容模板失败");
	}

	@Override
	public ResponseEntity<?> disableContractTemplate(Integer userId, int id) {
		ConTemplate conTemplate = this.findConTemplateById(id);
		if (conTemplate == null){
			return new ResponseEntity(ERROR,"请选择正确的合同内容模板进行操作");
		}
		try {
			ConTemplate updateCon = new ConTemplate();
			updateCon.setId(id);
			updateCon.setState(ContractConstants.CONTRACT_STATE_N);
			updateCon.setModifyUserId(userId);
			updateCon.setModifyTime(new Date());
			if (this.conTemplateDao.update(updateCon) > 0){
				return new ResponseEntity(SUCCESS,"禁用合同内容模板成功");
			}
		}catch (Exception e){
			logger.error("disableContractTemplate, 禁用合同内容模板异常, 用户标识: {}, 合同模板信息: {}, 异常信息: ",
					userId, conTemplate.toString(), e);
			throw new GeneralException("禁用合同内容模板异常！");
		}
		return new ResponseEntity<>(ERROR,"禁用合同内容模板失败");
	}

	@Override
	public ResponseEntity enableContractTemplate(Integer userId, int id) {
		ConTemplate conTemplate = this.findConTemplateById(id);
		if (conTemplate == null){
			return new ResponseEntity(ERROR,"请选择正确的合同内容模板进行操作");
		}
		try{
			//获取该合同类型已启用的合同模板
			ConTemplate condition = new ConTemplate();
			condition.setState(ContractConstants.CONTRACT_STATE_Y);
			condition.setContractType(conTemplate.getContractType());
			List<ConTemplate> contemplateList = this.findConTemplateList(condition);
			//若该合同类型已存在已启用的合同模板，将该合同模板列表全部禁用
			if (contemplateList != null && contemplateList.size() > 0){
				for (ConTemplate enableConTemplate:contemplateList){
					ConTemplate updateCon = new ConTemplate();
					updateCon.setId(enableConTemplate.getId());
					updateCon.setState(ContractConstants.CONTRACT_STATE_N);
					updateCon.setModifyUserId(userId);
					updateCon.setModifyTime(new Date());
					if (!(this.conTemplateDao.update(updateCon) > 0)){
						return new ResponseEntity(ERROR,"禁用已有的合同内容模板失败");
					}
				}
			}
			//更新该合同模板为启用状态
			ConTemplate updateContemplate = new ConTemplate();
			updateContemplate.setState(ContractConstants.CONTRACT_STATE_Y);
			updateContemplate.setId(id);
			updateContemplate.setModifyUserId(userId);
			updateContemplate.setModifyTime(new Date());
			if (this.conTemplateDao.update(updateContemplate) > 0){
				return new ResponseEntity(SUCCESS,"修改合同内容模板成功");
			}
		}catch (Exception e){
			logger.error("enableContractTemplate, 启用合同内容模板异常, 用户标识: {}, 合同内容模板信息: {}, 异常信息: ",
					userId, conTemplate.toString(), e);
			throw new GeneralException("启用合同内容模板异常！");
		}
        return new ResponseEntity(ERROR,"修改合同内容模板失败");
	}

	@Override
	public ConTemplate findConTemplateByType(Integer contractType) {
		return this.conTemplateDao.findConTemplateByType(contractType);
	}

	@Override
	public Map<Integer, ConTemplate> findEnableConTemplateList() {
		return this.conTemplateDao.findEnableConTemplateList();
	}
}
