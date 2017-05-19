package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/20.
 */

public class CouponBean implements Serializable {
    //优惠券编号
    private String couponId;
    //检验单位
    private String issuedBy;
    //客户ID
    private String customerId;
    //有效期至
    private long validTill;
    //兑换
    private String redeemed;
    //券项ID
    private String couponTermId;
    //券项标题
    private String couponTermTitle;
    //图片的键
    private String couponImgKey;
    //优惠条款的详细
    private String couponTermDetail;
    //回收价值
    private String paybackValue;
    //二维码
    private String qrCodePngHex;

    public String getCouponImgKey() {
        return couponImgKey;
    }

    public void setCouponImgKey(String couponImgKey) {
        this.couponImgKey = couponImgKey;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public long getValidTill() {
        return validTill;
    }

    public void setValidTill(long validTill) {
        this.validTill = validTill;
    }

    public String getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(String redeemed) {
        this.redeemed = redeemed;
    }

    public String getCouponTermId() {
        return couponTermId;
    }

    public void setCouponTermId(String couponTermId) {
        this.couponTermId = couponTermId;
    }

    public String getCouponTermTitle() {
        return couponTermTitle;
    }

    public void setCouponTermTitle(String couponTermTitle) {
        this.couponTermTitle = couponTermTitle;
    }

    public String getCouponTermDetail() {
        return couponTermDetail;
    }

    public void setCouponTermDetail(String couponTermDetail) {
        this.couponTermDetail = couponTermDetail;
    }

    public String getPaybackValue() {
        return paybackValue;
    }

    public void setPaybackValue(String paybackValue) {
        this.paybackValue = paybackValue;
    }

    public String getQrCodePngHex() {
        return qrCodePngHex;
    }

    public void setQrCodePngHex(String qrCodePngHex) {
        this.qrCodePngHex = qrCodePngHex;
    }
}
