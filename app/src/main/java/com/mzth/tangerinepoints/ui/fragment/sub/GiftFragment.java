package com.mzth.tangerinepoints.ui.fragment.sub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.BusinessProfileBean;
import com.mzth.tangerinepoints.bean.LocationBean;
import com.mzth.tangerinepoints.bean.NearbyBean;
import com.mzth.tangerinepoints.bean.NearbyBusinBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.SharedName;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.FetchAddressIntentService;
import com.mzth.tangerinepoints.ui.activity.sub.git.GoogleMapActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.MerchantActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.PermissionUtils;
import com.mzth.tangerinepoints.ui.activity.sub.git.RedeemOfferActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.GiftNearbyAdapter;
import com.mzth.tangerinepoints.ui.fragment.base.BaseFragment;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.ListDataSaveUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 */

public class GiftFragment extends BaseFragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMarkerDragListener,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private LinearLayout ll_nearby;
    private LinearLayout ll_hotest;

    @Override
    protected void doActivityResult(int requestCode, Intent intent) {

    }

    private LinearLayout ll_redeemed;
    private LinearLayout ll_map;
    private View view_nearby,view_hotest,view_redeemed,view_map;
    private MyListView lv_shopp;
    private LinearLayout ll_goole_map;
    private TextView tv_sortng,tv_ok;
    private DrawerLayout drawerLayout;//侧滑
    private ImageView iv_back;
    private List<OffersBean> list;//兑换奖品的listview
    private ImageView iv_selected_one,iv_selected_two,iv_selected_three,
            iv_selected_four,iv_selected_five,iv_selected_six,iv_selected_seven;
    private LinearLayout ll_default,ll_redemptions,ll_redemptions_no,
            ll_distance,ll_distance_no,ll_point,ll_point_no;
    private NestedScrollView scroll_shopp;
    private MaterialRefreshLayout refresh;//刷新
    private GiftNearbyAdapter adapter;
    private ListDataSaveUtil listsave;
    private List<Double> location;
    private Map<Double,Double> maps;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private View popView;// 气泡
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private GoogleMapActivity.AddressResultReceiver mResultReceiver;
    /**
     * 用来判断用户在连接上Google Play services之前是否有请求地址的操作
     */
    private boolean mAddressRequested;
    /**
     * 地图上锚点
     */
    private Marker perth ;
    private LatLng lastLatLng,perthLatLng;
    private boolean points_only,redeemed_only;//只有足够的点,以前的兑换
    private ImageView iv_points_only,iv_redeemed_only;
    private Dialog mWeiboDialog;//加载的动画
    public GiftFragment() {
    }
    @SuppressLint("ValidFragment")
    public GiftFragment(Context context, int resId) {
        super(context, resId);

    }

    @Override
    protected void initView(View v, Bundle savedInstanceState) {
//        ll_nearby= (LinearLayout) v.findViewById(R.id.ll_nearby);
//        ll_hotest= (LinearLayout) v.findViewById(R.id.ll_hotest);
//        ll_redeemed= (LinearLayout) v.findViewById(R.id.ll_redeemed);
        //地图
        ll_map= (LinearLayout) v.findViewById(R.id.ll_map);
        //分类
        tv_sortng = (TextView) v.findViewById(R.id.tv_sortng);
        //足够的点开关
        iv_points_only = (ImageView) v.findViewById(R.id.iv_points_only);
        iv_points_only.setImageResource(R.mipmap.btn_off);
        //以前的兑换开关
        iv_redeemed_only = (ImageView) v.findViewById(R.id.iv_redeemed_only);
        iv_redeemed_only.setImageResource(R.mipmap.btn_off);
//        view_nearby=v.findViewById(R.id.view_nearby);
//        view_hotest=v.findViewById(R.id.view_hotest);
//        view_redeemed=v.findViewById(R.id.view_redeemed);
        //view_map=v.findViewById(R.id.view_map);
        lv_shopp= (MyListView) v.findViewById(R.id.lv_shopp);
        //scrollview
        scroll_shopp = (NestedScrollView) v.findViewById(R.id.scroll_shopp);
        lv_shopp.setFocusable(false);
        scroll_shopp.setFocusable(true);
        //下拉刷新 加载
        refresh = (MaterialRefreshLayout) v.findViewById(R.id.refresh);
        //谷歌地图
        ll_goole_map = (LinearLayout) v.findViewById(R.id.ll_goole_map);
        //初始化谷歌地图
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        //侧滑
        drawerLayout = (DrawerLayout) v.findViewById(R.id.drawerLayout);
        //返回键
        iv_back = (ImageView) v.findViewById(R.id.iv_back);
        //侧滑上的ok键
        tv_ok = (TextView) v.findViewById(R.id.tv_ok);
        //选中图标
        iv_selected_one = (ImageView) v.findViewById(R.id.iv_selected_one);
        iv_selected_two = (ImageView) v.findViewById(R.id.iv_selected_two);
        iv_selected_three = (ImageView) v.findViewById(R.id.iv_selected_three);
        iv_selected_four = (ImageView) v.findViewById(R.id.iv_selected_four);
        iv_selected_five = (ImageView) v.findViewById(R.id.iv_selected_five);
        iv_selected_six = (ImageView) v.findViewById(R.id.iv_selected_six);
        iv_selected_seven = (ImageView) v.findViewById(R.id.iv_selected_seven);
        //侧滑布局的  7个分类
        ll_default = (LinearLayout) v.findViewById(R.id.ll_default);
        ll_redemptions = (LinearLayout) v.findViewById(R.id.ll_redemptions);
        ll_redemptions_no = (LinearLayout) v.findViewById(R.id.ll_redemptions_no);
        ll_distance = (LinearLayout) v.findViewById(R.id.ll_distance);
        ll_distance_no = (LinearLayout) v.findViewById(R.id.ll_distance_no);
        ll_point = (LinearLayout) v.findViewById(R.id.ll_point);
        ll_point_no = (LinearLayout) v.findViewById(R.id.ll_point_no);
    }
    //默认所有选中图标消失
    private void InitialData(){
        iv_selected_one.setVisibility(View.INVISIBLE);
        iv_selected_two.setVisibility(View.INVISIBLE);
        iv_selected_three.setVisibility(View.INVISIBLE);
        iv_selected_four.setVisibility(View.INVISIBLE);
        iv_selected_five.setVisibility(View.INVISIBLE);
        iv_selected_six.setVisibility(View.INVISIBLE);
        iv_selected_seven.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void initData() {
        //默认的第一个显示view横线
        //view_nearby.setVisibility(View.VISIBLE);
        //view_hotest.setVisibility(View.GONE);
        //view_redeemed.setVisibility(View.GONE);
        //view_map.setVisibility(View.GONE);
        //lv_shopp.setAdapter(new GiftNearbyAdapter(_context,list,R.layout.gift_nearby_item));
        //初次进来先禁用侧滑
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //默认选中图标消失
        InitialData();
        iv_selected_one.setVisibility(View.VISIBLE);

        points_only=false;//只有足够的点
        redeemed_only=false;//以前的兑换
        //默认类型
        OffersRequest();
        //接收FetchAddressIntentService返回的结果
        mResultReceiver = new GoogleMapActivity.AddressResultReceiver(new Handler());
        //创建GoogleAPIClient实例
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //取得SupportMapFragment,并在地图准备好后调用onMapReady
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        }

    @Override
    protected void BindComponentEvent() {
//        ll_nearby.setOnClickListener(myonclick);
//        ll_hotest.setOnClickListener(myonclick);
//        ll_redeemed.setOnClickListener(myonclick);
        //侧滑7个分类
        ll_default.setOnClickListener(myonclick);
        ll_redemptions.setOnClickListener(myonclick);
        ll_redemptions_no.setOnClickListener(myonclick);
        ll_distance.setOnClickListener(myonclick);
        ll_distance_no.setOnClickListener(myonclick);
        ll_point.setOnClickListener(myonclick);
        ll_point_no.setOnClickListener(myonclick);

        tv_sortng.setOnClickListener(myonclick);
        ll_map.setOnClickListener(myonclick);
        iv_back.setOnClickListener(myonclick);
        tv_ok.setOnClickListener(myonclick);
        lv_shopp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OffersBean bean= (OffersBean) adapterView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                startActivity(RedeemOfferActivity.class,bundle);
            }
        });
        //两个开关
        iv_points_only.setOnClickListener(myonclick);
        iv_redeemed_only.setOnClickListener(myonclick);
        //设置刷新控件的刷新事件
        refresh.setMaterialRefreshListener(materialRefreshListener);
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        //下拉刷新
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //刷新
            index = 0;
            OffersRequest();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            index += n;
            OffersRequest();

        }
    };
    //结束刷新
    private void finishRefresh(){
        //结束下拉刷新
        refresh.finishRefresh();
        //结束上拉加载
        refresh.finishRefreshLoadMore();
    }

    private View.OnClickListener myonclick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_points_only://点数开关
                    if(points_only){
                        points_only = false;
                        iv_points_only.setImageResource(R.mipmap.btn_off);
                    }else{
                        points_only = true;
                        iv_points_only.setImageResource(R.mipmap.btn);
                    }
                    break;
                case R.id.iv_redeemed_only://兑换开关
                    if(redeemed_only){
                        redeemed_only = false;
                        iv_redeemed_only.setImageResource(R.mipmap.btn_off);
                    }else{
                        redeemed_only = true;
                        iv_redeemed_only.setImageResource(R.mipmap.btn);
                    }
                    break;
//                case R.id.ll_nearby:
//                    view_nearby.setVisibility(View.VISIBLE);
//                    view_hotest.setVisibility(View.GONE);
//                    view_redeemed.setVisibility(View.GONE);
//                    view_map.setVisibility(View.GONE);
//                    //显示谷歌地图
//                    ll_goole_map.setVisibility(View.GONE);
//                    lv_shopp.setVisibility(View.VISIBLE);
//                    OffersRequest();
//                    break;
//                case R.id.ll_hotest:
//                    view_nearby.setVisibility(View.GONE);
//                    view_hotest.setVisibility(View.VISIBLE);
//                    view_redeemed.setVisibility(View.GONE);
//                    view_map.setVisibility(View.GONE);
//                    //显示谷歌地图
//                    ll_goole_map.setVisibility(View.GONE);
//                    lv_shopp.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.ll_redeemed:
//                    view_nearby.setVisibility(View.GONE);
//                    view_hotest.setVisibility(View.GONE);
//                    view_redeemed.setVisibility(View.VISIBLE);
//                    view_map.setVisibility(View.GONE);
//                    //显示谷歌地图
//                    ll_goole_map.setVisibility(View.GONE);
//                    lv_shopp.setVisibility(View.VISIBLE);
//                    break;
                case R.id.ll_default:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_one.setVisibility(View.VISIBLE);
                    //默认类型
                    type = "SCORED";
                    break;
                case R.id.ll_redemptions:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_two.setVisibility(View.VISIBLE);
                    //兑换降序
                    type = "REDEMPTION_DESC";
                    break;
                case R.id.ll_redemptions_no:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_three.setVisibility(View.VISIBLE);
                    //兑换升序
                    type = "REDEMPTION_ASC";
                    break;
                case R.id.ll_distance:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_four.setVisibility(View.VISIBLE);
                    //距离降序
                    type = "DISTANCE_DESC";
                    break;
                case R.id.ll_distance_no:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_five.setVisibility(View.VISIBLE);
                    //距离升序
                    type = "DISTANCE_ASC";
                    break;
                case R.id.ll_point:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_six.setVisibility(View.VISIBLE);
                    //点数降序
                    type = "POINTS_DESC";
                    break;
                case R.id.ll_point_no:
                    //默认选中图标消失
                    InitialData();
                    iv_selected_seven.setVisibility(View.VISIBLE);
                    //点数升序
                    type = "POINTS_ASC";
                    break;

                case R.id.tv_sortng://分类
                    //点击出现侧滑
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;

                case R.id.tv_ok://侧滑上的View Results
                    //关闭侧滑
                    drawerLayout.closeDrawers();
                    //发送可兑换奖品的请求
                    OffersRequest();
                    break;
                case R.id.ll_map://地图
                    //GoogleApiAvailability
                    //显示谷歌地图
                    ll_goole_map.setVisibility(View.VISIBLE);
                    refresh.setVisibility(View.GONE);
                    iv_back.setVisibility(View.VISIBLE);
                    tv_sortng.setText("Back to List View");
                    if(!StringUtil.isEmpty(mMap)){
                        NearbyShopsRequest();//附近的商店显示的坐标
                    }
//                    double latitude=0.0;
//                    double longitude=0.0;
//                    LatLng cenpt = null;
//                    for (int i=0;i<location.size();i++){
//                        latitude = location.get(i);//获取纬度
//                        longitude = maps.get(location.get(i));//获取经度
//                        cenpt = new LatLng(latitude,longitude);//显示位置
//                        //添加标记
//                        perth = mMap.addMarker(new MarkerOptions().position(cenpt));
//                    }
                    //让小图标消失
                    tv_sortng.setCompoundDrawables(null,null,null,null);
                    //禁止监听
                    tv_sortng.setClickable(false);
                    //禁用侧滑
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case R.id.iv_back://返回键
                    tv_sortng.setText("Sortng Options");
                    iv_back.setVisibility(View.GONE);
                    ll_goole_map.setVisibility(View.GONE);
                    refresh.setVisibility(View.VISIBLE);
                    //开启监听
                    tv_sortng.setClickable(true);
                    //让小图标显示
                    Drawable drawable = getResources().getDrawable(R.mipmap.icon08);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_sortng.setCompoundDrawables(drawable,null,null,null);
                    break;
            }
        }
    };


    //附近的商店请求
    private void NearbyShopsRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("location",Constans.location);//当前位置
        //可视范围
        LatLngBounds curScreen = mMap.getProjection()
                .getVisibleRegion().latLngBounds.including(lastLatLng);
        LatLng northeast = curScreen.northeast;//东北角
        LatLng southwest = curScreen.southwest;//西南角
        String dong = northeast.latitude+","+northeast.longitude;
        String xi = southwest.latitude+","+southwest.longitude;
        //map.put("boundry_northeast",dong);//东北角
        //map.put("boundry_southwest",xi);//西南角
        map.put("boundry_northeast",Constans.boundryNorthEast);//右上角
        map.put("boundry_southwest",Constans.boundrySouthWest);//左下角
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.BUSINESSES, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String business = GsonUtil.getJsonFromKey(json,"businesses");
                List<NearbyBusinBean> listBusiness = GsonUtil.getListFromJson(business,new TypeToken<List<NearbyBusinBean>>(){});
                //在地图上显示坐标
                double latitude=0.0;
                double longitude=0.0;
                LatLng cenpt = null;
                for (int i=0;i<listBusiness.size();i++){
                    latitude = listBusiness.get(i).getGeoLocation().getLatitude();//获取纬度
                    longitude = listBusiness.get(i).getGeoLocation().getLongitude();//获取经度
                    cenpt = new LatLng(latitude,longitude);//显示位置
                    //添加标记  地铺名称和地址都传过去
                    addMarker(listBusiness.get(i),cenpt,listBusiness.get(i).getBusinessName(),listBusiness.get(i).getFullAddress());
                    //perth = mMap.addMarker(new MarkerOptions().position(cenpt));
                }

            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){//错误信息
                    case "INVALID_LOCATION_DATA":
                        ToastUtil.showShort(_context,Constans.INVALID_LOCATION_DATA);
                        break;
                    case "Unauthorized":
                        ToastUtil.showLong(_context,"For security reason, please login again.");
                        startActivity(LoginActivity.class,null);
                        getActivity().finish();
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }
        });
    }
    //可兑换的奖品请求
    private void OffersRequest(){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sorting_option",type);
        map.put("enough_points_only",points_only);
        map.put("previously_redeemed_only",redeemed_only);
        map.put("start_index",index);
        map.put("n",n);
        //获取当前位置
        //String location = (String) SharedPreferencesUtil.getParam(_context, SharedName.LOCATION,"");
        map.put("location", Constans.location);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.OFFERS, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String offers = GsonUtil.getJsonFromKey(json,"offers");
                list = GsonUtil.getListFromJson(offers,new TypeToken<List<OffersBean>>(){});
                location = new ArrayList<Double>();
                maps = new HashMap<Double, Double>();
                for (int i = 0 ; i < list.size(); i++){
                    //得到经纬度
                    LocationBean locationBean = list.get(i).getNearbyBusiness().getGeoLocation();
                    location.add(locationBean.getLatitude());
                    maps.put(locationBean.getLatitude(),locationBean.getLongitude());
                }
                setDataToView(list);
                //结束刷新加载
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {

                switch (errorMsg){//错误信息
                    case "QR_CODE_GENERATOR_EXCEPTION":
                        ToastUtil.showShort(_context,Constans.QR_CODE_GENERATOR_EXCEPTION);
                        break;
                    case "INVALID_LOCATION_DATA":
                        ToastUtil.showShort(_context,Constans.INVALID_LOCATION_DATA);
                        break;
                    case "Unauthorized":
                        ToastUtil.showLong(_context,"For security reason, please login again.");
                        startActivity(LoginActivity.class,null);
                        getActivity().finish();
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
                //结束刷新加载
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                //结束刷新加载
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }
        });
    }
    //加载
    private void setDataToView(List<OffersBean> list){
        if(index==0){
            if(!StringUtil.isEmpty(list)){
                if(adapter==null){
                    adapter = new GiftNearbyAdapter(_context,list,R.layout.gift_nearby_item);
                    lv_shopp.setAdapter(adapter);
                }else{
                    adapter.clearAll();
                    adapter.add(list);
                }
            }else{
                if(adapter != null){
                    adapter.clearAll();
                }
                ToastUtil.showShort(_context, "No Information");
            }
        }else{
            if(!StringUtil.isEmpty(list)){
                adapter.add(list);
                if(list.size()<n){//如果 list的大小小于n返回的条数说明数据加载完毕
                    ToastUtil.showShort(_context, "Data Loaded");
                }
            }else {
                ToastUtil.showShort(_context, "Data Loaded");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerDragListener(this);
        //可视范围
        LatLngBounds curScreen = mMap.getProjection()
                .getVisibleRegion().latLngBounds;
        LatLng northeast = curScreen.northeast;//东北角
        LatLng southwest = curScreen.southwest;//西南角
        String dong = northeast.latitude+","+northeast.longitude;
        String xi = southwest.latitude+","+southwest.longitude;
        enableMyLocation();
    }

    /**
     * 检查是否已经连接到 Google Play services
     */
    private void checkIsGooglePlayConn() {
        Log.i("MapsActivity", "checkIsGooglePlayConn-->" +mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        }
        mAddressRequested = true;
    }
    /**
     * 启动地址搜索Service
     */
    protected void startIntentService(LatLng latLng) {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, latLng);
        _context.startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            Toast.makeText(getActivity(),"Permission to access the location is missing.",Toast.LENGTH_LONG).show();
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    /**
     * 如果取得了权限,显示地图定位层
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("MapsActivity", "--onConnected--" );
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(),"Permission to access the location is missing.",Toast.LENGTH_LONG).show();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            displayPerth(true,lastLatLng);
            initCamera(lastLatLng);
            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), "No geocoder available",Toast.LENGTH_LONG).show();
                return;
            }
            if (mAddressRequested) {
                startIntentService(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
            }
        }
    }
    /**
     * '我的位置'按钮点击时的调用
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        if (mLastLocation != null) {
            Log.i("MapsActivity", "Latitude-->" + String.valueOf(mLastLocation.getLatitude()));
            Log.i("MapsActivity", "Longitude-->" + String.valueOf(mLastLocation.getLongitude()));
        }
        if (lastLatLng != null)
            perth.setPosition(lastLatLng);
        checkIsGooglePlayConn();
        return false;
    }
    /**
     * 添加标记
     */
    private void displayPerth(boolean isDraggable,LatLng latLng) {
        if (perth==null){
            perth = mMap.addMarker(new MarkerOptions().position(latLng));

//            LatLng cenpt1 = new LatLng(39.917723,116.3722);
//            perth = mMap.addMarker(new MarkerOptions().position(cenpt1));
//            LatLng cenpt2 = new LatLng(39.90923,116.397428);
//            perth = mMap.addMarker(new MarkerOptions().position(cenpt2));
//            LatLng cenpt3 = new LatLng(39.9022,116.3922);
//            perth = mMap.addMarker(new MarkerOptions().position(cenpt3));
//            LatLng cenpt4 = new LatLng(39.915,116.404);
//            perth = mMap.addMarker(new MarkerOptions().position(cenpt4));

            perth.setDraggable(isDraggable); //设置可移动
        }

    }
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * 将地图视角切换到定位的位置
     */
    private void initCamera(final LatLng sydney) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
                    }
                });
            }
        }).start();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        perthLatLng = marker.getPosition() ;
        startIntentService(perthLatLng);
    }
//-------------------------谷歌地图添加气泡----------------------------
        public Marker addMarker(final NearbyBusinBean bean, LatLng position, final String name, final String address) {
            Marker marker = null;
            //mMap
            if ( mMap != null ) {
                MarkerOptions options = new MarkerOptions();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource( R.mipmap.map_icon01 );
                options.icon( icon ); options.position( position );
                marker = mMap.addMarker( options );
            }
            //气泡的点击事件
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BusinBean",bean);
                    //bundle.putInt("tag",1);
                    startActivity(MerchantActivity.class,bundle);
                }
            });
            //气泡的适配器
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    popView = LayoutInflater.from(_context).inflate(R.layout.map_bubble_view,null);
                    TextView tv_shop_name = (TextView) popView.findViewById(R.id.tv_shop_name);
                    TextView tv_shop_address = (TextView) popView.findViewById(R.id.tv_shop_address);
                    tv_shop_name.setText(name);//设置商店名
                    tv_shop_address.setText(address);//显示商店地址
                    return popView;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    return null;
                }
            });
            return marker;
        }


}



