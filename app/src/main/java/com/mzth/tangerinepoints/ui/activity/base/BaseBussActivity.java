package com.mzth.tangerinepoints.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by leeandy007 on 2017/3/11.
 */

public class BaseBussActivity extends BaseActivity {
    /**
     * @Desc 初始化布局
     * */
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {

    }

    /**
     * @Desc 初始化控件
     * */
    @Override
    protected void initView() {

    }

    /**
     * @Desc 绑定控件事件
     * */
    @Override
    protected void BindComponentEvent() {

    }

    /**
     * @Desc 初始化数据
     * */
    @Override
    protected void initData() {

    }

    /**
     * @Desc 带返回值跳转的数据的处理方法
     * @param requestCode 请求码
     * @param intent 取值载体
     * */
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {

    }




    /**
     * @Desc 返回键操作
     */
    @Override
    public void onBackPressed() {
        application.removeActivity(this);
        this.finish();
    }

    /**
     * @Desc 正常页面跳转
     * @param clszz 目标页面
     * @param bundle 传值载体
     * */
    public void startActivity(Class clszz, Bundle bundle){
        Intent intent = new Intent(_context, clszz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        _context.startActivity(intent);
    }

    /**
     * @Desc 带返回值跳转
     * @param clszz 目标页面
     * @param bundle 传值
     * @param requestCode 请求码
     * */
    public void startActivityForResult(Class clszz, Bundle bundle, int requestCode){
        Intent intent = new Intent(_context, clszz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        _context.startActivityForResult(intent, requestCode);
    }





}
