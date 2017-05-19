package com.mzth.tangerinepoints.ui.activity.sub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;

/**
 * Created by Administrator on 2017/5/12.
 *
 * Help & Feedback
 */

public class TermsOfServiceActivity extends BaseBussActivity {
    private ImageView iv_back;
    private TextView tv_title;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = TermsOfServiceActivity.this;
        setContentView(R.layout.activity_of_service);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
            }
        }
    };
    @Override
    protected void initData() {
        super.initData();
        tv_title.setText("Terms Of Service");
    }
}
