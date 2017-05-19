package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/28.
 * 通讯录 注册 成功 的 朋友
 */

public class RFriendBean implements Serializable{
    private String phoneNumber;//电话号码
    private String userId;//用户ID
    private String screenName;//昵称
    private String pictureKey;//图片的key

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPictureKey() {
        return pictureKey;
    }

    public void setPictureKey(String pictureKey) {
        this.pictureKey = pictureKey;
    }
}
