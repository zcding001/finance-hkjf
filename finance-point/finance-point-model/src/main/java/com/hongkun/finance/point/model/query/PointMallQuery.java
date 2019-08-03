package com.hongkun.finance.point.model.query;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

/**
 * 积分商城的搜索条件
 */
public class PointMallQuery extends BaseModel {

    /**
     * 商品的分类id
     */
    private Integer cateId;

    /**
     * 积分范围起始
     */
    private Integer pointStart;

    /**
     * 是否是推荐商品
     */
    private Integer recommend;

    /**
     * 是否限时商品
     */
    private Integer flashSale;

    /**
     * 积分范围结束
     */
    private Integer pointEnd;

    /**
     * 排序方式：1-默认 2-兑换排行 3-积分升序  4-积分降序
     */
    private Integer sortType=1;

    /**
     * 目录的ids
     */
    List<Integer> catesIds;

    /**
     * 产品名称
     */

    private String productName;


    /**
     * 是否来自ios请求
     */
    private Integer fromIos;



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
    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getPointStart() {
        return pointStart;
    }

    public void setPointStart(Integer pointStart) {
        this.pointStart = pointStart;
    }

    public Integer getPointEnd() {
        return pointEnd;
    }

    public void setPointEnd(Integer pointEnd) {
        this.pointEnd = pointEnd;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Integer> getCatesIds() {
        return catesIds;
    }

    public void setCatesIds(List<Integer> catesIds) {
        this.catesIds = catesIds;
    }

    public Integer getFromIos() {
        return fromIos;
    }

    public void setFromIos(Integer fromIos) {
        this.fromIos = fromIos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
