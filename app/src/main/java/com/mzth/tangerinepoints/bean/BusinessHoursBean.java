package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 */

public class BusinessHoursBean implements Serializable {
    private HoursBean SATURDAY;//周6
    private HoursBean WEDNESDAY;//周3
    private HoursBean TUESDAY;//周2
    private HoursBean THURSDAY;//周4
    private HoursBean FRIDAY;//周5
    private HoursBean MONDAY;//周1
    private HoursBean SUNDAY;//周日

    public HoursBean getSATURDAY() {
        return SATURDAY;
    }

    public void setSATURDAY(HoursBean SATURDAY) {
        this.SATURDAY = SATURDAY;
    }

    public HoursBean getWEDNESDAY() {
        return WEDNESDAY;
    }

    public void setWEDNESDAY(HoursBean WEDNESDAY) {
        this.WEDNESDAY = WEDNESDAY;
    }

    public HoursBean getTUESDAY() {
        return TUESDAY;
    }

    public void setTUESDAY(HoursBean TUESDAY) {
        this.TUESDAY = TUESDAY;
    }

    public HoursBean getTHURSDAY() {
        return THURSDAY;
    }

    public void setTHURSDAY(HoursBean THURSDAY) {
        this.THURSDAY = THURSDAY;
    }

    public HoursBean getFRIDAY() {
        return FRIDAY;
    }

    public void setFRIDAY(HoursBean FRIDAY) {
        this.FRIDAY = FRIDAY;
    }

    public HoursBean getMONDAY() {
        return MONDAY;
    }

    public void setMONDAY(HoursBean MONDAY) {
        this.MONDAY = MONDAY;
    }

    public HoursBean getSUNDAY() {
        return SUNDAY;
    }

    public void setSUNDAY(HoursBean SUNDAY) {
        this.SUNDAY = SUNDAY;
    }
}
