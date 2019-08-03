package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.dao.ConTypeDao;
import com.hongkun.finance.contract.model.ConType;
import com.hongkun.finance.contract.service.ConTypeService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.impl.ConTypeServiceImpl.java
 * @Class Name    : ConTypeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class ConTypeServiceImpl implements ConTypeService {

	private static final Logger logger = LoggerFactory.getLogger(ConTypeServiceImpl.class);
	
	/**
	 * ConTypeDAO
	 */
	@Autowired
	private ConTypeDao conTypeDao;

	@Override
	public List<ConType> findConTypeList(ConType conType) {
		return this.conTypeDao.findByCondition(conType);
	}

	@Override
	public Pager findConTypeList(ConType conType, Pager pager) {
		return this.conTypeDao.findByCondition(conType, pager);
	}

	@Override
	public ConType findConTypeById(int id) {
		return this.conTypeDao.findByPK(Long.valueOf(id), ConType.class);
	}

	@Override
	public Integer findConTypeCount(ConType condition) {
		return this.conTypeDao.getTotalCount(condition);
	}

	@Override
	public List<Map<String, Object>> getContractTypeAndName() {
		List<Map<String,Object>> contractTypeAndNameList = new ArrayList<>();
        ConType conType = new ConType();
        conType.setSortColumns("type");
        List<ConType> list = this.findConTypeList(conType);
        list.stream().forEach(ct -> {
            Map<String,Object> contractTypeAndName = new HashMap<>();
            contractTypeAndName.put("type",ct.getType());
            contractTypeAndName.put("name",ct.getName());
            contractTypeAndName.put("showName",ct.getShowName());
            contractTypeAndName.put("state",ct.getState());
            contractTypeAndNameList.add(contractTypeAndName);
        });
        return contractTypeAndNameList;
	}

	@Override
	public ResponseEntity addContractType(Integer userId, ConType conType) {
		try{
			//判断该合同类型的数据是否已经存在，防止合同类型重复
			ConType condition = new ConType();
			condition.setType(conType.getType());
			if (this.findConTypeCount(condition) > 0){
				return new ResponseEntity(ERROR,"合同类型重复，请勿重复添加合同类型!");
			}
			conType.setCreateUserId(userId);
			conType.setModifyUserId(userId);
			//初始状态为不启用
			conType.setState(ContractConstants.CONTRACT_STATE_N);

			if (this.conTypeDao.save(conType) > 0){
				return new ResponseEntity(SUCCESS,"添加合同类型成功");
			}
		}catch (Exception e){
			logger.error("addContractType, 添加合同类型异常, 用户标识: {}, 合同类型信息: {}, 异常信息: ", userId, conType.toString(), e);
			throw new GeneralException("添加合同类型异常！");
		}
        return new ResponseEntity(ERROR,"添加合同类型失败");
	}

	@Override
	public ResponseEntity updateContractType(Integer userId, ConType conType) {
		try{
			if (conType == null || conType.getId() == null ||
					this.findConTypeById(conType.getId()) == null){
				return new ResponseEntity(ERROR,"请选择正确的合同类型记录进行修改");
			}
			conType.setModifyUserId(userId);
			conType.setModifyTime(new Date());
			if (this.conTypeDao.update(conType) > 0){
				return new ResponseEntity(SUCCESS,"修改合同类型成功");
			}
		}catch (Exception e){
			logger.error("updateContractType, 更新合同类型异常, 用户标识: {}, 合同类型信息: {}, 异常信息: ", userId, conType.toString(), e);
			throw new GeneralException("更新合同类型异常！");
		}
        return new ResponseEntity(ERROR,"修改合同类型失败");
	}


    @Override
    public List<Map<String, Object>> findContractInfo(String contracts) {
        List<Map<String, Object>> contractList = new ArrayList<>();
        if(StringUtils.isNotBlank(contracts)) {
            ConType condition = new ConType();
            condition.setState(ContractConstants.CONTRACT_STATE_Y);
            List<ConType> conTypeList = this.conTypeDao.findByCondition(condition);
            if(CommonUtils.isNotEmpty(conTypeList)) {
                Arrays.asList(contracts.split(",")).stream().forEach(c -> {
                    conTypeList.stream().filter(o -> o.getType().toString().equals(c)).findFirst().ifPresent(d -> {
                        contractList.add(new HashMap<String, Object>() {{
                            put("type", c);
                            put("text", d.getShowName());
                        }});
                    });
                });
            }
        }
        return contractList;
    }
}
