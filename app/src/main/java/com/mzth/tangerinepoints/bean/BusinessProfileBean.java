package com.mzth.tangerinepoints.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 * 商店实体对象
 */

public class BusinessProfileBean implements Serializable {
    private String businessId;//商店ID
    private String merchantId;//商人ID
    private String businessName;//商店名字
    private int businessTypeId;//商店类型ID
    private String address;//地址
    private String zipcode;//邮政编码
    private String city;//城市
    private String state;//状态
    private String mainPicture;//图片
    private LocationBean geoLocation;//地址
    private boolean retired;//退休
    private List<String> pictures;//图片
    private BusinessHoursBean businessHours;//营业时间
    private double distance;//距离
    private String fullAddress;//详细地址

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

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(int businessTypeId) {
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

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public BusinessHoursBean getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(BusinessHoursBean businessHours) {
        this.businessHours = businessHours;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
