package com.hongkun.finance.vas.model.vo;

import com.hongkun.finance.vas.model.VasCouponDonationRecord;

/**
 * @Description : 卡券转赠记录VO
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.model.vo.VasCouponDonationRecordVO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasCouponDonationRecordVO extends VasCouponDonationRecord {
    /**卡券发送人名称**/
    private String sendUserName;

    /**卡券发送人手机号**/
    private Long sendUserTel;

    /**卡券接收人名称**/
    private String receiveUserName;

    /**卡券接收人手机号**/
    private Long receiveUserTel;

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public Long getSendUserTel() {
        return sendUserTel;
    }

    public void setSendUserTel(Long sendUserTel) {
        this.sendUserTel = sendUserTel;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public Long getReceiveUserTel() {
        return receiveUserTel;
    }

    public void setReceiveUserTel(Long receiveUserTel) {
        this.receiveUserTel = receiveUserTel;
    }

    @Override
    public String toString() {
        return "VasCouponDonationRecordVO{" +
                "sendUserName='" + sendUserName + '\'' +
                ", sendUserTel=" + sendUserTel +
                ", receiveUserName='" + receiveUserName + '\'' +
                ", receiveUserTel=" + receiveUserTel +
                ", systemName='" + systemName + '\'' +
                "} " + super.toString();
    }
}
