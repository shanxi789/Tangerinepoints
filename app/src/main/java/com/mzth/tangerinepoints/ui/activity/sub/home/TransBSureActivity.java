package com.mzth.tangerinepoints.ui.activity.sub.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.RFriendBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.widget.CircleImageView;

/**
 * Created by Administrator on 2017/5/2.
 * 积分转发成功页面
 */

public class TransBSureActivity extends BaseBussActivity {
    private TextView tv_title,tv_count_info,tv_BehindCount,
            tv_friend_mark,tv_friend_name;
    private ImageView iv_back;
    private RFriendBean bean;//好友信息
    private CircleImageView iv_friend_head;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = TransBSureActivity.this;
        setContentView(R.layout.activity_transfer_balance);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back= (ImageView) findViewById(R.id.iv_back);
        //好友头像
        iv_friend_head = (CircleImageView) findViewById(R.id.iv_friend_head);
        //好友昵称
        tv_friend_name = (TextView) findViewById(R.id.tv_friend_name);
        //好友通讯录中的昵称
        tv_friend_mark = (TextView) findViewById(R.id.tv_friend_mark);
        //用户要转发给好友点数的信息
        tv_count_info = (TextView) findViewById(R.id.tv_count_info);
        //用户余下的点数的信息
        tv_BehindCount = (TextView) findViewById(R.id.tv_BehindCount);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Transfer Balance");
        Intent intent = getIntent();
        bean = (RFriendBean) intent.getSerializableExtra("bean");
        String count = intent.getStringExtra("amount");//得到用户输入的点数
        int BehindCount = intent.getIntExtra("BehindCount",0);//用户余下的点数
        String BookName = intent.getStringExtra("BookName");//得到通讯录中好友的名字
        //设置好友头像
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getPictureKey()+"/100/100").dontAnimate().placeholder(R.mipmap.ic_launcher).into(iv_friend_head);
        tv_friend_name.setText(bean.getScreenName());//设置app中的昵称
        tv_friend_mark.setText(BookName);//设置通讯录中的昵称
        //初始化转发成功后展示的信息
        tv_count_info.setText("You have successfully transferred "+count+" points to "+bean.getScreenName());
        tv_BehindCount.setText("Your current point balance is "+BehindCount+".");
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}
