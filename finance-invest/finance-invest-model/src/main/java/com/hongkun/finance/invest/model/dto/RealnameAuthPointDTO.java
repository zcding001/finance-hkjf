package com.hongkun.finance.invest.model.dto;

import com.hongkun.finance.point.model.PointProcessorKeys;
import com.yirun.framework.jms.KeyAssignedAware;

import java.io.Serializable;

/**
 * @Description :用于传递投资送积分的数据DTO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.consumer.PointKeys
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class RealnameAuthPointDTO implements KeyAssignedAware,Serializable {

    private static final long serialVersionUID = 7350537051632310117L;

    private Integer userId;

    private Integer  point;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public String getAssignedKey() {
        return PointProcessorKeys.REAL_NAME_AUTH_POINTPROCESSOR;
    }
}
