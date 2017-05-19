package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.MerchantBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MerchantAdapter extends BaseInfoAdapter<OffersBean> {
    public MerchantAdapter(Context context, List<OffersBean> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<OffersBean> list, int resId, int position, View convertView) {
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
        private TextView tv_merchant_name,tv_merchant_pts,tv_merchant_redeems;
        private ImageView iv_merchant_shopp;
        private void initView(View v){
            tv_merchant_name = (TextView) v.findViewById(R.id.tv_merchant_name);
            tv_merchant_pts = (TextView) v.findViewById(R.id.tv_merchant_pts);
            tv_merchant_redeems = (TextView) v.findViewById(R.id.tv_merchant_redeems);
            iv_merchant_shopp = (ImageView) v.findViewById(R.id.iv_merchant_shopp);
        }
        private void initData(List<OffersBean> list, int position){
            OffersBean bean = list.get(position);
            Glide.with(_context).load(Constans.PHOTO_PATH+bean.getItemPicture()+"/300/300").placeholder(R.mipmap.ic_launcher).into(iv_merchant_shopp);
            tv_merchant_name.setText(bean.getItemName());
            tv_merchant_pts.setText(bean.getPoints()+"Pts");
            tv_merchant_redeems.setText(bean.getRedemptionCount()+" redeems");
        }
    }
}
