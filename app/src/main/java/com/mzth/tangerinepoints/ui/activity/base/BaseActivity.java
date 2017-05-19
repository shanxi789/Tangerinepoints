package com.mzth.tangerinepoints.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mzth.tangerinepoints.common.MainApplication;
import com.mzth.tangerinepoints.util.ListDataSaveUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;


/**
 * Created by leeandy007 on 2017/3/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Activity _context;
    protected MainApplication application;
    protected String Authorization;//消息头
    protected int index;//第几条
    protected int n;//显示的条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //避免 输入法将页面顶出去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        application = (MainApplication) this.getApplication();
        application.addActivity(this);
        index = 0;
        n = 5 ;
        //透明状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setCustomLayout(savedInstanceState);
        initView();
        BindComponentEvent();
        //判断如果accessKey不为空就将值传给Authorization
        if(!StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context, "accessKey", ""))) {
            Authorization = (String) SharedPreferencesUtil.getParam(_context, "accessKey", "");
        }
        initData();


    }
    /**
     * 初始化布局
     * */
    protected abstract void setCustomLayout(Bundle savedInstanceState);

    /**
     * 初始化控件
     * */
    protected abstract void initView();

    /**
     * 绑定控件事件
     * */
    protected abstract void BindComponentEvent();

    /**
     * 初始化数据
     * */
    protected abstract void initData();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            doActivityResult(requestCode, intent);
        }
    }


    /**
     * 带返回值跳转的数据的处理方法
     * */
    protected abstract void doActivityResult(int requestCode, Intent intent);

}
