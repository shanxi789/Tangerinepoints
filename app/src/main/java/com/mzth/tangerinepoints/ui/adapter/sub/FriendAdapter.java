package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.UnregisteredBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 */

public class FriendAdapter extends BaseInfoAdapter<UnregisteredBean> {
    private HashMap<String,String> nameList;//这个集合是为了  根据 电话号码 找到对应的 名字
    private String Authorization;//消息头
    private String locale;//国家
    private String Code;//电码
    public FriendAdapter(Context context, List<UnregisteredBean> list, int resId,HashMap<String,String> nameList,String Authorization) {
        super(context, list, resId);
        this.nameList = nameList ;
        this.Authorization = Authorization;
    }

    @Override
    public View dealView(Context context, List<UnregisteredBean> list, int resId, int position, View convertView) {
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
        private UnregisteredBean bean;
        private Button btn_invite;//邀请好友
        private void initView(View v){
            iv_friends_head = (CircleImageView) v.findViewById(R.id.iv_friends_head);
            tv_friends_name = (TextView) v.findViewById(R.id.tv_friends_name);
            tv_friends_mark = (TextView) v.findViewById(R.id.tv_friends_mark);
            btn_invite = (Button) v.findViewById(R.id.btn_invite);
        }
        private void initData(List<UnregisteredBean> list, int position){
            bean = list.get(position);
            iv_friends_head.setImageResource(R.mipmap.head01);
            tv_friends_name.setText(bean.getPhoneNumber());
            //显示电话号码对应的联系人名字
            String num = bean.getPhoneNumber();
            String number = "";
            if(num.indexOf("+86")!=-1){
                number = num.replace("+86","");
            }else if(num.indexOf("+1")!=-1){
                number = num.replace("+1","");
            }
            tv_friends_mark.setText(nameList.get(number));
            //点击邀请好友
            btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获得当前国家号
                    locale = _context.getResources().getConfiguration().locale.getCountry();
                    InviteFriendsRequest();
                }
            });

        }
        //邀请朋友（短信） 请求
        private void InviteFriendsRequest(){
            Map<String,Object> map = new HashMap<String,Object>();

            if(locale.equals("CN")){
                Code = "+86";
            }else if(locale.equals("US")||locale.equals("CA")){
                Code = "+1";
            }
            String PhoneNumber = bean.getPhoneNumber();
            //朋友的手机号
            map.put("target_number",PhoneNumber);
            NetUtil.Request(NetUtil.RequestMethod.POST, Constans.CONTACTS_INVITE_SMS, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
                @Override
                public void onSuccess(int statusCode, String json) {
                    ToastUtil.showShort(_context,"Invite Success");
                }

                @Override
                public void onFailure(int statusCode, String errorMsg) {
                    switch (errorMsg){//错误信息
                        case "ALREADY_INVITED":
                            ToastUtil.showLong(_context,Constans.ALREADY_INVITED);
                            break;
                        case "SMS_EXCEPTION":
                            ToastUtil.showLong(_context,Constans.SMS_EXCEPTION);
                            break;
                        default:
                            ToastUtil.showLong(_context,errorMsg);
                            break;
                    }
                }

                @Override
                public void onFailure(Exception e, String errorMsg) {
                    ToastUtil.showLong(_context,errorMsg);
                }
            });
        }


    }
}
