package com.mzth.tangerinepoints.ui.activity.sub.home;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.ContactsBean;
import com.mzth.tangerinepoints.bean.FriendBean;
import com.mzth.tangerinepoints.bean.RFriendBean;
import com.mzth.tangerinepoints.bean.UnregisteredBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.FriendAdapter;
import com.mzth.tangerinepoints.ui.adapter.sub.RegisteredUserAdapter;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 * 通讯录
 */

public class FriendsActivity extends BaseBussActivity {
    private ImageView iv_back;
    private TextView tv_title,tv_friend_phone;
    private MyListView lv_members,lv_members_no;
    private NestedScrollView scroll_friends;
    private ArrayList<ContactsBean> list;//获取手机联系人的电话号码
    private ContactsBean bean;//手机联系人
    private HashMap<String,String> nameMap;//这个集合是为了  根据 电话号码 找到对应的 名字
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = FriendsActivity.this;
        setContentView(R.layout.activity_friends);
    }

    @Override
    protected void initView() {
        super.initView();
        //已经注册的联系人
        lv_members = (MyListView) findViewById(R.id.lv_members);
        //没有注册的联系人
        lv_members_no = (MyListView) findViewById(R.id.lv_members_no);
        scroll_friends = (NestedScrollView) findViewById(R.id.scroll_friends);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        //显示一下  获取到的所有联系人
        tv_friend_phone = (TextView) findViewById(R.id.tv_friend_phone);
        scroll_friends.setFocusable(true);
        lv_members.setFocusable(false);
        lv_members_no.setFocusable(false);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Friends");
        //获取手机联系人
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        GetContacts();
        FriendRequest();
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myclick);
    }

    private View.OnClickListener myclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
            }
        }
    };
    //发现通讯录中已经注册的朋友
    private void FriendRequest(){
        Map<String,Object> map =new HashMap<String,Object>();
        String phone_num="";//初次得到的手机
        String phone="";//多个电话号码的字符串，逗号为分隔符
        for (int i = 0 ; i<list.size(); i++){
            phone_num = list.get(i).getPhone().trim()+",";
            phone +=phone_num;
        }
        //传入多个电话的时候phone.substring(0,phone.length()-1)是为了删掉最后一个,号
        map.put("phone_numbers",phone.substring(0,phone.length()-1));
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.COUPONS_PHONE_NUMBERS, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                String contacts = GsonUtil.getJsonFromKey(json,"contacts");
                String registered = GsonUtil.getJsonFromKey(contacts,"registered");
                String unregistered = GsonUtil.getJsonFromKey(contacts,"unregistered");
                List<RFriendBean> list = GsonUtil.getListFromJson(registered,new TypeToken<List<RFriendBean>>(){});
                List<UnregisteredBean> list2 = GsonUtil.getListFromJson(unregistered,new TypeToken<List<UnregisteredBean>>(){});
                //注册的用户
                lv_members.setAdapter(new RegisteredUserAdapter(_context,list,R.layout.friends_item,nameMap));
                //未注册的用户
                lv_members_no.setAdapter(new FriendAdapter(_context,list2,R.layout.friends_no_item,nameMap,Authorization));
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }
            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                if(statusCode==401){
                    ToastUtil.showLong(_context,"For security reason, please login again.");
                    startActivity(LoginActivity.class,null);
                    onBackPressed();
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }
        });
    }


    //获取手机联系人
    private void GetContacts(){
        list = new ArrayList<ContactsBean>();
        ContactsBean bean = null;
        Cursor cursor = _context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        nameMap = new HashMap<String,String>();
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            bean = new ContactsBean();
            bean.setName(name);
            //去除中间空格
            String t=" ";
            String phone = number.replace(t,"");
            //判断是否包含+86
            String s = "+86";
            String phone_number="";//带有+86的手机号
            if(phone.indexOf("+86")!=-1){
                phone_number = phone.replace(s,"");
                bean.setPhone(phone_number);
                nameMap.put(phone_number,name);
            }else{
                bean.setPhone(phone);
                //将电话号码 和  姓名保存在map中
                nameMap.put(phone,name);
            }
            list.add(bean);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
