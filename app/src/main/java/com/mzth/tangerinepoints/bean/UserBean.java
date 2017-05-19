package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class UserBean implements Serializable {
    //用户ID
    private String customerId;
    //用户手机号
    private String phoneNumber;
    //昵称
    private String screenName;
    //照片
    private String picture;
    //生日
    private String birthday;
    //邮箱
    private String email;
    //通知开关
    private boolean notificationSwitch;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotificationSwitch() {
        return notificationSwitch;
    }

    public void setNotificationSwitch(boolean notificationSwitch) {
        this.notificationSwitch = notificationSwitch;
    }
}
