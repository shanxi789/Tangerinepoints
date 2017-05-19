package com.mzth.tangerinepoints.ui.activity.sub;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RegisterActivity extends BaseBussActivity {
    private EditText et_phone;
    private TextView tv_commit;
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=RegisterActivity.this;
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        super.initView();
        //手机号输入框
        et_phone= (EditText) findViewById(R.id.et_phone);
        //下一步
        tv_commit= (TextView) findViewById(R.id.tv_commit);
    }

    @Override
    protected void initData() {
        super.initData();
        //et_phone.setText("+8618310968137");

    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtil.isEmpty(et_phone.getText().toString())){
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
                    PhoneRequest();
                }else{
                    ToastUtil.showShort(_context,"请输入手机号");
                }
            }
        });
    }
    //手机号请求
    private void PhoneRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        //传入一个用户输入的手机号
        final String phone=et_phone.getText().toString();
        map.put("mobile_number",phone);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.REGISTER_PHONE,map,null,null,new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Verification code sent successfully");
                Bundle bundle = new Bundle();
                bundle.putString("phone",phone);
                //判断手机号是否未保存到本地，如果没有保存则保存否则吐司显示手机号
                //if(StringUtil.isEmpty((String)SharedPreferencesUtil.getParam(_context,"phone",""))){
                SharedPreferencesUtil.setParam(_context,"phone",phone);
                ToastUtil.showShort(_context,(String)SharedPreferencesUtil.getParam(_context,"phone",""));
                startActivity(SendCodeActivity.class,bundle);
                finish();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){//打印错误信息
                    case "INVALID_MOBILE_NUMBER":
                        ToastUtil.showShort(_context,Constans.INVALID_MOBILE_NUMBER);
                        break;
                    case "MOBILE_NUMBER_ALREADY_REGISTERED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_ALREADY_REGISTERED);
                        break;
                    case "MOBILE_NUMBER_COUNTRY_NOT_ALLOWED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_COUNTRY_NOT_ALLOWED);
                        break;
                    case "SMS_EXCEPTION":
                        ToastUtil.showShort(_context,Constans.SMS_EXCEPTION);
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
