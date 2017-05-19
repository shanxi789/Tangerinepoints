package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/27.
 * 获取手机联系人姓名和电话
 */

public class ContactsBean implements Serializable {
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
