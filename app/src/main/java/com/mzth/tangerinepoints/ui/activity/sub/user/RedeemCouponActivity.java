package com.mzth.tangerinepoints.ui.activity.sub.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.CouponBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.BinaryHexConverter;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RedeemCouponActivity extends BaseBussActivity {
    private ImageView iv_back,iv_redeem_coupon,iv_qrCode_coupon;
    private TextView tv_title;
    private CouponBean bean;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=RedeemCouponActivity.this;
        setContentView(R.layout.activity_redeem_coupon);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        //优惠券图片
        iv_redeem_coupon = (ImageView) findViewById(R.id.iv_redeem_coupon);
        //二维码
        iv_qrCode_coupon = (ImageView) findViewById(R.id.iv_qrCode_coupon);
    }

    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Redeem Coupon");
        bean = (CouponBean) getIntent().getSerializableExtra("bean");
        //优惠券图片
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getCouponImgKey()+"/300/172").placeholder(R.mipmap.ic_launcher).into(iv_redeem_coupon);
        //二维码
        byte[] bit = BinaryHexConverter.hexStringToByteArray(bean.getQrCodePngHex());
        Bitmap myImage = BitmapFactory.decodeByteArray(bit, 0, bit.length);
        //设置二维码图片
        iv_qrCode_coupon.setImageBitmap(myImage);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
