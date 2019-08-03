package com.hongkun.finance.user.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 线下门店的数据
 */
public class OfflineStoreVO implements Serializable{

    /**
     * 物业名称
     */
    private String tenementName;
    /**
     * 线下门店名称
     */
    private List<String> offlineStoreName;

    public OfflineStoreVO() {
    }

    public String getTenementName() {
        return tenementName;
    }

    public void setTenementName(String tenementName) {
        this.tenementName = tenementName;
    }

    public List<String> getOfflineStoreName() {
        return offlineStoreName;
    }

    public void setOfflineStoreName(List<String> offlineStoreName) {
        this.offlineStoreName = offlineStoreName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
