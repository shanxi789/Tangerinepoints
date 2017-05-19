package com.mzth.tangerinepoints.ui.activity.sub;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.mzth.tangerinepoints.widget.email.EmailValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 * 编辑信息界面
 */

public class InfoActivity extends BaseBussActivity {
    private TextView tv_done;
    private EditText edit_username,edit_userbirthday,edit_useremail;
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=InfoActivity.this;
        setContentView(R.layout.activity_set_info);
    }

    @Override
    protected void initView() {
        super.initView();
        tv_done = (TextView) findViewById(R.id.tv_done);
        //昵称
        edit_username = (EditText) findViewById(R.id.edit_username);
        //生日
        edit_userbirthday = (EditText) findViewById(R.id.edit_userbirthday);
        //邮箱
        edit_useremail = (EditText) findViewById(R.id.edit_useremail);
    }
    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edit_username.getText().toString();//昵称
                String userbirthdy = edit_userbirthday.getText().toString();//生日
                String useremail = edit_useremail.getText().toString();//邮箱
                if(!StringUtil.isEmpty(username)){//判断用户输入的昵称是否为空
                    if(username.length()>20){//验证昵称长度是否大于20
                        ToastUtil.showShort(_context,"The nickname you entered is too long");
                        return;
                    }
                    if(!username.matches("[A-Za-z0-9\\\\_]+")){//验证昵称
                        ToastUtil.showShort(_context,"The name you entered is not valid");
                        return;
                    }
                }

                if(!StringUtil.isEmpty(useremail)){//判断用户输入的邮箱是否为空
                    EmailValidator emailValidator = new EmailValidator(true);//验证邮箱的工具类
                    if(!emailValidator.isValid(useremail)){//验证邮箱
                        ToastUtil.showShort(_context,"The mailbox you entered is illegal");
                        return;
                    }
                }

                if(!StringUtil.isEmpty(userbirthdy)){//判断用户输入的生日是否为空
                    if(!userbirthdy.matches("(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)[0-9]{2}")){//验证生日
                        ToastUtil.showShort(_context,"Your input is not in the correct format");
                        return;
                    }
                }
//                ToastUtil.showShort(_context,"验证成功哦哦哦");
                InfoRequest();
            }
        });
    }
    //编辑信息请求
    private void InfoRequest(){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        Map<String,Object> map = new HashMap<String,Object>();
        String name = edit_username.getText().toString();
        if(!StringUtil.isEmpty(name)){
            map.put("screen_name",name);
        }else{
            String phone=(String)SharedPreferencesUtil.getParam(_context,"phone","");
            map.put("screen_name",phone.substring(3,phone.length()));
        }
        map.put("notification_swith",true);
        map.put("birthday",edit_userbirthday.getText().toString());
        map.put("email",edit_useremail.getText().toString());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.PROFILE, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                startActivity(LoginActivity.class,null);
                finish();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
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
