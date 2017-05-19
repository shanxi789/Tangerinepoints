package com.mzth.tangerinepoints.ui.fragment.sub;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.CouponBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.activity.sub.home.FriendsActivity;
import com.mzth.tangerinepoints.ui.activity.sub.user.RedeemCouponActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.CouponAdapter;
import com.mzth.tangerinepoints.ui.fragment.base.BaseFragment;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class UserFragment extends BaseFragment {
    private ListView lv_coupon;
    private TextView tv_commit,tv_user_coppon_on;
    private NestedScrollView scroll_coupon;
    private LinearLayout ll_coupon_bottom;
    public UserFragment() {
    }
    @SuppressLint("ValidFragment")
    public UserFragment(Context context, int resId) {
        super(context, resId);
    }

    @Override
    protected void initView(View v, Bundle savedInstanceState) {
        //优惠券
        lv_coupon = (ListView) v.findViewById(R.id.lv_coupon);
        //邀请朋友加入获得更多的优惠券
        tv_commit = (TextView) v.findViewById(R.id.tv_commit);
        //底部导航
        ll_coupon_bottom = (LinearLayout) v.findViewById(R.id.ll_coupon_bottom);
        //未显示优惠券时显示的view
        tv_user_coppon_on = (TextView) v.findViewById(R.id.tv_user_coppon_on);
        //解决底部导航遮挡listview最后一条item
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        ll_coupon_bottom.measure(w,h);
        int height=ll_coupon_bottom.getMeasuredHeight();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, height);
        lv_coupon.setLayoutParams(lp);

        //scroll_coupon = (NestedScrollView) v.findViewById(R.id.scroll_coupon);
        //scroll_coupon.setFocusable(true);
        //lv_coupon.setFocusable(false);
    }

    @Override
    protected void initData() {
        CouponsAvailableRequest();//获取可以使用的优惠券
    }

    @Override
    protected void BindComponentEvent() {
        //点击优惠券的条目
        lv_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CouponBean bean = (CouponBean) adapterView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                startActivityForResult(RedeemCouponActivity.class,bundle,100);
            }
        });
        tv_commit.setOnClickListener(myclick);
    }

    private View.OnClickListener myclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_commit:////邀请朋友加入获得更多的优惠券
                    startActivity(FriendsActivity.class,null);
                    break;
            }
        }
    };
    //获取可以使用的优惠券
    private void CouponsAvailableRequest(){
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(_context,"Loading...");
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.COUPONS_AVAILABLE, null, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                String coupons = GsonUtil.getJsonFromKey(json,"coupons");
                List<CouponBean> list  = GsonUtil.getListFromJson(coupons,new TypeToken<List<CouponBean>>(){});
                if(!StringUtil.isEmpty(list)){
                lv_coupon.setAdapter(new CouponAdapter(_context,list,R.layout.fragment_user_item));
                }else{
                    tv_user_coppon_on.setVisibility(View.VISIBLE);//显示文字view
                }
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
                }

            @Override
            public void onFailure(int statusCode, String errorMsg) {

                switch (errorMsg){//错误信息
                    case "QR_CODE_GENERATOR_EXCEPTION":
                        ToastUtil.showShort(_context,Constans.QR_CODE_GENERATOR_EXCEPTION);
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
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }
        });
    }
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        switch (requestCode){
            case 100:
                CouponsAvailableRequest();
                break;
        }
    }


}
