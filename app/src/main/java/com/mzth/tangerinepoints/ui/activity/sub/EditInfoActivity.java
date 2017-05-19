package com.mzth.tangerinepoints.ui.activity.sub;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.RefreshUserBean;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;
import com.mzth.tangerinepoints.util.Base64Util;
import com.mzth.tangerinepoints.util.BinaryHexConverter;
import com.mzth.tangerinepoints.util.CustomUtil;
import com.mzth.tangerinepoints.util.FileStorage;
import com.mzth.tangerinepoints.util.NetUtil;
import com.mzth.tangerinepoints.util.SharedPreferencesUtil;
import com.mzth.tangerinepoints.util.StringUtil;
import com.mzth.tangerinepoints.util.ToastUtil;
import com.mzth.tangerinepoints.util.WeiboDialogUtils;
import com.mzth.tangerinepoints.widget.email.EmailValidator;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/15.
 * 仿QQ的点击头像对话框
 */

public class EditInfoActivity extends BaseBussActivity {
    private TextView textView,tv_delete_birthdy,tv_delete_email,tv_edit_done;//标题
    private ImageView iv_back,civ_head,iv_edit_notify;//返回键
    private String imagepath,cachPath,HeadUrl;
    private Uri imageUri;
    private File cacheFile;
    private EditText edit_user_name,edit_user_email,edit_user_birthdy;
    private boolean notify;
    private RefreshUserBean refreshUserBean;
    private Dialog mWeiboDialog;//加载的动画
    List<File> list = new ArrayList<File>();
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context=EditInfoActivity.this;
        setContentView(R.layout.activity_popup_edit_profile);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        textView= (TextView) findViewById(R.id.tv_title);
        //返回键
        iv_back= (ImageView) findViewById(R.id.iv_back);
        //头像
        civ_head = (ImageView) findViewById(R.id.civ_head);
        //邮箱删除×
        tv_delete_email = (TextView) findViewById(R.id.tv_delete_email);
        //生日删除×
        tv_delete_birthdy = (TextView) findViewById(R.id.tv_delete_birthdy);
        //名称
        edit_user_name = (EditText) findViewById(R.id.edit_user_name);
        //邮箱
        edit_user_email = (EditText) findViewById(R.id.edit_user_email);
        //生日
        edit_user_birthdy = (EditText) findViewById(R.id.edit_user_birthdy);
        //开关通知
        iv_edit_notify = (ImageView) findViewById(R.id.iv_edit_notify);
        //提交
        tv_edit_done = (TextView) findViewById(R.id.tv_edit_done);
    }
    @Override
    protected void initData() {
        super.initData();
        textView.setText("Edit Profile");
        //初始化用户信息
        refreshUserBean = (RefreshUserBean) getIntent().getSerializableExtra("UserBean");
        //头像
        Glide.with(_context).load(Constans.PHOTO_PATH+refreshUserBean.getPicture()+"/100/100").dontAnimate().placeholder(R.mipmap.ic_launcher).into(civ_head);
        edit_user_name.setText(refreshUserBean.getScreenName());//昵称
        edit_user_email.setText(refreshUserBean.getEmail());//邮箱
        String Birthday = refreshUserBean.getBirthday();//生日
        if(!StringUtil.isEmpty(Birthday)){//判断生日是否为空
        String bir = "";
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sim.parse(Birthday);
            sim = new SimpleDateFormat("MM/dd/yyyy");
            bir = sim.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            edit_user_birthdy.setText(bir);//yyyy-MM-dd的生日格式转换成MM/dd/yyyy
        }else{
            edit_user_birthdy.setText("");
        }
        if(refreshUserBean.isNotificationSwitch()){//通知开关
            iv_edit_notify.setImageResource(R.mipmap.btn);
        }else{
            iv_edit_notify.setImageResource(R.mipmap.btn_off);
        }
        notify = true;//推送开关默认为true
        if(!StringUtil.isEmpty(edit_user_birthdy.getText().toString())){
            tv_delete_birthdy.setVisibility(View.VISIBLE);
        }
        if(!StringUtil.isEmpty(edit_user_email.getText().toString())){
            tv_delete_email.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclcik);
        civ_head.setOnClickListener(myonclcik);
        iv_edit_notify.setOnClickListener(myonclcik);
        tv_delete_birthdy.setOnClickListener(myonclcik);
        tv_delete_email.setOnClickListener(myonclcik);
        tv_edit_done.setOnClickListener(myonclcik);
        edit_user_birthdy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!StringUtil.isEmpty(edit_user_birthdy.getText().toString())){
                    tv_delete_birthdy.setVisibility(View.VISIBLE);
                }
            }
        });
        edit_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!StringUtil.isEmpty(edit_user_email.getText().toString())){
                    tv_delete_email.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private View.OnClickListener myonclcik = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back://返回键监听
                    onBackPressed();
                    break;
                case R.id.civ_head://点击头像
                    if(CustomUtil.sdCardExist()){//判断sd卡是否存在
                        //showHeadDialog();
                        showDialog();
                        cachPath = Constans.PATH_HEAD + System.currentTimeMillis()+ ".jpg";
                        cacheFile = new File(cachPath);
                    }else {
                        ToastUtil.showShort(_context, "SD card is not detected, can not operate Oh ~");
                    }
                    break;
                case R.id.iv_edit_notify://开关通知
                    if(notify){
                        notify=false;
                        iv_edit_notify.setImageResource(R.mipmap.btn_off);
                    }else{
                        notify=true;
                        iv_edit_notify.setImageResource(R.mipmap.btn);
                    }
                    break;
                case R.id.tv_delete_email://邮箱的删除×
                    edit_user_email.setText("");
                    tv_delete_email.setVisibility(View.GONE);
                    break;
                case R.id.tv_delete_birthdy://生日的删除×
                    edit_user_birthdy.setText("");
                    tv_delete_birthdy.setVisibility(View.GONE);
                    break;
                case R.id.tv_edit_done://提交
                    String username = edit_user_name.getText().toString();//昵称
                    String userbirthdy = edit_user_birthdy.getText().toString();//生日
                    String useremail = edit_user_email.getText().toString();//邮箱
                    if(username.trim().length()>20){//验证昵称长度是否大于20
                        ToastUtil.showShort(_context,"The nickname you entered is too long");
                        return;
                    }
                    if(!username.matches("[A-Za-z0-9\\\\_]+")){//验证昵称
                        ToastUtil.showShort(_context,"The name you entered is not valid");
                        return;
                    }
                    EmailValidator emailValidator = new EmailValidator(true);//验证邮箱的工具类
                    if(!emailValidator.isValid(useremail)){//验证邮箱
                        ToastUtil.showShort(_context,"The mailbox you entered is illegal");
                        return;
                    }
                    if(!userbirthdy.matches("(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)[0-9]{2}")){//验证生日
                        ToastUtil.showShort(_context,"Your input is not in the correct format");
                        return;
                    }
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
                    UpdateInfoRequest();
                    break;
            }
        }
    };


    //上传头像的请求
    private void HeadRequest(String image){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pictureHex",image);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.PROFILE_PICTURE, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Avatar Success");
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                switch (errorMsg){//错误信息
                    case "CANNOT_UPLOAD_EMPTY_FILE":
                        ToastUtil.showShort(_context,Constans.CANNOT_UPLOAD_EMPTY_FILE);
                        break;
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
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


    //更新用户信息的请求
    private void UpdateInfoRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        String name = edit_user_name.getText().toString();
        if(!StringUtil.isEmpty(name)){
            map.put("screen_name",name);
        }else{
            String phone=(String) SharedPreferencesUtil.getParam(_context,"phone","");
            map.put("screen_name",phone.substring(3,phone.length()));
        }
        map.put("notification_swith",notify);
        map.put("birthday",edit_user_birthdy.getText().toString());
        map.put("email",edit_user_email.getText().toString());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.PROFILE, map, Authorization, Constans.APP_INSTANCE_ID, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,"Modify Success");
                WeiboDialogUtils.closeDialog(mWeiboDialog);//关闭动画
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
                    default:
                        ToastUtil.showShort(_context,errorMsg);
                        break;
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
    //仿QQ点击头像弹出的对话框
    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //图库
        Button imgcatch = (Button) view.findViewById(R.id.btn__userhead_catch);
        //拍照
        Button imgPhoto = (Button) view.findViewById(R.id.btn_userhead_photo);
        //取消
        Button cancle = (Button) view.findViewById(R.id.btn_cancle);
        //拍照的点击事件
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGetPermission = AndPermission.hasPermission(_context, Manifest.permission.CAMERA);
                if (!isGetPermission) {
                    AndPermission.defaultSettingDialog(_context, 1).show();
                }else{
                    openCamera();
                }
                dialog.dismiss();
            }
        });
        //相册的点击事件
        imgcatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到本地相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        //取消的点击事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
//    //弹出更换头像的对话框
//    private void showHeadDialog() {
//        final Dialog dialog = new Dialog(_context, R.style.custom_window_dialog);
//        //填充对话框布局
//        LinearLayout layout = (LinearLayout) ((LayoutInflater) _context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//                R.layout.dialog_choose_userhead, null);
//        ImageView imgcatch = (ImageView) layout
//                .findViewById(R.id.choose_userhead_catch);
//        ImageView imgPhoto = (ImageView) layout
//                .findViewById(R.id.choose_userhead_photo);
//        //拍照的点击事件
//        imgcatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean isGetPermission = AndPermission.hasPermission(_context, Manifest.permission.CAMERA);
//                if (!isGetPermission) {
//                    AndPermission.defaultSettingDialog(_context, 1).show();
//                }else{
//                    openCamera();
//                }
//                dialog.dismiss();
//            }
//        });
//        //相册的点击事件
//        imgPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //跳转到本地相册
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 2);
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//        WindowManager windowManager = ((Activity) _context).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth() * 0.8); // 设置宽度
//        lp.height = (int) (display.getHeight() * 0.4); // 设置宽度
//        dialog.getWindow().setAttributes(lp);
//    }
    //拍照
    private void openCamera(){
        Intent intent = new Intent();
        File file = new FileStorage().createIconFile();
        imagepath = Constans.PATH_HEAD + System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= 24) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri =  FileProvider.getUriForFile(_context, "com.sd.parentsofnetwork.fileprovider", new File(imagepath));//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(new File(imagepath));
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, 1);//启动拍照
    };
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        super.doActivityResult(requestCode, intent);
        switch (requestCode){
            case 1 ://拍照
                startPhotoZoom(new File(imagepath));
                break;
            case 2 ://本地相册返回
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(intent);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(intent);
                }
                break;
            case 3 ://裁剪
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(_context, "Loading...");
                if(new File(cachPath).exists()){
                    HeadUrl = cachPath;
                    Glide.with(_context).load(HeadUrl).crossFade().centerCrop().into(civ_head);
                    //上传头像  将图片转换成byte数组  然后通过hex加密上传到服务器
                    byte[] imagehead = Base64Util.getBytes(HeadUrl);
                    HeadRequest(BinaryHexConverter.bytesToHex(imagehead));
                }
                break;
        }
    }
    /**
     * 相册图片回调
     * android 4.4之后调用
     * @param data
     * @return
     */
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath= uriToPath(uri);
        startPhotoZoom(new File(imagePath));
    }
    /**
     * 相册图片回调
     * android 4.4之前调用
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        startPhotoZoom(new File(imagePath));
    }
    /**
     * 适配 android 7.0 的裁剪图片方法实现
     * @param file
     */
    public void startPhotoZoom(File file) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= 24){
                intent.setDataAndType(getImageContentUri(this,file), "image/*");//自己使用Content Uri替换File Uri
            }else{
                intent.setDataAndType(Uri.fromFile(file), "image/*");//自己使用Content Uri替换File Uri
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 150);
            intent.putExtra("outputY", 150);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 3);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取符合 android 7.0 的 Content Uri
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    /**
     * 将Uri转换成Path
     * android 4.4之后调用
     * @param uri
     * @return
     */
    private String uriToPath(Uri uri) {
        String path=null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return  path;
    }
    /**
     * 将Uri转换成Path
     * android 4.4之前调用
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
