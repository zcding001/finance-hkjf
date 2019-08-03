package com.hongkun.finance.contract.service;

import com.hongkun.finance.contract.model.ConType;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.service.ConTypeService.java
 * @Class Name    : ConTypeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface ConTypeService {

    /**
     * @Described			: 条件检索数据
     * @param conType 检索条件
     * @return	List<ConType>
     */
    List<ConType> findConTypeList(ConType conType);

    /**
     * @Described			: 条件检索数据
     * @param conType 检索条件
     * @param pager	分页数据
     * @return	Pager
     */
    Pager findConTypeList(ConType conType, Pager pager);

    /**
     * @Described			: 通过id查询数据
     * @param id id值
     * @return	ConType
     */
    ConType findConTypeById(int id);


    /**
     * @Described			: 通过条件查询合同类型数量
     * @param condition   id值
     * @return	ConType
     */
    Integer findConTypeCount(ConType condition);

    /**
     *  @Description    : 获取合同的数据字典信息
     *  @Method_Name    : getContractTypeAndName
     *  @return
     *  @Creation Date  : 2018年01月11日 下午17:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    List<Map<String,Object>> getContractTypeAndName();

    /**
     *  @Description    : 添加合同类型记录
     *  @Method_Name    : addContractType
     *  @param userId
     *  @param conType
     *  @return
     *  @Creation Date  : 2018年01月11日 下午5:51:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    ResponseEntity addContractType(Integer userId, ConType conType);

    /**
     *  @Description    : 修改合同类型记录
     *  @Method_Name    : updateContractType
     *  @param userId
     *  @param conType
     *  @return
     *  @Creation Date  : 2017年6月28日 上午10:05:10
     *  @Author         : pengwu@hongkun.com.cn
     */
    ResponseEntity updateContractType(Integer userId, ConType conType);
    
    /**
    *  合同的类型集合
    *  @Method_Name             ：findContractInfo
    *  @param contracts
    *  @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    *  @Creation Date           ：2018/5/31
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    List<Map<String, Object>> findContractInfo(String contracts);
}
