package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MerchantBean implements Serializable {
    //商人页面的测试数据
    private String name;
    private String pts;
    private String redeems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
    }

    public String getRedeems() {
        return redeems;
    }

    public void setRedeems(String redeems) {
        this.redeems = redeems;
    }
}
