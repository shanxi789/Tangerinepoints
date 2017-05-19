package com.mzth.tangerinepoints.ui.activity.sub.git;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.NearbyBusinBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.BinaryHexConverter;
import com.mzth.tangerinepoints.widget.CircleImageView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RedeemOfferActivity extends BaseBussActivity {
    private ImageView iv_back,iv_shopp_qrCode;
    private TextView tv_title,tv_shop_title,tv_shop_pts,tv_shop_infos,
    tv_coffee;
    private OffersBean bean;
    private CircleImageView cl_shop;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = RedeemOfferActivity.this;
        setContentView(R.layout.activity_redeem_offer);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        //商品图片
        cl_shop = (CircleImageView) findViewById(R.id.civ_head);
        //商品名称
        tv_shop_title  = (TextView) findViewById(R.id.tv_shop_title);
        //pts点数
        tv_shop_pts = (TextView) findViewById(R.id.tv_shop_pts);
        //商人
        tv_coffee = (TextView) findViewById(R.id.tv_coffee);
        //地址城市
        tv_shop_infos = (TextView) findViewById(R.id.tv_shop_infos);
        //二维码
        iv_shopp_qrCode = (ImageView) findViewById(R.id.iv_shopp_qrCode);
    }
    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Redeem Offer");
        bean = (OffersBean) getIntent().getSerializableExtra("bean");
        //商品图片
        Glide.with(_context).load(Constans.PHOTO_PATH+bean.getItemPicture()+"/200/200").dontAnimate().placeholder(R.mipmap.ic_launcher).into(cl_shop);
        //商品详情
        tv_shop_title.setText(bean.getItemDescription());
        //pts点数
        tv_shop_pts.setText(bean.getPoints()+"Pts");

        NearbyBusinBean businBean= bean.getNearbyBusiness();
        //名称
        tv_coffee.setText(businBean.getBusinessName());
        //城市地址
        tv_shop_infos.setText(businBean.getFullAddress());
        //二维码
        byte[] bit = BinaryHexConverter.hexStringToByteArray(bean.getQrCodePngHex());
        Bitmap myImage = BitmapFactory.decodeByteArray(bit, 0, bit.length);
        //设置二维码图片
        iv_shopp_qrCode.setImageBitmap(myImage);
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myclick);
        tv_coffee.setOnClickListener(myclick);
        cl_shop.setOnClickListener(myclick);//查看大图
    }

    private View.OnClickListener myclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.civ_head://查看大图
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image",bean.getItemPicture());
                    bundle1.putInt("tag",2);
                    startActivity(PhotoPreviewActivity.class,bundle1);
                    break;
                case R.id.tv_coffee://商人
                    NearbyBusinBean  BusinBean  = bean.getNearbyBusiness();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BusinBean",BusinBean);
                    startActivity(MerchantActivity.class,bundle);
                    finish();
                    break;
            }
        }
    };


}
