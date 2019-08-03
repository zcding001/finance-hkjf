package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 自动投资优先级vo
 * @Program: com.hongkun.finance.invest.model.vo.InvestPriorityVo
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-29 10:03
 **/
public class InvestPriorityVo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer priority;
    private List<BidInfoVO> list = new ArrayList<>();

    public InvestPriorityVo(Integer priority, List<BidInfoVO> list) {
        this.priority = priority;
        this.list = list;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<BidInfoVO> getList() {
        return list;
    }

    public void setList(List<BidInfoVO> list) {
        this.list = list;
    }
}
