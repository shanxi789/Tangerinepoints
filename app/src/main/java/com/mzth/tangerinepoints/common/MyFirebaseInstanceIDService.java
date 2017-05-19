package com.mzth.tangerinepoints.common;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private String AccessKey;
    @Override
    public void onCreate() {
        super.onCreate();
        onTokenRefresh();
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferencesUtil.setParam(getApplicationContext(),SharedName.TOKEN,refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AccessKey = (String)SharedPreferencesUtil.getParam(getApplicationContext(),SharedName.AccessKey,"");
        if(!StringUtil.isEmpty(AccessKey)){
            MessagePushRequest(token);
        }
    }
    //消息推送请求
    private void MessagePushRequest(String token){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",token);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.MESSAGE_PUSH, map, (String) SharedPreferencesUtil.getParam(getApplicationContext(),SharedName.AccessKey, ""), Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(getApplicationContext(),json);
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(getApplicationContext(),errorMsg);
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(getApplicationContext(),errorMsg);
            }
        });
    }
}
