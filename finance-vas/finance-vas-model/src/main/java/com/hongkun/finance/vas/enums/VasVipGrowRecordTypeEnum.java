package com.hongkun.finance.vas.enums;

import static java.lang.System.out;

/**
 * @Description : 会员成长值类型枚举类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.enums.VasVipGrowRecordTypeEnum
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public enum VasVipGrowRecordTypeEnum {
    INVEST(1,"投资"),FRIENDREGIST(2,"邀请好友注册"),SIGN(3,"签到"),FIRSTRECHARGE(4,"首次充值"),POINTEXCHANGE(5,"积分兑换"),
    FRIENDINVEST(6,"邀请好友投资"),POINTPAYMENT(7,"积分支付"),POINTTRANSFER(8,"积分转赠"),PLATGIVE(9,"平台赠送"),VIPDOWNGRADE(10,"会员降级");

    private int type;

    private String note;

    VasVipGrowRecordTypeEnum(int type, String note){
        this.type = type;
        this.note = note;
    }

    public static String getNote(int type){
        for(VasVipGrowRecordTypeEnum c : VasVipGrowRecordTypeEnum.values()){
            if (c.type == type){
                return c.note;
            }
        }
        return "";
    }

    @Override
    public String toString(){
        return "type：" + type + "；" + "note：" + note;
    }
}
