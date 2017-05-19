package com.mzth.tangerinepoints.ui.activity.sub.git;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.SystemPhotoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 * 查看大图 的viewpage页面
 */

public class PhotoPreviewActivity extends BaseBussActivity {
    private TextView tv_title;
    private ViewPager vp_photo;
    private SystemPhotoAdapter adapter;
    private List<String> list;
    private String position;
    private ImageView iv_back;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = PhotoPreviewActivity.this;
        setContentView(R.layout.activity_system_photo_preview);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        tv_title = (TextView) findViewById(R.id.tv_title);
        //展示图片的viewpager
        vp_photo = (ViewPager) findViewById(R.id.vp_photo);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }
    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        int tag = intent.getIntExtra("tag",0);
        if(tag==0){
        //接收传过来的图片和下标
        list = (List<String>) intent.getSerializableExtra("list");
        position = intent.getStringExtra("position");
        tv_title.setText(Integer.parseInt(position)+1+"/"+list.size());//标题变为1/1这种
        //获取屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;//获取屏幕宽度
        adapter = new SystemPhotoAdapter(width,_context,list,R.layout.adapter_system_photo_pageview_item);
        vp_photo.setAdapter(adapter);
        //显是第几张
        vp_photo.setCurrentItem(Integer.parseInt(position));
        }else{
           String image = intent.getStringExtra("image");
           tv_title.setText("1/1");//标题变为1/1这种
           List<String> listimage = new ArrayList<String>();
           listimage.add(image);
           adapter = new SystemPhotoAdapter(_context,listimage,R.layout.adapter_system_photo_pageview_item);
           vp_photo.setAdapter(adapter);
        }
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        vp_photo.addOnPageChangeListener(onPageChangeListener);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            int currentItem = position;
            tv_title.setText((currentItem + 1) + "/" + list.size());
        }
    };
}
