package com.mzth.tangerinepoints.ui.activity.sub.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.RFriendBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.DialogThridUtils;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/15.
 * 转积分给朋友页面
 */

public class TransferBalanceActivity extends BaseBussActivity {
    private TextView tv_title,tv_confirm,tv_friend_name,
            tv_friend_mark,tv_friend_info;
    private ImageView iv_back;
    private CircleImageView iv_friend_head;
    private EditText edit_friend_integral;
    private RFriendBean bean;//好友信息
    private int PointBalance;//用户的总积分点数
    private String bookname;//得到通讯录中好友的名字
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=TransferBalanceActivity.this;
        setContentView(R.layout.activity_transfer_balance_no);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back= (ImageView) findViewById(R.id.iv_back);
        //确认
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        //积分输入框
        edit_friend_integral = (EditText) findViewById(R.id.edit_friend_integral);
        //好友头像
        iv_friend_head = (CircleImageView) findViewById(R.id.iv_friend_head);
        //好友昵称
        tv_friend_name = (TextView) findViewById(R.id.tv_friend_name);
        //好友通讯录中的昵称
        tv_friend_mark = (TextView) findViewById(R.id.tv_friend_mark);
        //转发信息
        tv_friend_info = (TextView) findViewById(R.id.tv_friend_info);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Transfer Balance");
        bean =(RFriendBean) getIntent().getSerializableExtra("bean");
        bookname = getIntent().getStringExtra("BookName");
        //设置好友头像
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getPictureKey()+"/100/100").dontAnimate().placeholder(R.mipmap.ic_launcher).into(iv_friend_head);
        tv_friend_name.setText(bean.getScreenName());//设置app中的昵称
        tv_friend_mark.setText(bookname);//设置通讯录中的昵称
        //得到积分点数
        PointBalance = (int) SharedPreferencesUtil.getParam(_context,"PointBalance",0);
        tv_friend_info.setText("Please enter the amount of points" +
                " you wish to transter." +
                "Your have "+PointBalance+" points available.");//设置转发信息
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        tv_confirm.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键监听
                    onBackPressed();
                    break;
                case R.id.tv_confirm://确认
                    if(StringUtil.isEmpty(edit_friend_integral.getText().toString())){
                       ToastUtil.showShort(_context,"Please input the points you want to transfer");
                        return;
                    }
                    if(PointBalance<Integer.valueOf(edit_friend_integral.getText().toString())){
                        ToastUtil.showShort(_context,"Your points are insufficient");
                        return;
                    }
                    ForwardToFriendsRequest();//发送请求
                    break;
            }
        }
    };
    //转积分给朋友
    private void ForwardToFriendsRequest(){
        //加载的动画
        final Dialog dialog = DialogThridUtils.showWaitDialog(_context,"Loading",false,false);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("target_user_id",bean.getUserId());
        map.put("amount",edit_friend_integral.getText().toString());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.POINTS_TRANSFER, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Integral forwarding successful");
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                //用户输入的积分点数
                String count = edit_friend_integral.getText().toString();
                int BehindCount = PointBalance-Integer.parseInt(count);
                bundle.putString("amount",count);//用户输入的点数
                bundle.putInt("BehindCount",BehindCount);//用户余下的点数
                bundle.putString("BookName",bookname);
                startActivity(TransBSureActivity.class,bundle);
                finish();
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){//错误信息
                    case "INVALID_CUSTOMER_ID":
                        ToastUtil.showShort(_context,Constans.INVALID_USER_ID);
                        break;
                    case "INSUFFICIENT_POINTS":
                        ToastUtil.showShort(_context,Constans.INSUFFICIENT_POINTS);
                        break;
                    case "INVALID_CONTACT":
                        ToastUtil.showShort(_context,Constans.INVALID_CONTACT);
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }
        });
    }



}
