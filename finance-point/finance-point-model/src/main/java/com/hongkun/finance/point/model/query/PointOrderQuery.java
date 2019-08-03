package com.hongkun.finance.point.model.query;

import com.hongkun.finance.point.model.PointProductOrder;
import com.yirun.framework.core.annotation.Union;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

/**
 * 积分订单的查询条件
 */
public class PointOrderQuery  extends PointProductOrder{

    /**
     * 用户姓名
     */
    @Union(forLimitQuery = true)
    private String realName;

    /**
     * 用户电话
     */
    @Union(forLimitQuery = true)
    private Long login;

    /**
     * 查询限制id
     */
    private List<Integer> limitIds;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public List<Integer> getLimitIds() {
        return limitIds;
    }

    public void setLimitIds(List<Integer> limitIds) {
        this.limitIds = limitIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
