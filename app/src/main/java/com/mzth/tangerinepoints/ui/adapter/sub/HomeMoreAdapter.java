package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class HomeMoreAdapter extends BaseInfoAdapter {
    public HomeMoreAdapter(Context context, List list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List list, int resId, int position, View convertView) {
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
        private TextView tv_popuwindow_item;
        private void initView(View v){
            tv_popuwindow_item= (TextView) v.findViewById(R.id.tv_popuwindow_item);
        }
        private void initData(List list,int position){
            tv_popuwindow_item.setText(list.get(position).toString());
        }
    }
}
