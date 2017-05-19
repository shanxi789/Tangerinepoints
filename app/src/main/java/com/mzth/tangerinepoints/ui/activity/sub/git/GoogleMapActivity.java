package com.mzth.tangerinepoints.ui.activity.sub.git;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.bean.LocationBean;
import com.mzth.tangerinepoints.ui.activity.base.BaseBussActivity;

/**
 * Created by Administrator on 2017/4/22.
 */

public class GoogleMapActivity extends BaseBussActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMarkerDragListener,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
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

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    /**
     * 用来判断用户在连接上Google Play services之前是否有请求地址的操作
     */
    private boolean mAddressRequested;
    /**
     * 地图上锚点
     */
    private Marker perth ;
    private LatLng lastLatLng,perthLatLng;
    private LocationBean locationBean;//定位对象
    private ImageView iv_back;//返回键
    private TextView tv_title;//标题
    private LinearLayout ll_map_shopp;//地图 标题
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = GoogleMapActivity.this;
        setContentView(R.layout.activity_google_map);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_title= (TextView) findViewById(R.id.tv_title);
        ll_map_shopp = (LinearLayout) findViewById(R.id.ll_map_shopp);
    }
    @Override
    protected void initData() {
        super.initData();
        ll_map_shopp.setVisibility(View.VISIBLE);
        //设置标题
        tv_title.setText("Google Map");
        locationBean = (LocationBean) getIntent().getSerializableExtra("bean");
        //接收FetchAddressIntentService返回的结果
        mResultReceiver = new AddressResultReceiver(new Handler());
        //创建GoogleAPIClient实例
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //取得SupportMapFragment,并在地图准备好后调用onMapReady
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerDragListener(this);
        enableMyLocation();
    }
    /**
     * 如果取得了权限,显示地图定位层
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    /**
     * '我的位置'按钮点击时的调用
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, latLng);
        startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            Toast.makeText(getApplicationContext(),"Permission to access the location is missing.",Toast.LENGTH_LONG).show();
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
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("MapsActivity", "--onConnected--" );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Permission to access the location is missing.",Toast.LENGTH_LONG).show();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            lastLatLng = new LatLng(locationBean.getLatitude(), locationBean.getLongitude());
            displayPerth(true,lastLatLng);
            initCamera(lastLatLng);
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available",Toast.LENGTH_LONG).show();
                return;
            }
            if (mAddressRequested) {
                startIntentService(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
            }
        }
    }
    /**
     * 添加标记
     */
    private void displayPerth(boolean isDraggable,LatLng latLng) {
        if (perth==null){
            //perth = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Position"));

            MarkerOptions options = new MarkerOptions();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource( R.mipmap.map_icon01 );
            options.icon( icon );
            options.position(latLng);
            perth = mMap.addMarker( options );

            perth.setDraggable(isDraggable); //设置可移动
        }

    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
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
//                try {
//                    Thread.sleep(800);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                runOnUiThread(new Runnable() {
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


    public static class AddressResultReceiver extends ResultReceiver {
        private String mAddressOutput;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                Log.i("MapsActivity", "mAddressOutput-->" + mAddressOutput);
//                new AlertDialog.Builder(GoogleMapActivity.this)
//                        .setTitle("Position")
//                        .setMessage(mAddressOutput)
//                        .create()
//                        .show();
            }

        }
    }
}
