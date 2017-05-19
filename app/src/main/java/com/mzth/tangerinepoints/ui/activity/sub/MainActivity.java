package com.mzth.tangerinepoints.ui.activity.sub;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.bean.RefreshUserBean;
import com.mzth.tangerinepoints.bean.UserBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.MainApplication;
import com.mzth.tangerinepoints.common.MyFirebaseInstanceIDService;
import com.mzth.tangerinepoints.common.SharedName;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.activity.sub.home.FriendsActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.FragmentAdapter;
import com.mzth.tangerinepoints.ui.adapter.sub.HomeMoreAdapter;
import com.mzth.tangerinepoints.ui.fragment.base.BaseFragment;
import com.mzth.tangerinepoints.ui.fragment.sub.GiftFragment;
import com.mzth.tangerinepoints.ui.fragment.sub.HomeFragment;
import com.mzth.tangerinepoints.ui.fragment.sub.UserFragment;
import com.mzth.tangerinepoints.util.BinaryHexConverter;
import com.mzth.tangerinepoints.util.CacheUtil;
import com.mzth.tangerinepoints.util.DialogUtil;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.ListDataSaveUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseBussActivity implements BaseFragment.FragmentCallBack {
    private long mExitTime;
    private ViewPager vp_main;
    private ImageView iv_home, iv_gift, iv_user, iv_more;//顶部四张导航图
    private RelativeLayout rl_gift, rl_user;
    private LinearLayout rl_home;
    private Fragment homeFrag, giftFrag, userFrag;
    private FragmentAdapter adapter;
    private List<Fragment> fragments;
    private PopupWindow mPopupWindow;//更多显示的popupwindow
    private View popupView;
    private ListView lv_more;
    private TextView tv_integral;//用户积分
    private UserBean bean;//用户数据
    private RefreshUserBean refreshUserBean;//首页的刷新用户
    private List<OffersBean> list;//兑换的奖品
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        setContentView(R.layout.activity_home);
        _context = MainActivity.this;
        popupView = getLayoutInflater().inflate(R.layout.activity_more_popupwindow, null);
        //获取整个屏幕的宽和高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        mPopupWindow = new PopupWindow(popupView, width / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);//可触摸
        mPopupWindow.setOutsideTouchable(true);//点击屏幕外的地方关闭
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//背景

    }

    @Override
    protected void initView() {
        super.initView();
        //主页viewpager
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_gift = (ImageView) findViewById(R.id.iv_gift);
        iv_user = (ImageView) findViewById(R.id.iv_user);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        rl_home = (LinearLayout) findViewById(R.id.rl_home);
        rl_gift = (RelativeLayout) findViewById(R.id.rl_gift);
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        //用户积分
        tv_integral = (TextView) findViewById(R.id.tv_integral_home);
        //设置首次进来第一个图标为选中状态
        iv_home.setImageDrawable(_context.getResources().getDrawable(
                R.mipmap.top_icon01_on));
        //显示更多
        lv_more = (ListView) popupView.findViewById(R.id.lv_more);

    }

    private void Location() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            ToastUtil.showShort(_context, "There is no location provider available");
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,保存地理位置经纬度
            //ToastUtil.showShort(_context,location.getLatitude() + "," + location.getLongitude());
            SharedPreferencesUtil.setParam(_context, SharedName.LOCATION,
                    location.getLatitude() + "," + location.getLongitude());
        }
        //监视地理位置变化
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新保存
            SharedPreferencesUtil.setParam(_context,SharedName.LOCATION,
                    location.getLatitude()+","+location.getLongitude());
        }
    };
    @Override
    protected void initData() {
        super.initData();
        Location();//当前位置
        //接收登录成功之后传过来的用户数据
        Intent intent = new Intent(_context,MyFirebaseInstanceIDService.class);
        startService(intent);
        //Intent intent = getIntent();
        //bean = (UserBean) intent.getSerializableExtra("userbean");
        RrofileRequest();//刷新用户信息  此处只是为了设置积分
        //首次进来将标题下面的积分设置成红色
        tv_integral.setTextColor(getResources().getColor(R.color.red));
        FragmentManager fm = this.getSupportFragmentManager();
        fragments = new ArrayList<Fragment>();
        homeFrag=new HomeFragment(_context,R.layout.fragment_home,bean,tv_integral);
        giftFrag=new GiftFragment(_context,R.layout.fragment_gift);
        userFrag=new UserFragment(_context,R.layout.fragment_user);
        fragments.add(homeFrag);
        fragments.add(giftFrag);
        fragments.add(userFrag);
        adapter=new FragmentAdapter(fm,_context,fragments);
        vp_main.setAdapter(adapter);
    }
    //刷新用户信息
    private void RrofileRequest(){
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.REFRESH_PROFILE, null, (String)SharedPreferencesUtil.getParam(_context,"accessKey",""), Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String user = GsonUtil.getJsonFromKey(json,"user");
                refreshUserBean=GsonUtil.getBeanFromJson(user,RefreshUserBean.class);
                //设置积分
                int integral = refreshUserBean.getPointBalance();
                tv_integral.setText(integral+"");
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }
        });

    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        vp_main.addOnPageChangeListener(mOnPageChangeListener);
        rl_home.setOnClickListener(mOnClickListener);
        rl_gift.setOnClickListener(mOnClickListener);
        rl_user.setOnClickListener(mOnClickListener);
        iv_more.setOnClickListener(mOnClickListener);
        //给PopupWindow的listview设置点击事件
        lv_more.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                mPopupWindow.dismiss();
                switch (item){
                    case "Edit Profile":
                        RrofileRequest();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UserBean",refreshUserBean);
                        //startActivity(EditProfileActivity.class,bundle);//自定义带图片的点击头像对话框
                        startActivityForResult(EditInfoActivity.class,bundle,200);//仿QQ的点击头像对话框
                        break;
                    case "Friends&Invite":
                        if(hasSimCard()){//判断SIM卡是否存在
                            startActivity(FriendsActivity.class,null);
                        }else{
                            ToastUtil.showShort(_context,"No SIM card");
                        }
                        break;
                    case "Help&Feedback":
                        //ToastUtil.showShort(_context,"3");
                        startActivity(HelpFeedbackActivity.class,null);
                        break;
                    case "Terms of Service":
                        //ToastUtil.showShort(_context,"4");
                        startActivity(TermsOfServiceActivity.class,null);
                        break;
                    case "Check Update":
                        startActivity(VersionActivity.class,null);
                        break;
                    case "Sign Out"://点击退出登录弹出一个对话框
                        DialogUtil.alertDialog(_context, "Prompt", "Are you sure you want to exit?",
                                "Confirm", "Cancel", true, new DialogUtil.ReshActivity() {
                                    @Override
                                    public void reshActivity() {//确定按钮
                                        //将登录成功后返回的accesskey保存在sp中
                                    SharedPreferencesUtil.setParam(_context, SharedName.AccessKey,"");
                                    Authorization =null;
                                    BaseFragment.Authorization = null;
                                    //清空用户信息
                                    CacheUtil.putValue(_context,SharedName.USERBEAN,"");
                                //startActivity(LoginActivity.class,null);
                                //finish();
                                    }
                                });
                        break;
                }
            }
        });
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
           switch (position){
               case 0:
                   resetView();
                   //将导航栏里的积分设置成红色
                   tv_integral.setTextColor(getResources().getColor(R.color.red));
                   vp_main.setCurrentItem(0);
                   iv_home.setImageDrawable(_context.getResources().getDrawable(
                           R.mipmap.top_icon01_on));
                   break;
               case 1:
                   resetView();
                   //将导航栏里的积分设置成灰色
                   tv_integral.setTextColor(getResources().getColor(R.color.gray));
                   vp_main.setCurrentItem(1);
                   iv_gift.setImageDrawable(_context.getResources().getDrawable(
                           R.mipmap.top_icon02_on));
                   break;
               case 2:
                   resetView();
                   //将导航栏里的积分设置成灰色
                   tv_integral.setTextColor(getResources().getColor(R.color.gray));
                   vp_main.setCurrentItem(2);
                   iv_user.setImageDrawable(_context.getResources().getDrawable(
                           R.mipmap.top_icon03_on));
                   break;
           }
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private View.OnClickListener mOnClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_home://首页
                    vp_main.setCurrentItem(0);
                    //将导航栏里的积分设置成红色
                    tv_integral.setTextColor(getResources().getColor(R.color.red));
                    break;
                case R.id.rl_gift://礼物
                    vp_main.setCurrentItem(1);
                    //将导航栏里的积分设置成灰色
                    tv_integral.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case R.id.rl_user://用户
                    vp_main.setCurrentItem(2);
                    //将导航栏里的积分设置成灰色
                    tv_integral.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case R.id.iv_more://更多
                    mPopupWindow.showAsDropDown(view);
                    //更多的数据
                    List list=new ArrayList(){};
                    list.add("Edit Profile");
                    list.add("Friends&Invite");
                    list.add("Help&Feedback");
                    list.add("Terms of Service");
                    list.add("Check Update");
                    list.add("Sign Out");
                    //给PopupWindow的listview设置适配器
                    lv_more.setAdapter(new HomeMoreAdapter(_context,list,R.layout.activity_more_popupwindow_item));
                    break;
            }
        }
    };
    // 重置按钮背景
    private void resetView() {
        iv_home.setImageDrawable(_context.getResources().getDrawable(
                R.drawable.main_home));
        iv_gift.setImageDrawable(_context.getResources().getDrawable(
                R.drawable.main_school));
        iv_user.setImageDrawable(_context.getResources().getDrawable(
                R.drawable.main_user));
    }
    @Override
    public void setResult(Object... param) {

    }
    /** * 判断是否包含SIM卡 * * @return 状态 */
    public boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager)
                _context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        super.doActivityResult(requestCode, intent);
        switch (requestCode){
            case 200://编辑个人信息的回调
                RrofileRequest();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.showShort(this, "Press logout again");
            mExitTime = System.currentTimeMillis();
        } else {
            MainApplication.closeActivity();
        }
    }
}
