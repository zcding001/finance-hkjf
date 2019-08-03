package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @Description : 后台保存房产数据用于数据传输的对象
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.fund.model.HouseDTO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class HouseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public HouseDTO(){

    }
    /**
     * 房产预售证记录集合
     */
    private String permitList;

    /**
     * 房产信息图片记录集合
     */
    private String picList;

    public String getPermitList() {
        return permitList;
    }

    public void setPermitList(String permitList) {
        this.permitList = permitList;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
