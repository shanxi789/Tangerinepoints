package com.mzth.tangerinepoints.ui.activity.sub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.ReightUserBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.SharedName;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 * 设置密码页面
 */

public class PasswordActivity extends BaseBussActivity {
    private TextView tv_commit;
    private EditText et_password,et_password_again;
    private String phone,code;//上个页面传过来的手机号和验证码
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=PasswordActivity.this;
        setContentView(R.layout.activity_set_password);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        code = intent.getStringExtra("code");
    }
    @Override
    protected void initView() {
        super.initView();
        //下一步
        tv_commit= (TextView) findViewById(R.id.tv_password_commit);
        //输入密码
        et_password= (EditText) findViewById(R.id.et_password);
        //请再次输入密码
        et_password_again= (EditText) findViewById(R.id.et_password_again);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();

        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=et_password.getText().toString();
                String password=et_password_again.getText().toString();
                if(!StringUtil.isEmpty(pass)){
                    if(pass.length()<6) {
                        ToastUtil.showShort(_context, "The password you entered is too short");
                        return;
                    }

                    if(pass.length()>40){
                        ToastUtil.showShort(_context, "The password you entered is too long");
                        return;
                    }
                        if (!StringUtil.isEmpty(password)) {
                            if (password.equals(pass)) {
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
                                SetPasswordRequest();
                            } else {
                                ToastUtil.showShort(_context, "The password you entered two times is not consistent");
                            }
                        } else {
                            ToastUtil.showShort(_context, "Please enter password again");
                        }

                }else{
                    ToastUtil.showShort(_context,"Please input a password");
                }


            }
        });
    }
    //设置密码请求
    private void SetPasswordRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mobile_number",phone);
        map.put("verification_code",code);
        map.put("password",et_password_again.getText().toString());
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String UUID = tm.getDeviceId();//得到设备唯一ID
        SharedPreferencesUtil.setParam(_context, SharedName.UUID,UUID);
        map.put("app_instance_id", Constans.APP_INSTANCE_ID);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.SET_PASSWORD, map, null, null, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Password set successfully");
                String result = GsonUtil.getJsonFromKey(json,"result");
                String user = GsonUtil.getJsonFromKey(result,"user");
                //得到AccessKey
                String accessKey = GsonUtil.getJsonFromKey(result,"accessKey");
                //首先判断这个SharedPreference是否保存accessKey如果没有保存则设置,反之吐司
                //if(StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context,"accessKey",""))){
                    //将accessKey保存到SharedPreferencesUtil中
                    SharedPreferencesUtil.setParam(_context,"accessKey",accessKey);
                    //ToastUtil.showShort(_context,"accessKey保存成功");
                //}else{
                    //ToastUtil.showShort(_context,(String)SharedPreferencesUtil.getParam(_context,"accessKey",""));
                //}
                //ReightUserBean bean = GsonUtil.getBeanFromJson(user, ReightUserBean.class);
                startActivity(InfoActivity.class,null);
                finish();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){//打印错误信息
                    case "INVALID_MOBILE_NUMBER":
                        ToastUtil.showShort(_context,Constans.INVALID_MOBILE_NUMBER);
                        break;
                    case "INVALID_VERIFICATION_CODE":
                        ToastUtil.showShort(_context,Constans.INVALID_VERIFICATION_CODE);
                        break;
                    case "EXPIRED_VERIFICATION_CODE":
                        ToastUtil.showShort(_context,Constans.EXPIRED_VERIFICATION_CODE);
                        break;
                    case "INVALID_PASSWORD_FORMAT":
                        ToastUtil.showShort(_context,Constans.INVALID_PASSWORD_FORMAT);
                        break;
                    case "MOBILE_NUMBER_ALREADY_REGISTERED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_ALREADY_REGISTERED);
                        break;
                    case "MOBILE_NUMBER_COUNTRY_NOT_ALLOWED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_COUNTRY_NOT_ALLOWED);
                        break;
                    case "QR_CODE_GENERATOR_EXCEPTION":
                        ToastUtil.showShort(_context,Constans.QR_CODE_GENERATOR_EXCEPTION);
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }
        });
    }


}
