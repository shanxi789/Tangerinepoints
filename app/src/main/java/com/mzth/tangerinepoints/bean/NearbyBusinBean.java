package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/26.
 */

public class NearbyBusinBean implements Serializable {//附近的商业
    private String businessId;//商店id
    private String merchantId;//商人id
    private String businessName;//商店名称
    private String businessTypeId;//商店类型
    private String address;//地址
    private String zipcode;//邮政编码
    private String city;//城市
    private String state;//状态
    private String mainPicture;//主画面
    private LocationBean geoLocation;//地理位置
    private String outOfStock;//无现货的
    private String fullAddress;//城市地址

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public LocationBean getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(LocationBean geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(String outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
