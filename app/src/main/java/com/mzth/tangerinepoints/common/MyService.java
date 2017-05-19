package com.mzth.tangerinepoints.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.activity.sub.MainActivity;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    //用于和外界交互
    private final IBinder binder = new MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate()");
        //在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder
                (this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.logo01)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("近医通") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.logo01) // 设置状态栏内的小图标
                .setContentText("正在运行,点击查看详情") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(110, notification);// 开始前台服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//初始化

        //flags=START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        //stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }

    public class MyBinder extends Binder
    {
        MyService getService()
        {
            return MyService.this;
        }
    }
}
