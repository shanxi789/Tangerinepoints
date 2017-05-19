package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/26.
 */

public class OffersBean implements Serializable {
    //兑换ID
    private String offerId;
    //条目名称
    private String itemName;
    //项目描述
    private String itemDescription;
    //图片键名
    private String itemPicture;
    //最后编辑时间
    private long lastEditedAt;
    //是否过期
    private boolean retired;
    //点数
    private int points;
    //可在企业
    private AvaBusineBean availableAtBusinesses;
    //二维码
    private String qrCodePngHex;
    //附近的商业
    private NearbyBusinBean nearbyBusiness;
    //赎回计数
    private int redemptionCount;
    //距离比较
    private int comparisonDistance;
    //由当前用户赎回
    private boolean redeemedByCurrentUser;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(String itemPicture) {
        this.itemPicture = itemPicture;
    }

    public long getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(long lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public AvaBusineBean getAvailableAtBusinesses() {
        return availableAtBusinesses;
    }

    public void setAvailableAtBusinesses(AvaBusineBean availableAtBusinesses) {
        this.availableAtBusinesses = availableAtBusinesses;
    }

    public String getQrCodePngHex() {
        return qrCodePngHex;
    }

    public void setQrCodePngHex(String qrCodePngHex) {
        this.qrCodePngHex = qrCodePngHex;
    }

    public NearbyBusinBean getNearbyBusiness() {
        return nearbyBusiness;
    }

    public void setNearbyBusiness(NearbyBusinBean nearbyBusiness) {
        this.nearbyBusiness = nearbyBusiness;
    }

    public int getRedemptionCount() {
        return redemptionCount;
    }

    public void setRedemptionCount(int redemptionCount) {
        this.redemptionCount = redemptionCount;
    }

    public int getComparisonDistance() {
        return comparisonDistance;
    }

    public void setComparisonDistance(int comparisonDistance) {
        this.comparisonDistance = comparisonDistance;
    }

    public boolean isRedeemedByCurrentUser() {
        return redeemedByCurrentUser;
    }

    public void setRedeemedByCurrentUser(boolean redeemedByCurrentUser) {
        this.redeemedByCurrentUser = redeemedByCurrentUser;
    }
}

