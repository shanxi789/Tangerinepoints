package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.NearbyBean;
import com.mzth.tangerinepoints.bean.NearbyBusinBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GiftNearbyAdapter extends BaseInfoAdapter<OffersBean> {
    public GiftNearbyAdapter(Context context, List<OffersBean> list, int resId) {
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
        ImageView iv_shopp;
        TextView tv_shopp_name,tv_pts_num,tv_redeem_num,tv_customer,tv_shopp_info;
        private void initView(View v){
            iv_shopp= (ImageView) v.findViewById(R.id.iv_shopp);
            tv_shopp_name= (TextView) v.findViewById(R.id.tv_shopp_name);
            tv_pts_num= (TextView) v.findViewById(R.id.tv_pts_num);
            tv_redeem_num= (TextView) v.findViewById(R.id.tv_redeem_num);
            tv_customer= (TextView) v.findViewById(R.id.tv_customer);
            tv_shopp_info= (TextView) v.findViewById(R.id.tv_shopp_info);
        }
        private void initData(List<OffersBean> list,int position){
            OffersBean bean = list.get(position);
            //Glide.with(_context).load()
            Glide.with(_context).load(Constans.PHOTO_PATH+bean.getItemPicture()+"/200/200").placeholder(R.mipmap.ic_launcher).into(iv_shopp);
            //iv_shopp.setImageResource(R.mipmap.pic04);
            tv_shopp_name.setText(bean.getItemName());
            tv_pts_num.setText(bean.getPoints()+" Pts");
            tv_redeem_num.setText(bean.getRedemptionCount()+" redeems");


            NearbyBusinBean businBean = bean.getNearbyBusiness();
            //商人
            tv_customer.setText(businBean.getBusinessName());
            //地址
            tv_shopp_info.setText(businBean.getFullAddress());
        }
    }
}
