package com.mzth.tangerinepoints.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Constans {

    //-------------------------------测试数据-------------------------------
    //UUID
    public final static String APP_INSTANCE_ID ="0bbcae96-b71d-4562-94a7-bcaa24d7d3df";
    //商店ID
    public final static String businessId ="d6bcf726-17ef-4430-a8bb-97e4b77aa35b";
    //商店的位置
    public final static String location ="40.4792841,-79.9194674";
    //商店附近的一个位置
    public final static String location_nearby ="40.477218, -79.923742";
    //地图可视范围的西南角（左下角）
    public final static String boundrySouthWest ="40.473587, -79.932366";
    //地图可视范围的东北角（右上角）
    public final static String boundryNorthEast ="40.488726, -79.897110";
    //一个可兑换奖品的ID
    public final static String offerId ="9cfb752d-cb56-4273-88da-22121e2ee09e";
    //一个优惠券的ID
    public final static String couponId ="c1cb0b7a-fcd7-4c31-ae6d-e172a8529b93";
    //一个已注册用户端的ID
    public final static String customerId ="0bbcae96-b71d-4562-94a7-bcaa24d7d3df";
    //一个已注册用户端的手机号
    public final static String phoneNumber ="+8618310968137";
    //一个已注册用户端的密码
    public final static String password ="123456";
    //另外一个已注册用户端的ID
    public final static String customerId_other ="8d70fb5c-3684-4a02-bd35-2a6316e77c51";




    public final static String APP_NAME ="Tangerinepoints";
    // sd卡的绝对路径
    public final static String SDPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    // 设置头像文件保存路径
    public final static String PATH_HEAD = SDPATH + File.separator + APP_NAME
            + File.separator + "head" + File.separator;

    //------------------------------错误提示语------------------------
    //无效的电子邮件格式。
    public final static String INVALID_EMAIL_FORMAT ="Invalid email format.";
    //您输入的移动号码似乎无效。
    public final static String INVALID_MOBILE_NUMBER ="The mobile number you entered seems invalid.";
    //地理定位是当前不可用。
    public final static String CANNOT_ACQUIRE_GEOCODING ="Geolocation is currently unavailable.";
    //QR码暂时不可用。
    public final static String QR_CODE_GENERATOR_EXCEPTION ="QR Code is temporarily unavailable.";
    //短信服务遇到了问题。请稍后再试。
    public final static String SMS_EXCEPTION ="SMS service has encountered a problem. Please try again later.";
    //文件上传遇到了问题。请稍后再试。
    public final static String UPLOAD_EXCEPTION ="File upload has encountered a problem. Please try again later.";
    //您已经邀请了这个联系人。
    public final static String ALREADY_INVITED ="You have already invited this contact.";
    //无效生日格式。请使用格式MM/dd/yyyy。
    public final static String INCORRECT_BIRTHDAY_FORMAT ="Invalid birthday format. Please use format MM/DD/YYYY.";
    //指定的用户不在您的联系人簿中。
    public final static String INVALID_CONTACT ="The specified user is not in your contact book.";
    //您输入的电话号码或密码无效。
    public final static String LOGIN_FAILURE ="The phone number or password you have entered is invalid.";
    //对不起，但tangerinepoints目前可用在您的国家。
    public final static String MOBILE_NUMBER_COUNTRY_NOT_ALLOWED ="Sorry, but TangerinePoints is currently unavailable in your country.";
    //您输入的移动号码已注册。
    public final static String MOBILE_NUMBER_ALREADY_REGISTERED ="The mobile number you entered is already registered.";
    //您输入的验证码无效。
    public final static String INVALID_VERIFICATION_CODE ="The verification code you entered is invalid.";
    //您输入的验证码已过期。请另一个。
    public final static String EXPIRED_VERIFICATION_CODE ="The verification code you entered has expired. Please request another one.";
    //请提供有效密码。
    public final static String INVALID_PASSWORD_FORMAT ="Please provide a valid password.";
    //屏幕名称只能包含字母、数字和下划线。不能超过20个字符。
    public final static String INVALID_SCREEN_NAME ="Screen name can only contain letters,digits,and underscore. Cannot exceed 20 characters.";
    //请提供有效的生日。
    public final static String INVALID_BIRTH_DATE ="Please provide a valid birthday.";
    //请提供有效的电子邮件。
    public final static String INVALID_EMAIL ="Please provide a valid Email.";
    //请提供一个有效的自拍图片。
    public final static String INVALID_SELFIE_IMAGE ="Please provide a valid selfie picture.";
    //不支持的报价类型。
    public final static String UNSUPPORTED_OFFER_TYPE ="Unsupported offer type.";
    //无效位置数据。
    public final static String INVALID_LOCATION_DATA ="Invalid location data.";
    //对不起，你没有足够的积分来完成这个交易。
    public final static String INSUFFICIENT_POINTS ="Sorry, but you don't have enough points to complete this transaction.";
    //无效的用户ID。
    public final static String INVALID_USER_ID ="Invalid user id.";
    //不能上传空文件。
    public final static String CANNOT_UPLOAD_EMPTY_FILE ="Cannot upload empty file.";



//---------------------------------客户端app---------------------------------------
    //保存图片的路径
    public final static String SAVE_PHOTO ="TangerinepointsImage";
    //图片的访问路径
    public final static String PHOTO_PATH ="https://tangerinepoints.com/dev/api/image/";
    //注册新用户 - 验证手机号
    public final static String REGISTER_PHONE ="verify/mobile/";
    //注册新用户 - 验证码校验
    public final static String REGISTER_CODE ="verify/mobile/confirmation/";
    //注册新用户 - 设置密码
    public final static String SET_PASSWORD ="register";
    //用户资料更新
    public final static String PROFILE ="profile";
    //上传头像
    public final static String PROFILE_PICTURE ="profile/picture/hex";
    //登录
    public final static String LOGIN ="login";
    //刷新用户信息
    public final static String REFRESH_PROFILE ="profile";
    //可兑换的奖品
    public final static String OFFERS ="offers";
    //用户交易活动历史
    public final static String TRANSACTIONS ="transactions/";
    //获取可以使用的优惠券
    public final static String COUPONS_AVAILABLE ="coupons/available";
    //发现通讯录中已经注册的朋友
    public final static String COUPONS_PHONE_NUMBERS ="contacts/phone-numbers";
    //邀请朋友（短信）
    public final static String CONTACTS_INVITE_SMS ="contacts/invite/sms";
    //转积分给朋友
    public final static String POINTS_TRANSFER ="points/transfer";
    //商店详细信息
    public final static String BUSINESSES_DETAIL ="businesses/";
    //附近的商店
    public final static String BUSINESSES ="/businesses";
    //消息推送
    public final static String MESSAGE_PUSH ="fcm";
    //同步一项未完成交易
    public final static String SYNC_PURCHASES ="transaction/sync/purchases";
}
