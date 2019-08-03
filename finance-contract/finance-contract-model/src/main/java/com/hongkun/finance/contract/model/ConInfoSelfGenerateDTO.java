package com.hongkun.finance.contract.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 用于自定义生成合同类型数据对象
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class ConInfoSelfGenerateDTO implements Serializable {

    private List<Integer> investIdList;

    private Integer contractType;

    public List<Integer> getInvestIdList() {
        return investIdList;
    }

    public void setInvestIdList(List<Integer> investIdList) {
        this.investIdList = investIdList;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }
}
