package com.hongkun.finance.point.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分商品订单的VO
 */
public class PointOrderVO  extends BaseModel{
    /**************来源于PointProductorder***************/
    /**
     * 描述: id
     * 字段: ID  INT(10)
     */
    private Integer id;



    /**
     * 描述: 商品id
     * 字段: product_id  INT(10)
     * 默认值: 0
     */

    private Integer productId;


    /**
     * 描述: 兑换消耗积分
     * 字段: point  INT(10)
     * 默认值: 0
     */

    private Integer point;

    /**
     * 描述: 兑换数量
     * 字段: number  INT(10)
     * 默认值: 0
     */

    private Integer number;

    /**
     * 描述: 总价值
     * 字段: worth  DECIMAL(20)
     * 默认值: 0.00
     */
    private java.math.BigDecimal worth;

    /**
     * 描述: 地址信息
     * 字段: address  VARCHAR(100)
     * 默认值: ''
     */

    private String address;

    /**
     * 描述: 快递单号
     * 字段: courier_no  VARCHAR(50)
     * 默认值: ''
     */
    private String courierNo;

    /**
     * 描述: 状态：0-待审核，1-商家处理中，2-商家处理完成，3-订单已取消',
     * 字段: state  TINYINT(3)
     * 默认值: 0
     */
    private Integer state;

    /**
     * 描述: 创建用户id
     * 字段: create_user_id  INT(10)
     * 默认值: 0
     */
    private Integer createUserId;

    /**
     * 描述: 修改用户id
     * 字段: modify_user_id  INT(10)
     * 默认值: 0
     */
    private Integer modifyUserId;

 /****************来源于商品字段******************/
    /**
     * 描述: 商品名称
     * 字段: name  VARCHAR(100)
     * 默认值: ''
     */
    private String name;

    /**
     * 配送方式
     */
    private Integer sendWay;

    /**
     * 小图地址
     */
    private String smallImgUrl;



    /**********其他字段***************/

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 用户电话
     */
    private String login;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String consigneeTel;

    /**
     * 描述: 创建时间
     * 字段: create_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date createTime;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeEnd;
    /**
     * 描述: 修改时间
     * 字段: modify_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeEnd;


    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public Integer getSendWay() {
        return sendWay;
    }

    public void setSendWay(Integer sendWay) {
        this.sendWay = sendWay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTimeBegin() {
        return modifyTimeBegin;
    }

    public void setModifyTimeBegin(Date modifyTimeBegin) {
        this.modifyTimeBegin = modifyTimeBegin;
    }

    public Date getModifyTimeEnd() {
        return modifyTimeEnd;
    }

    public void setModifyTimeEnd(Date modifyTimeEnd) {
        this.modifyTimeEnd = modifyTimeEnd;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
