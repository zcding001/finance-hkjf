package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.dto.RedpacketDTO;
import com.hongkun.finance.vas.model.vo.RedPacketVO;
import com.hongkun.finance.vas.model.vo.VasRedpacketVO;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.service.VasRedpacketInfoService.java
 * @Class Name    : VasRedpacketInfoService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasRedpacketInfoService {

    /**
     * @param vasRedpacketInfo 要更新的数据
     * @return : void
     * @Described : 更新数据
     */
    int updateVasRedpacketInfo(VasRedpacketInfo vasRedpacketInfo);



    /**
     * @param id id值
     * @return VasRedpacketInfo
     * @Described : 通过id查询数据
     */
    VasRedpacketInfo findVasRedpacketInfoById(int id);

    /**
     * @param vasRedpacketInfo 检索条件
     * @return List<VasRedpacketInfo>
     * @Described : 条件检索数据
     */
    List<VasRedpacketInfo> findVasRedpacketInfoList(VasRedpacketInfo vasRedpacketInfo);



    /**
     * @param vasRedpacketInfo 检索条件
     * @return int
     * @Described : 统计条数
     */
    int findVasRedpacketInfoCount(VasRedpacketInfo vasRedpacketInfo);



    /**
     *  @Description    : 根据条件查询分页结果
     *  @Method_Name    : findVasRedpacketInfoList
     * @param sendUserIds   : 发送用户id集合
     * @param acceptorUserIds   : 接受用户的id集合
     * @param vasRedpacketVO   : 红包信息
     * @param pager   : 分页信息
     *  @return          : Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager findVasRedpacketInfoList(List<Integer> sendUserIds, List<Integer> acceptorUserIds, VasRedpacketVO vasRedpacketVO, Pager pager);


    /**
     *  @Description    : 获取红包总金额
     *  @Method_Name    : findVasRedpacketInfoList
     * @param sendUserIds   : 发送用户id集合
     * @param acceptorUserIds   : 接受用户的id集合
     * @param vasRedpacketVO   : 红包信息
     * @param pager   : 分页信息
     *  @return          : Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    BigDecimal findVasRedpacketTotalNum(List<Integer> sendUserIds, List<Integer> acceptorUserIds, VasRedpacketVO vasRedpacketVO, Pager pager);

    /**
     *  @Description    : 生成线下红包
     *  @Method_Name    : produceRedpacket
     *  @param redpacketDTO   : 红包信息
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<VasRedpacketInfo> produceRedpacket(RedpacketDTO redpacketDTO);


    /**
     *  @Description    : 给用户派发红包
     *  @Method_Name    : distributeRedPacketToUser
     * @param redpacketDTO   : 红包信息
     * @param source      : 发送来源
     *  @return          : List<Integer>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<VasRedpacketInfo> distributeRedPacketToUser(RedpacketDTO redpacketDTO, Integer source);


    /**
     *  @Description    : 更改红包的状态
     *  @Method_Name    : changeRedPacketStateForUnCheckPacket
     * @param redpacketDTO   : 红包信息
     *  @return          : List<VasRedpacketInfo>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<VasRedpacketInfo> changeRedPacketStateForUnCheckPacket(RedpacketDTO redpacketDTO);

    /**
     * @param vasRedpacketInfo   红包条件
     * @param pager  	            分页参数
     * @return :
     * @Description : 获取用户红包数据集合
     * @Method_Name : findVasRedpacketInfoList
     * @Creation Date : 2017年12月18日 上午10:51:50
     * @Author : pengwu@hongkun.com.cn
     */
    Pager findVasRedpacketInfoList(VasRedpacketInfo vasRedpacketInfo, Pager pager);

    /**
     * @param userId  用户id(必填)
     * @param pager   分页参数(必填)
     * @return : ResponseEntity
     * @Description : 获取当前用户已领取的红包记录
     * @Method_Name : getUserRedPacketInfo
     * @Creation Date  : 2018年01月11日 下午15:22:50
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity getUserRedPacketInfo(Integer userId,Pager pager);


   /**
     *  @Description    : 删除红包
     *  @Method_Name    : deleteRedPacket
     * @param redpacketDTO   : 红包信息
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity deleteRedPacket(RedpacketDTO redpacketDTO);

    /**
     * @param regUserId     用户id(必填)
     * @param state         状态:0-未领取，1-已领取，2-已过期，4-待审核，5-财务拒绝，9-失效, 10-已删除(后台不展示)
     * @param pager          分页参数(必填)
     * @return : ResponseEntity
     * @Description : 获取当前用户红包记录
     * @Method_Name : getRedPacketInfoList
     * @Creation Date  : 2018年03月09日 下午15:45:50
     * @Author : pengwu@hongkun.com.cn
     */
    ResponseEntity getRedPacketInfoList(Integer regUserId, int state, Pager pager);

    /**
     * @param regUserId     用户id(必填)
     * @return : ResponseEntity
     * @Description : 获取当前用户可领取红包记录数量
     * @Method_Name : getUnReceiveRedPacketCount
     * @Creation Date  : 2018年03月09日 下午17:19:50
     * @Author : pengwu@hongkun.com.cn
     */
    int getUnReceiveRedPacketCount(Integer regUserId);

    /**
     *  @Description    ：根据红包key获取红包信息
     *  @Method_Name    ：findVasRedpacketInfoByKey
     *  @param key
     *  @return com.hongkun.finance.vas.model.VasRedpacketInfo
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    VasRedpacketInfo findVasRedpacketInfoByKey(String key);

    /**
    *  @Description    ：找到对于当前日期已经失效的红包信息
    *  @Method_Name    ：findInvalidRedPackets
    *  @param currentDate
    *  @return java.util.List<com.hongkun.finance.vas.model.VasRedpacketInfo>
    *  @Creation Date  ：2018/4/27
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<VasRedpacketInfo> findInvalidRedPackets(Date currentDate);

    /**
    *  @Description    ：批量失效红包
    *  @Method_Name    ：updateVasRedpacketInfoBetch
    *  @param invalidRedPackets
    *  @return void
    *  @Creation Date  ：2018/4/27
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    void updateVasRedpacketInfoBetch(List<VasRedpacketInfo> invalidRedPackets);

    /**
     *  @Description    ：红包兑换
     *  @Method_Name    ：exchangeRedPacketInfo
     *  @param updateRedPacket 兑换的红包信息
     *  @return int
     *  @Creation Date  ：2018/5/21
     *  @Author         ：pengwu@hongkun.com.cn
     */
    int exchangeRedPacketInfo(VasRedpacketInfo updateRedPacket);
    /**
     *  @Description    : 查询领取红包人数
     *  @Method_Name    : findRedPacketUserSum;
     *  @param updateRedPacket
     *  @return
     *  @return         : RedPacketVO;
     *  @Creation Date  : 2018年9月21日 下午1:44:54;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    RedPacketVO findRedPacketUserSum (VasRedpacketInfo updateRedPacket);

    /**
     *  @Description    ：插入红包信息
     *  @Method_Name    ：insert
     *  @param vasRedpacketInfo
     *  @return String
     *  @Creation Date  ：2018年10月17日 15:11
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    VasRedpacketInfo insert(VasRedpacketInfo vasRedpacketInfo);
}
