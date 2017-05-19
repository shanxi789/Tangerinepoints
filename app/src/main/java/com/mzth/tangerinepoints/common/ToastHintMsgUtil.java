package com.mzth.tangerinepoints.common;

import com.mzth.tangerinepoints.util.ToastUtil;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ToastHintMsgUtil {

    public static String getToastMsg(String msg){
        switch (msg){//打印错误信息
            case "INVALID_MOBILE_NUMBER":
                return Constans.INVALID_MOBILE_NUMBER;
            case "MOBILE_NUMBER_ALREADY_REGISTERED":
                return Constans.MOBILE_NUMBER_ALREADY_REGISTERED;
            case "MOBILE_NUMBER_COUNTRY_NOT_ALLOWED":
                return  Constans.MOBILE_NUMBER_COUNTRY_NOT_ALLOWED;
            case "SMS_EXCEPTION":
                return Constans.SMS_EXCEPTION;
            default:
                return "";
        }

    }


}
