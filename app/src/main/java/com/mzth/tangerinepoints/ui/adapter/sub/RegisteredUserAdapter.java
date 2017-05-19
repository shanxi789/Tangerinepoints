package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.RFriendBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.SharedName;
import com.mzth.tangerinepoints.ui.activity.sub.home.TransferBalanceActivity;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.widget.CircleImageView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 * 通讯录中注册的朋友的适配器
 */

public class RegisteredUserAdapter extends BaseInfoAdapter<RFriendBean> {
    private HashMap<String,String> nameList;//这个集合是为了  根据 电话号码 找到对应的 名字
    public RegisteredUserAdapter(Context context, List<RFriendBean> list, int resId,HashMap<String,String> nameList) {
        super(context, list, resId);
        this.nameList = nameList;
    }

    @Override
    public View dealView(Context context, List<RFriendBean> list, int resId, int position, View convertView) {
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
        private CircleImageView iv_friends_head;
        private TextView tv_friends_name,tv_friends_mark;
        private ImageView iv_convert_friend;
        private void initView(View v){
            iv_friends_head = (CircleImageView) v.findViewById(R.id.iv_friends_head);
            tv_friends_name = (TextView) v.findViewById(R.id.tv_friends_name);
            tv_friends_mark = (TextView) v.findViewById(R.id.tv_friends_mark);
            iv_convert_friend = (ImageView) v.findViewById(R.id.iv_convert_friend);
        }
        private void initData(final List<RFriendBean> list, int position){
            final RFriendBean bean = list.get(position);
            //显示头像
            Glide.with(_context).load(Constans.PHOTO_PATH+bean.getPictureKey()+"/100/100").dontAnimate().placeholder(R.mipmap.ic_launcher).into(iv_friends_head);
            //iv_friends_head.setImageResource(R.mipmap.head01);
            //显示电话号码对应的联系人名字
            String num = bean.getPhoneNumber();
            String number = "";
            if(num.indexOf("+86")!=-1){
                number = num.replace("+86","");
            }else if(num.indexOf("+1")!=-1){
                number = num.replace("+1","");
            }
            final String BookName = nameList.get(number);
            tv_friends_name.setText(BookName);
            //显示app中的名称
            tv_friends_mark.setText(bean.getScreenName());
            //转积分给朋友
            iv_convert_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((String)SharedPreferencesUtil.getParam(_context, SharedName.CustomerId,"")).equals(bean.getUserId())){
                        ToastUtil.showShort(_context,"You can't forward points for yourself");
                        return;
                    }
                    Intent intent = new Intent(_context,TransferBalanceActivity.class);
                    intent.putExtra("bean", (Serializable) bean);
                    intent.putExtra("BookName",BookName);
                   _context.startActivity(intent);
                }
            });
        }


}
}
