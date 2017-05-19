package com.mzth.tangerinepoints.ui.activity.sub.git;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.BusinessProfileBean;
import com.mzth.tangerinepoints.bean.LocationBean;
import com.mzth.tangerinepoints.bean.NearbyBusinBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.GridMerImageAdapter;
import com.mzth.tangerinepoints.ui.adapter.sub.MerchantAdapter;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.MyGridView;
import com.mzth.tangerinepoints.widget.MyListView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/21.
 * 店铺详情页面
 */

public class MerchantActivity extends BaseBussActivity {
    private NestedScrollView scroll_merchant;
    private MyListView lv_merchant;
    private TextView tv_title,tv_merchant_name,tv_merchant_type,
            tv_merchant_address,tv_hours;
    private ImageView iv_back,iv_title_merchant;
    private NearbyBusinBean BusinBean;
    private MyGridView gv_shopp_photo;
    private Dialog mWeiboDialog;//加载的动画
    private BusinessProfileBean ProfileBean;//商店对象
    private ImageView iv_location;//定位图标
    private LocationBean locationBean;//定位对象
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = MerchantActivity.this;
        setContentView(R.layout.activity_merchant);
    }

    @Override
    protected void initView() {
        super.initView();
        scroll_merchant = (NestedScrollView) findViewById(R.id.scroll_merchant);
        //商品的MyListView
        lv_merchant = (MyListView) findViewById(R.id.lv_merchant);
        //设置标题
        tv_title = (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //顶部图片
        iv_title_merchant = (ImageView) findViewById(R.id.iv_title_merchant);
        //商店名字
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        //商店类型
        tv_merchant_type = (TextView) findViewById(R.id.tv_merchant_type);
        //地址
        tv_merchant_address = (TextView) findViewById(R.id.tv_merchant_address);
        //营业时间
        tv_hours = (TextView) findViewById(R.id.tv_hours);
        //展示图片的gridview
        gv_shopp_photo = (MyGridView) findViewById(R.id.gv_shopp_photo);
        //定位图标
        iv_location = (ImageView) findViewById(R.id.iv_location);
        scroll_merchant.setFocusable(true);
        lv_merchant.setFocusable(false);
        gv_shopp_photo.setFocusable(false);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Merchant");
        BusinBean = (NearbyBusinBean) getIntent().getSerializableExtra("BusinBean");
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        MerchantDetailRequest();//店铺详情请求
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        //展示商品listview的点击时间
        lv_merchant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OffersBean offersBean = (OffersBean) adapterView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",offersBean);
                startActivity(RedeemOfferActivity.class,bundle);
            }
        });
        //点击顶部图片查看大图
        iv_title_merchant.setOnClickListener(myonclick);
        //定位图标
        iv_location.setOnClickListener(myonclick);

    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键监听
                    finish();
                    break;
                case R.id.iv_title_merchant://查看大图
                    Bundle bundle = new Bundle();
                    bundle.putString("image",ProfileBean.getMainPicture());
                    bundle.putInt("tag",2);
                    startActivity(PhotoPreviewActivity.class,bundle);
                    break;
                case R.id.iv_location://定位图标
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("bean",locationBean);
                    startActivity(GoogleMapActivity.class,bundle1);
                    break;
            }
        }
    };
    //初始化数据
    private void initProfileBeanData(BusinessProfileBean bean){
        //初始化商店名字,类型,地址
        tv_merchant_name.setText(bean.getBusinessName());
        tv_merchant_type.setText(bean.getBusinessTypeId()+bean.getDistance()+"mi");
        tv_merchant_address.setText(bean.getFullAddress());
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String start = null;
        String end = null;
        switch (week){
            case 1://周日
                start = bean.getBusinessHours().getSUNDAY().getStart();//开始时间
                end = bean.getBusinessHours().getSUNDAY().getEnd();//结束时间
                break;
            case 2://周1
                start = bean.getBusinessHours().getMONDAY().getStart();//开始时间
                end = bean.getBusinessHours().getMONDAY().getEnd();//结束时间
                break;
            case 3://周2
                start = bean.getBusinessHours().getTUESDAY().getStart();//开始时间
                end = bean.getBusinessHours().getTUESDAY().getEnd();//结束时间
                break;
            case 4://周3
                start = bean.getBusinessHours().getWEDNESDAY().getStart();//开始时间
                end = bean.getBusinessHours().getWEDNESDAY().getEnd();//结束时间
                break;
            case 5://周4
                start = bean.getBusinessHours().getTHURSDAY().getStart();//开始时间
                end = bean.getBusinessHours().getTHURSDAY().getEnd();//结束时间
                break;
            case 6://周5
                start = bean.getBusinessHours().getFRIDAY().getStart();//开始时间
                end = bean.getBusinessHours().getFRIDAY().getEnd();//结束时间
                break;
            case 7://周6
                start = bean.getBusinessHours().getSATURDAY().getStart();//开始时间
                end = bean.getBusinessHours().getSATURDAY().getEnd();//结束时间
                break;
        }
        //营业时间
        String begin = "";//开始时间
        if(Integer.parseInt(start.substring(0,start.indexOf(":")))<12){
            begin = start.substring(0,start.indexOf(":"))+"AM";
        }else{
            begin = start.substring(0,start.indexOf(":"))+"PM";
        }
        String last = "";//结束时间
        if(Integer.parseInt(end.substring(0,start.indexOf(":")))<12){
            last = end.substring(0,start.indexOf(":"))+"AM";
        }else{
            last = end.substring(0,start.indexOf(":"))+"PM";
        }
        tv_hours.setText("Today's Business Hours "+begin+"-"+last);
        //顶部图片
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getMainPicture()+"/800/200").placeholder(R.mipmap.ic_launcher).into(iv_title_merchant);


    }
    //商店详细信息
    private void MerchantDetailRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("location", Constans.location);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.BUSINESSES_DETAIL+BusinBean.getBusinessId(), map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String businessProfile = GsonUtil.getJsonFromKey(json,"businessProfile");
                String business = GsonUtil.getJsonFromKey(businessProfile,"business");
                String offers = GsonUtil.getJsonFromKey(businessProfile,"offers");
                String location = GsonUtil.getJsonFromKey(business,"geoLocation");
                //定位对象
                locationBean = GsonUtil.getBeanFromJson(location,LocationBean.class);

                ProfileBean = GsonUtil.getBeanFromJson(business, BusinessProfileBean.class);
                initProfileBeanData(ProfileBean);//初始化数据
                List<String> image = ProfileBean.getPictures();
                //展示商品的gridview
                gv_shopp_photo.setAdapter(new GridMerImageAdapter(_context,image,R.layout.merchant_grid_item));
                //展示商品的listview
                List<OffersBean> OffersBean = GsonUtil.getListFromJson(offers,new TypeToken<List<OffersBean>>(){});
                lv_merchant.setAdapter(new MerchantAdapter(_context,OffersBean,R.layout.merchant_item));
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
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
                        onBackPressed();
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }
        });
    }
}
