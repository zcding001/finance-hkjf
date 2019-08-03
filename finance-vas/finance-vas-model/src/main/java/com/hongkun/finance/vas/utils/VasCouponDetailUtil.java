package com.hongkun.finance.vas.utils;

import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.yirun.framework.core.utils.DateUtils;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

/**
 * @Description : TODO wupeng 类描述
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.utils.VasCouponDetailUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasCouponDetailUtil {

    /**
     * @param product  卡券产品
     * @param state   状态
     * @param source  设置生成来源
     * @param reason  发送原因
     * @param userId  接收卡券用户id
     * @return : VasCouponDetail
     * @Description : 组装卡券数据
     * @Method_Name : assembleCouponDetail
     * @Creation Date : 2017年07月10日 上午11:54:50
     * @Author : pengwu@hongkun.com.cn
     */
    public static VasCouponDetail assembleCouponDetail(VasCouponProduct product, Integer state, Integer source,
                                                 String reason, Integer userId) {
        VasCouponDetail detail = new VasCouponDetail();
        Date currentDate = new Date();
        detail.setCreateTime(currentDate);
        detail.setModifyTime(currentDate);
        // 状态为已生成
        detail.setState(state);
        detail.setCouponProductId(product.getId());
        // 设置产品范围
        detail.setBidProductTypeRange(product.getBidProductTypeRange());
        detail.setWorth(product.getWorth());
        // 设置生成来源
        detail.setSource(source);
        // 设置卡券领取key
        detail.setAvtKey(getAvtKey(product.getType()));
        // 设置接收卡券用户
        detail.setAcceptorUserId(userId);
        // 设置生成卡券原因
        detail.setReason(reason);
        if (product.getDeadlineType() == 0) {
            // 截止日期类型过期天数
            String effectiveStrTime = DateUtils.format(currentDate);
            String effectiveEndTime = DateUtils.format(DateUtils.addDays(currentDate, product.getValidDay() - 1));
            detail.setBeginTime(DateUtils.parseDate(effectiveStrTime, "00:00:00"));
            detail.setEndTime(DateUtils.parseDate(effectiveEndTime, "23:59:59"));
        } else {
            // 截止日期类型过时间范围
            detail.setBeginTime(product.getBeginTime());
            detail.setEndTime(product.getEndTime());
        }
        return detail;
    }

    /**
     * @param type 卡券类型
     * @return : String
     * @Description : 生成卡券的key
     * @Method_Name : getAvtKey
     * @Creation Date : 2017年07月10日 下午11:35:50
     * @Author : pengwu@hongkun.com.cn
     */
    public static String getAvtKey(int type) {
        if (type == VasCouponConstants.COUPON_PRODUCT_TYPE_RATE_COUPON) {
            // 加息券
            return "J-" + get13UUID().toUpperCase();
        } else {
            // 投资红包
            return "K-" + get13UUID().toUpperCase();
        }
    }

    /**
     * @return : String
     * @Description : 获取13位长度的UUID
     * @Method_Name : get13UUID
     * @Creation Date : 2017年07月10日 上午11:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    private static String get13UUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

}
