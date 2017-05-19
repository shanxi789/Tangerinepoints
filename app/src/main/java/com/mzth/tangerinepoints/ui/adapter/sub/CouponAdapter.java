package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.CouponBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */

public class CouponAdapter extends BaseInfoAdapter<CouponBean> {
    public CouponAdapter(Context context, List<CouponBean> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<CouponBean> list, int resId, int position, View convertView) {
        ViewHolder vh=null;
        if(convertView==null){
            convertView=View.inflate(context,resId,null);
            vh=new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.initData(list,position);
        return convertView;
    }

    class ViewHolder{
        private TextView tv_coupon_info;
        private TextView tv_coupon_time;
        private ImageView iv_integral;
        private void initView(View v){
            tv_coupon_info = (TextView) v.findViewById(R.id.tv_coupon_info);
            tv_coupon_time = (TextView) v.findViewById(R.id.tv_coupon_time);
            iv_integral = (ImageView) v.findViewById(R.id.iv_integral);
        }
        private void initData(List<CouponBean> list, int position){
            CouponBean bean = list.get(position);
            //设置图片
            Glide.with(_context).load(Constans.PHOTO_PATH+bean.getCouponImgKey()+"/300/172").placeholder(R.mipmap.ic_launcher).into(iv_integral);
            tv_coupon_info.setText(bean.getCouponTermTitle()+bean.getCouponTermDetail());
            long ValidTill = bean.getValidTill();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date= new Date(ValidTill);
            String time = sdf.format(date);
            tv_coupon_time.setText("Expiration Date:"+time);
        }
    }
}
