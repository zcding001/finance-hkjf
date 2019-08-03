package com.hongkun.finance.fund.model.vo;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Description : 房产投资前台列表展示vo对象
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.fund.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class HouseProInfoVo extends BaseModel {

    private Integer id;
    private String name;//项目名称
    private String price;//每平米价格
    private String roomType;//主力户型
    private String address;//楼盘地址
    private String homeArea;//房屋面积范围
    private String picUrl;
    private Integer proType;//物业类别
    private String showProType;//物业类别名称

    private String detailUrl;//详情页url

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomeArea() {
        return homeArea;
    }

    public void setHomeArea(String homeArea) {
        this.homeArea = homeArea;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getProType() {
        return proType;
    }

    public void setProType(Integer proType) {
        this.proType = proType;
    }

    public String getShowProType() {
        return showProType;
    }

    public void setShowProType(String showProType) {
        this.showProType = showProType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
