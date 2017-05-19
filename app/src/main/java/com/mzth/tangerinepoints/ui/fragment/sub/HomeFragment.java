package com.mzth.tangerinepoints.ui.fragment.sub;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.RefreshUserBean;
import com.mzth.tangerinepoints.bean.UserBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.common.SharedName;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.activity.sub.git.PhotoPreviewActivity;
import com.mzth.tangerinepoints.ui.activity.sub.home.FriendsActivity;
import com.mzth.tangerinepoints.ui.activity.sub.home.HistoryActivity;
import com.mzth.tangerinepoints.ui.fragment.base.BaseFragment;
import com.mzth.tangerinepoints.util.BinaryHexConverter;
import com.mzth.tangerinepoints.util.CacheUtil;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.CircleImageView;
import com.mzth.tangerinepoints.widget.RiseNumberTextView;

/**
 * Created by Administrator on 2017/4/13.
 */

public class HomeFragment extends BaseFragment {
    private TextView tv_integral,tv_history,tv_user,tv_integral_title;
    private UserBean userbean;
    private CircleImageView civ_head;
    private MaterialRefreshLayout refresh;//刷新
    private ImageView iv_qrCode;
    private Dialog mWeiboDialog;//加载的动画
    private RiseNumberTextView tv_num;
    private RefreshUserBean bean;
    public HomeFragment() {
    }
    @SuppressLint("ValidFragment")
    public HomeFragment(Context context, int resId, UserBean bean ,TextView textView) {
        super(context, resId);
        //等到用户传来的数据
        this.userbean = bean;
        this.tv_integral_title=textView;
    }

    @Override
    protected void initView(View v, Bundle savedInstanceState) {
        //转积分给朋友
        tv_integral= (TextView) v.findViewById(R.id.tv_integral);
        //查看活动历史
        tv_history= (TextView) v.findViewById(R.id.tv_history);
        //用户的头像
        civ_head = (CircleImageView) v.findViewById(R.id.civ_head);
        //用户的昵称
        tv_user = (TextView) v.findViewById(R.id.tv_user);
        //刷新
        refresh = (MaterialRefreshLayout) v.findViewById(R.id.refresh);
        //积分点数
        tv_num = (RiseNumberTextView) v.findViewById(R.id.tv_num);
        //二维码
        iv_qrCode = (ImageView) v.findViewById(R.id.iv_qrCode);
    }

    @Override
    protected void initData() {
//        if(CacheUtil.getValue(_context,SharedName.USERBEAN).toString().equals("")||CacheUtil.getValue(_context,SharedName.USERBEAN)==null){
           RrofileRequest();//初始化请求用户信息
//        }else{
//            RefreshUserBean bean = (RefreshUserBean) CacheUtil.getValue(_context,SharedName.USERBEAN);
//            UserInfo(bean);//如果缓存里有用户信息那么就直接从缓存中获取并设置用户信息
        //}

    }
    //设置用户信息
    private void UserInfo(RefreshUserBean bean){
        //头像
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getPicture()+"/100/100").dontAnimate().placeholder(R.mipmap.ic_launcher).into(civ_head);
        //设置用户昵称
        tv_user.setText(bean.getScreenName());
        //设置积分点数
        if(Integer.valueOf(tv_num.getText().toString())!=bean.getPointBalance()){
            //设置点数
            tv_num.withNumber(bean.getPointBalance());
            tv_num.setDuration(1000);
            // 开始播放动画
            tv_num.start();
        }
        tv_integral_title.setText(bean.getPointBalance()+"");//顶部积分显示
        //得到这个图片的Hex编码的字符串
        String photo=bean.getQrCodePngHex();
        byte[] bit = BinaryHexConverter.hexStringToByteArray(photo);
        Bitmap myImage = BitmapFactory.decodeByteArray(bit, 0, bit.length);
        //设置二维码图片
        iv_qrCode.setImageBitmap(myImage);
    }
    @Override
    protected void BindComponentEvent() {
        tv_integral.setOnClickListener(myonclick);
        tv_history.setOnClickListener(myonclick);
        //设置刷新控件的刷新事件
        refresh.setMaterialRefreshListener(materialRefreshListener);
        //civ_head.setOnClickListener(myonclick);
    }
    //结束刷新
    private void finishRefresh(){
        //结束下拉刷新
        refresh.finishRefresh();
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        //下拉刷新
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //刷新用户信息
            RrofileRequest();
        }
    };
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_integral://转积分给朋友
                    if(hasSimCard()){//判断SIM卡是否存在
                    startActivityForResult(FriendsActivity.class,null,100);
                    }else{
                        ToastUtil.showShort(_context,"No SIM card");
                    }
                    break;
                case R.id.tv_history://查看活动历史
                    startActivity(HistoryActivity.class,null);
                    break;
                case R.id.civ_head://查看大图头像
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putString("image",bean.getPicture());
//                    bundle1.putInt("tag",2);
//                    startActivity(PhotoPreviewActivity.class,bundle1);
                    break;
            }
        }
    };
    //刷新用户信息
    private void RrofileRequest(){
        //加载动画
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.REFRESH_PROFILE, null, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String user = GsonUtil.getJsonFromKey(json,"user");
                bean=GsonUtil.getBeanFromJson(user,RefreshUserBean.class);
                UserInfo(bean);//初始化用户信息
                //将用户的信息保存到本地
                CacheUtil.putValue(_context,SharedName.USERBEAN,bean);
                //将积分点数保存在sp中
                SharedPreferencesUtil.setParam(_context,"PointBalance",bean.getPointBalance());
                //将用户的ID保存在sp中
                SharedPreferencesUtil.setParam(_context,SharedName.CustomerId,bean.getCustomerId());
                //结束刷新
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {

                switch (errorMsg){//错误信息
                    case "INVALID_SCREEN_NAME":
                        ToastUtil.showShort(_context,Constans.INVALID_SCREEN_NAME);
                        break;
                    case "INCORRECT_BIRTHDAY_FORMAT":
                        ToastUtil.showShort(_context,Constans.INVALID_SCREEN_NAME);
                        break;
                    case "INVALID_EMAIL_FORMAT":
                        ToastUtil.showShort(_context,Constans.INVALID_EMAIL_FORMAT);
                        break;
                    case "INVALID_CUSTOMER_ID":
                        ToastUtil.showShort(_context,Constans.INVALID_USER_ID);
                        break;
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
                //结束刷新
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                //结束刷新
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//请求成功关闭动画
            }
        });

    }
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        switch (requestCode){
            case 100://从通讯录页面跳转回来
                RrofileRequest();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
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
}
