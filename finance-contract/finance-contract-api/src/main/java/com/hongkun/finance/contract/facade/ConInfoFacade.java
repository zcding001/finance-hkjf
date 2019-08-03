package com.hongkun.finance.contract.facade;

import com.hongkun.finance.contract.model.ConInfo;
import com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;

import java.util.List;

/**
 * @Description : 合同信息facade层
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.facade.ConInfoFacade
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public interface ConInfoFacade {

    /**
     *  根据投资记录集合生成合同
     *  @Description    : 根据投资记录集合生成合同
     *  @Method_Name    : generateContract
     *  @param list       投资记录id集合
     *  @Creation Date  : 2018年01月11日 下午16:05:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    void generateContract(List<Integer> list);

    /**
     *  根据投资记录和合同类型
     *  @Description    : 根据投资记录和合同类型
     *  @Method_Name    : generateContract
     *  @param list               投资记录id集合
     *  @param contractType       合同类型
     *  @Creation Date  : 2018年01月11日 下午16:05:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    void generateContract(List<Integer> list, Integer contractType);

    /**
     *  获取合同展示模板
     *  @Description    : 获取合同展示模板
     *  @Method_Name    : showContractTemplate
     *  @param contractType       合同类型
     *  @return  String
     *  @Creation Date  : 2018年01月11日 下午16:05:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    String showContractTemplate(int contractType);

    /**
     *  获取合同模板信息
     *  @Description    : 获取合同模板信息
     *  @Method_Name    : findContractTemplateDetail
     *  @param id            合同信息条件
     *  @return  String
     *  @Creation Date  : 2018年01月11日 下午16:05:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    ResponseEntity<?> findContractTemplateDetail(int id);

    /**
     *  @Description    ：获取合同展示内容
     *  @Method_Name    ：getContractContent
     *  @param regUser  用户信息
     *  @param contractType  合同类型
     *  @param location  查看合同位置，1-标地信息、2-投资记录、3-还款计划
     *  @param bidInvestId  投资记录Id
     *  @param bidId  标地Id
     *  @return java.lang.String
     *  @Creation Date  ：2018/5/9
     *  @Author         ：pengwu@hongkun.com.cn
     */
    String getContractContent(RegUser regUser, int contractType, int location, Integer bidInvestId, Integer bidId);

    /**
     *  @Description    ：获取优选标匹配的散标投资记录对应的合同信息，查看源项目使用
     *  @Method_Name    ：findGoodInvestMatchContractInfo
     *  @param regUserId  用户id
     *  @param bidId  标地id
     *  @param investId  投资记录id
     *  @return java.util.List<com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO>
     *  @Creation Date  ：2018/6/1
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<CommonInvestConInfoVO> findGoodInvestMatchContractInfo(Integer regUserId, Integer bidId, Integer investId);

    /**
     *  @Description    ：获取借款人合同列表信息
     *  @Method_Name    ：findBorrowerContractInfo
     *  @param regUserId  用户id
     *  @param bidId  标地id
     *  @return java.util.List<com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO>
     *  @Creation Date  ：2018/6/4
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<CommonInvestConInfoVO> findBorrowerContractInfo(Integer regUserId, Integer bidId);
}
