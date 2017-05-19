package com.mzth.tangerinepoints.ui.activity.sub;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.GoogleMapActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.PhotoPreviewActivity;
import com.mzth.tangerinepoints.util.ToastUtil;

/**
 * Created by lenovo on 2017/5/19.
 * 版本页面
 */

public class VersionActivity extends BaseBussActivity {
    private TextView tv_title,tv_version,tv_update;
    private ImageView iv_back;
    private RelativeLayout rl_version;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = VersionActivity.this;
        setContentView(R.layout.activity_version);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置标题
        tv_title = (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //版本更新
        rl_version = (RelativeLayout) findViewById(R.id.rl_version);
        //版本号
        tv_version = (TextView) findViewById(R.id.tv_version);
        //已经是最新版本  状态
        tv_update = (TextView) findViewById(R.id.tv_update);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Version Update");
        tv_version.setText("V"+getVersionName());
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        rl_version.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键监听
                    onBackPressed();
                    break;
                case R.id.rl_version://版本更新
                    ToastUtil.showShort(_context,"当前版本号为"+getVersionName());
                    break;
            }
        }
    };

    //获取当前应用的版本号：
    private String getVersionName(){
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

}
