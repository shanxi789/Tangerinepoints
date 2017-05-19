package com.mzth.tangerinepoints.ui.activity.sub;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.common.BackUpServices;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.MyFirebaseInstanceIDService;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RegistrationActivity extends BaseBussActivity {
    private TextView tv_register,tv_login;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=RegistrationActivity.this;
        setContentView(R.layout.activity_registration);
    }

    @Override
    protected void initView() {
        super.initView();
        //点击注册
        tv_register= (TextView) findViewById(R.id.tv_register);
        //点击登录
        tv_login= (TextView) findViewById(R.id.tv_login);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        tv_register.setOnClickListener(mOnClickListener);
        tv_login.setOnClickListener(mOnClickListener);
    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_register:
                    startActivity(RegisterActivity.class,null);
                    finish();
                    break;
                case R.id.tv_login:
                    startActivity(LoginActivity.class,null);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void initData() {
        super.initData();
        //启动服务
        //Intent intent = new Intent(_context,MyFirebaseInstanceIDService.class);
        //startService(intent);
        if(!StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context, "accessKey", ""))){
            startActivity(MainActivity.class,null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
