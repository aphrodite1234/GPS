package com.will.gps.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.will.gps.R;
import com.will.gps.bean.ReceiverBean;
import com.will.gps.bean.SignTableBean;
import com.will.gps.utils.LocationUtil;

import java.util.List;

/**
 * Created by MaiBenBen on 2019/5/20.
 */

public class ReceiverMapActivity extends Activity implements LocationSource{
    private Intent intent;
    private MapView mapView;
    private SignTableBean signTableBean;
    private List<String> receivers;
    private Gson gson=new Gson();
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private LocationUtil locationUtil;
    private LinearLayout linearLayout;
    private AMap aMap;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        linearLayout=(LinearLayout)findViewById(R.id.map_linearlayout);
        linearLayout.setVisibility(View.GONE);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        if(aMap==null) aMap=mapView.getMap();
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        intent=getIntent();
        locationUtil=new LocationUtil();

        signTableBean=(SignTableBean)intent.getSerializableExtra("signtable");
        aMap.addMarker(locationUtil.getMarkerOption(signTableBean.getId()+"签到地点",Double.valueOf(signTableBean.getLatitude()),Double.valueOf(signTableBean.getLongitude())));

        receivers= (List<String>) intent.getSerializableExtra("receivers");
        if (!receivers.isEmpty()) {
            for (String receiver : receivers) {
                ReceiverBean receiver1 = gson.fromJson(receiver, ReceiverBean.class);
                MarkerOptions markerOptions=locationUtil.getMarkerOption(receiver1.getId()+"("+receiver1.getRealname()+")",Double.valueOf(receiver1.getRlatitude()),Double.valueOf(receiver1.getRlongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.position2));
                aMap.addMarker(markerOptions);
            }
        }
        LatLng latLng=new LatLng(Double.valueOf(signTableBean.getLatitude()),Double.valueOf(signTableBean.getLongitude()));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

        btn_back=(ImageView) findViewById(R.id.map_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
