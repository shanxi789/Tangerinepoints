package com.mzth.tangerinepoints.ui.activity.sub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.bean.RefreshUserBean;
import com.mzth.tangerinepoints.bean.UserBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.MainApplication;
import com.mzth.tangerinepoints.common.MyFirebaseInstanceIDService;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.DialogThridUtils;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 */

public class LoginActivity extends BaseBussActivity {
    //点击登录
    private TextView tv_login,tv_sign_up,tv_find_pwd;
    //用户名和密码
    private EditText et_user_name,et_user_password;
    private RefreshUserBean refreshUserBean;
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=LoginActivity.this;
        setContentView(R.layout.activity_login);

    }
    @Override
    protected void initData() {
        super.initData();
        //设置用户名和密码 目前测试数据
//        et_user_name.setText(Constans.phoneNumber);
//        et_user_password.setText(Constans.password);
    }
    @Override
    protected void initView() {
        super.initView();
        //登录
        tv_login= (TextView) findViewById(R.id.tv_login);
        //用户名
        et_user_name= (EditText) findViewById(R.id.et_user_name);
        //密码
        et_user_password= (EditText) findViewById(R.id.et_user_password);
        //注册
        tv_sign_up= (TextView) findViewById(R.id.tv_sign_up);
        //找回密码
        tv_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isEmpty(et_user_name.getText().toString())){
                    ToastUtil.showShort(_context,"The cell phone number cannot be empty");
                    return;
                }
                if(StringUtil.isEmpty(et_user_password.getText().toString())){
                    ToastUtil.showShort(_context,"Password cannot be empty");
                    return;
                }
                LoginRequest();
            }
        });
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击进入注册页面
                startActivity(RegisterActivity.class,null);
                onBackPressed();
            }
        });
        tv_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ResetPasswordActivity.class,null);
            }
        });
    }

    private void LoginRequest(){
        mWeiboDialog = DialogThridUtils.showWaitDialog(_context, "Loging...",false,false);
        Map<String,Object> map = new HashMap<String,Object>();
        String phone=et_user_name.getText().toString();
        String password=et_user_password.getText().toString();
        map.put("mobile_number",phone);
        map.put("password",password);
        map.put("app_instance_id",Constans.APP_INSTANCE_ID);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.LOGIN, map,null,null,new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                String result=GsonUtil.getJsonFromKey(json,"result");
                String user=GsonUtil.getJsonFromKey(result,"user");
                String accessKey=GsonUtil.getJsonFromKey(result,"accessKey");
                //首先判断这个SharedPreference是否保存accessKey如果没有保存则设置,反之吐司
                //将accessKey保存到SharedPreferencesUtil中
                SharedPreferencesUtil.setParam(_context,"accessKey",accessKey);

                RrofileRequest();//请求用户信息
                //传递token信息
                String token = (String)SharedPreferencesUtil.getParam(_context,"refreshedToken","");
                //MessagePushRequest(token);//给服务器传递token
                //ToastUtil.showShort(_context,"accessKey保存成功");
                //ToastUtil.showShort(_context,(String)SharedPreferencesUtil.getParam(_context,"accessKey",""));
                //获取到userbean对象之后将他传入主页
                MainApplication.setUserBean((UserBean) GsonUtil.getBeanFromJson(user, UserBean.class));
                //登录成功进入主页
                Bundle bundle = new Bundle();
                bundle.putSerializable("userbean",MainApplication.getUserBean());
                startActivity(MainActivity.class,bundle);
                onBackPressed();
                ToastUtil.showShort(_context,"Login Success");
                DialogThridUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){
                    case "LOGIN_FAILURE":
                        ToastUtil.showShort(_context,Constans.LOGIN_FAILURE);
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
                DialogThridUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                DialogThridUtils.closeDialog(mWeiboDialog);//关闭动画
            }
        });
    }

    //刷新用户信息
    private void RrofileRequest(){
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.REFRESH_PROFILE, null, (String)SharedPreferencesUtil.getParam(_context,"accessKey",""), Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String user = GsonUtil.getJsonFromKey(json,"user");
                refreshUserBean=GsonUtil.getBeanFromJson(user,RefreshUserBean.class);
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }
        });

    }
    //消息推送请求
    private void MessagePushRequest(String token){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",token);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.MESSAGE_PUSH, map, Authorization,Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,json);
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }
        });
    }

}
