package com.mzth.tangerinepoints.ui.activity.sub.home;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.HistoryBean;
import com.mzth.tangerinepoints.bean.OffersBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.ui.activity.sub.LoginActivity;
import com.mzth.tangerinepoints.ui.adapter.sub.GiftNearbyAdapter;
import com.mzth.tangerinepoints.ui.adapter.sub.HistoryAdapter;
import com.mzth.tangerinepoints.util.GsonUtil;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 */

public class HistoryActivity extends BaseBussActivity {
    private ImageView iv_back;
    private TextView tv_title,tv_sum_earned;
    private NestedScrollView scroll_history;
    private MyListView lv_history;
    private MaterialRefreshLayout refresh;
    private HistoryAdapter adapter ;
    private Dialog mWeiboDialog;//加载的动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = HistoryActivity.this;
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        scroll_history = (NestedScrollView) findViewById(R.id.scroll_history);
        lv_history = (MyListView) findViewById(R.id.lv_history);
        //赢得总积分
        tv_sum_earned = (TextView) findViewById(R.id.tv_sum_earned);
        //刷新控件
        refresh = (MaterialRefreshLayout) findViewById(R.id.refresh);
        //置顶scroll_history
        scroll_history.setFocusable(true);
        lv_history.setFocusable(false);
    }

    @Override
    protected void initData() {
        super.initData();
        //设置标题
        tv_title.setText("Activity History");
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
        HistoryRequest();
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myclick);
        //设置刷新控件的刷新事件
        refresh.setMaterialRefreshListener(materialRefreshListener);
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        //下拉刷新
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //刷新
            index = 0;
            HistoryRequest();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            index += n;
            HistoryRequest();

        }
    };
    //结束刷新
    private void finishRefresh(){
        //结束下拉刷新
        refresh.finishRefresh();
        //结束上拉加载
        refresh.finishRefreshLoadMore();
    }
    //加载
    private void setDataToView(List<HistoryBean> list){
        if(index==0){
            if(!StringUtil.isEmpty(list)){
                if(adapter==null){
                    adapter = new HistoryAdapter(_context,list,R.layout.history_item);
                    lv_history.setAdapter(adapter);
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
    //用户交易活动历史
    private void HistoryRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("n",n);
        map.put("start_index",index);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.TRANSACTIONS, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String history=GsonUtil.getJsonFromKey(json,"history");
                String totalPointsEarned  = GsonUtil.getJsonFromKey(history,"totalPointsEarned");
                tv_sum_earned.setText(totalPointsEarned);
                String transactions = GsonUtil.getJsonFromKey(history,"transactions");
                List<HistoryBean> list = GsonUtil.getListFromJson(transactions,new TypeToken<List<HistoryBean>>(){});
                //设置适配器
                setDataToView(list);
                finishRefresh();
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
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                finishRefresh();
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }
        });
    }


}
