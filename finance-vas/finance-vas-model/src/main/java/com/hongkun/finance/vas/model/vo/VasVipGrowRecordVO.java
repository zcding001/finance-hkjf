package com.hongkun.finance.vas.model.vo;

import com.hongkun.finance.vas.model.VasVipGrowRecord;

/**
 * @Description : 会员等级成长记录展示VO
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.model.vo.VasVipGrowRecordVO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasVipGrowRecordVO extends VasVipGrowRecord {

    /**用户手机号*/
    private Long userTel;

    /**用户名称*/
    private String userName;

    /**用户等级*/
    private Integer level;

    public Long getUserTel() {
        return userTel;
    }

    public void setUserTel(Long userTel) {
        this.userTel = userTel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
