package com.will.gps.map;

/**
 * Created by MaiBenBen on 2019/5/11.
 */
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.will.gps.CreateSignActivity;
import com.will.gps.R;
import com.will.gps.base.PermissionActivity;
import com.will.gps.utils.LocationUtil;

public class AmapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private LocationUtil locationUtil;
    private double latitude,longitude;
    private GeocodeSearch geocodeSearch;
    private String simpleAddress;
    private TextView tvChoseAddress;
    private Button btn_dingwei;
    private ImageView btn_back;
    private Intent intent;
    private boolean highaccuracy=false;
    private final static int GPS_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        intent=getIntent();
        if(intent.getStringExtra("jingdu").equals("high")) {
            openGPSSettings();
            highaccuracy=true;
            startActivityForResult(new Intent(this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
        } else{
            highaccuracy=false;
            startActivityForResult(new Intent(this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
        }
        init();
    }

    private void init() {
        if(aMap == null){
            aMap = mapView.getMap();
        }
        //aMap.setOnMapLongClickListener(this);
        setLocationCallBack();
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//地图的定位标志是否可见
        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
        //地理搜索类
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        aMap.setOnMapClickListener(this);

        tvChoseAddress=(TextView)findViewById(R.id.map_latLng);
        btn_back=(ImageView) findViewById(R.id.map_back);
        btn_dingwei=(Button)findViewById(R.id.map_btn);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AmapActivity.this, CreateSignActivity.class);
                i.putExtra("lat",latitude);
                i.putExtra("lgt",longitude);
                //startActivity(i);
                AmapActivity.this.setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen(){
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }
    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (!checkGPSIsOpen()) {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })

                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();
        } /*else {
            *//*initLocation(); //自己写的定位方法*//*
        }*/
    }
    private void setLocationCallBack(){
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str,double lat,double lgt,AMapLocation aMapLocation) {

                //根据获取的经纬度，将地图移动到定位位置
                //aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lgt)));
                latitude=lat; longitude=lgt;
                mListener.onLocationChanged(aMapLocation);
                //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption(str,lat,lgt));
            }
        });
    }

    //定位激活回调(点击定位按钮)
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        locationUtil.startLocate(this,highaccuracy);
        tvChoseAddress.setText(String.format("纬度:%s   经度:%s", latitude, longitude));
    }

    /**     * map点击事件     * @param latLng 经纬度     */
    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        latitude=latLng.latitude;
        longitude=latLng.longitude;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.position2));
        otMarkerOptions.position(latLng);
        otMarkerOptions.title(simpleAddress);
        otMarkerOptions.snippet("纬度:" + latLng.latitude + "   经度:" + latLng.longitude);
        getAddressByLatlng(latLng);
        aMap.addMarker(otMarkerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        tvChoseAddress.setText(String.format("纬度:%s   经度:%s", latLng.latitude, latLng.longitude));
    }
    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }
    /**     *  得到逆地理编码异步查询结果     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        simpleAddress = formatAddress.substring(9);
        //tvChoseAddress.setText("查询经纬度对应详细地址：\n" + simpleAddress);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE){
            switch (resultCode){
                case PermissionActivity.CALL_BACK_RESULT_CODE_SUCCESS:
                    Toast.makeText(this,"权限申请成功！",Toast.LENGTH_SHORT).show();
                    break;
                case PermissionActivity.CALL_BACK_RESULE_CODE_FAILURE:
                    Toast.makeText(this,"权限申请失败！",Toast.LENGTH_SHORT).show();
                    break;
                case GPS_REQUEST_CODE:
                    Toast.makeText(this,"GPS打开成功！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this,"GPS打开失败!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
