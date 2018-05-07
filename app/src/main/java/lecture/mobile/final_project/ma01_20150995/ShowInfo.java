package lecture.mobile.final_project.ma01_20150995;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import lecture.mobile.final_project.ma01_20150995.dto.FestDTO;

public class ShowInfo extends AppCompatActivity {

    private Geocoder geocoder;
    private GoogleMap googleMap;
    private MarkerOptions markerOptions;
    private  RoadDTO roadDTO  ;
    private FestDTO festDTO;
    private String intent_place;
    private String intent_title;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        markerOptions = new MarkerOptions();
        geocoder = new Geocoder(this);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(mapReadyCallBack);
        TextView title = (TextView)findViewById(R.id.date_title);
        TextView info = (TextView)findViewById(R.id.info);
        TextView about = (TextView)findViewById(R.id.about);
        TextView date = (TextView)findViewById(R.id.date_time);

        Intent intent = getIntent();

        flag = intent.getBooleanExtra("flag",false);

        if(!flag)
            roadDTO = (RoadDTO)intent.getSerializableExtra("data");
        else
            festDTO = (FestDTO)intent.getSerializableExtra("data");

        if(!flag){
            title.setText(roadDTO.getTitle());
            info.setText("총길이 :"+roadDTO.getTotalLength());
            date.setText("총시간 : "+roadDTO.getTotalTime());
            about.setText(roadDTO.getIntro());
            intent_title = roadDTO.getTitle();
            if(roadDTO.getStartAd().equals("null"))
                intent_place = roadDTO.getStartRoad();
            else
                intent_place = roadDTO.getStartAd();
        }
        else {
            title.setText(festDTO.getTitle());
            info.setText("축제기간 : "+festDTO.getStartDay()+" - "+festDTO.getEndDay());
            if(festDTO.getUrl().equals("null"))
                date.setText("");
            else
                date.setText(festDTO.getUrl());
            about.setText(festDTO.getAboutFes());
            intent_title = festDTO.getTitle();
            if(festDTO.getRoadAdd().equals("null"))
                intent_place = festDTO.getAdd();
            else
                intent_place = festDTO.getRoadAdd();
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng startPlace = null;
            LatLng endPlace= null;

            if(!flag) {
                String add = "";
                if(roadDTO.getStartAd().equals("null"))
                    add = roadDTO.getStartRoad();
                else
                    add = roadDTO.getStartAd();
                Log.e("ShowInfo",add);
                try {
                    List<Address> start = geocoder.getFromLocationName(add, 1);
                    if(start != null && start.size()>0)
                        startPlace = new LatLng(start.get(0).getLatitude(),start.get(0).getLongitude());
                    if(roadDTO.getEndAd().equals("null"))
                        add = roadDTO.getEndRoad();
                    else
                        add = roadDTO.getEndAd();
                    List<Address> end = geocoder.getFromLocationName(add, 1);
                    if(end != null && end.size()>0)
                        endPlace = new LatLng(end.get(0).getLatitude(),end.get(0).getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPlace, 14));
                    markerOptions.position(startPlace);
                    markerOptions.title("시작지점");
                    markerOptions.snippet(roadDTO.getStart());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.flag1));
                    Marker startMarker = googleMap.addMarker(markerOptions);

                    markerOptions.position(endPlace);
                    markerOptions.title("종료지점");
                    markerOptions.snippet(roadDTO.getEnd());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.flag2));
                    Marker endMarker = googleMap.addMarker(markerOptions);

                    startMarker.showInfoWindow();
                    endMarker.showInfoWindow();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                String add = "";
                if(festDTO.getRoadAdd().equals("null"))
                    add = festDTO.getAdd();
                else
                    add = festDTO.getRoadAdd();
                try {
                    List<Address> addre = geocoder.getFromLocationName(add, 1);
                    if(addre != null && addre.size()>0)
                        startPlace = new LatLng(addre.get(0).getLatitude(),addre.get(0).getLongitude());
                    else
                        startPlace = new LatLng(37.495647,126.772745);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPlace, 14));
                    markerOptions.position(startPlace);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker));
                    Marker startMarker = googleMap.addMarker(markerOptions);

                    startMarker.showInfoWindow();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void onClick(View view){
        switch(view.getId()){
            case R.id.save_list:
                Intent intent = new Intent(this,AddDateList.class);
                intent.putExtra("title",intent_title);
                intent.putExtra("place",intent_place);
                startActivity(intent);
                break;
            case R.id.show_info_cancel:
                finish();

        }

    }
}
