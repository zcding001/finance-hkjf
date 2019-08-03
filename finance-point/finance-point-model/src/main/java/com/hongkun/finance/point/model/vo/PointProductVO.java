package com.hongkun.finance.point.model.vo;

import com.hongkun.finance.point.model.PointProductImg;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description : 积分商品Vo
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.model.vo.PointProductVO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class PointProductVO extends BaseModel {

    /**###############积分商品字段######################*/

    /**
     * 描述: id
     * 字段: id  INT(10)
     */
    @NotNull(message = "请指定商品id",groups = {UPDATE.class})
    private Integer id;

    /**
     * 描述: 商品种类id
     * 字段: product_category_id  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请指定商品种类",groups = {UPDATE.class,SAVE.class})
    private Integer productCategoryId;

    /**
     * 描述: 商品名称
     * 字段: name  VARCHAR(100)
     * 默认值: ''
     */
    @NotNull(message = "请填写商品名称",groups =  {UPDATE.class,SAVE.class})
    private String name;

    /**
     * 描述: 所需积分
     * 字段: point  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请填写商品积分",groups =  {UPDATE.class,SAVE.class})
    private Integer point;

    /**
     * 描述: 商品价值
     * 字段: worth  DECIMAL(20)
     * 默认值: 0.00
     */
    private java.math.BigDecimal worth;

    /**
     * 描述: 商品数量
     * 字段: number  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请填写商品库存",groups =  {UPDATE.class,SAVE.class})
    private Integer number;

    /**
     * 描述: 排序号
     * 字段: sort  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请填写商品排序号",groups = {UPDATE.class,SAVE.class})
    private Integer sort;

    /**
     * 描述: 商品发送方式：0-邮寄或自提，1-自提，2-邮寄，3-兑换码
     * 字段: send_way  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请指定商品发送方式",groups =  {UPDATE.class,SAVE.class})
    private Integer sendWay;

    /**
     * 描述: 折扣价格
     * 字段: point  INT(10)
     * 默认值: 0
     */
    private Integer discountPoint;

    /**
     * 描述: 是否为推荐商品 :0-推荐商品 1 -非推荐商品
     * 字段: recommend  TINYINT(3)
     * 默认值: 0
     */
    private Integer recommend;

    /**
     * 描述: 是否为限时抢购： 0- 非限时抢购 1-限时抢购
     * 字段: recommend  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请指定是否为限时抢购商品",groups =  {UPDATE.class,SAVE.class})
    private Integer flashSale;

    /**
     * 描述: 是否在ios端展示：0-不显示，1-显示
     * 字段: ios_show  TINYINT(3)
     * 默认值: 1
     */
    @NotNull(message = "请指定是否在ios端显示",groups =  {UPDATE.class,SAVE.class})
    private Integer iosShow;

    /**
     * 描述: 审核状态：0-待审核，1-待上架（审核通过），2-已上架，3-已拒绝
     * 字段: state  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请指定状态",groups = CHECK.class)
    private Integer state;

    /**
     * 描述: 描述
     * 字段: comments  VARCHAR(500)
     * 默认值: ''
     */
    private String comments;

    /**
     * 描述: 商品信息
     * 字段: prduct_info  TEXT(65535)
     */
    @NotNull(message = "请填写商品信息",groups =  {UPDATE.class,SAVE.class})
    private String prductInfo;

    /**
     * 描述: 商品详情
     * 字段: goods_details  TEXT(65535)
     */
    @NotNull(message = "请填写商品详情",groups = {UPDATE.class,SAVE.class})
    private String goodsDetails;

    /**
     * 描述: 未通过原因
     * 字段: refuse_cause  VARCHAR(200)
     */
    private String refuseCause;

    /**
     * 描述: 商品销售数量统计
     * 字段: sales_count  INT(10)
     * 默认值: 0
     */
    private Integer salesCount;

    /**###############商品图像字段######################*/
    /**
     * 描述: 图片路径
     * 字段: big_img_url  VARCHAR(200)
     * 默认值: ''
     */
    @NotEmpty(message = "请指定商品图片",groups = {UPDATE.class,SAVE.class})
    private List<String> imgs;


    /**
     * 商品首图
     */
    @NotNull(message = "请指定商品首图",groups = {UPDATE.class,SAVE.class})
    private String firstImg;

    /**
     * 描述: 展示开始时间
     * 字段: show_time_begin  DATETIME(19)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date showTimeStart;
    /**
     * 描述: 展示结束时间
     * 字段: show_time_end  DATETIME(19)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date showTimeEnd;

    /**###############其他字段######################*/
    /**
     * 分类名称
     */
    private String productCategoryName;

    /**
     * 产品id
     */
    List<Integer> productIds;

    /**
     * 限制id
     */
    List<Integer> limitIds;

    /**
     * 商品的所有的图片
     */
    private List<PointProductImg> productImgList;

    /**
     * 商品首图
     */

    private PointProductImg headImg;

    @NotNull(message = "请指定审核条目",groups = {CHECK.class})
    private List<Integer>  checkIds;


    //【非数据库字段，查询时使用】
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeEnd;
    /**
     * 描述: 修改时间
     * 字段: modify_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTimeEnd;

    /**********************分组接口*************************************/
    public interface  SAVE{}
    public interface  UPDATE{}
    public interface  CHECK{}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSendWay() {
        return sendWay;
    }

    public void setSendWay(Integer sendWay) {
        this.sendWay = sendWay;
    }

    public Integer getIosShow() {
        return iosShow;
    }

    public void setIosShow(Integer iosShow) {
        this.iosShow = iosShow;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPrductInfo() {
        return prductInfo;
    }

    public void setPrductInfo(String prductInfo) {
        this.prductInfo = prductInfo;
    }

    public String getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(String goodsDetails) {
        this.goodsDetails = goodsDetails;
    }

    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public PointProductImg getHeadImg() {
        return headImg;
    }

    public void setHeadImg(PointProductImg headImg) {
        this.headImg = headImg;
    }

    public List<PointProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<PointProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public List<Integer> getCheckIds() {
        return checkIds;
    }

    public void setCheckIds(List<Integer> checkIds) {
        this.checkIds = checkIds;
    }


    public List<Integer> getLimitIds() {
        return limitIds;
    }

    public void setLimitIds(List<Integer> limitIds) {
        this.limitIds = limitIds;
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


    public Date getShowTimeStart() {
        return showTimeStart;
    }

    public void setShowTimeStart(Date showTimeStart) {
        this.showTimeStart = showTimeStart;
    }

    public Date getShowTimeEnd() {
        return showTimeEnd;
    }

    public void setShowTimeEnd(Date showTimeEnd) {
        this.showTimeEnd = showTimeEnd;
    }


    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public Integer getDiscountPoint() {
        return discountPoint;
    }

    public void setDiscountPoint(Integer discountPoint) {
        this.discountPoint = discountPoint;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public Integer getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(Integer flashSale) {
        this.flashSale = flashSale;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
