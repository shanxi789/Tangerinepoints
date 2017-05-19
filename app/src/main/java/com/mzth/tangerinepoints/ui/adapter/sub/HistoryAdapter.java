package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.HistoryBean;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;
import com.mzth.tangerinepoints.util.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/4/19.
 */

public class HistoryAdapter extends BaseInfoAdapter<HistoryBean> {
    public HistoryAdapter(Context context, List<HistoryBean> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<HistoryBean> list, int resId, int position, View convertView) {
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
        TextView tv_history_pts;
        TextView tv_history_name;
        TextView tv_history_time;
        private void initView(View v){
            tv_history_pts = (TextView) v.findViewById(R.id.tv_history_pts);
            tv_history_name = (TextView) v.findViewById(R.id.tv_history_name);
            tv_history_time = (TextView) v.findViewById(R.id.tv_history_time);
        }
        private void initData(List<HistoryBean> list,int position){
            HistoryBean bean = list.get(position);
            if(!StringUtil.isEmpty(bean.getPoints())) {
                if (Integer.parseInt(bean.getPoints()) < 0) {//如果点数小于0就显示红色的字体
                    tv_history_pts.setTextColor(_context.getResources().getColor(R.color.red));
                    tv_history_pts.setText(bean.getPoints() + "pts");
                    tv_history_pts.setTextSize(25);
                } else {
                    tv_history_pts.setTextColor(_context.getResources().getColor(R.color.orange));
                    tv_history_pts.setText(bean.getPoints() + "pts");
                    tv_history_pts.setTextSize(25);
                }
            }else{
                tv_history_pts.setTextColor(_context.getResources().getColor(R.color.orange));
                tv_history_pts.setText(bean.getCoupon());
                tv_history_pts.setTextSize(15);
            }
            tv_history_name.setText(bean.getOtherParty());
            //将时间格式转换
            String times= DateFormat.getDateTimeInstance(DateFormat.LONG, 3).format(new Date(bean.getTime()));
            String timesub = "";
            if(times.indexOf("上午")!=-1){
                timesub = times.substring(times.indexOf("午")+1,times.length())+"AM";
            }else{
                timesub = times.substring(times.indexOf("午")+1,times.length())+"PM";
            }
            String time=DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(new Date(bean.getTime()));
            tv_history_time.setText(time+"\t "+timesub);
        }
    }
}
