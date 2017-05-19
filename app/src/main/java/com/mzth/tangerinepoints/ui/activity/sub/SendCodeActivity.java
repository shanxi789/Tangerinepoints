package com.mzth.tangerinepoints.ui.activity.sub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SendCodeActivity extends BaseBussActivity {
    private EditText et_code;
    private TextView tv_commit;
    private String phone;//获取上个页面传过来的手机号
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = SendCodeActivity.this;
        setContentView(R.layout.activity_send_code);
    }

    @Override
    protected void initView() {
        super.initView();
        //验证码的Edittext
        et_code = (EditText) findViewById(R.id.et_code);
        //发送验证码的textview
        tv_commit = (TextView) findViewById(R.id.tv_code_commit);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtil.isEmpty(et_code.getText().toString())){
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
                    SendCodeRequest();
                }else{
                    ToastUtil.showShort(_context,"Please enter the verification code");
                }
            }
        });
    }
    //提交验证码与手机号的请求
    private void SendCodeRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        final String code = et_code.getText().toString();
        map.put("verification_code",code);
        map.put("mobile_number",phone);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.REGISTER_CODE, map, null, null, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Verification code correct");
                Bundle bundle = new Bundle();
                bundle.putString("phone",phone);
                bundle.putString("code",code);
                startActivity(PasswordActivity.class,bundle);
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
                    case "MOBILE_NUMBER_ALREADY_REGISTERED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_ALREADY_REGISTERED);
                        break;
                    case "MOBILE_NUMBER_COUNTRY_NOT_ALLOWED":
                        ToastUtil.showShort(_context,Constans.MOBILE_NUMBER_COUNTRY_NOT_ALLOWED);
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
