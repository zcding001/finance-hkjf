package com.hongkun.finance.fund.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 房产项目详细信息
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.fund.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class HouseInfoAndDetail implements Serializable {
    //info
    private Integer id;//项目编号
    private String name;//项目名称
    private String price;//价格
    private Integer proType;//物业类别：0-普通住宅
    private String feature;//重要特色1-品牌地产 2-小户型 3-花园洋房
    private String buildType;//建筑类别：1-板楼2-低层3-多层4-小高层5-高层
    private String redecorate;//装修状况：1-毛胚2-精装修3-普通装修
    private String proYears;//产权年限
    private String developers;//北京鸿坤地产集团' COMMENT '开发商
    private String address;//楼盘地址
    private Integer saleState;//状态：0-售罄1-在售2-停盘
    private String prefer;//楼盘优惠：0-暂无 1-金服增值卡
    private String startSaleDate;//开盘时间
    private String makeHouseDate;//交房时间
    private String saleAddress;//售楼地址
    private String salTel;//咨询电话
    private String roomType;//主力户型：2-二居3-三居4-四居5-五居6-六居
    private Integer state;//状态：1-上架 0-下架
    private String homeArea;//房屋面积
    private String modifiedUser;//操作人
    private Date createTime;//发布时间

    //detail
    private String landArea;//占地面积(平方米)
    private String buildArea;//建筑面积(平方米)
    private String capRate;//容积率
    private String greenRate;//绿化率
    private String parkSpace;//停车位
    private String floorNum;//楼栋数量
    private String doorNum;//总户数
    private String proCompany;//物业公司
    private String proFee;//物业费
    //showtext
    private String showText;

    private String saleStateStr;//销售状态描述


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

    public Integer getProType() {
        return proType;
    }

    public void setProType(Integer proType) {
        this.proType = proType;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getRedecorate() {
        return redecorate;
    }

    public void setRedecorate(String redecorate) {
        this.redecorate = redecorate;
    }

    public String getProYears() {
        return proYears;
    }

    public void setProYears(String proYears) {
        this.proYears = proYears;
    }

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSaleState() {
        return saleState;
    }

    public void setSaleState(Integer saleState) {
        this.saleState = saleState;
    }

    public String getPrefer() {
        return prefer;
    }

    public void setPrefer(String prefer) {
        this.prefer = prefer;
    }

    public String getStartSaleDate() {
        return startSaleDate;
    }

    public void setStartSaleDate(String startSaleDate) {
        this.startSaleDate = startSaleDate;
    }

    public String getMakeHouseDate() {
        return makeHouseDate;
    }

    public void setMakeHouseDate(String makeHouseDate) {
        this.makeHouseDate = makeHouseDate;
    }

    public String getSaleAddress() {
        return saleAddress;
    }

    public void setSaleAddress(String saleAddress) {
        this.saleAddress = saleAddress;
    }

    public String getSalTel() {
        return salTel;
    }

    public void setSalTel(String salTel) {
        this.salTel = salTel;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getHomeArea() {
        return homeArea;
    }

    public void setHomeArea(String homeArea) {
        this.homeArea = homeArea;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

    public String getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(String buildArea) {
        this.buildArea = buildArea;
    }

    public String getCapRate() {
        return capRate;
    }

    public void setCapRate(String capRate) {
        this.capRate = capRate;
    }

    public String getGreenRate() {
        return greenRate;
    }

    public void setGreenRate(String greenRate) {
        this.greenRate = greenRate;
    }

    public String getParkSpace() {
        return parkSpace;
    }

    public void setParkSpace(String parkSpace) {
        this.parkSpace = parkSpace;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getDoorNum() {
        return doorNum;
    }

    public void setDoorNum(String doorNum) {
        this.doorNum = doorNum;
    }

    public String getProCompany() {
        return proCompany;
    }

    public void setProCompany(String proCompany) {
        this.proCompany = proCompany;
    }

    public String getProFee() {
        return proFee;
    }

    public void setProFee(String proFee) {
        this.proFee = proFee;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getSaleStateStr() {
        return saleStateStr;
    }

    public void setSaleStateStr(String saleStateStr) {
        this.saleStateStr = saleStateStr;
    }
}
