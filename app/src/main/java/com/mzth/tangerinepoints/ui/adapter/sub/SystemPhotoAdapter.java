package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.FilePhotoBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.adapter.base.ViewPagerAdapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SystemPhotoAdapter extends ViewPagerAdapter<String> {
    private int width;
    public SystemPhotoAdapter(List<String> list) {
        super(list);
    }

    public SystemPhotoAdapter(int width,Context context, List<String> list, int resId) {
        super(context, list, resId);
        this.width = width;
    }
    public SystemPhotoAdapter(Context context, List<String> list, int resId) {
        super(context, list, resId);
        this.width = width;
    }
    @Override
    public void removeItem(ViewGroup viewGroup, int position, Object object) {
        viewGroup.removeView((View) object);
    }

    @Override
    public View dealView(Context context, List<String> list, int resId, int position, ViewGroup viewGroup, View view) {
        view = LayoutInflater.from(context).inflate(resId, viewGroup, false);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.iv_item_img);
        //final ProgressBar pb_item_loading = (ProgressBar) view.findViewById(R.id.pb_item_loading);
        String image = (String) getItem(position);

        Glide.with(context).load(Constans.PHOTO_PATH+image+"/"+300+"/"+300).dontAnimate().crossFade().into(imageView);
        //Glide.with(context).load(Constans.PHOTO_PATH+image+"/200/200").placeholder(R.mipmap.ic_launcher).into(imageView);
        viewGroup.addView(view, 0);
        view.setId(position);
        return view;
    }
}