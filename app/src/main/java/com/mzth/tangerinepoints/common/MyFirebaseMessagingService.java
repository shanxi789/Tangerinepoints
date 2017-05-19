package com.mzth.tangerinepoints.common;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mzth.tangerinepoints.ui.activity.sub.MainActivity;

/**
 * Created by Administrator on 2017/5/9.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MyMessagingService";

    /**

     * 处理从firebase接收到的推送消息

     */

    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {

        //打印消息来源

        Log.i(TAG, "From:" + remoteMessage.getFrom());

        //判定是否承载了「数据」信息

        if (remoteMessage.getData().size() > 0) {

            Log.i(TAG, "Message data payload:" + remoteMessage.getData());


            //start MainActivity并传递msgA

            Intent i = new Intent(this, MainActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.putExtra("msgA", remoteMessage.getData().get("msgA")+"\n(onMessageReceived处理的)");

            startActivity(i);

        }

        //检验是否承载了「通知」信息

        if (remoteMessage.getNotification() != null) {

            Log.i(TAG, "Message Notification Title:" + remoteMessage.getNotification().getTitle());

            Log.i(TAG, "Message Notification Body:" + remoteMessage.getNotification().getBody());

        }

    }
}
