package com.hongkun.finance.vas.model.dto;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;

/**
 * @Description : 封装生成红包相关前端参数的描述类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.vas.model.dto.RedpacketDTO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class RedpacketDTO extends BaseModel {
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = {ProduceRedpacket.class})
    @Min(value = 0, message = "最小值不能0", groups = {ProduceRedpacket.class})
    private Integer num;
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空", groups = {ProduceRedpacket.class, DistributeRedPacketToUser.class})
    private BigDecimal value;

    /**
     * 描述: 红包名称
     * 字段: name  VARCHAR(50)
     * 默认值: ''
     */
    private String name;
    /**
     * 截止日期
     */
    @NotNull(message = "截止日期不能为空", groups = {ProduceRedpacket.class, DistributeRedPacketToUser.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 目标用户id
     */
    @NotEmpty(message = "请至少指定一个用户", groups = {DistributeRedPacketToUser.class})
    private HashSet<Integer> userIds;

    /**
     * 描述: 红包类型
     * 字段: type  TINYINT(3)
     * 默认值: 0
     */
    private Integer redPacketType;
    /**
     * 发送原因
     */
    @NotNull(message = "请输入发送原因", groups = {DistributeRedPacketToUser.class})
    private String sendReason;

    /**审核id*/
    @NotNull(message = "请至少指定一个记录", groups = {CheckRedPacket.class})
    private String checkId;
    /**状态*/
    @NotNull(message = "请指定审核状态", groups = {CheckRedPacket.class})
    @Union(changeAble = false)/*防止被loginUser覆盖*/
    private Integer state;
    /**审核原因*/
    @NotNull(message = "请输入审核原因", groups = {CheckRedPacket.class})
    private String reason;

    /**
     * 存储红包id的集合
     */
    @NotEmpty(message = "请至少指定一个红包", groups = {InvalidRedPacket.class,DeleteRedPacket.class})
    private HashSet<Integer> redPacketIds;

    /**
     * 设定红包发送之后的信息
     */
    private String packetInfo;

    /************************操作用户相关************************/
    /**
     * 当前操作用户id
     */
    private Integer id;
    /**
     * 当前用户名称
     */
    private String nickName;

    /**
     * 当前用户手机号
     */
    private Long login;
    /********************验证组******************************/
    /**
     * 生成线下红包验证组
     */
    public interface ProduceRedpacket {
    }

    /**
     * 用户派发红包验证组
     */
    public interface DistributeRedPacketToUser {
    }
    /**
     * 审核红包
     */
    public interface CheckRedPacket {
    }

    /**
     * 失效红包
     */
    public interface InvalidRedPacket {
    }

    /**
     * 删除红包
     */
    public interface DeleteRedPacket {
    }


    public String getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(String packetInfo) {
        this.packetInfo = packetInfo;
    }

    public Integer getRedPacketType() {
        return redPacketType;
    }

    public void setRedPacketType(Integer redPacketType) {
        this.redPacketType = redPacketType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public HashSet<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(HashSet<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getSendReason() {
        return sendReason;
    }

    public void setSendReason(String sendReason) {
        this.sendReason = sendReason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public HashSet<Integer> getRedPacketIds() {
        return redPacketIds;
    }

    public void setRedPacketIds(HashSet<Integer> redPacketIds) {
        this.redPacketIds = redPacketIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "RedpacketDTO{" +
                "num=" + num +
                ", value=" + value +
                ", endTime=" + endTime +
                ", userIds=" + userIds +
                ", sendReason='" + sendReason + '\'' +
                ", checkId='" + checkId + '\'' +
                ", state=" + state +
                ", reason='" + reason + '\'' +
                ", id=" + id +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
