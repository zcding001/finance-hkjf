package com.hongkun.finance.vas.facade;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.Date;

/**
 * @Description :
 * @Project : finance
 * @Program Name  : com.hongkun.finance.vas.facade.VasRedpacketFacade
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

public interface VasRedpacketFacade {

    /**
     * 查询线下红包的分页数据
     * @param vasRedpacketVO
     * @param pager
     * @return
     */
    ResponseEntity listPage(VasRedpacketVO vasRedpacketVO, Pager pager);


    /**
     * 生成或者派发红包
     * @param redpacketDTO
     * @param type 派发红包的类型，比如线下生成，运营派发，个人派发等等
     * @return
     */
    ResponseEntity distributeRedpacket(RedpacketDTO redpacketDTO,Integer type,PlatformSourceEnums platformSourceEnums);

    /**
     * 审核红包
     * @param redpacketDTO
     * @return
     */
    ResponseEntity changeRedPacketState(RedpacketDTO redpacketDTO,PlatformSourceEnums platformSourceEnums);

    /**
     * @param userId  用户id
     * @param key     红包兑换码
     * @param platformSourceEnums     平台来源枚举类
     * @return : ResponseEntity
     * @Description : 用户兑换红包
     * @Method_Name : exchangeRedPacketInfo
     * @Creation Date  : 2017年12月18日 下午14:30:50
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity exchangeRedPacketInfo(Integer userId, String key,PlatformSourceEnums platformSourceEnums);

    /**
     * @param userId  用户id
     * @param pager   分页参数
     * @return : ResponseEntity
     * @Description : 获取用户已发送红包记录
     * @Method_Name : getUserSendOutRedPacketInfo
     * @Creation Date  : 2017年01月11日 上午09:36:50
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity getUserSendOutRedPacketInfo(Integer userId, Pager pager);

    /**
    *  @Description    ：失效红包操作
    *  @Method_Name    ：invalidateRedPacketOverTime
    *  @param currentDate
    *  @param shardingItem
    *  @return void
    *  @Creation Date  ：2018/4/27
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    void invalidateRedPacketOverTime(Date currentDate, int shardingItem);

    /**
     *  @Description    ：导入红包操作
     *  @Method_Name    ：importRedPackets
     *  @param filePath  导入红包记录excel路径
     *  @param currentUserId  当前登录用户id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/9/14
     *  @Author         ：pengwu@hongkun.com.cn
     */
    ResponseEntity<?> importRedPackets(String filePath,Integer currentUserId);
}
