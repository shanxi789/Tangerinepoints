package com.mzth.tangerinepoints.ui.activity.sub;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.CountDownTimerUtils;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;

/**
 * Created by lenovo on 2017/5/19.
 * 找回密码
 */

public class ResetPasswordActivity extends BaseBussActivity {
    private TextView tv_title,tv_commit_phone,tv_commit_code,
            tv_find_password_commit;
    private ImageView iv_back;
    private LinearLayout ll_reset02,ll_reset01;
    private EditText et_find_phone,et_find_code,et_find_password,
            et_find_password_again;
    private CountDownTimerUtils timerUtils;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = ResetPasswordActivity.this;
        setContentView(R.layout.activity_reset_password01);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置标题
        tv_title = (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //找回密码第二部
        ll_reset02 = (LinearLayout) findViewById(R.id.ll_reset02);
        //找回密码第一步
        ll_reset01 = (LinearLayout) findViewById(R.id.ll_reset01);
        //手机号
        et_find_phone = (EditText) findViewById(R.id.et_find_phone);
        //确认手机号
        tv_commit_phone = (TextView) findViewById(R.id.tv_commit_phone);
        //验证码
        et_find_code = (EditText) findViewById(R.id.et_find_code);
        //确认验证码
        tv_commit_code = (TextView) findViewById(R.id.tv_commit_code);
        //第一次的密码
        et_find_password = (EditText) findViewById(R.id.et_find_password);
        //第二次的密码
        et_find_password_again = (EditText) findViewById(R.id.et_find_password_again);
        //确认密码
        tv_find_password_commit = (TextView) findViewById(R.id.tv_find_password_commit);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Reset Password");
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        tv_commit_phone.setOnClickListener(myonclick);
        tv_commit_code.setOnClickListener(myonclick);
        tv_find_password_commit.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键监听
                    onBackPressed();
                    break;
                case R.id.tv_commit_phone://确认手机号
                    if(StringUtil.isEmpty(et_find_phone.getText().toString())){
                        ToastUtil.showShort(_context,"The cell phone number cannot be empty");
                        return;
                    }
                    //发生验证码倒计时
                    timerUtils = new CountDownTimerUtils(_context, tv_commit_phone, 1000*30, 1000, "s Regain", "Press to send verification code to this phone.", false);
                    timerUtils.start();
                    break;
                case R.id.tv_commit_code://确认验证码
                    if(StringUtil.isEmpty(et_find_code.getText().toString())){
                        ToastUtil.showShort(_context,"The cell phone number cannot be empty");
                        return;
                    }
                    if(StringUtil.isEmpty(et_find_phone.getText().toString())){
                        ToastUtil.showShort(_context,"The verification code cannot be null");
                        return;
                    }

                    break;
                case R.id.tv_find_password_commit://确认密码
                    String pwd = et_find_password.getText().toString();
                    String pwd02 = et_find_password_again.getText().toString();
                    if(StringUtil.isEmpty(pwd)){
                        ToastUtil.showShort(_context,"Please input a password");
                        return;
                    }
                    if(StringUtil.isEmpty(pwd02)){
                        ToastUtil.showShort(_context,"Please enter your password again");
                        return;
                    }
                    if(!pwd.equals(pwd02)){
                        ToastUtil.showShort(_context,"The password entered for the two time is inconsistent");
                        return;
                    }
                    break;
            }
        }
    };


}
